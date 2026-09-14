package dev.kuromiichi.damprep.data.lessons.contents

import dev.kuromiichi.damprep.data.lessons.LessonCatalog
import dev.kuromiichi.damprep.data.lessons.LessonCodeExample
import dev.kuromiichi.damprep.data.lessons.LessonCodeToken
import dev.kuromiichi.damprep.data.lessons.LessonContent
import dev.kuromiichi.damprep.data.lessons.LessonLink
import dev.kuromiichi.damprep.data.lessons.LessonPractice
import dev.kuromiichi.damprep.data.lessons.LessonStep
import dev.kuromiichi.damprep.data.lessons.LessonVisualContent
import dev.kuromiichi.damprep.data.lessons.codeLine
import dev.kuromiichi.damprep.data.lessons.emphasis
import dev.kuromiichi.damprep.data.lessons.function
import dev.kuromiichi.damprep.data.lessons.highlight
import dev.kuromiichi.damprep.data.lessons.inlineCode
import dev.kuromiichi.damprep.data.lessons.keyword
import dev.kuromiichi.damprep.data.lessons.number
import dev.kuromiichi.damprep.data.lessons.operator
import dev.kuromiichi.damprep.data.lessons.paragraph
import dev.kuromiichi.damprep.data.lessons.plain
import dev.kuromiichi.damprep.data.lessons.punctuation
import dev.kuromiichi.damprep.data.lessons.regular
import dev.kuromiichi.damprep.data.lessons.richText
import dev.kuromiichi.damprep.data.lessons.string

