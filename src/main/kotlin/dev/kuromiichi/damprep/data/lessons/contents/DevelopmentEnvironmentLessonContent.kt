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
import dev.kuromiichi.damprep.data.lessons.codeLine
import dev.kuromiichi.damprep.data.lessons.consoleOutput
import dev.kuromiichi.damprep.data.lessons.consoleRun
import dev.kuromiichi.damprep.data.lessons.emphasis
import dev.kuromiichi.damprep.data.lessons.function
import dev.kuromiichi.damprep.data.lessons.highlight
import dev.kuromiichi.damprep.data.lessons.inlineCode
import dev.kuromiichi.damprep.data.lessons.keyword
import dev.kuromiichi.damprep.data.lessons.paragraph
import dev.kuromiichi.damprep.data.lessons.plain
import dev.kuromiichi.damprep.data.lessons.punctuation
import dev.kuromiichi.damprep.data.lessons.regular
import dev.kuromiichi.damprep.data.lessons.richText
import dev.kuromiichi.damprep.data.lessons.string

val developmentEnvironmentLessonContent =
    LessonContent(
        lessonId = "development-environment",
        steps =
            listOf(
                LessonStep(
                    title = "Tu entorno de trabajo",
                    body = "Antes de escribir código con comodidad necesitamos preparar el lugar donde vas a trabajar.\n\nUn entorno de desarrollo es el conjunto de herramientas que te ayudan a escribir, ejecutar, revisar y corregir programas.\n\nEmpezaremos con cuatro piezas: IntelliJ IDEA, Kotlin, un JDK y Git.",
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
                    body = "IDE significa entorno de desarrollo integrado. Lo importante está en integrado: reúne herramientas que por separado serían más incómodas.\n\nEl editor colorea el código y avisa de errores. El sistema de ejecución lanza el programa y el depurador permite detenerlo para mirar valores.\n\nEl IDE no programa por ti: te da señales mientras piensas y mantiene juntos el SDK, el lenguaje y las dependencias.",
                    visual =
                        LessonVisualContent.Screenshot(
                            image = LessonScreenshot.IntellijIdeWorkspace,
                            description = "Captura de IntelliJ IDEA con el proyecto, el editor, herramientas de ejecución y panel de Git.",
                        ),
                ),
                LessonStep(
                    title = "JDK y JRE: no son lo mismo",
                    body = "Java aparece aunque escribamos Kotlin porque Kotlin/JVM se apoya en la máquina virtual de Java.\n\nUn entorno de ejecución permite arrancar programas ya preparados. El JDK añade las herramientas necesarias para desarrollarlos, entre ellas el compilador javac.\n\nDesde Java 11 el JDK ya no contiene una carpeta JRE separada. Para DAM Prep instalaremos el JDK completo y lo seleccionaremos como SDK del proyecto.",
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
                    body = "Kotlin será el idioma en el que escribiremos las instrucciones. IntelliJ IDEA será el taller donde las escribiremos.\n\nEl soporte del lenguaje permite al IDE reconocer la sintaxis, mostrar avisos, ofrecer autocompletado y conectar con el compilador.\n\nEn IntelliJ moderno viene integrado. Aun así, el proyecto debe indicar con qué versión de Kotlin va a trabajar.",
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
                    body = "Gradle es una herramienta de construcción. Lee la configuración del proyecto para saber cómo compilar, ejecutar pruebas, empaquetar y resolver dependencias.\n\nAl principio lo verás como archivos y botones de sincronización dentro de IntelliJ.\n\nNo hace falta dominarlo ahora. Su papel es evitar el trabajo manual y permitir que el proyecto se construya igual en otro ordenador.",
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
                    body = "Git no compila ni ejecuta el programa. Registra la historia del proyecto: qué cambió, cuándo cambió y por qué.\n\nEl control de versiones forma parte del entorno profesional, aunque todavía no necesites escribir comandos.\n\nMás adelante nos permitirá guardar puntos seguros, comparar cambios y trabajar sin miedo a romper algo.",
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
                    body = "Selecciona el sistema operativo que vas a utilizar para ver el paquete, la arquitectura y los pasos de instalación adecuados.",
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
                    body = "Selecciona tu sistema operativo para instalar IntelliJ IDEA mediante el formato recomendado por JetBrains.",
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
                    body = "Selecciona tu sistema operativo para instalar Git con el método apropiado y comprobar que está disponible.",
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
                    body = "No necesitas descargar Kotlin ni Gradle por separado. El soporte de Kotlin viene incluido en IntelliJ IDEA y el Gradle Wrapper descargará la versión configurada.\n\nEn New Project, elige Kotlin, Gradle, JDK 25 y Kotlin DSL. Activa Add sample code y, en Advanced Settings, comprueba que Gradle distribution utiliza Wrapper.\n\nCrea el proyecto y espera a que finalice la primera sincronización antes de ejecutar nada.",
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
                    body = "Cuando ejecutas un programa, lo pones en marcha y observas el resultado.\n\nCuando depuras, puedes detenerlo en una línea, avanzar paso a paso y mirar cuánto vale cada variable.\n\nEjecutar responde qué ha pasado. Depurar ayuda a descubrir por qué.",
                    visual =
                        LessonVisualContent.Flow(
                            nodes =
                                listOf(
                                    "Run",
                                    "Consola",
                                    "Breakpoint",
                                    "Paso a paso",
                                    "Variables",
                                ),
                        ),
                ),
                LessonStep(
                    title = "Configurar sin convertirlo en ritual",
                    body = "Los IDE tienen paneles de configuración, atajos, temas, plugins y actualizaciones. No hace falta tocarlo todo el primer día.\n\nCambia solo lo que mejora tu trabajo: el tema visual, el tamaño de letra, el SDK correcto y los plugins necesarios.\n\nDe momento buscamos una base estable: abrir, entender, ejecutar y corregir.",
                    visual =
                        LessonVisualContent.IconText(
                            title = "Base estable",
                            body = "SDK correcto, proyecto sincronizado, botón de ejecutar claro, Git disponible y errores visibles. Con eso ya se puede aprender sin pelearse con la herramienta.",
                        ),
                ),
                LessonStep(
                    title = "Comprobar el entorno con Hello World",
                    body = "IntelliJ IDEA habrá creado un pequeño programa de ejemplo. No necesitas entender todavía cómo está escrito.\n\nAbre Main.kt, pulsa el icono verde situado junto a main y ejecuta MainKt.\n\nCuando termine, comprueba que la consola muestra Hello World!.",
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
                                            listOf(
                                                codeLine(
                                                    keyword("fun "),
                                                    function("main"),
                                                    punctuation("() {"),
                                                ),
                                                codeLine(
                                                    plain("    "),
                                                    function("println"),
                                                    punctuation("("),
                                                    string("\"Hello World!\""),
                                                    punctuation(")"),
                                                ),
                                                codeLine(punctuation("}")),
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
                                        inlineCode("Run 'MainKt'"),
                                        regular("."),
                                    ),
                                    richText(
                                        regular("Si no aparece la salida, abre la ventana "),
                                        inlineCode("Run"),
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
