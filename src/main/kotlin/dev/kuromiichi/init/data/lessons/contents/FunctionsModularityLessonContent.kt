package dev.kuromiichi.init.data.lessons.contents

import dev.kuromiichi.init.data.lessons.LessonCalloutTone
import dev.kuromiichi.init.data.lessons.LessonCodeExample
import dev.kuromiichi.init.data.lessons.LessonContent
import dev.kuromiichi.init.data.lessons.LessonLink
import dev.kuromiichi.init.data.lessons.LessonPractice
import dev.kuromiichi.init.data.lessons.LessonStep
import dev.kuromiichi.init.data.lessons.LessonTraceTable
import dev.kuromiichi.init.data.lessons.LessonVisualContent
import dev.kuromiichi.init.data.lessons.callout
import dev.kuromiichi.init.data.lessons.consoleInput
import dev.kuromiichi.init.data.lessons.consoleOutput
import dev.kuromiichi.init.data.lessons.consoleRun
import dev.kuromiichi.init.data.lessons.emphasis
import dev.kuromiichi.init.data.lessons.highlight
import dev.kuromiichi.init.data.lessons.inlineCode
import dev.kuromiichi.init.data.lessons.kotlinCode
import dev.kuromiichi.init.data.lessons.paragraph
import dev.kuromiichi.init.data.lessons.regular
import dev.kuromiichi.init.data.lessons.richText
import dev.kuromiichi.init.data.lessons.steps

