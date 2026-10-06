package dev.kuromiichi.damprep.data.lessons.contents

import dev.kuromiichi.damprep.data.lessons.LessonCalloutTone
import dev.kuromiichi.damprep.data.lessons.LessonLink
import dev.kuromiichi.damprep.data.lessons.LessonStepContent
import dev.kuromiichi.damprep.data.lessons.LessonVisualContent
import dev.kuromiichi.damprep.data.lessons.callout
import dev.kuromiichi.damprep.data.lessons.emphasis
import dev.kuromiichi.damprep.data.lessons.explanationTerminal
import dev.kuromiichi.damprep.data.lessons.highlight
import dev.kuromiichi.damprep.data.lessons.inlineCode
import dev.kuromiichi.damprep.data.lessons.paragraph
import dev.kuromiichi.damprep.data.lessons.regular
import dev.kuromiichi.damprep.data.lessons.richText
import dev.kuromiichi.damprep.data.lessons.steps

internal val linuxDevelopmentEnvironmentContent =
    DevelopmentEnvironmentPlatformContent(
        jdkInstallation =
            LessonStepContent(
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
                            richText("Abre la guía oficial y añade la clave y el repositorio."),
                            richText("Actualiza el índice de paquetes."),
                            richText(regular("Instala "), inlineCode("temurin-25-jdk"), regular(" con "), inlineCode("apt"), regular(" o "), inlineCode("dnf"), regular(".")),
                            richText("Abre una terminal nueva al finalizar."),
                        ),
                        explanationTerminal(
                            note = richText(regular("Utiliza el bloque que corresponda a tu distribución.")),
                            "# Debian o Ubuntu",
                            "sudo apt update",
                            "sudo apt install temurin-25-jdk",
                            "# Fedora o RHEL",
                            "sudo dnf install temurin-25-jdk",
                            caption = "Después de añadir el repositorio",
                        ),
                        callout(
                            title = "Otras distribuciones",
                            text = richText(regular("La guía incluye openSUSE y Alpine. Si la tuya no está cubierta, usa el "), inlineCode("TAR.GZ"), regular(" oficial.")),
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
            LessonStepContent(
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
                            richText("Elige x86_64 o ARM64 según tu procesador."),
                            richText(regular("Descarga el "), inlineCode("TAR.GZ"), regular(" y no lo mezcles con una instalación anterior.")),
                            richText(regular("Extráelo en "), inlineCode("/opt"), regular(" y ejecuta "), inlineCode("idea.sh"), regular(" desde su carpeta "), inlineCode("bin"), regular(".")),
                            richText(regular("En el IDE, usa "), emphasis("Tools | Create Desktop Entry"), regular(" si quieres añadirlo al menú.")),
                        ),
                        explanationTerminal(
                            note = richText(regular("Sustituye "), inlineCode("<carpeta-extraída>"), regular(" por el directorio creado en "), inlineCode("/opt"), regular(".")),
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
            LessonStepContent(
                explanation =
                    listOf(
                        paragraph(
                            regular("Instala "),
                            highlight("Git"),
                            regular(" con el gestor de paquetes de tu distribución."),
                        ),
                        explanationTerminal(
                            note = richText(regular("Ejecuta "), inlineCode("apt"), regular(" en Debian/Ubuntu o "), inlineCode("dnf"), regular(" en Fedora; después comprueba la versión.")),
                            "# Debian o Ubuntu",
                            "sudo apt update",
                            "sudo apt install git",
                            "# Fedora",
                            "sudo dnf install git",
                            "git --version",
                            caption = "Instalación y comprobación",
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
