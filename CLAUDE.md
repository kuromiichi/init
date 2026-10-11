# init

Aplicación de escritorio en Kotlin/JVM con Compose Multiplatform y Material 3 para aprender a programar mediante un itinerario lineal de lecciones guiadas, con contenidos seleccionados a partir de la programación de 1º de DAM. Responder siempre en español (chat, propuestas y commits).

## Flujo de trabajo

El agente principal lleva cada tarea de principio a fin; la verificación se ajusta al alcance y al riesgo. No hay subagentes ni revisión independiente obligatorios.

1. **Definir** objetivo, experiencia esperada y alcance. Si una ambigüedad cambia materialmente el resultado, preguntar; si no, asumir algo razonable y decirlo.
2. **Investigar** solo lo relevante. Para localizar código, preferir el MCP de IntelliJ (`search_symbol`, `get_symbol_info`) a leer archivos enteros; los contenidos de lecciones son largos: leer por rangos.
3. **Implementar** cambios acotados e idiomáticos, reutilizando componentes y patrones existentes, con pruebas útiles para la lógica afectada. Contrastar cada patrón o API nuevos con la documentación oficial (guías de arquitectura de Android para ViewModels, estado y corrutinas; Kotlin; Compose Multiplatform; Material 3; Koin; Room) y con la versión de la dependencia del proyecto. Si algo se aparta de la práctica recomendada, corregirlo o justificarlo, e indicar qué se ha verificado.
4. **Verificar según el riesgo** con la skill `verificacion`.
5. **Probar la experiencia en la app** con la skill `qa-app` si cambia la interfaz o una lección.
6. **Corregir** y repetir las comprobaciones afectadas. Commit solo si se pide o forma parte del encargo.

Skills del proyecto: `lecciones` (crear o editar lecciones y prácticas, guía visual), `verificacion`, `qa-app` y `capturas-intellij`. Cargarlas antes de trabajar en su ámbito.

## Principios

- Itinerario lineal, centrado en programación y adecuado para 1º de DAM. Las materias de apoyo contextualizan lecciones, no crean caminos alternativos.
- Mantener el lenguaje visual establecido de Compose y Material 3. El contraste texto/fondo del tema se fija en `ThemeContrastTest`.
- Preferir Kotlin idiomático y patrones nativos del framework frente a infraestructura propia.
- Cambios acotados: sin refactorizaciones oportunistas ni modificaciones ajenas a la tarea.
- La interfaz debe verse y funcionar igual en macOS, Windows y Linux. Limitación conocida: `sqlite-bundled` no incluye Windows ARM64.

## Gradle

- Nunca invocaciones en paralelo (un hook lo bloquea). Un hook hace pasar cada `./gradlew` por `.claude/hooks/gradle.py run`, que imprime solo un resumen (estado, errores, fallos de pruebas y Ktlint) y guarda el log completo en `.claude/logs/`, tanto si la build pasa como si falla. Lanzar Gradle desde la raíz del proyecto, sin `| tail` ni `| grep` y sin heredocs en el mismo comando (con `<<` no se reescribe).
- Si se cuelga: `./gradlew --stop` y repetir con `clean` delante.
- Cambios sustanciales: cerrar con `./gradlew --stop` y `./gradlew --no-daemon clean build` (compilación, KSP, pruebas y Ktlint; la build limpia evita declaraciones `Res` duplicadas).
- Todo lo de `build/` es salida desechable: nunca editar ni borrar archivos generados sueltos.

## Commits

Conventional Commits en español (`feat:`, `fix:`, `refactor:`, `docs:`, `test:`, `chore:`…). Incluir solo cambios relacionados y no reescribir el historial sin petición explícita.
