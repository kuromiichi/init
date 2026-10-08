#!/usr/bin/env python3
"""Instalación de QA de DAM Prep con datos aislados en `.claude/qa-home`.

  qa.py launch [--theme Light|Dark|System] [--os MacOS|Windows|Linux]
               [--lesson <id> [--step N]] [--clean]
  qa.py stop
  qa.py size <ancho> <alto>

`launch` cierra la app si estaba abierta, escribe preferencias con el
onboarding completado y todas las lecciones desbloqueadas, opcionalmente deja
la lección indicada como la siguiente pendiente en el paso N (0 = primero), y
lanza `./gradlew run` en segundo plano. Termina cuando la ventana está abierta.
"""

import argparse
import json
import os
import re
import sqlite3
import subprocess
import sys
import time
from pathlib import Path

PROJECT = Path(__file__).resolve().parents[2]
QA_HOME = PROJECT / ".claude" / "qa-home"
DATA_DIR = QA_HOME / "Library" / "Application Support" / "DAM Prep"
LOG = PROJECT / ".claude" / "logs" / "qa-run.log"
MAIN_CLASS = "dev.kuromiichi.damprep.MainKt"
LAUNCH_TIMEOUT = 600


def main() -> None:
    parser = argparse.ArgumentParser()
    commands = parser.add_subparsers(dest="command", required=True)
    launch_parser = commands.add_parser("launch")
    launch_parser.add_argument("--theme", default="Light", choices=["Light", "Dark", "System"])
    launch_parser.add_argument("--os", default="MacOS", choices=["MacOS", "Windows", "Linux"])
    launch_parser.add_argument("--lesson")
    launch_parser.add_argument("--step", type=int, default=0)
    launch_parser.add_argument("--clean", action="store_true")
    commands.add_parser("stop")
    size_parser = commands.add_parser("size")
    size_parser.add_argument("width", type=int)
    size_parser.add_argument("height", type=int)
    args = parser.parse_args()

    if args.command == "launch":
        launch(args)
    elif args.command == "stop":
        stop()
    else:
        resize(args.width, args.height)


def launch(args) -> None:
    stop(quiet=True)
    if gradle_running():
        sys.exit("Hay otra invocación de Gradle en curso; espera a que termine.")
    DATA_DIR.mkdir(parents=True, exist_ok=True)
    write_preferences(args.theme, args.os)
    if args.lesson:
        seed_progress(args.lesson, args.step)

    LOG.parent.mkdir(parents=True, exist_ok=True)
    tasks = ["clean", "run"] if args.clean else ["run"]
    env = {
        **os.environ,
        "GRADLE_USER_HOME": str(Path.home() / ".gradle"),
        "JAVA_TOOL_OPTIONS": f"-Duser.home={QA_HOME}",
    }
    with LOG.open("w") as log:
        subprocess.Popen(
            ["./gradlew", "--no-daemon", *tasks],
            cwd=PROJECT,
            env=env,
            stdout=log,
            stderr=subprocess.STDOUT,
            start_new_session=True,
        )

    deadline = time.time() + LAUNCH_TIMEOUT
    while time.time() < deadline:
        time.sleep(2)
        output = LOG.read_text(errors="replace")
        if "BUILD FAILED" in output or "FAILURE:" in output:
            errors = [line for line in output.splitlines() if line.startswith("e: ")]
            sys.exit("La build ha fallado:\n" + "\n".join(errors[:20]) + f"\nLog: {LOG.relative_to(PROJECT)}")
        if app_pid():
            time.sleep(4)
            lesson = f", lección {args.lesson} paso {args.step}" if args.lesson else ""
            print(f"App de QA abierta (tema {args.theme}, SO {args.os}{lesson}).")
            return
    sys.exit(f"La app no se ha abierto en {LAUNCH_TIMEOUT} s. Log: {LOG.relative_to(PROJECT)}. Si está colgada, repetir con --clean.")


def stop(quiet: bool = False) -> None:
    pid = app_pid()
    if pid:
        subprocess.run(["kill", pid])
        for _ in range(30):
            if not app_pid():
                break
            time.sleep(0.5)
        time.sleep(1)
    if not quiet:
        print("App de QA cerrada." if pid else "La app de QA no estaba abierta.")


