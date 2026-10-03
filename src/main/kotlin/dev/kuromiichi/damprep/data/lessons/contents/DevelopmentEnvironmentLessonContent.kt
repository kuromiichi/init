package dev.kuromiichi.damprep.data.lessons.contents

import dev.kuromiichi.damprep.data.lessons.LessonCodeExample
import dev.kuromiichi.damprep.data.lessons.LessonContent
import dev.kuromiichi.damprep.data.lessons.LessonLink
import dev.kuromiichi.damprep.data.lessons.LessonPractice
import dev.kuromiichi.damprep.data.lessons.LessonScreenshot
import dev.kuromiichi.damprep.data.lessons.LessonStep
import dev.kuromiichi.damprep.data.lessons.LessonStepContent
import dev.kuromiichi.damprep.data.lessons.LessonTool
import dev.kuromiichi.damprep.data.lessons.LessonToolLogo
import dev.kuromiichi.damprep.data.lessons.LessonVisualContent
import dev.kuromiichi.damprep.data.lessons.consoleOutput
import dev.kuromiichi.damprep.data.lessons.consoleRun
import dev.kuromiichi.damprep.data.lessons.emphasis
import dev.kuromiichi.damprep.data.lessons.highlight
import dev.kuromiichi.damprep.data.lessons.inlineCode
import dev.kuromiichi.damprep.data.lessons.kotlinCode
import dev.kuromiichi.damprep.data.lessons.paragraph
import dev.kuromiichi.damprep.data.lessons.paragraphs
import dev.kuromiichi.damprep.data.lessons.regular
import dev.kuromiichi.damprep.data.lessons.richText
import dev.kuromiichi.damprep.data.lessons.steps

