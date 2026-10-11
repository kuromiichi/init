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
import dev.kuromiichi.init.data.lessons.consoleOutput
import dev.kuromiichi.init.data.lessons.consoleRun
import dev.kuromiichi.init.data.lessons.emphasis
import dev.kuromiichi.init.data.lessons.highlight
import dev.kuromiichi.init.data.lessons.inlineCode
import dev.kuromiichi.init.data.lessons.kotlinCode
import dev.kuromiichi.init.data.lessons.paragraph
import dev.kuromiichi.init.data.lessons.regular
import dev.kuromiichi.init.data.lessons.richText

val functionalProgrammingLessonContent =
    LessonContent(
        lessonId = "functional-programming",
        steps =
            listOf(
                LessonStep(
                    title = "Funciones como valores",
                    explanation =
                        listOf(
                            paragraph(regular("Hasta ahora has programado paso a paso: variables que cambian y bucles que recorren. Es el estilo "), emphasis("imperativo"), regular(": le dices al ordenador cómo hacer cada cosa.")),
                            paragraph(regular("La "), highlight("programación funcional"), regular(" trata las funciones como un valor más, igual que un número o un texto: se guardan en variables, se pasan a otras funciones y se devuelven. Así se describe "), emphasis("qué"), regular(" quieres y se escriben menos bucles.")),
                            paragraph(regular("Kotlin mezcla los dos estilos. En esta lección usarás la parte funcional donde deja el código más corto y claro, sobre todo al trabajar con colecciones.")),
                        ),
                    visual = LessonVisualContent.Flow(listOf("Una función es un valor", "Se guarda o se pasa", "Otra función la aplica", "Menos bucles, más intención")),
                ),
                LessonStep(
                    title = "Una función en una variable",
                    explanation =
                        listOf(
                            paragraph(regular("Una "), highlight("lambda"), regular(" es una función sin nombre escrita entre llaves. Antes de la flecha "), inlineCode("->"), regular(" van sus parámetros y después, el cuerpo. En el ejemplo, "), inlineCode("{ n: Int -> n * 2 }"), regular(" recibe un número y devuelve el doble.")),
                            paragraph(regular("La lambda se guarda en una variable y se llama con paréntesis, como cualquier función. No lleva "), inlineCode("return"), regular(": el resultado es la "), emphasis("última expresión"), regular(" del cuerpo, aunque tenga varias líneas.")),
                        ),
                    visual =
                        LessonVisualContent.CodeExamples(
                            listOf(
                                LessonCodeExample(
                                    title = "Una lambda que duplica",
                                    lines =
                                        kotlinCode(
                                            """
                                            fun main() {
                                                val doble = { n: Int -> n * 2 }
                                                println(doble(4))
                                                println(doble(10))
                                            }
                                            """.trimIndent(),
                                        ),
                                    note = richText(regular("Muestra "), inlineCode("8"), regular(" y "), inlineCode("20"), regular(".")),
                                ),
                                LessonCodeExample(
                                    title = "Una lambda de varias líneas",
                                    lines =
                                        kotlinCode(
                                            $$"""
                                                fun main() {
                                                    val describir = { nota: Int ->
                                                        val texto = if (nota >= 5) "aprobado" else "suspenso"
                                                        "$nota: $texto"
                                                    }
                                                    println(describir(7))
                                                    println(describir(3))
                                                }
                                            """.trimIndent(),
                                        ),
                                    note = richText(regular("Muestra "), inlineCode("7: aprobado"), regular(" y "), inlineCode("3: suspenso"), regular(". El texto de la última línea es lo que devuelve.")),
                                ),
                            ),
                        ),
                ),
                LessonStep(
                    title = "El tipo de una función",
                    explanation =
                        listOf(
                            paragraph(regular("Una función también tiene tipo: "), inlineCode("(Int) -> Boolean"), regular(" es «recibe un "), inlineCode("Int"), regular(" y devuelve un "), inlineCode("Boolean"), regular("». Sin parámetros se escribe "), inlineCode("() -> Unit"), regular(", y "), inlineCode("Unit"), regular(" significa que no devuelve nada útil.")),
                            paragraph(regular("Si la variable declara el tipo, la lambda no necesita repetirlo. Y si tiene un solo parámetro, puedes no nombrarlo: Kotlin lo llama "), inlineCode("it"), regular(". En el ejemplo, "), inlineCode("{ it % 2 == 0 }"), regular(" equivale a "), inlineCode("{ n -> n % 2 == 0 }"), regular(".")),
                        ),
                    visual =
                        LessonVisualContent.CodeExamples(
                            listOf(
                                LessonCodeExample(
                                    title = "Tres tipos de función",
                                    lines =
                                        kotlinCode(
                                            """
                                            fun main() {
                                                val esPar: (Int) -> Boolean = { it % 2 == 0 }
                                                val saludar: () -> Unit = { println("Hola") }
                                                val sumar: (Int, Int) -> Int = { a, b -> a + b }
                                                println(esPar(4))
                                                saludar()
                                                println(sumar(2, 3))
                                            }
                                            """.trimIndent(),
                                        ),
                                    note = richText(regular("Muestra "), inlineCode("true"), regular(", "), inlineCode("Hola"), regular(" y "), inlineCode("5"), regular(". Con dos parámetros no hay "), inlineCode("it"), regular(": hay que nombrarlos.")),
                                ),
                            ),
                        ),
                ),
                LessonStep(
                    title = "Funciones que reciben funciones",
                    explanation =
                        listOf(
                            paragraph(regular("Si un parámetro tiene tipo de función, la función que lo recibe es "), highlight("de orden superior"), regular(". En el ejemplo, "), inlineCode("aplicar"), regular(" no sabe qué operación hará: la recibe en "), inlineCode("operacion"), regular(" y la llama con "), inlineCode("n"), regular(".")),
                            paragraph(regular("Cada llamada pasa una lambda distinta y obtiene un resultado distinto sin cambiar "), inlineCode("aplicar"), regular(". Es la idea que hay detrás de todo lo que verás en esta lección: separar el recorrido de lo que se hace con cada dato.")),
                        ),
                    visual =
                        LessonVisualContent.Trace(
                            example =
                                LessonCodeExample(
                                    title = "Una operación que llega de fuera",
                                    lines =
                                        kotlinCode(
                                            """
                                            fun aplicar(n: Int, operacion: (Int) -> Int): Int {
                                                return operacion(n)
                                            }

                                            fun main() {
                                                println(aplicar(5, { it * 2 }))
                                                println(aplicar(5, { it + 100 }))
                                                println(aplicar(5, { it * it }))
                                            }
                                            """.trimIndent(),
                                        ),
                                    note = richText(regular("Muestra "), inlineCode("10"), regular(", "), inlineCode("105"), regular(" y "), inlineCode("25"), regular(".")),
                                ),
                            table =
                                LessonTraceTable(
                                    title = "Qué hace cada llamada",
                                    columns = listOf("Línea", "n", "operacion", "Devuelve"),
                                    rows =
                                        listOf(
                                            listOf("6", "5", "{ it * 2 }", "10"),
                                            listOf("7", "5", "{ it + 100 }", "105"),
                                            listOf("8", "5", "{ it * it }", "25"),
                                        ),
                                ),
                        ),
                ),
                LessonStep(
                    title = "La lambda, fuera del paréntesis",
                    explanation =
                        listOf(
                            paragraph(regular("Si el último parámetro es una función, la lambda se escribe "), emphasis("después"), regular(" del paréntesis: "), inlineCode("aplicar(5) { it * 2 }"), regular(". Si es el único argumento, el paréntesis desaparece. IntelliJ IDEA te lo propone al ver una lambda dentro.")),
                            paragraph(inlineCode("repeat(3) { … }"), regular(" repite la lambda tres veces y le pasa el número de vuelta, desde "), inlineCode("0"), regular(". Las llaves parecen las de un bucle, pero son una lambda.")),
                            paragraph(regular("Ya lo habías usado: en "), inlineCode("require(personas > 0) { \"…\" }"), regular(", el mensaje es una lambda. Solo se ejecuta si la condición falla, así que no se construye el texto cuando no hace falta.")),
                        ),
                    visual =
                        LessonVisualContent.CodeExamples(
                            listOf(
                                LessonCodeExample(
                                    title = "Lambdas al final",
                                    lines =
                                        kotlinCode(
                                            $$"""
                                                fun aplicar(n: Int, operacion: (Int) -> Int): Int = operacion(n)

                                                fun main() {
                                                    println(aplicar(5) { it * 2 })
                                                    repeat(3) { vuelta ->
                                                        println("Vuelta $vuelta")
                                                    }
                                                }
                                            """.trimIndent(),
                                        ),
                                    note = richText(regular("Muestra "), inlineCode("10"), regular(", "), inlineCode("Vuelta 0"), regular(", "), inlineCode("Vuelta 1"), regular(" y "), inlineCode("Vuelta 2"), regular(".")),
                                ),
                                LessonCodeExample(
                                    title = "El mensaje de require",
                                    lines =
                                        kotlinCode(
                                            """
                                            fun repartir(total: Double, personas: Int): Double {
                                                require(personas > 0) { "Hace falta al menos una persona" }
                                                return total / personas
                                            }

                                            fun main() {
                                                println(repartir(30.0, 3))
                                            }
                                            """.trimIndent(),
                                        ),
                                    note = richText(regular("Muestra "), inlineCode("10.0"), regular(". Con "), inlineCode("0"), regular(" personas lanzaría la excepción con ese mensaje.")),
                                ),
                            ),
                        ),
                ),
                LessonStep(
                    title = "Pasar una función con nombre",
                    explanation =
                        listOf(
                            paragraph(regular("Una función normal también se puede pasar como valor. "), inlineCode("::esPar"), regular(" es una "), emphasis("referencia"), regular(" a la función "), inlineCode("esPar"), regular(": no la llama, la entrega para que otra la llame.")),
                            paragraph(regular("Sirve cualquier función o lambda con el tipo adecuado. En el ejemplo, "), inlineCode("contarSi"), regular(" cuenta con un bucle los números que cumplen la condición recibida; enseguida verás que Kotlin ya trae una función que hace eso.")),
                        ),
                    visual =
                        LessonVisualContent.CodeExamples(
                            listOf(
                                LessonCodeExample(
                                    title = "Una referencia y una lambda",
                                    lines =
                                        kotlinCode(
                                            """
                                            fun esPar(n: Int): Boolean = n % 2 == 0

                                            fun contarSi(numeros: List<Int>, condicion: (Int) -> Boolean): Int {
                                                var total = 0
                                                for (n in numeros) {
                                                    if (condicion(n)) total++
                                                }
                                                return total
                                            }

                                            fun main() {
                                                val numeros = listOf(3, 8, 10, 7, 4)
                                                println(contarSi(numeros, ::esPar))
                                                println(contarSi(numeros) { it > 7 })
                                            }
                                            """.trimIndent(),
                                        ),
                                    note = richText(regular("Muestra "), inlineCode("3"), regular(" (8, 10 y 4) y "), inlineCode("2"), regular(" (8 y 10).")),
                                ),
                            ),
                        ),
                ),
                LessonStep(
                    title = "Lambdas que recuerdan",
                    explanation =
                        listOf(
                            paragraph(regular("Una lambda puede usar variables de donde se escribió y las sigue viendo después, aunque esa función ya haya terminado. A esto se le llama "), highlight("clausura"), regular(" ("), emphasis("closure"), regular(").")),
                            paragraph(regular("En el ejemplo, cada llamada a "), inlineCode("crearContador"), regular(" crea su propia "), inlineCode("cuenta"), regular(" y devuelve una lambda que la incrementa. "), inlineCode("siguiente"), regular(" y "), inlineCode("otro"), regular(" no comparten la variable: cada lambda recuerda la suya.")),
                        ),
                    visual =
                        LessonVisualContent.Trace(
                            example =
                                LessonCodeExample(
                                    title = "Dos contadores independientes",
                                    lines =
                                        kotlinCode(
                                            """
                                            fun crearContador(): () -> Int {
                                                var cuenta = 0
                                                return {
                                                    cuenta++
                                                    cuenta
                                                }
                                            }

                                            fun main() {
                                                val siguiente = crearContador()
                                                println(siguiente())
                                                println(siguiente())
                                                val otro = crearContador()
                                                println(otro())
                                                println(siguiente())
                                            }
                                            """.trimIndent(),
                                        ),
                                    note = richText(regular("Muestra "), inlineCode("1"), regular(", "), inlineCode("2"), regular(", "), inlineCode("1"), regular(" y "), inlineCode("3"), regular(".")),
                                ),
                            table =
                                LessonTraceTable(
                                    title = "La cuenta de cada lambda tras cada línea",
                                    columns = listOf("Línea", "cuenta de siguiente", "cuenta de otro", "Muestra"),
                                    rows =
                                        listOf(
                                            listOf("10", "0", "–", "–"),
                                            listOf("11", "1", "–", "1"),
                                            listOf("12", "2", "–", "2"),
                                            listOf("13", "2", "0", "–"),
                                            listOf("14", "2", "1", "1"),
                                            listOf("15", "3", "1", "3"),
                                        ),
                                ),
                        ),
                ),
                LessonStep(
                    title = "Recorrer y filtrar sin bucle",
                    explanation =
                        listOf(
                            paragraph(regular("Las colecciones traen funciones de orden superior que hacen el recorrido por ti. "), inlineCode("forEach"), regular(" ejecuta la lambda con cada elemento, como un "), inlineCode("for"), regular(".")),
                            paragraph(inlineCode("filter"), regular(" se queda con los elementos para los que la lambda devuelve "), inlineCode("true"), regular(" y los entrega en una "), emphasis("lista nueva"), regular(". La original no cambia: en el ejemplo, "), inlineCode("notas"), regular(" sigue teniendo las cinco.")),
                            paragraph(regular("Compara las dos versiones: el bucle dice cómo construir la lista paso a paso; "), inlineCode("filter"), regular(" solo dice qué condición deben cumplir.")),
                        ),
                    visual =
                        LessonVisualContent.CodeExamples(
                            listOf(
                                LessonCodeExample(
                                    title = "forEach y filter",
                                    lines =
                                        kotlinCode(
                                            $$"""
                                                fun main() {
                                                    val notas = listOf(7, 4, 9, 3, 6)
                                                    notas.forEach { println("Nota: $it") }
                                                    val aprobadas = notas.filter { it >= 5 }
                                                    println(aprobadas)
                                                    println(notas)
                                                }
                                            """.trimIndent(),
                                        ),
                                    note = richText(regular("Muestra una línea por nota, "), inlineCode("[7, 9, 6]"), regular(" y "), inlineCode("[7, 4, 9, 3, 6]"), regular(".")),
                                ),
                                LessonCodeExample(
                                    title = "Lo mismo con un bucle",
                                    lines =
                                        kotlinCode(
                                            """
                                            fun main() {
                                                val notas = listOf(7, 4, 9, 3, 6)
                                                val aprobadas = mutableListOf<Int>()
                                                for (nota in notas) {
                                                    if (nota >= 5) aprobadas.add(nota)
                                                }
                                                println(aprobadas)
                                            }
                                            """.trimIndent(),
                                        ),
                                    note = richText(regular("Muestra "), inlineCode("[7, 9, 6]"), regular(". Cuatro líneas y una lista mutable para lo que "), inlineCode("filter"), regular(" hace en una.")),
                                ),
                            ),
                        ),
                ),
                LessonStep(
                    title = "Transformar con map",
                    explanation =
                        listOf(
                            paragraph(inlineCode("map"), regular(" aplica la lambda a cada elemento y devuelve una lista nueva con los resultados, del mismo tamaño y en el mismo orden.")),
                            paragraph(regular("El tipo puede cambiar: en el ejemplo, de una lista de "), inlineCode("Alumno"), regular(" sale una de "), inlineCode("String"), regular(" con sus nombres. Con "), inlineCode("copy"), regular(" se obtienen alumnos nuevos con la nota cambiada, sin tocar los originales.")),
                        ),
                    visual =
                        LessonVisualContent.CodeExamples(
                            listOf(
                                LessonCodeExample(
                                    title = "Cada elemento, transformado",
                                    lines =
                                        kotlinCode(
                                            """
                                            data class Alumno(val nombre: String, val nota: Int)

                                            fun main() {
                                                val notas = listOf(7, 4, 9)
                                                println(notas.map { it + 1 })
                                                val alumnos = listOf(Alumno("Ana", 7), Alumno("Luis", 4))
                                                println(alumnos.map { it.nombre })
                                                println(alumnos.map { it.copy(nota = it.nota + 1) })
                                            }
                                            """.trimIndent(),
                                        ),
                                    note = richText(regular("Muestra "), inlineCode("[8, 5, 10]"), regular(", "), inlineCode("[Ana, Luis]"), regular(" y "), inlineCode("[Alumno(nombre=Ana, nota=8), Alumno(nombre=Luis, nota=5)]"), regular(".")),
                                ),
                            ),
                        ),
                ),
                LessonStep(
                    title = "Preguntar a una colección",
                    explanation =
                        listOf(
                            paragraph(regular("Muchas preguntas sobre una colección se responden con una condición. "), inlineCode("any"), regular(", "), inlineCode("all"), regular(" y "), inlineCode("none"), regular(" dicen si la cumple alguno, todos o ninguno; "), inlineCode("count"), regular(" cuenta cuántos la cumplen.")),
                            paragraph(inlineCode("find"), regular(" devuelve el primer elemento que la cumple, o "), inlineCode("null"), regular(" si no hay ninguno, así que su tipo admite nulo: en el ejemplo, "), inlineCode("Alumno?"), regular(". "), inlineCode("firstOrNull"), regular(" hace lo mismo con otro nombre.")),
                        ),
                    visual =
                        LessonVisualContent.Trace(
                            example =
                                LessonCodeExample(
                                    title = "Preguntas con condición",
                                    lines =
                                        kotlinCode(
                                            """
                                            data class Alumno(val nombre: String, val nota: Int)

                                            fun main() {
                                                val alumnos = listOf(Alumno("Ana", 7), Alumno("Luis", 4), Alumno("Marta", 9))
                                                println(alumnos.any { it.nota < 5 })
                                                println(alumnos.all { it.nota >= 5 })
                                                println(alumnos.none { it.nota == 10 })
                                                println(alumnos.count { it.nota >= 5 })
                                                println(alumnos.find { it.nota > 8 })
                                                println(alumnos.find { it.nota > 9 })
                                            }
                                            """.trimIndent(),
                                        ),
                                    note = richText(regular("Cada línea muestra el resultado de la tabla.")),
                                ),
                            table =
                                LessonTraceTable(
                                    title = "Qué responde cada una",
                                    columns = listOf("Línea", "Pregunta", "Muestra"),
                                    rows =
                                        listOf(
                                            listOf("5", "¿Alguien ha suspendido?", "true"),
                                            listOf("6", "¿Han aprobado todos?", "false"),
                                            listOf("7", "¿Nadie tiene un 10?", "true"),
                                            listOf("8", "¿Cuántos han aprobado?", "2"),
                                            listOf("9", "¿Quién tiene más de un 8?", "Alumno(nombre=Marta, nota=9)"),
                                            listOf("10", "¿Quién tiene más de un 9?", "null"),
                                        ),
                                ),
                        ),
                ),
                LessonStep(
                    title = "Resumir y ordenar",
                    explanation =
                        listOf(
                            paragraph(regular("En estas funciones la lambda dice "), emphasis("con qué dato"), regular(" trabajar. "), inlineCode("sumOf"), regular(" suma lo que devuelve; "), inlineCode("maxByOrNull"), regular(" y "), inlineCode("minByOrNull"), regular(" buscan el elemento con el valor mayor o menor.")),
                            paragraph(inlineCode("sortedBy"), regular(" y "), inlineCode("sortedByDescending"), regular(" devuelven una lista nueva ordenada por ese dato. Con una colección vacía no hay máximo: "), inlineCode("maxByOrNull"), regular(" devuelve "), inlineCode("null"), regular(", y "), inlineCode("?."), regular(" lo trata como ya sabes.")),
                        ),
                    visual =
                        LessonVisualContent.CodeExamples(
                            listOf(
                                LessonCodeExample(
                                    title = "Sumar, buscar el mejor y ordenar",
                                    lines =
                                        kotlinCode(
                                            """
                                            data class Alumno(val nombre: String, val nota: Int)

                                            fun main() {
                                                val alumnos = listOf(Alumno("Ana", 7), Alumno("Luis", 4), Alumno("Marta", 9))
                                                println(alumnos.sumOf { it.nota })
                                                println(alumnos.maxByOrNull { it.nota }?.nombre)
                                                println(alumnos.sortedBy { it.nota }.map { it.nombre })
                                                println(alumnos.sortedByDescending { it.nota }.map { it.nombre })
                                                val nadie = listOf<Alumno>()
                                                println(nadie.maxByOrNull { it.nota })
                                            }
                                            """.trimIndent(),
                                        ),
                                    note = richText(regular("Muestra "), inlineCode("20"), regular(", "), inlineCode("Marta"), regular(", "), inlineCode("[Luis, Ana, Marta]"), regular(", "), inlineCode("[Marta, Ana, Luis]"), regular(" y "), inlineCode("null"), regular(".")),
                                ),
                            ),
                        ),
                ),
                LessonStep(
                    title = "Encadenar operaciones",
                    explanation =
                        listOf(
                            paragraph(regular("Como cada operación devuelve una colección nueva, se pueden "), highlight("encadenar"), regular(": la salida de una es la entrada de la siguiente. Se escribe una por línea, empezando por el punto, y se lee de arriba abajo como una receta.")),
                            paragraph(regular("En el ejemplo: quédate con los aprobados, ordénalos de mayor a menor nota y conviértelos en texto. Ninguna línea modifica "), inlineCode("alumnos"), regular(".")),
                        ),
                    visual =
                        LessonVisualContent.Trace(
                            example =
                                LessonCodeExample(
                                    title = "El cuadro de honor",
                                    lines =
                                        kotlinCode(
                                            $$"""
                                                data class Alumno(val nombre: String, val nota: Int)

                                                fun main() {
                                                    val alumnos = listOf(Alumno("Ana", 7), Alumno("Luis", 4), Alumno("Marta", 9), Alumno("Pau", 6))
                                                    val cuadro =
                                                        alumnos
                                                            .filter { it.nota >= 5 }
                                                            .sortedByDescending { it.nota }
                                                            .map { "${it.nombre} (${it.nota})" }
                                                    println(cuadro)
                                                }
                                            """.trimIndent(),
                                        ),
                                    note = richText(regular("Muestra "), inlineCode("[Marta (9), Ana (7), Pau (6)]"), regular(".")),
                                ),
                            table =
                                LessonTraceTable(
                                    title = "Lo que sale de cada etapa",
                                    columns = listOf("Línea", "Etapa", "Resultado"),
                                    rows =
                                        listOf(
                                            listOf("6", "alumnos", "Ana 7, Luis 4, Marta 9, Pau 6"),
                                            listOf("7", "filter", "Ana 7, Marta 9, Pau 6"),
                                            listOf("8", "sortedByDescending", "Marta 9, Ana 7, Pau 6"),
                                            listOf("9", "map", "[Marta (9), Ana (7), Pau (6)]"),
                                        ),
                                ),
                        ),
                ),
                LessonStep(
                    title = "Funciones puras y efectos",
                    explanation =
                        listOf(
                            paragraph(regular("Una función es "), highlight("pura"), regular(" si su resultado depende solo de sus parámetros y no cambia nada fuera de ella. Con los mismos datos devuelve siempre lo mismo, así que es fácil de entender y de probar.")),
                            paragraph(regular("Mostrar en pantalla o modificar una variable de fuera son "), emphasis("efectos"), regular(". Hacen falta, pero conviene dejarlos en sitios claros, como "), inlineCode("forEach"), regular(" o "), inlineCode("main"), regular(", y no esconderlos dentro de un "), inlineCode("map"), regular(" o un "), inlineCode("filter"), regular(".")),
                            callout(
                                "No todo tiene que ser funcional",
                                richText(regular("Si un bucle se entiende mejor, por ejemplo porque calcula varias cosas a la vez o termina antes con "), inlineCode("break"), regular(", usa el bucle. El objetivo es que el código se lea bien.")),
                                LessonCalloutTone.Tip,
                            ),
                        ),
                    visual =
                        LessonVisualContent.CodeExamples(
                            listOf(
                                LessonCodeExample(
                                    title = "Una función pura",
                                    lines =
                                        kotlinCode(
                                            """
                                            fun conDescuento(precio: Double, porcentaje: Int): Double {
                                                return precio - precio * porcentaje / 100
                                            }

                                            fun main() {
                                                println(conDescuento(80.0, 25))
                                                println(conDescuento(80.0, 25))
                                            }
                                            """.trimIndent(),
                                        ),
                                    note = richText(regular("Muestra "), inlineCode("60.0"), regular(" las dos veces: mismos datos, mismo resultado.")),
                                ),
                                LessonCodeExample(
                                    title = "Un efecto escondido",
                                    lines =
                                        kotlinCode(
                                            """
                                            fun main() {
                                                val notas = listOf(7, 4, 9)
                                                var suma = 0
                                                val dobles =
                                                    notas.map {
                                                        suma += it
                                                        it * 2
                                                    }
                                                println(dobles)
                                                println(suma)
                                            }
                                            """.trimIndent(),
                                        ),
                                    note = richText(regular("Muestra "), inlineCode("[14, 8, 18]"), regular(" y "), inlineCode("20"), regular(", pero el "), inlineCode("map"), regular(" hace dos cosas. Es más claro "), inlineCode("notas.map { it * 2 }"), regular(" por un lado y "), inlineCode("notas.sum()"), regular(" por otro.")),
                                ),
                            ),
                        ),
                ),
                LessonStep(
                    title = "Funciones de extensión",
                    explanation =
                        listOf(
                            paragraph(regular("Una "), highlight("función de extensión"), regular(" añade una función a un tipo que ya existe, sin heredar ni tocar su código. Se declara con el tipo delante del nombre, "), inlineCode("fun String.esPalindromo()"), regular(", y se llama con punto, como un método.")),
                            paragraph(regular("Dentro, "), inlineCode("this"), regular(" es el valor sobre el que se llama, y se puede omitir: en el ejemplo, "), inlineCode("isEmpty()"), regular(" y "), inlineCode("size"), regular(" son los de la lista. "), inlineCode("filter"), regular(", "), inlineCode("map"), regular(" y las demás son extensiones de la biblioteca estándar.")),
                        ),
                    visual =
                        LessonVisualContent.CodeExamples(
                            listOf(
                                LessonCodeExample(
                                    title = "Dos extensiones",
                                    lines =
                                        kotlinCode(
                                            """
                                            fun String.esPalindromo(): Boolean {
                                                val limpio = this.lowercase().replace(" ", "")
                                                return limpio == limpio.reversed()
                                            }

                                            fun List<Int>.media(): Double? {
                                                if (isEmpty()) return null
                                                return sum().toDouble() / size
                                            }

                                            fun main() {
                                                println("Anita lava la tina".esPalindromo())
                                                println("Kotlin".esPalindromo())
                                                println(listOf(7, 4, 9, 6).media())
                                                println(listOf<Int>().media())
                                            }
                                            """.trimIndent(),
                                        ),
                                    note = richText(regular("Muestra "), inlineCode("true"), regular(", "), inlineCode("false"), regular(", "), inlineCode("6.5"), regular(" y "), inlineCode("null"), regular(". "), inlineCode("reversed()"), regular(" da el texto al revés.")),
                                ),
                            ),
                        ),
                ),
                LessonStep(
                    title = "Funciones de ámbito",
                    explanation =
                        listOf(
                            paragraph(regular("Las "), highlight("funciones de ámbito"), regular(" ejecutan una lambda sobre un objeto. "), inlineCode("let"), regular(" se usa sobre todo con "), inlineCode("?."), regular(": la lambda solo se ejecuta si el valor no es nulo, y dentro "), inlineCode("it"), regular(" ya no lo es.")),
                            paragraph(inlineCode("apply"), regular(" sirve para configurar un objeto recién creado: dentro, sus propiedades se usan sin nombrarlo, y devuelve el propio objeto. "), inlineCode("also"), regular(" hace algo más con un valor, como mostrarlo, y lo devuelve sin cambios.")),
                            callout(
                                "Con moderación",
                                richText(regular("Existen también "), inlineCode("run"), regular(" y "), inlineCode("with"), regular(". Úsalas cuando aclaren el código; varias anidadas hacen difícil saber a qué se refiere cada "), inlineCode("it"), regular(" o "), inlineCode("this"), regular(".")),
                                LessonCalloutTone.Warning,
                            ),
                        ),
                    visual =
                        LessonVisualContent.Trace(
                            example =
                                LessonCodeExample(
                                    title = "let, apply y also",
                                    lines =
                                        kotlinCode(
                                            $$"""
                                                class Pedido {
                                                    var producto = ""
                                                    var unidades = 0
                                                }

                                                fun buscarNota(nombre: String): Int? = if (nombre == "Ana") 7 else null

                                                fun main() {
                                                    buscarNota("Ana")?.let { println("Ana tiene un $it") }
                                                    buscarNota("Pau")?.let { println("Pau tiene un $it") }
                                                    val pedido =
                                                        Pedido().apply {
                                                            producto = "Teclado"
                                                            unidades = 2
                                                        }
                                                    val total = (pedido.unidades * 25).also { println("Total calculado: $it") }
                                                    println("${pedido.producto}: $total €")
                                                }
                                            """.trimIndent(),
                                        ),
                                    note = richText(regular("Muestra "), inlineCode("Ana tiene un 7"), regular(", "), inlineCode("Total calculado: 50"), regular(" y "), inlineCode("Teclado: 50 €"), regular(". La línea de Pau no muestra nada.")),
                                ),
                            table =
                                LessonTraceTable(
                                    title = "Qué recibe y qué devuelve cada una",
                                    columns = listOf("Función", "Dentro, el objeto es", "Devuelve", "Para qué"),
                                    rows =
                                        listOf(
                                            listOf("let", "it", "Lo que devuelve la lambda", "Usar un valor que puede ser nulo"),
                                            listOf("apply", "this", "El propio objeto", "Configurar un objeto nuevo"),
                                            listOf("also", "it", "El propio objeto", "Hacer algo más sin cambiarlo"),
                                        ),
                                ),
                        ),
                ),
                LessonStep(
                    title = "Probar código funcional",
                    explanation =
                        listOf(
                            paragraph(regular("Una cadena de operaciones devuelve una colección corriente, así que se prueba como siempre: con "), inlineCode("assertEquals"), regular(" y el resultado esperado.")),
                            paragraph(regular("Al ser una función pura, basta con preparar los datos de entrada. Prueba el caso normal, el límite (un 5 aprueba) y la lista vacía, que debe dar una lista vacía y no un error.")),
                        ),
                    visual =
                        LessonVisualContent.CodeExamples(
                            listOf(
                                LessonCodeExample(
                                    title = "src/main/kotlin/Alumnos.kt",
                                    fileName = "Alumnos.kt",
                                    lines =
                                        kotlinCode(
                                            """
                                            data class Alumno(val nombre: String, val nota: Int)

                                            fun aprobados(alumnos: List<Alumno>): List<String> =
                                                alumnos
                                                    .filter { it.nota >= 5 }
                                                    .map { it.nombre }
                                                    .sorted()
                                            """.trimIndent(),
                                        ),
                                    note = richText(regular("Devuelve los nombres de los aprobados en orden alfabético.")),
                                ),
                                LessonCodeExample(
                                    title = "src/test/kotlin/AlumnosTest.kt",
                                    fileName = "AlumnosTest.kt",
                                    lines =
                                        kotlinCode(
                                            """
                                            import kotlin.test.Test
                                            import kotlin.test.assertEquals

                                            class AlumnosTest {
                                                @Test
                                                fun `solo aparecen los aprobados en orden alfabetico`() {
                                                    val alumnos = listOf(Alumno("Marta", 9), Alumno("Luis", 4), Alumno("Pau", 5), Alumno("Ana", 7))
                                                    assertEquals(listOf("Ana", "Marta", "Pau"), aprobados(alumnos))
                                                }

                                                @Test
                                                fun `sin alumnos no hay aprobados`() {
                                                    assertEquals(listOf(), aprobados(listOf()))
                                                }
                                            }
                                            """.trimIndent(),
                                        ),
                                    note = richText(regular("Las dos pruebas pasan. La primera incluye a Pau con un 5 para comprobar el límite.")),
                                ),
                            ),
                        ),
                ),
                LessonStep(
                    title = "Ahora te toca: el inventario",
                    explanation =
                        listOf(
                            paragraph(regular("Este programa guarda el inventario de una tienda de informática y lo muestra entero. Hay productos agotados, con "), inlineCode("0"), regular(" unidades.")),
                            paragraph(regular("Amplíalo para que muestre un resumen útil para reponer, y "), highlight("resuélvelo sin escribir bucles"), regular(": con lo que has visto en esta lección.")),
                        ),
                    visual =
                        LessonVisualContent.CodeExamples(
                            listOf(
                                LessonCodeExample(
                                    title = "Inventario de la tienda",
                                    lines =
                                        kotlinCode(
                                            $$"""
                                                fun main() {
                                                    val inventario =
                                                        listOf(
                                                            Producto("Teclado", 25.0, 4),
                                                            Producto("Ratón", 12.5, 0),
                                                            Producto("Monitor", 150.0, 2),
                                                            Producto("Cable HDMI", 7.5, 10),
                                                            Producto("Auriculares", 40.0, 1),
                                                        )
                                                    mostrar(inventario)
                                                }

                                                data class Producto(val nombre: String, val precio: Double, val unidades: Int)

                                                fun mostrar(productos: List<Producto>) {
                                                    for (producto in productos) {
                                                        println("${producto.nombre} x${producto.unidades}: ${producto.precio} €")
                                                    }
                                                }
                                            """.trimIndent(),
                                        ),
                                    note = richText(regular("Muestra los cinco productos, de "), inlineCode("Teclado x4: 25.0 €"), regular(" a "), inlineCode("Auriculares x1: 40.0 €"), regular(".")),
                                    isCopyable = true,
                                ),
                            ),
                        ),
                    links =
                        listOf(
                            LessonLink("Funciones de orden superior y lambdas", "https://kotlinlang.org/docs/lambdas.html"),
                            LessonLink("Operaciones con colecciones", "https://kotlinlang.org/docs/collection-operations.html"),
                            LessonLink("Filtrar colecciones", "https://kotlinlang.org/docs/collection-filtering.html"),
                            LessonLink("Transformar colecciones", "https://kotlinlang.org/docs/collection-transformations.html"),
                            LessonLink("Funciones de extensión", "https://kotlinlang.org/docs/extensions.html"),
                            LessonLink("Funciones de ámbito", "https://kotlinlang.org/docs/scope-functions.html"),
                        ),
                    practice =
                        LessonPractice(
                            id = "functional-programming-inventory",
                            objective = richText(highlight("Resolver con operaciones sobre colecciones lo que antes hacías con bucles, y pasar una condición como parámetro.")),
                            statement = richText(regular("Cambia "), inlineCode("main"), regular(" y "), inlineCode("mostrar"), regular(" para que muestren el resumen del inventario con las funciones que se piden, sin "), inlineCode("for"), regular(" ni "), inlineCode("while"), regular(", y comprueba su resultado con pruebas.")),
                            requirements =
                                listOf(
                                    richText(regular("Crea "), inlineCode("disponibles(productos: List<Producto>): List<Producto>"), regular(", que devuelva los productos con unidades, del más caro al más barato.")),
                                    richText(regular("Crea "), inlineCode("valorTotal(productos: List<Producto>): Double"), regular(", el valor de todo el inventario: precio por unidades de cada producto, sumado.")),
                                    richText(regular("Crea "), inlineCode("masCaro(productos: List<Producto>): Producto?"), regular(", que devuelva el producto de mayor precio, o "), inlineCode("null"), regular(" si no hay productos.")),
                                    richText(regular("Crea "), inlineCode("contarSi(productos: List<Producto>, condicion: (Producto) -> Boolean): Int"), regular(", que cuente los productos que cumplen la condición, y úsala en "), inlineCode("main"), regular(" para contar los agotados y los que tienen 1 o 2 unidades.")),
                                    richText(inlineCode("main"), regular(" muestra el resumen como en la primera ejecución. Si el inventario está vacío, como en la segunda (con "), inlineCode("listOf<Producto>()"), regular("), muestra solo "), inlineCode("No hay productos"), regular(".")),
                                    richText(regular("Crea "), inlineCode("InventarioTest"), regular(" con pruebas de las cuatro funciones, con productos y con una lista vacía.")),
                                ),
                            expectedOutput =
                                listOf(
                                    consoleRun(
                                        consoleOutput("Disponibles:"),
                                        consoleOutput("Monitor x2: 150.0 €"),
                                        consoleOutput("Auriculares x1: 40.0 €"),
                                        consoleOutput("Teclado x4: 25.0 €"),
                                        consoleOutput("Cable HDMI x10: 7.5 €"),
                                        consoleOutput("Valor del inventario: 515.0 €"),
                                        consoleOutput("Más caro: Monitor"),
                                        consoleOutput("Agotados: 1"),
                                        consoleOutput("Quedan pocos: 2"),
                                    ),
                                    consoleRun(
                                        consoleOutput("No hay productos"),
                                    ),
                                ),
                            hints =
                                listOf(
                                    richText(inlineCode("disponibles"), regular(" es una cadena: "), inlineCode("filter { it.unidades > 0 }"), regular(" y después "), inlineCode("sortedByDescending"), regular(" por precio.")),
                                    richText(inlineCode("valorTotal"), regular(" cabe en una línea con "), inlineCode("sumOf { it.precio * it.unidades }"), regular(", y "), inlineCode("masCaro"), regular(" con "), inlineCode("maxByOrNull"), regular(", que ya devuelve "), inlineCode("null"), regular(" si la lista está vacía.")),
                                    richText(inlineCode("contarSi"), regular(" puede entregar su "), inlineCode("condicion"), regular(" a "), inlineCode("count"), regular(". En "), inlineCode("main"), regular(" la llamas dos veces con lambdas distintas: "), inlineCode("contarSi(inventario) { it.unidades == 0 }"), regular(" y otra para 1 o 2 unidades.")),
                                    richText(regular("En "), inlineCode("mostrar"), regular(", "), inlineCode("forEach"), regular(" sustituye al "), inlineCode("for"), regular(". En "), inlineCode("main"), regular(", comprueba primero si el inventario está vacío con "), inlineCode("isEmpty()"), regular(" y, si lo está, termina con "), inlineCode("return"), regular(" tras el aviso.")),
                                ),
                            extension = richText(regular("Añade la extensión "), inlineCode("fun Producto.valor(): Double"), regular(" y úsala en "), inlineCode("valorTotal"), regular(". ¿Qué versión se lee mejor?")),
                            confirmation = richText(regular("Confirma que las dos ejecuciones dan la salida esperada, que ninguna de tus funciones usa "), inlineCode("for"), regular(" ni "), inlineCode("while"), regular(" y que "), inlineCode("./gradlew test"), regular(" termina con "), inlineCode("BUILD SUCCESSFUL"), regular(".")),
                        ),
                ),
            ),
    )
