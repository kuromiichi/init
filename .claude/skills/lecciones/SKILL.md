---
name: lecciones
description: Crear, editar o revisar lecciones y prácticas de init - fuentes y criterio docente, reglas de las prácticas «Ahora te toca», guía visual (paneles, bloques de código, terminales, capturas) y mapa de archivos. Usar antes de tocar contenido de lecciones o sus visuales.
---

# Lecciones

## Mapa del código

- `data/lessons/LessonCatalog.kt`: orden e identificadores (`LessonSummary`: id, título, `SubjectArea`).
- `data/lessons/LessonContent.kt`: modelo y DSL (`paragraph`, `steps`, `callout`, `explanationCode`, `explanationTerminal`, `operatingSystemStep`, `codeLine`/`keyword`/`string`…) y `LessonContentCatalog`, donde se registra cada contenido.
- `data/lessons/contents/<Tema>LessonContent.kt`: una lección por archivo (600–1100 líneas). Localizar con `search_symbol`/`grep -n` y leer por rangos.
- Pruebas: `LessonCatalogTest` (lista de ids), `LessonContentRulesTest` (contenido legible, variantes por SO, prácticas verificables, programas completos, sin trazabilidad curricular, sin construcciones que se enseñan después: añadir la entrada de la lección nueva), `LessonContentCatalogTest`.
- Visuales: `ui/lessons/LessonVisual.kt`, `ui/common/CodeExamplesVisual.kt`, `TerminalWindowVisual.kt`, `LearningDiagrams.kt`, `ToolLogoCards.kt`, `TraceTableVisual.kt`; resaltado en `data/lessons/KotlinCodeHighlighter.kt`.

Para una lección nueva: archivo en `contents/`, entrada en `LessonCatalog` y `LessonContentCatalog`, y actualizar `LessonCatalogTest` y `LessonContentRulesTest`. Copiar la estructura de la lección más reciente.

## Criterio docente

- Itinerario lineal centrado en programación para 1º de DAM; las materias de apoyo solo contextualizan.
- Contrastar con [Programación 1DAM](https://github.com/joseluisgs/Programacion-00-2022-2023), [Entornos de Desarrollo 1DAM](https://github.com/joseluisgs/EntornosDesarrollo-00-2022-2023) y, cuando proceda, el BOE.
- No copiar la redacción de las fuentes (CC BY-NC-SA 4.0, exige atribución).
- No mostrar al alumno trazabilidad curricular (resultados de aprendizaje, criterios, enlaces al BOE) salvo petición explícita.
- No usar construcciones que se enseñan en lecciones posteriores. Cuando una explicación se refiera al ejemplo, decirlo explícitamente.

## Prácticas («Ahora te toca»)

Enunciado, requisitos y confirmación describen qué debe hacer el programa (comportamiento observable, salida esperada, firmas necesarias para las pruebas), nunca cómo construirlo:
- No nombrar la construcción a usar (clase abstracta, interfaz, sealed, lista privada…) ni qué clase implementa qué.
- No enumerar los casos de prueba exactos ni añadir confirmaciones que revelen el diseño («X no compila»).
- La explicación previa tampoco debe delatarlo («eso se modela con una interfaz»).
- Las firmas sí se dan. La orientación de diseño va solo en las pistas progresivas.

Al escribir o revisar una práctica, releer explicación, objetivo, enunciado, requisitos y confirmación y quitar lo que decida el diseño por el alumno. Pendiente si se pide: revisar con este criterio las prácticas de las lecciones 15 («el monedero») y 16 («la biblioteca»).

## Guía visual

- Diagramas, tarjetas de logos e iconos van sobre un panel más oscuro que la página (`surfaceContainerLow`) con nodos más claros y borde fino (`surfaceContainerHigh` + `outlineVariant`). Nada se funde con el fondo en ningún tema.
- Código, consola y tablas usan `codeBlockSurface`. El resaltado de Kotlin reproduce IntelliJ IDEA con Catppuccin (`KotlinCodeHighlighter`, `spanStyle` en `CodeExamplesVisual`). Errores subrayados: `⟦…⟧` dentro del código.
- Los bloques de código llevan pestaña de editor con `fileName` («Main.kt» por defecto) y el título descriptivo como leyenda.
- Las salidas de terminal (`LessonConsoleKind.Terminal`, `explanationTerminal`) van en una ventana con decoraciones del sistema elegido (semáforo en macOS, botones de Windows 11, botones redondos de GNOME), «Terminal» centrado y fondo Catppuccin Mocha (Latte `crust` en claro, `CatppuccinLatteTerminal`). Las salidas del programa (`IntelliJRun`) llevan la cabecera de la ventana Run.
- Código en línea: color del texto sobre fondo translúcido de ese color (`toAnnotatedString(tintedContentColor = …)`).
- Un color nuevo debe pasar `ThemeContrastTest` en claro y oscuro.
- Todo paso cuyo contenido cambie según el SO (comandos, rutas, archivos del sistema, atajos) se declara con `operatingSystemStep`.
- Capturas de IntelliJ: siempre en pareja `intellij_<nombre>_{light,dark}.png`, elegidas por `LocalCatppuccinFlavor`; se muestran a resolución real, con borde y ampliables. Para hacerlas, skill `capturas-intellij`.

Tras cambiar una lección: skill `verificacion` (al menos `./gradlew test --tests '*Lesson*'`) y skill `qa-app`.
