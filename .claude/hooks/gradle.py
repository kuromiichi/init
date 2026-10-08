#!/usr/bin/env python3
"""Hooks de Gradle para Claude Code.

PreToolUse: bloquea una invocación de Gradle si ya hay otra en curso
(salvo `./gradlew --stop`).
PostToolUse: sustituye la salida larga de Gradle por un resumen (estado,
errores de compilación, fallos de pruebas con su mensaje, infracciones de
Ktlint) y guarda la salida completa en `.claude/logs/`.
"""

import json
import os
import re
import subprocess
import sys
import time
import xml.etree.ElementTree as ElementTree
from pathlib import Path

GRADLE_COMMAND = re.compile(r"(^|[\s;&|(])(\./)?gradlew\b")
STOP_ONLY = re.compile(r"^\s*(\./)?gradlew\s+--stop\b")
MIN_LENGTH_TO_SUMMARIZE = 600
MAX_LINES_PER_SECTION = 25
KEPT_LOGS = 10

PROJECT_DIR = Path(os.environ.get("CLAUDE_PROJECT_DIR", os.getcwd()))


def main() -> None:
    payload = json.load(sys.stdin)
    command = payload.get("tool_input", {}).get("command", "")
    if not GRADLE_COMMAND.search(command):
        return
    event = payload.get("hook_event_name")
    if event == "PreToolUse":
        guard_parallel_gradle(command)
    elif event == "PostToolUse":
        summarize(payload)


def guard_parallel_gradle(command: str) -> None:
    if STOP_ONLY.match(command):
        return
    result = subprocess.run(
        ["pgrep", "-fl", "org.gradle.wrapper.GradleWrapperMain"],
        capture_output=True,
        text=True,
    )
    running = result.stdout.strip()
    if not running:
        return
    print(
        "Ya hay una invocación de Gradle en curso; no lances otra en paralelo.\n"
        f"{running[:300]}\n"
        "Espera a que termine (o cierra la app si es `run`). Si está colgada: "
        "`./gradlew --stop` y repite con `clean`.",
        file=sys.stderr,
    )
    sys.exit(2)


def summarize(payload: dict) -> None:
    response = payload.get("tool_response")
    if not isinstance(response, dict) or response.get("interrupted"):
        return
    stdout = response.get("stdout") or ""
    stderr = response.get("stderr") or ""
    output = stdout + ("\n" + stderr if stderr else "")
    finished = re.search(r"^BUILD (SUCCESSFUL|FAILED) in (.+)$", output, re.M)
    if len(output) < MIN_LENGTH_TO_SUMMARIZE or not (finished or "FAILURE:" in output):
        return

    log_path = save_log(output)
    output = output.replace(str(PROJECT_DIR) + "/", "").replace("file://", "")
    lines = output.splitlines()
    status = finished.group(0) if finished else "BUILD FAILED"
    summary = [f"[resumen del hook de Gradle] {status}"]

    tests = None
    if re.search(r"^> Task :test( FAILED)?$", output, re.M):
        tests = test_results(seconds_ago(finished.group(2) if finished else "60s"))
    if tests:
        total, failures = tests
        summary.append(f"Pruebas ejecutadas: {total}, fallidas: {len(failures)}")
    else:
        failures = []
        if re.search(r"^> Task :test (UP-TO-DATE|FROM-CACHE)$", output, re.M):
            summary.append("Pruebas: al día, no se han vuelto a ejecutar")

    compiler_errors = [line for line in lines if line.startswith("e: ")]
    warnings = [line for line in lines if line.startswith("w: ")]
    ktlint = [line for line in lines if re.match(r"^\S+\.kts?:\d+:\d+ ", line)]
    failed_tasks = re.findall(r"^> Task (\S+) FAILED", output, re.M)
    failed_tests = [line.strip() for line in lines if re.search(r" > .+ FAILED$", line)]

    section(summary, "Tareas fallidas", failed_tasks)
    section(summary, "Errores de compilación", compiler_errors)
    section(summary, "Ktlint", ktlint)
    if failures:
        section(summary, "Pruebas fallidas", failures)
    else:
        section(summary, "Pruebas fallidas", failed_tests)
    if warnings:
        summary.append(f"Avisos del compilador: {len(warnings)} (ver log)")
    if status.startswith("BUILD FAILED") and not (
        failed_tasks or compiler_errors or ktlint or failed_tests
    ):
        section(summary, "Qué ha fallado", what_went_wrong(output))
    summary.append(f"Salida completa: {log_path.relative_to(PROJECT_DIR)}")

    print(
        json.dumps(
            {
                "hookSpecificOutput": {
                    "hookEventName": "PostToolUse",
                    "updatedToolOutput": {
                        **response,
                        "stdout": "\n".join(summary),
                        "stderr": "",
                    },
                }
            }
        )
    )


def section(summary: list, title: str, items: list) -> None:
    if not items:
        return
    summary.append(f"{title}:")
    summary.extend(f"  {item}" for item in items[:MAX_LINES_PER_SECTION])
    if len(items) > MAX_LINES_PER_SECTION:
        summary.append(f"  … y {len(items) - MAX_LINES_PER_SECTION} más (ver log)")


def what_went_wrong(output: str) -> list:
    blocks = re.findall(r"\* What went wrong:\n(.*?)(?:\n\* Try:|\Z)", output, re.S)
    return [line for block in blocks for line in block.strip().splitlines()[:6]]


def seconds_ago(duration: str) -> float:
    units = {"h": 3600, "m": 60, "s": 1, "ms": 0.001}
    seconds = sum(
        float(value) * units[unit]
        for value, unit in re.findall(r"(\d+(?:\.\d+)?)(ms|h|m|s)", duration)
    )
    return time.time() - seconds - 10


def test_results(since: float):
    results_dir = PROJECT_DIR / "build" / "test-results"
    if not results_dir.is_dir():
        return None
    total = 0
    failures = []
    for report in results_dir.glob("*/TEST-*.xml"):
        if report.stat().st_mtime < since:
            continue
        try:
            root = ElementTree.parse(report).getroot()
        except ElementTree.ParseError:
            continue
        for case in root.iter("testcase"):
            total += 1
            problem = case.find("failure")
            if problem is None:
                problem = case.find("error")
            if problem is not None:
                message = (problem.get("message") or problem.get("type") or "").strip()
                message = message.splitlines()[0][:300] if message else ""
                test_class = case.get("classname", "").rsplit(".", 1)[-1]
                failures.append(f"{test_class} > {case.get('name')}: {message}")
    return (total, failures) if total else None


def save_log(output: str) -> Path:
    logs_dir = PROJECT_DIR / ".claude" / "logs"
    logs_dir.mkdir(parents=True, exist_ok=True)
    log_path = logs_dir / time.strftime("gradle-%Y%m%d-%H%M%S.log")
    log_path.write_text(output)
    for old in sorted(logs_dir.glob("gradle-*.log"))[:-KEPT_LOGS]:
        old.unlink()
    return log_path


if __name__ == "__main__":
    main()
