---
name: verificacion
description: Verificar cambios de código en DAM Prep con el mínimo coste - diagnóstico con el MCP de IntelliJ, tareas Gradle focalizadas, build limpia final y recuperación de builds colgadas. Usar tras editar código Kotlin y antes de dar un cambio por bueno.
---

# Verificación

Escalar de lo barato a lo caro y parar en el nivel que el riesgo exija.

## 1. Diagnóstico inmediato con IntelliJ (sin Gradle)

Tras editar, `mcp__idea__get_file_problems` con `errorsOnly: true` y `projectPath: /Users/kuromi/Projects/dam-prep` en cada archivo tocado. Detecta errores de compilación y tipos en segundos sin lanzar Gradle.

- Si responde que el proyecto no está abierto: `open -a "$HOME/Applications/IntelliJ IDEA.app" /Users/kuromi/Projects/dam-prep` y reintentar tras unos segundos (el primer análisis tras indexar puede tardar).
- Para buscar código: `search_symbol` (declaraciones), `get_symbol_info` (firma y documentación en una posición), `analyze_calls` (quién llama a qué), `search_text`/`search_regex` acotando `paths`. Devuelven coordenadas: leer después solo ese rango del archivo.
- Para renombrar símbolos usados en varios archivos: `rename_refactoring`.
- No usar `build_project` ni las configuraciones run de Gradle del IDE mientras haya un Gradle de terminal en curso: comparten `build/`.

## 2. Tareas Gradle focalizadas

Un hook resume la salida (no añadir `| tail`/`| grep`; el log completo queda en `.claude/logs/`). Otro hook rechaza una invocación si ya hay otra en curso.

- Una clase de prueba: `./gradlew test --tests '*LessonContentRulesTest'`
- Todas las pruebas: `./gradlew test`
- Estilo: `./gradlew ktlintFormat` y después `./gradlew ktlintCheck` (combinables: `./gradlew ktlintFormat test`)
- Solo compilar: `./gradlew compileKotlin compileTestKotlin`

Si el resumen no basta para diagnosticar un fallo, leer el log con `grep -n` o por rangos, no entero.

## 3. Cierre de cambios sustanciales

```sh
./gradlew --stop
./gradlew --no-daemon clean build
```

## Builds colgadas

Suelen deberse a permisos sobre salidas previas en `build/`. Detener la tarea, `./gradlew --stop` y repetir precedida de `clean` (`./gradlew --no-daemon clean compileKotlin`). Nunca editar ni borrar archivos sueltos de `build/`.

## Informe

Indicar qué se ha comprobado (archivos diagnosticados, tareas ejecutadas y resultado) y qué no.
