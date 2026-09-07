package dev.kuromiichi.damprep.data.lessons.contents

import dev.kuromiichi.damprep.data.lessons.LessonCalloutTone
import dev.kuromiichi.damprep.data.lessons.LessonLink
import dev.kuromiichi.damprep.data.lessons.LessonVisualContent
import dev.kuromiichi.damprep.data.lessons.callout
import dev.kuromiichi.damprep.data.lessons.emphasis
import dev.kuromiichi.damprep.data.lessons.explanationCode
import dev.kuromiichi.damprep.data.lessons.highlight
import dev.kuromiichi.damprep.data.lessons.inlineCode
import dev.kuromiichi.damprep.data.lessons.paragraph
import dev.kuromiichi.damprep.data.lessons.regular
import dev.kuromiichi.damprep.data.lessons.steps
import dev.kuromiichi.damprep.data.lessons.structuredStepContent

internal val linuxDevelopmentEnvironmentContent =
    DevelopmentEnvironmentPlatformContent(
        jdkInstallation =
            structuredStepContent(
                explanation =
                    listOf(
                        paragraph(
                            regular("Para "),
                            emphasis("Linux"),
                            regular(" utilizaremos "),
                            highlight("Eclipse Temurin 25"),
                            regular(" desde el repositorio de Adoptium, mediante el paquete "),
                            inlineCode("temurin-25-jdk"),
                            regular("."),
                        ),
                        steps(
                            "Abre la guía oficial y añade la clave y el repositorio.",
                            "Actualiza el índice de paquetes.",
                            "Instala temurin-25-jdk con apt o dnf.",
                            "Abre una terminal nueva al finalizar.",
                        ),
                        explanationCode(
                            title = "Después de añadir el repositorio",
                            note = "Utiliza el bloque que corresponda a tu distribución.",
                            "# Debian o Ubuntu",
                            "sudo apt update",
                            "sudo apt install temurin-25-jdk",
                            "# Fedora o RHEL",
                            "sudo dnf install temurin-25-jdk",
                        ),
                        callout(
                            title = "Otras distribuciones",
                            text = "La guía incluye openSUSE y Alpine. Si la tuya no está cubierta, usa el TAR.GZ oficial.",
                            tone = LessonCalloutTone.Information,
                        ),
                    ),
                visual =
                    LessonVisualContent.Flow(
                        nodes = listOf("Repositorio", "Actualizar", "temurin-25-jdk", "Nueva terminal"),
                    ),
                links =
                    listOf(
                        LessonLink(
                            label = "Configurar el repositorio de Adoptium",
                            url = "https://adoptium.net/installation/linux/",
                        ),
                        LessonLink(
                            label = "Descargar Temurin 25 para Linux",
                            url = "https://adoptium.net/temurin/releases/?version=25&os=linux&package=jdk",
                        ),
                    ),
            ),
        intellijInstallation =
            structuredStepContent(
                explanation =
                    listOf(
                        paragraph(
                            regular("JetBrains distribuye "),
                            highlight("IntelliJ IDEA"),
                            regular(" para Linux en formato "),
                            inlineCode("TAR.GZ"),
                            regular("."),
                        ),
                        steps(
                            "Elige x86_64 o ARM64 según tu procesador.",
                            "Descarga el TAR.GZ y no lo mezcles con una instalación anterior.",
                            "Extráelo en /opt y ejecuta idea.sh desde su carpeta bin.",
                            "En el IDE, usa Tools > Create Desktop Entry si quieres añadirlo al menú.",
                        ),
                        explanationCode(
                            title = "Terminal",
                            note = "Sustituye <carpeta-extraída> por el directorio creado en /opt.",
                            "sudo tar -xzf idea-*.tar.gz -C /opt",
                            "/opt/<carpeta-extraída>/bin/idea.sh",
                        ),
                    ),
                visual =
                    LessonVisualContent.Flow(
                        nodes = listOf("TAR.GZ", "/opt", "idea.sh", "Desktop Entry"),
                    ),
                links =
                    listOf(
                        LessonLink(
                            label = "Descargar IntelliJ IDEA para Linux",
                            url = "https://www.jetbrains.com/idea/download/?section=linux",
                        ),
                        LessonLink(
                            label = "Consultar la instalación TAR.GZ oficial",
                            url = "https://www.jetbrains.com/help/idea/installation-guide.html#standalone_linux",
                        ),
                    ),
            ),
        gitInstallation =
            structuredStepContent(
                explanation =
                    listOf(
                        paragraph(
                            regular("Instala "),
                            highlight("Git"),
                            regular(" con el gestor de paquetes de tu distribución."),
                        ),
                        explanationCode(
                            title = "Instalación y comprobación",
                            note = "Ejecuta apt en Debian/Ubuntu o dnf en Fedora; después comprueba la versión.",
                            "# Debian o Ubuntu",
                            "sudo apt update",
                            "sudo apt install git",
                            "# Fedora",
                            "sudo dnf install git",
                            "git --version",
                        ),
                        callout(
                            title = "¿Usas otra distribución?",
                            text = "Consulta la página oficial de descargas de Git para obtener el comando adecuado.",
                        ),
                    ),
                visual =
                    LessonVisualContent.Flow(
                        nodes = listOf("Gestor de paquetes", "Instalar Git", "Nueva terminal", "git --version"),
                    ),
                links =
                    listOf(
                        LessonLink(
                            label = "Ver las opciones de Git para Linux",
                            url = "https://git-scm.com/download/linux",
                        ),
                        LessonLink(
                            label = "Consultar la instalación oficial de Git",
                            url = "https://git-scm.com/book/es/v2/Inicio---Sobre-el-Control-de-Versiones-Instalaci%C3%B3n-de-Git",
                        ),
                    ),
            ),
    )
