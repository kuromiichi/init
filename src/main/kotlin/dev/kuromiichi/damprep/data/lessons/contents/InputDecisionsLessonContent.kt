package dev.kuromiichi.damprep.data.lessons.contents

import dev.kuromiichi.damprep.data.lessons.LessonCatalog
import dev.kuromiichi.damprep.data.lessons.LessonCodeExample
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

val inputDecisionsLessonContent =
    LessonContent(
        lessonId = LessonCatalog.lessons[4].id,
        steps =
            listOf(
                LessonStep(
                    title = "Pedir una respuesta",
                    body = "Un programa puede pedir información por la consola. print deja el cursor en la misma línea y readln espera a que escribas una respuesta y pulses Intro.",
                    explanation =
                        listOf(
                            paragraph(inlineCode("print"), regular(" muestra el mensaje sin añadir un salto de línea; así puedes escribir la respuesta a su lado.")),
                            paragraph(inlineCode("readln()"), regular(" detiene el programa hasta que pulsas Intro. Devuelve toda la línea escrita como texto, es decir, un "), inlineCode("String"), regular(".")),
                            paragraph(regular("El valor leído se guarda en "), inlineCode("nombre"), regular(" y después se usa en el saludo. Prueba con tu propio nombre para observar el cambio.")),
                        ),
                    visual =
                        LessonVisualContent.CodeExamples(
                            listOf(
                                LessonCodeExample(
                                    title = "Un saludo personal",
                                    lines =
                                        listOf(
                                            codeLine(keyword("fun "), function("main"), punctuation("() {")),
                                            codeLine(plain("    "), function("print"), punctuation("("), string("\"Tu nombre: \""), punctuation(")")),
                                            codeLine(plain("    "), keyword("val "), plain("nombre "), operator("= "), function("readln"), punctuation("()")),
                                            codeLine(plain("    "), function("println"), punctuation("("), string("\"Hola, "), plain("\$nombre"), string("\""), punctuation(")")),
                                            codeLine(punctuation("}")),
                                        ),
                                    note = "Si escribes Ada, aparece Hola, Ada.",
                                ),
                            ),
                        ),
                ),
                LessonStep(
                    title = "Operar con números",
                    body = "Los números permiten calcular antes de decidir. Int representa enteros; Kotlin puede sumar, restar, multiplicar y dividir con +, -, * y /.",
                    explanation =
                        listOf(
                            paragraph(inlineCode("Int"), regular(" es un tipo para números enteros, sin decimales. Un literal como "), inlineCode("12"), regular(" permite a Kotlin inferir ese tipo.")),
                            paragraph(inlineCode("+"), regular(", "), inlineCode("-"), regular(", "), inlineCode("*"), regular(" y "), inlineCode("/"), regular(" realizan las operaciones habituales. Con dos "), inlineCode("Int"), regular(", la división descarta la parte decimal: "), inlineCode("7 / 2"), regular(" da "), inlineCode("3"), regular(".")),
                            paragraph(regular("Si necesitas decimales, usa un valor como "), inlineCode("2.0"), regular(" (tipo "), inlineCode("Double"), regular("). Aquí basta trabajar con minutos enteros. Desde este ejemplo, los fragmentos muestran instrucciones que pondrías dentro de "), inlineCode("main"), regular(".")),
                        ),
                    visual =
                        LessonVisualContent.CodeExamples(
                            listOf(
                                LessonCodeExample(
                                    title = "Repartir tiempo de estudio",
                                    lines =
                                        listOf(
                                            codeLine(keyword("val "), plain("total "), operator("= "), number("30")),
                                            codeLine(keyword("val "), plain("porTema "), operator("= "), plain("total "), operator("/ "), number("2")),
                                            codeLine(function("println"), punctuation("("), plain("porTema"), punctuation(")")),
                                        ),
                                    note = "La consola muestra 15 minutos por tema.",
                                ),
                            ),
                        ),
                ),
                LessonStep(
                    title = "Preguntas con respuesta verdadera o falsa",
                    body = "Una comparación produce un Boolean: true o false. Puedes comparar números o comprobar si dos valores son iguales.",
                    explanation =
                        listOf(
                            paragraph(inlineCode(">="), regular(" pregunta si el valor de la izquierda es mayor o igual que el de la derecha. También tienes "), inlineCode(">"), regular(", "), inlineCode("<"), regular(" y "), inlineCode("<="), regular(".")),
                            paragraph(inlineCode("=="), regular(" comprueba igualdad y "), inlineCode("!="), regular(" desigualdad. No confundas "), inlineCode("=="), regular(" con "), inlineCode("="), regular(", que asigna un valor.")),
                            paragraph(regular("En el ejemplo, 18 no es mayor o igual que 20; por eso la primera línea muestra "), inlineCode("false"), regular(". La segunda muestra "), inlineCode("true"), regular(".")),
                        ),
                    visual =
                        LessonVisualContent.CodeExamples(
                            listOf(
                                LessonCodeExample(
                                    title = "Comprobar la temperatura",
                                    lines =
                                        listOf(
                                            codeLine(keyword("val "), plain("grados "), operator("= "), number("18")),
                                            codeLine(function("println"), punctuation("("), plain("grados "), operator(">= "), number("20"), punctuation(")")),
                                            codeLine(function("println"), punctuation("("), plain("grados "), operator("!= "), number("0"), punctuation(")")),
                                        ),
                                    note = "La consola muestra false y después true.",
                                ),
                            ),
                        ),
                ),
                LessonStep(
                    title = "Elegir entre dos caminos",
                    body = "if ejecuta su bloque cuando la condición es true. Si es false y hay un else, ejecuta el otro bloque.",
                    explanation =
                        listOf(
                            paragraph(regular("La condición va entre paréntesis. Las llaves marcan qué instrucciones pertenecen a cada camino.")),
                            paragraph(regular("Solo se ejecuta uno de los dos bloques. Después, el programa continúa con las instrucciones que haya tras el "), inlineCode("if"), regular(".")),
                            paragraph(regular("Aquí la edad es 16, así que aparece el mensaje del "), inlineCode("else"), regular(". Cambia 16 por 18 para comprobar el otro camino.")),
                        ),
                    visual =
                        LessonVisualContent.CodeExamples(
                            listOf(
                                LessonCodeExample(
                                    title = "Una decisión por edad",
                                    lines =
                                        listOf(
                                            codeLine(keyword("val "), plain("edad "), operator("= "), number("16")),
                                            codeLine(keyword("if "), punctuation("("), plain("edad "), operator(">= "), number("18"), punctuation(") {")),
                                            codeLine(plain("    "), function("println"), punctuation("("), string("\"Puedes entrar\""), punctuation(")")),
                                            codeLine(punctuation("} "), keyword("else "), punctuation("{")),
                                            codeLine(plain("    "), function("println"), punctuation("("), string("\"Todavía no\""), punctuation(")")),
                                            codeLine(punctuation("}")),
                                        ),
                                    note = "Con 16 aparece Todavía no; con 18 aparece Puedes entrar.",
                                ),
                            ),
                        ),
                ),
                LessonStep(
                    title = "Distinguir una tercera respuesta",
                    body = "Con else if puedes comprobar otra condición antes de llegar a else. Así no confundes una respuesta inesperada con un «no».",
                    explanation =
                        listOf(
                            paragraph(regular("La primera condición que resulte verdadera determina el bloque que se ejecuta. Las siguientes ya no se evalúan.")),
                            paragraph(regular("Como "), inlineCode("readln()"), regular(" devuelve texto, comparamos con los textos "), inlineCode("\"si\""), regular(" y "), inlineCode("\"no\""), regular("; no hacen falta números.")),
                            paragraph(regular("El "), inlineCode("else"), regular(" final recoge cualquier otra escritura, incluso una respuesta vacía. Todavía no estamos corrigiendo mayúsculas ni espacios.")),
                        ),
                    visual =
                        LessonVisualContent.CodeExamples(
                            listOf(
                                LessonCodeExample(
                                    title = "Una invitación",
                                    lines =
                                        listOf(
                                            codeLine(keyword("val "), plain("respuesta "), operator("= "), function("readln"), punctuation("()")),
                                            codeLine(keyword("if "), punctuation("("), plain("respuesta "), operator("== "), string("\"si\""), punctuation(") {")),
                                            codeLine(plain("    "), function("println"), punctuation("("), string("\"Te esperamos\""), punctuation(")")),
                                            codeLine(punctuation("} "), keyword("else if "), punctuation("("), plain("respuesta "), operator("== "), string("\"no\""), punctuation(") {")),
                                            codeLine(plain("    "), function("println"), punctuation("("), string("\"Otra vez será\""), punctuation(")")),
                                            codeLine(punctuation("} "), keyword("else "), punctuation("{")),
                                            codeLine(plain("    "), function("println"), punctuation("("), string("\"Escribe si o no\""), punctuation(")")),
                                            codeLine(punctuation("}")),
                                        ),
                                    note = "Escribir tal vez activa el último bloque, no el de no.",
                                ),
                            ),
                        ),
                ),
                LessonStep(
                    title = "Combinar condiciones",
                    body = "Los operadores lógicos forman una condición a partir de otras: && exige ambas, || acepta cualquiera y ! invierte true y false.",
                    explanation =
                        listOf(
                            paragraph(inlineCode("&&"), regular(" se lee «y»: solo da "), inlineCode("true"), regular(" si las dos condiciones son verdaderas. "), inlineCode("||"), regular(" se lee «o»: basta con una.")),
                            paragraph(inlineCode("!"), regular(" niega un valor Boolean. En el ejemplo, "), inlineCode("!entrada"), regular(" sería verdadero porque entrada vale false.")),
                            paragraph(regular("Aquí hay plaza, pero no entrada: con "), inlineCode("&&"), regular(" la condición es falsa y con "), inlineCode("||"), regular(" sería verdadera.")),
                        ),
                    visual =
                        LessonVisualContent.CodeExamples(
                            listOf(
                                LessonCodeExample(
                                    title = "Acceso a una actividad",
                                    lines =
                                        listOf(
                                            codeLine(keyword("val "), plain("plaza "), operator("= "), keyword("true")),
                                            codeLine(keyword("val "), plain("entrada "), operator("= "), keyword("false")),
                                            codeLine(keyword("if "), punctuation("("), plain("plaza "), operator("&& "), plain("entrada"), punctuation(") {")),
                                            codeLine(plain("    "), function("println"), punctuation("("), string("\"Pasa\""), punctuation(")")),
                                            codeLine(punctuation("} "), keyword("else "), punctuation("{")),
                                            codeLine(plain("    "), function("println"), punctuation("("), string("\"Espera\""), punctuation(")")),
                                            codeLine(punctuation("}")),
                                        ),
                                    note = "Aparece Espera. Cambia entrada a true y vuelve a ejecutar.",
                                ),
                            ),
                        ),
                ),
                LessonStep(
                    title = "Elegir entre varias opciones",
                    body = "when expresa con claridad una selección entre varios valores. Cada rama tiene un valor, una flecha y la acción correspondiente.",
                    explanation =
                        listOf(
                            paragraph(regular("Kotlin compara "), inlineCode("opcion"), regular(" con cada valor escrito a la izquierda de "), inlineCode("->"), regular(". Ejecuta la rama que coincida.")),
                            paragraph(regular("El "), inlineCode("else"), regular(" atiende cualquier valor distinto de las opciones previstas. Como la respuesta de "), inlineCode("readln()"), regular(" es texto, las opciones llevan comillas.")),
                            paragraph(regular("Aquí usamos "), inlineCode("when"), regular(" como instrucción. Más adelante podrás usarlo también para producir un valor.")),
                        ),
                    visual =
                        LessonVisualContent.CodeExamples(
                            listOf(
                                LessonCodeExample(
                                    title = "Elegir un mensaje",
                                    lines =
                                        listOf(
                                            codeLine(keyword("val "), plain("opcion "), operator("= "), function("readln"), punctuation("()")),
                                            codeLine(keyword("when "), punctuation("("), plain("opcion"), punctuation(") {")),
                                            codeLine(plain("    "), string("\"1\" "), operator("-> "), function("println"), punctuation("("), string("\"Buenos días\""), punctuation(")")),
                                            codeLine(plain("    "), string("\"2\" "), operator("-> "), function("println"), punctuation("("), string("\"Buenas noches\""), punctuation(")")),
                                            codeLine(plain("    "), keyword("else "), operator("-> "), function("println"), punctuation("("), string("\"Opción no válida\""), punctuation(")")),
                                            codeLine(punctuation("}")),
                                        ),
                                    note = "1 y 2 tienen su mensaje; cualquier otra respuesta avisa del error.",
                                ),
                            ),
                        ),
                ),
                LessonStep(
                    title = "Cuando necesitas un número",
                    body = "readln devuelve texto incluso si escribes dígitos. toIntOrNull intenta convertirlo a Int y devuelve null cuando no es un número entero válido.",
                    explanation =
                        listOf(
                            paragraph(regular("Comparar directamente la respuesta de "), inlineCode("readln()"), regular(" con un número no sirve: primero hay que convertir el texto.")),
                            paragraph(inlineCode("toIntOrNull()"), regular(" permite comprobar el resultado antes de usarlo. Si el texto no representa un entero, "), inlineCode("null"), regular(" indica que no se pudo convertir; no es el número cero.")),
                            paragraph(regular("Tras comprobar que no es "), inlineCode("null"), regular(", Kotlin permite tratar "), inlineCode("minutos"), regular(" como número en el otro bloque. El estudio completo de valores nulos queda para más adelante.")),
                        ),
                    visual =
                        LessonVisualContent.CodeExamples(
                            listOf(
                                LessonCodeExample(
                                    title = "Minutos de lectura",
                                    lines =
                                        listOf(
                                            codeLine(keyword("val "), plain("texto "), operator("= "), function("readln"), punctuation("()")),
                                            codeLine(keyword("val "), plain("minutos "), operator("= "), plain("texto."), function("toIntOrNull"), punctuation("()")),
                                            codeLine(keyword("if "), punctuation("("), plain("minutos "), operator("== "), keyword("null"), punctuation(") {")),
                                            codeLine(plain("    "), function("println"), punctuation("("), string("\"Escribe un número\""), punctuation(")")),
                                            codeLine(punctuation("} "), keyword("else "), punctuation("{")),
                                            codeLine(plain("    "), function("println"), punctuation("("), plain("minutos "), operator("+ "), number("5"), punctuation(")")),
                                            codeLine(punctuation("}")),
                                        ),
                                    note = "Con 20 aparece 25; con hola se pide un número.",
                                ),
                            ),
                        ),
                ),
                LessonStep(
                    title = "Ahora te toca: planear una pausa",
                    body = "Combina lectura, conversión y decisión en un programa pequeño. Comprueba tanto una entrada válida como una inválida antes de darlo por terminado.",
                    explanation =
                        listOf(
                            paragraph(regular("Piensa primero en los caminos posibles: un texto que no sea un número, un tiempo inferior al límite y un tiempo que alcance el límite.")),
                            paragraph(regular("Trabaja dentro de "), inlineCode("main"), regular(". Puedes volver a los ejemplos de lectura y conversión, pero construye tú el programa.")),
                            paragraph(regular("Ejecuta varias veces e introduce cada dato de prueba. Si el mensaje no corresponde, revisa la comparación y qué camino se ejecutó.")),
                        ),
                    visual = LessonVisualContent.Flow(listOf("Leer minutos", "Comprobar el dato", "Elegir un mensaje")),
                    links =
                        listOf(
                            LessonLink("Entrada estándar en Kotlin", "https://kotlinlang.org/docs/read-standard-input.html"),
                            LessonLink("Condiciones en Kotlin", "https://kotlinlang.org/docs/control-flow.html"),
                        ),
                    practice =
                        LessonPractice(
                            id = "input-decisions-break",
                            objective = richText(highlight("Decidir si toca una pausa a partir de un dato introducido por la persona.")),
                            statement = richText("Crea un programa que pida los minutos estudiados y muestre un único mensaje según la respuesta."),
                            requirements =
                                listOf(
                                    richText(emphasis("Lee una respuesta"), regular(" por consola e intenta convertirla a un número entero sin provocar un error si no se puede.")),
                                    richText(regular("Si la conversión falla, muestra "), inlineCode("Escribe un número entero"), regular(".")),
                                    richText(regular("Si el número es menor que 25, muestra "), inlineCode("Sigue estudiando"), regular(".")),
                                    richText(regular("Si es 25 o más, muestra "), inlineCode("Toca una pausa"), regular(".")),
                                    richText(regular("Muestra solo uno de esos mensajes por ejecución.")),
                                ),
                            expectedResult = richText("Con 24: Sigue estudiando\nCon 25: Toca una pausa\nCon hola: Escribe un número entero"),
                            hints =
                                listOf(
                                    richText("Separa primero la respuesta que no puede convertirse de las que sí son números."),
                                    richText("Cuando ya tienes un número, basta una comparación con 25 para elegir entre los otros dos caminos."),
                                    richText("Prueba el valor justo por debajo del límite y el propio límite."),
                                ),
                            extension = richText("Añade un mensaje específico para los números negativos, que no representan minutos estudiados."),
                            confirmation = richText(regular("Confirma las salidas para "), inlineCode("24"), regular(", "), inlineCode("25"), regular(" y "), inlineCode("hola"), regular(", sin que el programa falle.")),
                        ),
                ),
            ),
    )
