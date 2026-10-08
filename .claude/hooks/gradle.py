#!/usr/bin/env python3
"""Hook y ejecutor de Gradle para Claude Code.

PreToolUse (Bash): bloquea una invocación de Gradle si ya hay otra en curso (salvo
`./gradlew --stop`) y reescribe cada `./gradlew` del comando para que pase por
`gradle.py run`.

`gradle.py run ./gradlew <args>`: ejecuta Gradle, guarda la salida completa en
`.claude/logs/` e imprime solo un resumen (estado, errores de compilación,
fallos de pruebas con su mensaje, infracciones de Ktlint). Conserva el código de
salida, así que el resumen llega igual si la build pasa o falla.
"""

import json
import os
import re
import shlex
import signal
import subprocess
import sys
import time
import xml.etree.ElementTree as ElementTree
from pathlib import Path

SCRIPT = Path(__file__).resolve()
PROJECT_DIR = SCRIPT.parents[2]
GRADLE_COMMAND = re.compile(r"(^|[\s;&|(])(\./)?gradlew\b")
GRADLE_INVOCATION = re.compile(r"(^|&&|\|\||;|\()(\s*)(\./gradlew\b)")
STOP_ONLY = re.compile(r"^\s*(\./)?gradlew\s+--stop\b")
MAX_LINES_PER_SECTION = 25
KEPT_LOGS = 10


def main() -> None:
    if len(sys.argv) > 2 and sys.argv[1] == "run":
        sys.exit(run(sys.argv[2:]))
    payload = json.load(sys.stdin)
    if payload.get("hook_event_name") != "PreToolUse":
        return
    tool_input = payload.get("tool_input", {})
    command = tool_input.get("command", "")
    if not GRADLE_COMMAND.search(command) or str(SCRIPT) in command:
        return
    guard_parallel_gradle(command)
    if not only_real_invocations(command):
        return
    wrapped = GRADLE_INVOCATION.sub(
        lambda match: f"{match.group(1)}{match.group(2)}python3 {shlex.quote(str(SCRIPT))} run {match.group(3)}",
        command,
    )
    if wrapped == command:
        return
    print(
        json.dumps(
            {
                "hookSpecificOutput": {
                    "hookEventName": "PreToolUse",
                    "permissionDecision": "allow",
                    "permissionDecisionReason": "Gradle pasa por el ejecutor que resume la salida",
                    "updatedInput": {**tool_input, "command": wrapped},
                }
            }
        )
    )


def only_real_invocations(command: str) -> bool:
    """Si cada `./gradlew` que encontraría la expresión regular es una invocación real y no texto entre comillas."""
    if "<<" in command:
        return False
    try:
        lexer = shlex.shlex(command, posix=True, punctuation_chars=True)
        lexer.whitespace_split = True
        tokens = list(lexer)
    except ValueError:
        return False
    starts_command = True
    invocations = 0
    for token in tokens:
        if token == "./gradlew" and starts_command:
            invocations += 1
        starts_command = token in {"&&", "||", ";", "(", "|"}
    return invocations == len(GRADLE_INVOCATION.findall(command))


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
        "Espera a que termine (si es la app de QA: `python3 .claude/scripts/qa.py stop`). "
        "Si está colgada: `./gradlew --stop` y repite con `clean`.",
        file=sys.stderr,
    )
    sys.exit(2)


def run(command: list) -> int:
    started = time.time()
    try:
        process = subprocess.Popen(command, stdout=subprocess.PIPE, stderr=subprocess.STDOUT, text=True, errors="replace")
    except FileNotFoundError:
        print(f"No existe {command[0]} en {os.getcwd()}; ejecutar desde {PROJECT_DIR}.")
        return 127
    signal.signal(signal.SIGTERM, lambda *_: process.terminate())
    try:
        output, _ = process.communicate()
    except KeyboardInterrupt:
        process.terminate()
        output, _ = process.communicate()
    log_path = save_log(output)
    print(summarize(output, started, log_path))
    return process.returncode


def summarize(output: str, started: float, log_path: Path) -> str:
    output = output.replace(str(PROJECT_DIR) + "/", "").replace("file://", "")
    lines = output.splitlines()
    finished = re.search(r"^BUILD (SUCCESSFUL|FAILED) in (.+)$", output, re.M)
    log = f"Salida completa: {log_path.relative_to(PROJECT_DIR)}"
    if not finished and "FAILURE:" not in output:
        # Sin resultado de build (p. ej. `--stop` o `--version`): la salida suele ser corta.
        tail = lines[-MAX_LINES_PER_SECTION:]
        return "\n".join([*tail, log])

    status = finished.group(0) if finished else "BUILD FAILED"
    summary = [status]

    failures = []
    if re.search(r"^> Task :test( FAILED)?$", output, re.M):
        tests = test_results(started)
        if tests:
            total, failures = tests
            summary.append(f"Pruebas ejecutadas: {total}, fallidas: {len(failures)}")
    elif re.search(r"^> Task :test (UP-TO-DATE|FROM-CACHE)$", output, re.M):
        summary.append("Pruebas: al día, no se han vuelto a ejecutar")

    compiler_errors = [line for line in lines if line.startswith("e: ")]
    warnings = [line for line in lines if line.startswith("w: ")]
    ktlint = [line for line in lines if re.match(r"^\S+\.kts?:\d+:\d+ ", line)]
    failed_tasks = re.findall(r"^> Task (\S+) FAILED", output, re.M)
    failed_tests = [line.strip() for line in lines if re.search(r" > .+ FAILED$", line)]

    section(summary, "Tareas fallidas", failed_tasks)
    section(summary, "Errores de compilación", compiler_errors)
    section(summary, "Ktlint", ktlint)
    section(summary, "Pruebas fallidas", failures or failed_tests)
    if warnings:
        summary.append(f"Avisos del compilador: {len(warnings)} (ver log)")
    if status.startswith("BUILD FAILED") and not (failed_tasks or compiler_errors or ktlint or failed_tests):
        section(summary, "Qué ha fallado", what_went_wrong(output))
    summary.append(log)
    return "\n".join(summary)


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
