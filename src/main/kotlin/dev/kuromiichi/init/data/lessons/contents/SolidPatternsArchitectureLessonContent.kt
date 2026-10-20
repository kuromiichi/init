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
import dev.kuromiichi.init.data.lessons.steps
import dev.kuromiichi.init.data.lessons.umlClass
import dev.kuromiichi.init.data.lessons.umlClassName

val solidPatternsArchitectureLessonContent =
    LessonContent(
        lessonId = "solid-patterns-architecture",
        steps =
            listOf(
                LessonStep(
                    title = "Diseñar para cambiar",
                    explanation =
                        listOf(
                            paragraph(regular("Un programa que se usa no deja de cambiar: llegan reglas nuevas, otra forma de guardar los datos u otra pantalla. Un buen diseño no adivina esos cambios, pero hace que cada uno toque "), highlight("poco código"), regular(" y en un sitio previsible.")),
                            paragraph(regular("En esta lección pondrás nombre a ideas que ya has usado: los principios "), highlight("SOLID"), regular(", unos pocos "), highlight("patrones de diseño"), regular(" y una forma sencilla de organizar un programa en "), highlight("capas"), regular(".")),
                        ),
                    visual = LessonVisualContent.Flow(listOf("Llega un cambio", "¿Cuántas clases toca?", "Pocas y previsibles: buen diseño")),
                ),
                LessonStep(
                    title = "Cohesión y acoplamiento",
                    explanation =
                        listOf(
                            paragraph(regular("Dos medidas resumen un buen diseño. La "), highlight("cohesión"), regular(" es alta cuando todo lo que hay en una clase sirve a un mismo propósito. El "), highlight("acoplamiento"), regular(" es bajo cuando una clase necesita saber poco de las demás.")),
                            paragraph(regular("En el primer ejemplo, "), inlineCode("total"), regular(" conoce por dentro el carrito, sus líneas y sus productos: si cambia cualquiera, hay que cambiarla. En el segundo, solo pide el total, como en «Pedir en lugar de hurgar» de la lección de relaciones.")),
                        ),
                    visual =
                        LessonVisualContent.CodeExamples(
                            listOf(
                                LessonCodeExample(
                                    title = "Saber demasiado",
                                    lines =
                                        kotlinCode(
                                            """
                                            fun total(carrito: Carrito): Int {
                                                var suma = 0
                                                for (linea in carrito.lineas) {
                                                    suma += linea.producto.precio *
                                                        linea.cantidad
                                                }
                                                return suma
                                            }
                                            """.trimIndent(),
                                        ),
                                    note = richText(regular("Acoplada a "), inlineCode("Carrito"), regular(", "), inlineCode("Linea"), regular(" y "), inlineCode("Producto"), regular(". Si las líneas pasan a guardar un descuento, esta función también cambia.")),
                                ),
                                LessonCodeExample(
                                    title = "Pedir lo que se necesita",
                                    lines =
                                        kotlinCode(
                                            """
                                            fun total(carrito: Carrito): Int =
                                                carrito.total()
                                            """.trimIndent(),
                                        ),
                                    note = richText(regular("Solo depende de que el carrito sepa calcular su total. Cómo lo hace queda dentro de "), inlineCode("Carrito"), regular(", que es quien tiene los datos.")),
                                ),
                            ),
                        ),
                ),
                LessonStep(
                    title = "S: responsabilidad única",
                    explanation =
                        listOf(
                            paragraph(regular("Una clase tiene una "), highlight("responsabilidad"), regular(" cuando se ocupa de una sola tarea: guardar alumnos, calcular notas o mostrar un informe. El "), highlight("principio de responsabilidad única"), regular(" pide que cada clase tenga solo una.")),
                            paragraph(regular("Una pista: si para describir lo que hace una clase necesitas decir «y», tiene más de una. El primer "), inlineCode("Informe"), regular(" calcula la media, prepara el texto y lo imprime. En el segundo ejemplo, "), inlineCode("Calculadora"), regular(" solo calcula, "), inlineCode("Formato"), regular(" solo prepara el texto y "), inlineCode("main"), regular(" lo muestra.")),
                            paragraph(regular("Así cada clase es más fácil de entender y de probar, y cambiar el formato del texto no obliga a tocar el cálculo.")),
                        ),
                    visual =
                        LessonVisualContent.CodeExamples(
                            listOf(
                                LessonCodeExample(
                                    title = "Tres tareas en una clase",
                                    lines =
                                        kotlinCode(
                                            $$"""
                                                class Informe(val notas: List<Int>) {
                                                    fun media(): Int =
                                                        notas.sum() / notas.size

                                                    fun comoTexto(): String =
                                                        "Media: ${media()}"

                                                    fun imprimir() {
                                                        println(comoTexto())
                                                    }
                                                }
                                            """.trimIndent(),
                                        ),
                                    note = richText(regular("Calcula, prepara el texto e imprime: tres tareas en una sola clase.")),
                                ),
                                LessonCodeExample(
                                    title = "Una tarea por clase",
                                    lines =
                                        kotlinCode(
                                            $$"""
                                                class Calculadora {
                                                    fun media(notas: List<Int>): Int =
                                                        notas.sum() / notas.size
                                                }

                                                class Formato {
                                                    fun comoTexto(media: Int): String =
                                                        "Media: $media"
                                                }

                                                fun main() {
                                                    val notas = listOf(6, 8)
                                                    val media = Calculadora().media(notas)
                                                    println(Formato().comoTexto(media))
                                                }
                                            """.trimIndent(),
                                        ),
                                    note = richText(regular("Muestra "), inlineCode("Media: 7"), regular(". Cada clase hace una sola tarea y se puede probar por separado.")),
                                ),
                            ),
                        ),
                ),
                LessonStep(
                    title = "O: abierto a ampliar, cerrado a modificar",
                    explanation =
                        listOf(
                            paragraph(regular("El "), highlight("principio abierto/cerrado"), regular(" pide que se pueda añadir comportamiento sin modificar el código que ya funciona. Cada vez que editas una función probada para meter un caso más, puedes romper los anteriores.")),
                            paragraph(regular("En el primer ejemplo, un descuento nuevo es una rama más del "), inlineCode("when"), regular(". En el segundo, cada descuento es una clase que cumple la interfaz "), inlineCode("Descuento"), regular(": añadir uno es escribir una clase nueva, y "), inlineCode("precioFinal"), regular(" no cambia.")),
                        ),
                    visual =
                        LessonVisualContent.CodeExamples(
                            listOf(
                                LessonCodeExample(
                                    title = "Un when que crece",
                                    lines =
                                        kotlinCode(
                                            """
                                            fun precioFinal(
                                                precio: Int,
                                                tipo: String,
                                            ): Int =
                                                when (tipo) {
                                                    "estudiante" -> precio * 80 / 100
                                                    "socio" -> precio * 90 / 100
                                                    else -> precio
                                                }
                                            """.trimIndent(),
                                        ),
                                    note = richText(regular("Un descuento para familias numerosas obliga a abrir y modificar "), inlineCode("precioFinal"), regular(".")),
                                ),
                                LessonCodeExample(
                                    title = "Un descuento, una clase",
                                    lines =
                                        kotlinCode(
                                            """
                                            interface Descuento {
                                                fun aplicar(precio: Int): Int
                                            }

                                            class Estudiante : Descuento {
                                                override fun aplicar(precio: Int): Int =
                                                    precio * 80 / 100
                                            }

                                            class SinDescuento : Descuento {
                                                override fun aplicar(precio: Int): Int =
                                                    precio
                                            }

                                            fun precioFinal(
                                                precio: Int,
                                                descuento: Descuento,
                                            ): Int = descuento.aplicar(precio)
                                            """.trimIndent(),
                                        ),
                                    note = richText(inlineCode("precioFinal(50, Estudiante())"), regular(" devuelve "), inlineCode("40"), regular(". El descuento de socio sería otra clase, sin tocar nada de lo anterior.")),
                                ),
                            ),
                        ),
                ),
                LessonStep(
                    title = "L: sustitución de Liskov",
                    explanation =
                        listOf(
                            paragraph(regular("El "), highlight("principio de sustitución de Liskov"), regular(" dice que una hija debe poder ocupar el lugar de su base sin sorpresas. Ya lo viste con el pingüino que no vuela, en «Herencia o composición» de la lección de herencia.")),
                            paragraph(regular("En el primer ejemplo, "), inlineCode("Deposito"), regular(" hereda "), inlineCode("retirar"), regular(" y lo convierte en una excepción: quien recibe una "), inlineCode("Cuenta"), regular(" ya no puede fiarse. En el segundo, solo la cuenta corriente sabe retirar, y el compilador impide el error.")),
                        ),
                    visual =
                        LessonVisualContent.CodeExamples(
                            listOf(
                                LessonCodeExample(
                                    title = "Una hija que rompe la promesa",
                                    lines =
                                        kotlinCode(
                                            """
                                            open class Cuenta(var saldo: Int) {
                                                open fun retirar(importe: Int) {
                                                    saldo -= importe
                                                }
                                            }

                                            class Deposito(saldo: Int) : Cuenta(saldo) {
                                                override fun retirar(importe: Int) {
                                                    throw IllegalStateException(
                                                        "No admite retiradas",
                                                    )
                                                }
                                            }

                                            fun cobrarCuota(cuenta: Cuenta) {
                                                cuenta.retirar(5)
                                            }
                                            """.trimIndent(),
                                        ),
                                    note = richText(inlineCode("cobrarCuota(Deposito(100))"), regular(" compila, pero lanza "), inlineCode("IllegalStateException"), regular(" al ejecutarse.")),
                                ),
                                LessonCodeExample(
                                    title = "Solo retira quien puede",
                                    lines =
                                        kotlinCode(
                                            """
                                            abstract class Cuenta(var saldo: Int)

                                            class Corriente(
                                                saldo: Int,
                                            ) : Cuenta(saldo) {
                                                fun retirar(importe: Int) {
                                                    saldo -= importe
                                                }
                                            }

                                            class Deposito(saldo: Int) : Cuenta(saldo)

                                            fun cobrarCuota(cuenta: Corriente) {
                                                cuenta.retirar(5)
                                            }
                                            """.trimIndent(),
                                        ),
                                    note = richText(inlineCode("cobrarCuota(Deposito(100))"), regular(" ya no compila: el error aparece al escribirlo y no en manos de quien usa el programa.")),
                                ),
                            ),
                        ),
                ),
                LessonStep(
                    title = "I: interfaces pequeñas",
                    explanation =
                        listOf(
                            paragraph(regular("El "), highlight("principio de segregación de interfaces"), regular(" pide que ninguna clase tenga que implementar métodos que no usa. Una interfaz grande obliga a rellenar huecos, a menudo con una excepción, y vuelve al problema del paso anterior.")),
                            paragraph(regular("En el segundo ejemplo, la interfaz se divide en dos. La impresora sencilla solo implementa "), inlineCode("Imprime"), regular("; una multifunción implementaría las dos.")),
                        ),
                    visual =
                        LessonVisualContent.CodeExamples(
                            listOf(
                                LessonCodeExample(
                                    title = "Métodos que sobran",
                                    lines =
                                        kotlinCode(
                                            """
                                            interface Multifuncion {
                                                fun imprimir(texto: String)

                                                fun escanear(): String
                                            }

                                            class Sencilla : Multifuncion {
                                                override fun imprimir(texto: String) {
                                                    println(texto)
                                                }

                                                override fun escanear(): String {
                                                    throw IllegalStateException(
                                                        "No sabe escanear",
                                                    )
                                                }
                                            }
                                            """.trimIndent(),
                                        ),
                                    note = richText(regular("Quien recibe una "), inlineCode("Multifuncion"), regular(" cree que puede escanear con cualquiera.")),
                                ),
                                LessonCodeExample(
                                    title = "Una interfaz por capacidad",
                                    lines =
                                        kotlinCode(
                                            """
                                            interface Imprime {
                                                fun imprimir(texto: String)
                                            }

                                            interface Escanea {
                                                fun escanear(): String
                                            }

                                            class Sencilla : Imprime {
                                                override fun imprimir(texto: String) {
                                                    println(texto)
                                                }
                                            }
                                            """.trimIndent(),
                                        ),
                                    note = richText(regular("Una función que necesita escanear pide un "), inlineCode("Escanea"), regular(", y una impresora sencilla no encaja.")),
                                ),
                            ),
                        ),
                ),
                LessonStep(
                    title = "D: inversión de dependencias",
                    explanation =
                        listOf(
                            paragraph(regular("El "), highlight("principio de inversión de dependencias"), regular(" dice que las clases importantes, las que contienen las reglas, no deben depender de los detalles, como dónde se guardan los datos. Las dos partes dependen de una interfaz.")),
                            paragraph(regular("Si "), inlineCode("GestorPedidos"), regular(" creara él mismo una lista de pedidos, dependería de ella. En el diagrama depende de la interfaz "), inlineCode("RepositorioPedidos"), regular(", y las implementaciones también apuntan a ella: la flecha se ha invertido.")),
                            paragraph(regular("Así, guardar los pedidos en un fichero o en una base de datos es añadir otra implementación, sin tocar el gestor.")),
                        ),
                    visual =
                        LessonVisualContent.UmlDiagram(
                            nodes =
                                listOf(
                                    umlClass(id = "GestorPedidos", cell = cell(0, 0, columnSpan = 2), operations = listOf("+ confirmar(pedido: String)")),
                                    umlClass(id = "RepositorioPedidos", cell = cell(1, 0, columnSpan = 2), operations = listOf("+ guardar(pedido: String)*"), stereotype = "interface"),
                                    umlClassName(id = "PedidosEnMemoria", cell = cell(2, 0)),
                                    umlClassName(id = "PedidosEnFichero", cell = cell(2, 1)),
                                ),
                            relations =
                                listOf(
                                    UmlRelation(from = "GestorPedidos", to = "RepositorioPedidos", kind = UmlRelationKind.Dependency),
                                    UmlRelation(from = "PedidosEnMemoria", to = "RepositorioPedidos", kind = UmlRelationKind.Realization),
                                    UmlRelation(from = "PedidosEnFichero", to = "RepositorioPedidos", kind = UmlRelationKind.Realization),
                                ),
                            caption = "Todas las flechas apuntan a la interfaz",
                        ),
                ),
                LessonStep(
                    title = "Inyección de dependencias",
                    explanation =
                        listOf(
                            paragraph(regular("Para que el gestor no cree su repositorio, alguien tiene que dárselo. Es lo que hiciste en «Recibir lo que se necesita» de la lección de relaciones, y tiene nombre: "), highlight("inyección de dependencias"), regular(".")),
                            paragraph(regular("El ejemplo es el código del diagrama del paso anterior. "), inlineCode("GestorPedidos"), regular(" recibe el repositorio en el constructor y "), inlineCode("main"), regular(" es el único sitio que crea los objetos y los conecta. Una prueba puede entregarle otro repositorio sin cambiar el gestor.")),
                            callout(
                                "Un valor por defecto",
                                richText(regular("Si casi siempre se usa la misma implementación, el parámetro puede tener un valor por defecto, como "), inlineCode("= PedidosEnMemoria()"), regular(". Quien necesite otra, como una prueba, la sigue pudiendo pasar.")),
                                LessonCalloutTone.Tip,
                            ),
                        ),
                    visual =
                        LessonVisualContent.CodeExamples(
                            listOf(
                                LessonCodeExample(
                                    title = "El repositorio llega por el constructor",
                                    lines =
                                        kotlinCode(
                                            $$"""
                                                interface RepositorioPedidos {
                                                    fun guardar(pedido: String)
                                                }

                                                class PedidosEnMemoria :
                                                    RepositorioPedidos {
                                                    private val datos: MutableList<String> =
                                                        mutableListOf()

                                                    override fun guardar(pedido: String) {
                                                        datos.add(pedido)
                                                    }
                                                }

                                                class GestorPedidos(
                                                    private val pedidos: RepositorioPedidos,
                                                ) {
                                                    fun confirmar(pedido: String) {
                                                        pedidos.guardar(pedido)
                                                        println("Confirmado: $pedido")
                                                    }
                                                }

                                                fun main() {
                                                    val gestor =
                                                        GestorPedidos(PedidosEnMemoria())
                                                    gestor.confirmar("Libro")
                                                }
                                            """.trimIndent(),
                                        ),
                                    note = richText(regular("Muestra "), inlineCode("Confirmado: Libro"), regular(". "), inlineCode("GestorPedidos"), regular(" solo conoce la interfaz; "), inlineCode("PedidosEnMemoria"), regular(" solo aparece en "), inlineCode("main"), regular(".")),
                                ),
                            ),
                        ),
                ),
                LessonStep(
                    title = "Patrones de diseño",
                    explanation =
                        listOf(
                            paragraph(regular("Un "), highlight("patrón de diseño"), regular(" es una solución con nombre a un problema que aparece una y otra vez. No es código para copiar, sino una idea que adaptas a tu programa; su nombre sirve para explicar un diseño en una palabra.")),
                            paragraph(regular("Los patrones clásicos se agrupan en "), emphasis("creacionales"), regular(" (cómo se crean los objetos), "), emphasis("estructurales"), regular(" (cómo se combinan) y "), emphasis("de comportamiento"), regular(" (cómo colaboran). Verás cuatro muy habituales.")),
                            callout(
                                "Primero el problema",
                                richText(regular("Usa un patrón cuando reconozcas el problema que resuelve. Meterlo sin ese problema añade clases y no mejora nada.")),
                                LessonCalloutTone.Warning,
                            ),
                        ),
                    visual = LessonVisualContent.IconText("Cuatro patrones", "Estrategia: cambiar un algoritmo. Instancia única: un solo objeto de una clase. Factoría: decidir en un sitio qué objeto se crea. Observador: avisar a quien se interese por un cambio."),
                ),
                LessonStep(
                    title = "Estrategia",
                    explanation =
                        listOf(
                            paragraph(regular("El patrón "), highlight("estrategia"), regular(" separa un algoritmo de quien lo usa, para poder cambiarlo sin tocarlo. En el primer ejemplo, el boletín recibe la forma de redondear la nota como un objeto que cumple la interfaz "), inlineCode("Redondeo"), regular(".")),
                            paragraph(regular("En Kotlin, cuando la estrategia es una sola función, basta un "), highlight("tipo función"), regular(": el segundo ejemplo recibe "), inlineCode("(Double) -> Int"), regular(" y cada boletín se crea con una lambda.")),
                        ),
                    visual =
                        LessonVisualContent.CodeExamples(
                            listOf(
                                LessonCodeExample(
                                    title = "Una estrategia con interfaz",
                                    lines =
                                        kotlinCode(
                                            """
                                            import kotlin.math.ceil

                                            interface Redondeo {
                                                fun aplicar(nota: Double): Int
                                            }

                                            class Truncar : Redondeo {
                                                override fun aplicar(
                                                    nota: Double,
                                                ): Int = nota.toInt()
                                            }

                                            class AlAlza : Redondeo {
                                                override fun aplicar(
                                                    nota: Double,
                                                ): Int = ceil(nota).toInt()
                                            }

                                            class Boletin(
                                                private val redondeo: Redondeo,
                                            ) {
                                                fun nota(media: Double): Int =
                                                    redondeo.aplicar(media)
                                            }

                                            fun main() {
                                                println(Boletin(Truncar()).nota(4.6))
                                                println(Boletin(AlAlza()).nota(4.6))
                                            }
                                            """.trimIndent(),
                                        ),
                                    note = richText(regular("Muestra "), inlineCode("4"), regular(" y "), inlineCode("5"), regular(". "), inlineCode("Boletin"), regular(" no sabe cómo se redondea.")),
                                ),
                                LessonCodeExample(
                                    title = "Una estrategia con lambda",
                                    lines =
                                        kotlinCode(
                                            """
                                            import kotlin.math.ceil

                                            class Boletin(
                                                private val redondeo: (Double) -> Int,
                                            ) {
                                                fun nota(media: Double): Int =
                                                    redondeo(media)
                                            }

                                            fun main() {
                                                val truncar = Boletin { it.toInt() }
                                                val subir = Boletin { ceil(it).toInt() }
                                                println(truncar.nota(4.6))
                                                println(subir.nota(4.6))
                                            }
                                            """.trimIndent(),
                                        ),
                                    note = richText(regular("Muestra lo mismo, sin interfaz ni clases para cada forma de redondear.")),
                                ),
                            ),
                        ),
                ),
                LessonStep(
                    title = "Una única instancia con object",
                    explanation =
                        listOf(
                            paragraph(regular("A veces solo debe existir un objeto de una clase, como un contador de identificadores para todo el programa. Es el patrón "), highlight("instancia única"), regular(" ("), emphasis("singleton"), regular("), y en Kotlin se escribe con "), inlineCode("object"), regular(".")),
                            paragraph(regular("Una "), highlight("declaración object"), regular(" declara la clase y su único objeto a la vez: no tiene constructor, se crea la primera vez que se usa y se llama por su nombre. El "), inlineCode("companion object"), regular(" que ya conoces es un "), inlineCode("object"), regular(" dentro de una clase.")),
                            callout(
                                "Cuidado con el estado compartido",
                                richText(regular("Todo el programa comparte el mismo "), inlineCode("Contador"), regular(", también las pruebas: lo que cambia una prueba lo ve la siguiente. Para lo que tenga estado suele ser mejor crear un objeto normal e inyectarlo.")),
                                LessonCalloutTone.Warning,
                            ),
                        ),
                    visual =
                        LessonVisualContent.CodeExamples(
                            listOf(
                                LessonCodeExample(
                                    title = "Un contador para todo el programa",
                                    lines =
                                        kotlinCode(
                                            """
                                            object Contador {
                                                private var siguiente = 1

                                                fun nuevoId(): Int = siguiente++
                                            }

                                            fun main() {
                                                println(Contador.nuevoId())
                                                println(Contador.nuevoId())
                                            }
                                            """.trimIndent(),
                                        ),
                                    note = richText(regular("Muestra "), inlineCode("1"), regular(" y "), inlineCode("2"), regular(": las dos llamadas usan el mismo objeto. "), inlineCode("Contador()"), regular(" no compila.")),
                                ),
                            ),
                        ),
                ),
                LessonStep(
                    title = "Factoría",
                    explanation =
                        listOf(
                            paragraph(regular("Una "), highlight("factoría"), regular(" decide en un único sitio qué objeto crear. Así el resto del programa no necesita conocer las clases concretas, y el "), inlineCode("when"), regular(" que elige no se repite por todas partes.")),
                            paragraph(regular("En el ejemplo, la función "), inlineCode("de"), regular(" del "), inlineCode("companion object"), regular(" de "), inlineCode("Descuento"), regular(" convierte un código en el descuento que le corresponde. "), inlineCode("main"), regular(" solo maneja "), inlineCode("Descuento"), regular(".")),
                        ),
                    visual =
                        LessonVisualContent.CodeExamples(
                            listOf(
                                LessonCodeExample(
                                    title = "Crear el descuento a partir de un código",
                                    lines =
                                        kotlinCode(
                                            """
                                            interface Descuento {
                                                fun aplicar(precio: Int): Int

                                                companion object {
                                                    fun de(codigo: String): Descuento =
                                                        when (codigo) {
                                                            "EST" -> Estudiante()
                                                            else -> SinDescuento()
                                                        }
                                                }
                                            }

                                            class Estudiante : Descuento {
                                                override fun aplicar(precio: Int): Int =
                                                    precio * 80 / 100
                                            }

                                            class SinDescuento : Descuento {
                                                override fun aplicar(precio: Int): Int =
                                                    precio
                                            }

                                            fun main() {
                                                val descuento = Descuento.de("EST")
                                                println(descuento.aplicar(50))
                                            }
                                            """.trimIndent(),
                                        ),
                                    note = richText(regular("Muestra "), inlineCode("40"), regular(". Un descuento nuevo es una clase y una rama en "), inlineCode("de"), regular("; nada más cambia.")),
                                ),
                            ),
                        ),
                ),
                LessonStep(
                    title = "Observador",
                    explanation =
                        listOf(
                            paragraph(regular("El patrón "), highlight("observador"), regular(" sirve para que varios objetos se enteren de un cambio sin que el objeto que cambia sepa quiénes son. Los interesados se "), emphasis("suscriben"), regular(", y cada vez que hay un cambio se avisa a todos.")),
                            paragraph(regular("En el ejemplo, "), inlineCode("Pedido"), regular(" guarda una lista de "), inlineCode("Oyente"), regular(", una interfaz con un solo método, "), inlineCode("avisar"), regular(". Al cambiar de estado recorre la lista y avisa a cada oyente, sin saber si es un cliente o el almacén.")),
                            paragraph(regular("Para avisar también por SMS bastaría otra clase que cumpla "), inlineCode("Oyente"), regular(" y suscribirla en "), inlineCode("main"), regular(": "), inlineCode("Pedido"), regular(" no cambia.")),
                        ),
                    visual =
                        LessonVisualContent.Trace(
                            example =
                                LessonCodeExample(
                                    title = "Avisar de cada cambio de estado",
                                    lines =
                                        kotlinCode(
                                            $$"""
                                                interface Oyente {
                                                    fun avisar(estado: String)
                                                }

                                                class Cliente(val nombre: String) : Oyente {
                                                    override fun avisar(estado: String) {
                                                        println("Correo a $nombre: $estado")
                                                    }
                                                }

                                                class Almacen : Oyente {
                                                    override fun avisar(estado: String) {
                                                        println("Almacén: pedido $estado")
                                                    }
                                                }

                                                class Pedido {
                                                    private val oyentes =
                                                        mutableListOf<Oyente>()

                                                    fun suscribir(oyente: Oyente) {
                                                        oyentes.add(oyente)
                                                    }

                                                    fun cambiarEstado(estado: String) {
                                                        for (oyente in oyentes) {
                                                            oyente.avisar(estado)
                                                        }
                                                    }
                                                }

                                                fun main() {
                                                    val pedido = Pedido()
                                                    pedido.suscribir(Cliente("Ana"))
                                                    pedido.suscribir(Almacen())
                                                    pedido.cambiarEstado("enviado")
                                                    pedido.cambiarEstado("entregado")
                                                }
                                            """.trimIndent(),
                                        ),
                                    note = richText(regular("Cada cambio de estado llega a los dos oyentes, en el orden en que se suscribieron.")),
                                ),
                            table =
                                LessonTraceTable(
                                    title = "Qué muestra cada cambio de estado",
                                    columns = listOf("Línea", "Oyente", "Muestra"),
                                    rows =
                                        listOf(
                                            listOf("36", "Cliente", "Correo a Ana: enviado"),
                                            listOf("36", "Almacen", "Almacén: pedido enviado"),
                                            listOf("37", "Cliente", "Correo a Ana: entregado"),
                                            listOf("37", "Almacen", "Almacén: pedido entregado"),
                                        ),
                                ),
                        ),
                ),
                LessonStep(
                    title = "Arquitectura en capas",
                    explanation =
                        listOf(
                            paragraph(regular("Los principios y patrones ordenan clases sueltas. La "), highlight("arquitectura"), regular(" ordena el programa entero. La más sencilla lo divide en tres capas:")),
                            steps(
                                richText(emphasis("Presentación"), regular(": habla con el usuario. Aquí, "), inlineCode("main"), regular(" y la consola.")),
                                richText(emphasis("Lógica"), regular(": las reglas del programa, en un servicio como "), inlineCode("ServicioPedidos"), regular(".")),
                                richText(emphasis("Datos"), regular(": guarda y recupera la información, detrás de un repositorio.")),
                            ),
                            paragraph(regular("Cada capa usa solo la de debajo, y la lógica llega a los datos a través de una interfaz. Cada capa va en su propio paquete. La presentación tiene sus propios patrones, que verás con las interfaces gráficas.")),
                        ),
                    visual =
                        LessonVisualContent.UmlDiagram(
                            nodes =
                                listOf(
                                    umlClassName(id = "main", cell = cell(0, 0)),
                                    umlClassName(id = "ServicioPedidos", cell = cell(1, 0)),
                                    umlClassName(id = "RepositorioPedidos", cell = cell(2, 0), stereotype = "interface"),
                                    umlClassName(id = "PedidosEnMemoria", cell = cell(3, 0)),
                                ),
                            relations =
                                listOf(
                                    UmlRelation(from = "main", to = "ServicioPedidos", kind = UmlRelationKind.Dependency),
                                    UmlRelation(from = "ServicioPedidos", to = "RepositorioPedidos", kind = UmlRelationKind.Dependency),
                                    UmlRelation(from = "PedidosEnMemoria", to = "RepositorioPedidos", kind = UmlRelationKind.Realization),
                                ),
                            caption = "Presentación, lógica y datos",
                        ),
                ),
                LessonStep(
                    title = "El repositorio",
                    explanation =
                        listOf(
                            paragraph(regular("Un "), highlight("repositorio"), regular(" es el objeto que guarda y recupera los datos de un tipo, como si fueran una colección. El servicio no sabe si están en memoria, en un fichero o en una base de datos.")),
                            paragraph(regular("Los ejemplos son las capas del diagrama del paso anterior, cada una en su paquete y su archivo. Por ahora los pedidos viven en memoria; en lecciones posteriores los guardarás en un fichero y en una base de datos cambiando solo esta implementación.")),
                        ),
                    visual =
                        LessonVisualContent.CodeExamples(
                            listOf(
                                LessonCodeExample(
                                    title = "src/main/kotlin/tienda/datos/RepositorioPedidos.kt",
                                    fileName = "RepositorioPedidos.kt",
                                    lines =
                                        kotlinCode(
                                            """
                                            package tienda.datos

                                            data class Pedido(
                                                val cliente: String,
                                                val importe: Int,
                                            )

                                            interface RepositorioPedidos {
                                                fun guardar(pedido: Pedido)

                                                fun todos(): List<Pedido>
                                            }
                                            """.trimIndent(),
                                        ),
                                    note = richText(regular("Qué se guarda y qué se puede hacer con ello, sin decir dónde.")),
                                ),
                                LessonCodeExample(
                                    title = "src/main/kotlin/tienda/datos/PedidosEnMemoria.kt",
                                    fileName = "PedidosEnMemoria.kt",
                                    lines =
                                        kotlinCode(
                                            """
                                            package tienda.datos

                                            class PedidosEnMemoria :
                                                RepositorioPedidos {
                                                private val datos: MutableList<Pedido> =
                                                    mutableListOf()

                                                override fun guardar(pedido: Pedido) {
                                                    datos.add(pedido)
                                                }

                                                override fun todos(): List<Pedido> =
                                                    datos.toList()
                                            }
                                            """.trimIndent(),
                                        ),
                                    note = richText(regular("La implementación de hoy. "), inlineCode("todos()"), regular(" devuelve una copia para que nadie cambie la lista desde fuera.")),
                                ),
                                LessonCodeExample(
                                    title = "src/main/kotlin/tienda/logica/ServicioPedidos.kt",
                                    fileName = "ServicioPedidos.kt",
                                    lines =
                                        kotlinCode(
                                            """
                                            package tienda.logica

                                            import tienda.datos.Pedido
                                            import tienda.datos.RepositorioPedidos

                                            class ServicioPedidos(
                                                private val pedidos: RepositorioPedidos,
                                            ) {
                                                fun confirmar(
                                                    cliente: String,
                                                    importe: Int,
                                                ) {
                                                    pedidos.guardar(
                                                        Pedido(cliente, importe),
                                                    )
                                                }

                                                fun total(): Int =
                                                    pedidos.todos().sumOf { it.importe }
                                            }
                                            """.trimIndent(),
                                        ),
                                    note = richText(regular("Solo importa la interfaz, nunca "), inlineCode("PedidosEnMemoria"), regular(".")),
                                ),
                                LessonCodeExample(
                                    title = "src/main/kotlin/Main.kt",
                                    lines =
                                        kotlinCode(
                                            $$"""
                                                import tienda.datos.PedidosEnMemoria
                                                import tienda.logica.ServicioPedidos

                                                fun main() {
                                                    val servicio =
                                                        ServicioPedidos(PedidosEnMemoria())
                                                    servicio.confirmar("Ana", 30)
                                                    servicio.confirmar("Luis", 12)
                                                    println("Total: ${servicio.total()}")
                                                }
                                            """.trimIndent(),
                                        ),
                                    note = richText(regular("Muestra "), inlineCode("Total: 42"), regular(". "), inlineCode("main"), regular(" elige la implementación y la conecta.")),
                                ),
                            ),
                        ),
                ),
                LessonStep(
                    title = "Probar cada capa",
                    explanation =
                        listOf(
                            paragraph(regular("Con capas, cada parte se prueba por separado. Las pruebas del ejemplo son de "), inlineCode("ServicioPedidos"), regular(", del paso «El repositorio», y le entregan un "), inlineCode("PedidosEnMemoria"), regular(" nuevo en cada prueba.")),
                            paragraph(regular("Cuando el programa guarde los pedidos en una base de datos, estas pruebas seguirán usando el repositorio en memoria: serán rápidas y no dependerán de que la base de datos exista.")),
                        ),
                    visual =
                        LessonVisualContent.CodeExamples(
                            listOf(
                                LessonCodeExample(
                                    title = "src/test/kotlin/ServicioPedidosTest.kt",
                                    fileName = "ServicioPedidosTest.kt",
                                    lines =
                                        kotlinCode(
                                            """
                                            import kotlin.test.Test
                                            import kotlin.test.assertEquals
                                            import tienda.datos.PedidosEnMemoria
                                            import tienda.logica.ServicioPedidos

                                            class ServicioPedidosTest {
                                                private fun nuevoServicio() =
                                                    ServicioPedidos(PedidosEnMemoria())

                                                @Test
                                                fun `el total suma los pedidos`() {
                                                    val servicio = nuevoServicio()
                                                    servicio.confirmar("Ana", 30)
                                                    servicio.confirmar("Luis", 12)
                                                    assertEquals(42, servicio.total())
                                                }

                                                @Test
                                                fun `sin pedidos el total es 0`() {
                                                    val servicio = nuevoServicio()
                                                    assertEquals(0, servicio.total())
                                                }
                                            }
                                            """.trimIndent(),
                                        ),
                                    note = richText(regular("Las dos pruebas pasan, y ninguna escribe en la consola ni guarda nada fuera de la memoria.")),
                                ),
                            ),
                        ),
                ),
                LessonStep(
                    title = "Ahora te toca: la tienda online",
                    explanation =
                        listOf(
                            paragraph(regular("Este programa confirma los pedidos de una tienda online: calcula el envío, guarda el total, lo muestra y avisa al cliente. Funciona, pero "), inlineCode("Tienda"), regular(" lo hace todo, y añadir un tipo de envío obliga a modificarla.")),
                            paragraph(regular("Reorganízalo con lo que has visto en esta lección para que cada parte tenga una sola tarea y se pueda "), highlight("ampliar sin modificar lo que ya funciona"), regular(".")),
                        ),
                    visual =
                        LessonVisualContent.CodeExamples(
                            listOf(
                                LessonCodeExample(
                                    title = "Tienda online",
                                    lines =
                                        kotlinCode(
                                            $$"""
                                                fun main() {
                                                    val tienda = Tienda()
                                                    tienda.confirmar("Ana", 30, "normal")
                                                    tienda.confirmar("Luis", 12, "urgente")
                                                    tienda.confirmar("Marta", 50, "normal")
                                                    println("Total: ${tienda.total()} €")
                                                }

                                                class Tienda {
                                                    private val lista = mutableListOf<Int>()

                                                    fun confirmar(
                                                        cliente: String,
                                                        importe: Int,
                                                        envio: String,
                                                    ): Int {
                                                        val coste =
                                                            when (envio) {
                                                                "normal" -> 4
                                                                "urgente" -> 9
                                                                else -> null
                                                            }
                                                        require(coste != null) {
                                                            "Envío desconocido: $envio"
                                                        }
                                                        val total = importe + coste
                                                        lista.add(total)
                                                        println("$cliente paga $total €")
                                                        println("Aviso enviado a $cliente")
                                                        return total
                                                    }

                                                    fun total(): Int = lista.sum()
                                                }
                                            """.trimIndent(),
                                        ),
                                    note = richText(regular("Muestra lo que paga cada cliente, un aviso para cada uno y "), inlineCode("Total: 109 €"), regular(".")),
                                    isCopyable = true,
                                ),
                            ),
                        ),
                    links =
                        listOf(
                            LessonLink("Declaraciones object", "https://kotlinlang.org/docs/object-declarations.html"),
                            LessonLink("Interfaces en Kotlin", "https://kotlinlang.org/docs/interfaces.html"),
                            LessonLink("Funciones de orden superior y lambdas", "https://kotlinlang.org/docs/lambdas.html"),
                            LessonLink("Catálogo de patrones de diseño", "https://refactoring.guru/es/design-patterns"),
                            LessonLink("Guía de arquitectura de apps de Android", "https://developer.android.com/topic/architecture"),
                        ),
                    practice =
                        LessonPractice(
                            id = "solid-patterns-architecture-shop",
                            objective = richText(highlight("Reorganizar un programa para que cada clase tenga una sola responsabilidad y se pueda ampliar sin modificar lo que ya funciona.")),
                            statement = richText(regular("Rediseña la tienda sin cambiar lo que muestra, añade un tipo de envío nuevo y comprueba su comportamiento con pruebas.")),
                            requirements =
                                listOf(
                                    richText(regular("Con los mismos pedidos, el programa muestra exactamente lo mismo que el original.")),
                                    richText(inlineCode("Tienda"), regular(" mantiene "), inlineCode("confirmar(cliente: String, importe: Int, envio: String): Int"), regular(", que devuelve lo que paga el cliente, y "), inlineCode("total(): Int"), regular(". Cómo se construye una "), inlineCode("Tienda"), regular(" lo decides tú.")),
                                    richText(regular("Hay un envío nuevo, "), inlineCode("\"recogida\""), regular(", que cuesta 0 €. Añadirlo, y añadir cualquier envío futuro, no obliga a modificar "), inlineCode("Tienda"), regular(".")),
                                    richText(regular("Un envío desconocido se sigue rechazando con "), inlineCode("IllegalArgumentException"), regular(" y el mensaje "), inlineCode("Envío desconocido: dron"), regular(", con el nombre del envío.")),
                                    richText(regular("Crea "), inlineCode("TiendaTest"), regular(": comprueba lo que paga cada tipo de envío, que el total acumula los pedidos, que un envío desconocido se rechaza y que se avisa al cliente, sin que las pruebas escriban nada en la consola.")),
                                ),
                            expectedOutput =
                                listOf(
                                    consoleRun(
                                        consoleOutput("Ana paga 34 €"),
                                        consoleOutput("Aviso enviado a Ana"),
                                        consoleOutput("Luis paga 21 €"),
                                        consoleOutput("Aviso enviado a Luis"),
                                        consoleOutput("Marta paga 54 €"),
                                        consoleOutput("Aviso enviado a Marta"),
                                        consoleOutput("Total: 109 €"),
                                    ),
                                ),
                            hints =
                                listOf(
                                    richText(regular("Haz una lista de todo lo que hace "), inlineCode("Tienda"), regular(": si para describirlo necesitas decir «y», hace más de una tarea. Repasa «S: responsabilidad única».")),
                                    richText(regular("Lo que cambia de un envío a otro es su coste. Repasa «O: abierto a ampliar, cerrado a modificar» y «Estrategia»: ¿qué tendría que recibir "), inlineCode("Tienda"), regular(" para no conocer los envíos?")),
                                    richText(regular("Alguien tiene que convertir el texto "), inlineCode("\"urgente\""), regular(" en el envío que le corresponde, y debe hacerlo en un único sitio. Repasa «Factoría».")),
                                    richText(regular("Para comprobar el aviso sin consola, "), inlineCode("Tienda"), regular(" no debe escribir ella misma: repasa «D: inversión de dependencias», «Inyección de dependencias» y «Probar con interfaces» de la lección de herencia.")),
                                ),
                            extension = richText(regular("Guarda los pedidos detrás de un repositorio, como en «El repositorio», y reparte el programa en paquetes por capas. ¿Qué pruebas has tenido que cambiar?")),
                            confirmation = richText(regular("Confirma que el programa da la salida esperada, que el envío "), inlineCode("\"recogida\""), regular(" funciona sin haber modificado "), inlineCode("Tienda"), regular(" y que "), inlineCode("./gradlew test"), regular(" termina con "), inlineCode("BUILD SUCCESSFUL"), regular(".")),
                        ),
                ),
            ),
    )
