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

val setsMapsLessonContent =
    LessonContent(
        lessonId = "sets-maps",
        steps =
            listOf(
                LessonStep(
                    title = "Cuando la posición no importa",
                    explanation =
                        listOf(
                            paragraph(regular("Una lista guarda los valores en orden y permite repetirlos. Eso no siempre es lo que hace falta: la lista de alumnos matriculados no debería tener a nadie dos veces, y para saber la nota de Ana no quieres recorrer todo buscando su posición.")),
                            paragraph(regular("Un "), highlight("conjunto"), regular(" guarda valores "), emphasis("sin repetir"), regular(". Un "), highlight("mapa"), regular(" guarda "), emphasis("pares"), regular(": cada "), emphasis("clave"), regular(" única lleva asociado un "), emphasis("valor"), regular(", y se busca por la clave, como en un diccionario.")),
                            paragraph(regular("Los dos son colecciones, como las listas, y comparten buena parte de lo que ya sabes: "), inlineCode("size"), regular(", "), inlineCode("for"), regular(" e "), inlineCode("in"), regular(". Cambia la pregunta que responden bien.")),
                        ),
                    visual = LessonVisualContent.Flow(listOf("Lista: valores en orden", "Conjunto: valores sin repetir", "Mapa: clave y valor", "Se elige según la pregunta")),
                ),
                LessonStep(
                    title = "Crear un conjunto",
                    explanation =
                        listOf(
                            paragraph(inlineCode("setOf(1, 2, 3)"), regular(" crea un conjunto de solo lectura. Si repites un valor, se guarda una sola vez: no es un error, simplemente ya estaba.")),
                            paragraph(inlineCode("size"), regular(" cuenta los elementos distintos, y "), inlineCode("in"), regular(" pregunta si un valor pertenece al conjunto. En un conjunto, esa pregunta es la operación para la que está pensado.")),
                            paragraph(regular("Dos conjuntos son iguales si tienen los mismos elementos, en el orden que sea. Con "), inlineCode("=="), regular(" no hace falta nada parecido a "), inlineCode("contentEquals"), regular(" de los arrays.")),
                        ),
                    visual =
                        LessonVisualContent.CodeExamples(
                            listOf(
                                LessonCodeExample(
                                    title = "Los repetidos desaparecen",
                                    lines =
                                        kotlinCode(
                                            """
                                            fun main() {
                                                val conjunto = setOf(1, 2, 3, 4, 5, 1, 1)
                                                println(conjunto)
                                                println(conjunto.size)
                                                println(3 in conjunto)
                                            }
                                            """.trimIndent(),
                                        ),
                                    note = richText(regular("Muestra "), inlineCode("[1, 2, 3, 4, 5]"), regular(", "), inlineCode("5"), regular(" y "), inlineCode("true"), regular(".")),
                                ),
                                LessonCodeExample(
                                    title = "El orden no cuenta",
                                    lines =
                                        kotlinCode(
                                            """
                                            fun main() {
                                                val a = setOf(1, 2)
                                                val b = setOf(2, 1)
                                                println(a == b)
                                            }
                                            """.trimIndent(),
                                        ),
                                    note = richText(regular("Muestra "), inlineCode("true"), regular(": mismos elementos, aunque se hayan escrito al revés.")),
                                ),
                            ),
                        ),
                ),
                LessonStep(
                    title = "Conjuntos que cambian",
                    explanation =
                        listOf(
                            paragraph(inlineCode("mutableSetOf"), regular(" crea un conjunto al que se puede añadir con "), inlineCode("add"), regular(" y quitar con "), inlineCode("remove"), regular(", igual que en las listas mutables.")),
                            paragraph(inlineCode("add"), regular(" devuelve un "), inlineCode("Boolean"), regular(": "), inlineCode("true"), regular(" si el valor era nuevo y "), inlineCode("false"), regular(" si ya estaba. Así se detectan repetidos sin buscar antes. "), inlineCode("remove"), regular(" devuelve "), inlineCode("true"), regular(" si lo había y lo quitó.")),
                        ),
                    visual =
                        LessonVisualContent.Trace(
                            example =
                                LessonCodeExample(
                                    title = "Registro de asistencia",
                                    lines =
                                        kotlinCode(
                                            """
                                            fun main() {
                                                val asistentes = mutableSetOf<String>()
                                                println(asistentes.add("Ana"))
                                                println(asistentes.add("Luis"))
                                                println(asistentes.add("Ana"))
                                                println(asistentes.remove("Luis"))
                                                println(asistentes)
                                            }
                                            """.trimIndent(),
                                        ),
                                    note = richText(regular("Muestra "), inlineCode("true"), regular(", "), inlineCode("true"), regular(", "), inlineCode("false"), regular(", "), inlineCode("true"), regular(" y "), inlineCode("[Ana]"), regular(". La tercera línea no cambia nada: Ana ya estaba.")),
                                ),
                            table =
                                LessonTraceTable(
                                    title = "El conjunto tras cada línea",
                                    columns = listOf("Línea", "asistentes", "Muestra"),
                                    rows =
                                        listOf(
                                            listOf("2", "[]", "–"),
                                            listOf("3", "[Ana]", "true"),
                                            listOf("4", "[Ana, Luis]", "true"),
                                            listOf("5", "[Ana, Luis]", "false"),
                                            listOf("6", "[Ana]", "true"),
                                            listOf("7", "[Ana]", "[Ana]"),
                                        ),
                                ),
                        ),
                ),
                LessonStep(
                    title = "Sin posiciones",
                    explanation =
                        listOf(
                            paragraph(regular("Un conjunto no tiene índices: "), inlineCode("conjunto[0]"), regular(" ni compila, porque la pregunta «¿qué hay en la posición 0?» no tiene sentido aquí. Se recorre con "), inlineCode("for"), regular(" y se consulta con "), inlineCode("in"), regular(".")),
                            paragraph(inlineCode("toSet()"), regular(" convierte una lista en un conjunto y es la forma más corta de quitar repetidos. Con "), inlineCode("toList()"), regular(" se vuelve a una lista.")),
                            callout(
                                "Orden de los elementos",
                                richText(regular("Los conjuntos que crea Kotlin muestran los elementos en el orden en que se añadieron. Pero un conjunto no promete nada sobre el orden: no construyas el programa apoyándote en él.")),
                                LessonCalloutTone.Warning,
                            ),
                        ),
                    visual =
                        LessonVisualContent.CodeExamples(
                            listOf(
                                LessonCodeExample(
                                    title = "No hay posiciones",
                                    lines =
                                        kotlinCode(
                                            """
                                            fun main() {
                                                val conjunto = setOf("Ana", "Luis")
                                                println(⟦conjunto[0]⟧)
                                            }
                                            """.trimIndent(),
                                        ),
                                    note = richText(regular("IntelliJ IDEA lo marca como error: un "), inlineCode("Set"), regular(" no admite corchetes.")),
                                ),
                                LessonCodeExample(
                                    title = "Recorrer un conjunto",
                                    lines =
                                        kotlinCode(
                                            $$"""
                                                fun main() {
                                                    val conjunto = setOf("Ana", "Luis")
                                                    for (nombre in conjunto) {
                                                        println("Hola, $nombre")
                                                    }
                                                }
                                            """.trimIndent(),
                                        ),
                                    note = richText(regular("Muestra "), inlineCode("Hola, Ana"), regular(" y "), inlineCode("Hola, Luis"), regular(".")),
                                ),
                                LessonCodeExample(
                                    title = "Quitar los repetidos de una lista",
                                    lines =
                                        kotlinCode(
                                            """
                                            fun main() {
                                                val pedidos = listOf(7, 4, 7, 9, 4)
                                                val distintos = pedidos.toSet()
                                                println(distintos)
                                                println(distintos.toList())
                                                println(distintos.size)
                                            }
                                            """.trimIndent(),
                                        ),
                                    note = richText(regular("Muestra "), inlineCode("[7, 4, 9]"), regular(", "), inlineCode("[7, 4, 9]"), regular(" y "), inlineCode("3"), regular(".")),
                                ),
                            ),
                        ),
                ),
                LessonStep(
                    title = "Unir, cruzar y restar",
                    explanation =
                        listOf(
                            paragraph(regular("Los conjuntos se combinan como en matemáticas. "), inlineCode("union"), regular(" reúne los elementos de los dos; "), inlineCode("intersect"), regular(" deja los que están en ambos; "), inlineCode("subtract"), regular(" deja los del primero que no están en el segundo.")),
                            paragraph(regular("Se escriben entre los dos conjuntos, sin punto ni paréntesis, y cada una devuelve un conjunto nuevo sin tocar los originales.")),
                        ),
                    visual =
                        LessonVisualContent.Trace(
                            example =
                                LessonCodeExample(
                                    title = "Dos grupos de alumnos",
                                    lines =
                                        kotlinCode(
                                            """
                                            fun main() {
                                                val matematicas = setOf("Ana", "Luis", "Marta")
                                                val ingles = setOf("Luis", "Marta", "Pau")
                                                println(matematicas union ingles)
                                                println(matematicas intersect ingles)
                                                println(matematicas subtract ingles)
                                            }
                                            """.trimIndent(),
                                        ),
                                    note = richText(regular("Cada línea muestra el resultado de la tabla.")),
                                ),
                            table =
                                LessonTraceTable(
                                    title = "Qué devuelve cada operación",
                                    columns = listOf("Operación", "Resultado", "Quiénes son"),
                                    rows =
                                        listOf(
                                            listOf("union", "[Ana, Luis, Marta, Pau]", "Están en alguno de los dos"),
                                            listOf("intersect", "[Luis, Marta]", "Están en los dos"),
                                            listOf("subtract", "[Ana]", "Solo en matemáticas"),
                                        ),
                                ),
                        ),
                ),
                LessonStep(
                    title = "Mapas: cada clave, su valor",
                    explanation =
                        listOf(
                            paragraph(inlineCode("mapOf"), regular(" recibe pares con la forma "), inlineCode("clave to valor"), regular(". "), inlineCode("to"), regular(" crea un par: "), inlineCode("\"Ana\" to 7"), regular(" asocia la clave "), inlineCode("\"Ana\""), regular(" con el valor "), inlineCode("7"), regular(".")),
                            paragraph(regular("Las claves no se repiten, pero los valores sí: dos alumnos pueden tener un 7. Se lee con "), inlineCode("mapa[clave]"), regular(", y "), inlineCode("size"), regular(" cuenta los pares. "), inlineCode("println"), regular(" muestra el mapa como "), inlineCode("{clave=valor, …}"), regular(".")),
                            callout(
                                "Clave repetida",
                                richText(regular("Si escribes dos veces la misma clave en "), inlineCode("mapOf"), regular(", la segunda sustituye a la primera: "), inlineCode("mapOf(\"Ana\" to 7, \"Ana\" to 8)"), regular(" solo guarda "), inlineCode("Ana=8"), regular(".")),
                                LessonCalloutTone.Warning,
                            ),
                        ),
                    visual =
                        LessonVisualContent.CodeExamples(
                            listOf(
                                LessonCodeExample(
                                    title = "Un mapa de notas",
                                    lines =
                                        kotlinCode(
                                            """
                                            fun main() {
                                                val notas = mapOf("Ana" to 7, "Luis" to 4, "Marta" to 9)
                                                println(notas)
                                                println(notas["Ana"])
                                                println(notas.size)
                                            }
                                            """.trimIndent(),
                                        ),
                                    note = richText(regular("Muestra "), inlineCode("{Ana=7, Luis=4, Marta=9}"), regular(", "), inlineCode("7"), regular(" y "), inlineCode("3"), regular(".")),
                                ),
                            ),
                        ),
                ),
                LessonStep(
                    title = "Claves que faltan",
                    explanation =
                        listOf(
                            paragraph(regular("¿Qué pasa si la clave no está? "), inlineCode("notas[\"Pau\"]"), regular(" devuelve "), inlineCode("null"), regular(", así que su tipo es "), inlineCode("Int?"), regular(", no "), inlineCode("Int"), regular(". Ya sabes tratarlo: "), inlineCode("notas[\"Pau\"] ?: 0"), regular(" da un valor por defecto.")),
                            paragraph(inlineCode("containsKey"), regular(" pregunta si la clave existe. "), inlineCode("getValue"), regular(" exige que exista y, si no, lanza una "), inlineCode("NoSuchElementException"), regular(": útil cuando la ausencia sería un fallo del programa.")),
                            explanationCode(
                                "Main.kt",
                                richText(regular("La línea 3 funciona; la 4 pide una clave que no existe.")),
                                "fun main() {",
                                "    val notas = mapOf(\"Ana\" to 7, \"Luis\" to 4)",
                                "    println(notas[\"Ana\"])",
                                "    println(notas.getValue(\"Pau\"))",
                                "}",
                            ),
                        ),
                    visual =
                        LessonVisualContent.Console(
                            title = "Consola de IntelliJ IDEA",
                            runs =
                                listOf(
                                    consoleRun(
                                        consoleOutput("7"),
                                        consoleError("Exception in thread \"main\" java.util.NoSuchElementException: Key Pau is missing in the map."),
                                        consoleError("    at kotlin.collections.MapsKt__MapWithDefaultKt.getOrImplicitDefaultNullable(MapWithDefault.kt:25)"),
                                        consoleError("    at kotlin.collections.MapsKt__MapsKt.getValue(Maps.kt:437)"),
                                        consoleError("    at MainKt.main(Main.kt:4)"),
                                        consoleError("    at MainKt.main(Main.kt)"),
                                        consoleOutput(""),
                                        consoleOutput("Process finished with exit code 1"),
                                    ),
                                ),
                        ),
                ),
                LessonStep(
                    title = "Mapas que cambian",
                    explanation =
                        listOf(
                            paragraph(inlineCode("mutableMapOf<String, Int>()"), regular(" crea un mapa vacío; los tipos entre "), inlineCode("<>"), regular(" son el de la clave y el del valor. Con "), inlineCode("mapa[clave] = valor"), regular(" se añade un par, o se sustituye el valor si la clave ya estaba.")),
                            paragraph(inlineCode("remove(clave)"), regular(" quita el par entero. Con "), inlineCode("val"), regular(" la variable no cambia de mapa, pero el contenido de un mapa mutable sí.")),
                        ),
                    visual =
                        LessonVisualContent.Trace(
                            example =
                                LessonCodeExample(
                                    title = "Añadir, sustituir y quitar",
                                    lines =
                                        kotlinCode(
                                            """
                                            fun main() {
                                                val notas = mutableMapOf<String, Int>()
                                                notas["Ana"] = 7
                                                notas["Luis"] = 4
                                                notas["Ana"] = 8
                                                notas.remove("Luis")
                                                println(notas)
                                                println(notas.containsKey("Luis"))
                                            }
                                            """.trimIndent(),
                                        ),
                                    note = richText(regular("Muestra "), inlineCode("{Ana=8}"), regular(" y "), inlineCode("false"), regular(". La línea 5 no añade a Ana: cambia su valor.")),
                                ),
                            table =
                                LessonTraceTable(
                                    title = "El mapa tras cada línea",
                                    columns = listOf("Línea", "notas", "Muestra"),
                                    rows =
                                        listOf(
                                            listOf("2", "{}", "–"),
                                            listOf("3", "{Ana=7}", "–"),
                                            listOf("4", "{Ana=7, Luis=4}", "–"),
                                            listOf("5", "{Ana=8, Luis=4}", "–"),
                                            listOf("6", "{Ana=8}", "–"),
                                            listOf("7", "{Ana=8}", "{Ana=8}"),
                                            listOf("8", "{Ana=8}", "false"),
                                        ),
                                ),
                        ),
                ),
                LessonStep(
                    title = "Recorrer un mapa",
                    explanation =
                        listOf(
                            paragraph(regular("Un "), inlineCode("for"), regular(" sobre un mapa entrega cada par, y la desestructuración que viste con "), inlineCode("withIndex()"), regular(" lo separa: "), inlineCode("for ((nombre, nota) in notas)"), regular(" da la clave y el valor en dos variables.")),
                            paragraph(inlineCode("keys"), regular(" es el conjunto de las claves y "), inlineCode("values"), regular(" la colección de los valores. Recorrer "), inlineCode("values"), regular(" sirve cuando solo interesan los valores, como para sumar notas.")),
                        ),
                    visual =
                        LessonVisualContent.CodeExamples(
                            listOf(
                                LessonCodeExample(
                                    title = "Clave y valor a la vez",
                                    lines =
                                        kotlinCode(
                                            $$"""
                                                fun main() {
                                                    val notas = mapOf("Ana" to 7, "Luis" to 4, "Marta" to 9)
                                                    for ((nombre, nota) in notas) {
                                                        println("$nombre: $nota")
                                                    }
                                                }
                                            """.trimIndent(),
                                        ),
                                    note = richText(regular("Muestra "), inlineCode("Ana: 7"), regular(", "), inlineCode("Luis: 4"), regular(" y "), inlineCode("Marta: 9"), regular(".")),
                                ),
                                LessonCodeExample(
                                    title = "Claves y valores por separado",
                                    lines =
                                        kotlinCode(
                                            """
                                            fun main() {
                                                val notas = mapOf("Ana" to 7, "Luis" to 4, "Marta" to 9)
                                                println(notas.keys)
                                                println(notas.values)
                                                var suma = 0
                                                for (nota in notas.values) {
                                                    suma += nota
                                                }
                                                println(suma)
                                            }
                                            """.trimIndent(),
                                        ),
                                    note = richText(regular("Muestra "), inlineCode("[Ana, Luis, Marta]"), regular(", "), inlineCode("[7, 4, 9]"), regular(" y "), inlineCode("20"), regular(".")),
                                ),
                            ),
                        ),
                ),
                LessonStep(
                    title = "Contar con un mapa",
                    explanation =
                        listOf(
                            paragraph(regular("Un uso muy habitual: contar cuántas veces aparece cada valor. La clave es lo que se cuenta y el valor, el contador.")),
                            paragraph(inlineCode("conteo[palabra] ?: 0"), regular(" da el contador actual, o "), inlineCode("0"), regular(" si es la primera vez. Sumarle "), inlineCode("1"), regular(" y guardarlo con "), inlineCode("conteo[palabra] = …"), regular(" lo crea o lo actualiza, sin comprobar antes si existía.")),
                        ),
                    visual =
                        LessonVisualContent.Trace(
                            example =
                                LessonCodeExample(
                                    title = "Frecuencia de palabras",
                                    lines =
                                        kotlinCode(
                                            """
                                            fun main() {
                                                val palabras = listOf("sol", "luna", "sol", "mar", "sol", "luna")
                                                val conteo = mutableMapOf<String, Int>()
                                                for (palabra in palabras) {
                                                    conteo[palabra] = (conteo[palabra] ?: 0) + 1
                                                }
                                                println(conteo)
                                            }
                                            """.trimIndent(),
                                        ),
                                    note = richText(regular("Muestra "), inlineCode("{sol=3, luna=2, mar=1}"), regular(".")),
                                ),
                            table =
                                LessonTraceTable(
                                    title = "Al terminar cada vuelta",
                                    columns = listOf("Vuelta", "palabra", "Contador previo", "conteo"),
                                    rows =
                                        listOf(
                                            listOf("1", "sol", "null", "{sol=1}"),
                                            listOf("2", "luna", "null", "{sol=1, luna=1}"),
                                            listOf("3", "sol", "1", "{sol=2, luna=1}"),
                                            listOf("4", "mar", "null", "{sol=2, luna=1, mar=1}"),
                                            listOf("5", "sol", "2", "{sol=3, luna=1, mar=1}"),
                                            listOf("6", "luna", "1", "{sol=3, luna=2, mar=1}"),
                                        ),
                                ),
                        ),
                ),
                LessonStep(
                    title = "Un mapa de listas",
                    explanation =
                        listOf(
                            paragraph(regular("El valor de un mapa puede ser cualquier tipo, también una lista. Así se guardan varias notas por alumno: "), inlineCode("MutableMap<String, MutableList<Int>>"), regular(".")),
                            paragraph(regular("Antes de añadir hay que mirar si la clave ya tiene lista. Si no, se crea y se guarda; si sí, se añade a la que hay. La lista del mapa es una referencia, como el array de «Copiar no es duplicar»: "), inlineCode("lista.add"), regular(" cambia lo que contiene el mapa.")),
                        ),
                    visual =
                        LessonVisualContent.Trace(
                            example =
                                LessonCodeExample(
                                    title = "Notas agrupadas por alumno",
                                    lines =
                                        kotlinCode(
                                            """
                                            fun main() {
                                                val examenes = listOf("Ana" to 7, "Luis" to 4, "Ana" to 9)
                                                val notas = mutableMapOf<String, MutableList<Int>>()
                                                for ((alumno, nota) in examenes) {
                                                    val lista = notas[alumno]
                                                    if (lista == null) {
                                                        notas[alumno] = mutableListOf(nota)
                                                    } else {
                                                        lista.add(nota)
                                                    }
                                                }
                                                println(notas)
                                            }
                                            """.trimIndent(),
                                        ),
                                    note = richText(regular("Muestra "), inlineCode("{Ana=[7, 9], Luis=[4]}"), regular(".")),
                                ),
                            table =
                                LessonTraceTable(
                                    title = "Al terminar cada vuelta",
                                    columns = listOf("Vuelta", "alumno", "nota", "lista", "notas"),
                                    rows =
                                        listOf(
                                            listOf("1", "Ana", "7", "null", "{Ana=[7]}"),
                                            listOf("2", "Luis", "4", "null", "{Ana=[7], Luis=[4]}"),
                                            listOf("3", "Ana", "9", "[7]", "{Ana=[7, 9], Luis=[4]}"),
                                        ),
                                ),
                        ),
                ),
                LessonStep(
                    title = "¿Lista, conjunto o mapa?",
                    explanation =
                        listOf(
                            paragraph(regular("Usa una "), emphasis("lista"), regular(" si el orden o la posición importan, o si hay repetidos con sentido. Un "), emphasis("conjunto"), regular(" si solo importa si algo está o no. Un "), emphasis("mapa"), regular(" si buscas un dato a partir de otro, como la nota de un alumno.")),
                            paragraph(regular("Un conjunto y un mapa no se ordenan por sí mismos, pero "), inlineCode("sorted()"), regular(" convierte cualquier colección en una lista ordenada. Con "), inlineCode("notas.keys.sorted()"), regular(" obtienes los nombres de la A a la Z.")),
                            callout(
                                "Mapa o dos listas",
                                richText(regular("Si te ves manteniendo dos listas paralelas, una de nombres y otra de notas, y buscando el índice de una para leer la otra, lo que necesitas es un mapa.")),
                                LessonCalloutTone.Tip,
                            ),
                        ),
                    visual = LessonVisualContent.Flow(listOf("¿Importa la posición?", "Sí: lista", "¿Solo si está o no? conjunto", "¿Dato a partir de otro? mapa")),
                ),
                LessonStep(
                    title = "Probar funciones con mapas",
                    explanation =
                        listOf(
                            paragraph(regular("Dos mapas son iguales si tienen las mismas claves con los mismos valores, sin importar el orden. Por eso "), inlineCode("assertEquals"), regular(" sirve directamente para comparar el resultado de una función con el mapa esperado.")),
                            paragraph(regular("Prueba el caso normal, que una palabra repetida suma bien y el caso vacío: una lista sin palabras debe dar un mapa vacío, no un error.")),
                        ),
                    visual =
                        LessonVisualContent.CodeExamples(
                            listOf(
                                LessonCodeExample(
                                    title = "src/main/kotlin/Conteo.kt",
                                    lines =
                                        kotlinCode(
                                            """
                                            fun contar(palabras: List<String>): Map<String, Int> {
                                                val conteo = mutableMapOf<String, Int>()
                                                for (palabra in palabras) {
                                                    conteo[palabra] = (conteo[palabra] ?: 0) + 1
                                                }
                                                return conteo
                                            }
                                            """.trimIndent(),
                                        ),
                                    note = richText(regular("Declarar el tipo de retorno como "), inlineCode("Map"), regular(" impide que quien la llame modifique el resultado.")),
                                ),
                                LessonCodeExample(
                                    title = "src/test/kotlin/ConteoTest.kt",
                                    lines =
                                        kotlinCode(
                                            """
                                            import kotlin.test.Test
                                            import kotlin.test.assertEquals

                                            class ConteoTest {
                                                @Test
                                                fun `cada palabra cuenta las veces que aparece`() {
                                                    assertEquals(mapOf("sol" to 2, "luna" to 1), contar(listOf("sol", "luna", "sol")))
                                                }

                                                @Test
                                                fun `una lista vacia da un mapa vacio`() {
                                                    assertEquals(mapOf(), contar(listOf()))
                                                }
                                            }
                                            """.trimIndent(),
                                        ),
                                    note = richText(regular("Las dos pruebas pasan. "), inlineCode("mapOf()"), regular(" sin pares crea un mapa vacío del tipo que pide la comparación.")),
                                ),
                            ),
                        ),
                ),
                LessonStep(
                    title = "Ahora te toca: la votación de la clase",
                    explanation =
                        listOf(
                            paragraph(regular("Este programa lee los votos de la clase, uno por línea, hasta una línea vacía, y los guarda en una lista. El conjunto "), inlineCode("opciones"), regular(" tiene las opciones válidas. Ahora solo muestra la lista.")),
                            paragraph(regular("Amplíalo para que descarte los votos que no son una opción, cuente los válidos y diga quién gana, y haz que "), highlight("funcione también cuando nadie vota"), regular(". Escribe tú los recorridos con bucles.")),
                        ),
                    visual =
                        LessonVisualContent.CodeExamples(
                            listOf(
                                LessonCodeExample(
                                    title = "Votación de la clase",
                                    lines =
                                        kotlinCode(
                                            $$"""
                                                fun main() {
                                                    val opciones = setOf("Kotlin", "Java", "Python")
                                                    println("Opciones: Kotlin, Java, Python")
                                                    println("Escribe un voto por línea (línea vacía para terminar):")
                                                    val votos = mutableListOf<String>()
                                                    var linea = readln()
                                                    while (linea != "") {
                                                        votos.add(linea)
                                                        linea = readln()
                                                    }
                                                    println("Votos: $votos")
                                                }
                                            """.trimIndent(),
                                        ),
                                    note = richText(regular("Con "), inlineCode("Kotlin"), regular(", "), inlineCode("Java"), regular(" y una línea vacía muestra "), inlineCode("Votos: [Kotlin, Java]"), regular(".")),
                                    isCopyable = true,
                                ),
                            ),
                        ),
                    links =
                        listOf(
                            LessonLink("Visión general de colecciones", "https://kotlinlang.org/docs/collections-overview.html"),
                            LessonLink("Operaciones con conjuntos", "https://kotlinlang.org/docs/set-operations.html"),
                            LessonLink("Operaciones con mapas", "https://kotlinlang.org/docs/map-operations.html"),
                            LessonLink("Referencia de kotlin.test", "https://kotlinlang.org/api/core/kotlin-test/"),
                        ),
                    practice =
                        LessonPractice(
                            id = "sets-maps-class-vote",
                            objective = richText(highlight("Validar con un conjunto, contar con un mapa y cubrir el caso de que no haya datos.")),
                            statement = richText(regular("Sustituye la última línea de "), inlineCode("main"), regular(" por el recuento de votos y el resultado, con las funciones que se piden, y comprueba su resultado con pruebas.")),
                            requirements =
                                listOf(
                                    richText(regular("Al leer, avisa con "), inlineCode("«Rust» no es una opción"), regular(" (con el texto escrito) y no cuentes los votos que no estén en "), inlineCode("opciones"), regular(".")),
                                    richText(regular("Crea "), inlineCode("contarVotos(votos: List<String>): Map<String, Int>"), regular(", que recorra la lista y devuelva cuántos votos tiene cada opción votada.")),
                                    richText(regular("Crea "), inlineCode("ganadora(recuento: Map<String, Int>): String?"), regular(", que devuelva la opción con más votos, o "), inlineCode("null"), regular(" si el mapa está vacío.")),
                                    richText(inlineCode("main"), regular(" muestra "), inlineCode("No hay votos"), regular(" si no hubo ninguno válido. Si no, muestra los votos de cada opción, también las que tienen "), inlineCode("0"), regular(", y "), inlineCode("Gana …"), regular(".")),
                                    richText(regular("Crea "), inlineCode("VotacionTest"), regular(" con pruebas de "), inlineCode("contarVotos"), regular(" (con votos y con lista vacía) y de "), inlineCode("ganadora"), regular(" (con votos y con mapa vacío).")),
                                ),
                            expectedOutput =
                                listOf(
                                    consoleRun(
                                        consoleOutput("Opciones: Kotlin, Java, Python"),
                                        consoleOutput("Escribe un voto por línea (línea vacía para terminar):"),
                                        consoleInput("Kotlin"),
                                        consoleInput("Java"),
                                        consoleInput("Kotlin"),
                                        consoleInput("Rust"),
                                        consoleOutput("«Rust» no es una opción"),
                                        consoleInput("Kotlin"),
                                        consoleInput(""),
                                        consoleOutput("Kotlin: 3"),
                                        consoleOutput("Java: 1"),
                                        consoleOutput("Python: 0"),
                                        consoleOutput("Gana Kotlin"),
                                    ),
                                    consoleRun(
                                        consoleOutput("Opciones: Kotlin, Java, Python"),
                                        consoleOutput("Escribe un voto por línea (línea vacía para terminar):"),
                                        consoleInput(""),
                                        consoleOutput("No hay votos"),
                                    ),
                                ),
                            hints =
                                listOf(
                                    richText(regular("Dentro del bucle de lectura, decide con "), inlineCode("if (linea in opciones)"), regular(": si está, "), inlineCode("votos.add(linea)"), regular("; si no, muestra el aviso.")),
                                    richText(inlineCode("contarVotos"), regular(" es el patrón de contar con un mapa: "), inlineCode("recuento[voto] = (recuento[voto] ?: 0) + 1"), regular(" en un bucle sobre la lista.")),
                                    richText(inlineCode("ganadora"), regular(" recorre el mapa con "), inlineCode("for ((opcion, votos) in recuento)"), regular(" y recuerda la mejor hasta ahora, como el máximo de un array. Parte de "), inlineCode("null"), regular(" y de "), inlineCode("0"), regular(" votos.")),
                                    richText(regular("Para mostrar también las opciones sin votos, recorre "), inlineCode("opciones"), regular(" y usa "), inlineCode("recuento[opcion] ?: 0"), regular(" en lugar de recorrer el mapa.")),
                                ),
                            extension = richText(regular("Si dos opciones empatan, "), inlineCode("ganadora"), regular(" devuelve la primera que encuentra. ¿Cómo mostrarías todas las empatadas?")),
                            confirmation = richText(regular("Confirma que las dos ejecuciones dan la salida esperada, que el recuento se hace con un mapa y que "), inlineCode("./gradlew test"), regular(" termina con "), inlineCode("BUILD SUCCESSFUL"), regular(".")),
                        ),
                ),
            ),
    )
