package dev.kuromiichi.damprep.data.lessons.contents

import dev.kuromiichi.damprep.data.lessons.LessonCalloutTone
import dev.kuromiichi.damprep.data.lessons.LessonCodeExample
import dev.kuromiichi.damprep.data.lessons.LessonConsoleKind
import dev.kuromiichi.damprep.data.lessons.LessonContent
import dev.kuromiichi.damprep.data.lessons.LessonLink
import dev.kuromiichi.damprep.data.lessons.LessonPractice
import dev.kuromiichi.damprep.data.lessons.LessonStep
import dev.kuromiichi.damprep.data.lessons.LessonTraceTable
import dev.kuromiichi.damprep.data.lessons.LessonVisualContent
import dev.kuromiichi.damprep.data.lessons.callout
import dev.kuromiichi.damprep.data.lessons.consoleError
import dev.kuromiichi.damprep.data.lessons.consoleInput
import dev.kuromiichi.damprep.data.lessons.consoleOutput
import dev.kuromiichi.damprep.data.lessons.consoleRun
import dev.kuromiichi.damprep.data.lessons.emphasis
import dev.kuromiichi.damprep.data.lessons.explanationCode
import dev.kuromiichi.damprep.data.lessons.highlight
import dev.kuromiichi.damprep.data.lessons.inlineCode
import dev.kuromiichi.damprep.data.lessons.kotlinCode
import dev.kuromiichi.damprep.data.lessons.paragraph
import dev.kuromiichi.damprep.data.lessons.regular
import dev.kuromiichi.damprep.data.lessons.richText

