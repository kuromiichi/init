package dev.kuromiichi.init.data.lessons.contents

import dev.kuromiichi.init.data.lessons.LessonCalloutTone
import dev.kuromiichi.init.data.lessons.LessonCodeExample
import dev.kuromiichi.init.data.lessons.LessonContent
import dev.kuromiichi.init.data.lessons.LessonLink
import dev.kuromiichi.init.data.lessons.LessonPractice
import dev.kuromiichi.init.data.lessons.LessonStep
import dev.kuromiichi.init.data.lessons.LessonTraceTable
import dev.kuromiichi.init.data.lessons.LessonVisualContent
import dev.kuromiichi.init.data.lessons.UmlRelation
import dev.kuromiichi.init.data.lessons.UmlRelationKind
import dev.kuromiichi.init.data.lessons.callout
import dev.kuromiichi.init.data.lessons.cell
import dev.kuromiichi.init.data.lessons.consoleOutput
import dev.kuromiichi.init.data.lessons.consoleRun
import dev.kuromiichi.init.data.lessons.emphasis
import dev.kuromiichi.init.data.lessons.highlight
import dev.kuromiichi.init.data.lessons.inlineCode
import dev.kuromiichi.init.data.lessons.kotlinCode
import dev.kuromiichi.init.data.lessons.paragraph
import dev.kuromiichi.init.data.lessons.regular
import dev.kuromiichi.init.data.lessons.richText
import dev.kuromiichi.init.data.lessons.umlClassName

