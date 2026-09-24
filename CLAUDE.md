# DAM Prep

Aplicación de escritorio en Kotlin/JVM con Compose Multiplatform y Material 3 para preparar la programación de 1º de DAM mediante un itinerario lineal de lecciones guiadas.

## Flujo de trabajo

El agente principal lleva cada tarea de principio a fin. Los pasos son flexibles y la verificación se ajusta al alcance y al riesgo del cambio. No hay subagentes ni revisión independiente obligatorios.

1. **Definir** el objetivo, la experiencia esperada y el alcance cuando haga falta. Si una ambigüedad cambia materialmente el resultado, preguntar; si no, asumir algo razonable y decirlo.
2. **Investigar** solo el código y las fuentes relevantes. Para contenido docente, contrastar con [Programación 1DAM](https://github.com/joseluisgs/Programacion-00-2022-2023), [Entornos de Desarrollo 1DAM](https://github.com/joseluisgs/EntornosDesarrollo-00-2022-2023) y, cuando proceda, el BOE. Para detalles técnicos, priorizar la documentación oficial de Kotlin, Compose Multiplatform, Material 3, Room, Koin y Gradle.
3. **Implementar** cambios acotados e idiomáticos, reutilizando componentes y patrones existentes y añadiendo pruebas útiles para la lógica afectada. Antes de dar un cambio por bueno, contrastar cada patrón o API nuevos con la documentación oficial (guías de arquitectura de Android para ViewModels, estado y corrutinas; Kotlin; Compose Multiplatform; Material 3; Koin; Room) y con la versión de la dependencia que usa el proyecto. No basta con que compile y funcione: si algo se aparta de la práctica recomendada, corregirlo o justificarlo, e indicar qué se ha verificado.
4. **Revisar el diff y verificar según el riesgo.** Para cambios sustanciales, ejecutar la build limpia descrita abajo.
5. **Probar la experiencia en la app** si cambia la interfaz o una lección.
6. **Corregir** lo detectado y repetir las comprobaciones afectadas. Hacer commit solo si se pide o forma parte explícita del encargo.

## Principios

- Itinerario lineal, centrado en programación y adecuado para 1º de DAM. Las materias de apoyo contextualizan lecciones, no crean caminos alternativos.
- No mostrar al alumno trazabilidad curricular (resultados de aprendizaje, criterios, enlaces al BOE) salvo petición explícita. El currículo guía el alcance internamente.
- No copiar la redacción de las fuentes; el material de referencia es CC BY-NC-SA 4.0 y exige atribución.
- Mantener el lenguaje visual establecido de Compose y Material 3.
- Preferir Kotlin idiomático y patrones nativos del framework frente a infraestructura propia.
- Cambios acotados: sin refactorizaciones oportunistas ni modificaciones ajenas a la tarea.

## Verificación

No ejecutar invocaciones de Gradle en paralelo. Si una compilación se queda colgada (suele deberse a permisos sobre salidas previas en `build/`), detenerla, ejecutar `./gradlew --stop` y repetir la tarea precedida de `clean`, por ejemplo `./gradlew --no-daemon clean compileKotlin`.

Durante la iteración bastan tareas focalizadas (`./gradlew test`, `./gradlew ktlintCheck`, `./gradlew ktlintFormat`). Para cambios sustanciales, cerrar con:

```sh
./gradlew --stop
./gradlew --no-daemon clean build
```

Esa invocación cubre compilación, KSP, pruebas y Ktlint. La build limpia importa porque los recursos de Compose obsoletos pueden generar declaraciones `Res` duplicadas. Todo lo que hay bajo `build/` es salida desechable: nunca editar ni borrar archivos generados sueltos.

## QA de la experiencia

Lanzar la app con `./gradlew run` en segundo plano (sin otra invocación de Gradle en curso). En macOS el proceso aparece como «DAM Prep» con el identificador `net.java.openjdk.java`; hay que solicitar acceso de computer use a ese identificador. En control en segundo plano, los clics por accesibilidad (botones como «Paso anterior» o «Paso siguiente») funcionan, pero las teclas y el desplazamiento no llegan a Compose: para probar los atajos de teclado o el scroll hace falta control de pantalla completa. Al terminar, cerrar la ventana para que finalice la tarea `run`.

Si `run` no sirve, empaquetar con `./gradlew createDistributable` y abrir el `.app` de `build/compose/binaries/main/app/`. El checkout vive en una carpeta del Escritorio gestionada por File Provider, así que empaquetar puede crear duplicados numerados como `Res 2.kt` o `git 2.svg`. Si ocurre, detener Gradle, empaquetar desde una copia temporal local (excluyendo `.git`, `.gradle` y `build`) y repetir después la build limpia en el checkout real para que su estado final sea el válido.

La app usa los datos reales del usuario (preferencias y progreso): evitar completar lecciones o reiniciar el progreso durante la QA salvo que la prueba lo requiera, y dejar el paso guardado como estaba.

Revisar los flujos afectados, estados alternativos, navegación con teclado y foco, redimensionado de ventana, temas claro y oscuro, legibilidad, accesibilidad y el contenido didáctico. Una build o un paquete correctos no demuestran que la QA visual haya pasado: indicar siempre qué no se ha podido comprobar.

## Commits

Conventional Commits en español (`feat:`, `fix:`, `refactor:`, `docs:`, `test:`, `chore:`…). Incluir solo cambios relacionados y no reescribir el historial sin petición explícita.