val functionsModularityLessonContent =
    LessonContent(
        lessonId = "functions-modularity",
        steps =
            listOf(
                LessonStep(
                    title = "Dividir un problema en partes",
                    explanation =
                        listOf(
                            paragraph(regular("Hasta ahora has escrito todo el programa dentro de "), inlineCode("main"), regular(". Si necesitas pedir tres números válidos, acabas copiando el mismo bucle tres veces, y si encuentras un fallo tienes que corregirlo en cada copia.")),
                            paragraph(regular("Una "), highlight("función"), regular(" es un bloque de código con nombre que se encarga de una tarea concreta. La escribes una vez y la usas siempre que la necesites.")),
                            paragraph(regular("La "), highlight("programación modular"), regular(" consiste en dividir un problema en partes más pequeñas y resolver cada una con una función; "), inlineCode("main"), regular(" se limita a combinarlas. Así el código es más fácil de leer, probar, depurar y reutilizar.")),
                        ),
                    visual = LessonVisualContent.Flow(listOf("Problema", "Subproblemas", "Una función por tarea", "main las combina")),
                ),
                LessonStep(
                    title = "Declarar y llamar a una función",
                    explanation =
                        listOf(
                            paragraph(regular("Ya has usado funciones: "), inlineCode("main"), regular(", "), inlineCode("println"), regular(" y "), inlineCode("readln"), regular(". Para crear una propia, escribe "), inlineCode("fun"), regular(", su nombre, unos paréntesis y, entre llaves, el "), emphasis("cuerpo"), regular(" con sus instrucciones. Eso es la "), highlight("declaración"), regular(".")),
                            paragraph(regular("Declarar una función no la ejecuta. Se ejecuta cuando la "), highlight("llamas"), regular(" escribiendo su nombre seguido de paréntesis: el programa entra en el cuerpo, lo ejecuta y continúa justo después de la llamada.")),
                            paragraph(regular("Las funciones se escriben fuera de "), inlineCode("main"), regular(", en el mismo archivo, y el orden no importa: puedes llamar a una función que aparece más abajo.")),
                        ),
                    visual =
                        LessonVisualContent.CodeExamples(
                            listOf(
                                LessonCodeExample(
                                    title = "Un saludo reutilizable",
                                    lines =
                                        kotlinCode(
                                            """
                                            fun saludar() {
                                                println("Hola")
                                                println("Empezamos")
                                            }

                                            fun main() {
                                                saludar()
                                                println("---")
                                                saludar()
                                            }
                                            """.trimIndent(),
                                        ),
                                    note = richText(regular("El cuerpo de "), inlineCode("saludar"), regular(" se ejecuta en cada llamada, por eso aparecen "), inlineCode("Hola"), regular(" y "), inlineCode("Empezamos"), regular(" antes y después de "), inlineCode("---"), regular(".")),
                                ),
                            ),
                        ),
                ),
                LessonStep(
                    title = "Pasar datos con parámetros",
                    explanation =
                        listOf(
                            paragraph(regular("Un "), highlight("parámetro"), regular(" es una variable que la función recibe cuando la llamas. Se escribe entre los paréntesis, con su nombre y su tipo, que es obligatorio.")),
                            paragraph(regular("Los valores que pasas en la llamada son los "), highlight("argumentos"), regular(" y se asignan en orden. En el ejemplo, "), inlineCode("\"Lucía\""), regular(" va a "), inlineCode("nombre"), regular(" y "), inlineCode("2"), regular(" a "), inlineCode("veces"), regular(". Si el tipo no coincide, el programa no compila.")),
                            paragraph(regular("Así, una misma función sirve para datos distintos. Cuando la cabecera es larga, se suele poner un parámetro por línea; la coma después del último es opcional.")),
                        ),
                    visual =
                        LessonVisualContent.CodeExamples(
                            listOf(
                                LessonCodeExample(
                                    title = "Un saludo con datos",
                                    lines =
                                        kotlinCode(
                                            $$"""
                                                fun saludar(
                                                    nombre: String,
                                                    veces: Int,
                                                ) {
                                                    for (i in 1..veces) {
                                                        println("Hola, $nombre")
                                                    }
                                                }

                                                fun main() {
                                                    saludar("Lucía", 2)
                                                    saludar("Mario", 1)
                                                }
                                            """.trimIndent(),
                                        ),
                                    note = richText(regular("Muestra dos veces "), inlineCode("Hola, Lucía"), regular(" y una vez "), inlineCode("Hola, Mario"), regular(".")),
                                ),
                            ),
                        ),
                ),
                LessonStep(
                    title = "Devolver un resultado",
                    explanation =
                        listOf(
                            paragraph(regular("Una función también puede calcular un valor y entregárselo a quien la llama. El tipo de ese valor es el "), highlight("tipo de retorno"), regular(" y se escribe después de los parámetros: aquí, "), inlineCode(": Int"), regular(" en la línea 4.")),
                            paragraph(inlineCode("return"), regular(" entrega el valor y termina la función. En el "), inlineCode("main"), regular(" del ejemplo, cada llamada a "), inlineCode("calcularMedia"), regular(" se sustituye por el valor devuelto, que puedes guardar en una variable, mostrar o usar en una operación, como en la línea 11.")),
                            paragraph(regular("Además, la fórmula queda en un solo sitio. Si tuviera un error, como olvidar los paréntesis de "), inlineCode("(a + b)"), regular(", bastaría con corregirlo una vez para que todas las llamadas dieran el resultado correcto.")),
                        ),
                    visual =
                        LessonVisualContent.CodeExamples(
                            listOf(
                                LessonCodeExample(
                                    title = "Calcular una media",
                                    lines =
                                        kotlinCode(
                                            """
                                            fun calcularMedia(
                                                a: Int,
                                                b: Int,
                                            ): Int {
                                                return (a + b) / 2
                                            }

                                            fun main() {
                                                val media = calcularMedia(6, 9)
                                                println(media)
                                                println(calcularMedia(10, 5) + 1)
                                            }
                                            """.trimIndent(),
                                        ),
                                    note = richText(regular("Muestra "), inlineCode("7"), regular(" y "), inlineCode("8"), regular(": la segunda llamada devuelve "), inlineCode("7"), regular(" y a ese valor se le suma 1.")),
                                ),
                            ),
                        ),
                ),
                LessonStep(
                    title = "Calcular o mostrar",
                    explanation =
                        listOf(
                            paragraph(regular("Si no indicas tipo de retorno, la función devuelve "), inlineCode("Unit"), regular(", es decir, no entrega ningún dato: solo hace algo, como mostrar un texto. A veces se las llama "), emphasis("procedimientos"), regular(", aunque en Kotlin todas son funciones.")),
                            paragraph(regular("Conviene separar "), highlight("calcular"), regular(" de "), highlight("mostrar"), regular(". Una función que calcula devuelve el resultado y deja que otra parte del programa decida qué hacer con él. Así podrás reutilizarla aunque no haya consola y, más adelante, probarla automáticamente.")),
                            paragraph(regular("Si "), inlineCode("calcularArea"), regular(", la función del ejemplo, usara "), inlineCode("println"), regular(" en lugar de "), inlineCode("return"), regular(", el área solo aparecería en pantalla y no podrías usarla para nada más.")),
                        ),
                    visual =
                        LessonVisualContent.CodeExamples(
                            listOf(
                                LessonCodeExample(
                                    title = "Una función calcula y otra muestra",
                                    lines =
                                        kotlinCode(
                                            $$"""
                                                fun calcularArea(
                                                    base: Int,
                                                    altura: Int,
                                                ): Int {
                                                    return base * altura
                                                }

                                                fun mostrarArea(area: Int) {
                                                    println("Área: $area m²")
                                                }

                                                fun main() {
                                                    val area = calcularArea(4, 3)
                                                    mostrarArea(area)
                                                }
                                            """.trimIndent(),
                                        ),
                                    note = richText(regular("Muestra "), inlineCode("Área: 12 m²"), regular(".")),
                                ),
                            ),
                        ),
                ),
                LessonStep(
                    title = "Terminar antes con return",
                    explanation =
                        listOf(
                            paragraph(inlineCode("return"), regular(" termina la función en ese mismo momento, aunque esté dentro de un bucle. En la lección de bucles necesitabas una bandera y "), inlineCode("break"), regular(" para saber si un número era primo; ahora basta con devolver "), inlineCode("false"), regular(" en cuanto aparece un divisor.")),
                            paragraph(regular("Si el bucle termina sin encontrar ninguno, se llega al "), inlineCode("return true"), regular(" del final. Una función con tipo de retorno debe devolver un valor en todos los casos; si falta alguno, no compila.")),
                            paragraph(regular("Cuando todo el cuerpo es una sola expresión, puedes escribirla después de "), inlineCode("="), regular(", sin llaves ni "), inlineCode("return"), regular(", como hace "), inlineCode("esPar"), regular(" en el ejemplo.")),
                        ),
                    visual =
                        LessonVisualContent.CodeExamples(
                            listOf(
                                LessonCodeExample(
                                    title = "Responder sí o no",
                                    lines =
                                        kotlinCode(
                                            """
                                            fun esPrimo(n: Int): Boolean {
                                                if (n < 2) {
                                                    return false
                                                }
                                                for (d in 2..<n) {
                                                    if (n % d == 0) {
                                                        return false
                                                    }
                                                }
                                                return true
                                            }

                                            fun esPar(n: Int): Boolean =
                                                n % 2 == 0
                                            """.trimIndent(),
                                        ),
                                    note = richText(inlineCode("esPrimo(21)"), regular(" devuelve "), inlineCode("false"), regular(" al llegar a "), inlineCode("3"), regular("; "), inlineCode("esPrimo(13)"), regular(" y "), inlineCode("esPar(4)"), regular(" devuelven "), inlineCode("true"), regular(".")),
                                ),
                            ),
                        ),
                ),
                LessonStep(
                    title = "Valores por defecto y argumentos con nombre",
                    explanation =
                        listOf(
                            paragraph(regular("Un parámetro puede tener un "), highlight("valor por defecto"), regular(", que se escribe después de "), inlineCode("="), regular(". Si la llamada no le pasa ningún argumento, se usa ese valor.")),
                            paragraph(regular("Con los "), highlight("argumentos con nombre"), regular(" indicas a qué parámetro corresponde cada valor escribiendo "), inlineCode("nombre = valor"), regular(". Así puedes omitir los que tienen valor por defecto o pasarlos en otro orden.")),
                            paragraph(regular("También hacen más legibles las llamadas con números: en el ejemplo, "), inlineCode("trazar(largo = 5)"), regular(" se entiende mejor que "), inlineCode("trazar(5)"), regular(".")),
                        ),
                    visual =
                        LessonVisualContent.CodeExamples(
                            listOf(
                                LessonCodeExample(
                                    title = "Líneas a medida",
                                    lines =
                                        kotlinCode(
                                            """
                                            fun trazar(
                                                largo: Int = 10,
                                                simbolo: String = "-",
                                            ) {
                                                for (i in 1..largo) {
                                                    print(simbolo)
                                                }
                                                println()
                                            }

                                            fun main() {
                                                trazar()
                                                trazar(5)
                                                trazar(simbolo = "*")
                                                trazar(simbolo = "=", largo = 3)
                                            }
                                            """.trimIndent(),
                                        ),
                                    note = richText(regular("Muestra diez guiones, cinco guiones, diez asteriscos y "), inlineCode("==="), regular(".")),
                                ),
                            ),
                        ),
                ),
                LessonStep(
                    title = "Cada función tiene sus variables",
                    explanation =
                        listOf(
                            paragraph(regular("Las variables que declaras dentro de una función son "), highlight("locales"), regular(": existen mientras la función se ejecuta y desaparecen al terminar. Por eso, en el ejemplo, "), inlineCode("main"), regular(" no puede usar "), inlineCode("resultado"), regular("; el compilador mostraría "), inlineCode("Unresolved reference 'resultado'"), regular(".")),
                            paragraph(regular("Los parámetros también son locales y, además, de solo lectura, como un "), inlineCode("val"), regular(". Si escribes "), inlineCode("n = n * 2"), regular(" dentro de "), inlineCode("duplicar"), regular(", la función del ejemplo, aparece "), inlineCode("'val' cannot be reassigned"), regular(". Guarda el cálculo en una variable nueva.")),
                            callout(
                                "Evita las variables globales",
                                richText(regular("Una variable declarada fuera de cualquier función es visible desde todas. Si es "), inlineCode("var"), regular(", cualquiera puede modificarla y cuesta saber quién lo hizo. Pasa los datos como parámetros y devuélvelos con "), inlineCode("return"), regular(".")),
                                LessonCalloutTone.Warning,
                            ),
                        ),
                    visual =
                        LessonVisualContent.CodeExamples(
                            listOf(
                                LessonCodeExample(
                                    title = "Dos variables con el mismo nombre",
                                    lines =
                                        kotlinCode(
                                            """
                                            fun duplicar(n: Int): Int {
                                                val resultado = n * 2
                                                return resultado
                                            }

                                            fun main() {
                                                val n = 5
                                                println(duplicar(n))
                                                println(n)
                                            }
                                            """.trimIndent(),
                                        ),
                                    note = richText(regular("Muestra "), inlineCode("10"), regular(" y "), inlineCode("5"), regular(". La "), inlineCode("n"), regular(" de "), inlineCode("main"), regular(" y la de "), inlineCode("duplicar"), regular(" son variables distintas, aunque se llamen igual.")),
                                ),
                            ),
                        ),
                ),
                LessonStep(
                    title = "Entrar en una función con el depurador",
                    explanation =
                        listOf(
                            paragraph(regular("Con funciones, el programa salta de una parte del código a otra. Para seguirlo, pon un breakpoint en la línea 10 y lanza "), emphasis("Debug"), regular(".")),
                            steps(
                                richText(emphasis("Step Over"), regular(" ejecutaría toda la llamada y pararía en "), inlineCode("println"), regular(". "), emphasis("Step Into"), regular(", la flecha hacia abajo, entra en "), inlineCode("calcularMedia"), regular(" y se detiene en su primera instrucción.")),
                                richText(regular("En "), emphasis("Threads & Variables"), regular(", "), inlineCode("calcularMedia"), regular(" aparece encima de "), inlineCode("main"), regular(": es la "), highlight("pila de llamadas"), regular(". Las variables que ves son las de la función, "), inlineCode("a"), regular(" y "), inlineCode("b"), regular(".")),
                                richText(emphasis("Step Out"), regular(", la flecha hacia arriba, termina la función y vuelve a "), inlineCode("main"), regular(" con el resultado guardado en "), inlineCode("media"), regular(".")),
                            ),
                            paragraph(regular("Si entras sin querer en una función de Kotlin, como "), inlineCode("println"), regular(", sal con "), emphasis("Step Out"), regular(".")),
                        ),
                    visual =
                        LessonVisualContent.CodeExamples(
                            listOf(
                                LessonCodeExample(
                                    title = "Seguir una llamada",
                                    lines =
                                        kotlinCode(
                                            """
                                            fun calcularMedia(
                                                a: Int,
                                                b: Int,
                                            ): Int {
                                                val suma = a + b
                                                return suma / 2
                                            }

                                            fun main() {
                                                val media = calcularMedia(6, 9)
                                                println(media)
                                            }
                                            """.trimIndent(),
                                        ),
                                    note = richText(regular("Dentro de la función verás "), inlineCode("a = 6"), regular(" y "), inlineCode("b = 9"), regular("; tras un "), emphasis("Step Over"), regular(", también "), inlineCode("suma = 15"), regular(".")),
                                ),
                            ),
                        ),
                ),
                LessonStep(
                    title = "Nombrar y documentar funciones",
                    explanation =
                        listOf(
                            paragraph(regular("El nombre de una función debe decir qué hace. Usa un verbo en "), emphasis("lowerCamelCase"), regular(", como "), inlineCode("calcularMedia"), regular(" o "), inlineCode("leerNota"), regular(". Si la función responde sí o no, suele empezar por "), inlineCode("es"), regular(" o "), inlineCode("tiene"), regular(", como "), inlineCode("esPar"), regular(".")),
                            paragraph(regular("Para documentarla, escribe justo encima un comentario "), highlight("KDoc"), regular(", que empieza por "), inlineCode("/**"), regular(". Describe qué hace la función; "), inlineCode("@param"), regular(" explica cada parámetro y "), inlineCode("@return"), regular(", el valor devuelto.")),
                            paragraph(regular("IntelliJ IDEA muestra esa documentación al pasar el ratón por el nombre de la función, tanto en su declaración como en cualquier llamada.")),
                        ),
                    visual =
                        LessonVisualContent.CodeExamples(
                            listOf(
                                LessonCodeExample(
                                    title = "Una función documentada",
                                    lines =
                                        kotlinCode(
                                            """
                                            /**
                                             * Media entera de dos notas.
                                             *
                                             * @param a primera nota, de 0 a 10.
                                             * @param b segunda nota, de 0 a 10.
                                             * @return la media sin decimales.
                                             */
                                            fun calcularMedia(
                                                a: Int,
                                                b: Int,
                                            ): Int = (a + b) / 2
                                            """.trimIndent(),
                                        ),
                                    note = richText(regular("Documenta lo que no se ve a simple vista en el código, como el rango de las notas o que se pierden los decimales.")),
                                ),
                            ),
                        ),
                ),
                LessonStep(
                    title = "Funciones de bibliotecas y de otros archivos",
                    explanation =
                        listOf(
                            paragraph(regular("Kotlin incluye una biblioteca estándar con muchas funciones organizadas en "), highlight("paquetes"), regular(". Algunas, como "), inlineCode("println"), regular(" o "), inlineCode("maxOf"), regular(", están siempre disponibles; las de "), inlineCode("kotlin.math"), regular(" necesitan un "), inlineCode("import"), regular(" al principio del archivo.")),
                            paragraph(regular("Si escribes "), inlineCode("abs"), regular(" sin haberla importado, IntelliJ IDEA la marca en rojo y te ofrece añadir el "), inlineCode("import"), regular(".")),
                            paragraph(regular("También puedes repartir tus propias funciones en varios archivos. Si creas "), inlineCode("Entrada.kt"), regular(" junto a "), inlineCode("Main.kt"), regular(", en "), inlineCode("src/main/kotlin"), regular(", puedes llamar a sus funciones desde "), inlineCode("main"), regular(" sin "), inlineCode("import"), regular(", porque están en el mismo paquete.")),
                        ),
                    visual =
                        LessonVisualContent.CodeExamples(
                            listOf(
                                LessonCodeExample(
                                    title = "Funciones de la biblioteca estándar",
                                    lines =
                                        kotlinCode(
                                            """
                                            import kotlin.math.abs
                                            import kotlin.math.sqrt

                                            fun main() {
                                                println(abs(-7))
                                                println(sqrt(16.0))
                                                println(maxOf(3, 8))
                                            }
                                            """.trimIndent(),
                                        ),
                                    note = richText(regular("Muestra "), inlineCode("7"), regular(", "), inlineCode("4.0"), regular(" y "), inlineCode("8"), regular(". "), inlineCode("sqrt"), regular(" trabaja con decimales, por eso recibe "), inlineCode("16.0"), regular(".")),
                                ),
                            ),
                        ),
                ),
                LessonStep(
                    title = "Una función que se llama a sí misma",
                    explanation =
                        listOf(
                            paragraph(regular("Una función "), highlight("recursiva"), regular(" se llama a sí misma para resolver una versión más pequeña del problema. Necesita un "), emphasis("caso base"), regular(" en el que devuelva un valor sin volver a llamarse; aquí, "), inlineCode("n <= 1"), regular(".")),
                            paragraph(regular("Cada llamada espera a que termine la siguiente. Por eso la tabla se lee hacia abajo al llamar y hacia arriba al devolver: "), inlineCode("1"), regular(", "), inlineCode("2"), regular(", "), inlineCode("6"), regular(" y "), inlineCode("24"), regular(".")),
                            paragraph(regular("Sin caso base, las llamadas no terminarían nunca y el programa se detendría con "), inlineCode("StackOverflowError"), regular(". El factorial también se puede calcular con un "), inlineCode("for"), regular("; la recursividad resulta útil sobre todo en problemas que se dividen en otros del mismo tipo.")),
                        ),
                    visual =
                        LessonVisualContent.Trace(
                            example =
                                LessonCodeExample(
                                    title = "Factorial de 4",
                                    lines =
                                        kotlinCode(
                                            """
                                            fun factorial(n: Int): Int {
                                                if (n <= 1) {
                                                    return 1
                                                }
                                                return n * factorial(n - 1)
                                            }
                                            """.trimIndent(),
                                        ),
                                    note = richText(inlineCode("println(factorial(4))"), regular(" muestra "), inlineCode("24"), regular(", que es "), inlineCode("4 * 3 * 2 * 1"), regular(".")),
                                ),
                            table =
                                LessonTraceTable(
                                    title = "Cada llamada",
                                    columns = listOf("n", "Llama a", "Devuelve"),
                                    rows =
                                        listOf(
                                            listOf("4", "factorial(3)", "4 * 6 = 24"),
                                            listOf("3", "factorial(2)", "3 * 2 = 6"),
                                            listOf("2", "factorial(1)", "2 * 1 = 2"),
                                            listOf("1", "Caso base", "1"),
                                        ),
                                ),
                        ),
                ),
                LessonStep(
                    title = "Ahora te toca: ordenar un programa largo",
                    explanation =
                        listOf(
                            paragraph(regular("Este programa funciona, pero repite tres veces el mismo bucle de lectura y mezcla los cálculos con los mensajes, todo dentro de "), inlineCode("main"), regular(".")),
                            paragraph(highlight("Refactorizar"), regular(" es reorganizar el código sin cambiar lo que hace. Antes de tocar nada, ejecútalo con los datos del resultado esperado: cuando termines, la salida debe ser exactamente la misma.")),
                            paragraph(regular("Haz los cambios de uno en uno y vuelve a ejecutar después de cada uno.")),
                        ),
                    visual =
                        LessonVisualContent.CodeExamples(
                            listOf(
                                LessonCodeExample(
                                    title = "Notas de un trimestre",
                                    lines =
                                        kotlinCode(
                                            $$"""
                                                fun main() {
                                                    var suma = 0
                                                    println("Nota del examen:")
                                                    var nota = readln().toIntOrNull()
                                                    while (nota == null) {
                                                        println("Escribe un número")
                                                        nota = readln().toIntOrNull()
                                                    }
                                                    suma += nota
                                                    println("Nota de la práctica:")
                                                    nota = readln().toIntOrNull()
                                                    while (nota == null) {
                                                        println("Escribe un número")
                                                        nota = readln().toIntOrNull()
                                                    }
                                                    suma += nota
                                                    println("Nota del proyecto:")
                                                    nota = readln().toIntOrNull()
                                                    while (nota == null) {
                                                        println("Escribe un número")
                                                        nota = readln().toIntOrNull()
                                                    }
                                                    suma += nota
                                                    val media = suma / 3
                                                    println("Media: $media")
                                                    if (media < 5) {
                                                        println("Suspenso")
                                                    } else if (media < 7) {
                                                        println("Aprobado")
                                                    } else if (media < 9) {
                                                        println("Notable")
                                                    } else {
                                                        println("Sobresaliente")
                                                    }
                                                }
                                            """.trimIndent(),
                                        ),
                                    note = richText(regular("Funciona, pero cualquier cambio en la lectura hay que repetirlo tres veces.")),
                                    isCopyable = true,
                                ),
                            ),
                        ),
                    links =
                        listOf(
                            LessonLink("Funciones en Kotlin", "https://kotlinlang.org/docs/functions.html"),
                            LessonLink("Nombres de funciones en Kotlin", "https://kotlinlang.org/docs/coding-conventions.html#function-names"),
                            LessonLink("Documentar código con KDoc", "https://kotlinlang.org/docs/kotlin-doc.html"),
                            LessonLink("Paquetes e importaciones en Kotlin", "https://kotlinlang.org/docs/packages.html"),
                        ),
                    practice =
                        LessonPractice(
                            id = "functions-modularity-grades",
                            objective = richText(highlight("Dividir un programa en funciones, cada una con una sola tarea.")),
                            statement = richText(regular("Reorganiza el programa de ejemplo en funciones sin cambiar lo que muestra.")),
                            requirements =
                                listOf(
                                    richText(regular("Crea "), inlineCode("leerNota(mensaje: String): Int"), regular(", que muestre el mensaje y vuelva a leer hasta recibir un número. Debe ser el único sitio con ese bucle.")),
                                    richText(regular("Crea "), inlineCode("calcularMedia"), regular(", que reciba las tres notas y devuelva la media, y "), inlineCode("calificar(nota: Int): String"), regular(", que devuelva el texto de la calificación.")),
                                    richText(regular("Las funciones que calculan "), emphasis("no muestran nada"), regular(": todos los "), inlineCode("println"), regular(" de resultados se quedan en "), inlineCode("main"), regular(".")),
                                    richText(regular("Documenta al menos una función con KDoc.")),
                                    richText(regular("Con los mismos datos, la salida es idéntica a la del programa original.")),
                                ),
                            expectedOutput =
                                listOf(
                                    consoleRun(
                                        consoleOutput("Nota del examen:"),
                                        consoleInput("7"),
                                        consoleOutput("Nota de la práctica:"),
                                        consoleInput("8"),
                                        consoleOutput("Nota del proyecto:"),
                                        consoleInput("6"),
                                        consoleOutput("Media: 7"),
                                        consoleOutput("Notable"),
                                    ),
                                    consoleRun(
                                        consoleOutput("Nota del examen:"),
                                        consoleInput("once"),
                                        consoleOutput("Escribe un número"),
                                        consoleInput("4"),
                                        consoleOutput("Nota de la práctica:"),
                                        consoleInput("5"),
                                        consoleOutput("Nota del proyecto:"),
                                        consoleInput("3"),
                                        consoleOutput("Media: 4"),
                                        consoleOutput("Suspenso"),
                                    ),
                                ),
                            hints =
                                listOf(
                                    richText(regular("Empieza por "), inlineCode("leerNota"), regular(": copia uno de los bloques de lectura, muestra primero el mensaje recibido y termina con "), inlineCode("return"), regular(".")),
                                    richText(regular("Cuando "), inlineCode("leerNota"), regular(" funcione, cada bloque de lectura de "), inlineCode("main"), regular(" se reduce a una línea, como "), inlineCode("val examen = leerNota(\"Nota del examen:\")"), regular(".")),
                                    richText(regular("En "), inlineCode("calificar"), regular(", cambia cada "), inlineCode("println"), regular(" por un "), inlineCode("return"), regular(" con el mismo texto; así "), inlineCode("main"), regular(" podrá mostrar lo que devuelva.")),
                                ),
                            extension = richText(regular("Mueve "), inlineCode("leerNota"), regular(" a un archivo "), inlineCode("Entrada.kt"), regular(" y añádele los parámetros "), inlineCode("minimo"), regular(" y "), inlineCode("maximo"), regular(", con "), inlineCode("0"), regular(" y "), inlineCode("10"), regular(" como valores por defecto. Haz que también repita la lectura si la nota queda fuera de ese rango.")),
                            confirmation = richText(regular("Confirma que las dos ejecuciones de ejemplo dan la misma salida que el programa original y que "), inlineCode("main"), regular(" ya no tiene ningún bucle.")),
                        ),
                ),
            ),
    )
