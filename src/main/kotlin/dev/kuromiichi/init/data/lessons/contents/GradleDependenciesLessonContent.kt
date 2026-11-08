package dev.kuromiichi.init.data.lessons.contents

import dev.kuromiichi.init.data.lessons.LessonCalloutTone
import dev.kuromiichi.init.data.lessons.LessonCodeExample
import dev.kuromiichi.init.data.lessons.LessonConsoleKind
import dev.kuromiichi.init.data.lessons.LessonContent
import dev.kuromiichi.init.data.lessons.LessonLink
import dev.kuromiichi.init.data.lessons.LessonPractice
import dev.kuromiichi.init.data.lessons.LessonStep
import dev.kuromiichi.init.data.lessons.LessonStepContent
import dev.kuromiichi.init.data.lessons.LessonTextSpan
import dev.kuromiichi.init.data.lessons.LessonVisualContent
import dev.kuromiichi.init.data.lessons.callout
import dev.kuromiichi.init.data.lessons.consoleError
import dev.kuromiichi.init.data.lessons.consoleInput
import dev.kuromiichi.init.data.lessons.consoleOutput
import dev.kuromiichi.init.data.lessons.consoleRun
import dev.kuromiichi.init.data.lessons.emphasis
import dev.kuromiichi.init.data.lessons.explanationCode
import dev.kuromiichi.init.data.lessons.highlight
import dev.kuromiichi.init.data.lessons.inlineCode
import dev.kuromiichi.init.data.lessons.kotlinCode
import dev.kuromiichi.init.data.lessons.operatingSystemStep
import dev.kuromiichi.init.data.lessons.paragraph
import dev.kuromiichi.init.data.lessons.regular
import dev.kuromiichi.init.data.lessons.richText
import dev.kuromiichi.init.data.lessons.steps
import dev.kuromiichi.init.data.preferences.TargetOperatingSystem

/** Cómo se llama al Wrapper de Gradle desde la terminal de cada sistema. */
private fun gradlew(system: TargetOperatingSystem?): String = if (system == TargetOperatingSystem.Windows) ".\\gradlew.bat" else "./gradlew"

/** Aclaración para la variante sin sistema elegido, que muestra la forma de macOS y Linux. */
private fun windowsNote(
    system: TargetOperatingSystem?,
    arguments: String,
): Array<LessonTextSpan> =
    if (system == null) {
        arrayOf(regular(" ("), inlineCode(".\\gradlew.bat $arguments"), regular(" en Windows)"))
    } else {
        emptyArray()
    }

