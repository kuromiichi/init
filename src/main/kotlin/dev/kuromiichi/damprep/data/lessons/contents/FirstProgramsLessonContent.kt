package dev.kuromiichi.damprep.data.lessons.contents

import dev.kuromiichi.damprep.data.lessons.LessonCodeExample
import dev.kuromiichi.damprep.data.lessons.LessonCodeToken
import dev.kuromiichi.damprep.data.lessons.LessonContent
import dev.kuromiichi.damprep.data.lessons.LessonLink
import dev.kuromiichi.damprep.data.lessons.LessonPractice
import dev.kuromiichi.damprep.data.lessons.LessonStep
import dev.kuromiichi.damprep.data.lessons.LessonVisualContent
import dev.kuromiichi.damprep.data.lessons.codeLine
import dev.kuromiichi.damprep.data.lessons.consoleOutput
import dev.kuromiichi.damprep.data.lessons.consoleRun
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
        lessonId = "first-programs",
        steps =
            listOf(
                LessonStep(
                    title = "Un programa empieza en main",
                    body = "Un programa Kotlin se organiza en un archivo y empieza a ejecutar desde main. Dentro de su bloque escribimos las instrucciones, con sangría para leerlas mejor.",
                    explanation =
                        listOf(
                            paragraph(
                                inlineCode("Main.kt"),
                                regular(" es un nombre habitual para el archivo que contiene este primer programa. Es solo el archivo donde guardas el código: Kotlin no exige ese nombre para encontrar "),
                                inlineCode("main"),
                                regular("."),
                            ),
                            paragraph(
                                inlineCode("fun"),
                                regular(" indica que declaramos una función. "),
                                inlineCode("main"),
                                regular(" es la función especial desde la que Kotlin inicia este programa."),
                            ),
                            paragraph(
                                regular("Los paréntesis "),
                                inlineCode("()"),
                                regular(" indican que esta versión de "),
                                inlineCode("main"),
                                regular(" no recibe ningún dato. Las llaves delimitan su bloque; la sangría de las líneas interiores no cambia el resultado, pero muestra claramente qué pertenece al bloque."),
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
                                        note = richText(regular("Dentro de las llaves escribirás las instrucciones del programa.")),
                                    ),
                                ),
                        ),
                ),
                LessonStep(
                    title = "Instrucciones en orden",
                    body = "Una instrucción ordena al programa hacer una acción. Kotlin las ejecuta de arriba abajo; println muestra información en la consola.",
                    explanation =
                        listOf(
                            paragraph(
                                regular("Una instrucción es una línea de código que pide una acción. Las instrucciones se leen "),
                                emphasis("de arriba abajo"),
                                regular(". Ese orden es parte del significado del programa."),
                            ),
                            paragraph(
                                inlineCode("println"),
                                regular(" es una función de Kotlin para mostrar algo en la consola. Al llamarla, sus paréntesis contienen el argumento: el valor que queremos mostrar."),
                            ),
                            paragraph(
                                regular("El texto que escribes entre comillas es código; lo que aparece al ejecutar es la salida. Cada "),
                                inlineCode("println"),
                                regular(" termina su salida con un salto de línea antes de continuar."),
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
                                        note = richText(regular("Primero aparece el primer mensaje; después, el segundo.")),
                                    ),
                                ),
                        ),
                ),
                LessonStep(
                    title = "Comentarios para explicar una decisión",
                    body = "Los comentarios aclaran el código para las personas. Kotlin los ignora al ejecutar: puedes usar // en una línea o /* */ en varias.",
                    explanation =
                        listOf(
                            paragraph(
                                regular("Un comentario deja una aclaración para quien lee el código. Kotlin lo ignora al ejecutar, así que nunca aparece en la consola."),
                            ),
                            paragraph(
                                inlineCode("//"),
                                regular(" comenta una línea y "),
                                inlineCode("/* */"),
                                regular(" permite abarcar varias líneas."),
                            ),
                            paragraph(
                                regular("Úsalos para explicar una intención que el código no cuenta por sí solo, como el motivo de un mensaje. Evita comentar lo obvio, por ejemplo que "),
                                inlineCode("println"),
                                regular(" imprime."),
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
                                        note = richText(regular("Los comentarios acompañan al código, pero no forman parte de la salida.")),
                                    ),
                                ),
                        ),
                ),
                LessonStep(
                    title = "Valores escritos en el programa",
                    body = "Un literal es un valor escrito directamente en el programa. Las comillas delimitan texto; un número como 4 se escribe sin ellas.",
                    explanation =
                        listOf(
                            paragraph(
                                regular("Un "),
                                highlight("literal"),
                                regular(" es un valor escrito directamente en el código, sin guardarlo antes en un nombre."),
                            ),
                            paragraph(
                                inlineCode("4"),
                                regular(" y "),
                                inlineCode("\"4\""),
                                regular(" no son el mismo literal: el primero representa un número y el segundo representa texto con el carácter 4."),
                            ),
                            paragraph(
                                regular("Las comillas delimitan el texto en el código, pero no se muestran en la consola. Puedes pasar cualquiera de estos valores como argumento a "),
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
                                                codeLine(plain("    "), function("println"), punctuation("("), string("\"Temas para repasar\""), punctuation(")")),
                                                codeLine(plain("    "), function("println"), punctuation("("), number("4"), punctuation(")")),
                                                codeLine(punctuation("}")),
                                            ),
                                        note = richText(regular("Las comillas distinguen el texto del número.")),
                                    ),
                                ),
                        ),
                ),
                LessonStep(
                    title = "Guardar valores con val y var",
                    body = "Una variable guarda un valor bajo un nombre. Empieza con val si no debe cambiar; usa var cuando necesites reasignar el valor.",
                    explanation =
                        listOf(
                            paragraph(
                                regular("Una variable tiene un nombre para poder usar un valor más de una vez. Declararla es crear ese nombre; inicializarla es darle su primer valor."),
                            ),
                            paragraph(
                                inlineCode("="),
                                regular(" asigna el valor de la derecha al nombre de la izquierda. "),
                                inlineCode("val"),
                                regular(" es la opción preferida cuando el valor no cambia."),
                            ),
                            paragraph(
                                inlineCode("var"),
                                regular(" permite reasignar: después escribes el nombre, "),
                                inlineCode("="),
                                regular(" y el valor nuevo, sin repetir "),
                                inlineCode("var"),
                                regular(". La reasignación sustituye el valor anterior."),
                            ),
                        ),
                    visual =
                        LessonVisualContent.CodeExamples(
                            examples =
                                listOf(
                                    LessonCodeExample(
                                        title = "Un curso y sus lecciones",
                                        lines =
                                            listOf(
                                                codeLine(keyword("fun "), function("main"), punctuation("() {")),
                                                codeLine(plain("    "), keyword("val "), plain("curso "), operator("="), plain(" "), string("\"Kotlin\"")),
                                                codeLine(plain("    "), keyword("var "), plain("lecciones "), operator("="), plain(" "), number("1")),
                                                codeLine(plain("    "), plain("lecciones "), operator("="), plain(" "), number("2")),
                                                codeLine(plain("    "), function("println"), punctuation("("), string("\""), interpolation("\$curso"), string(": "), interpolation("\$lecciones"), string("\""), punctuation(")")),
                                                codeLine(punctuation("}")),
                                            ),
                                        note = richText(inlineCode("curso"), regular(" no cambia; "), inlineCode("lecciones"), regular(" pasa de 1 a 2. La consola muestra "), inlineCode("Kotlin: 2"), regular(".")),
                                    ),
                                ),
                        ),
                ),
                LessonStep(
                    title = "Interpolación de variables",
                    body = "En el ejemplo anterior, el mensaje usa \$curso y \$lecciones. La interpolación inserta sus valores dentro del texto al ejecutar esa línea.",
                    explanation =
                        listOf(
                            paragraph(
                                regular("Dentro de un texto entre comillas, escribe "),
                                inlineCode("\$nombre"),
                                regular(" para interpolar la variable llamada nombre. El signo "),
                                inlineCode("\$"),
                                regular(" avisa a Kotlin de que debe insertar su valor."),
                            ),
                            paragraph(
                                regular("En este saludo, Kotlin sustituye "),
                                inlineCode("\$nombre"),
                                regular(" por el nombre guardado. Las comillas delimitan el texto en el código, pero no forman parte de la salida."),
                            ),
                            paragraph(
                                regular("Si interpolas un nombre que puede cambiar, se usa el valor que tenga al llegar a esa instrucción. Por eso el ejemplo anterior muestra 2 y no 1."),
                            ),
                        ),
                    visual =
                        LessonVisualContent.CodeExamples(
                            examples =
                                listOf(
                                    LessonCodeExample(
                                        title = "Un saludo que usa un nombre",
                                        lines = interpolatedGreetingProgram(),
                                        note = richText(regular("La consola muestra "), inlineCode("Hola, Ada"), regular(" sin el signo "), inlineCode("\$"), regular(" ni las comillas.")),
                                    ),
                                ),
                        ),
                ),
                LessonStep(
                    title = "Ahora te toca: una tarea que cambia",
                    body = "Ya conoces la estructura de un programa, la salida por consola, los comentarios, los literales, los nombres y la interpolación. Úsalos juntos en una práctica breve.",
                    explanation =
                        listOf(
                            paragraph(
                                regular("Antes de escribir, distingue el dato que permanecerá igual del que vas a cambiar."),
                            ),
                            paragraph(
                                regular("Usa el diagrama como plan: no necesitas escribirlo todo a la vez. Comprueba cada parte antes de pasar a la siguiente."),
                            ),
                            paragraph(
                                regular("Después de ejecutar, compara las dos líneas de la consola con el resultado esperado. Si algo difiere, revisa los valores y el momento en que los muestras."),
                            ),
                        ),
                    visual =
                        LessonVisualContent.Flow(
                            nodes = listOf("Preparar los datos", "Mostrar el estado inicial", "Actualizar y mostrar"),
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
                            id = "first-programs-task-status",
                            objective = richText(highlight("Construir un programa breve que muestre el cambio de estado de una tarea.")),
                            statement =
                                richText(
                                    regular("Crea un programa que registre el estado de una tarea antes y después de terminarla. Construye las líneas tú y vuelve a las pantallas anteriores si necesitas recordar cada pieza."),
                                ),
                            requirements =
                                listOf(
                                    richText(emphasis("Un único punto de entrada"), regular(" y un comentario que explique el cambio de estado.")),
                                    richText(regular("Guarda el texto fijo de la tarea "), inlineCode("Repasar Kotlin"), regular(" en un nombre que no necesite cambiar.")),
                                    richText(regular("Guarda el estado inicial "), inlineCode("pendiente"), regular(" en un nombre que sí pueda actualizarse.")),
                                    richText(regular("Muestra una primera línea con la tarea y su estado mediante interpolación.")),
                                    richText(regular("Actualiza el estado a "), inlineCode("completada"), regular(" y muestra una segunda línea con el mismo formato.")),
                                ),
                            expectedOutput =
                                listOf(
                                    consoleRun(
                                        consoleOutput("Repasar Kotlin: pendiente"),
                                        consoleOutput("Repasar Kotlin: completada"),
                                    ),
                                ),
                            hints =
                                listOf(
                                    richText(regular("Empieza por decidir qué dato se mantiene y cuál debe poder cambiar durante el programa.")),
                                    richText(regular("La primera salida observa el estado inicial; el cambio va entre las dos salidas.")),
                                    richText(regular("Revisa la pantalla sobre interpolación para recordar cómo insertar los dos nombres en el mismo texto.")),
                                ),
                            extension = richText(regular("Cambia solo el texto de la tarea y conserva el mismo cambio de estado y el formato de las dos líneas.")),
                            confirmation =
                                richText(
                                    regular("Confirma que la consola muestra las dos líneas indicadas, primero con el estado inicial y después con el "),
                                    inlineCode("estado"),
                                    regular(" actualizado."),
                                ),
                        ),
                ),
            ),
    )

private fun interpolatedGreetingProgram() =
    listOf(
        codeLine(keyword("fun "), function("main"), punctuation("() {")),
        codeLine(plain("    "), keyword("val "), plain("nombre "), operator("="), plain(" "), string("\"Ada\"")),
        codeLine(plain("    "), function("println"), punctuation("("), string("\"Hola, "), interpolation("\$nombre"), string("\""), punctuation(")")),
        codeLine(punctuation("}")),
    )

private fun comment(text: String): LessonCodeToken = LessonCodeToken(text = text, kind = dev.kuromiichi.damprep.data.lessons.LessonCodeTokenKind.Comment)

private fun interpolation(text: String): LessonCodeToken = LessonCodeToken(text = text, kind = dev.kuromiichi.damprep.data.lessons.LessonCodeTokenKind.Interpolation)
