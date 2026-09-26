package dev.kuromiichi.damprep.data.lessons.contents

import dev.kuromiichi.damprep.data.lessons.LessonCalloutTone
import dev.kuromiichi.damprep.data.lessons.LessonContent
import dev.kuromiichi.damprep.data.lessons.LessonLink
import dev.kuromiichi.damprep.data.lessons.LessonPractice
import dev.kuromiichi.damprep.data.lessons.LessonScreenshot
import dev.kuromiichi.damprep.data.lessons.LessonStep
import dev.kuromiichi.damprep.data.lessons.LessonVisualContent
import dev.kuromiichi.damprep.data.lessons.callout
import dev.kuromiichi.damprep.data.lessons.consoleInput
import dev.kuromiichi.damprep.data.lessons.consoleOutput
import dev.kuromiichi.damprep.data.lessons.consoleRun
import dev.kuromiichi.damprep.data.lessons.emphasis
import dev.kuromiichi.damprep.data.lessons.explanationCode
import dev.kuromiichi.damprep.data.lessons.highlight
import dev.kuromiichi.damprep.data.lessons.inlineCode
import dev.kuromiichi.damprep.data.lessons.paragraph
import dev.kuromiichi.damprep.data.lessons.regular
import dev.kuromiichi.damprep.data.lessons.richText
import dev.kuromiichi.damprep.data.lessons.steps