val genericsCollectionsLessonContent =
    LessonContent(
        lessonId = "generics-collections",
        steps =
            listOf(
                LessonStep(
                    title = "Un mismo código para muchos tipos",
                    explanation =
                        listOf(
                            paragraph(regular("Llevas tiempo escribiendo "), inlineCode("List<Int>"), regular(" o "), inlineCode("MutableMap<String, Int>"), regular(". Lo que va entre "), inlineCode("< >"), regular(" es un "), highlight("parámetro de tipo"), regular(": "), inlineCode("List"), regular(" se escribió una sola vez y sirve para guardar cualquier tipo.")),
                            paragraph(regular("En esta lección escribirás tus propias clases y funciones "), highlight("genéricas"), regular(", conocerás qué hay detrás de las colecciones que usas, aprenderás a ordenar tus objetos y verás las operaciones que resuelven en una línea lo que antes pedía un bucle.")),
                        ),
                    visual = LessonVisualContent.Flow(listOf("Escribir el código una vez", "Dejar el tipo como parámetro", "Elegirlo al usarlo", "El compilador lo comprueba")),
                ),
                LessonStep(
                    title = "Repetir código o perder el tipo",
                    explanation =
                        listOf(
                            paragraph(regular("Imagina una caja que guarda un valor. Si escribes una clase por tipo, como en el primer ejemplo, repites el mismo código una y otra vez: solo cambia "), inlineCode("String"), regular(" por "), inlineCode("Int"), regular(".")),
                            paragraph(regular("Con "), inlineCode("Any"), regular(" cabe todo, pero el compilador olvida qué guardaste. Para usarlo hay que convertirlo con "), inlineCode("as"), regular(", y si te equivocas el error no aparece al compilar, sino al ejecutar: en el segundo ejemplo, la segunda caja guarda un número.")),
                        ),
                    visual =
                        LessonVisualContent.CodeExamples(
                            listOf(
                                LessonCodeExample(
                                    title = "Una caja por tipo",
                                    lines =
                                        kotlinCode(
                                            """
                                            class CajaDeTexto(val contenido: String)

                                            class CajaDeNumero(val contenido: Int)

                                            fun main() {
                                                val nombre = CajaDeTexto("Ana")
                                                val nota = CajaDeNumero(7)
                                                println(nombre.contenido.uppercase())
                                                println(nota.contenido + 1)
                                            }
                                            """.trimIndent(),
                                        ),
                                    note = richText(regular("Muestra "), inlineCode("ANA"), regular(" y "), inlineCode("8"), regular(". Dos clases iguales salvo por el tipo.")),
                                ),
                                LessonCodeExample(
                                    title = "Una caja para todo",
                                    lines =
                                        kotlinCode(
                                            """
                                            class Caja(val contenido: Any)

                                            fun main() {
                                                val cajas = listOf(Caja("Ana"), Caja(7))
                                                val nombre = cajas[0].contenido as String
                                                println(nombre.uppercase())
                                                val otro = cajas[1].contenido as String
                                                println(otro.uppercase())
                                            }
                                            """.trimIndent(),
                                        ),
                                    note = richText(regular("Muestra "), inlineCode("ANA"), regular(" y después lanza "), inlineCode("ClassCastException"), regular(": un "), inlineCode("Int"), regular(" no se puede convertir en "), inlineCode("String"), regular(".")),
                                ),
                            ),
                        ),
                ),
                LessonStep(
                    title = "Clases genéricas",
                    explanation =
                        listOf(
                            paragraph(regular("Una "), highlight("clase genérica"), regular(" declara un parámetro de tipo tras su nombre: "), inlineCode("class Caja<T>"), regular(". Dentro, "), inlineCode("T"), regular(" se usa como cualquier tipo; al crear una caja se decide cuál es, y Kotlin suele deducirlo del valor.")),
                            paragraph(regular("En el ejemplo, "), inlineCode("Caja(\"Ana\")"), regular(" es una "), inlineCode("Caja<String>"), regular(" y "), inlineCode("Caja(7)"), regular(", una "), inlineCode("Caja<Int>"), regular(". No hace falta convertir nada, y un error de tipos se detecta al compilar, no al ejecutar.")),
                            callout(
                                "Nombres de una letra",
                                richText(regular("Por costumbre, "), inlineCode("T"), regular(" es un tipo cualquiera, "), inlineCode("E"), regular(" el de los elementos de una colección, y "), inlineCode("K"), regular(" y "), inlineCode("V"), regular(" los de la clave y el valor de un mapa.")),
                                LessonCalloutTone.Tip,
                            ),
                        ),
                    visual =
                        LessonVisualContent.CodeExamples(
                            listOf(
                                LessonCodeExample(
                                    title = "Una caja para cualquier tipo",
                                    lines =
                                        kotlinCode(
                                            """
                                            class Caja<T>(val contenido: T)

                                            fun main() {
                                                val nombre = Caja("Ana")
                                                val nota = Caja(7)
                                                println(nombre.contenido.uppercase())
                                                println(nota.contenido + 1)
                                            }
                                            """.trimIndent(),
                                        ),
                                    note = richText(regular("Muestra "), inlineCode("ANA"), regular(" y "), inlineCode("8"), regular(", con una sola clase y sin "), inlineCode("as"), regular(".")),
                                ),
                                LessonCodeExample(
                                    title = "El tipo se comprueba al compilar",
                                    lines =
                                        kotlinCode(
                                            """
                                            class Caja<T>(val contenido: T)

                                            fun main() {
                                                val nota = Caja(7)
                                                val texto: String = ⟦nota.contenido⟧
                                            }
                                            """.trimIndent(),
                                        ),
                                    note = richText(regular("No compila: "), inlineCode("nota"), regular(" es una "), inlineCode("Caja<Int>"), regular(" y su contenido es un "), inlineCode("Int"), regular(", no un "), inlineCode("String"), regular(".")),
                                ),
                            ),
                        ),
                ),
                LessonStep(
                    title = "Pares y tríos",
                    explanation =
                        listOf(
                            paragraph(regular("Kotlin trae dos clases genéricas muy útiles: "), inlineCode("Pair<A, B>"), regular(" guarda dos valores, que pueden ser de tipos distintos, y "), inlineCode("Triple<A, B, C>"), regular(", tres. El "), inlineCode("to"), regular(" que usabas en los mapas crea un "), inlineCode("Pair"), regular(".")),
                            paragraph(regular("Se leen con "), inlineCode("first"), regular(", "), inlineCode("second"), regular(" y "), inlineCode("third"), regular(", o se separan en variables, como en el ejemplo: así una función puede devolver dos resultados. Si los datos tienen un significado propio, una "), inlineCode("data class"), regular(" con nombres claros se entiende mejor.")),
                        ),
                    visual =
                        LessonVisualContent.CodeExamples(
                            listOf(
                                LessonCodeExample(
                                    title = "Dos resultados a la vez",
                                    lines =
                                        kotlinCode(
                                            $$"""
                                                fun extremos(notas: List<Int>): Pair<Int, Int> {
                                                    return Pair(notas.min(), notas.max())
                                                }

                                                fun main() {
                                                    val par = "Ana" to 7
                                                    println(par.first)
                                                    println(par.second)
                                                    val (minima, maxima) = extremos(listOf(7, 4, 9, 6))
                                                    println("De $minima a $maxima")
                                                    val fecha = Triple(12, "octubre", 2026)
                                                    println(fecha)
                                                }
                                            """.trimIndent(),
                                        ),
                                    note = richText(regular("Muestra "), inlineCode("Ana"), regular(", "), inlineCode("7"), regular(", "), inlineCode("De 4 a 9"), regular(" y "), inlineCode("(12, octubre, 2026)"), regular(". "), inlineCode("min()"), regular(" y "), inlineCode("max()"), regular(" fallan con una lista vacía.")),
                                ),
                            ),
                        ),
                ),
                LessonStep(
                    title = "Funciones genéricas",
                    explanation =
                        listOf(
                            paragraph(regular("Una función también puede ser genérica: el parámetro de tipo se declara "), emphasis("antes"), regular(" de su nombre, "), inlineCode("fun <T> repetir(…)"), regular(". En el ejemplo, "), inlineCode("repetir"), regular(" devuelve una lista del mismo tipo que el valor que recibe.")),
                            paragraph(regular("Kotlin deduce "), inlineCode("T"), regular(" de los argumentos. Si no puede, se escribe tras el nombre, como ya hacías en "), inlineCode("listOf<String>()"), regular(". La extensión "), inlineCode("segundoOrNull"), regular(" sirve para listas de cualquier tipo.")),
                        ),
                    visual =
                        LessonVisualContent.CodeExamples(
                            listOf(
                                LessonCodeExample(
                                    title = "Una función y una extensión genéricas",
                                    lines =
                                        kotlinCode(
                                            """
                                            fun <T> repetir(valor: T, veces: Int): List<T> {
                                                val lista = mutableListOf<T>()
                                                repeat(veces) { lista.add(valor) }
                                                return lista
                                            }

                                            fun <T> List<T>.segundoOrNull(): T? = if (size >= 2) this[1] else null

                                            fun main() {
                                                println(repetir("hola", 3))
                                                println(repetir(0, 4))
                                                println(listOf("Ana", "Luis").segundoOrNull())
                                                println(listOf(7).segundoOrNull())
                                                val vacia = listOf<String>()
                                                println(vacia.segundoOrNull())
                                            }
                                            """.trimIndent(),
                                        ),
                                    note = richText(regular("Muestra "), inlineCode("[hola, hola, hola]"), regular(", "), inlineCode("[0, 0, 0, 0]"), regular(", "), inlineCode("Luis"), regular(", "), inlineCode("null"), regular(" y "), inlineCode("null"), regular(".")),
                                ),
                            ),
                        ),
                ),
                LessonStep(
                    title = "Limitar el tipo",
                    explanation =
                        listOf(
                            paragraph(regular("Con "), inlineCode("T"), regular(" a secas solo puedes hacer lo que admite cualquier tipo. Para pedir más se pone un "), highlight("límite"), regular(": "), inlineCode("<T : Number>"), regular(" acepta solo números, sean "), inlineCode("Int"), regular(", "), inlineCode("Double"), regular(" u otros, y a cambio deja usar lo que tienen en común, como "), inlineCode("toDouble()"), regular(".")),
                            paragraph(regular("En el ejemplo, "), inlineCode("media"), regular(" sirve para listas de enteros y de decimales. Con una lista de textos el programa no compila, que es justo lo que queremos.")),
                        ),
                    visual =
                        LessonVisualContent.CodeExamples(
                            listOf(
                                LessonCodeExample(
                                    title = "Solo números",
                                    lines =
                                        kotlinCode(
                                            """
                                            fun <T : Number> media(valores: List<T>): Double {
                                                if (valores.isEmpty()) return 0.0
                                                return valores.sumOf { it.toDouble() } / valores.size
                                            }

                                            fun main() {
                                                println(media(listOf(7, 4, 9, 6)))
                                                println(media(listOf(2.5, 3.5)))
                                                println(media(⟦listOf("siete", "cuatro")⟧))
                                            }
                                            """.trimIndent(),
                                        ),
                                    note = richText(regular("La última llamada no compila: "), inlineCode("String"), regular(" no es un "), inlineCode("Number"), regular(". Sin ella, muestra "), inlineCode("6.5"), regular(" y "), inlineCode("3.0"), regular(".")),
                                ),
                            ),
                        ),
                ),
                LessonStep(
                    title = "Una pila para cualquier tipo",
                    explanation =
                        listOf(
                            paragraph(regular("Las interfaces también pueden ser genéricas. Un "), highlight("tipo abstracto de datos"), regular(" describe qué operaciones ofrece una estructura sin decir cómo guarda los datos. Una "), emphasis("pila"), regular(" funciona como un montón de platos: se apila arriba y se saca el de arriba.")),
                            paragraph(regular("En el ejemplo, la interfaz "), inlineCode("Pila<T>"), regular(" fija las operaciones y "), inlineCode("PilaEnLista<T>"), regular(" las implementa con una lista. Quien la usa solo conoce la interfaz: la lista podría cambiarse sin tocar "), inlineCode("main"), regular(".")),
                        ),
                    visual =
                        LessonVisualContent.Trace(
                            example =
                                LessonCodeExample(
                                    title = "Deshacer cambios con una pila",
                                    lines =
                                        kotlinCode(
                                            """
                                            interface Pila<T> {
                                                fun apilar(elemento: T)

                                                fun desapilar(): T?

                                                fun cima(): T?

                                                fun estaVacia(): Boolean
                                            }

                                            class PilaEnLista<T> : Pila<T> {
                                                private val elementos = mutableListOf<T>()

                                                override fun apilar(elemento: T) {
                                                    elementos.add(elemento)
                                                }

                                                override fun desapilar(): T? = elementos.removeLastOrNull()

                                                override fun cima(): T? = elementos.lastOrNull()

                                                override fun estaVacia(): Boolean = elementos.isEmpty()
                                            }

                                            fun main() {
                                                val deshacer: Pila<String> = PilaEnLista()
                                                deshacer.apilar("Escribir hola")
                                                deshacer.apilar("Poner negrita")
                                                deshacer.apilar("Borrar línea")
                                                println(deshacer.desapilar())
                                                println(deshacer.desapilar())
                                                println(deshacer.cima())
                                                println(deshacer.estaVacia())
                                            }
                                            """.trimIndent(),
                                        ),
                                    note = richText(inlineCode("removeLastOrNull()"), regular(" quita y devuelve el último elemento, o "), inlineCode("null"), regular(" si no hay ninguno; "), inlineCode("lastOrNull()"), regular(" lo devuelve sin quitarlo.")),
                                ),
                            table =
                                LessonTraceTable(
                                    title = "La pila tras cada línea",
                                    columns = listOf("Línea", "Operación", "Pila (cima a la derecha)", "Muestra"),
                                    rows =
                                        listOf(
                                            listOf("27", "apilar", "[Escribir hola]", "–"),
                                            listOf("28", "apilar", "[Escribir hola, Poner negrita]", "–"),
                                            listOf("29", "apilar", "[Escribir hola, Poner negrita, Borrar línea]", "–"),
                                            listOf("30", "desapilar", "[Escribir hola, Poner negrita]", "Borrar línea"),
                                            listOf("31", "desapilar", "[Escribir hola]", "Poner negrita"),
                                            listOf("32", "cima", "[Escribir hola]", "Escribir hola"),
                                            listOf("33", "estaVacia", "[Escribir hola]", "false"),
                                        ),
                                ),
                        ),
                ),
                LessonStep(
                    title = "Listas de hijas",
                    explanation =
                        listOf(
                            paragraph(regular("Un perro es un animal, así que una "), inlineCode("List<Perro>"), regular(" se puede usar donde se pide una "), inlineCode("List<Animal>"), regular(": "), inlineCode("presentar"), regular(" solo lee, y todo lo que lea será un animal.")),
                            paragraph(regular("Con "), inlineCode("MutableList"), regular(" no ocurre. "), inlineCode("adoptarGato"), regular(" añade un gato, y si le pasaras la lista de perros, acabaría con un gato dentro. El compilador lo impide.")),
                            paragraph(regular("La biblioteca declara la lista de solo lectura como "), inlineCode("List<out E>"), regular(": "), inlineCode("out"), regular(" indica que solo entrega elementos y nunca los recibe. No necesitas escribirlo por ahora, pero sí reconocerlo en la documentación.")),
                        ),
                    visual =
                        LessonVisualContent.CodeExamples(
                            listOf(
                                LessonCodeExample(
                                    title = "Leer sí, añadir no",
                                    lines =
                                        kotlinCode(
                                            """
                                            open class Animal(val nombre: String)

                                            class Perro(nombre: String) : Animal(nombre)

                                            class Gato(nombre: String) : Animal(nombre)

                                            fun presentar(animales: List<Animal>) {
                                                animales.forEach { println(it.nombre) }
                                            }

                                            fun adoptarGato(animales: MutableList<Animal>) {
                                                animales.add(Gato("Misi"))
                                            }

                                            fun main() {
                                                val perros = mutableListOf(Perro("Toby"), Perro("Laika"))
                                                presentar(perros)
                                                adoptarGato(⟦perros⟧)
                                            }
                                            """.trimIndent(),
                                        ),
                                    note = richText(regular("La última línea no compila: se espera una "), inlineCode("MutableList<Animal>"), regular(" y "), inlineCode("perros"), regular(" es una "), inlineCode("MutableList<Perro>"), regular(". Sin ella, muestra "), inlineCode("Toby"), regular(" y "), inlineCode("Laika"), regular(".")),
                                ),
                            ),
                        ),
                ),
                LessonStep(
                    title = "La familia de las colecciones",
                    explanation =
                        listOf(
                            paragraph(regular("Las colecciones de Kotlin son interfaces genéricas emparentadas. "), inlineCode("Collection<E>"), regular(" reúne lo común a listas y conjuntos, como "), inlineCode("size"), regular(" o "), inlineCode("contains"), regular("; los mapas van aparte, porque guardan parejas de clave y valor.")),
                            paragraph(regular("Cada interfaz de solo lectura tiene una hija "), inlineCode("Mutable"), regular(" que añade los métodos para cambiarla. Por eso una "), inlineCode("MutableList"), regular(" sirve donde se pide una "), inlineCode("List"), regular(", y no al revés.")),
                            callout(
                                "Pide lo mínimo",
                                richText(regular("Si una función solo recorre los datos, que su parámetro sea "), inlineCode("List"), regular(" o incluso "), inlineCode("Collection"), regular(", no "), inlineCode("MutableList"), regular(": aceptará más colecciones y dejará claro que no las modifica.")),
                                LessonCalloutTone.Tip,
                            ),
                        ),
                    visual =
                        LessonVisualContent.UmlDiagram(
                            nodes =
                                listOf(
                                    umlClassName(id = "Collection<E>", cell = cell(0, 0, columnSpan = 2), stereotype = "interface"),
                                    umlClassName(id = "Map<K, V>", cell = cell(0, 2), stereotype = "interface"),
                                    umlClassName(id = "List<E>", cell = cell(1, 0), stereotype = "interface"),
                                    umlClassName(id = "Set<E>", cell = cell(1, 1), stereotype = "interface"),
                                    umlClassName(id = "MutableMap<K, V>", cell = cell(1, 2), stereotype = "interface"),
                                    umlClassName(id = "MutableList<E>", cell = cell(2, 0), stereotype = "interface"),
                                    umlClassName(id = "MutableSet<E>", cell = cell(2, 1), stereotype = "interface"),
                                ),
                            relations =
                                listOf(
                                    UmlRelation(from = "List<E>", to = "Collection<E>", kind = UmlRelationKind.Inheritance),
                                    UmlRelation(from = "Set<E>", to = "Collection<E>", kind = UmlRelationKind.Inheritance),
                                    UmlRelation(from = "MutableList<E>", to = "List<E>", kind = UmlRelationKind.Inheritance),
                                    UmlRelation(from = "MutableSet<E>", to = "Set<E>", kind = UmlRelationKind.Inheritance),
                                    UmlRelation(from = "MutableMap<K, V>", to = "Map<K, V>", kind = UmlRelationKind.Inheritance),
                                ),
                            caption = "Las interfaces principales, simplificadas",
                        ),
                ),
                LessonStep(
                    title = "Interfaz e implementación",
                    explanation =
                        listOf(
                            paragraph(regular("Detrás de cada interfaz hay una clase que la implementa. "), inlineCode("mutableSetOf"), regular(" crea un "), inlineCode("LinkedHashSet"), regular(", que recuerda el orden de llegada; "), inlineCode("sortedSetOf"), regular(" crea un "), inlineCode("TreeSet"), regular(", que mantiene los elementos siempre ordenados.")),
                            paragraph(regular("Lo mismo pasa con los mapas: "), inlineCode("sortedMapOf"), regular(" crea un "), inlineCode("TreeMap"), regular(", con las claves ordenadas. Las versiones "), inlineCode("hash"), regular(" no garantizan ningún orden. Elige según el orden que necesites y declara la variable con la interfaz.")),
                        ),
                    visual =
                        LessonVisualContent.Trace(
                            example =
                                LessonCodeExample(
                                    title = "Orden de llegada u orden alfabético",
                                    lines =
                                        kotlinCode(
                                            """
                                            fun main() {
                                                val llegada = mutableSetOf("pera", "kiwi", "manzana")
                                                val ordenado = sortedSetOf("pera", "kiwi", "manzana")
                                                llegada.add("fresa")
                                                ordenado.add("fresa")
                                                println(llegada)
                                                println(ordenado)
                                                val stock = sortedMapOf("pera" to 3, "kiwi" to 8)
                                                stock["fresa"] = 5
                                                println(stock)
                                            }
                                            """.trimIndent(),
                                        ),
                                    note = richText(regular("Muestra "), inlineCode("[pera, kiwi, manzana, fresa]"), regular(", "), inlineCode("[fresa, kiwi, manzana, pera]"), regular(" y "), inlineCode("{fresa=5, kiwi=8, pera=3}"), regular(".")),
                                ),
                            table =
                                LessonTraceTable(
                                    title = "Qué crea cada función",
                                    columns = listOf("Función", "Clase", "Orden al recorrerla"),
                                    rows =
                                        listOf(
                                            listOf("mutableListOf", "ArrayList", "Por posición"),
                                            listOf("mutableSetOf", "LinkedHashSet", "De llegada"),
                                            listOf("hashSetOf", "HashSet", "Sin garantía"),
                                            listOf("sortedSetOf", "TreeSet", "Ordenado"),
                                            listOf("mutableMapOf", "LinkedHashMap", "De llegada"),
                                            listOf("hashMapOf", "HashMap", "Sin garantía"),
                                            listOf("sortedMapOf", "TreeMap", "Por clave"),
                                        ),
                                ),
                        ),
                ),
                LessonStep(
                    title = "Pilas y colas",
                    explanation =
                        listOf(
                            paragraph(regular("Una "), highlight("cola"), regular(" atiende por orden de llegada, como la de una tienda: se entra por el final y se sale por el principio. Una pila hace lo contrario: sale primero lo último que entró.")),
                            paragraph(inlineCode("ArrayDeque"), regular(" sirve para las dos cosas, porque añade y quita rápido por ambos extremos. Como cola, "), inlineCode("addLast"), regular(" y "), inlineCode("removeFirst"), regular("; como pila, "), inlineCode("addLast"), regular(" y "), inlineCode("removeLast"), regular(". Con una colección vacía, "), inlineCode("removeFirstOrNull"), regular(" devuelve "), inlineCode("null"), regular(" en vez de fallar.")),
                        ),
                    visual =
                        LessonVisualContent.Trace(
                            example =
                                LessonCodeExample(
                                    title = "Una cola de turnos y una pila de cambios",
                                    lines =
                                        kotlinCode(
                                            """
                                            fun main() {
                                                val cola = ArrayDeque<String>()
                                                cola.addLast("Ana")
                                                cola.addLast("Luis")
                                                cola.addLast("Marta")
                                                println(cola.removeFirst())
                                                cola.addLast("Pau")
                                                println(cola.removeFirst())
                                                println(cola)
                                                val cambios = ArrayDeque(listOf("escribir", "negrita"))
                                                cambios.addLast("borrar")
                                                println(cambios.removeLast())
                                                println(cambios)
                                            }
                                            """.trimIndent(),
                                        ),
                                    note = richText(regular("Muestra "), inlineCode("Ana"), regular(", "), inlineCode("Luis"), regular(", "), inlineCode("[Marta, Pau]"), regular(", "), inlineCode("borrar"), regular(" y "), inlineCode("[escribir, negrita]"), regular(".")),
                                ),
                            table =
                                LessonTraceTable(
                                    title = "La cola tras cada línea",
                                    columns = listOf("Línea", "Operación", "cola", "Muestra"),
                                    rows =
                                        listOf(
                                            listOf("3–5", "addLast ×3", "[Ana, Luis, Marta]", "–"),
                                            listOf("6", "removeFirst", "[Luis, Marta]", "Ana"),
                                            listOf("7", "addLast", "[Luis, Marta, Pau]", "–"),
                                            listOf("8", "removeFirst", "[Marta, Pau]", "Luis"),
                                        ),
                                ),
                        ),
                ),
                LessonStep(
                    title = "Iguales para un conjunto",
                    explanation =
                        listOf(
                            paragraph(regular("Para saber si un elemento ya está, un conjunto o las claves de un mapa usan dos métodos que toda clase hereda de "), inlineCode("Any"), regular(": "), inlineCode("hashCode()"), regular(", que da un número para buscar rápido, y "), inlineCode("equals()"), regular(", que confirma si dos objetos son iguales.")),
                            paragraph(regular("En una clase normal, dos objetos solo son iguales si son el mismo. Una "), inlineCode("data class"), regular(" genera los dos métodos a partir de las propiedades del constructor: en el ejemplo, por eso el segundo conjunto no admite el repetido.")),
                            callout(
                                "No cambies lo que hay dentro",
                                richText(regular("Si un objeto de un conjunto cambia una propiedad que interviene en "), inlineCode("equals"), regular(", el conjunto puede dejar de encontrarlo. Usa "), inlineCode("val"), regular(" en los objetos que guardes en conjuntos o como claves.")),
                                LessonCalloutTone.Warning,
                            ),
                        ),
                    visual =
                        LessonVisualContent.CodeExamples(
                            listOf(
                                LessonCodeExample(
                                    title = "Clase normal y data class",
                                    lines =
                                        kotlinCode(
                                            """
                                            class Punto(val x: Int, val y: Int)

                                            data class PuntoDatos(val x: Int, val y: Int)

                                            fun main() {
                                                val visitados = mutableSetOf(Punto(1, 2))
                                                println(Punto(1, 2) in visitados)
                                                visitados.add(Punto(1, 2))
                                                println(visitados.size)
                                                val visitadosDatos = mutableSetOf(PuntoDatos(1, 2))
                                                println(PuntoDatos(1, 2) in visitadosDatos)
                                                visitadosDatos.add(PuntoDatos(1, 2))
                                                println(visitadosDatos.size)
                                            }
                                            """.trimIndent(),
                                        ),
                                    note = richText(regular("Muestra "), inlineCode("false"), regular(", "), inlineCode("2"), regular(", "), inlineCode("true"), regular(" y "), inlineCode("1"), regular(". Con la clase normal, el mismo punto se guarda dos veces.")),
                                ),
                            ),
                        ),
                ),
                LessonStep(
                    title = "Un orden natural",
                    explanation =
                        listOf(
                            paragraph(regular("Los números y los textos ya saben ordenarse. Para que tus objetos también, implementa la interfaz genérica "), inlineCode("Comparable<T>"), regular(": su método "), inlineCode("compareTo"), regular(" devuelve un número negativo si el objeto va antes que "), inlineCode("other"), regular(", cero si son iguales y positivo si va después.")),
                            paragraph(regular("A cambio, funcionan "), inlineCode("sorted()"), regular(", "), inlineCode("max()"), regular(" y los operadores "), inlineCode("<"), regular(" y "), inlineCode(">"), regular(". Y con el límite "), inlineCode("<T : Comparable<T>>"), regular(", "), inlineCode("mayorDe"), regular(" sirve para versiones, textos o cualquier tipo que se pueda comparar.")),
                        ),
                    visual =
                        LessonVisualContent.CodeExamples(
                            listOf(
                                LessonCodeExample(
                                    title = "Versiones que se ordenan",
                                    lines =
                                        kotlinCode(
                                            $$"""
                                                data class Version(val mayor: Int, val menor: Int) : Comparable<Version> {
                                                    override fun compareTo(other: Version): Int {
                                                        if (mayor != other.mayor) return mayor - other.mayor
                                                        return menor - other.menor
                                                    }

                                                    override fun toString(): String = "$mayor.$menor"
                                                }

                                                fun <T : Comparable<T>> mayorDe(a: T, b: T): T = if (a > b) a else b

                                                fun main() {
                                                    val versiones = listOf(Version(1, 10), Version(0, 9), Version(1, 2))
                                                    println(versiones.sorted())
                                                    println(versiones.max())
                                                    println(Version(1, 2) < Version(1, 10))
                                                    println(mayorDe(Version(2, 0), Version(1, 10)))
                                                    println(mayorDe("pera", "kiwi"))
                                                }
                                            """.trimIndent(),
                                        ),
                                    note = richText(regular("Muestra "), inlineCode("[0.9, 1.2, 1.10]"), regular(", "), inlineCode("1.10"), regular(", "), inlineCode("true"), regular(", "), inlineCode("2.0"), regular(" y "), inlineCode("pera"), regular(". Como textos, "), inlineCode("\"1.10\""), regular(" iría antes que "), inlineCode("\"1.2\""), regular(".")),
                                ),
                            ),
                        ),
                ),
                LessonStep(
                    title = "Varios criterios",
                    explanation =
                        listOf(
                            paragraph(regular("Un objeto tiene un solo orden natural, pero a menudo hace falta ordenar de otra forma. Un "), inlineCode("Comparator<T>"), regular(" es un criterio de orden aparte del objeto, y "), inlineCode("sortedWith"), regular(" ordena con él.")),
                            paragraph(inlineCode("compareBy"), regular(" y "), inlineCode("compareByDescending"), regular(" crean uno a partir de una lambda, y "), inlineCode("thenBy"), regular(" o "), inlineCode("thenByDescending"), regular(" añaden un criterio para desempatar. Entre "), inlineCode("< >"), regular(" va el tipo que se compara, porque aquí Kotlin no puede deducirlo.")),
                        ),
                    visual =
                        LessonVisualContent.CodeExamples(
                            listOf(
                                LessonCodeExample(
                                    title = "Por curso y por nota",
                                    lines =
                                        kotlinCode(
                                            """
                                            data class Alumno(val nombre: String, val curso: Int, val nota: Int)

                                            fun main() {
                                                val alumnos = listOf(Alumno("Pau", 2, 7), Alumno("Ana", 1, 9), Alumno("Luis", 2, 9), Alumno("Marta", 1, 6))
                                                val porCurso = compareBy<Alumno> { it.curso }.thenByDescending { it.nota }
                                                println(alumnos.sortedWith(porCurso).map { it.nombre })
                                                val porNota = compareByDescending<Alumno> { it.nota }.thenBy { it.nombre }
                                                println(alumnos.sortedWith(porNota).map { it.nombre })
                                            }
                                            """.trimIndent(),
                                        ),
                                    note = richText(regular("Muestra "), inlineCode("[Ana, Marta, Luis, Pau]"), regular(": por curso y, dentro de cada curso, de mayor a menor nota. Después, "), inlineCode("[Ana, Luis, Pau, Marta]"), regular(": Ana y Luis empatan a 9 y se ordenan por nombre.")),
                                ),
                            ),
                        ),
                ),
                LessonStep(
                    title = "Agrupar y contar",
                    explanation =
                        listOf(
                            paragraph(regular("¿Recuerdas los bucles para contar palabras y para guardar varias notas por alumno? "), inlineCode("groupingBy { … }.eachCount()"), regular(" cuenta cuántas veces sale cada valor, y "), inlineCode("groupBy"), regular(" reúne en un mapa de listas los elementos que comparten clave.")),
                            paragraph(inlineCode("associateBy"), regular(" crea un mapa para buscar cada elemento por una clave única, como el DNI, y "), inlineCode("associateWith"), regular(" calcula un valor para cada elemento. "), inlineCode("mapValues"), regular(" transforma los valores de un mapa; dentro, "), inlineCode("it"), regular(" es cada entrada, con su "), inlineCode("key"), regular(" y su "), inlineCode("value"), regular(".")),
                        ),
                    visual =
                        LessonVisualContent.CodeExamples(
                            listOf(
                                LessonCodeExample(
                                    title = "Contar y agrupar sin bucles",
                                    lines =
                                        kotlinCode(
                                            """
                                            fun main() {
                                                val palabras = listOf("sol", "luna", "sol", "mar", "sol", "luna")
                                                println(palabras.groupingBy { it }.eachCount())
                                                val examenes = listOf("Ana" to 7, "Luis" to 4, "Ana" to 9)
                                                val notas = examenes.groupBy(keySelector = { it.first }, valueTransform = { it.second })
                                                println(notas)
                                                println(notas.mapValues { it.value.max() })
                                            }
                                            """.trimIndent(),
                                        ),
                                    note = richText(regular("Muestra "), inlineCode("{sol=3, luna=2, mar=1}"), regular(", "), inlineCode("{Ana=[7, 9], Luis=[4]}"), regular(" y "), inlineCode("{Ana=9, Luis=4}"), regular(": la nota más alta de cada uno.")),
                                ),
                                LessonCodeExample(
                                    title = "Buscar por clave",
                                    lines =
                                        kotlinCode(
                                            """
                                            data class Alumno(val dni: String, val nombre: String, val curso: Int)

                                            fun main() {
                                                val alumnos = listOf(Alumno("111A", "Ana", 1), Alumno("222B", "Luis", 2), Alumno("333C", "Marta", 1))
                                                println(alumnos.groupBy { it.curso }.mapValues { it.value.map { alumno -> alumno.nombre } })
                                                val porDni = alumnos.associateBy { it.dni }
                                                println(porDni["222B"]?.nombre)
                                                println(listOf("Ana", "Luis", "Marta").associateWith { it.length })
                                            }
                                            """.trimIndent(),
                                        ),
                                    note = richText(regular("Muestra "), inlineCode("{1=[Ana, Marta], 2=[Luis]}"), regular(", "), inlineCode("Luis"), regular(" y "), inlineCode("{Ana=3, Luis=4, Marta=5}"), regular(".")),
                                ),
                            ),
                        ),
                ),
                LessonStep(
                    title = "Partir, emparejar y aplanar",
                    explanation =
                        listOf(
                            paragraph(inlineCode("partition"), regular(" separa una colección en dos listas, las que cumplen la condición y las que no, y las devuelve en un "), inlineCode("Pair"), regular(" que se puede separar en dos variables.")),
                            paragraph(inlineCode("zip"), regular(" empareja dos listas posición a posición. "), inlineCode("flatten"), regular(" convierte una lista de listas en una sola, y "), inlineCode("flatMap"), regular(" hace un "), inlineCode("map"), regular(" que devuelve listas y las une. "), inlineCode("split(\" \")"), regular(" parte un texto por los espacios.")),
                        ),
                    visual =
                        LessonVisualContent.Trace(
                            example =
                                LessonCodeExample(
                                    title = "Cuatro operaciones",
                                    lines =
                                        kotlinCode(
                                            """
                                            fun main() {
                                                val notas = listOf(7, 4, 9, 3, 6)
                                                val (aprobadas, suspensas) = notas.partition { it >= 5 }
                                                println(aprobadas)
                                                println(suspensas)
                                                val nombres = listOf("Ana", "Luis", "Marta")
                                                println(nombres.zip(listOf(1, 2, 3)))
                                                val grupos = listOf(listOf("Ana", "Luis"), listOf("Marta"), listOf("Pau", "Eva"))
                                                println(grupos.flatten())
                                                val frases = listOf("hola mundo", "adiós")
                                                println(frases.flatMap { it.split(" ") })
                                            }
                                            """.trimIndent(),
                                        ),
                                    note = richText(regular("Cada línea con "), inlineCode("println"), regular(" muestra el resultado de la tabla.")),
                                ),
                            table =
                                LessonTraceTable(
                                    title = "Qué muestra cada línea",
                                    columns = listOf("Línea", "Operación", "Muestra"),
                                    rows =
                                        listOf(
                                            listOf("4", "partition (cumplen)", "[7, 9, 6]"),
                                            listOf("5", "partition (no cumplen)", "[4, 3]"),
                                            listOf("7", "zip", "[(Ana, 1), (Luis, 2), (Marta, 3)]"),
                                            listOf("9", "flatten", "[Ana, Luis, Marta, Pau, Eva]"),
                                            listOf("11", "flatMap", "[hola, mundo, adiós]"),
                                        ),
                                ),
                        ),
                ),
                LessonStep(
                    title = "Acumular con fold",
                    explanation =
                        listOf(
                            paragraph(inlineCode("fold"), regular(" recorre la colección llevando un "), emphasis("acumulado"), regular(": empieza con el valor inicial y, en cada vuelta, la lambda recibe el acumulado y el elemento y devuelve el nuevo acumulado. Es el patrón de tus bucles de suma, en una línea.")),
                            paragraph(regular("Para sumar ya existe "), inlineCode("sum()"), regular("; "), inlineCode("fold"), regular(" brilla cuando el resultado es de otro tipo, como el texto de las siglas del ejemplo. "), inlineCode("reduce"), regular(" empieza por el primer elemento y falla si la colección está vacía.")),
                        ),
                    visual =
                        LessonVisualContent.Trace(
                            example =
                                LessonCodeExample(
                                    title = "Sumar, construir un texto y quedarse con el mayor",
                                    lines =
                                        kotlinCode(
                                            """
                                            fun main() {
                                                val precios = listOf(12.5, 7.5, 30.0)
                                                val total = precios.fold(0.0) { acumulado, precio -> acumulado + precio }
                                                println(total)
                                                val palabras = listOf("kotlin", "es", "divertido")
                                                val siglas = palabras.fold("") { texto, palabra -> texto + palabra.first().uppercase() }
                                                println(siglas)
                                                println(listOf(3, 8, 5).reduce { mayor, n -> if (n > mayor) n else mayor })
                                            }
                                            """.trimIndent(),
                                        ),
                                    note = richText(regular("Muestra "), inlineCode("50.0"), regular(", "), inlineCode("KED"), regular(" y "), inlineCode("8"), regular(".")),
                                ),
                            table =
                                LessonTraceTable(
                                    title = "El fold de la línea 3, vuelta a vuelta",
                                    columns = listOf("Vuelta", "acumulado", "precio", "Devuelve"),
                                    rows =
                                        listOf(
                                            listOf("1", "0.0", "12.5", "12.5"),
                                            listOf("2", "12.5", "7.5", "20.0"),
                                            listOf("3", "20.0", "30.0", "50.0"),
                                        ),
                                ),
                        ),
                ),
                LessonStep(
                    title = "Probar una clase genérica",
                    explanation =
                        listOf(
                            paragraph(regular("Una clase genérica se prueba como cualquier otra, pero conviene hacerlo con más de un tipo para comprobar que no depende de ninguno. Al crearla vacía no hay valor del que deducir "), inlineCode("T"), regular(", así que se escribe: "), inlineCode("PilaEnLista<String>()"), regular(".")),
                            paragraph(regular("Las pruebas del ejemplo son de la pila de «Una pila para cualquier tipo», guardada en "), inlineCode("src/main/kotlin/Pila.kt"), regular(". Cubren el orden de salida, otro tipo y la pila vacía.")),
                        ),
                    visual =
                        LessonVisualContent.CodeExamples(
                            listOf(
                                LessonCodeExample(
                                    title = "src/test/kotlin/PilaTest.kt",
                                    fileName = "PilaTest.kt",
                                    lines =
                                        kotlinCode(
                                            """
                                            import kotlin.test.Test
                                            import kotlin.test.assertEquals
                                            import kotlin.test.assertNull
                                            import kotlin.test.assertTrue

                                            class PilaTest {
                                                @Test
                                                fun `sale primero el ultimo que entra`() {
                                                    val pila = PilaEnLista<String>()
                                                    pila.apilar("a")
                                                    pila.apilar("b")
                                                    assertEquals("b", pila.desapilar())
                                                    assertEquals("a", pila.desapilar())
                                                    assertTrue(pila.estaVacia())
                                                }

                                                @Test
                                                fun `funciona con numeros`() {
                                                    val pila = PilaEnLista<Int>()
                                                    pila.apilar(7)
                                                    assertEquals(7, pila.cima())
                                                    assertEquals(7, pila.desapilar())
                                                }

                                                @Test
                                                fun `una pila vacia no tiene cima`() {
                                                    assertNull(PilaEnLista<Int>().cima())
                                                }
                                            }
                                            """.trimIndent(),
                                        ),
                                    note = richText(regular("Las tres pruebas pasan.")),
                                ),
                            ),
                        ),
                ),
                LessonStep(
                    title = "Ahora te toca: el torneo",
                    explanation =
                        listOf(
                            paragraph(regular("Este programa guarda las partidas de un torneo de juegos de mesa y las muestra todas. Cada partida dice quién jugó, a qué y cuántos puntos sacó.")),
                            paragraph(regular("Amplíalo para que muestre la clasificación y algunos datos del torneo. Intenta "), highlight("no escribir bucles"), regular(" y escribir cada función para que sirva con el tipo que le pidan.")),
                        ),
                    visual =
                        LessonVisualContent.CodeExamples(
                            listOf(
                                LessonCodeExample(
                                    title = "Partidas del torneo",
                                    lines =
                                        kotlinCode(
                                            $$"""
                                                fun main() {
                                                    val partidas =
                                                        listOf(
                                                            Partida("Ana", "Ajedrez", 30),
                                                            Partida("Luis", "Damas", 20),
                                                            Partida("Marta", "Ajedrez", 40),
                                                            Partida("Ana", "Damas", 10),
                                                            Partida("Luis", "Ajedrez", 20),
                                                            Partida("Pau", "Go", 50),
                                                            Partida("Marta", "Damas", 10),
                                                        )
                                                    mostrar(partidas)
                                                }

                                                data class Partida(val jugador: String, val juego: String, val puntos: Int)

                                                fun mostrar(partidas: List<Partida>) {
                                                    partidas.forEach { println("${it.jugador} (${it.juego}): ${it.puntos}") }
                                                }
                                            """.trimIndent(),
                                        ),
                                    note = richText(regular("Muestra las siete partidas, de "), inlineCode("Ana (Ajedrez): 30"), regular(" a "), inlineCode("Marta (Damas): 10"), regular(".")),
                                    isCopyable = true,
                                ),
                            ),
                        ),
                    links =
                        listOf(
                            LessonLink("Genéricos", "https://kotlinlang.org/docs/generics.html"),
                            LessonLink("Visión general de las colecciones", "https://kotlinlang.org/docs/collections-overview.html"),
                            LessonLink("Ordenar colecciones", "https://kotlinlang.org/docs/collection-ordering.html"),
                            LessonLink("Agrupar elementos", "https://kotlinlang.org/docs/collection-grouping.html"),
                            LessonLink("Operaciones de agregación", "https://kotlinlang.org/docs/collection-aggregate.html"),
                            LessonLink("ArrayDeque", "https://kotlinlang.org/api/core/kotlin-stdlib/kotlin.collections/-array-deque/"),
                        ),
                    practice =
                        LessonPractice(
                            id = "generics-collections-tournament",
                            objective = richText(highlight("Escribir funciones y clases que sirvan para cualquier tipo, y resumir datos agrupando, contando y ordenando con varios criterios.")),
                            statement = richText(regular("Cambia el programa para que muestre el informe del torneo con las funciones que se piden, sin "), inlineCode("for"), regular(" ni "), inlineCode("while"), regular(", y comprueba su resultado con pruebas.")),
                            requirements =
                                listOf(
                                    richText(regular("Crea "), inlineCode("puntosPorJugador(partidas: List<Partida>): Map<String, Int>"), regular(", con el total de puntos de cada jugador.")),
                                    richText(regular("Crea "), inlineCode("clasificacion(partidas: List<Partida>): List<Pair<String, Int>>"), regular(", con cada jugador y su total, de más a menos puntos. Si dos jugadores empatan, va antes el primero en orden alfabético.")),
                                    richText(regular("Crea "), inlineCode("fun <T> masRepetido(elementos: List<T>): T?"), regular(", que devuelva el elemento que más veces aparece; si hay empate, el que aparece antes en la lista; y "), inlineCode("null"), regular(" si la lista está vacía. Úsala para el juego más jugado y la puntuación más repetida.")),
                                    richText(regular("Crea "), inlineCode("class Recientes<T>(capacidad: Int)"), regular(" con "), inlineCode("registrar(elemento: T)"), regular(" y "), inlineCode("todos(): List<T>"), regular(". Recuerda solo los "), inlineCode("capacidad"), regular(" últimos elementos registrados y los devuelve del más antiguo al más reciente. Úsala para mostrar las tres últimas partidas.")),
                                    richText(regular("El programa muestra el informe como en la primera ejecución. Si no hay partidas, como en la segunda (con "), inlineCode("listOf<Partida>()"), regular("), muestra solo "), inlineCode("No hay partidas"), regular(".")),
                                    richText(regular("Crea "), inlineCode("TorneoTest"), regular(" con pruebas de las tres funciones y de "), inlineCode("Recientes"), regular(", con empates, con listas vacías y con más de un tipo.")),
                                ),
                            expectedOutput =
                                listOf(
                                    consoleRun(
                                        consoleOutput("Clasificación:"),
                                        consoleOutput("1. Marta: 50"),
                                        consoleOutput("2. Pau: 50"),
                                        consoleOutput("3. Ana: 40"),
                                        consoleOutput("4. Luis: 40"),
                                        consoleOutput("Juego más jugado: Ajedrez"),
                                        consoleOutput("Puntuación más repetida: 20"),
                                        consoleOutput("Últimas partidas:"),
                                        consoleOutput("Luis (Ajedrez): 20"),
                                        consoleOutput("Pau (Go): 50"),
                                        consoleOutput("Marta (Damas): 10"),
                                    ),
                                    consoleRun(
                                        consoleOutput("No hay partidas"),
                                    ),
                                ),
                            hints =
                                listOf(
                                    richText(inlineCode("puntosPorJugador"), regular(" cabe en una línea: agrupa las partidas por jugador con "), inlineCode("groupBy"), regular(" y suma los puntos de cada grupo con "), inlineCode("mapValues"), regular(" y "), inlineCode("sumOf"), regular(".")),
                                    richText(inlineCode("toList()"), regular(" convierte un mapa en una lista de pares. Ordénala con "), inlineCode("sortedWith"), regular(" y un criterio que empiece por "), inlineCode("compareByDescending<Pair<String, Int>> { it.second }"), regular(" y desempate con "), inlineCode("thenBy"), regular(".")),
                                    richText(inlineCode("groupingBy { it }.eachCount()"), regular(" respeta el orden de aparición, y "), inlineCode("maxByOrNull"), regular(" se queda con el primero de los empatados. El resultado es una entrada del mapa: te interesa su "), inlineCode("key"), regular(".")),
                                    richText(regular("Dentro de "), inlineCode("Recientes"), regular(", guarda los elementos en un "), inlineCode("ArrayDeque<T>"), regular(" privado: añade por el final y, si te pasas de la capacidad, quita el primero. "), inlineCode("todos()"), regular(" puede devolver una copia con "), inlineCode("toList()"), regular(". Para numerar la clasificación, recuerda "), inlineCode("withIndex()"), regular(".")),
                                ),
                            extension = richText(regular("Cambia "), inlineCode("clasificacion"), regular(" para que devuelva una lista de "), inlineCode("data class Puntuacion(val jugador: String, val puntos: Int)"), regular(", con su orden natural, y ordénala con "), inlineCode("sorted()"), regular(". ¿Qué versión se lee mejor?")),
                            confirmation = richText(regular("Confirma que las dos ejecuciones dan la salida esperada, que ninguna de tus funciones usa "), inlineCode("for"), regular(" ni "), inlineCode("while"), regular(" y que "), inlineCode("./gradlew test"), regular(" termina con "), inlineCode("BUILD SUCCESSFUL"), regular(".")),
                        ),
                ),
            ),
    )
