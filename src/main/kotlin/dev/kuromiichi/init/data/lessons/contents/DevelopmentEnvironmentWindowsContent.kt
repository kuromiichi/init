package dev.kuromiichi.init.data.lessons.contents

import dev.kuromiichi.init.data.lessons.LessonCalloutTone
import dev.kuromiichi.init.data.lessons.LessonLink
import dev.kuromiichi.init.data.lessons.LessonStepContent
import dev.kuromiichi.init.data.lessons.LessonVisualContent
import dev.kuromiichi.init.data.lessons.callout
import dev.kuromiichi.init.data.lessons.emphasis
import dev.kuromiichi.init.data.lessons.explanationTerminal
import dev.kuromiichi.init.data.lessons.highlight
import dev.kuromiichi.init.data.lessons.inlineCode
import dev.kuromiichi.init.data.lessons.paragraph
import dev.kuromiichi.init.data.lessons.regular
import dev.kuromiichi.init.data.lessons.richText
import dev.kuromiichi.init.data.lessons.steps

internal val windowsDevelopmentEnvironmentContent =
    DevelopmentEnvironmentPlatformContent(
        jdkInstallation =
            LessonStepContent(
                explanation =
                    listOf(
                        paragraph(
                            regular("Para "),
                            emphasis("Windows"),
                            regular(" utilizaremos "),
                            highlight("Eclipse Temurin 25"),
                            regular(" en formato "),
                            inlineCode("MSI"),
                            regular("."),
                        ),
                        steps(
                            richText("Descarga el paquete JDK 25 para Windows x64."),
                            richText(regular("Abre el "), inlineCode("MSI"), regular(", acepta la licencia y conserva la ruta propuesta.")),
                            richText(regular("En "), emphasis("Custom Setup"), regular(", deja activada la opción que añade Java al "), inlineCode("PATH"), regular(" y activa también la que configura "), inlineCode("JAVA_HOME"), regular(".")),
                            richText("Finaliza y cierra las terminales que estuvieran abiertas."),
                        ),
                        callout(
                            title = "Comprueba el paquete",
                            text = richText(regular("Elige JDK, no JRE: necesitamos también el compilador "), inlineCode("javac"), regular(".")),
                            tone = LessonCalloutTone.Warning,
                        ),
                    ),
                visual =
                    LessonVisualContent.Flow(
                        nodes =
                            listOf(
                                "JDK 25 · x64",
                                "Instalador MSI",
                                "PATH",
                                "JAVA_HOME",
                                "Finalizar",
                            ),
                    ),
                links =
                    listOf(
                        LessonLink(
                            label = "Descargar Temurin 25 para Windows",
                            url = "https://adoptium.net/temurin/releases/?version=25&os=windows&arch=x64&package=jdk",
                        ),
                        LessonLink(
                            label = "Seguir la instalación MSI oficial",
                            url = "https://adoptium.net/installation/windows/",
                        ),
                    ),
            ),
        intellijInstallation =
            LessonStepContent(
                explanation =
                    listOf(
                        paragraph(
                            regular("Instala "),
                            highlight("IntelliJ IDEA"),
                            regular(" con el instalador "),
                            inlineCode("EXE"),
                            regular(" oficial de Windows."),
                        ),
                        steps(
                            richText(regular("Descarga el "), inlineCode("EXE"), regular(" x64, salvo que tu equipo use ARM64.")),
                            richText(regular("Ejecuta el asistente hasta llegar a "), emphasis("Installation Options"), regular(".")),
                            richText(regular("Activa "), emphasis("Open Folder as Project"), regular("; añadir los lanzadores al "), inlineCode("PATH"), regular(" es opcional.")),
                            richText(regular("Termina la instalación y abre IntelliJ IDEA desde "), emphasis("Inicio"), regular(".")),
                        ),
                        callout(
                            title = "Dos runtimes distintos",
                            text = "IntelliJ incluye Java para arrancar el IDE, pero el proyecto seguirá usando el JDK 25 instalado antes.",
                        ),
                    ),
                visual =
                    LessonVisualContent.Flow(
                        nodes =
                            listOf(
                                "Descargar EXE",
                                "Ejecutar asistente",
                                "Open Folder as Project",
                                "Instalar",
                                "Abrir IntelliJ",
                            ),
                    ),
                links =
                    listOf(
                        LessonLink(
                            label = "Descargar IntelliJ IDEA para Windows",
                            url = "https://www.jetbrains.com/idea/download/?section=windows",
                        ),
                        LessonLink(
                            label = "Consultar la guía oficial de instalación",
                            url = "https://www.jetbrains.com/help/idea/installation-guide.html#windows",
                        ),
                    ),
            ),
        gitInstallation =
            LessonStepContent(
                explanation =
                    listOf(
                        paragraph(
                            highlight("Git for Windows"),
                            regular(" proporciona Git y su integración con la terminal."),
                        ),
                        steps(
                            richText("Descarga Git for Windows y abre el instalador."),
                            richText("Conserva las opciones recomendadas, incluida la línea de comandos."),
                            richText("Finaliza y abre PowerShell, Windows Terminal o Git Bash."),
                        ),
                        explanationTerminal(
                            note = richText(regular("Debe responder con una versión de Git.")),
                            "git --version",
                            caption = "Comprueba Git",
                        ),
                    ),
                visual =
                    LessonVisualContent.Flow(
                        nodes = listOf("Git for Windows", "Instalador", "Nueva terminal", "Git disponible"),
                    ),
                links =
                    listOf(
                        LessonLink(
                            label = "Descargar Git for Windows",
                            url = "https://git-scm.com/download/win",
                        ),
                        LessonLink(
                            label = "Consultar la instalación oficial de Git",
                            url = "https://git-scm.com/book/es/v2/Inicio---Sobre-el-Control-de-Versiones-Instalaci%C3%B3n-de-Git",
                        ),
                    ),
            ),
    )