val developmentEnvironmentLessonContent =
    LessonContent(
        lessonId = "development-environment",
        steps =
            listOf(
                LessonStep(
                    title = "Tu entorno de trabajo",
                    explanation =
                        paragraphs(
                            "Antes de escribir código con comodidad necesitamos preparar el lugar donde vas a trabajar.",
                            "Un entorno de desarrollo es el conjunto de herramientas que te ayudan a escribir, ejecutar, revisar y corregir programas.",
                            "Empezaremos con cuatro piezas: IntelliJ IDEA, Kotlin, un JDK y Git.",
                        ),
                    visual =
                        LessonVisualContent.ToolCards(
                            tools =
                                listOf(
                                    LessonTool(
                                        name = "IntelliJ IDEA",
                                        kind = "IDE",
                                        logo = LessonToolLogo.IntellijIdea,
                                    ),
                                    LessonTool(
                                        name = "Kotlin",
                                        kind = "Lenguaje",
                                        logo = LessonToolLogo.Kotlin,
                                    ),
                                    LessonTool(
                                        name = "JDK",
                                        kind = "Java",
                                        logo = LessonToolLogo.Jdk,
                                    ),
                                    LessonTool(
                                        name = "Git",
                                        kind = "Versiones",
                                        logo = LessonToolLogo.Git,
                                    ),
                                ),
                        ),
                ),
                LessonStep(
                    title = "IDE: la mesa de trabajo",
                    explanation =
                        paragraphs(
                            "IDE significa entorno de desarrollo integrado. Lo importante está en integrado: reúne herramientas que por separado serían más incómodas.",
                            "El editor colorea el código y avisa de errores. El sistema de ejecución lanza el programa y el depurador permite detenerlo para mirar valores.",
                            "El IDE no programa por ti: te da señales mientras piensas y mantiene juntos el SDK, el lenguaje y las dependencias.",
                        ),
                    visual =
                        LessonVisualContent.Screenshot(
                            image = LessonScreenshot.IntellijIdeWorkspace,
                            description = "Captura de IntelliJ IDEA con el árbol del proyecto, el editor con código Kotlin y la ventana Git con el historial de commits.",
                        ),
                ),
                LessonStep(
                    title = "JDK y JRE: no son lo mismo",
                    explanation =
                        listOf(
                            paragraph("Java aparece aunque escribamos Kotlin porque Kotlin/JVM se apoya en la máquina virtual de Java."),
                            paragraph(
                                regular("El JRE, o entorno de ejecución, permite arrancar programas ya preparados. El JDK añade las herramientas necesarias para desarrollarlos, entre ellas el compilador "),
                                inlineCode("javac"),
                                regular("."),
                            ),
                            paragraph("Desde Java 9 el JDK ya no incluye una carpeta JRE en su interior, y desde Java 11 Oracle dejó de publicar el JRE por separado. Para DAM Prep instalaremos el JDK completo y lo seleccionaremos como SDK del proyecto."),
                        ),
                    visual =
                        LessonVisualContent.Flow(
                            nodes =
                                listOf(
                                    "Código Kotlin",
                                    "Compilador",
                                    "Bytecode",
                                    "JVM",
                                    "Programa",
                                ),
                        ),
                ),
                LessonStep(
                    title = "Kotlin, IntelliJ y el plugin",
                    explanation =
                        paragraphs(
                            "Kotlin será el idioma en el que escribiremos las instrucciones. IntelliJ IDEA será el taller donde las escribiremos.",
                            "El soporte del lenguaje permite al IDE reconocer la sintaxis, mostrar avisos, ofrecer autocompletado y conectar con el compilador.",
                            "En IntelliJ moderno viene integrado. Aun así, el proyecto debe indicar con qué versión de Kotlin va a trabajar.",
                        ),
                    visual =
                        LessonVisualContent.ToolCards(
                            tools =
                                listOf(
                                    LessonTool(
                                        name = "Kotlin",
                                        kind = "Lenguaje",
                                        logo = LessonToolLogo.Kotlin,
                                    ),
                                    LessonTool(
                                        name = "IntelliJ IDEA",
                                        kind = "IDE",
                                        logo = LessonToolLogo.IntellijIdea,
                                    ),
                                ),
                        ),
                ),
                LessonStep(
                    title = "Gradle: construir sin hacerlo a mano",
                    explanation =
                        paragraphs(
                            "Gradle es una herramienta de construcción. Lee la configuración del proyecto para saber cómo compilar, ejecutar pruebas, empaquetar y resolver dependencias.",
                            "Al principio lo verás como archivos y botones de sincronización dentro de IntelliJ.",
                            "No hace falta dominarlo ahora. Su papel es evitar el trabajo manual y permitir que el proyecto se construya igual en otro ordenador.",
                        ),
                    visual =
                        LessonVisualContent.Flow(
                            nodes =
                                listOf(
                                    "Configuración",
                                    "Dependencias",
                                    "Compilar",
                                    "Probar",
                                    "Ejecutar",
                                ),
                        ),
                ),
                LessonStep(
                    title = "Git: memoria del proyecto",
                    explanation =
                        paragraphs(
                            "Git no compila ni ejecuta el programa. Registra la historia del proyecto: qué cambió, cuándo cambió y por qué.",
                            "El control de versiones forma parte del entorno profesional, aunque todavía no necesites escribir comandos.",
                            "Más adelante nos permitirá guardar puntos seguros, comparar cambios y trabajar sin miedo a romper algo.",
                        ),
                    visual =
                        LessonVisualContent.Flow(
                            nodes =
                                listOf(
                                    "Cambios",
                                    "Commit",
                                    "Historia",
                                    "Comparar",
                                    "Recuperar",
                                ),
                        ),
                ),
                LessonStep(
                    title = "1. Instalar el JDK 25 para DAM Prep",
                    explanation =
                        paragraphs(
                            "Selecciona el sistema operativo que vas a utilizar para ver el paquete, la arquitectura y los pasos de instalación adecuados.",
                        ),
                    visual =
                        LessonVisualContent.Flow(
                            nodes =
                                listOf(
                                    "Temurin 25",
                                    "JDK",
                                    "Sistema",
                                    "Arquitectura",
                                    "Instalar",
                                ),
                        ),
                    operatingSystemContent = platformContent { content -> content.jdkInstallation },
                ),
                LessonStep(
                    title = "2. Instalar IntelliJ IDEA",
                    explanation =
                        paragraphs(
                            "Selecciona tu sistema operativo para instalar IntelliJ IDEA mediante el formato recomendado por JetBrains.",
                        ),
                    visual =
                        LessonVisualContent.ToolCards(
                            tools =
                                listOf(
                                    LessonTool(
                                        name = "IntelliJ IDEA",
                                        kind = "IDE",
                                        logo = LessonToolLogo.IntellijIdea,
                                    ),
                                ),
                        ),
                    operatingSystemContent = platformContent { content -> content.intellijInstallation },
                ),
                LessonStep(
                    title = "3. Instalar Git",
                    explanation =
                        paragraphs(
                            "Selecciona tu sistema operativo para instalar Git con el método apropiado y comprobar que está disponible.",
                        ),
                    visual =
                        LessonVisualContent.Flow(
                            nodes =
                                listOf(
                                    "Descargar",
                                    "Instalar",
                                    "Nueva terminal",
                                    "git --version",
                                ),
                        ),
                    operatingSystemContent = platformContent { content -> content.gitInstallation },
                ),
                LessonStep(
                    title = "4. Crear el proyecto Kotlin con Gradle",
                    explanation =
                        listOf(
                            paragraph("No necesitas descargar Kotlin ni Gradle por separado. El soporte de Kotlin viene incluido en IntelliJ IDEA y el Gradle Wrapper descargará la versión configurada."),
                            steps(
                                richText(regular("Abre "), emphasis("New Project"), regular(" y elige el generador "), emphasis("Kotlin"), regular(".")),
                                richText(regular("En "), emphasis("Build system"), regular(" selecciona "), emphasis("Gradle"), regular("; en "), emphasis("JDK"), regular(", el JDK 25 que instalaste; y en "), emphasis("Gradle DSL"), regular(", "), emphasis("Kotlin"), regular(".")),
                                richText(regular("Activa "), emphasis("Add sample code"), regular(" para que se cree un programa de ejemplo.")),
                                richText(regular("En "), emphasis("Advanced Settings"), regular(", comprueba que "), emphasis("Gradle distribution"), regular(" es "), emphasis("Wrapper"), regular(".")),
                            ),
                            paragraph("Crea el proyecto y espera a que finalice la primera sincronización antes de ejecutar nada."),
                        ),
                    visual =
                        LessonVisualContent.Flow(
                            nodes =
                                listOf(
                                    "New Project",
                                    "Kotlin",
                                    "Gradle",
                                    "JDK 25",
                                    "Gradle sync",
                                ),
                        ),
                    links =
                        listOf(
                            LessonLink(
                                label = "Seguir el tutorial oficial de Kotlin/JVM",
                                url = "https://kotlinlang.org/docs/get-started-with-jvm-gradle-project.html",
                            ),
                            LessonLink(
                                label = "Entender qué hace Gradle Wrapper",
                                url = "https://docs.gradle.org/current/userguide/gradle_wrapper.html",
                            ),
                        ),
                ),
                LessonStep(
                    title = "Ejecutar no es lo mismo que depurar",
                    explanation =
                        listOf(
                            paragraph(
                                regular("Cuando ejecutas un programa con el triángulo verde situado junto a "),
                                inlineCode("main"),
                                regular(", lo pones en marcha y observas el resultado en la consola."),
                            ),
                            paragraph(
                                regular("Para depurar, haz clic junto al número de una línea: aparece un punto rojo, un "),
                                highlight("breakpoint"),
                                regular(". Al lanzar "),
                                emphasis("Debug"),
                                regular(", el programa se detiene en esa línea y puedes avanzar paso a paso y mirar cuánto vale cada variable."),
                            ),
                            paragraph("Ejecutar responde qué ha pasado. Depurar ayuda a descubrir por qué."),
                        ),
                    visual =
                        LessonVisualContent.Screenshot(
                            image = LessonScreenshot.IntellijDebugBreakpoint,
                            description = "Editor de IntelliJ IDEA con el triángulo verde para ejecutar main y un breakpoint rojo en la línea de println.",
                        ),
                ),
                LessonStep(
                    title = "Configurar sin convertirlo en ritual",
                    explanation =
                        paragraphs(
                            "Los IDE tienen paneles de configuración, atajos, temas, plugins y actualizaciones. No hace falta tocarlo todo el primer día.",
                            "Cambia solo lo que mejora tu trabajo: el tema visual, el tamaño de letra, el SDK correcto y los plugins necesarios.",
                            "De momento buscamos una base estable: abrir, entender, ejecutar y corregir.",
                        ),
                    visual =
                        LessonVisualContent.IconText(
                            title = "Base estable",
                            body = "SDK correcto, proyecto sincronizado, botón de ejecutar claro, Git disponible y errores visibles. Con eso ya se puede aprender sin pelearse con la herramienta.",
                        ),
                ),
                LessonStep(
                    title = "Comprobar el entorno con Hello World",
                    explanation =
                        listOf(
                            paragraph(
                                highlight("IntelliJ IDEA"),
                                regular(" habrá creado un pequeño programa de ejemplo. "),
                                emphasis("No necesitas entender todavía cómo está escrito."),
                            ),
                            paragraph(
                                regular("Abre "),
                                inlineCode("Main.kt"),
                                regular(", pulsa el icono verde situado junto a "),
                                inlineCode("main"),
                                regular(" y ejecuta "),
                                inlineCode("MainKt"),
                                regular("."),
                            ),
                            paragraph(
                                regular("Cuando termine, comprueba que la consola muestra "),
                                inlineCode("Hello World!"),
                                regular("."),
                            ),
                        ),
                    visual =
                        LessonVisualContent.CodeExamples(
                            examples =
                                listOf(
                                    LessonCodeExample(
                                        title = "Main.kt",
                                        lines =
                                            kotlinCode(
                                                """
                                                fun main() {
                                                    println("Hello World!")
                                                }
                                                """.trimIndent(),
                                            ),
                                        note = richText(regular("Resultado esperado en la consola: "), inlineCode("Hello World!"), regular(".")),
                                    ),
                                ),
                        ),
                    practice =
                        LessonPractice(
                            id = "development-environment-hello-world",
                            objective =
                                richText(
                                    regular("Verificar que tu primer proyecto está bien configurado y puede ejecutar "),
                                    highlight("código Kotlin"),
                                    regular("."),
                                ),
                            statement =
                                richText(
                                    regular("Abre "),
                                    inlineCode("Main.kt"),
                                    regular(" y ejecuta la función "),
                                    inlineCode("main"),
                                    regular(". Todavía no necesitas modificar el código generado."),
                                ),
                            requirements =
                                listOf(
                                    richText(
                                        regular("El proyecto utiliza el "),
                                        inlineCode("JDK 25"),
                                        regular(" seleccionado en la configuración."),
                                    ),
                                    richText(
                                        inlineCode("Main.kt"),
                                        regular(" contiene una función "),
                                        inlineCode("main"),
                                        regular(" que llama a "),
                                        inlineCode("println"),
                                        regular("."),
                                    ),
                                    richText(
                                        regular("La ejecución finaliza "),
                                        emphasis("sin errores"),
                                        regular("."),
                                    ),
                                ),
                            expectedOutput = listOf(consoleRun(consoleOutput("Hello World!"))),
                            hints =
                                listOf(
                                    richText(
                                        regular("Pulsa el icono verde junto a "),
                                        inlineCode("fun main()"),
                                        regular(" y elige "),
                                        emphasis("Run 'MainKt'"),
                                        regular("."),
                                    ),
                                    richText(
                                        regular("Si no aparece la salida, abre la ventana "),
                                        emphasis("Run"),
                                        regular(" y comprueba que se ejecutó "),
                                        inlineCode("MainKt"),
                                        regular("."),
                                    ),
                                ),
                            extension =
                                richText(
                                    regular("Sustituye "),
                                    inlineCode("Hello World!"),
                                    regular(" por una presentación breve y vuelve a ejecutar el programa."),
                                ),
                            confirmation =
                                richText(
                                    regular("He ejecutado "),
                                    inlineCode("MainKt"),
                                    regular(" y la consola muestra "),
                                    inlineCode("Hello World!"),
                                    regular("."),
                                ),
                        ),
                ),
            ),
    )

private fun platformContent(select: (DevelopmentEnvironmentPlatformContent) -> LessonStepContent) = developmentEnvironmentPlatformContent.mapValues { (_, content) -> select(content) }
