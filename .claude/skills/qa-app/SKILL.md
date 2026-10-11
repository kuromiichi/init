---
name: qa-app
description: QA de la experiencia de init en la app real - instalación de QA preparada (sin onboarding, todo desbloqueado, tema/SO/lección/paso por parámetro), control en segundo plano por defecto y pocas capturas. Usar cuando cambie la interfaz o una lección.
---

# QA de la experiencia

## Instalación de QA

`.claude/scripts/qa.py` usa un `user.home` propio en `.claude/qa-home` (ignorado por Git); los datos reales del usuario no se tocan. Cada lanzamiento escribe preferencias con onboarding completado y todas las lecciones desbloqueadas.

```sh
python3 .claude/scripts/qa.py launch --lesson <id> --step <N>   # tema Light y SO MacOS por defecto
python3 .claude/scripts/qa.py launch --theme Dark --os Windows --lesson <id> --step <N>
python3 .claude/scripts/qa.py size 900 700                       # redimensiona sin control total
python3 .claude/scripts/qa.py stop
```

- `launch` cierra la app anterior, lanza `./gradlew run` y vuelve cuando la ventana está abierta (unos 10 s si está compilado). Con `--lesson`, las lecciones anteriores quedan completadas y la indicada guardada en el paso N (0 = primero), así que «Continuar lección» lleva directamente allí. Ids en `LessonCatalog.kt`.
- Cambiar de tema o de SO = relanzar con otro `--theme`/`--os`; no hace falta tocar Configuración.
- Si la build falla, el script muestra los errores `e:`; si se cuelga, repetir con `--clean`.
- Mientras la app está abierta hay un Gradle en curso: cerrarla con `stop` antes de lanzar otras tareas de Gradle.

## Computer use en segundo plano (por defecto)

Pedir acceso a `net.java.openjdk.java` («init») y usar las herramientas `app_*`.

- El árbol de accesibilidad de Compose se rellena de forma perezosa: la primera `app_screenshot` solo devuelve los botones de la ventana; una segunda ya trae los de la app. Usar `scale: 0.1` cuando la captura solo sirve para refrescar el árbol.
- «Paso siguiente», «Paso anterior», «Volver», «Ver lecciones»… se pulsan por `element_index`. «Continuar lección» no responde a AXPress: clic por coordenadas en su centro.
- `app_ax_find` con `title_contains` confirma que un texto está en pantalla, pero no devuelve su contenido. Para leer el contenido de una lección es más barato leer el archivo fuente que capturar.
- Capturas solo para lo visual (maquetación, contraste, temas): `scale` entre 0.25 y 0.4 para orientarse y `app_zoom` sobre la región concreta. Agrupar acciones previsibles en un `app_batch` con una sola captura al final.
- Comprobar el tamaño de la ventana con `app_list_windows` (bounds), sin imagen.

## Control de pantalla completa (solo cuando un paso lo requiera)

La lógica del teclado y del foco de la lección, el inicio y la lista ya la cubren las pruebas de interfaz (skill `verificacion`): no repetirla a mano. Las teclas, el scroll y los botones segmentados/radio no llegan a Compose en segundo plano, así que el control total queda para lo que las pruebas no ven: cómo se ve el contorno de foco, cómo se siente el scroll (incluido el borde superior del panel de práctica), pantallas sin pruebas de teclado (Configuración, onboarding) y controles segmentados. En ese caso, pedir `request_full_control`, hacer esas comprobaciones en un único bloque con `computer_batch` y volver con `release_full_control`.

## Lista de comprobaciones

- Flujos afectados y estados alternativos.
- Contorno de foco visible y sensación del scroll (pantalla completa, solo si el cambio les afecta).
- Redimensionado mínimo y amplio (`qa.py size`).
- Temas claro y oscuro (relanzar): nada se funde con el fondo y el texto es legible.
- Accesibilidad y contenido didáctico.

Al terminar, `qa.py stop`. Una build correcta no demuestra que la QA visual haya pasado: indicar siempre qué no se ha podido comprobar.