def resize(width: int, height: int) -> None:
    script = (
        'tell application "System Events" to tell (first process whose unix id is '
        f"{app_pid() or 0}) to set size of window 1 to {{{width}, {height}}}"
    )
    result = subprocess.run(["osascript", "-e", script], capture_output=True, text=True)
    if result.returncode != 0:
        sys.exit("No se ha podido redimensionar: " + result.stderr.strip())
    print(f"Ventana redimensionada a {width}×{height}.")


def app_pid() -> str:
    result = subprocess.run(["pgrep", "-f", MAIN_CLASS], capture_output=True, text=True)
    for pid in result.stdout.split():
        environment = subprocess.run(["ps", "eww", "-o", "command=", "-p", pid], capture_output=True, text=True).stdout
        if f"user.home={QA_HOME}" in environment:
            return pid
    return ""


def gradle_running() -> bool:
    result = subprocess.run(["pgrep", "-f", "org.gradle.wrapper.GradleWrapperMain"], capture_output=True)
    return result.returncode == 0


def write_preferences(theme: str, target_os: str) -> None:
    """Escribe `user.preferences_pb` (PreferenceMap de DataStore en protobuf)."""
    values = {
        "student_name": ("string", "QA"),
        "pronoun_preference": ("string", "Neutral"),
        "theme_preference": ("string", theme),
        "target_operating_system": ("string", target_os),
        "onboarding_completed": ("bool", True),
        "unlock_all_lessons": ("bool", True),
    }
    preference_map = b""
    for key, (kind, value) in values.items():
        if kind == "bool":
            encoded_value = b"\x08" + (b"\x01" if value else b"\x00")
        else:
            encoded_value = length_delimited(5, value.encode())
        entry = length_delimited(1, key.encode()) + length_delimited(2, encoded_value)
        preference_map += length_delimited(1, entry)
    (DATA_DIR / "user.preferences_pb").write_bytes(preference_map)


def length_delimited(field: int, payload: bytes) -> bytes:
    return varint(field << 3 | 2) + varint(len(payload)) + payload


def varint(value: int) -> bytes:
    encoded = b""
    while True:
        byte = value & 0x7F
        value >>= 7
        if value:
            encoded += bytes([byte | 0x80])
        else:
            return encoded + bytes([byte])


def seed_progress(lesson_id: str, step: int) -> None:
    """Marca como completadas las lecciones anteriores y guarda el paso de la indicada."""
    catalog = (PROJECT / "src/main/kotlin/dev/kuromiichi/damprep/data/lessons/LessonCatalog.kt").read_text()
    lesson_ids = re.findall(r'id = "([^"]+)"', catalog)
    if lesson_id not in lesson_ids:
        sys.exit(f"Lección desconocida: {lesson_id}. Disponibles: {', '.join(lesson_ids)}")
    database = sqlite3.connect(DATA_DIR / "dam-prep.db")
    create_schema_if_needed(database)
    now = int(time.time() * 1000)
    with database:
        database.execute("DELETE FROM lesson_progress")
        database.executemany(
            "INSERT INTO lesson_progress (lesson_id, completed_at) VALUES (?, ?)",
            [(previous, now) for previous in lesson_ids[: lesson_ids.index(lesson_id)]],
        )
        database.execute(
            "INSERT OR REPLACE INTO lesson_step_progress (lesson_id, step_index) VALUES (?, ?)",
            (lesson_id, step),
        )
    database.close()


def create_schema_if_needed(database: sqlite3.Connection) -> None:
    if database.execute("SELECT 1 FROM sqlite_master WHERE name = 'room_master_table'").fetchone():
        return
    schemas = (PROJECT / "schemas").glob("*/*.json")
    latest = max(schemas, key=lambda path: int(path.stem))
    schema = json.loads(latest.read_text())["database"]
    with database:
        for entity in schema["entities"]:
            database.execute(entity["createSql"].replace("${TABLE_NAME}", entity["tableName"]))
        for query in schema["setupQueries"]:
            database.execute(query)
        database.execute(f"PRAGMA user_version = {schema['version']}")


if __name__ == "__main__":
    main()
