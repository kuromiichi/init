package dev.kuromiichi.damprep.data.lessons.contents

import dev.kuromiichi.damprep.data.lessons.LessonCalloutTone
import dev.kuromiichi.damprep.data.lessons.LessonLink
import dev.kuromiichi.damprep.data.lessons.LessonStepContent
import dev.kuromiichi.damprep.data.lessons.LessonVisualContent
import dev.kuromiichi.damprep.data.lessons.callout
import dev.kuromiichi.damprep.data.lessons.emphasis
import dev.kuromiichi.damprep.data.lessons.explanationCode
import dev.kuromiichi.damprep.data.lessons.highlight
import dev.kuromiichi.damprep.data.lessons.inlineCode
import dev.kuromiichi.damprep.data.lessons.paragraph
import dev.kuromiichi.damprep.data.lessons.regular
import dev.kuromiichi.damprep.data.lessons.richText
import dev.kuromiichi.damprep.data.lessons.steps

internal val macOsDevelopmentEnvironmentContent =
    DevelopmentEnvironmentPlatformContent(
        jdkInstallation =
            LessonStepContent(
                explanation =
                    listOf(
                        paragraph(
                            regular("Para "),
                            emphasis("macOS"),
                            regular(" utilizaremos "),
                            highlight("Eclipse Temurin 25"),
                            regular(" en formato "),
                            inlineCode("PKG"),
                            regular("."),
                        ),
                        steps(
                            richText(regular("Abre "), emphasis("Acerca de este Mac"), regular(" y comprueba el procesador.")),
                            richText(regular("Elige "), inlineCode("aarch64"), regular(" para Apple Silicon —M1 o posterior—, o "), inlineCode("x64"), regular(" para Intel.")),
                            richText(regular("Descarga el JDK 25, abre el "), inlineCode("PKG"), regular(" y acepta la licencia.")),
                            richText(regular("Conserva el destino propuesto y pulsa "), emphasis("Instalar"), regular(".")),
                        ),
                        callout(
                            title = "Destino de la instalación",
                            text = richText(regular("Temurin quedará dentro de "), inlineCode("/Library/Java/JavaVirtualMachines"), regular(".")),
                            tone = LessonCalloutTone.Tip,
                        ),
                    ),
                visual =
                    LessonVisualContent.Flow(
                        nodes =
                            listOf(
                                "Apple Silicon o Intel",
                                "JDK 25",
                                "Paquete PKG",
                                "Instalar",
                                "JavaVirtualMachines",
                            ),
                    ),
                links =
                    listOf(
                        LessonLink(
                            label = "Descargar Temurin 25 para macOS",
                            url = "https://adoptium.net/temurin/releases/?version=25&os=mac&package=jdk",
                        ),
                        LessonLink(
                            label = "Seguir la instalación PKG oficial",
                            url = "https://adoptium.net/installation/macOS/",
                        ),
                    ),
            ),
        intellijInstallation =
            LessonStepContent(
                explanation =
                    listOf(
                        paragraph(
                            regular("Descarga "),
                            highlight("IntelliJ IDEA"),
                            regular(" en formato "),
                            inlineCode("DMG"),
                            regular(" para la arquitectura de tu Mac."),
                        ),
                        steps(
                            richText("Elige Apple Silicon para M1 o posterior, o Intel para x64."),
                            richText(regular("Abre el "), inlineCode("DMG"), regular(" y arrastra IntelliJ IDEA a "), emphasis("Aplicaciones"), regular(".")),
                            richText(regular("Expulsa la imagen y abre el IDE desde "), emphasis("Aplicaciones"), regular(", Launchpad o Spotlight.")),
                        ),
                        callout(
                            title = "Primer arranque",
                            text = "El aviso de seguridad de macOS es normal si la descarga procede de JetBrains.",
                        ),
                    ),
                visual =
                    LessonVisualContent.Flow(
                        nodes =
                            listOf(
                                "Elegir arquitectura",
                                "Abrir DMG",
                                "Arrastrar a Aplicaciones",
                                "Expulsar imagen",
                                "Abrir IntelliJ",
                            ),
                    ),
                links =
                    listOf(
                        LessonLink(
                            label = "Descargar IntelliJ IDEA para macOS",
                            url = "https://www.jetbrains.com/idea/download/?section=mac",
                        ),
                        LessonLink(
                            label = "Consultar la guía oficial de instalación",
                            url = "https://www.jetbrains.com/help/idea/installation-guide.html#macos",
                        ),
                    ),
            ),
        gitInstallation =
            LessonStepContent(
                explanation =
                    listOf(
                        paragraph(
                            emphasis("macOS"),
                            regular(" puede instalar "),
                            highlight("Git"),
                            regular(" mediante las herramientas de línea de comandos de Xcode."),
                        ),
                        explanationCode(
                            title = "Terminal",
                            note = richText(regular("Si Git no existe, macOS ofrecerá instalarlo. Repite el comando cuando termine.")),
                            "git --version",
                        ),
                        callout(
                            title = "Alternativa",
                            text = "La página oficial de Git también ofrece un instalador para macOS.",
                            tone = LessonCalloutTone.Tip,
                        ),
                    ),
                visual =
                    LessonVisualContent.Flow(
                        nodes = listOf("Terminal", "git --version", "Instalar herramientas", "Nueva Terminal"),
                    ),
                links =
                    listOf(
                        LessonLink(
                            label = "Ver las opciones de Git para macOS",
                            url = "https://git-scm.com/download/mac",
                        ),
                        LessonLink(
                            label = "Consultar la instalación oficial de Git",
                            url = "https://git-scm.com/book/es/v2/Inicio---Sobre-el-Control-de-Versiones-Instalaci%C3%B3n-de-Git",
                        ),
                    ),
            ),
    )
