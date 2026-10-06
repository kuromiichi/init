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

val oopRelationshipsLessonContent =
    LessonContent(
        lessonId = "oop-relationships",
        steps =
            listOf(
                LessonStep(
                    title = "Objetos que se conocen",
                    explanation =
                        listOf(
                            paragraph(regular("Un programa real casi nunca es un solo objeto. Un curso tiene alumnos, un pedido tiene líneas y una biblioteca presta libros a sus socios. Cada objeto hace lo suyo y colabora con otros para resolver el problema entero.")),
                            paragraph(regular("Hay tres relaciones muy comunes: "), highlight("tiene un"), regular(" (un alumno tiene una dirección), "), highlight("tiene varios"), regular(" (un curso tiene varios alumnos) y "), highlight("usa"), regular(" (una factura usa un descuento para calcular su total).")),
                            paragraph(regular("En código, las tres son lo mismo: un objeto que se guarda en una propiedad o que se recibe como parámetro. Esta lección enseña a escribirlas, a protegerlas y a probarlas.")),
                        ),
                    visual = LessonVisualContent.Flow(listOf("Un objeto necesita a otro", "Lo guarda en una propiedad", "Le pide lo que sabe hacer", "Los dos colaboran")),
                ),
                LessonStep(
                    title = "Un objeto dentro de otro",
                    explanation =
                        listOf(
                            paragraph(regular("La relación «tiene un» se escribe con una propiedad cuyo tipo es otra clase. Se crea primero el objeto que va dentro y se entrega al constructor del que lo contiene.")),
                            paragraph(regular("Para llegar al dato de la parte se encadenan puntos: "), inlineCode("ana.direccion.ciudad"), regular(" se lee de izquierda a derecha. "), inlineCode("ana.direccion"), regular(" es un objeto "), inlineCode("Direccion"), regular(" y a ese objeto se le pide su "), inlineCode("ciudad"), regular(".")),
                        ),
                    visual =
                        LessonVisualContent.CodeExamples(
                            listOf(
                                LessonCodeExample(
                                    title = "Un alumno con su dirección",
                                    lines =
                                        kotlinCode(
                                            """
                                            class Direccion(val calle: String, val ciudad: String)

                                            class Alumno(val nombre: String, val direccion: Direccion)

                                            fun main() {
                                                val direccion = Direccion("Calle Mayor 3", "Toledo")
                                                val ana = Alumno("Ana", direccion)
                                                println(ana.nombre)
                                                println(ana.direccion.ciudad)
                                            }
                                            """.trimIndent(),
                                        ),
                                    note = richText(regular("Muestra "), inlineCode("Ana"), regular(" y "), inlineCode("Toledo"), regular(". Dos objetos distintos: el alumno guarda una referencia a la dirección.")),
                                ),
                                LessonCodeExample(
                                    title = "El tipo tiene que ser el de la parte",
                                    lines =
                                        kotlinCode(
                                            """
                                            class Direccion(val calle: String, val ciudad: String)

                                            class Alumno(val nombre: String, val direccion: Direccion)

                                            fun main() {
                                                val ana = Alumno("Ana", ⟦"Toledo"⟧)
                                            }
                                            """.trimIndent(),
                                        ),
                                    note = richText(regular("IntelliJ IDEA lo marca como error: el constructor espera una "), inlineCode("Direccion"), regular(", no un texto.")),
                                ),
                            ),
                        ),
                ),
                LessonStep(
                    title = "Pedir en lugar de hurgar",
                    explanation =
                        listOf(
                            paragraph(regular("Con las propiedades de las partes se puede hacer la cuenta desde fuera, pero entonces cada sitio que use una línea tiene que saber cómo se calcula su subtotal. Si algún día cambia, hay que encontrarlos todos.")),
                            paragraph(regular("Es mejor que la propia clase lo responda con un método. El que llama pregunta qué quiere saber, no cómo se averigua: es la misma idea de las funciones, aplicada a los objetos.")),
                        ),
                    visual =
                        LessonVisualContent.Trace(
                            example =
                                LessonCodeExample(
                                    title = "La cuenta, hecha fuera y hecha por la clase",
                                    lines =
                                        kotlinCode(
                                            """
                                            class Producto(val nombre: String, val precio: Int)

                                            class Linea(val producto: Producto, val cantidad: Int) {
                                                fun subtotal(): Int {
                                                    return producto.precio * cantidad
                                                }
                                            }

                                            fun main() {
                                                val cafe = Producto("Café", 2)
                                                val linea = Linea(cafe, 3)
                                                println(linea.producto.precio * linea.cantidad)
                                                println(linea.subtotal())
                                            }
                                            """.trimIndent(),
                                        ),
                                    note = richText(regular("Muestra "), inlineCode("6"), regular(" dos veces. El resultado es el mismo, pero con "), inlineCode("subtotal()"), regular(" la fórmula vive en un solo sitio.")),
                                ),
                            table =
                                LessonTraceTable(
                                    title = "Qué ocurre en cada línea de main",
                                    columns = listOf("Línea", "Qué ocurre", "Muestra"),
                                    rows =
                                        listOf(
                                            listOf("10", "cafe es un Producto: Café, 2 €", "–"),
                                            listOf("11", "linea guarda cafe y la cantidad 3", "–"),
                                            listOf("12", "main multiplica el precio y la cantidad", "6"),
                                            listOf("13", "linea calcula su subtotal: 2 × 3", "6"),
                                        ),
                                ),
                        ),
                ),
                LessonStep(
                    title = "Uno a varios",
                    explanation =
                        listOf(
                            paragraph(regular("Cuando un objeto tiene varios, guarda una colección: "), inlineCode("mutableListOf<Alumno>()"), regular(" para una lista de alumnos. Los métodos de la clase la recorren con un bucle y usan lo que cada elemento sabe de sí mismo.")),
                            paragraph(regular("Un curso sin alumnos no tiene media: devolver "), inlineCode("0.0"), regular(" es una decisión de la clase, y conviene comprobar "), inlineCode("isEmpty()"), regular(" antes de dividir, igual que con una lista suelta.")),
                        ),
                    visual =
                        LessonVisualContent.Trace(
                            example =
                                LessonCodeExample(
                                    title = "Un curso que calcula la media de sus alumnos",
                                    lines =
                                        kotlinCode(
                                            """
                                            class Alumno(val nombre: String, var nota: Int)

                                            class Curso(val nombre: String) {
                                                private val alumnos = mutableListOf<Alumno>()

                                                fun matricular(alumno: Alumno) {
                                                    alumnos.add(alumno)
                                                }

                                                fun media(): Double {
                                                    if (alumnos.isEmpty()) {
                                                        return 0.0
                                                    }
                                                    var suma = 0
                                                    for (alumno in alumnos) {
                                                        suma += alumno.nota
                                                    }
                                                    return suma.toDouble() / alumnos.size
                                                }
                                            }

                                            fun main() {
                                                val curso = Curso("Kotlin")
                                                println(curso.media())
                                                curso.matricular(Alumno("Ana", 7))
                                                curso.matricular(Alumno("Luis", 4))
                                                println(curso.media())
                                            }
                                            """.trimIndent(),
                                        ),
                                    note = richText(regular("Muestra "), inlineCode("0.0"), regular(" y "), inlineCode("5.5"), regular(". La lista es "), inlineCode("private"), regular(": desde fuera solo se puede matricular y preguntar la media.")),
                                ),
                            table =
                                LessonTraceTable(
                                    title = "Alumnos del curso en cada línea de main",
                                    columns = listOf("Línea", "Alumnos", "Muestra"),
                                    rows =
                                        listOf(
                                            listOf("23", "ninguno", "–"),
                                            listOf("24", "ninguno", "0.0"),
                                            listOf("25", "Ana (7)", "–"),
                                            listOf("26", "Ana (7), Luis (4)", "–"),
                                            listOf("27", "Ana (7), Luis (4)", "5.5"),
                                        ),
                                ),
                        ),
                ),
                LessonStep(
                    title = "Proteger la colección",
                    explanation =
                        listOf(
                            paragraph(regular("A veces el exterior necesita ver los alumnos. Si la propiedad pública fuera una lista mutable, cualquiera podría añadir, quitar o vaciar sin pasar por la clase. Un "), inlineCode("val"), regular(" solo impide reasignar la propiedad, no cambiar su contenido.")),
                            paragraph(regular("La convención de Kotlin es una propiedad privada mutable con guion bajo ("), inlineCode("_alumnos"), regular(") y otra pública de solo lectura con el nombre bueno, que la devuelve como "), inlineCode("List<Alumno>"), regular(". Quien la usa puede leerla, pero no modificarla.")),
                            callout(
                                "Una vista, no una copia",
                                richText(inlineCode("alumnos"), regular(" muestra la lista real: si el curso cambia después, lo que se ve también cambia. Para quedarse con una foto de este momento, "), inlineCode("toList()"), regular(" crea una copia independiente.")),
                                LessonCalloutTone.Information,
                            ),
                        ),
                    visual =
                        LessonVisualContent.CodeExamples(
                            listOf(
                                LessonCodeExample(
                                    title = "Una vista y una copia",
                                    lines =
                                        kotlinCode(
                                            """
                                            class Alumno(val nombre: String, var nota: Int)

                                            class Curso(val nombre: String) {
                                                private val _alumnos = mutableListOf<Alumno>()
                                                val alumnos: List<Alumno>
                                                    get() = _alumnos

                                                fun matricular(alumno: Alumno) {
                                                    _alumnos.add(alumno)
                                                }
                                            }

                                            fun main() {
                                                val curso = Curso("Kotlin")
                                                curso.matricular(Alumno("Ana", 7))
                                                val vista = curso.alumnos
                                                println(vista.size)
                                                curso.matricular(Alumno("Luis", 4))
                                                println(vista.size)
                                                val copia = curso.alumnos.toList()
                                                curso.matricular(Alumno("Eva", 9))
                                                println(copia.size)
                                                println(curso.alumnos.size)
                                            }
                                            """.trimIndent(),
                                        ),
                                    note = richText(regular("Muestra "), inlineCode("1"), regular(", "), inlineCode("2"), regular(", "), inlineCode("2"), regular(" y "), inlineCode("3"), regular(". La vista crece con el curso; la copia se queda como estaba.")),
                                ),
                                LessonCodeExample(
                                    title = "Desde fuera no se puede modificar",
                                    lines =
                                        kotlinCode(
                                            """
                                            class Alumno(val nombre: String, var nota: Int)

                                            class Curso(val nombre: String) {
                                                private val _alumnos = mutableListOf<Alumno>()
                                                val alumnos: List<Alumno>
                                                    get() = _alumnos
                                            }

                                            fun main() {
                                                val curso = Curso("Kotlin")
                                                curso.alumnos.⟦add⟧(Alumno("Ana", 7))
                                            }
                                            """.trimIndent(),
                                        ),
                                    note = richText(regular("IntelliJ IDEA lo marca como error: "), inlineCode("List"), regular(" no tiene "), inlineCode("add"), regular(". Para matricular hay que llamar al método del curso.")),
                                ),
                            ),
                        ),
                ),
                LessonStep(
                    title = "Composición: partes que no viven solas",
                    explanation =
                        listOf(
                            paragraph(regular("Hay partes que no tienen sentido sin el todo: una línea de pedido no existe fuera de su pedido. Es la "), highlight("composición"), regular(": el todo crea sus partes, las guarda y nadie más las ve. Si el pedido desaparece, sus líneas también.")),
                            paragraph(regular("Se consigue declarando la clase de la parte dentro del todo, con "), inlineCode("private"), regular(". Desde fuera no se puede ni crear ni nombrar. El producto, en cambio, existía antes y se entrega ya creado.")),
                        ),
                    visual =
                        LessonVisualContent.CodeExamples(
                            listOf(
                                LessonCodeExample(
                                    title = "Un pedido que crea sus líneas",
                                    lines =
                                        kotlinCode(
                                            """
                                            class Producto(val nombre: String, val precio: Int)

                                            class Pedido(val cliente: String) {
                                                private class Linea(val producto: Producto, val cantidad: Int) {
                                                    fun subtotal(): Int {
                                                        return producto.precio * cantidad
                                                    }
                                                }

                                                private val lineas = mutableListOf<Linea>()

                                                fun agregar(producto: Producto, cantidad: Int) {
                                                    require(cantidad > 0) { "La cantidad debe ser positiva" }
                                                    lineas.add(Linea(producto, cantidad))
                                                }

                                                fun total(): Int {
                                                    var total = 0
                                                    for (linea in lineas) {
                                                        total += linea.subtotal()
                                                    }
                                                    return total
                                                }
                                            }

                                            fun main() {
                                                val cafe = Producto("Café", 2)
                                                val pedido = Pedido("Ana")
                                                pedido.agregar(cafe, 3)
                                                pedido.agregar(Producto("Bollo", 3), 2)
                                                println(pedido.total())
                                            }
                                            """.trimIndent(),
                                        ),
                                    note = richText(regular("Muestra "), inlineCode("12"), regular(" (6 del café y 6 de los bollos). "), inlineCode("main"), regular(" nunca ve una "), inlineCode("Linea"), regular(": solo habla con el pedido.")),
                                ),
                                LessonCodeExample(
                                    title = "Una línea suelta no se puede crear",
                                    lines =
                                        kotlinCode(
                                            """
                                            class Producto(val nombre: String, val precio: Int)

                                            class Pedido(val cliente: String) {
                                                private class Linea(val producto: Producto, val cantidad: Int)
                                            }

                                            fun main() {
                                                val cafe = Producto("Café", 2)
                                                val linea = Pedido.⟦Linea⟧(cafe, 1)
                                            }
                                            """.trimIndent(),
                                        ),
                                    note = richText(regular("IntelliJ IDEA lo marca como error: "), inlineCode("Linea"), regular(" es privada en "), inlineCode("Pedido"), regular(".")),
                                ),
                            ),
                        ),
                ),
                LessonStep(
                    title = "Agregación: objetos que ya existían",
                    explanation =
                        listOf(
                            paragraph(regular("Otras partes tienen vida propia. Un alumno existe antes de matricularse, puede estar en varios cursos a la vez y sigue existiendo cuando un curso lo da de baja. Es la "), highlight("agregación"), regular(": el todo recibe objetos ya creados y los referencia.")),
                            paragraph(regular("Es la misma idea de «Copiar no es duplicar»: los dos cursos guardan una referencia al "), emphasis("mismo"), regular(" alumno, no una copia. Y "), inlineCode("remove"), regular(" compara con "), inlineCode("=="), regular(", que en una clase normal es identidad: quita justo ese objeto.")),
                        ),
                    visual =
                        LessonVisualContent.Trace(
                            example =
                                LessonCodeExample(
                                    title = "Una alumna en dos cursos",
                                    lines =
                                        kotlinCode(
                                            """
                                            class Alumno(val nombre: String)

                                            class Curso(val nombre: String) {
                                                private val alumnos = mutableListOf<Alumno>()

                                                fun matricular(alumno: Alumno) {
                                                    alumnos.add(alumno)
                                                }

                                                fun darDeBaja(alumno: Alumno) {
                                                    alumnos.remove(alumno)
                                                }

                                                fun numeroDeAlumnos(): Int {
                                                    return alumnos.size
                                                }
                                            }

                                            fun main() {
                                                val lucia = Alumno("Lucía")
                                                val programacion = Curso("Programación")
                                                val bd = Curso("Bases de datos")
                                                programacion.matricular(lucia)
                                                bd.matricular(lucia)
                                                programacion.darDeBaja(lucia)
                                                println(programacion.numeroDeAlumnos())
                                                println(bd.numeroDeAlumnos())
                                                println(lucia.nombre)
                                            }
                                            """.trimIndent(),
                                        ),
                                    note = richText(regular("Muestra "), inlineCode("0"), regular(", "), inlineCode("1"), regular(" y "), inlineCode("Lucía"), regular(". Darla de baja de un curso no la destruye ni la quita del otro.")),
                                ),
                            table =
                                LessonTraceTable(
                                    title = "Alumnos de cada curso en cada línea de main",
                                    columns = listOf("Línea", "programacion", "bd", "Muestra"),
                                    rows =
                                        listOf(
                                            listOf("22", "ninguno", "ninguno", "–"),
                                            listOf("23", "Lucía", "ninguno", "–"),
                                            listOf("24", "Lucía", "Lucía", "–"),
                                            listOf("25", "ninguno", "Lucía", "–"),
                                            listOf("26", "ninguno", "Lucía", "0"),
                                            listOf("27", "ninguno", "Lucía", "1"),
                                            listOf("28", "ninguno", "Lucía", "Lucía"),
                                        ),
                                ),
                        ),
                ),
                LessonStep(
                    title = "Recibir lo que se necesita",
                    explanation =
                        listOf(
                            paragraph(regular("En la relación «usa», una clase necesita a otra para hacer su trabajo. Si la crea ella misma, queda atada a esa elección: para probar otro caso habría que editar la clase. Si la recibe, quien la construye decide.")),
                            paragraph(regular("Recibirla en el constructor es lo más habitual; también puede llegar como parámetro de un método, si solo se usa en esa llamada. Y las pruebas se vuelven sencillas: cada una entrega el colaborador que necesita.")),
                        ),
                    visual =
                        LessonVisualContent.CodeExamples(
                            listOf(
                                LessonCodeExample(
                                    title = "La factura crea su descuento",
                                    lines =
                                        kotlinCode(
                                            """
                                            class Descuento(val porcentaje: Int) {
                                                fun aplicar(importe: Int): Int {
                                                    return importe - importe * porcentaje / 100
                                                }
                                            }

                                            class Factura(val cliente: String, val importe: Int) {
                                                private val descuento = Descuento(10)

                                                fun total(): Int {
                                                    return descuento.aplicar(importe)
                                                }
                                            }

                                            fun main() {
                                                val factura = Factura("Ana", 200)
                                                println(factura.total())
                                            }
                                            """.trimIndent(),
                                        ),
                                    note = richText(regular("Muestra "), inlineCode("180"), regular(". Funciona, pero siempre aplica un 10 %: no hay forma de probar otro porcentaje sin cambiar la clase.")),
                                ),
                                LessonCodeExample(
                                    title = "La factura recibe su descuento",
                                    lines =
                                        kotlinCode(
                                            """
                                            class Descuento(val porcentaje: Int) {
                                                fun aplicar(importe: Int): Int {
                                                    return importe - importe * porcentaje / 100
                                                }
                                            }

                                            class Factura(val cliente: String, val importe: Int, private val descuento: Descuento) {
                                                fun total(): Int {
                                                    return descuento.aplicar(importe)
                                                }
                                            }

                                            fun main() {
                                                val sinDescuento = Factura("Ana", 200, Descuento(0))
                                                val conDescuento = Factura("Luis", 200, Descuento(25))
                                                println(sinDescuento.total())
                                                println(conDescuento.total())
                                            }
                                            """.trimIndent(),
                                        ),
                                    note = richText(regular("Muestra "), inlineCode("200"), regular(" y "), inlineCode("150"), regular(". Con "), inlineCode("private val"), regular(" en el constructor, el descuento es propiedad de la factura pero no se ve desde fuera.")),
                                ),
                            ),
                        ),
                ),
                LessonStep(
                    title = "Buscar entre objetos",
                    explanation =
                        listOf(
                            paragraph(regular("Con una colección de objetos hace falta encontrar uno concreto. Con una lista se recorre y se devuelve el primero que coincide; si no hay ninguno, el resultado es "), inlineCode("null"), regular(", y el tipo lo dice: "), inlineCode("Alumno?"), regular(".")),
                            paragraph(regular("Si se busca siempre por la misma clave, un mapa lo hace sin recorrer nada: "), inlineCode("alumnos[nombre]"), regular(" ya devuelve "), inlineCode("Alumno?"), regular(". Quien llama maneja el "), inlineCode("null"), regular(" como ya sabe: comprobándolo, con "), inlineCode("?."), regular(" o con "), inlineCode("?:"), regular(".")),
                            callout(
                                "La clave tiene que ser única",
                                richText(regular("Con un mapa por nombre, dos alumnos con el mismo nombre se pisarían. En un programa real la clave sería un identificador que no se repita, como el DNI.")),
                                LessonCalloutTone.Warning,
                            ),
                        ),
                    visual =
                        LessonVisualContent.CodeExamples(
                            listOf(
                                LessonCodeExample(
                                    title = "Buscar recorriendo la lista",
                                    lines =
                                        kotlinCode(
                                            """
                                            class Alumno(val nombre: String, var nota: Int)

                                            class Curso(val nombre: String) {
                                                private val alumnos = mutableListOf<Alumno>()

                                                fun matricular(alumno: Alumno) {
                                                    alumnos.add(alumno)
                                                }

                                                fun buscar(nombre: String): Alumno? {
                                                    for (alumno in alumnos) {
                                                        if (alumno.nombre == nombre) {
                                                            return alumno
                                                        }
                                                    }
                                                    return null
                                                }
                                            }

                                            fun main() {
                                                val curso = Curso("Kotlin")
                                                curso.matricular(Alumno("Ana", 7))
                                                val ana = curso.buscar("Ana")
                                                if (ana != null) {
                                                    println(ana.nota)
                                                }
                                                println(curso.buscar("Eva")?.nota)
                                                println(curso.buscar("Eva")?.nota ?: "No está matriculada")
                                            }
                                            """.trimIndent(),
                                        ),
                                    note = richText(regular("Muestra "), inlineCode("7"), regular(", "), inlineCode("null"), regular(" y "), inlineCode("No está matriculada"), regular(". Con Eva, "), inlineCode("?."), regular(" no llega a pedir la nota.")),
                                ),
                                LessonCodeExample(
                                    title = "Buscar con un mapa",
                                    lines =
                                        kotlinCode(
                                            """
                                            class Alumno(val nombre: String, var nota: Int)

                                            class Curso(val nombre: String) {
                                                private val alumnos = mutableMapOf<String, Alumno>()

                                                fun matricular(alumno: Alumno) {
                                                    alumnos[alumno.nombre] = alumno
                                                }

                                                fun buscar(nombre: String): Alumno? {
                                                    return alumnos[nombre]
                                                }
                                            }

                                            fun main() {
                                                val curso = Curso("Kotlin")
                                                curso.matricular(Alumno("Ana", 7))
                                                val ana = curso.buscar("Ana")
                                                if (ana != null) {
                                                    println(ana.nota)
                                                }
                                                println(curso.buscar("Eva")?.nota)
                                                println(curso.buscar("Eva")?.nota ?: "No está matriculada")
                                            }
                                            """.trimIndent(),
                                        ),
                                    note = richText(regular("Misma salida y mismo "), inlineCode("main"), regular(": quien usa el curso no sabe si por dentro hay una lista o un mapa.")),
                                ),
                            ),
                        ),
                ),
                LessonStep(
                    title = "Relaciones en las dos direcciones",
                    explanation =
                        listOf(
                            paragraph(regular("A veces parece útil que el curso conozca a sus alumnos y que cada alumno conozca su curso. Es posible, pero cada flecha extra es otra cosa que mantener coherente: si matriculas a Ana y olvidas apuntarle el curso, los dos objetos se contradicen.")),
                            paragraph(regular("Con "), inlineCode("data class"), regular(" hay un peligro más. Su "), inlineCode("toString()"), regular(" muestra las propiedades del constructor, y si el curso muestra al alumno y el alumno muestra al curso, el ciclo no termina: la pila se agota.")),
                            explanationCode(
                                "Main.kt",
                                richText(regular("La línea 8 cierra el ciclo y la 10 lo recorre sin fin.")),
                                "data class Alumno(val nombre: String, var curso: Curso?)",
                                "",
                                "data class Curso(val nombre: String, val alumnos: MutableList<Alumno>)",
                                "",
                                "fun main() {",
                                "    val ana = Alumno(\"Ana\", null)",
                                "    val kotlin = Curso(\"Kotlin\", mutableListOf(ana))",
                                "    ana.curso = kotlin",
                                "    println(ana.nombre)",
                                "    println(kotlin)",
                                "}",
                            ),
                            callout(
                                "Quita una flecha",
                                richText(regular("Si el curso conoce a sus alumnos, que el alumno no guarde el curso. Si de verdad hacen falta las dos, deja una fuera del "), inlineCode("toString()"), regular(" con uno propio, como en la lección anterior.")),
                                LessonCalloutTone.Tip,
                            ),
                        ),
                    visual =
                        LessonVisualContent.Console(
                            kind = LessonConsoleKind.IntelliJRun,
                            runs =
                                listOf(
                                    consoleRun(
                                        consoleOutput("Ana"),
                                        consoleError("Exception in thread \"main\" java.lang.StackOverflowError"),
                                        consoleError("    at java.base/java.lang.AbstractStringBuilder.append(AbstractStringBuilder.java:649)"),
                                        consoleError("    at java.base/java.lang.StringBuilder.append(StringBuilder.java:179)"),
                                        consoleError("    at Alumno.toString(Main.kt:1)"),
                                        consoleError("    at java.base/java.lang.String.valueOf(String.java:4530)"),
                                        consoleError("    at java.base/java.lang.StringBuilder.append(StringBuilder.java:173)"),
                                        consoleError("    at java.base/java.util.AbstractCollection.toString(AbstractCollection.java:459)"),
                                        consoleError("    at java.base/java.lang.String.valueOf(String.java:4530)"),
                                        consoleError("    at java.base/java.lang.StringBuilder.append(StringBuilder.java:173)"),
                                        consoleError("    at Curso.toString(Main.kt:3)"),
                                        consoleError("    at java.base/java.lang.String.valueOf(String.java:4530)"),
                                        consoleError("    ..."),
                                        consoleOutput(""),
                                        consoleOutput("Process finished with exit code 1"),
                                    ),
                                ),
                            caption = "La traza real sigue repitiendo el mismo ciclo hasta llenar la pila.",
                        ),
                ),
                LessonStep(
                    title = "Organizar en paquetes",
                    explanation =
                        listOf(
                            paragraph(regular("Con varias clases, cada una vive en su propio archivo con su nombre. Para agruparlas se usan los "), highlight("paquetes"), regular(": la primera línea del archivo es "), inlineCode("package escuela"), regular(", y los archivos van en una carpeta con ese nombre, en minúsculas.")),
                            paragraph(regular("Dentro del mismo paquete las clases se ven entre sí. Para usar una de otro paquete se escribe "), inlineCode("import"), regular(" con su ruta completa. Todo es público por defecto; "), inlineCode("private"), regular(" lo limita a su clase o archivo, e "), inlineCode("internal"), regular(" a todo el módulo.")),
                        ),
                    visual =
                        LessonVisualContent.CodeExamples(
                            listOf(
                                LessonCodeExample(
                                    title = "src/main/kotlin/escuela/Alumno.kt",
                                    fileName = "Alumno.kt",
                                    lines =
                                        kotlinCode(
                                            """
                                            package escuela

                                            class Alumno(val nombre: String, var nota: Int)
                                            """.trimIndent(),
                                        ),
                                    note = richText(regular("El paquete es lo primero del archivo. La carpeta "), inlineCode("escuela"), regular(" tiene el mismo nombre.")),
                                ),
                                LessonCodeExample(
                                    title = "src/main/kotlin/escuela/Curso.kt",
                                    fileName = "Curso.kt",
                                    lines =
                                        kotlinCode(
                                            """
                                            package escuela

                                            class Curso(val nombre: String) {
                                                private val alumnos = mutableListOf<Alumno>()

                                                fun matricular(alumno: Alumno) {
                                                    alumnos.add(alumno)
                                                }

                                                fun numeroDeAlumnos(): Int {
                                                    return alumnos.size
                                                }
                                            }
                                            """.trimIndent(),
                                        ),
                                    note = richText(inlineCode("Curso"), regular(" usa "), inlineCode("Alumno"), regular(" sin "), inlineCode("import"), regular(": están en el mismo paquete.")),
                                ),
                                LessonCodeExample(
                                    title = "src/main/kotlin/Main.kt",
                                    fileName = "Main.kt",
                                    lines =
                                        kotlinCode(
                                            """
                                            import escuela.Alumno
                                            import escuela.Curso

                                            fun main() {
                                                val curso = Curso("Kotlin")
                                                curso.matricular(Alumno("Ana", 7))
                                                println(curso.numeroDeAlumnos())
                                            }
                                            """.trimIndent(),
                                        ),
                                    note = richText(regular("Muestra "), inlineCode("1"), regular(". "), inlineCode("main"), regular(" está fuera del paquete y necesita los dos "), inlineCode("import"), regular(".")),
                                ),
                            ),
                        ),
                ),
                LessonStep(
                    title = "Probar objetos que colaboran",
                    explanation =
                        listOf(
                            paragraph(regular("Probar clases que colaboran es igual que probar una sola: se crean los objetos, se llama a los métodos y se comprueba el estado que queda. No hace falta nada especial, porque cada prueba construye sus propios objetos.")),
                            paragraph(regular("Prueba lo que el curso promete: que matricular añade, que la media sale de las notas de sus alumnos, que un curso vacío no falla y que dar de baja a un alumno no lo destruye.")),
                        ),
                    visual =
                        LessonVisualContent.CodeExamples(
                            listOf(
                                LessonCodeExample(
                                    title = "src/main/kotlin/Curso.kt",
                                    fileName = "Curso.kt",
                                    lines =
                                        kotlinCode(
                                            """
                                            class Curso(val nombre: String) {
                                                private val _alumnos = mutableListOf<Alumno>()
                                                val alumnos: List<Alumno>
                                                    get() = _alumnos

                                                fun matricular(alumno: Alumno) {
                                                    _alumnos.add(alumno)
                                                }

                                                fun darDeBaja(alumno: Alumno) {
                                                    _alumnos.remove(alumno)
                                                }

                                                fun media(): Double {
                                                    if (_alumnos.isEmpty()) {
                                                        return 0.0
                                                    }
                                                    var suma = 0
                                                    for (alumno in _alumnos) {
                                                        suma += alumno.nota
                                                    }
                                                    return suma.toDouble() / _alumnos.size
                                                }
                                            }
                                            """.trimIndent(),
                                        ),
                                    note = richText(regular("El curso final: lista protegida, matricular, dar de baja y media.")),
                                ),
                                LessonCodeExample(
                                    title = "src/test/kotlin/CursoTest.kt",
                                    fileName = "CursoTest.kt",
                                    lines =
                                        kotlinCode(
                                            """
                                            import kotlin.test.Test
                                            import kotlin.test.assertEquals

                                            class CursoTest {
                                                @Test
                                                fun `matricular a un alumno lo añade al curso`() {
                                                    val curso = Curso("Kotlin")
                                                    curso.matricular(Alumno("Ana", 7))
                                                    assertEquals(1, curso.alumnos.size)
                                                }

                                                @Test
                                                fun `la media sale de las notas de los alumnos`() {
                                                    val curso = Curso("Kotlin")
                                                    curso.matricular(Alumno("Ana", 7))
                                                    curso.matricular(Alumno("Luis", 4))
                                                    assertEquals(5.5, curso.media())
                                                }

                                                @Test
                                                fun `un curso vacío tiene media cero`() {
                                                    assertEquals(0.0, Curso("Kotlin").media())
                                                }

                                                @Test
                                                fun `dar de baja a un alumno no lo destruye`() {
                                                    val curso = Curso("Kotlin")
                                                    val ana = Alumno("Ana", 7)
                                                    curso.matricular(ana)
                                                    curso.darDeBaja(ana)
                                                    assertEquals(0, curso.alumnos.size)
                                                    assertEquals(7, ana.nota)
                                                }
                                            }
                                            """.trimIndent(),
                                        ),
                                    note = richText(regular("Las cuatro pruebas pasan. Cada una crea su propio curso, así que no dependen unas de otras.")),
                                ),
                            ),
                        ),
                ),
                LessonStep(
                    title = "Ahora te toca: la biblioteca",
                    explanation =
                        listOf(
                            paragraph(regular("Este programa lee el nombre de un socio y una serie de operaciones: prestar o devolver un libro por su título. Los libros son textos en dos listas y el socio es solo un nombre. Nada impide que un socio acumule todos los libros.")),
                            paragraph(regular("Conviértelo en clases que colaboren y añade una regla: "), highlight("un socio no puede tener más de dos libros a la vez"), regular(". Una operación que no se puede hacer se rechaza sin cambiar nada.")),
                        ),
                    visual =
                        LessonVisualContent.CodeExamples(
                            listOf(
                                LessonCodeExample(
                                    title = "Biblioteca",
                                    lines =
                                        kotlinCode(
                                            $$"""
                                                fun main() {
                                                    val disponibles = mutableListOf("Kotlin", "Java", "SQL")
                                                    val prestados = mutableListOf<String>()
                                                    println("Socio:")
                                                    val socio = readln()
                                                    println("Operación (1 prestar, 2 devolver, 0 terminar):")
                                                    var operacion = readln().toInt()
                                                    while (operacion != 0) {
                                                        println("Título:")
                                                        val titulo = readln()
                                                        if (operacion == 1) {
                                                            if (titulo in disponibles) {
                                                                disponibles.remove(titulo)
                                                                prestados.add(titulo)
                                                            } else {
                                                                println("No se puede prestar $titulo")
                                                            }
                                                        } else {
                                                            if (titulo in prestados) {
                                                                prestados.remove(titulo)
                                                                disponibles.add(titulo)
                                                            } else {
                                                                println("No se puede devolver $titulo")
                                                            }
                                                        }
                                                        println("Operación (1 prestar, 2 devolver, 0 terminar):")
                                                        operacion = readln().toInt()
                                                    }
                                                    println("$socio tiene $prestados")
                                                }
                                            """.trimIndent(),
                                        ),
                                    note = richText(regular("Con "), inlineCode("Ana"), regular(" y tres préstamos seguidos, la última línea muestra "), inlineCode("Ana tiene [Kotlin, Java, SQL]"), regular(": no hay límite.")),
                                    isCopyable = true,
                                ),
                            ),
                        ),
                    links =
                        listOf(
                            LessonLink("Clases e instancias", "https://kotlinlang.org/docs/classes.html"),
                            LessonLink("Propiedades y propiedades de respaldo", "https://kotlinlang.org/docs/properties.html"),
                            LessonLink("Colecciones", "https://kotlinlang.org/docs/collections-overview.html"),
                            LessonLink("Paquetes e importaciones", "https://kotlinlang.org/docs/packages.html"),
                            LessonLink("Modificadores de visibilidad", "https://kotlinlang.org/docs/visibility-modifiers.html"),
                        ),
                    practice =
                        LessonPractice(
                            id = "oop-relationships-library",
                            objective = richText(highlight("Modelar un problema con varias clases que colaboran, protegiendo sus colecciones y rechazando lo que no tiene sentido.")),
                            statement = richText(regular("Crea las clases "), inlineCode("Libro"), regular(", "), inlineCode("Socio"), regular(" y "), inlineCode("Biblioteca"), regular(", haz que "), inlineCode("main"), regular(" trabaje con ellas en lugar de con las dos listas, y comprueba la biblioteca con pruebas.")),
                            requirements =
                                listOf(
                                    richText(inlineCode("Libro(val titulo: String)"), regular(": al mostrar un libro con "), inlineCode("println"), regular(" sale su título.")),
                                    richText(inlineCode("Socio(val nombre: String)"), regular(": no puede tener más de "), inlineCode("2"), regular(" libros a la vez.")),
                                    richText(inlineCode("prestamos"), regular(": los libros del socio. Se pueden consultar, pero no modificar desde fuera.")),
                                    richText(inlineCode("Biblioteca"), regular(": "), inlineCode("agregar(libro: Libro)"), regular(" añade un libro al catálogo.")),
                                    richText(inlineCode("prestar(socio: Socio, titulo: String): Boolean"), regular(": presta el libro con ese título al socio. Se rechaza si no existe, si ya está prestado o si el socio tiene el máximo; devuelve "), inlineCode("false"), regular(" y todo queda como estaba.")),
                                    richText(inlineCode("devolver(socio: Socio, titulo: String): Boolean"), regular(": el socio devuelve el libro y puede volver a prestarse. Se rechaza si ese socio no lo tiene; devuelve "), inlineCode("false"), regular(" y todo queda como estaba.")),
                                    richText(regular("Al mostrar un socio con "), inlineCode("println"), regular(" sale "), inlineCode("Ana tiene [Java]"), regular(".")),
                                    richText(inlineCode("main"), regular(" crea la biblioteca con "), inlineCode("Kotlin"), regular(", "), inlineCode("Java"), regular(" y "), inlineCode("SQL"), regular(", y si una operación se rechaza avisa con "), inlineCode("No se puede prestar SQL"), regular(" o "), inlineCode("No se puede devolver Kotlin"), regular(".")),
                                    richText(regular("Cada clase va en su archivo. Crea "), inlineCode("BibliotecaTest"), regular(" con pruebas de un préstamo, un libro ya prestado a otro socio, el límite de préstamos, una devolución y una devolución de un libro que el socio no tiene.")),
                                ),
                            expectedOutput =
                                listOf(
                                    consoleRun(
                                        consoleOutput("Socio:"),
                                        consoleInput("Ana"),
                                        consoleOutput("Operación (1 prestar, 2 devolver, 0 terminar):"),
                                        consoleInput("1"),
                                        consoleOutput("Título:"),
                                        consoleInput("Kotlin"),
                                        consoleOutput("Operación (1 prestar, 2 devolver, 0 terminar):"),
                                        consoleInput("1"),
                                        consoleOutput("Título:"),
                                        consoleInput("Java"),
                                        consoleOutput("Operación (1 prestar, 2 devolver, 0 terminar):"),
                                        consoleInput("1"),
                                        consoleOutput("Título:"),
                                        consoleInput("SQL"),
                                        consoleOutput("No se puede prestar SQL"),
                                        consoleOutput("Operación (1 prestar, 2 devolver, 0 terminar):"),
                                        consoleInput("2"),
                                        consoleOutput("Título:"),
                                        consoleInput("Kotlin"),
                                        consoleOutput("Operación (1 prestar, 2 devolver, 0 terminar):"),
                                        consoleInput("2"),
                                        consoleOutput("Título:"),
                                        consoleInput("Kotlin"),
                                        consoleOutput("No se puede devolver Kotlin"),
                                        consoleOutput("Operación (1 prestar, 2 devolver, 0 terminar):"),
                                        consoleInput("0"),
                                        consoleOutput("Ana tiene [Java]"),
                                    ),
                                    consoleRun(
                                        consoleOutput("Socio:"),
                                        consoleInput("Luis"),
                                        consoleOutput("Operación (1 prestar, 2 devolver, 0 terminar):"),
                                        consoleInput("1"),
                                        consoleOutput("Título:"),
                                        consoleInput("Python"),
                                        consoleOutput("No se puede prestar Python"),
                                        consoleOutput("Operación (1 prestar, 2 devolver, 0 terminar):"),
                                        consoleInput("0"),
                                        consoleOutput("Luis tiene []"),
                                    ),
                                ),
                            hints =
                                listOf(
                                    richText(regular("Antes de escribir, piensa qué tiene que saber cada objeto y qué le va a preguntar a otro. Repasa «Uno a varios» y «Proteger la colección».")),
                                    richText(regular("Piensa dónde está un libro mientras está prestado y adónde va al devolverlo. «Agregación: objetos que ya existían» te ayuda a decidir qué objeto es el mismo en cada momento.")),
                                    richText(regular("Una operación que puede fallar por varios motivos: comprueba todos antes de cambiar nada, y así un rechazo no deja la operación a medias.")),
                                    richText(regular("Buscar un título puede no encontrar nada: repasa «Buscar entre objetos». Para mostrar el socio y el libro, repasa «Mostrar un objeto» de la lección anterior.")),
                                ),
                            extension = richText(regular("Haz que la biblioteca guarde un historial de préstamos con una "), inlineCode("data class Prestamo(val socio: String, val titulo: String)"), regular(". ¿Cómo lo expones sin que nadie pueda modificarlo desde fuera?")),
                            confirmation = richText(regular("Confirma que las dos ejecuciones dan la salida esperada, que "), inlineCode("socio.prestamos.add(...)"), regular(" no compila desde "), inlineCode("main"), regular(" y que "), inlineCode("./gradlew test"), regular(" termina con "), inlineCode("BUILD SUCCESSFUL"), regular(".")),
                        ),
                ),
            ),
    )
