package dev.kuromiichi.damprep.data.lessons.contents

import dev.kuromiichi.damprep.data.lessons.LessonContent
import dev.kuromiichi.damprep.data.lessons.LessonStep
import dev.kuromiichi.damprep.data.lessons.LessonTool
import dev.kuromiichi.damprep.data.lessons.LessonToolLogo
import dev.kuromiichi.damprep.data.lessons.LessonVisualContent
import dev.kuromiichi.damprep.data.lessons.paragraphs

val welcomeRoadmapLessonContent =
    LessonContent(
        lessonId = "welcome-roadmap",
        steps =
            listOf(
                LessonStep(
                    title = "Bienvenida y mapa del camino",
                    explanation =
                        paragraphs(
                            "Esta app está pensada como una tutoría guiada.",
                            "Irás avanzando poco a poco, con pantallas breves y una idea importante cada vez.",
                        ),
                    visual =
                        LessonVisualContent.IconText(
                            title = "Ruta",
                            body = "Primero entenderás el camino, después escribirás código y más adelante usarás herramientas profesionales.",
                        ),
                ),
                LessonStep(
                    title = "No necesitas saber programar todavía",
                    explanation =
                        paragraphs(
                            "Empezamos desde cero. No pasa nada si aún no sabes qué es una variable, un proyecto o un error de compilación.",
                            "Lo iremos construyendo paso a paso.",
                        ),
                    visual =
                        LessonVisualContent.IconText(
                            title = "Punto de partida",
                            body = "La idea no es correr, sino entender qué estás haciendo y por qué.",
                        ),
                ),
                LessonStep(
                    title = "Usaremos Kotlin e IntelliJ IDEA",
                    explanation =
                        paragraphs(
                            "Kotlin será el lenguaje principal: la forma en la que escribiremos instrucciones para el ordenador.",
                            "IntelliJ IDEA será el entorno donde escribiremos, ejecutaremos y revisaremos esos programas.",
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
                                        kind = "Entorno",
                                        logo = LessonToolLogo.IntellijIdea,
                                    ),
                                ),
                        ),
                ),
                LessonStep(
                    title = "Programar es aprender a pensar con orden",
                    explanation =
                        paragraphs(
                            "Programar no va de memorizar comandos.",
                            "Va de observar un problema, dividirlo en partes pequeñas y explicar una solución con claridad.",
                        ),
                    visual =
                        LessonVisualContent.IconText(
                            title = "Forma de pensar",
                            body = "Problema, datos, pasos y resultado. Esa estructura aparecerá una y otra vez.",
                        ),
                ),
                LessonStep(
                    title = "Las herramientas también cuentan",
                    explanation =
                        paragraphs(
                            "También aprenderás a trabajar con un entorno real.",
                            "Ejecutarás proyectos, leerás errores, depurarás, guardarás versiones y comprobarás que tu código hace lo esperado.",
                        ),
                    visual =
                        LessonVisualContent.IconText(
                            title = "Herramientas",
                            body = "El editor, el compilador, Git y las pruebas serán parte natural del aprendizaje.",
                        ),
                ),
                LessonStep(
                    title = "Cómo avanzar",
                    explanation =
                        paragraphs(
                            "Lee cada pantalla con calma.",
                            "Muchas lecciones terminan con una práctica guiada: te propone un pequeño programa y te pide confirmar que funciona antes de continuar.",
                        ),
                    visual =
                        LessonVisualContent.IconText(
                            title = "Progreso",
                            body = "Cada paso debe dejarte un poco más situado que el anterior.",
                        ),
                ),
            ),
    )