val gitVersionControlLessonContent =
    LessonContent(
        lessonId = "git-version-control",
        steps =
            listOf(
                LessonStep(
                    title = "Guardar la historia del proyecto",
                    explanation =
                        listOf(
                            paragraph(regular("Cuando un programa crece, cualquier cambio puede romper algo que funcionaba. Guardar copias como "), inlineCode("Notas_final_v2"), regular(" no sirve de mucho: no sabes qué cambió en cada una ni cuál es la buena.")),
                            paragraph(regular("Un "), highlight("sistema de control de versiones"), regular(" registra la historia del proyecto: qué cambió, cuándo, quién lo hizo y por qué. Esa historia se guarda en un "), highlight("repositorio"), regular(".")),
                            paragraph(regular("Git es el sistema más usado. Es "), emphasis("distribuido"), regular(": cada copia del repositorio tiene la historia completa, así que puedes trabajar sin conexión y cualquier copia sirve de respaldo.")),
                        ),
                    visual =
                        LessonVisualContent.Flow(listOf("Cambiar el código", "Guardar una versión", "Consultar la historia", "Volver atrás si hace falta")),
                    links =
                        listOf(
                            LessonLink("Qué es el control de versiones (Pro Git)", "https://git-scm.com/book/es/v2/Inicio---Sobre-el-Control-de-Versiones-Acerca-del-Control-de-Versiones"),
                        ),
                ),
                LessonStep(
                    title = "Presentarte a Git",
                    explanation =
                        listOf(
                            paragraph(regular("Cada versión que guardas lleva tu nombre y tu correo, así que Git los necesita antes de empezar. Se configuran una sola vez en cada ordenador.")),
                            explanationCode(
                                "Terminal",
                                richText(regular("Con "), inlineCode("--global"), regular(", la configuración sirve para todos tus proyectos.")),
                                "git config --global user.name \"Tu Nombre\"",
                                "git config --global user.email \"tu@correo.com\"",
                                "git config --global init.defaultBranch main",
                            ),
                            paragraph(regular("La última línea hace que los repositorios nuevos empiecen en la rama "), inlineCode("main"), regular(", el nombre habitual hoy. Sin ella, Git usa "), inlineCode("master"), regular(".")),
                            paragraph(regular("Puedes usar la terminal de tu sistema o la ventana "), emphasis("Terminal"), regular(" de IntelliJ IDEA: los comandos son iguales en Windows, macOS y Linux.")),
                        ),
                    visual =
                        LessonVisualContent.Console(
                            title = "Terminal",
                            runs =
                                listOf(
                                    consoleRun(
                                        consoleInput("git config --list"),
                                        consoleOutput("user.name=Estudiante DAM"),
                                        consoleOutput("user.email=estudiante@example.com"),
                                        consoleOutput("init.defaultbranch=main"),
                                    ),
                                ),
                        ),
                    links =
                        listOf(
                            LessonLink("Configurar Git por primera vez (Pro Git)", "https://git-scm.com/book/es/v2/Inicio---Sobre-el-Control-de-Versiones-Configurando-Git-por-primera-vez"),
                        ),
                ),
                LessonStep(
                    title = "Crear un repositorio",
                    explanation =
                        listOf(
                            paragraph(regular("Abre la terminal en la carpeta de tu proyecto y ejecuta "), inlineCode("git init"), regular(". Git crea dentro una carpeta oculta, "), inlineCode(".git"), regular(", donde guarda toda la historia. No la modifiques a mano.")),
                            paragraph(regular("En IntelliJ IDEA se hace con "), emphasis("VCS › Enable Version Control Integration"), regular(", eligiendo "), emphasis("Git"), regular(". Equivale a "), inlineCode("git init"), regular(" y basta con hacerlo una vez por proyecto.")),
                            paragraph(regular("Crear el repositorio todavía no guarda nada: los archivos siguen en la carpeta, pero Git aún no los sigue.")),
                        ),
                    visual =
                        LessonVisualContent.Console(
                            title = "Terminal",
                            runs =
                                listOf(
                                    consoleRun(
                                        consoleInput("git init"),
                                        consoleOutput("Initialized empty Git repository in /ruta/a/Notas/.git/"),
                                    ),
                                ),
                        ),
                    links =
                        listOf(
                            LessonLink("Obtener un repositorio (Pro Git)", "https://git-scm.com/book/es/v2/Fundamentos-de-Git-Obteniendo-un-repositorio-Git"),
                            LessonLink("Crear un repositorio en IntelliJ IDEA", "https://www.jetbrains.com/help/idea/set-up-a-git-repository.html"),
                        ),
                ),
                LessonStep(
                    title = "Los tres estados",
                    explanation =
                        listOf(
                            paragraph(regular("Para Git, un archivo puede estar "), highlight("modificado"), regular(", "), highlight("preparado"), regular(" o "), highlight("confirmado"), regular(". Cada estado corresponde a una zona: el "), emphasis("directorio de trabajo"), regular(", donde editas; el "), emphasis("área de preparación"), regular(", donde eliges qué irá en la próxima versión; y el "), emphasis("repositorio"), regular(", donde queda guardada.")),
                            paragraph(inlineCode("git add"), regular(" lleva los cambios del directorio de trabajo al área de preparación. "), inlineCode("git commit"), regular(" guarda en el repositorio todo lo preparado como una nueva versión, llamada "), highlight("commit"), regular(".")),
                            paragraph(regular("Separar los dos pasos te deja elegir: si has cambiado tres archivos, puedes confirmar dos y dejar el tercero para otro commit.")),
                        ),
                    visual =
                        LessonVisualContent.Flow(listOf("Directorio de trabajo", "git add", "Área de preparación", "git commit", "Repositorio")),
                ),
                LessonStep(
                    title = "Mirar antes de actuar",
                    explanation =
                        listOf(
                            paragraph(inlineCode("git status"), regular(" te dice en qué estado está cada archivo. Úsalo antes y después de cada paso: es la forma más rápida de saber qué va a pasar.")),
                            paragraph(regular("Con el repositorio recién creado, todo aparece en "), emphasis("Untracked files"), regular(": son archivos que Git ve, pero que todavía no sigue. "), inlineCode("No commits yet"), regular(" indica que la historia está vacía.")),
                            paragraph(regular("Los mensajes están en inglés, pero casi siempre sugieren el siguiente comando, como "), inlineCode("git add <file>..."), regular(".")),
                        ),
                    visual =
                        LessonVisualContent.Console(
                            title = "Terminal",
                            runs =
                                listOf(
                                    consoleRun(
                                        consoleInput("git status"),
                                        consoleOutput("On branch main"),
                                        consoleOutput(""),
                                        consoleOutput("No commits yet"),
                                        consoleOutput(""),
                                        consoleOutput("Untracked files:"),
                                        consoleOutput("  (use \"git add <file>...\" to include in what will be committed)"),
                                        consoleOutput("        .gitignore"),
                                        consoleOutput("        build.gradle.kts"),
                                        consoleOutput("        gradle/"),
                                        consoleOutput("        gradlew"),
                                        consoleOutput("        settings.gradle.kts"),
                                        consoleOutput("        src/"),
                                        consoleOutput(""),
                                        consoleOutput("nothing added to commit but untracked files present (use \"git add\" to track)"),
                                    ),
                                ),
                        ),
                ),
                LessonStep(
                    title = "Preparar y confirmar",
                    explanation =
                        listOf(
                            paragraph(inlineCode("git add ."), regular(" prepara todos los cambios de la carpeta en la que estás. También puedes indicar un archivo concreto, como "), inlineCode("git add src/main/kotlin/Main.kt"), regular(".")),
                            paragraph(inlineCode("git commit -m"), regular(" crea el commit con el mensaje que va entre comillas. Git le asigna un identificador, el "), highlight("hash"), regular("; normalmente basta con sus siete primeros caracteres, como "), inlineCode("852b01d"), regular(".")),
                            paragraph(regular("Un buen mensaje resume el cambio en una frase corta que empieza por un verbo: "), emphasis("Añadir el programa de notas"), regular(", "), emphasis("Corregir el cálculo de la media"), regular(". Evita mensajes como "), emphasis("cambios"), regular(" o "), emphasis("arreglos"), regular(".")),
                        ),
                    visual =
                        LessonVisualContent.Console(
                            title = "Terminal",
                            runs =
                                listOf(
                                    consoleRun(
                                        consoleInput("git add ."),
                                        consoleInput("git commit -m \"Añadir el programa de notas\""),
                                        consoleOutput("[main (root-commit) 852b01d] Añadir el programa de notas"),
                                        consoleOutput(" 7 files changed, 317 insertions(+)"),
                                        consoleInput("git status"),
                                        consoleOutput("On branch main"),
                                        consoleOutput("nothing to commit, working tree clean"),
                                    ),
                                ),
                        ),
                    links =
                        listOf(
                            LessonLink("Guardar cambios en el repositorio (Pro Git)", "https://git-scm.com/book/es/v2/Fundamentos-de-Git-Guardando-cambios-en-el-Repositorio"),
                        ),
                ),
                LessonStep(
                    title = "Lo que no se guarda",
                    explanation =
                        listOf(
                            paragraph(regular("Hay archivos que no deben entrar en el repositorio: lo que se genera al compilar, en "), inlineCode("build/"), regular("; las cachés de Gradle, en "), inlineCode(".gradle/"), regular("; y la configuración personal del IDE, en "), inlineCode(".idea/"), regular(". Se pueden regenerar y solo estorban.")),
                            paragraph(regular("El archivo "), inlineCode(".gitignore"), regular(", en la raíz del proyecto, indica qué debe ignorar Git: una carpeta, un archivo o un patrón como "), inlineCode("*.log"), regular(" en cada línea. Si tu proyecto no lo tiene, créalo antes del primer commit; un generador o las plantillas de GitHub te dan una base para Gradle e IntelliJ IDEA.")),
                            explanationCode(
                                ".gitignore",
                                richText(regular("La línea que empieza por "), inlineCode("!"), regular(" es una excepción: el Wrapper de Gradle sí debe guardarse.")),
                                ".gradle",
                                "build/",
                                "!gradle/wrapper/gradle-wrapper.jar",
                                ".idea/",
                                "*.iml",
                                ".DS_Store",
                            ),
                        ),
                    visual =
                        LessonVisualContent.Console(
                            title = "Terminal",
                            runs =
                                listOf(
                                    consoleRun(
                                        consoleInput("git status --ignored"),
                                        consoleOutput("On branch main"),
                                        consoleOutput("Ignored files:"),
                                        consoleOutput("  (use \"git add -f <file>...\" to include in what will be committed)"),
                                        consoleOutput("        .gradle/"),
                                        consoleOutput("        .idea/"),
                                        consoleOutput("        build/"),
                                        consoleOutput(""),
                                        consoleOutput("nothing to commit, working tree clean"),
                                    ),
                                ),
                        ),
                    links =
                        listOf(
                            LessonLink("Generador de .gitignore", "https://www.toptal.com/developers/gitignore"),
                            LessonLink("Plantillas de .gitignore de GitHub", "https://github.com/github/gitignore"),
                            LessonLink("Referencia de .gitignore", "https://git-scm.com/docs/gitignore"),
                        ),
                ),
                LessonStep(
                    title = "Consultar la historia",
                    explanation =
                        listOf(
                            paragraph(inlineCode("git log"), regular(" muestra los commits del más reciente al más antiguo, con su hash, autor, fecha y mensaje. Con "), inlineCode("--oneline"), regular(", cada commit ocupa una sola línea.")),
                            paragraph(inlineCode("git diff"), regular(" enseña lo que has cambiado y aún no has preparado: las líneas que empiezan por "), inlineCode("-"), regular(" se quitan y las que empiezan por "), inlineCode("+"), regular(" se añaden. "), inlineCode("git diff --staged"), regular(" muestra lo que ya está preparado.")),
                            paragraph(inlineCode("HEAD"), regular(" es el nombre del commit en el que estás, que normalmente es el último de tu rama.")),
                        ),
                    visual =
                        LessonVisualContent.Console(
                            title = "Terminal",
                            runs =
                                listOf(
                                    consoleRun(
                                        consoleInput("git log --oneline"),
                                        consoleOutput("0fd9829 Separar el cálculo de la media y la calificación"),
                                        consoleOutput("c970365 Extraer la lectura de notas a leerNota"),
                                        consoleOutput("852b01d Añadir el programa de notas"),
                                        consoleInput("git diff"),
                                        consoleOutput("diff --git a/src/main/kotlin/Main.kt b/src/main/kotlin/Main.kt"),
                                        consoleOutput("--- a/src/main/kotlin/Main.kt"),
                                        consoleOutput("+++ b/src/main/kotlin/Main.kt"),
                                        consoleOutput("@@ -16,6 +16,6 @@ fun main() {"),
                                        consoleOutput("     val media = calcularMedia(examen, practica, proyecto)"),
                                        consoleOutput("-    println(\"Media: \$media\")"),
                                        consoleOutput("+    println(\"Media: \$media de 10\")"),
                                        consoleOutput("     println(calificar(media))"),
                                    ),
                                ),
                        ),
                    links =
                        listOf(
                            LessonLink("Ver el historial (Pro Git)", "https://git-scm.com/book/es/v2/Fundamentos-de-Git-Ver-el-Historial-de-Confirmaciones"),
                        ),
                ),
                LessonStep(
                    title = "Deshacer sin miedo",
                    explanation =
                        listOf(
                            steps(
                                richText(inlineCode("git restore <archivo>"), regular(" descarta los cambios del archivo y lo deja como en el último commit. Úsalo con cuidado: lo que no habías guardado se pierde.")),
                                richText(inlineCode("git restore --staged <archivo>"), regular(" saca el archivo del área de preparación, pero conserva tus cambios.")),
                                richText(inlineCode("git revert HEAD"), regular(" crea un commit nuevo que deshace el último. La historia no se borra: queda constancia del error y de su corrección.")),
                            ),
                            callout(
                                "Cuidado con reset --hard",
                                richText(regular("Verás "), inlineCode("git reset --hard"), regular(" en muchos tutoriales. Borra commits y cambios sin preguntar; si no sabes exactamente qué hace, usa "), inlineCode("git revert"), regular(".")),
                                LessonCalloutTone.Warning,
                            ),
                        ),
                    visual =
                        LessonVisualContent.Console(
                            title = "Terminal",
                            runs =
                                listOf(
                                    consoleRun(
                                        consoleInput("git revert HEAD"),
                                        consoleOutput("[main dfafe67] Revert \"Aclarar el texto de la media\""),
                                        consoleOutput(" 1 file changed, 1 insertion(+), 1 deletion(-)"),
                                    ),
                                ),
                        ),
                    links =
                        listOf(
                            LessonLink("Deshacer cambios (Pro Git)", "https://git-scm.com/book/es/v2/Fundamentos-de-Git-Deshacer-Cosas"),
                        ),
                ),
                LessonStep(
                    title = "Git en IntelliJ IDEA",
                    explanation =
                        listOf(
                            paragraph(regular("La ventana "), emphasis("Commit"), regular(", en la barra lateral izquierda, reúne los cambios. "), emphasis("Changes"), regular(" lista los archivos modificados, y "), emphasis("Unversioned Files"), regular(", los nuevos que Git aún no sigue.")),
                            paragraph(regular("IntelliJ IDEA no usa el área de preparación: marcas con casillas los archivos que irán en el commit, escribes el mensaje y pulsas "), emphasis("Commit"), regular(". Si prefieres el flujo de la terminal, actívalo en "), emphasis("Settings › Version Control › Git › Enable staging area"), regular(".")),
                            paragraph(regular("Si seleccionas un archivo y pulsas "), emphasis("Show Diff"), regular(", ves la versión anterior y la actual lado a lado. "), emphasis("Rollback"), regular(" descarta los cambios, como "), inlineCode("git restore"), regular(".")),
                        ),
                    visual =
                        LessonVisualContent.Screenshot(
                            image = LessonScreenshot.IntellijGitCommit,
                            description = "Ventana Commit de IntelliJ IDEA con Main.kt marcado en Changes, Resumen.kt en Unversioned Files y, a la derecha, el diff de Main.kt con el comentario KDoc y el texto de la media añadidos.",
                        ),
                    links =
                        listOf(
                            LessonLink("Hacer commit en IntelliJ IDEA", "https://www.jetbrains.com/help/idea/commit-and-push-changes.html"),
                        ),
                ),
                LessonStep(
                    title = "Probar en una rama",
                    explanation =
                        listOf(
                            paragraph(regular("Una "), highlight("rama"), regular(" es una línea de trabajo independiente. Te permite probar una idea sin tocar "), inlineCode("main"), regular(", que debería tener siempre una versión que funciona.")),
                            explanationCode(
                                "Terminal",
                                richText(inlineCode("git branch"), regular(" lista las ramas y marca con "), inlineCode("*"), regular(" aquella en la que estás.")),
                                "git switch -c entrada",
                                "git add .",
                                "git commit -m \"Mover leerNota a Entrada.kt\"",
                                "git switch main",
                            ),
                            paragraph(inlineCode("git switch -c"), regular(" crea la rama y te lleva a ella; sin "), inlineCode("-c"), regular(", cambia a una rama que ya existe. Al volver a "), inlineCode("main"), regular(", los archivos recuperan el estado de esa rama.")),
                            paragraph(regular("En IntelliJ IDEA, el nombre de la rama actual aparece arriba a la izquierda. Al pulsarlo puedes crear una con "), emphasis("New Branch…"), regular(" o elegir otra y pulsar "), emphasis("Checkout"), regular(".")),
                        ),
                    visual =
                        LessonVisualContent.Screenshot(
                            image = LessonScreenshot.IntellijGitBranches,
                            description = "Menú de ramas de IntelliJ IDEA con las acciones New Branch y Checkout, y el submenú de la rama mensajes con Checkout, Rebase y Merge.",
                        ),
                    links =
                        listOf(
                            LessonLink("Qué es una rama (Pro Git)", "https://git-scm.com/book/es/v2/Ramificaciones-en-Git-%C2%BFQu%C3%A9-es-una-rama%3F"),
                            LessonLink("Gestionar ramas en IntelliJ IDEA", "https://www.jetbrains.com/help/idea/manage-branches.html"),
                        ),
                ),
                LessonStep(
                    title = "Fusionar una rama",
                    explanation =
                        listOf(
                            paragraph(regular("Cuando el trabajo de la rama está listo, vuelve a "), inlineCode("main"), regular(" y ejecuta "), inlineCode("git merge entrada"), regular(". Si "), inlineCode("main"), regular(" no ha cambiado desde que creaste la rama, Git solo la adelanta: es un "), emphasis("fast-forward"), regular(".")),
                            paragraph(regular("Si las dos ramas tienen commits nuevos, Git crea un "), highlight("commit de fusión"), regular(" que las une. En la pestaña "), emphasis("Log"), regular(" de la ventana "), emphasis("Git"), regular(" de IntelliJ IDEA lo verás como dos líneas que se juntan.")),
                            explanationCode(
                                "Terminal",
                                richText(regular("Después de fusionar, la rama ya no hace falta: sus commits forman parte de "), inlineCode("main"), regular(".")),
                                "git switch main",
                                "git merge entrada",
                                "git branch -d entrada",
                            ),
                        ),
                    visual =
                        LessonVisualContent.Screenshot(
                            image = LessonScreenshot.IntellijGitLog,
                            description = "Pestaña Log de IntelliJ IDEA con el historial del proyecto Notas: la rama con el commit Mover leerNota a Entrada.kt se une a main en el commit Merge branch entrada.",
                        ),
                    links =
                        listOf(
                            LessonLink("Ramificar y fusionar (Pro Git)", "https://git-scm.com/book/es/v2/Ramificaciones-en-Git-Procedimientos-B%C3%A1sicos-para-Ramificar-y-Fusionar"),
                            LessonLink("La pestaña Log de IntelliJ IDEA", "https://www.jetbrains.com/help/idea/log-tab.html"),
                        ),
                ),
                LessonStep(
                    title = "Resolver un conflicto",
                    explanation =
                        listOf(
                            paragraph(regular("Si dos ramas cambian la misma línea de forma distinta, Git no puede elegir y detiene la fusión con un "), highlight("conflicto"), regular(". "), inlineCode("git status"), regular(" marca el archivo como "), emphasis("both modified"), regular(".")),
                            explanationCode(
                                "Entrada.kt",
                                richText(regular("Entre "), inlineCode("<<<<<<<"), regular(" y "), inlineCode("======="), regular(" está la versión de tu rama; entre "), inlineCode("======="), regular(" y "), inlineCode(">>>>>>>"), regular(", la de la otra.")),
                                "while (nota == null) {",
                                "<<<<<<< HEAD",
                                "    println(\"Introduce un número\")",
                                "=======",
                                "    println(\"Escribe un entero\")",
                                ">>>>>>> mensajes",
                                "    nota = readln().toIntOrNull()",
                                "}",
                            ),
                            paragraph(regular("Edita el archivo para dejar el resultado que quieres, borra las marcas y ejecuta "), inlineCode("git add"), regular(" y "), inlineCode("git commit"), regular(". Si prefieres empezar de nuevo, "), inlineCode("git merge --abort"), regular(" cancela la fusión.")),
                        ),
                    visual =
                        LessonVisualContent.Console(
                            title = "Terminal",
                            runs =
                                listOf(
                                    consoleRun(
                                        consoleInput("git merge mensajes"),
                                        consoleOutput("Auto-merging src/main/kotlin/Entrada.kt"),
                                        consoleOutput("CONFLICT (content): Merge conflict in src/main/kotlin/Entrada.kt"),
                                        consoleOutput("Automatic merge failed; fix conflicts and then commit the result."),
                                    ),
                                ),
                        ),
                    links =
                        listOf(
                            LessonLink("Conflictos al fusionar (Pro Git)", "https://git-scm.com/book/es/v2/Ramificaciones-en-Git-Procedimientos-B%C3%A1sicos-para-Ramificar-y-Fusionar"),
                        ),
                ),
                LessonStep(
                    title = "Conflictos en IntelliJ IDEA",
                    explanation =
                        listOf(
                            paragraph(regular("IntelliJ IDEA detecta el conflicto y muestra "), emphasis("Merge Conflicts"), regular(" en la ventana "), emphasis("Commit"), regular(". Al pulsar "), emphasis("Resolve"), regular(" puedes quedarte con una de las dos versiones completas o elegir "), emphasis("Resolve Manually"), regular(".")),
                            paragraph(regular("La ventana de fusión muestra tu rama a la izquierda, la otra a la derecha y el resultado en el centro. Con las flechas "), inlineCode(">>"), regular(" y "), inlineCode("<<"), regular(" llevas cada cambio al resultado, y también puedes editarlo a mano.")),
                            paragraph(regular("Al pulsar "), emphasis("Apply Changes"), regular(", el conflicto queda resuelto y solo falta hacer el commit de la fusión.")),
                        ),
                    visual =
                        LessonVisualContent.Screenshot(
                            image = LessonScreenshot.IntellijGitConflicts,
                            description = "Diálogo Conflicts de IntelliJ IDEA al fusionar mensajes en main: Entrada.kt sin resolver, con botones Accept para cada versión y el botón Resolve Manually.",
                        ),
                    links =
                        listOf(
                            LessonLink("Resolver conflictos en IntelliJ IDEA", "https://www.jetbrains.com/help/idea/resolve-conflicts.html"),
                        ),
                ),
                LessonStep(
                    title = "Reorganizar con rebase",
                    explanation =
                        listOf(
                            paragraph(inlineCode("git rebase main"), regular(", ejecutado desde tu rama, vuelve a aplicar tus commits encima del último commit de "), inlineCode("main"), regular(". El resultado es una historia en línea recta, sin commit de fusión.")),
                            paragraph(regular("Rebase "), emphasis("reescribe"), regular(" tus commits, que obtienen hashes nuevos. Por eso, úsalo solo con commits que aún no has compartido; en ramas que usan otras personas, fusiona con "), inlineCode("merge"), regular(".")),
                            paragraph(regular("Si aparece un conflicto durante el rebase, resuélvelo igual que antes y continúa con "), inlineCode("git rebase --continue"), regular(", o cancélalo con "), inlineCode("git rebase --abort"), regular(".")),
                        ),
                    visual =
                        LessonVisualContent.Console(
                            title = "Terminal",
                            runs =
                                listOf(
                                    consoleRun(
                                        consoleInput("git log --oneline --graph --all"),
                                        consoleOutput("* fa51867 Documentar leerNota"),
                                        consoleOutput("| * d4c020e Añadir el README"),
                                        consoleOutput("|/"),
                                        consoleOutput("* 7466532 Merge branch 'mensajes'"),
                                        consoleInput("git rebase main"),
                                        consoleOutput("Successfully rebased and updated refs/heads/documentar."),
                                        consoleInput("git log --oneline --graph"),
                                        consoleOutput("* 6579ce3 Documentar leerNota"),
                                        consoleOutput("* d4c020e Añadir el README"),
                                        consoleOutput("* 7466532 Merge branch 'mensajes'"),
                                    ),
                                ),
                        ),
                    links =
                        listOf(
                            LessonLink("Reorganizar el trabajo con rebase (Pro Git)", "https://git-scm.com/book/es/v2/Ramificaciones-en-Git-Reorganizar-el-Trabajo-Realizado"),
                        ),
                ),
                LessonStep(
                    title = "Traer un commit concreto",
                    explanation =
                        listOf(
                            paragraph(inlineCode("git cherry-pick"), regular(" copia un commit de otra rama en la tuya sin traer el resto. Sirve, por ejemplo, para recuperar una corrección hecha en una rama que no quieres fusionar entera.")),
                            paragraph(regular("El commit copiado es nuevo y tiene otro hash, aunque conserve el mensaje y los cambios.")),
                            paragraph(regular("En IntelliJ IDEA, abre la pestaña "), emphasis("Log"), regular(", selecciona el commit y pulsa "), emphasis("Cherry-Pick"), regular(", el botón con forma de cereza.")),
                        ),
                    visual =
                        LessonVisualContent.Console(
                            title = "Terminal",
                            runs =
                                listOf(
                                    consoleRun(
                                        consoleInput("git log --oneline --graph --all"),
                                        consoleOutput("* aa88d09 Explicar cómo usar el programa"),
                                        consoleOutput("| * c34a318 Borrador sin terminar"),
                                        consoleOutput("| * 5c9ef9f Aclarar el texto de la media"),
                                        consoleOutput("|/"),
                                        consoleOutput("* 6579ce3 Documentar leerNota"),
                                        consoleInput("git cherry-pick 5c9ef9f"),
                                        consoleOutput("[main 67b3110] Aclarar el texto de la media"),
                                        consoleOutput(" 1 file changed, 1 insertion(+), 1 deletion(-)"),
                                    ),
                                ),
                        ),
                    links =
                        listOf(
                            LessonLink("Referencia de git cherry-pick", "https://git-scm.com/docs/git-cherry-pick"),
                            LessonLink("Merge, rebase y cherry-pick en IntelliJ IDEA", "https://www.jetbrains.com/help/idea/apply-changes-from-one-branch-to-another.html"),
                        ),
                ),
                LessonStep(
                    title = "Organizar las ramas: Git Flow",
                    explanation =
                        listOf(
                            paragraph(regular("En un equipo conviene acordar cómo se usan las ramas. "), highlight("Git Flow"), regular(" es un modelo clásico: "), inlineCode("main"), regular(" guarda solo versiones publicadas y "), inlineCode("develop"), regular(" reúne el trabajo en curso.")),
                            paragraph(regular("Cada funcionalidad se desarrolla en una rama "), inlineCode("feature/…"), regular(" que sale de "), inlineCode("develop"), regular(" y vuelve a ella. Para preparar una publicación se crea una rama "), inlineCode("release/…"), regular(", y las correcciones urgentes van en "), inlineCode("hotfix/…"), regular(", que sale de "), inlineCode("main"), regular(".")),
                            paragraph(regular("Está pensado para proyectos con versiones planificadas. En proyectos pequeños basta algo más sencillo, como "), emphasis("GitHub flow"), regular(": "), inlineCode("main"), regular(" y una rama corta para cada tarea, como harás en la práctica.")),
                        ),
                    visual =
                        LessonVisualContent.Flow(listOf("feature/…", "develop", "release/…", "main")),
                    links =
                        listOf(
                            LessonLink("Git Flow explicado paso a paso (Atlassian)", "https://www.atlassian.com/es/git/tutorials/comparing-workflows/gitflow-workflow"),
                            LessonLink("El artículo original de Git Flow", "https://nvie.com/posts/a-successful-git-branching-model/"),
                            LessonLink("GitHub flow, una alternativa más sencilla", "https://docs.github.com/es/get-started/using-github/github-flow"),
                        ),
                ),
                LessonStep(
                    title = "Ahora te toca: versionar el proyecto de notas",
                    explanation =
                        listOf(
                            paragraph(regular("Vas a guardar en Git la refactorización de la lección anterior paso a paso, como si la hicieras ahora.")),
                            paragraph(regular("Si ya la terminaste, crea un proyecto nuevo, pega el programa original de aquella práctica y repite los cambios. Así cada commit tendrá sentido.")),
                            paragraph(regular("Usa la terminal o IntelliJ IDEA, como prefieras, y ejecuta "), inlineCode("git status"), regular(" a menudo.")),
                        ),
                    visual =
                        LessonVisualContent.Flow(listOf("git init", "Un commit por paso", "Rama entrada", "Fusionar en main")),
                    links =
                        listOf(
                            LessonLink("Libro Pro Git en español", "https://git-scm.com/book/es/v2"),
                            LessonLink("Compartir un proyecto en GitHub desde IntelliJ IDEA", "https://www.jetbrains.com/help/idea/manage-projects-hosted-on-github.html"),
                        ),
                    practice =
                        LessonPractice(
                            id = "git-version-control-grades",
                            objective = richText(highlight("Guardar la evolución de un programa en commits pequeños y con sentido.")),
                            statement = richText(regular("Pon el proyecto de notas bajo Git y registra cada paso de la refactorización en su propio commit.")),
                            requirements =
                                listOf(
                                    richText(regular("Configura tu nombre, tu correo y "), inlineCode("main"), regular(" como rama inicial.")),
                                    richText(regular("Crea el repositorio y comprueba con "), inlineCode("git status"), regular(" que no aparecen "), inlineCode("build/"), regular(", "), inlineCode(".gradle/"), regular(" ni "), inlineCode(".idea/"), regular(".")),
                                    richText(regular("Haz un commit con el programa original y otro por cada paso: extraer "), inlineCode("leerNota"), regular(", separar "), inlineCode("calcularMedia"), regular(" y "), inlineCode("calificar"), regular(", y documentar con KDoc.")),
                                    richText(regular("Mueve "), inlineCode("leerNota"), regular(" a "), inlineCode("Entrada.kt"), regular(" en una rama llamada "), inlineCode("entrada"), regular(", fusiónala en "), inlineCode("main"), regular(" y borra la rama.")),
                                    richText(regular("Al terminar, "), inlineCode("git status"), regular(" indica "), inlineCode("working tree clean"), regular(".")),
                                ),
                            expectedOutput =
                                listOf(
                                    consoleRun(
                                        consoleInput("git log --oneline"),
                                        consoleOutput("a661edb Mover leerNota a Entrada.kt"),
                                        consoleOutput("3b8c21f Documentar las funciones con KDoc"),
                                        consoleOutput("0fd9829 Separar el cálculo de la media y la calificación"),
                                        consoleOutput("c970365 Extraer la lectura de notas a leerNota"),
                                        consoleOutput("852b01d Añadir el programa de notas"),
                                    ),
                                ),
                            hints =
                                listOf(
                                    richText(regular("Si "), inlineCode("git commit"), regular(" responde "), inlineCode("nothing added to commit"), regular(", te falta "), inlineCode("git add"), regular(". Comprueba con "), inlineCode("git status"), regular(" qué está preparado.")),
                                    richText(regular("Antes de crear la rama, asegúrate de que "), inlineCode("main"), regular(" está limpio; si no, haz primero el commit pendiente.")),
                                    richText(regular("Como "), inlineCode("main"), regular(" no cambia mientras trabajas en "), inlineCode("entrada"), regular(", la fusión será un "), emphasis("fast-forward"), regular(" y no creará commit de fusión.")),
                                ),
                            extension = richText(regular("Publica el repositorio en GitHub desde IntelliJ IDEA con "), emphasis("Git › GitHub › Share Project on GitHub"), regular(". Necesitarás una cuenta, y la primera vez IntelliJ IDEA te pedirá iniciar sesión.")),
                            confirmation = richText(regular("Confirma que "), inlineCode("git log --oneline"), regular(" muestra los cinco commits en ese orden. Los hashes serán distintos en tu ordenador.")),
                        ),
                ),
            ),
    )