val arraysListsLessonContent =
    LessonContent(
        lessonId = "arrays-lists",
        steps =
            listOf(
                LessonStep(
                    title = "Muchos datos, una variable",
                    explanation =
                        listOf(
                            paragraph(regular("Imagina guardar las notas de 30 alumnos. Con lo que sabes hasta ahora tendrías que crear "), inlineCode("nota1"), regular(", "), inlineCode("nota2"), regular("… hasta "), inlineCode("nota30"), regular(": treinta variables que no puedes recorrer con un bucle ni pasar juntas a una función.")),
                            paragraph(regular("Una "), highlight("colección"), regular(" guarda muchos valores del mismo tipo bajo un solo nombre. Cada valor ocupa una posición, llamada "), emphasis("índice"), regular(", y la primera posición es la "), inlineCode("0"), regular(", no la "), inlineCode("1"), regular(".")),
                            paragraph(regular("Kotlin ofrece dos colecciones básicas: los "), emphasis("arrays"), regular(", de tamaño fijo, y las "), emphasis("listas"), regular(", que pueden crecer y encogerse. Empezamos por los arrays y terminamos con las listas.")),
                        ),
                    visual = LessonVisualContent.Flow(listOf("30 variables sueltas", "Una colección", "Un índice por valor", "Un bucle la recorre")),
                ),
                LessonStep(
                    title = "Crear un array",
                    explanation =
                        listOf(
                            paragraph(regular("En el ejemplo, "), inlineCode("intArrayOf(7, 4, 9)"), regular(" crea un array de números enteros con esos valores. Para otros tipos, como texto, se usa "), inlineCode("arrayOf"), regular(". Con "), inlineCode("IntArray(5)"), regular(" se crean cinco posiciones, todas a "), inlineCode("0"), regular(".")),
                            paragraph(inlineCode("size"), regular(" dice cuántos elementos tiene. El tamaño se fija al crearlo y ya no cambia: no se pueden añadir posiciones a un array.")),
                            paragraph(regular("Mostrar un array con "), inlineCode("println"), regular(" a secas no enseña sus valores. Para verlos se usa "), inlineCode("contentToString()"), regular(".")),
                        ),
                    visual =
                        LessonVisualContent.CodeExamples(
                            listOf(
                                LessonCodeExample(
                                    title = "Tres formas de crear un array",
                                    lines =
                                        kotlinCode(
                                            """
                                            fun main() {
                                                val notas = intArrayOf(7, 4, 9)
                                                val nombres = arrayOf("Ana", "Luis", "Marta")
                                                val vacio = IntArray(5)
                                                println(notas.contentToString())
                                                println(nombres.size)
                                                println(vacio.contentToString())
                                            }
                                            """.trimIndent(),
                                        ),
                                    note = richText(regular("Muestra "), inlineCode("[7, 4, 9]"), regular(", "), inlineCode("3"), regular(" y "), inlineCode("[0, 0, 0, 0, 0]"), regular(".")),
                                ),
                            ),
                        ),
                ),
                LessonStep(
                    title = "Leer y cambiar por posición",
                    explanation =
                        listOf(
                            paragraph(regular("Se accede a un elemento con corchetes: en el ejemplo, "), inlineCode("notas[0]"), regular(" es el primero. Con la misma notación se puede asignar un valor nuevo: "), inlineCode("notas[1] = 6"), regular(" sustituye el segundo.")),
                            paragraph(regular("Con "), inlineCode("3"), regular(" elementos los índices válidos son "), inlineCode("0"), regular(", "), inlineCode("1"), regular(" y "), inlineCode("2"), regular(". El último siempre es "), inlineCode("size - 1"), regular(", y "), inlineCode("lastIndex"), regular(" lo da ya calculado.")),
                        ),
                    visual =
                        LessonVisualContent.Trace(
                            example =
                                LessonCodeExample(
                                    title = "Leer y asignar",
                                    lines =
                                        kotlinCode(
                                            """
                                            fun main() {
                                                val notas = intArrayOf(7, 4, 9)
                                                println(notas[0])
                                                notas[1] = 6
                                                println(notas[1])
                                                println(notas[notas.lastIndex])
                                            }
                                            """.trimIndent(),
                                        ),
                                    note = richText(regular("Muestra "), inlineCode("7"), regular(", "), inlineCode("6"), regular(" y "), inlineCode("9"), regular(". El array cambia en la línea 4.")),
                                ),
                            table =
                                LessonTraceTable(
                                    title = "El array tras cada línea",
                                    columns = listOf("Línea", "notas", "Muestra"),
                                    rows =
                                        listOf(
                                            listOf("2", "[7, 4, 9]", "–"),
                                            listOf("3", "[7, 4, 9]", "7"),
                                            listOf("4", "[7, 6, 9]", "–"),
                                            listOf("5", "[7, 6, 9]", "6"),
                                            listOf("6", "[7, 6, 9]", "9"),
                                        ),
                                ),
                        ),
                ),
                LessonStep(
                    title = "Salirse del array",
                    explanation =
                        listOf(
                            paragraph(regular("Pedir una posición que no existe lanza una "), inlineCode("ArrayIndexOutOfBoundsException"), regular(". El mensaje dice qué índice se pidió y cuántos elementos hay. Como en la lección anterior, la traza señala la línea que falló.")),
                            explanationCode(
                                "Main.kt",
                                richText(regular("Con tres notas, el índice "), inlineCode("3"), regular(" no existe: la línea 4 falla.")),
                                "fun main() {",
                                "    val notas = intArrayOf(7, 4, 9)",
                                "    println(notas[0])",
                                "    println(notas[3])",
                                "}",
                            ),
                            callout(
                                "Error típico",
                                richText(regular("Usar "), inlineCode("size"), regular(" como último índice. Un bucle "), inlineCode("for (i in 0..notas.size)"), regular(" da una vuelta de más; el rango correcto es "), inlineCode("0..<notas.size"), regular(".")),
                                LessonCalloutTone.Warning,
                            ),
                        ),
                    visual =
                        LessonVisualContent.Console(
                            kind = LessonConsoleKind.IntelliJRun,
                            runs =
                                listOf(
                                    consoleRun(
                                        consoleOutput("7"),
                                        consoleError("Exception in thread \"main\" java.lang.ArrayIndexOutOfBoundsException: Index 3 out of bounds for length 3"),
                                        consoleError("    at MainKt.main(Main.kt:4)"),
                                        consoleError("    at MainKt.main(Main.kt)"),
                                        consoleOutput(""),
                                        consoleOutput("Process finished with exit code 1"),
                                    ),
                                ),
                        ),
                ),
                LessonStep(
                    title = "Recorrer un array",
                    explanation =
                        listOf(
                            paragraph(regular("Si solo necesitas los valores, "), inlineCode("for (nota in notas)"), regular(" los entrega uno a uno. Si necesitas la posición, "), inlineCode("indices"), regular(" recorre de "), inlineCode("0"), regular(" hasta el último índice sin que tengas que calcularlo.")),
                            paragraph(regular("Para tener a la vez posición y valor, "), inlineCode("withIndex()"), regular(" entrega cada elemento como un par: su posición y su valor. Cambiar elementos exige siempre el índice.")),
                            paragraph(regular("En el ejemplo, los paréntesis de "), inlineCode("(i, nombre)"), regular(" son una "), highlight("desestructuración"), regular(": abren el par y guardan cada parte en su propia variable, "), inlineCode("i"), regular(" para la posición y "), inlineCode("nombre"), regular(" para el valor.")),
                        ),
                    visual =
                        LessonVisualContent.CodeExamples(
                            listOf(
                                LessonCodeExample(
                                    title = "Recorrer los valores",
                                    lines =
                                        kotlinCode(
                                            """
                                            fun main() {
                                                val notas = intArrayOf(7, 4, 9)
                                                for (nota in notas) {
                                                    println(nota)
                                                }
                                            }
                                            """.trimIndent(),
                                        ),
                                    note = richText(regular("Muestra "), inlineCode("7"), regular(", "), inlineCode("4"), regular(" y "), inlineCode("9"), regular(", uno por línea.")),
                                ),
                                LessonCodeExample(
                                    title = "Recorrer con el índice",
                                    lines =
                                        kotlinCode(
                                            $$"""
                                                fun main() {
                                                    val nombres = arrayOf("Ana", "Luis", "Marta")
                                                    for (i in nombres.indices) {
                                                        println("${i + 1}. ${nombres[i]}")
                                                    }
                                                }
                                            """.trimIndent(),
                                        ),
                                    note = richText(regular("Muestra "), inlineCode("1. Ana"), regular(", "), inlineCode("2. Luis"), regular(" y "), inlineCode("3. Marta"), regular(".")),
                                ),
                                LessonCodeExample(
                                    title = "Posición y valor a la vez",
                                    lines =
                                        kotlinCode(
                                            $$"""
                                                fun main() {
                                                    val nombres = arrayOf("Ana", "Luis", "Marta")
                                                    for ((i, nombre) in nombres.withIndex()) {
                                                        println("$i: $nombre")
                                                    }
                                                }
                                            """.trimIndent(),
                                        ),
                                    note = richText(regular("Muestra "), inlineCode("0: Ana"), regular(", "), inlineCode("1: Luis"), regular(" y "), inlineCode("2: Marta"), regular(".")),
                                ),
                            ),
                        ),
                ),
                LessonStep(
                    title = "Sumar, contar y buscar el máximo",
                    explanation =
                        listOf(
                            paragraph(regular("Casi todo algoritmo sobre un array es un recorrido con una variable que se va actualizando: un acumulador para sumar, un contador para contar o el «mejor valor hasta ahora» para el máximo.")),
                            paragraph(regular("El máximo parte del primer elemento, no de "), inlineCode("0"), regular(": con valores negativos, el "), inlineCode("0"), regular(" ganaría siempre. Eso exige que el array no esté vacío. Para la media, "), inlineCode("toDouble()"), regular(" evita perder los decimales al dividir.")),
                        ),
                    visual =
                        LessonVisualContent.Trace(
                            example =
                                LessonCodeExample(
                                    title = "Suma, máximo y media",
                                    lines =
                                        kotlinCode(
                                            """
                                            fun main() {
                                                val notas = intArrayOf(7, 4, 9, 6)
                                                var suma = 0
                                                var maxima = notas[0]
                                                for (nota in notas) {
                                                    suma += nota
                                                    if (nota > maxima) {
                                                        maxima = nota
                                                    }
                                                }
                                                println(suma)
                                                println(maxima)
                                                println(suma.toDouble() / notas.size)
                                            }
                                            """.trimIndent(),
                                        ),
                                    note = richText(regular("Muestra "), inlineCode("26"), regular(", "), inlineCode("9"), regular(" y "), inlineCode("6.5"), regular(".")),
                                ),
                            table =
                                LessonTraceTable(
                                    title = "Al terminar cada vuelta",
                                    columns = listOf("Vuelta", "nota", "suma", "maxima"),
                                    rows =
                                        listOf(
                                            listOf("1", "7", "7", "7"),
                                            listOf("2", "4", "11", "7"),
                                            listOf("3", "9", "20", "9"),
                                            listOf("4", "6", "26", "9"),
                                        ),
                                ),
                        ),
                ),
                LessonStep(
                    title = "Buscar un elemento",
                    explanation =
                        listOf(
                            paragraph(regular("La "), highlight("búsqueda secuencial"), regular(" mira los elementos uno a uno hasta encontrar el buscado. Si lo encuentra, devuelve su índice al instante; si termina el bucle sin encontrarlo, devuelve "), inlineCode("-1"), regular(", porque ningún índice válido es negativo.")),
                            paragraph(regular("Devolver "), inlineCode("-1"), regular(" obliga a quien llama a recordar ese convenio. Una alternativa es devolver "), inlineCode("Int?"), regular(" y "), inlineCode("null"), regular(" cuando no está, como viste con los valores que pueden faltar.")),
                            callout(
                                "Búsqueda binaria",
                                richText(regular("Si el array está ordenado hay una forma más rápida: mirar el elemento central y descartar la mitad en cada paso. Con 1000 elementos bastan unas 10 comprobaciones en lugar de hasta 1000. Aquí solo hace falta conocer la idea.")),
                            ),
                        ),
                    visual =
                        LessonVisualContent.Trace(
                            example =
                                LessonCodeExample(
                                    title = "Buscar una posición",
                                    lines =
                                        kotlinCode(
                                            """
                                            fun buscar(
                                                notas: IntArray,
                                                objetivo: Int,
                                            ): Int {
                                                for (i in notas.indices) {
                                                    if (notas[i] == objetivo) {
                                                        return i
                                                    }
                                                }
                                                return -1
                                            }
                                            """.trimIndent(),
                                        ),
                                    note = richText(inlineCode("return"), regular(" dentro del bucle termina la función en cuanto hay acierto; el "), inlineCode("-1"), regular(" solo se alcanza si no lo hubo.")),
                                ),
                            table =
                                LessonTraceTable(
                                    title = "buscar con [7, 4, 9, 6] y objetivo 9",
                                    columns = listOf("i", "notas[i]", "¿Es 9?", "Resultado"),
                                    rows =
                                        listOf(
                                            listOf("0", "7", "No", "Sigue"),
                                            listOf("1", "4", "No", "Sigue"),
                                            listOf("2", "9", "Sí", "Devuelve 2"),
                                        ),
                                ),
                        ),
                ),
                LessonStep(
                    title = "Ordenar con el método de la burbuja",
                    explanation =
                        listOf(
                            paragraph(regular("El método de la burbuja compara cada elemento con su vecino y los intercambia si están al revés. Tras cada pasada, el mayor de los que quedan «sube» hasta el final, igual que una burbuja.")),
                            paragraph(regular("Para intercambiar hace falta una variable auxiliar: si asignas "), inlineCode("notas[i] = notas[i + 1]"), regular(" sin guardar antes el valor, lo pierdes. Cada pasada necesita una comparación menos que la anterior.")),
                            callout(
                                "En la práctica",
                                richText(inlineCode("notas.sort()"), regular(" ordena el array en su sitio y es lo que se usa en proyectos reales. Programar una ordenación una vez sirve para entender el recorrido y el intercambio. Otros métodos, como selección o inserción, parten de la misma idea.")),
                                LessonCalloutTone.Tip,
                            ),
                        ),
                    visual =
                        LessonVisualContent.Trace(
                            example =
                                LessonCodeExample(
                                    title = "Burbuja",
                                    lines =
                                        kotlinCode(
                                            """
                                            fun main() {
                                                val notas = intArrayOf(7, 4, 9, 6)
                                                for (pasada in 1..<notas.size) {
                                                    for (i in 0..<notas.size - pasada) {
                                                        if (notas[i] > notas[i + 1]) {
                                                            val auxiliar = notas[i]
                                                            notas[i] = notas[i + 1]
                                                            notas[i + 1] = auxiliar
                                                        }
                                                    }
                                                }
                                                println(notas.contentToString())
                                            }
                                            """.trimIndent(),
                                        ),
                                    note = richText(regular("Muestra "), inlineCode("[4, 6, 7, 9]"), regular(". Hacen falta tres pasadas para cuatro elementos.")),
                                ),
                            table =
                                LessonTraceTable(
                                    title = "Primera pasada",
                                    columns = listOf("i", "Compara", "¿Intercambia?", "notas"),
                                    rows =
                                        listOf(
                                            listOf("0", "7 y 4", "Sí", "[4, 7, 9, 6]"),
                                            listOf("1", "7 y 9", "No", "[4, 7, 9, 6]"),
                                            listOf("2", "9 y 6", "Sí", "[4, 7, 6, 9]"),
                                        ),
                                ),
                        ),
                ),
                LessonStep(
                    title = "Copiar no es duplicar",
                    explanation =
                        listOf(
                            paragraph(regular("La variable de un array no contiene los valores, sino dónde está el array. En el ejemplo, con "), inlineCode("val alias = original"), regular(" no se copia nada: hay dos nombres para el mismo array y cambiar uno cambia el otro.")),
                            paragraph(inlineCode("copyOf()"), regular(" crea una copia independiente. Y "), inlineCode("=="), regular(" pregunta si es el mismo array, no si tienen los mismos valores: para comparar el contenido se usa "), inlineCode("contentEquals"), regular(".")),
                            explanationCode(
                                "Main.kt",
                                richText(regular("Una función que recibe un array y lo modifica cambia el array de quien la llama, sin devolver nada.")),
                                "fun subirUno(notas: IntArray) {",
                                "    for (i in notas.indices) {",
                                "        notas[i] = notas[i] + 1",
                                "    }",
                                "}",
                            ),
                        ),
                    visual =
                        LessonVisualContent.CodeExamples(
                            listOf(
                                LessonCodeExample(
                                    title = "Dos nombres, un solo array",
                                    lines =
                                        kotlinCode(
                                            """
                                            fun main() {
                                                val original = intArrayOf(7, 4, 9)
                                                val alias = original
                                                alias[0] = 1
                                                println(original.contentToString())
                                            }
                                            """.trimIndent(),
                                        ),
                                    note = richText(regular("Muestra "), inlineCode("[1, 4, 9]"), regular(": el cambio hecho con "), inlineCode("alias"), regular(" se ve en "), inlineCode("original"), regular(".")),
                                ),
                                LessonCodeExample(
                                    title = "Una copia independiente",
                                    lines =
                                        kotlinCode(
                                            """
                                            fun main() {
                                                val original = intArrayOf(7, 4, 9)
                                                val copia = original.copyOf()
                                                copia[0] = 1
                                                println(original.contentToString())
                                                println(copia.contentToString())
                                            }
                                            """.trimIndent(),
                                        ),
                                    note = richText(regular("Muestra "), inlineCode("[7, 4, 9]"), regular(" y "), inlineCode("[1, 4, 9]"), regular(": ahora son dos arrays distintos.")),
                                ),
                                LessonCodeExample(
                                    title = "Igualdad de contenido",
                                    lines =
                                        kotlinCode(
                                            """
                                            fun main() {
                                                val a = intArrayOf(7, 4, 9)
                                                val b = intArrayOf(7, 4, 9)
                                                println(a == b)
                                                println(a.contentEquals(b))
                                            }
                                            """.trimIndent(),
                                        ),
                                    note = richText(regular("Muestra "), inlineCode("false"), regular(" y "), inlineCode("true"), regular(": son dos arrays distintos con el mismo contenido. IntelliJ IDEA avisa de que "), inlineCode("=="), regular(" en arrays solo compara referencias.")),
                                ),
                            ),
                        ),
                ),
                LessonStep(
                    title = "Tablas de dos dimensiones",
                    explanation =
                        listOf(
                            paragraph(regular("Una tabla, o "), highlight("matriz"), regular(", es un array de arrays: cada fila es un array. Se accede con dos índices, primero la fila y luego la columna: en el ejemplo, "), inlineCode("notas[1][2]"), regular(" es la fila "), inlineCode("1"), regular(", columna "), inlineCode("2"), regular(", es decir, el "), inlineCode("6"), regular(".")),
                            paragraph(regular("Para recorrerla hace falta un bucle dentro de otro, como en el rectángulo de asteriscos: el de fuera elige la fila y el de dentro recorre sus columnas.")),
                        ),
                    visual =
                        LessonVisualContent.Trace(
                            example =
                                LessonCodeExample(
                                    title = "Sumar cada fila",
                                    lines =
                                        kotlinCode(
                                            $$"""
                                                fun main() {
                                                    val notas =
                                                        arrayOf(
                                                            intArrayOf(7, 4, 9),
                                                            intArrayOf(5, 8, 6),
                                                        )
                                                    for (fila in notas.indices) {
                                                        var suma = 0
                                                        for (columna in notas[fila].indices) {
                                                            suma += notas[fila][columna]
                                                        }
                                                        println("Fila $fila: $suma")
                                                    }
                                                }
                                            """.trimIndent(),
                                        ),
                                    note = richText(regular("Muestra "), inlineCode("Fila 0: 20"), regular(" y "), inlineCode("Fila 1: 19"), regular(".")),
                                ),
                            table =
                                LessonTraceTable(
                                    title = "Contenido y suma por fila",
                                    columns = listOf("Fila", "[0]", "[1]", "[2]", "Suma"),
                                    rows =
                                        listOf(
                                            listOf("0", "7", "4", "9", "20"),
                                            listOf("1", "5", "8", "6", "19"),
                                        ),
                                ),
                        ),
                ),
                LessonStep(
                    title = "Un texto también es una secuencia",
                    explanation =
                        listOf(
                            paragraph(regular("Un "), inlineCode("String"), regular(" es una secuencia de caracteres con índices desde "), inlineCode("0"), regular(", como un array: "), inlineCode("texto[0]"), regular(" es la primera letra y "), inlineCode("length"), regular(" su longitud. Se recorre con "), inlineCode("for (letra in texto)"), regular(".")),
                            paragraph(regular("En el ejemplo, "), inlineCode("letra in \"aeiouáéíóú\""), regular(" pregunta si esa letra aparece en ese texto. Un "), inlineCode("String"), regular(" no se puede modificar: para construir uno letra a letra se usa "), inlineCode("StringBuilder"), regular(" con "), inlineCode("append"), regular(".")),
                        ),
                    visual =
                        LessonVisualContent.CodeExamples(
                            listOf(
                                LessonCodeExample(
                                    title = "Contar vocales",
                                    lines =
                                        kotlinCode(
                                            $$"""
                                                fun main() {
                                                    val texto = "programación"
                                                    var vocales = 0
                                                    for (letra in texto) {
                                                        if (letra in "aeiouáéíóú") {
                                                            vocales++
                                                        }
                                                    }
                                                    println("$texto tiene $vocales vocales")
                                                }
                                            """.trimIndent(),
                                        ),
                                    note = richText(regular("Muestra "), inlineCode("programación tiene 5 vocales"), regular(".")),
                                ),
                                LessonCodeExample(
                                    title = "Dar la vuelta a un texto",
                                    lines =
                                        kotlinCode(
                                            """
                                            fun main() {
                                                val texto = "kotlin"
                                                println(texto[0])
                                                println(texto.length)
                                                val resultado = StringBuilder()
                                                for (i in texto.length - 1 downTo 0) {
                                                    resultado.append(texto[i])
                                                }
                                                println(resultado)
                                            }
                                            """.trimIndent(),
                                        ),
                                    note = richText(regular("Muestra "), inlineCode("k"), regular(", "), inlineCode("6"), regular(" y "), inlineCode("niltok"), regular(".")),
                                ),
                            ),
                        ),
                ),
                LessonStep(
                    title = "Listas: tamaño variable",
                    explanation =
                        listOf(
                            paragraph(regular("Una "), highlight("lista"), regular(" guarda valores en orden como un array, pero puede crecer y encogerse. "), inlineCode("listOf"), regular(" crea una lista de solo lectura; "), inlineCode("mutableListOf"), regular(" crea una que admite "), inlineCode("add"), regular(", "), inlineCode("removeAt"), regular(" y "), inlineCode("remove"), regular(".")),
                            paragraph(regular("Los corchetes, "), inlineCode("size"), regular(", "), inlineCode("indices"), regular(" y el bucle "), inlineCode("for"), regular(" funcionan igual, y "), inlineCode("println"), regular(" muestra la lista directamente. Con "), inlineCode("val"), regular(", la variable no cambia de lista, pero el contenido de una lista mutable sí.")),
                            callout(
                                "Lista vacía",
                                richText(inlineCode("mutableListOf<Int>()"), regular(" crea una lista vacía. Hay que indicar el tipo entre "), inlineCode("<>"), regular(" porque no hay valores de los que deducirlo. Con listas de "), inlineCode("Int"), regular(", "), inlineCode("remove(4)"), regular(" quita el valor "), inlineCode("4"), regular(" y "), inlineCode("removeAt(4)"), regular(" la posición "), inlineCode("4"), regular(".")),
                                LessonCalloutTone.Warning,
                            ),
                        ),
                    visual =
                        LessonVisualContent.Trace(
                            example =
                                LessonCodeExample(
                                    title = "Añadir y quitar",
                                    lines =
                                        kotlinCode(
                                            """
                                            fun main() {
                                                val notas = mutableListOf(7, 4)
                                                notas.add(9)
                                                notas.add(6)
                                                notas.removeAt(1)
                                                println(notas)
                                                println(notas.size)
                                                println(notas.contains(9))
                                                println(notas.indexOf(6))
                                            }
                                            """.trimIndent(),
                                        ),
                                    note = richText(regular("Muestra "), inlineCode("[7, 9, 6]"), regular(", "), inlineCode("3"), regular(", "), inlineCode("true"), regular(" y "), inlineCode("2"), regular(".")),
                                ),
                            table =
                                LessonTraceTable(
                                    title = "La lista tras cada línea",
                                    columns = listOf("Línea", "notas", "Muestra"),
                                    rows =
                                        listOf(
                                            listOf("2", "[7, 4]", "–"),
                                            listOf("3", "[7, 4, 9]", "–"),
                                            listOf("4", "[7, 4, 9, 6]", "–"),
                                            listOf("5", "[7, 9, 6]", "–"),
                                            listOf("6", "[7, 9, 6]", "[7, 9, 6]"),
                                            listOf("7", "[7, 9, 6]", "3"),
                                            listOf("8", "[7, 9, 6]", "true"),
                                            listOf("9", "[7, 9, 6]", "2"),
                                        ),
                                ),
                        ),
                ),
                LessonStep(
                    title = "¿Array o lista?",
                    explanation =
                        listOf(
                            paragraph(regular("Usa un array cuando el tamaño es fijo y conocido, como una tabla de 3 filas por 3 columnas. Usa una lista cuando los datos van llegando o se quitan, como las notas que escribe el usuario hasta decir basta. En Kotlin, lo habitual es la lista.")),
                            paragraph(regular("Las colecciones ya traen operaciones hechas. Fíjate en los vacíos del ejemplo: "), inlineCode("maxOrNull()"), regular(" devuelve "), inlineCode("null"), regular(" porque una lista vacía no tiene máximo, y "), inlineCode("first()"), regular(" lanza una excepción porque pedir el primero de nada es un mal uso.")),
                            explanationCode(
                                "Main.kt",
                                richText(regular("Con datos, todo funciona; con la lista vacía, cada operación falla a su manera.")),
                                "val notas = listOf(7, 4, 9)",
                                "println(notas.sum())",
                                "println(notas.maxOrNull())",
                                "println(notas.first())",
                                "",
                                "val vacia = mutableListOf<Int>()",
                                "println(vacia.maxOrNull())",
                                "println(vacia.firstOrNull())",
                                "println(vacia.first())",
                            ),
                        ),
                    visual = LessonVisualContent.Flow(listOf("¿Sé cuántos datos habrá?", "Sí y no cambian: array", "No o cambian: lista", "En la duda: lista")),
                ),
                LessonStep(
                    title = "Probar funciones con listas",
                    explanation =
                        listOf(
                            paragraph(regular("Las funciones que trabajan con listas se prueban con los mismos recursos que ya conoces. Compara resultados con "), inlineCode("assertEquals"), regular(" y los vacíos con "), inlineCode("assertNull"), regular(".")),
                            paragraph(regular("Una buena prueba incluye siempre el caso de la lista vacía, y a menudo el de un solo elemento: son los lugares donde los recorridos suelen fallar.")),
                        ),
                    visual =
                        LessonVisualContent.CodeExamples(
                            listOf(
                                LessonCodeExample(
                                    title = "src/main/kotlin/Notas.kt",
                                    fileName = "Notas.kt",
                                    lines =
                                        kotlinCode(
                                            """
                                            fun notaMaxima(notas: List<Int>): Int? {
                                                if (notas.isEmpty()) {
                                                    return null
                                                }
                                                var maxima = notas[0]
                                                for (nota in notas) {
                                                    if (nota > maxima) {
                                                        maxima = nota
                                                    }
                                                }
                                                return maxima
                                            }
                                            """.trimIndent(),
                                        ),
                                    note = richText(inlineCode("isEmpty()"), regular(" dice si la lista no tiene elementos. Se comprueba antes de leer "), inlineCode("notas[0]"), regular(", que fallaría.")),
                                ),
                                LessonCodeExample(
                                    title = "src/test/kotlin/NotasTest.kt",
                                    fileName = "NotasTest.kt",
                                    lines =
                                        kotlinCode(
                                            """
                                            import kotlin.test.Test
                                            import kotlin.test.assertEquals
                                            import kotlin.test.assertNull

                                            class NotasTest {
                                                @Test
                                                fun `la nota maxima de 7, 4 y 9 es 9`() {
                                                    assertEquals(9, notaMaxima(listOf(7, 4, 9)))
                                                }

                                                @Test
                                                fun `con una sola nota esa es la maxima`() {
                                                    assertEquals(5, notaMaxima(listOf(5)))
                                                }

                                                @Test
                                                fun `una lista vacia no tiene nota maxima`() {
                                                    assertNull(notaMaxima(listOf()))
                                                }
                                            }
                                            """.trimIndent(),
                                        ),
                                    note = richText(regular("Las tres pruebas pasan con la función de arriba. "), inlineCode("listOf()"), regular(" sin valores crea una lista vacía del tipo que pide el parámetro.")),
                                ),
                            ),
                        ),
                ),
                LessonStep(
                    title = "Ahora te toca: las notas de la clase",
                    explanation =
                        listOf(
                            paragraph(regular("Este programa guarda en una lista las notas que escribe el profesorado, una por línea, hasta que llega una línea vacía. Ahora solo las muestra.")),
                            paragraph(regular("Amplíalo con un resumen de la clase y una búsqueda, y haz que "), highlight("funcione también cuando no hay ninguna nota"), regular(". Escribe tú los recorridos con bucles, sin "), inlineCode("sum()"), regular(" ni "), inlineCode("average()"), regular(".")),
                        ),
                    visual =
                        LessonVisualContent.CodeExamples(
                            listOf(
                                LessonCodeExample(
                                    title = "Notas de la clase",
                                    lines =
                                        kotlinCode(
                                            $$"""
                                                fun main() {
                                                    println("Escribe las notas (línea vacía para terminar):")
                                                    val notas = mutableListOf<Int>()
                                                    var linea = readln()
                                                    while (linea != "") {
                                                        notas.add(linea.toInt())
                                                        linea = readln()
                                                    }
                                                    println("Notas: $notas")
                                                }
                                            """.trimIndent(),
                                        ),
                                    note = richText(regular("Con "), inlineCode("7"), regular(", "), inlineCode("4"), regular(" y una línea vacía muestra "), inlineCode("Notas: [7, 4]"), regular(".")),
                                    isCopyable = true,
                                ),
                            ),
                        ),
                    links =
                        listOf(
                            LessonLink("Arrays en Kotlin", "https://kotlinlang.org/docs/arrays.html"),
                            LessonLink("Visión general de colecciones", "https://kotlinlang.org/docs/collections-overview.html"),
                            LessonLink("Operaciones con listas", "https://kotlinlang.org/docs/list-operations.html"),
                            LessonLink("Cadenas de texto en Kotlin", "https://kotlinlang.org/docs/strings.html"),
                            LessonLink("Referencia de kotlin.test", "https://kotlinlang.org/api/core/kotlin-test/"),
                        ),
                    practice =
                        LessonPractice(
                            id = "arrays-lists-class-grades",
                            objective = richText(highlight("Guardar datos en una lista y recorrerla para resumirlos, buscar en ella y cubrir el caso de la lista vacía.")),
                            statement = richText(regular("Sustituye la última línea de "), inlineCode("main"), regular(" por un resumen de las notas y una búsqueda, con las funciones que se piden, y comprueba su resultado con pruebas.")),
                            requirements =
                                listOf(
                                    richText(regular("Crea "), inlineCode("media(notas: List<Int>): Double?"), regular(", que recorra la lista con un bucle y devuelva la media, o "), inlineCode("null"), regular(" si no hay notas.")),
                                    richText(regular("Crea "), inlineCode("contarAprobados(notas: List<Int>): Int"), regular(": cuántas notas son de "), inlineCode("5"), regular(" o más.")),
                                    richText(regular("Crea "), inlineCode("buscar(notas: List<Int>, objetivo: Int): Int?"), regular(", que devuelva la posición de la primera nota igual al objetivo, o "), inlineCode("null"), regular(" si no está.")),
                                    richText(inlineCode("main"), regular(" muestra "), inlineCode("No hay notas"), regular(" si la lista está vacía. Si no, muestra la media y los aprobados, pide una nota a buscar y dice en qué posición está o que nadie la tiene.")),
                                    richText(regular("Crea "), inlineCode("NotasTest"), regular(" con pruebas de "), inlineCode("media"), regular(" (con notas y con lista vacía), de "), inlineCode("contarAprobados"), regular(" y de "), inlineCode("buscar"), regular(" con una nota que no está.")),
                                ),
                            expectedOutput =
                                listOf(
                                    consoleRun(
                                        consoleOutput("Escribe las notas (línea vacía para terminar):"),
                                        consoleInput("7"),
                                        consoleInput("4"),
                                        consoleInput("9"),
                                        consoleInput("6"),
                                        consoleInput(""),
                                        consoleOutput("Media: 6.5"),
                                        consoleOutput("Aprobados: 3 de 4"),
                                        consoleOutput("Nota a buscar:"),
                                        consoleInput("9"),
                                        consoleOutput("La primera nota 9 está en la posición 2"),
                                    ),
                                    consoleRun(
                                        consoleOutput("Escribe las notas (línea vacía para terminar):"),
                                        consoleInput(""),
                                        consoleOutput("No hay notas"),
                                    ),
                                ),
                            hints =
                                listOf(
                                    richText(regular("Empieza por "), inlineCode("media"), regular(": acumula la suma con un bucle como en el paso de sumar y comprueba "), inlineCode("notas.isEmpty()"), regular(" antes de dividir. Divide con "), inlineCode("suma.toDouble() / notas.size"), regular(".")),
                                    richText(inlineCode("buscar"), regular(" recorre "), inlineCode("notas.indices"), regular(" y hace "), inlineCode("return i"), regular(" en cuanto encuentra el objetivo. Solo después del bucle devuelve "), inlineCode("null"), regular(".")),
                                    richText(regular("En "), inlineCode("main"), regular(" guarda el resultado de "), inlineCode("media"), regular(" y de "), inlineCode("buscar"), regular(" en variables y decide con "), inlineCode("if (media == null)"), regular(" y "), inlineCode("if (posicion == null)"), regular(", como con cualquier valor que puede faltar.")),
                                ),
                            extension = richText(regular("Ordena la lista con "), inlineCode("notas.sort()"), regular(" y muéstrala. Si hay notas repetidas, ¿cambia la posición que devuelve "), inlineCode("buscar"), regular(" tras ordenar?")),
                            confirmation = richText(regular("Confirma que las dos ejecuciones dan la salida esperada, que ningún recorrido usa "), inlineCode("sum()"), regular(" ni "), inlineCode("average()"), regular(" y que "), inlineCode("./gradlew test"), regular(" termina con "), inlineCode("BUILD SUCCESSFUL"), regular(".")),
                        ),
                ),
            ),
    )