val gradleDependenciesLessonContent =
    LessonContent(
        lessonId = "gradle-dependencies",
        steps =
            listOf(
                LessonStep(
                    title = "Construir un proyecto",
                    explanation =
                        listOf(
                            paragraph(regular("Desde «Preparar el entorno de trabajo» usas "), highlight("Gradle"), regular(" sin mirarlo: compila tu código, ejecuta las pruebas y prepara el programa. Una "), highlight("herramienta de construcción"), regular(" hace siempre los mismos pasos, en el mismo orden, en cualquier ordenador.")),
                            paragraph(regular("En esta lección leerás su configuración, lanzarás sus tareas desde la terminal y añadirás a tu proyecto "), highlight("bibliotecas"), regular(" escritas por otras personas, que Gradle descarga por ti.")),
                            paragraph(regular("En Java también es muy común "), emphasis("Maven"), regular(", que se configura con un "), inlineCode("pom.xml"), regular(". Las ideas de esta lección, como repositorios, coordenadas y versiones, son las mismas en los dos.")),
                        ),
                    visual = LessonVisualContent.Flow(listOf("Leer la configuración", "Descargar bibliotecas", "Compilar", "Probar", "Empaquetar")),
                    links =
                        listOf(
                            LessonLink("Qué es Maven", "https://maven.apache.org/what-is-maven.html"),
                        ),
                ),
                LessonStep(
                    title = "Los archivos de Gradle",
                    explanation =
                        listOf(
                            paragraph(regular("El proyecto que creó IntelliJ IDEA tiene estos archivos en su carpeta raíz:")),
                            steps(
                                richText(inlineCode("settings.gradle.kts"), regular(": el nombre del proyecto.")),
                                richText(inlineCode("build.gradle.kts"), regular(": cómo se construye y qué bibliotecas usa. Es el que más tocarás.")),
                                richText(inlineCode("gradlew"), regular(", "), inlineCode("gradlew.bat"), regular(" y "), inlineCode("gradle/wrapper"), regular(": el Wrapper, que lanza la versión correcta de Gradle.")),
                                richText(inlineCode(".gradle"), regular(" y "), inlineCode("build"), regular(": lo que genera Gradle al trabajar, como las clases compiladas.")),
                            ),
                            paragraph(regular("Los cuatro primeros se guardan en Git; "), inlineCode(".gradle"), regular(" y "), inlineCode("build"), regular(" no, porque se pueden regenerar. Es lo que dejaste escrito en el "), inlineCode(".gitignore"), regular(" en la lección de control de versiones.")),
                            paragraph(regular("La extensión "), inlineCode(".kts"), regular(" significa "), emphasis("Kotlin script"), regular(": la configuración se escribe en Kotlin, así que ya sabes leerla.")),
                        ),
                    visual =
                        LessonVisualContent.CodeExamples(
                            listOf(
                                LessonCodeExample(
                                    title = "El nombre del proyecto",
                                    lines =
                                        kotlinCode(
                                            """
                                            rootProject.name = "Agenda"
                                            """.trimIndent(),
                                        ),
                                    note = richText(regular("Es todo lo que necesita un proyecto sencillo. El nombre aparece en IntelliJ IDEA y en los archivos que genera Gradle.")),
                                    fileName = "settings.gradle.kts",
                                ),
                            ),
                        ),
                    links =
                        listOf(
                            LessonLink("Archivos de un proyecto Gradle", "https://docs.gradle.org/current/userguide/gradle_directories.html"),
                        ),
                ),
                LessonStep(
                    title = "Leer build.gradle.kts",
                    explanation =
                        listOf(
                            paragraph(regular("Cada bloque es una llamada a función con una lambda, como "), inlineCode("filter { }"), regular(". De arriba abajo:")),
                            steps(
                                richText(inlineCode("plugins"), regular(": los "), highlight("plugins"), regular(" añaden capacidades a Gradle. "), inlineCode("kotlin(\"jvm\")"), regular(" le enseña a compilar Kotlin, en la versión indicada.")),
                                richText(inlineCode("group"), regular(" y "), inlineCode("version"), regular(": identifican tu proyecto si algún día lo publicas.")),
                                richText(inlineCode("repositories"), regular(": dónde buscar bibliotecas. "), inlineCode("mavenCentral()"), regular(" es el repositorio público más usado.")),
                                richText(inlineCode("dependencies"), regular(": qué bibliotecas usa el proyecto.")),
                                richText(inlineCode("kotlin { jvmToolchain(25) }"), regular(": con qué JDK se compila.")),
                                richText(inlineCode("tasks.test"), regular(": cómo se ejecutan las pruebas.")),
                            ),
                        ),
                    visual =
                        LessonVisualContent.CodeExamples(
                            listOf(
                                LessonCodeExample(
                                    title = "La configuración de la plantilla",
                                    lines =
                                        kotlinCode(
                                            """
                                            plugins {
                                                kotlin("jvm") version "2.4.0"
                                            }

                                            group = "org.example"
                                            version = "1.0-SNAPSHOT"

                                            repositories {
                                                mavenCentral()
                                            }

                                            dependencies {
                                                testImplementation(kotlin("test"))
                                            }

                                            kotlin {
                                                jvmToolchain(25)
                                            }

                                            tasks.test {
                                                useJUnitPlatform()
                                            }
                                            """.trimIndent(),
                                        ),
                                    note = richText(regular("Las versiones de tu proyecto pueden ser otras: dependen de cuándo lo creaste. "), inlineCode("SNAPSHOT"), regular(" indica una versión en desarrollo.")),
                                    fileName = "build.gradle.kts",
                                ),
                            ),
                        ),
                    links =
                        listOf(
                            LessonLink("Conceptos básicos del archivo de construcción", "https://docs.gradle.org/current/userguide/build_file_basics.html"),
                            LessonLink("Configurar un proyecto Kotlin con Gradle", "https://kotlinlang.org/docs/gradle-configure-project.html"),
                        ),
                ),
                operatingSystemStep("Tareas") { system ->
                    val gradle = gradlew(system)
                    LessonStepContent(
                        explanation =
                            listOf(
                                paragraph(regular("Gradle trabaja con "), highlight("tareas"), regular(": "), inlineCode("compileKotlin"), regular(" compila, "), inlineCode("test"), regular(" prueba, "), inlineCode("jar"), regular(" empaqueta y "), inlineCode("clean"), regular(" borra la carpeta "), inlineCode("build"), regular(". "), inlineCode("$gradle tasks"), *windowsNote(system, "tasks"), regular(" las lista todas.")),
                                paragraph(regular("Unas tareas dependen de otras: "), inlineCode("build"), regular(" necesita "), inlineCode("jar"), regular(" y "), inlineCode("test"), regular(", que a su vez necesitan el código compilado. Pides la última y Gradle ejecuta las anteriores en orden.")),
                                paragraph(regular("Con "), inlineCode("--console=plain"), regular(" ves cada tarea. Si nada ha cambiado desde la última vez, Gradle marca la tarea como "), inlineCode("UP-TO-DATE"), regular(" y se la salta: por eso la segunda construcción es mucho más rápida.")),
                                paragraph(regular("En IntelliJ IDEA tienes las mismas tareas en la ventana "), emphasis("Gradle"), regular(", el icono del elefante del lateral derecho: haz doble clic en una para ejecutarla.")),
                            ),
                        visual =
                            LessonVisualContent.Console(
                                kind = LessonConsoleKind.Terminal,
                                runs =
                                    listOf(
                                        consoleRun(
                                            consoleInput("$gradle build --console=plain"),
                                            consoleOutput("> Task :compileKotlin UP-TO-DATE"),
                                            consoleOutput("> Task :jar UP-TO-DATE"),
                                            consoleOutput("> Task :assemble UP-TO-DATE"),
                                            consoleOutput("> Task :compileTestKotlin UP-TO-DATE"),
                                            consoleOutput("> Task :test UP-TO-DATE"),
                                            consoleOutput("> Task :check UP-TO-DATE"),
                                            consoleOutput("> Task :build UP-TO-DATE"),
                                            consoleOutput(""),
                                            consoleOutput("BUILD SUCCESSFUL in 311ms"),
                                            consoleOutput("4 actionable tasks: 4 up-to-date"),
                                        ),
                                    ),
                                caption = "Una segunda construcción sin cambios, resumida: todas las tareas están al día.",
                            ),
                        links =
                            listOf(
                                LessonLink("Gradle desde la línea de comandos", "https://docs.gradle.org/current/userguide/command_line_interface.html"),
                                LessonLink("Tareas de Gradle en IntelliJ IDEA", "https://www.jetbrains.com/help/idea/work-with-gradle-tasks.html"),
                                LessonLink("Construcción incremental", "https://docs.gradle.org/current/userguide/incremental_build.html"),
                            ),
                    )
                },
                LessonStep(
                    title = "El Wrapper",
                    explanation =
                        listOf(
                            paragraph(regular("No instalaste Gradle: el "), highlight("Wrapper"), regular(" ("), inlineCode("gradlew"), regular(") lo descarga la primera vez y lo reutiliza después. La versión está fijada en un archivo de "), inlineCode("gradle/wrapper"), regular(".")),
                            explanationCode(
                                "gradle-wrapper.properties",
                                richText(regular("La línea que importa. El resto de líneas indican dónde guardarlo y cuánto esperar a la descarga.")),
                                "distributionUrl=https\\://services.gradle.org/distributions/gradle-9.7.1-bin.zip",
                            ),
                            paragraph(regular("Como el Wrapper está en Git, cualquiera que clone el proyecto construye con la misma versión de Gradle que tú, sin instalar nada. Si un día hay que cambiarla, se cambia en un solo sitio y le llega a todo el equipo.")),
                        ),
                    visual = LessonVisualContent.Flow(listOf("Ejecutas gradlew", "¿Tienes esa versión?", "Si no, la descarga", "Construye el proyecto")),
                    links =
                        listOf(
                            LessonLink("El Gradle Wrapper", "https://docs.gradle.org/current/userguide/gradle_wrapper.html"),
                        ),
                ),
                LessonStep(
                    title = "Qué es una dependencia",
                    explanation =
                        listOf(
                            paragraph(regular("Una "), highlight("dependencia"), regular(" es una biblioteca que tu proyecto necesita para compilar o funcionar. En lugar de copiar archivos "), inlineCode(".jar"), regular(" a mano, la declaras y Gradle la busca.")),
                            paragraph(regular("Cada biblioteca se identifica con tres "), highlight("coordenadas"), regular(" separadas por dos puntos, por ejemplo "), inlineCode("org.jetbrains.kotlinx:kotlinx-datetime:0.8.0"), regular(":")),
                            steps(
                                richText(emphasis("Grupo"), regular(", "), inlineCode("org.jetbrains.kotlinx"), regular(": quién la publica.")),
                                richText(emphasis("Artefacto"), regular(", "), inlineCode("kotlinx-datetime"), regular(": qué biblioteca es.")),
                                richText(emphasis("Versión"), regular(", "), inlineCode("0.8.0"), regular(": cuál de sus publicaciones.")),
                            ),
                            paragraph(regular("Gradle la descarga del repositorio, la guarda en una caché de tu carpeta personal y la reutiliza en todos tus proyectos. Por eso la primera construcción necesita conexión y tarda más.")),
                        ),
                    visual = LessonVisualContent.Flow(listOf("dependencies en build.gradle.kts", "Maven Central", "Caché de tu ordenador", "Tu programa la usa")),
                    links =
                        listOf(
                            LessonLink("Declarar dependencias", "https://docs.gradle.org/current/userguide/declaring_dependencies.html"),
                            LessonLink("Buscar bibliotecas en Maven Central", "https://central.sonatype.com/"),
                        ),
                ),
                LessonStep(
                    title = "implementation y testImplementation",
                    explanation =
                        listOf(
                            paragraph(regular("La palabra que va delante de las coordenadas es la "), highlight("configuración"), regular(": decide qué parte del proyecto ve la biblioteca.")),
                            steps(
                                richText(inlineCode("implementation"), regular(": la usa tu programa, en "), inlineCode("src/main"), regular(". También la ven las pruebas.")),
                                richText(inlineCode("testImplementation"), regular(": solo existe para las pruebas, en "), inlineCode("src/test"), regular(". No viaja con tu programa.")),
                            ),
                            paragraph(regular("Así, "), inlineCode("kotlin.test"), regular(" no puede colarse en el código del programa por error: si lo intentas, no compila. "), inlineCode("kotlin(\"test\")"), regular(" es una forma corta de escribir las coordenadas de esa biblioteca.")),
                        ),
                    visual =
                        LessonVisualContent.CodeExamples(
                            listOf(
                                LessonCodeExample(
                                    title = "Una biblioteca para el programa y otra para las pruebas",
                                    lines =
                                        kotlinCode(
                                            """
                                            dependencies {
                                                implementation(
                                                    "org.jetbrains.kotlinx:kotlinx-datetime:0.8.0",
                                                )
                                                testImplementation(kotlin("test"))
                                            }
                                            """.trimIndent(),
                                        ),
                                    note = richText(regular("Las fechas sirven en "), inlineCode("src/main"), regular(" y en "), inlineCode("src/test"), regular("; "), inlineCode("kotlin.test"), regular(", solo en "), inlineCode("src/test"), regular(".")),
                                    fileName = "build.gradle.kts",
                                ),
                                LessonCodeExample(
                                    title = "Usar la biblioteca de pruebas en el programa",
                                    lines =
                                        kotlinCode(
                                            """
                                            import kotlin.⟦test⟧.assertEquals
                                            """.trimIndent(),
                                        ),
                                    note = richText(regular("En "), inlineCode("src/main"), regular(" no compila: "), inlineCode("Unresolved reference 'test'"), regular(". Para "), inlineCode("main"), regular(", esa biblioteca no existe.")),
                                ),
                            ),
                        ),
                    links =
                        listOf(
                            LessonLink("Configuraciones de dependencias", "https://docs.gradle.org/current/userguide/dependency_configurations.html"),
                        ),
                ),
                LessonStep(
                    title = "Añadir una biblioteca",
                    explanation =
                        listOf(
                            paragraph(regular("Kotlin no trae un tipo para fechas sin hora. La biblioteca "), inlineCode("kotlinx-datetime"), regular(" sí, y sabe contar los días entre dos fechas sin que tengas que pensar en meses de 30 o 31 días ni en años bisiestos.")),
                            steps(
                                richText(regular("Busca en la documentación de la biblioteca, o en Maven Central, sus coordenadas y la última versión estable.")),
                                richText(regular("Añade la línea con "), inlineCode("implementation"), regular(" dentro de "), inlineCode("dependencies"), regular(".")),
                                richText(regular("Pulsa el botón flotante del elefante, "), emphasis("Load Gradle Changes"), regular(", para que IntelliJ IDEA descargue la biblioteca y la reconozca.")),
                                richText(regular("Importa lo que necesites, como "), inlineCode("LocalDate"), regular(", y úsalo.")),
                            ),
                            paragraph(regular("En el ejemplo, "), inlineCode("LocalDate(2026, 11, 8)"), regular(" crea una fecha a partir de sus números y "), inlineCode("LocalDate.parse"), regular(" a partir de un texto con formato "), inlineCode("AAAA-MM-DD"), regular(". "), inlineCode("daysUntil"), regular(" cuenta los días que van de una a otra.")),
                        ),
                    visual =
                        LessonVisualContent.CodeExamples(
                            listOf(
                                LessonCodeExample(
                                    title = "Días que faltan para el examen",
                                    lines =
                                        kotlinCode(
                                            $$"""
                                            import kotlinx.datetime.LocalDate
                                            import kotlinx.datetime.daysUntil

                                            fun main() {
                                                val hoy = LocalDate(2026, 11, 8)
                                                val examen =
                                                    LocalDate.parse("2026-12-15")

                                                val dias = hoy.daysUntil(examen)
                                                println("Examen en $dias días")
                                            }
                                            """.trimIndent(),
                                        ),
                                    note = richText(regular("Muestra "), inlineCode("Examen en 37 días"), regular(". Si la fecha ya hubiera pasado, "), inlineCode("daysUntil"), regular(" devolvería un número negativo.")),
                                ),
                            ),
                        ),
                    links =
                        listOf(
                            LessonLink("kotlinx-datetime", "https://github.com/Kotlin/kotlinx-datetime"),
                            LessonLink("Proyectos Gradle en IntelliJ IDEA", "https://www.jetbrains.com/help/idea/work-with-gradle-projects.html"),
                        ),
                ),
                operatingSystemStep("Dependencias de tus dependencias") { system ->
                    val gradle = gradlew(system)
                    LessonStepContent(
                        explanation =
                            listOf(
                                paragraph(regular("Las bibliotecas también usan otras bibliotecas. Esas "), highlight("dependencias transitivas"), regular(" llegan solas: no las escribes, pero Gradle las descarga y tu programa las necesita para funcionar.")),
                                paragraph(inlineCode("$gradle dependencies --configuration runtimeClasspath"), *windowsNote(system, "dependencies --configuration runtimeClasspath"), regular(" dibuja el árbol de lo que usa tu programa al ejecutarse. Cada nivel de sangría es una biblioteca que pide la de arriba.")),
                                paragraph(regular("Fíjate en "), inlineCode("2.1.20 -> 2.4.0"), regular(": "), inlineCode("kotlinx-datetime"), regular(" pide una versión de Kotlin más antigua que la de tu proyecto. Cuando dos piden versiones distintas de la misma biblioteca, Gradle se queda con la más alta, que sirve a ambas.")),
                            ),
                        visual =
                            LessonVisualContent.Console(
                                kind = LessonConsoleKind.Terminal,
                                runs =
                                    listOf(
                                        consoleRun(
                                            consoleInput("$gradle dependencies --configuration runtimeClasspath"),
                                            consoleOutput("runtimeClasspath - Runtime classpath of 'main'."),
                                            consoleOutput("+--- org.jetbrains.kotlin:kotlin-stdlib:2.4.0"),
                                            consoleOutput("|    \\--- org.jetbrains:annotations:13.0"),
                                            consoleOutput("\\--- org.jetbrains.kotlinx:kotlinx-datetime:0.8.0"),
                                            consoleOutput("     \\--- org.jetbrains.kotlinx:kotlinx-datetime-jvm:0.8.0"),
                                            consoleOutput("          \\--- org.jetbrains.kotlin:kotlin-stdlib:2.1.20 -> 2.4.0 (*)"),
                                            consoleOutput(""),
                                            consoleOutput("BUILD SUCCESSFUL in 323ms"),
                                        ),
                                    ),
                                caption = "Tú escribiste una dependencia; Gradle ha resuelto el resto del árbol.",
                            ),
                        links =
                            listOf(
                                LessonLink("Ver y depurar dependencias", "https://docs.gradle.org/current/userguide/viewing_debugging_dependencies.html"),
                                LessonLink("Cómo resuelve Gradle las versiones", "https://docs.gradle.org/current/userguide/dependency_resolution.html"),
                            ),
                    )
                },
                LessonStep(
                    title = "Elegir versiones",
                    explanation =
                        listOf(
                            paragraph(regular("Muchas bibliotecas numeran sus versiones con "), highlight("versionado semántico"), regular(": "), inlineCode("MAYOR.MENOR.PARCHE"), regular(". Subir el parche corrige errores, subir el menor añade funciones y subir el mayor puede romper el código que ya usaba la biblioteca.")),
                            paragraph(regular("Las versiones que empiezan por "), inlineCode("0."), regular(", como "), inlineCode("0.8.0"), regular(", avisan de que la biblioteca aún puede cambiar en cualquier versión. Y las que llevan "), inlineCode("-rc"), regular(", "), inlineCode("-beta"), regular(" o "), inlineCode("-SNAPSHOT"), regular(" todavía son pruebas.")),
                            paragraph(regular("Escribe siempre una versión concreta. Así el proyecto se construye igual hoy que dentro de un año, y actualizar es una decisión tuya: cambias el número, construyes y pasas las pruebas.")),
                            callout(
                                "Versiones que cambian solas",
                                richText(regular("Gradle acepta cosas como "), inlineCode("0.+"), regular(" o "), inlineCode("latest.release"), regular(", que toman la última versión publicada. Evítalas: el mismo código podría dejar de compilar sin que hayas tocado nada.")),
                                LessonCalloutTone.Warning,
                            ),
                        ),
                    visual = LessonVisualContent.Flow(listOf("2.4.0", "2.4.1: corrige errores", "2.5.0: añade funciones", "3.0.0: puede romper tu código")),
                    links =
                        listOf(
                            LessonLink("Versionado semántico", "https://semver.org/lang/es/"),
                            LessonLink("Declarar versiones en Gradle", "https://docs.gradle.org/current/userguide/dependency_versions.html"),
                        ),
                ),
                operatingSystemStep("Ejecutar con Gradle") { system ->
                    val gradle = gradlew(system)
                    LessonStepContent(
                        explanation =
                            listOf(
                                paragraph(regular("El plugin "), highlight("application"), regular(" añade la tarea "), inlineCode("run"), regular(", que compila y ejecuta el programa. Solo necesita saber dónde está "), inlineCode("main"), regular(".")),
                                explanationCode(
                                    "build.gradle.kts",
                                    richText(regular("Para "), inlineCode("main"), regular(" en "), inlineCode("Main.kt"), regular(", Kotlin genera la clase "), inlineCode("MainKt"), regular(".")),
                                    "plugins {",
                                    "    kotlin(\"jvm\") version \"2.4.0\"",
                                    "    application",
                                    "}",
                                    "",
                                    "application {",
                                    "    mainClass = \"MainKt\"",
                                    "}",
                                ),
                                paragraph(regular("Ahora "), inlineCode("$gradle run"), *windowsNote(system, "run"), regular(" ejecuta el programa desde la terminal, sin abrir IntelliJ IDEA.")),
                                paragraph(regular("El "), inlineCode(".jar"), regular(" de "), inlineCode("build/libs"), regular(" solo lleva tu código. "), inlineCode("installDist"), regular(" prepara en "), inlineCode("build/install"), regular(" una carpeta con el programa, todas sus bibliotecas y un script para lanzarlo.")),
                            ),
                        visual =
                            LessonVisualContent.Console(
                                kind = LessonConsoleKind.Terminal,
                                runs =
                                    listOf(
                                        consoleRun(
                                            consoleInput("$gradle run"),
                                            consoleOutput(""),
                                            consoleOutput("> Task :run"),
                                            consoleOutput("Examen en 37 días"),
                                            consoleOutput(""),
                                            consoleOutput("BUILD SUCCESSFUL in 1s"),
                                            consoleOutput("2 actionable tasks: 2 executed"),
                                        ),
                                    ),
                            ),
                        links =
                            listOf(
                                LessonLink("El plugin application", "https://docs.gradle.org/current/userguide/application_plugin.html"),
                            ),
                    )
                },
                operatingSystemStep("Cuando la construcción falla") { system ->
                    val gradle = gradlew(system)
                    LessonStepContent(
                        explanation =
                            listOf(
                                paragraph(regular("Lee primero "), inlineCode("What went wrong"), regular(". Aquí Gradle no encuentra "), inlineCode("kotlinx-datetim"), regular(" porque falta una letra, y dice dónde lo ha buscado. Las causas más habituales:")),
                                steps(
                                    richText(regular("Una errata en el grupo, el artefacto o la versión.")),
                                    richText(regular("Falta "), inlineCode("mavenCentral()"), regular(" en "), inlineCode("repositories"), regular(", así que no sabe dónde buscar.")),
                                    richText(regular("No hay conexión y la biblioteca aún no estaba en la caché.")),
                                ),
                                paragraph(regular("Si "), inlineCode("$gradle build"), *windowsNote(system, "build"), regular(" funciona pero IntelliJ IDEA marca los "), inlineCode("import"), regular(" en rojo, no has cargado los cambios: pulsa "), emphasis("Load Gradle Changes"), regular(" o "), emphasis("Sync All Gradle Projects"), regular(" en la ventana Gradle.")),
                            ),
                        visual =
                            LessonVisualContent.Console(
                                kind = LessonConsoleKind.Terminal,
                                runs =
                                    listOf(
                                        consoleRun(
                                            consoleInput("$gradle build"),
                                            consoleOutput(""),
                                            consoleError("FAILURE: Build failed with an exception."),
                                            consoleOutput(""),
                                            consoleOutput("* What went wrong:"),
                                            consoleOutput("Execution failed for task ':compileKotlin' (registered by plugin 'org.jetbrains.kotlin.jvm')."),
                                            consoleOutput("> Could not resolve all files for configuration ':compileClasspath'."),
                                            consoleOutput("   > Could not find org.jetbrains.kotlinx:kotlinx-datetim:0.8.0."),
                                            consoleOutput("     Searched in the following locations:"),
                                            consoleOutput("       - https://repo.maven.apache.org/maven2/org/jetbrains/kotlinx/kotlinx-datetim/0.8.0/kotlinx-datetim-0.8.0.pom"),
                                            consoleOutput(""),
                                            consoleError("BUILD FAILED in 998ms"),
                                        ),
                                    ),
                            ),
                        links =
                            listOf(
                                LessonLink("Resolver problemas de construcción", "https://docs.gradle.org/current/userguide/troubleshooting.html"),
                            ),
                    )
                },
                LessonStep(
                    title = "Ahora te toca: la cuenta atrás",
                    explanation =
                        listOf(
                            paragraph(regular("Vas a convertir un esbozo en un proyecto completo: una biblioteca externa, pruebas y un programa que se lanza desde la terminal.")),
                            paragraph(regular("Crea un proyecto nuevo, o usa uno que ya tengas, y copia el programa de partida en "), inlineCode("Main.kt"), regular(". Las fechas están escritas en el código para que la salida sea siempre la misma.")),
                        ),
                    visual =
                        LessonVisualContent.CodeExamples(
                            listOf(
                                LessonCodeExample(
                                    title = "La cuenta atrás",
                                    lines =
                                        kotlinCode(
                                            $$"""
                                            fun main() {
                                                val hoy = "2026-11-08"
                                                val eventos =
                                                    mapOf(
                                                        "Inicio" to "2026-09-15",
                                                        "Examen" to "2026-12-15",
                                                        "Entrega" to "2026-11-27",
                                                        "Vacaciones" to "2026-12-23",
                                                    )

                                                println("Hoy es $hoy")
                                            }
                                            """.trimIndent(),
                                        ),
                                    note = richText(regular("Por ahora solo muestra la fecha de hoy. Las fechas son textos con formato "), inlineCode("AAAA-MM-DD"), regular(".")),
                                    isCopyable = true,
                                ),
                            ),
                        ),
                    links =
                        listOf(
                            LessonLink("kotlinx-datetime", "https://github.com/Kotlin/kotlinx-datetime"),
                            LessonLink("Declarar dependencias", "https://docs.gradle.org/current/userguide/declaring_dependencies.html"),
                            LessonLink("El plugin application", "https://docs.gradle.org/current/userguide/application_plugin.html"),
                        ),
                    practice =
                        LessonPractice(
                            id = "gradle-dependencies-countdown",
                            objective = richText(highlight("Preparar un proyecto que use una biblioteca externa y se ejecute y se pruebe solo con Gradle.")),
                            statement = richText(regular("Completa la cuenta atrás: para cada evento, el programa dice cuántos días faltan o hace cuántos pasó y, al final, cuál es el evento más próximo.")),
                            requirements =
                                listOf(
                                    richText(regular("Los días se calculan con "), inlineCode("kotlinx-datetime"), regular(", en su versión "), inlineCode("0.8.0"), regular(", no contándolos a mano.")),
                                    richText(inlineCode("diasHasta(desde: LocalDate, hasta: LocalDate): Int"), regular(" devuelve los días que van de "), inlineCode("desde"), regular(" a "), inlineCode("hasta"), regular(", en negativo si "), inlineCode("hasta"), regular(" es anterior.")),
                                    richText(regular("Un evento futuro se muestra como "), inlineCode("Vacaciones: faltan 45 días"), regular(" y uno pasado como "), inlineCode("Inicio: hace 54 días"), regular(". La última línea nombra el evento futuro más cercano.")),
                                    richText(inlineCode("./gradlew run"), regular(" ("), inlineCode(".\\gradlew.bat run"), regular(" en Windows) ejecuta el programa desde la terminal.")),
                                    richText(regular("Crea "), inlineCode("AgendaTest"), regular(" con pruebas de "), inlineCode("diasHasta"), regular(" que pasen con "), inlineCode("./gradlew test"), regular(".")),
                                ),
                            expectedOutput =
                                listOf(
                                    consoleRun(
                                        consoleOutput("Inicio: hace 54 días"),
                                        consoleOutput("Examen: faltan 37 días"),
                                        consoleOutput("Entrega: faltan 19 días"),
                                        consoleOutput("Vacaciones: faltan 45 días"),
                                        consoleOutput("Lo próximo: Entrega"),
                                    ),
                                ),
                            hints =
                                listOf(
                                    richText(regular("Empieza por "), inlineCode("build.gradle.kts"), regular(": ¿en qué bloque va la biblioteca y con qué palabra delante? Repasa «Qué es una dependencia» e «implementation y testImplementation». No olvides cargar los cambios en IntelliJ IDEA.")),
                                    richText(regular("Las fechas del programa son textos. Repasa «Añadir una biblioteca»: la biblioteca sabe convertir un texto en una fecha y contar los días entre dos.")),
                                    richText(regular("Para el evento más próximo, quédate con el menor número de días que no sea negativo, como en los algoritmos de la lección de bucles.")),
                                    richText(regular("Si "), inlineCode("run"), regular(" no aparece entre las tareas, falta el plugin que la añade: repasa «Ejecutar con Gradle».")),
                                ),
                            extension = richText(regular("Mira el árbol de dependencias de tu programa: ¿qué bibliotecas han llegado sin que las escribieras? Después prepara con "), inlineCode("installDist"), regular(" una versión que se pueda lanzar sin Gradle y ejecútala con el script de "), inlineCode("build/install"), regular(".")),
                            confirmation = richText(regular("Confirma que "), inlineCode("./gradlew run"), regular(" da la salida esperada, que "), inlineCode("./gradlew test"), regular(" termina con "), inlineCode("BUILD SUCCESSFUL"), regular(" y que en "), inlineCode("src/main"), regular(" no se puede importar "), inlineCode("kotlin.test"), regular(".")),
                        ),
                ),
            ),
    )
