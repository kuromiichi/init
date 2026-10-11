---
name: capturas-intellij
description: Hacer o rehacer capturas de IntelliJ IDEA para las lecciones de init en pareja claro/oscuro, con computer use limitado a clics - repositorio de muestra reproducible, captura de ventana, cambio de tema en vivo y recorte. Usar al añadir o actualizar capturas del IDE.
---

# Capturas de IntelliJ IDEA

IntelliJ se concede a computer use en nivel «click»: sin teclas ni `app_menu`.

## Requisitos de las capturas

- Pareja `_light`/`_dark`: mismo estado, mismo recorte; solo cambia el tema (Islands Catppuccin Latte / Macchiato).
- Solo la ventana del IDE, sin marco ni sombra, sin el degradado de color de proyecto y sin el resplandor de computer use.
- En la app: `intellij_<nombre>_{light,dark}.png` en `composeResources/drawable`, elegidas por `LocalCatppuccinFlavor` (no `drawable-dark`, que sigue el tema del SO).

## Preparación

- Hacer copia de `~/Library/Application Support/JetBrains/IntelliJIdea*/options/{laf,colors.scheme,window.state,recentProjects}.xml` y `disabled_plugins.txt`, y restaurarlos al terminar (IdeaVim desactivado para una barra de estado limpia).
- Desactivar Settings › Appearance › «Use project colors in main toolbar» (escribe `options/ui.lnf.xml` con `DIFFERENTIATE_PROJECTS=false`; borrar ese archivo al terminar).
- Repositorio de muestra en el scratchpad con fechas deterministas (`GIT_AUTHOR_DATE`/`GIT_COMMITTER_DATE`) y `refs/qa/*`, de modo que cualquier estado de Git (log, ramas, conflicto, commit) se reconstruya por script. Abrir con `open -a "$HOME/Applications/IntelliJ IDEA.app" <dir>`.

## Captura

- `screencapture -x -o -l <windowID>` (id de `app_list_windows`). Las capturas de pantalla completa o de región recogen el resplandor naranja; `-R` puede incluir la ventana de Claude si se solapa. Los popups sí necesitan `-R`.
- Recortar con Pillow en un venv del scratchpad (no viene instalado).
- Cambiar de tema en vivo: `⋮` de la barra › `Theme…` › Islands Catppuccin Latte/Macchiato. El primer clic dentro de un popup recién abierto se pierde: mover el ratón un poco y volver a hacer clic.
- Alternar el foco (otra app y volver) hace que IDEA recargue los archivos cambiados en disco.

Después, comprobar ambas capturas en la app con la skill `qa-app`.
