package dev.kuromiichi.damprep.data.lessons.contents

import dev.kuromiichi.damprep.data.lessons.LessonCatalog
import dev.kuromiichi.damprep.data.lessons.LessonContent
import dev.kuromiichi.damprep.data.lessons.LessonStep
import dev.kuromiichi.damprep.data.lessons.LessonTool
import dev.kuromiichi.damprep.data.lessons.LessonToolLogo
import dev.kuromiichi.damprep.data.lessons.LessonVisualContent

val welcomeRoadmapLessonContent = LessonContent(
    lessonId = LessonCatalog.lessons[0].id,
    steps = listOf(
        LessonStep(
            title = "Bienvenida y mapa del camino",
            body = "Esta app está pensada como una tutoría guiada. Irás avanzando poco a poco, con pantallas breves y una idea importante cada vez.",
            visual = LessonVisualContent.IconText(
                title = "Ruta",
                body = "Primero entenderás el camino, después escribirás código y más adelante usarás herramientas profesionales.",
            ),
        ),
        LessonStep(
            title = "No necesitas saber programar todavía",
            body = "Empezamos desde cero. No pasa nada si aún no sabes qué es una variable, un proyecto o un error de compilación. Lo iremos construyendo paso a paso.",
            visual = LessonVisualContent.IconText(
                title = "Punto de partida",
                body = "La idea no es correr, sino entender qué estás haciendo y por qué.",
            ),
        ),
        LessonStep(
            title = "Usaremos Kotlin e IntelliJ IDEA",
            body = "Kotlin será el lenguaje principal: la forma en la que escribiremos instrucciones para el ordenador. IntelliJ IDEA será el entorno donde escribiremos, ejecutaremos y revisaremos esos programas.",
            visual = LessonVisualContent.ToolCards(
                tools = listOf(
                    LessonTool(
                        name = "Kotlin",
                        kind = "Lenguaje",
                        logo = LessonToolLogo.Kotlin,
                    ),
                    LessonTool(
                        name = "IntelliJ IDEA",
                        kind = "Entorno",
                        logo = LessonToolLogo.IntellijIdea,
                    ),
                ),
            ),
        ),
        LessonStep(
            title = "Programar es aprender a pensar con orden",
            body = "Programar no va de memorizar comandos. Va de observar un problema, dividirlo en partes pequeñas y explicar una solución con claridad.",
            visual = LessonVisualContent.IconText(
                title = "Forma de pensar",
                body = "Problema, datos, pasos y resultado. Esa estructura aparecerá una y otra vez.",
            ),
        ),
        LessonStep(
            title = "Las herramientas también cuentan",
            body = "También aprenderás a trabajar con un entorno real: ejecutar proyectos, leer errores, depurar, guardar versiones y comprobar que tu código hace lo esperado.",
            visual = LessonVisualContent.IconText(
                title = "Herramientas",
                body = "El editor, el compilador, Git y las pruebas serán parte natural del aprendizaje.",
            ),
        ),
        LessonStep(
            title = "Cómo avanzar",
            body = "Lee cada pantalla con calma. Más adelante, cada tema tendrá actividades para confirmar que puedes aplicar lo aprendido antes de continuar.",
            visual = LessonVisualContent.IconText(
                title = "Progreso",
                body = "Cada paso debe dejarte un poco más situado que el anterior.",
            ),
        ),
    ),
)
