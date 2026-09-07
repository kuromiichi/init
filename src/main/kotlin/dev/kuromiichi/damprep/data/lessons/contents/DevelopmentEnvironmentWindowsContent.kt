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

internal val windowsDevelopmentEnvironmentContent = DevelopmentEnvironmentPlatformContent(
    jdkInstallation = structuredStepContent(
        explanation = listOf(
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
                "Descarga el paquete JDK 25 para Windows x64.",
                "Abre el MSI, acepta la licencia y conserva la ruta propuesta.",
                "En Custom Setup deja activado PATH y activa Set JAVA_HOME.",
                "Finaliza y cierra las terminales que estuvieran abiertas.",
            ),
            callout(
                title = "Comprueba el paquete",
                text = "Elige JDK, no JRE: necesitamos también el compilador javac.",
                tone = LessonCalloutTone.Warning,
            ),
        ),
        visual = LessonVisualContent.Flow(
            nodes = listOf(
                "JDK 25 · x64",
                "Instalador MSI",
                "PATH",
                "JAVA_HOME",
                "Finalizar",
            ),
        ),
        links = listOf(
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
    intellijInstallation = structuredStepContent(
        explanation = listOf(
            paragraph(
                regular("Instala "),
                highlight("IntelliJ IDEA"),
                regular(" con el instalador "),
                inlineCode("EXE"),
                regular(" oficial de Windows."),
            ),
            steps(
                "Descarga el EXE x64, salvo que tu equipo use ARM64.",
                "Ejecuta el asistente y abre Installation Options.",
                "Activa Open Folder as Project; añadir el lanzador al PATH es opcional.",
                "Termina la instalación y abre IntelliJ IDEA desde Inicio.",
            ),
            callout(
                title = "Dos runtimes distintos",
                text = "IntelliJ incluye Java para arrancar el IDE, pero el proyecto seguirá usando el JDK 25 instalado antes.",
            ),
        ),
        visual = LessonVisualContent.Flow(
            nodes = listOf(
                "Descargar EXE",
                "Ejecutar asistente",
                "Open Folder as Project",
                "Instalar",
                "Abrir IntelliJ",
            ),
        ),
        links = listOf(
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
    gitInstallation = structuredStepContent(
        explanation = listOf(
            paragraph(
                highlight("Git for Windows"),
                regular(" proporciona Git y su integración con la terminal."),
            ),
            steps(
                "Descarga Git for Windows y abre el instalador.",
                "Conserva las opciones recomendadas, incluida la línea de comandos.",
                "Finaliza y abre PowerShell, Windows Terminal o Git Bash.",
            ),
            explanationCode(
                title = "Comprueba Git",
                note = "Debe responder con una versión de Git.",
                "git --version",
            ),
        ),
        visual = LessonVisualContent.Flow(
            nodes = listOf("Git for Windows", "Instalador", "Nueva terminal", "Git disponible"),
        ),
        links = listOf(
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
