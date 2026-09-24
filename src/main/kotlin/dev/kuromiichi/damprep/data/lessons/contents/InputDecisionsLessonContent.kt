package dev.kuromiichi.damprep.data.lessons.contents

import dev.kuromiichi.damprep.data.lessons.LessonCodeExample
import dev.kuromiichi.damprep.data.lessons.LessonContent
import dev.kuromiichi.damprep.data.lessons.LessonLink
import dev.kuromiichi.damprep.data.lessons.LessonPractice
import dev.kuromiichi.damprep.data.lessons.LessonStep
import dev.kuromiichi.damprep.data.lessons.LessonVisualContent
import dev.kuromiichi.damprep.data.lessons.consoleInput
import dev.kuromiichi.damprep.data.lessons.consoleOutput
import dev.kuromiichi.damprep.data.lessons.consoleRun
import dev.kuromiichi.damprep.data.lessons.emphasis
import dev.kuromiichi.damprep.data.lessons.highlight
import dev.kuromiichi.damprep.data.lessons.inlineCode
import dev.kuromiichi.damprep.data.lessons.kotlinCode
import dev.kuromiichi.damprep.data.lessons.paragraph
import dev.kuromiichi.damprep.data.lessons.regular
import dev.kuromiichi.damprep.data.lessons.richText

val inputDecisionsLessonContent =
    LessonContent(
        lessonId = "input-decisions",
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
                                        kotlinCode(
                                            $$"""
                                                fun main() {
                                                    print("Tu nombre: ")
                                                    val nombre = readln()
                                                    println("Hola, $nombre")
                                                }
                                            """.trimIndent(),
                                        ),
                                    note = richText(regular("Si escribes "), inlineCode("Ada"), regular(", aparece "), inlineCode("Hola, Ada"), regular(".")),
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
                                        kotlinCode(
                                            """
                                            val total = 30
                                            val porTema = total / 2
                                            println(porTema)
                                            """.trimIndent(),
                                        ),
                                    note = richText(regular("La consola muestra "), inlineCode("15"), regular(": los minutos para cada tema.")),
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
                            paragraph(regular("En el ejemplo, "), inlineCode("18"), regular(" no es mayor o igual que "), inlineCode("20"), regular("; por eso la línea 2 muestra "), inlineCode("false"), regular(". La línea 3 muestra "), inlineCode("true"), regular(".")),
                        ),
                    visual =
                        LessonVisualContent.CodeExamples(
                            listOf(
                                LessonCodeExample(
                                    title = "Comprobar la temperatura",
                                    lines =
                                        kotlinCode(
                                            """
                                            val grados = 18
                                            println(grados >= 20)
                                            println(grados != 0)
                                            """.trimIndent(),
                                        ),
                                    note = richText(regular("La consola muestra "), inlineCode("false"), regular(" y después "), inlineCode("true"), regular(".")),
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
                            paragraph(regular("Aquí "), inlineCode("edad"), regular(" vale "), inlineCode("16"), regular(", así que aparece el mensaje del "), inlineCode("else"), regular(". Cambia "), inlineCode("16"), regular(" por "), inlineCode("18"), regular(" para comprobar el otro camino.")),
                        ),
                    visual =
                        LessonVisualContent.CodeExamples(
                            listOf(
                                LessonCodeExample(
                                    title = "Una decisión por edad",
                                    lines =
                                        kotlinCode(
                                            """
                                            val edad = 16
                                            if (edad >= 18) {
                                                println("Puedes entrar")
                                            } else {
                                                println("Todavía no")
                                            }
                                            """.trimIndent(),
                                        ),
                                    note = richText(regular("Con "), inlineCode("16"), regular(" aparece "), inlineCode("Todavía no"), regular("; con "), inlineCode("18"), regular(" aparece "), inlineCode("Puedes entrar"), regular(".")),
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
                                        kotlinCode(
                                            """
                                            val respuesta = readln()
                                            if (respuesta == "si") {
                                                println("Te esperamos")
                                            } else if (respuesta == "no") {
                                                println("Otra vez será")
                                            } else {
                                                println("Escribe si o no")
                                            }
                                            """.trimIndent(),
                                        ),
                                    note = richText(regular("Escribir "), inlineCode("tal vez"), regular(" activa el último bloque, no el de "), inlineCode("no"), regular(".")),
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
                            paragraph(inlineCode("!"), regular(" niega un valor "), inlineCode("Boolean"), regular(". En el ejemplo, "), inlineCode("!entrada"), regular(" sería "), inlineCode("true"), regular(" porque "), inlineCode("entrada"), regular(" vale "), inlineCode("false"), regular(".")),
                            paragraph(regular("Aquí hay plaza, pero no entrada: con "), inlineCode("&&"), regular(" la condición es falsa y con "), inlineCode("||"), regular(" sería verdadera.")),
                        ),
                    visual =
                        LessonVisualContent.CodeExamples(
                            listOf(
                                LessonCodeExample(
                                    title = "Acceso a una actividad",
                                    lines =
                                        kotlinCode(
                                            """
                                            val plaza = true
                                            val entrada = false
                                            if (plaza && entrada) {
                                                println("Pasa")
                                            } else {
                                                println("Espera")
                                            }
                                            """.trimIndent(),
                                        ),
                                    note = richText(regular("Aparece "), inlineCode("Espera"), regular(". Cambia "), inlineCode("entrada"), regular(" a "), inlineCode("true"), regular(" y vuelve a ejecutar.")),
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
                                        kotlinCode(
                                            """
                                            val opcion = readln()
                                            when (opcion) {
                                                "1" -> println("Buenos días")
                                                "2" -> println("Buenas noches")
                                                else -> println("Opción no válida")
                                            }
                                            """.trimIndent(),
                                        ),
                                    note = richText(inlineCode("1"), regular(" y "), inlineCode("2"), regular(" tienen su mensaje; cualquier otra respuesta avisa del error.")),
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
                                        kotlinCode(
                                            """
                                            val texto = readln()
                                            val minutos = texto.toIntOrNull()
                                            if (minutos == null) {
                                                println("Escribe un número")
                                            } else {
                                                println(minutos + 5)
                                            }
                                            """.trimIndent(),
                                        ),
                                    note = richText(regular("Con "), inlineCode("20"), regular(" aparece "), inlineCode("25"), regular("; con "), inlineCode("hola"), regular(" aparece "), inlineCode("Escribe un número"), regular(".")),
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
                            expectedOutput =
                                listOf(
                                    consoleRun(consoleInput("24"), consoleOutput("Sigue estudiando")),
                                    consoleRun(consoleInput("25"), consoleOutput("Toca una pausa")),
                                    consoleRun(consoleInput("hola"), consoleOutput("Escribe un número entero")),
                                ),
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