val firstProgramsLessonContent =
    LessonContent(
        lessonId = LessonCatalog.lessons[3].id,
        steps =
            listOf(
                LessonStep(
                    title = "Un programa empieza en main",
                    body = "El archivo Main.kt que ya creaste contiene el punto de partida del programa. Kotlin comienza por main y ejecuta lo que encuentre entre sus llaves.",
                    explanation =
                        listOf(
                            paragraph(
                                regular("El archivo "),
                                inlineCode("Main.kt"),
                                regular(" que ya creaste contiene el punto de partida del programa."),
                            ),
                            paragraph(
                                inlineCode("fun"),
                                regular(" declara una función y "),
                                inlineCode("main"),
                                regular(" le da el nombre especial que Kotlin reconoce como punto de entrada. Las llaves agrupan sus instrucciones."),
                            ),
                        ),
                    visual =
                        LessonVisualContent.CodeExamples(
                            examples =
                                listOf(
                                    LessonCodeExample(
                                        title = "La estructura mínima",
                                        lines =
                                            listOf(
                                                codeLine(keyword("fun "), function("main"), punctuation("() {")),
                                                codeLine(punctuation("}")),
                                            ),
                                        note = "Dentro de las llaves escribirás las instrucciones del programa.",
                                    ),
                                ),
                        ),
                ),
                LessonStep(
                    title = "Instrucciones en orden",
                    body = "Las instrucciones se leen de arriba abajo. println muestra un texto en la consola y después el programa continúa con la siguiente línea.",
                    explanation =
                        listOf(
                            paragraph(
                                regular("Las instrucciones se leen "),
                                emphasis("de arriba abajo"),
                                regular(". Ese orden es parte del significado del programa."),
                            ),
                            paragraph(
                                inlineCode("println"),
                                regular(" muestra un texto en la consola y después el programa continúa con la siguiente línea."),
                            ),
                        ),
                    visual =
                        LessonVisualContent.CodeExamples(
                            examples =
                                listOf(
                                    LessonCodeExample(
                                        title = "Dos salidas consecutivas",
                                        lines =
                                            listOf(
                                                codeLine(keyword("fun "), function("main"), punctuation("() {")),
                                                codeLine(plain("    "), function("println"), punctuation("("), string("\"Empiezo a estudiar.\""), punctuation(")")),
                                                codeLine(plain("    "), function("println"), punctuation("("), string("\"Termino este bloque.\""), punctuation(")")),
                                                codeLine(punctuation("}")),
                                            ),
                                        note = "Primero aparece el primer mensaje; después, el segundo.",
                                    ),
                                ),
                        ),
                ),
                LessonStep(
                    title = "Comentarios para explicar una decisión",
                    body = "Los comentarios sirven para dejar una aclaración a quien lea el código. Kotlin ofrece // para una línea y /* */ para varias líneas; el programa los ignora.",
                    explanation =
                        listOf(
                            paragraph(
                                regular("Un comentario deja una aclaración a quien lea el código. El programa lo ignora, por eso conviene que explique una decisión útil."),
                            ),
                            paragraph(
                                inlineCode("//"),
                                regular(" comenta una línea y "),
                                inlineCode("/* */"),
                                regular(" permite abarcar varias líneas."),
                            ),
                        ),
                    visual =
                        LessonVisualContent.CodeExamples(
                            examples =
                                listOf(
                                    LessonCodeExample(
                                        title = "Una nota breve y otra de varias líneas",
                                        lines =
                                            listOf(
                                                codeLine(keyword("fun "), function("main"), punctuation("() {")),
                                                codeLine(plain("    "), comment("// Este mensaje abre el resumen")),
                                                codeLine(plain("    "), function("println"), punctuation("("), string("\"Resumen del día\""), punctuation(")")),
                                                codeLine(plain("    "), comment("/* Esta nota explica")),
                                                codeLine(plain("       "), comment("   el cierre. */")),
                                                codeLine(punctuation("}")),
                                            ),
                                        note = "Los comentarios acompañan al código, pero no forman parte de la salida.",
                                    ),
                                ),
                        ),
                ),
                LessonStep(
                    title = "Valores escritos en el programa",
                    body = "Un literal es un valor escrito directamente en el código. Entre comillas hay texto; sin comillas, un número. Podemos mostrar ambos con println.",
                    explanation =
                        listOf(
                            paragraph(
                                regular("Un "),
                                highlight("literal"),
                                regular(" es un valor escrito directamente en el código."),
                            ),
                            paragraph(
                                regular("Entre comillas hay texto; sin comillas, un número. Podemos mostrar ambos con "),
                                inlineCode("println"),
                                regular("."),
                            ),
                        ),
                    visual =
                        LessonVisualContent.CodeExamples(
                            examples =
                                listOf(
                                    LessonCodeExample(
                                        title = "Texto y número",
                                        lines =
                                            listOf(
                                                codeLine(keyword("fun "), function("main"), punctuation("() {")),
                                                codeLine(plain("    "), function("println"), punctuation("("), string("\"Tema completado\""), punctuation(")")),
                                                codeLine(plain("    "), function("println"), punctuation("("), number("4"), punctuation(")")),
                                                codeLine(punctuation("}")),
                                            ),
                                        note = "Las comillas distinguen el texto del número.",
                                    ),
                                ),
                        ),
                ),
                LessonStep(
                    title = "Guardar valores con val y var",
                    body = "val guarda un valor local que no cambia y es la opción recomendada. var se usa cuando ese valor necesita recibir otro durante el programa.",
                    explanation =
                        listOf(
                            paragraph(
                                inlineCode("val"),
                                regular(" guarda un valor local de solo lectura. Es la opción recomendada cuando no necesitas cambiarlo."),
                            ),
                            paragraph(
                                inlineCode("var"),
                                regular(" se usa cuando el valor necesita recibir otro durante el programa."),
                            ),
                        ),
                    visual =
                        LessonVisualContent.CodeExamples(
                            examples =
                                listOf(
                                    LessonCodeExample(
                                        title = "Nombre fijo y puntos actualizados",
                                        lines =
                                            listOf(
                                                codeLine(keyword("fun "), function("main"), punctuation("() {")),
                                                codeLine(plain("    "), keyword("val "), plain("nombre "), operator("="), plain(" "), string("\"Luna\"")),
                                                codeLine(plain("    "), keyword("var "), plain("puntos "), operator("="), plain(" "), number("0")),
                                                codeLine(plain("    "), plain("puntos "), operator("="), plain(" "), number("10")),
                                                codeLine(plain("    "), function("println"), punctuation("("), string("\""), interpolation("\$nombre"), string(": "), interpolation("\$puntos"), string("\""), punctuation(")")),
                                                codeLine(punctuation("}")),
                                            ),
                                        note = "La interpolación inserta el valor de nombre y puntos dentro del texto.",
                                    ),
                                ),
                        ),
                ),
                LessonStep(
                    title = "Une las piezas: marcador de práctica",
                    body = "Ahora combina main, una nota útil, valores y una salida. Este programa pequeño te prepara para escribir instrucciones claras antes de trabajar con datos o decisiones.",
                    explanation =
                        listOf(
                            paragraph(
                                regular("Ahora combina "),
                                inlineCode("main"),
                                regular(", una nota útil, valores y una salida. El objetivo es que cada línea tenga una responsabilidad clara."),
                            ),
                        ),
                    visual =
                        LessonVisualContent.CodeExamples(
                            examples =
                                listOf(
                                    LessonCodeExample(
                                        title = "Resumen completo",
                                        lines = studySummaryProgram(),
                                        note = "La salida exacta es: Luna: 10",
                                    ),
                                ),
                        ),
                    links =
                        listOf(
                            LessonLink(
                                label = "Sintaxis básica de Kotlin",
                                url = "https://kotlinlang.org/docs/basic-syntax.html",
                            ),
                        ),
                    practice =
                        LessonPractice(
                            id = "first-programs-study-summary",
                            objective = richText(highlight("Construir un programa breve que muestre un marcador de práctica.")),
                            statement =
                                richText(
                                    regular("En "),
                                    inlineCode("Main.kt"),
                                    regular(", escribe un único programa que muestre el resumen indicado."),
                                ),
                            requirements =
                                listOf(
                                    richText(emphasis("Una sola main"), regular(" con un comentario útil.")),
                                    richText(regular("Un "), inlineCode("val"), regular(" para el nombre y un "), inlineCode("var"), regular(" para los puntos.")),
                                    richText(regular("Inicializa los puntos en "), inlineCode("0"), regular(" y asígnales después "), inlineCode("10"), regular(".")),
                                    richText(regular("Muestra el nombre y los puntos con "), inlineCode("println"), regular(" e interpolación.")),
                                ),
                            expectedResult = richText(inlineCode("Luna: 10")),
                            hints =
                                listOf(
                                    richText(regular("Coloca las instrucciones dentro de las llaves de "), inlineCode("main"), regular(".")),
                                    richText(regular("En el texto de salida usa "), inlineCode("\$nombre"), regular(" y "), inlineCode("\$puntos"), regular(".")),
                                ),
                            extension = richText(regular("Cambia el nombre y el valor final de los puntos, manteniendo el mismo formato de salida.")),
                            confirmation = richText(regular("Confirma que la consola muestra exactamente "), inlineCode("Luna: 10"), regular(" antes de continuar.")),
                        ),
                ),
            ),
    )

private fun studySummaryProgram() =
    listOf(
        codeLine(keyword("fun "), function("main"), punctuation("() {")),
        codeLine(plain("    "), comment("// Los puntos cambiarán durante la práctica")),
        codeLine(plain("    "), keyword("val "), plain("nombre "), operator("="), plain(" "), string("\"Luna\"")),
        codeLine(plain("    "), keyword("var "), plain("puntos "), operator("="), plain(" "), number("0")),
        codeLine(plain("    "), plain("puntos "), operator("="), plain(" "), number("10")),
        codeLine(plain("    "), function("println"), punctuation("("), string("\""), interpolation("\$nombre"), string(": "), interpolation("\$puntos"), string("\""), punctuation(")")),
        codeLine(punctuation("}")),
    )

private fun comment(text: String): LessonCodeToken = LessonCodeToken(text = text, kind = dev.kuromiichi.damprep.data.lessons.LessonCodeTokenKind.Comment)

private fun interpolation(text: String): LessonCodeToken = LessonCodeToken(text = text, kind = dev.kuromiichi.damprep.data.lessons.LessonCodeTokenKind.Interpolation)
