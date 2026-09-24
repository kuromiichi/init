package dev.kuromiichi.damprep.data.lessons.contents

import dev.kuromiichi.damprep.data.lessons.LessonCalloutTone
import dev.kuromiichi.damprep.data.lessons.LessonCodeExample
import dev.kuromiichi.damprep.data.lessons.LessonContent
import dev.kuromiichi.damprep.data.lessons.LessonLink
import dev.kuromiichi.damprep.data.lessons.LessonPractice
import dev.kuromiichi.damprep.data.lessons.LessonStep
import dev.kuromiichi.damprep.data.lessons.LessonTraceTable
import dev.kuromiichi.damprep.data.lessons.LessonVisualContent
import dev.kuromiichi.damprep.data.lessons.callout
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

val loopsLessonContent =
    LessonContent(
        lessonId = "loops",
        steps =
            listOf(
                LessonStep(
                    title = "Repetir sin copiar",
                    explanation =
                        listOf(
                            paragraph(regular("Hasta ahora tus programas ejecutaban instrucciones en orden y elegían entre caminos. Falta la tercera estructura básica: la "), highlight("repetición"), regular(".")),
                            paragraph(regular("Un "), highlight("bucle"), regular(" ejecuta el mismo bloque varias veces sin copiarlo. Cada repetición se llama "), emphasis("vuelta"), regular(" o "), emphasis("iteración"), regular(".")),
                            paragraph(regular("Antes de cada vuelta se comprueba una "), emphasis("condición"), regular(". Si es verdadera, se ejecuta el "), emphasis("cuerpo"), regular(", se "), emphasis("actualizan"), regular(" los datos y se vuelve a comprobar. Si es falsa, el bucle termina y el programa sigue con lo que haya después.")),
                        ),
                    visual = LessonVisualContent.Cycle(listOf("Condición", "Cuerpo", "Actualizar")),
                ),
                LessonStep(
                    title = "Repetir mientras se cumpla",
                    explanation =
                        listOf(
                            paragraph(inlineCode("while"), regular(" repite su bloque mientras la condición entre paréntesis sea verdadera. La comprueba antes de cada vuelta, así que puede no ejecutarse ninguna vez.")),
                            paragraph(inlineCode("vuelta++"), regular(" suma 1 a la variable; "), inlineCode("--"), regular(" resta 1. Equivale a "), inlineCode("vuelta += 1"), regular(" y a "), inlineCode("vuelta = vuelta + 1"), regular(". Con "), inlineCode("+="), regular(" puedes sumar cualquier valor, y también existen "), inlineCode("-="), regular(" y "), inlineCode("*="), regular(".")),
                            paragraph(regular("La variable de la condición es "), inlineCode("var"), regular(" porque su valor cambia en cada vuelta.")),
                        ),
                    visual =
                        LessonVisualContent.CodeExamples(
                            listOf(
                                LessonCodeExample(
                                    title = "Contar tres vueltas",
                                    lines =
                                        kotlinCode(
                                            $$"""
                                                var vuelta = 1
                                                while (vuelta <= 3) {
                                                    println("Vuelta $vuelta")
                                                    vuelta++
                                                }
                                                println("Fin")
                                            """.trimIndent(),
                                        ),
                                    note = richText(regular("Aparecen "), inlineCode("Vuelta 1"), regular(", "), inlineCode("Vuelta 2"), regular(", "), inlineCode("Vuelta 3"), regular(" y "), inlineCode("Fin"), regular(". Cuando "), inlineCode("vuelta"), regular(" vale "), inlineCode("4"), regular(", la condición es falsa.")),
                                ),
                            ),
                        ),
                ),
                LessonStep(
                    title = "Bucles que no terminan",
                    explanation =
                        listOf(
                            paragraph(regular("Si nada dentro del cuerpo cambia la condición, el bucle se repite para siempre: es un "), highlight("bucle infinito"), regular(".")),
                            paragraph(inlineCode("vuelta++"), regular(" no está en este ejemplo, así que "), inlineCode("vuelta"), regular(" siempre vale "), inlineCode("1"), regular(" y la condición nunca deja de cumplirse.")),
                            paragraph(regular("Antes de ejecutar, comprueba que la variable de la condición cambia en cada vuelta y se acerca al final.")),
                            callout(
                                "Si se queda repitiendo",
                                richText(regular("Detén el programa con el botón cuadrado rojo "), emphasis("Stop"), regular(" de la ventana "), emphasis("Run"), regular(" de IntelliJ IDEA.")),
                                LessonCalloutTone.Warning,
                            ),
                        ),
                    visual =
                        LessonVisualContent.CodeExamples(
                            listOf(
                                LessonCodeExample(
                                    title = "Falta avanzar",
                                    lines =
                                        kotlinCode(
                                            $$"""
                                                var vuelta = 1
                                                while (vuelta <= 3) {
                                                    println("Vuelta $vuelta")
                                                }
                                            """.trimIndent(),
                                        ),
                                    note = richText(regular("La consola muestra "), inlineCode("Vuelta 1"), regular(" una y otra vez hasta que detienes el programa.")),
                                ),
                            ),
                        ),
                ),
                LessonStep(
                    title = "Pedir hasta que el dato sea válido",
                    explanation =
                        listOf(
                            paragraph(regular("Un uso habitual de "), inlineCode("while"), regular(" es insistir hasta recibir un dato correcto. "), inlineCode("readln().toIntOrNull()"), regular(" lee y convierte en una sola línea.")),
                            paragraph(regular("Se lee una vez antes del bucle para tener algo que comprobar. Mientras "), inlineCode("edad"), regular(" sea "), inlineCode("null"), regular(", se avisa y se vuelve a leer dentro del cuerpo.")),
                            paragraph(regular("Si el primer dato ya es válido, el cuerpo no se ejecuta ninguna vez.")),
                        ),
                    visual =
                        LessonVisualContent.CodeExamples(
                            listOf(
                                LessonCodeExample(
                                    title = "Insistir con la edad",
                                    lines =
                                        kotlinCode(
                                            $$"""
                                                print("Edad: ")
                                                var edad = readln().toIntOrNull()
                                                while (edad == null) {
                                                    print("Escribe un número: ")
                                                    edad = readln().toIntOrNull()
                                                }
                                                println("Tienes $edad años")
                                            """.trimIndent(),
                                        ),
                                    note = richText(regular("Con "), inlineCode("hola"), regular(" vuelve a preguntar; con "), inlineCode("17"), regular(" aparece "), inlineCode("Tienes 17 años"), regular(".")),
                                ),
                            ),
                        ),
                ),
                LessonStep(
                    title = "Al menos una vez",
                    explanation =
                        listOf(
                            paragraph(inlineCode("do"), regular("-"), inlineCode("while"), regular(" ejecuta primero el cuerpo y comprueba la condición al final, así que siempre hay al menos una vuelta.")),
                            paragraph(regular("Encaja cuando no puedes decidir nada hasta haber preguntado: no hace falta leer antes del bucle.")),
                            paragraph(regular("Lo que declaras dentro del cuerpo, como "), inlineCode("respuesta"), regular(", se puede usar en la condición del "), inlineCode("while"), regular(" final.")),
                        ),
                    visual =
                        LessonVisualContent.CodeExamples(
                            listOf(
                                LessonCodeExample(
                                    title = "Preguntar antes de salir",
                                    lines =
                                        kotlinCode(
                                            """
                                            do {
                                                print("¿Terminar? (s/n): ")
                                                val respuesta = readln()
                                            } while (respuesta != "s")
                                            println("Hasta pronto")
                                            """.trimIndent(),
                                        ),
                                    note = richText(regular("Con "), inlineCode("n"), regular(" o cualquier otra respuesta vuelve a preguntar; con "), inlineCode("s"), regular(" aparece "), inlineCode("Hasta pronto"), regular(".")),
                                ),
                            ),
                        ),
                ),
                LessonStep(
                    title = "Repetir un número conocido de veces",
                    explanation =
                        listOf(
                            paragraph(inlineCode("for"), regular(" recorre los valores de un "), highlight("rango"), regular(". En cada vuelta, "), inlineCode("i"), regular(" toma el siguiente valor: no se declara con "), inlineCode("val"), regular(" ni "), inlineCode("var"), regular(" y no hay que actualizarla.")),
                            paragraph(inlineCode("1..5"), regular(" incluye los dos extremos; "), inlineCode("1..<5"), regular(" deja fuera el final. "), inlineCode("downTo"), regular(" cuenta hacia atrás y "), inlineCode("step"), regular(" salta de varios en varios. Con saltos, el final puede no alcanzarse: "), inlineCode("1..9 step 3"), regular(" da "), inlineCode("1"), regular(", "), inlineCode("4"), regular(" y "), inlineCode("7"), regular(".")),
                            paragraph(regular("Usa "), inlineCode("for"), regular(" cuando sabes cuántas vueltas habrá, y "), inlineCode("while"), regular(" cuando depende de lo que ocurra durante la ejecución.")),
                        ),
                    visual =
                        LessonVisualContent.CodeExamples(
                            listOf(
                                LessonCodeExample(
                                    title = "Contar hacia delante y hacia atrás",
                                    lines =
                                        kotlinCode(
                                            """
                                            for (i in 1..5) {
                                                println(i)
                                            }
                                            for (i in 10 downTo 0 step 5) {
                                                println(i)
                                            }
                                            """.trimIndent(),
                                        ),
                                    note = richText(regular("El primer bucle muestra del "), inlineCode("1"), regular(" al "), inlineCode("5"), regular("; el segundo, "), inlineCode("10"), regular(", "), inlineCode("5"), regular(" y "), inlineCode("0"), regular(".")),
                                ),
                            ),
                        ),
                ),
                LessonStep(
                    title = "Contar y acumular",
                    explanation =
                        listOf(
                            paragraph(regular("Un "), highlight("contador"), regular(" suma 1 cada vez que ocurre algo; un "), highlight("acumulador"), regular(" va sumando valores. Aquí "), inlineCode("cuantos"), regular(" cuenta los impares y "), inlineCode("suma"), regular(" los acumula.")),
                            paragraph(regular("Ambos se crean antes del bucle, empezando en "), inlineCode("0"), regular(", y se muestran después. Si los declararas dentro del cuerpo, volverían a "), inlineCode("0"), regular(" en cada vuelta.")),
                            paragraph(regular("El "), inlineCode("%"), regular(" de la lección anterior decide qué números cuentan: un número es impar si el resto de dividirlo entre 2 no es 0.")),
                        ),
                    visual =
                        LessonVisualContent.CodeExamples(
                            listOf(
                                LessonCodeExample(
                                    title = "Impares del 1 al 20",
                                    lines =
                                        kotlinCode(
                                            $$"""
                                                var cuantos = 0
                                                var suma = 0
                                                for (n in 1..20) {
                                                    if (n % 2 != 0) {
                                                        cuantos++
                                                        suma += n
                                                    }
                                                }
                                                println("Impares: $cuantos")
                                                println("Suma: $suma")
                                            """.trimIndent(),
                                        ),
                                    note = richText(regular("La consola muestra "), inlineCode("Impares: 10"), regular(" y "), inlineCode("Suma: 100"), regular(".")),
                                ),
                            ),
                        ),
                ),
                LessonStep(
                    title = "Un bucle dentro de otro",
                    explanation =
                        listOf(
                            paragraph(regular("Un bucle puede ir dentro del cuerpo de otro: son "), highlight("bucles anidados"), regular(". En cada vuelta del exterior, el interior hace todas sus vueltas.")),
                            paragraph(regular("Aquí el exterior recorre 3 filas y el interior dibuja 4 asteriscos en cada una: 12 en total. "), inlineCode("println()"), regular(" sin nada entre paréntesis solo salta de línea.")),
                            paragraph(regular("Son útiles para trabajar con filas y columnas, como una tabla de multiplicar o, más adelante, tablas de datos.")),
                        ),
                    visual =
                        LessonVisualContent.CodeExamples(
                            listOf(
                                LessonCodeExample(
                                    title = "Un rectángulo de asteriscos",
                                    lines =
                                        kotlinCode(
                                            """
                                            for (fila in 1..3) {
                                                for (columna in 1..4) {
                                                    print("* ")
                                                }
                                                println()
                                            }
                                            """.trimIndent(),
                                        ),
                                    note = richText(regular("La consola muestra tres líneas iguales, cada una con cuatro asteriscos.")),
                                ),
                            ),
                        ),
                ),
                LessonStep(
                    title = "Salir antes de tiempo",
                    explanation =
                        listOf(
                            paragraph(regular("Una "), highlight("bandera"), regular(" es un "), inlineCode("Boolean"), regular(" que parte de una suposición y cambia si algo la contradice. Aquí se supone que "), inlineCode("n"), regular(" es primo hasta encontrar un divisor.")),
                            paragraph(inlineCode("break"), regular(" termina el bucle en ese momento: si ya hay un divisor, no hace falta probar más. "), inlineCode("continue"), regular(", en cambio, salta el resto de la vuelta actual y pasa a la siguiente.")),
                            paragraph(inlineCode("2..<n"), regular(" prueba todos los posibles divisores menores que "), inlineCode("n"), regular(". La bandera empieza con "), inlineCode("n > 1"), regular(" porque ni 0 ni 1 son primos.")),
                        ),
                    visual =
                        LessonVisualContent.CodeExamples(
                            listOf(
                                LessonCodeExample(
                                    title = "¿Es primo?",
                                    lines =
                                        kotlinCode(
                                            """
                                            val n = 21
                                            var esPrimo = n > 1
                                            for (d in 2..<n) {
                                                if (n % d == 0) {
                                                    esPrimo = false
                                                    break
                                                }
                                            }
                                            println(esPrimo)
                                            """.trimIndent(),
                                        ),
                                    note = richText(regular("Muestra "), inlineCode("false"), regular(": al llegar a "), inlineCode("3"), regular(" encuentra un divisor y sale. Con "), inlineCode("13"), regular(" muestra "), inlineCode("true"), regular(".")),
                                ),
                            ),
                        ),
                ),
                LessonStep(
                    title = "Seguir un bucle a mano",
                    explanation =
                        listOf(
                            paragraph(regular("Una "), highlight("tabla de traza"), regular(" apunta el valor de cada variable al terminar cada vuelta. Hazla con papel antes de ejecutar: si el resultado final no es el que esperas, el fallo está en el algoritmo.")),
                            paragraph(regular("Fíjate sobre todo en la primera y la última vuelta. Ahí se esconden los errores de una vuelta de más o de menos: con "), inlineCode("1..<4"), regular(" la tabla terminaría en la vuelta 3 y la suma sería "), inlineCode("6"), regular(".")),
                            paragraph(regular("En la lección de depuración verás cómo IntelliJ IDEA hace este seguimiento por ti.")),
                        ),
                    visual =
                        LessonVisualContent.Trace(
                            example =
                                LessonCodeExample(
                                    title = "Sumar del 1 al 4",
                                    lines =
                                        kotlinCode(
                                            """
                                            var suma = 0
                                            for (i in 1..4) {
                                                suma += i
                                            }
                                            println(suma)
                                            """.trimIndent(),
                                        ),
                                    note = richText(regular("La consola muestra "), inlineCode("10"), regular(", el valor de "), inlineCode("suma"), regular(" en la última fila.")),
                                ),
                            table =
                                LessonTraceTable(
                                    title = "Al terminar cada vuelta",
                                    columns = listOf("Vuelta", "i", "suma"),
                                    rows =
                                        listOf(
                                            listOf("1", "1", "1"),
                                            listOf("2", "2", "3"),
                                            listOf("3", "3", "6"),
                                            listOf("4", "4", "10"),
                                        ),
                                ),
                        ),
                ),
                LessonStep(
                    title = "Ahora te toca: el número secreto",
                    explanation =
                        listOf(
                            paragraph(regular("Antes de escribir código, decide qué bucle encaja: hay un máximo de intentos, pero la partida puede acabar antes.")),
                            paragraph(regular("Trabaja dentro de "), inlineCode("main"), regular(". Reutiliza la lectura con "), inlineCode("toIntOrNull()"), regular(" y las comparaciones de la lección anterior.")),
                            paragraph(regular("Haz la tabla de traza de una partida corta antes de ejecutar, y después prueba acertando y fallando todos los intentos.")),
                        ),
                    visual = LessonVisualContent.Flow(listOf("Leer un intento", "Comparar con el secreto", "Dar una pista", "Acertar o agotar intentos")),
                    links =
                        listOf(
                            LessonLink("Bucles y condiciones en Kotlin", "https://kotlinlang.org/docs/control-flow.html"),
                            LessonLink("Rangos en Kotlin", "https://kotlinlang.org/docs/ranges.html"),
                            LessonLink("break y continue en Kotlin", "https://kotlinlang.org/docs/returns.html"),
                        ),
                    practice =
                        LessonPractice(
                            id = "loops-secret-number",
                            objective = richText(highlight("Repetir preguntas hasta acertar o agotar los intentos.")),
                            statement = richText(regular("Crea un juego en el que la persona tenga cinco intentos para adivinar un número secreto escrito en el programa.")),
                            requirements =
                                listOf(
                                    richText(regular("Guarda el número en "), inlineCode("val secreto = 42"), regular(".")),
                                    richText(regular("En cada intento, "), emphasis("lee una respuesta"), regular(" e intenta convertirla a entero. Si no es un número, muestra "), inlineCode("Escribe un número"), regular("; ese intento también cuenta.")),
                                    richText(regular("Si el número es mayor que el secreto, muestra "), inlineCode("Demasiado alto"), regular("; si es menor, "), inlineCode("Demasiado bajo"), regular(".")),
                                    richText(regular("Si acierta, muestra "), inlineCode("¡Acertaste!"), regular(" y no pidas más números.")),
                                    richText(regular("Si se agotan los cinco intentos sin acertar, muestra "), inlineCode("Sin intentos. Era 42"), regular(".")),
                                ),
                            expectedOutput =
                                listOf(
                                    consoleRun(
                                        consoleInput("50"),
                                        consoleOutput("Demasiado alto"),
                                        consoleInput("30"),
                                        consoleOutput("Demasiado bajo"),
                                        consoleInput("42"),
                                        consoleOutput("¡Acertaste!"),
                                    ),
                                    consoleRun(
                                        consoleInput("hola"),
                                        consoleOutput("Escribe un número"),
                                        consoleInput("10"),
                                        consoleOutput("Demasiado bajo"),
                                        consoleInput("90"),
                                        consoleOutput("Demasiado alto"),
                                        consoleInput("41"),
                                        consoleOutput("Demasiado bajo"),
                                        consoleInput("43"),
                                        consoleOutput("Demasiado alto"),
                                        consoleOutput("Sin intentos. Era 42"),
                                    ),
                                ),
                            hints =
                                listOf(
                                    richText(regular("Como hay un máximo de cinco intentos, un "), inlineCode("for"), regular(" de 1 a 5 marca las vueltas; "), inlineCode("break"), regular(" permite salir antes.")),
                                    richText(regular("Para saber al final si hubo acierto, usa una bandera "), inlineCode("Boolean"), regular(" que cambie al acertar.")),
                                    richText(regular("El mensaje final solo debe aparecer si la bandera no cambió. Compruébalo con la traza de la segunda ejecución.")),
                                ),
                            extension = richText(regular("Al terminar la partida, pregunta si se quiere jugar otra vez y repítela mientras la respuesta sea "), inlineCode("s"), regular(". El bucle de la partida quedará dentro de otro.")),
                            confirmation = richText(regular("Confirma las dos ejecuciones de ejemplo: acertar al tercer intento y agotar los cinco intentos con un texto que no es un número.")),
                        ),
                ),
            ),
    )
