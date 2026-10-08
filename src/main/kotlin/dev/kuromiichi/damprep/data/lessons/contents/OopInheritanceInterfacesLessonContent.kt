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

val oopInheritanceInterfacesLessonContent =
    LessonContent(
        lessonId = "oop-inheritance-interfaces",
        steps =
            listOf(
                LessonStep(
                    title = "Familias de clases",
                    explanation =
                        listOf(
                            paragraph(regular("Un perro y un gato son animales: tienen nombre, hacen un sonido y se presentan. Escribir esas partes dos veces es pedir problemas. Con la "), highlight("herencia"), regular(", lo común se escribe una vez en una clase base y cada clase hija añade o cambia lo suyo.")),
                            paragraph(regular("Se comprueba con la pregunta «es un»: un perro "), emphasis("es un"), regular(" animal. No es lo mismo que el «tiene un» de la lección anterior: un coche tiene un motor, pero no es un motor. Y toda clase de Kotlin hereda, sin escribirlo, de "), inlineCode("Any"), regular(".")),
                            paragraph(regular("Esta lección enseña a heredar y sobrescribir, a tratar a todas las hijas como si fueran la base, a firmar contratos con interfaces y a decidir cuándo conviene heredar y cuándo no.")),
                        ),
                    visual = LessonVisualContent.Flow(listOf("Lo común va en la clase base", "Cada clase hija añade lo suyo", "Se tratan todas como la base", "Cada una responde a su manera")),
                ),
                LessonStep(
                    title = "Heredar de una clase",
                    explanation =
                        listOf(
                            paragraph(regular("En Kotlin las clases están cerradas: nadie hereda de ellas salvo que se marquen con "), inlineCode("open"), regular(". Es una protección, porque heredar crea una dependencia fuerte entre las dos clases.")),
                            paragraph(regular("La hija nombra a su base tras los dos puntos y le entrega lo que su constructor pide. En el ejemplo, "), inlineCode("Perro(nombre: String) : Animal(nombre)"), regular(": "), inlineCode("nombre"), regular(" no lleva "), inlineCode("val"), regular(" porque ya es propiedad de "), inlineCode("Animal"), regular(".")),
                        ),
                    visual =
                        LessonVisualContent.CodeExamples(
                            listOf(
                                LessonCodeExample(
                                    title = "Un perro que hereda de animal",
                                    lines =
                                        kotlinCode(
                                            $$"""
                                            open class Animal(val nombre: String) {
                                                fun presentarse() {
                                                    println("Soy $nombre")
                                                }
                                            }

                                            class Perro(nombre: String, val raza: String) : Animal(nombre)

                                            fun main() {
                                                val perro = Perro("Toby", "Beagle")
                                                perro.presentarse()
                                                println(perro.raza)
                                            }
                                            """.trimIndent(),
                                        ),
                                    note = richText(regular("Muestra "), inlineCode("Soy Toby"), regular(" y "), inlineCode("Beagle"), regular(". "), inlineCode("Perro"), regular(" no escribe "), inlineCode("presentarse()"), regular(", pero lo tiene porque es un animal.")),
                                ),
                                LessonCodeExample(
                                    title = "Una clase cerrada no se hereda",
                                    lines =
                                        kotlinCode(
                                            """
                                            class Animal(val nombre: String)

                                            class Perro(nombre: String) : ⟦Animal⟧(nombre)
                                            """.trimIndent(),
                                        ),
                                    note = richText(regular("IntelliJ IDEA lo marca como error: "), inlineCode("Animal"), regular(" es final. Para heredar de ella hay que abrirla con "), inlineCode("open class Animal"), regular(".")),
                                ),
                            ),
                        ),
                ),
                LessonStep(
                    title = "Sobrescribir comportamiento",
                    explanation =
                        listOf(
                            paragraph(regular("A veces la hija debe hacer algo distinto. La base marca el método con "), inlineCode("open"), regular(" y la hija lo redefine con "), inlineCode("override"), regular(". Sin "), inlineCode("open"), regular(" el método es final; y sin "), inlineCode("override"), regular(", el compilador no deja redefinirlo por descuido.")),
                            paragraph(regular("Dentro de la hija, "), inlineCode("super.metodo()"), regular(" llama a la versión de la base: sirve para ampliarla en vez de sustituirla. Es lo que ya hacías con "), inlineCode("toString()"), regular(": viene de "), inlineCode("Any"), regular(" y tu clase lo sobrescribía.")),
                        ),
                    visual =
                        LessonVisualContent.CodeExamples(
                            listOf(
                                LessonCodeExample(
                                    title = "Cada animal con su sonido",
                                    lines =
                                        kotlinCode(
                                            $$"""
                                            open class Animal(val nombre: String) {
                                                open fun sonido(): String {
                                                    return "..."
                                                }

                                                open fun presentarse() {
                                                    println("$nombre dice ${sonido()}")
                                                }
                                            }

                                            class Perro(nombre: String) : Animal(nombre) {
                                                override fun sonido(): String {
                                                    return "guau"
                                                }

                                                override fun presentarse() {
                                                    super.presentarse()
                                                    println("¡Y mueve la cola!")
                                                }
                                            }

                                            class Gato(nombre: String) : Animal(nombre) {
                                                override fun sonido(): String {
                                                    return "miau"
                                                }
                                            }

                                            fun main() {
                                                Perro("Toby").presentarse()
                                                Gato("Misi").presentarse()
                                                Animal("Bicho").presentarse()
                                            }
                                            """.trimIndent(),
                                        ),
                                    note = richText(regular("Muestra cuatro líneas. "), inlineCode("Perro"), regular(" amplía "), inlineCode("presentarse()"), regular(" con "), inlineCode("super"), regular(", "), inlineCode("Gato"), regular(" solo cambia el sonido y "), inlineCode("Animal"), regular(" usa el suyo: "), inlineCode("..."), regular(".")),
                                ),
                                LessonCodeExample(
                                    title = "Un método final no se sobrescribe",
                                    lines =
                                        kotlinCode(
                                            """
                                            open class Animal(val nombre: String) {
                                                fun sonido(): String {
                                                    return "..."
                                                }
                                            }

                                            class Perro(nombre: String) : Animal(nombre) {
                                                ⟦override⟧ fun sonido(): String {
                                                    return "guau"
                                                }
                                            }
                                            """.trimIndent(),
                                        ),
                                    note = richText(regular("IntelliJ IDEA lo marca como error: "), inlineCode("sonido"), regular(" no es "), inlineCode("open"), regular(" en "), inlineCode("Animal"), regular(". Es la base quien decide qué se puede cambiar.")),
                                ),
                            ),
                        ),
                ),
                LessonStep(
                    title = "Primero se inicializa la base",
                    explanation =
                        listOf(
                            paragraph(regular("En el ejemplo, un "), inlineCode("Perro"), regular(" es un solo objeto que también es un "), inlineCode("Animal"), regular(". Al crearlo, primero se inicializa lo que hereda de "), inlineCode("Animal"), regular(" y después lo propio de "), inlineCode("Perro"), regular(". Por eso "), inlineCode("Animal(nombre)"), regular(" va en la cabecera: llama al constructor de la base con lo que necesita.")),
                            paragraph(regular("Los bloques "), inlineCode("init"), regular(" siguen el mismo orden: primero los de la base y luego los de la hija. Cuando empieza el "), inlineCode("init"), regular(" del perro, el nombre del animal ya está guardado.")),
                            callout(
                                "No llames a métodos abiertos desde init",
                                richText(regular("Si el "), inlineCode("init"), regular(" de la base llama a un método "), inlineCode("open"), regular(", se ejecuta la versión de la hija antes de que la hija termine de construirse, y sus propiedades todavía no tienen valor. Evítalo.")),
                                LessonCalloutTone.Warning,
                            ),
                        ),
                    visual =
                        LessonVisualContent.Trace(
                            example =
                                LessonCodeExample(
                                    title = "Primero la base, después la hija",
                                    lines =
                                        kotlinCode(
                                            $$"""
                                            open class Animal(val nombre: String) {
                                                init {
                                                    println("Animal: nace $nombre")
                                                }
                                            }

                                            class Perro(nombre: String, val raza: String) : Animal(nombre) {
                                                init {
                                                    println("Perro: de raza $raza")
                                                }
                                            }

                                            fun main() {
                                                println("Antes")
                                                val perro = Perro("Toby", "Beagle")
                                                println("Después")
                                                println("${perro.nombre} es un ${perro.raza}")
                                            }
                                            """.trimIndent(),
                                        ),
                                    note = richText(regular("Muestra cinco líneas. El "), inlineCode("init"), regular(" de "), inlineCode("Animal"), regular(" se ejecuta antes que el de "), inlineCode("Perro"), regular(", aunque se crea con una sola llamada.")),
                                ),
                            table =
                                LessonTraceTable(
                                    title = "Qué ocurre, en orden de ejecución",
                                    columns = listOf("Línea", "Qué ocurre", "Muestra"),
                                    rows =
                                        listOf(
                                            listOf("14", "main empieza", "Antes"),
                                            listOf("15", "Se crea el Perro: primero se inicializa lo heredado", "–"),
                                            listOf("3", "Se ejecuta el init de Animal", "Animal: nace Toby"),
                                            listOf("9", "Después, el init de Perro", "Perro: de raza Beagle"),
                                            listOf("16", "La creación ha terminado", "Después"),
                                            listOf("17", "main usa la propiedad heredada y la propia", "Toby es un Beagle"),
                                        ),
                                ),
                        ),
                ),
                LessonStep(
                    title = "Lo que ven las hijas",
                    explanation =
                        listOf(
                            paragraph(regular("Con "), inlineCode("private"), regular(", la hija tampoco ve la propiedad de la base. Para que la vean las hijas pero no el resto del programa existe "), inlineCode("protected"), regular(": visible en la clase y en sus hijas, oculto desde fuera.")),
                            paragraph(regular("Úsalo poco. Cada hija que toca ese dato queda atada a cómo lo guarda la base. Si basta con un método de la base, deja la propiedad "), inlineCode("private"), regular(".")),
                        ),
                    visual =
                        LessonVisualContent.CodeExamples(
                            listOf(
                                LessonCodeExample(
                                    title = "La energía la ven las hijas",
                                    lines =
                                        kotlinCode(
                                            """
                                            open class Animal(val nombre: String, protected var energia: Int) {
                                                fun consultarEnergia(): Int {
                                                    return energia
                                                }
                                            }

                                            class Perro(nombre: String) : Animal(nombre, 10) {
                                                fun correr() {
                                                    energia -= 4
                                                }
                                            }

                                            fun main() {
                                                val perro = Perro("Toby")
                                                perro.correr()
                                                println(perro.consultarEnergia())
                                            }
                                            """.trimIndent(),
                                        ),
                                    note = richText(regular("Muestra "), inlineCode("6"), regular(". El perro gasta 4 de energía, pero "), inlineCode("main"), regular(" solo puede consultarla con el método.")),
                                ),
                                LessonCodeExample(
                                    title = "Desde fuera no se ve",
                                    lines =
                                        kotlinCode(
                                            """
                                            open class Animal(val nombre: String, protected var energia: Int)

                                            class Perro(nombre: String) : Animal(nombre, 10)

                                            fun main() {
                                                val perro = Perro("Toby")
                                                println(perro.⟦energia⟧)
                                            }
                                            """.trimIndent(),
                                        ),
                                    note = richText(regular("IntelliJ IDEA lo marca como error: "), inlineCode("energia"), regular(" es protegida y "), inlineCode("main"), regular(" no es una clase hija.")),
                                ),
                            ),
                        ),
                ),
                LessonStep(
                    title = "Clases abstractas",
                    explanation =
                        listOf(
                            paragraph(regular("Hay bases que no deberían existir solas: una figura genérica no tiene área. Una clase "), inlineCode("abstract"), regular(" no se puede instanciar y declara métodos "), inlineCode("abstract"), regular(", sin cuerpo, que cada hija está obligada a implementar.")),
                            paragraph(regular("En el ejemplo, "), inlineCode("Figura"), regular(" mezcla lo que ya sabe hacer ("), inlineCode("describir()"), regular(") con lo que deja por hacer ("), inlineCode("area()"), regular("). "), inlineCode("describir()"), regular(" llama a "), inlineCode("area()"), regular(" sin saber qué figura es: cada hija pone el cálculo. Lo abstracto ya es abierto, no hace falta "), inlineCode("open"), regular(".")),
                        ),
                    visual =
                        LessonVisualContent.CodeExamples(
                            listOf(
                                LessonCodeExample(
                                    title = "Figuras que calculan su área",
                                    lines =
                                        kotlinCode(
                                            $$"""
                                            abstract class Figura(val nombre: String) {
                                                abstract fun area(): Int

                                                fun describir() {
                                                    println("$nombre: área ${area()}")
                                                }
                                            }

                                            class Rectangulo(val ancho: Int, val alto: Int) : Figura("Rectángulo") {
                                                override fun area(): Int {
                                                    return ancho * alto
                                                }
                                            }

                                            class Triangulo(val base: Int, val alto: Int) : Figura("Triángulo") {
                                                override fun area(): Int {
                                                    return base * alto / 2
                                                }
                                            }

                                            fun main() {
                                                val rectangulo = Rectangulo(3, 4)
                                                val triangulo = Triangulo(5, 4)
                                                rectangulo.describir()
                                                triangulo.describir()
                                            }
                                            """.trimIndent(),
                                        ),
                                    note = richText(regular("Muestra "), inlineCode("Rectángulo: área 12"), regular(" y "), inlineCode("Triángulo: área 10"), regular(". "), inlineCode("describir()"), regular(" se escribe una vez y sirve para todas las figuras.")),
                                ),
                                LessonCodeExample(
                                    title = "Lo abstracto obliga y no se instancia",
                                    lines =
                                        kotlinCode(
                                            """
                                            abstract class Figura(val nombre: String) {
                                                abstract fun area(): Int
                                            }

                                            class ⟦Cuadrado⟧(val lado: Int) : Figura("Cuadrado")

                                            fun main() {
                                                val figura = ⟦Figura⟧("Genérica")
                                            }
                                            """.trimIndent(),
                                        ),
                                    note = richText(regular("IntelliJ IDEA marca dos errores: "), inlineCode("Cuadrado"), regular(" no implementa "), inlineCode("area()"), regular(" y "), inlineCode("Figura"), regular(" es abstracta, no se puede crear.")),
                                ),
                            ),
                        ),
                ),
                LessonStep(
                    title = "Polimorfismo",
                    explanation =
                        listOf(
                            paragraph(regular("Como un rectángulo es una figura, cabe en una variable o en una lista de tipo "), inlineCode("Figura"), regular(". Esa lista puede mezclar rectángulos y triángulos, y un solo bucle los trata a todos igual.")),
                            paragraph(regular("En el bucle del ejemplo, al llamar a "), inlineCode("area()"), regular(", Kotlin ejecuta el método del objeto real, no el del tipo de la variable. Es el "), highlight("polimorfismo"), regular(": muchas formas detrás de un mismo nombre. Añadir otra figura mañana no obliga a tocar el bucle.")),
                        ),
                    visual =
                        LessonVisualContent.Trace(
                            example =
                                LessonCodeExample(
                                    title = "Un bucle para todas las figuras",
                                    lines =
                                        kotlinCode(
                                            $$"""
                                            abstract class Figura(val nombre: String) {
                                                abstract fun area(): Int

                                                fun describir() {
                                                    println("$nombre: área ${area()}")
                                                }
                                            }

                                            class Rectangulo(val ancho: Int, val alto: Int) : Figura("Rectángulo") {
                                                override fun area(): Int {
                                                    return ancho * alto
                                                }
                                            }

                                            class Triangulo(val base: Int, val alto: Int) : Figura("Triángulo") {
                                                override fun area(): Int {
                                                    return base * alto / 2
                                                }
                                            }

                                            fun main() {
                                                val figuras: List<Figura> = listOf(Rectangulo(3, 4), Triangulo(5, 4), Rectangulo(2, 2))
                                                var total = 0
                                                for (figura in figuras) {
                                                    figura.describir()
                                                    total += figura.area()
                                                }
                                                println("Total: $total")
                                            }
                                            """.trimIndent(),
                                        ),
                                    note = richText(regular("Muestra cuatro líneas, la última "), inlineCode("Total: 26"), regular(". En cada vuelta, "), inlineCode("area()"), regular(" es la de un rectángulo o la de un triángulo, según el objeto.")),
                                ),
                            table =
                                LessonTraceTable(
                                    title = "Qué área calcula cada vuelta del bucle",
                                    columns = listOf("Vuelta", "figura.area()", "total"),
                                    rows =
                                        listOf(
                                            listOf("1", "Rectángulo: 3 × 4 = 12", "12"),
                                            listOf("2", "Triángulo: 5 × 4 / 2 = 10", "22"),
                                            listOf("3", "Rectángulo: 2 × 2 = 4", "26"),
                                        ),
                                ),
                        ),
                ),
                LessonStep(
                    title = "Preguntar el tipo",
                    explanation =
                        listOf(
                            paragraph(regular("En el ejemplo, la variable "), inlineCode("figura"), regular(" es de tipo "), inlineCode("Figura"), regular(": solo permite pedir lo que "), inlineCode("Figura"), regular(" declara. Si hace falta algo propio de una hija, se pregunta con "), inlineCode("is"), regular(". Dentro del "), inlineCode("if"), regular(", Kotlin ya sabe el tipo y deja usarlo sin más: es un "), highlight("smart cast"), regular(".")),
                            paragraph(inlineCode("!is"), regular(" pregunta lo contrario y "), inlineCode("as?"), regular(" convierte y devuelve "), inlineCode("null"), regular(" si no es de ese tipo, como cualquier valor nulo. El "), inlineCode("as"), regular(" a secas lanza "), inlineCode("ClassCastException"), regular(" si se equivoca: casi nunca compensa.")),
                            callout(
                                "Si abusas de is, falta un método",
                                richText(regular("Un bucle con una cadena de "), inlineCode("is Rectangulo"), regular(", "), inlineCode("is Triangulo"), regular("… indica que ese comportamiento debería ser un método de la base que cada hija implementa. Preguntar el tipo se reserva para lo que solo existe en una hija.")),
                                LessonCalloutTone.Tip,
                            ),
                        ),
                    visual =
                        LessonVisualContent.CodeExamples(
                            listOf(
                                LessonCodeExample(
                                    title = "is, as? y !is",
                                    lines =
                                        kotlinCode(
                                            $$"""
                                            abstract class Figura(val nombre: String) {
                                                abstract fun area(): Int
                                            }

                                            class Rectangulo(val ancho: Int, val alto: Int) : Figura("Rectángulo") {
                                                override fun area(): Int {
                                                    return ancho * alto
                                                }
                                            }

                                            class Triangulo(val base: Int, val alto: Int) : Figura("Triángulo") {
                                                override fun area(): Int {
                                                    return base * alto / 2
                                                }
                                            }

                                            fun main() {
                                                val figuras: List<Figura> = listOf(Rectangulo(3, 4), Triangulo(5, 4))
                                                for (figura in figuras) {
                                                    if (figura is Rectangulo) {
                                                        println("Ancho: ${figura.ancho}")
                                                    }
                                                }
                                                val segunda = figuras[1] as? Rectangulo
                                                println(segunda?.ancho ?: "No es un rectángulo")
                                                println(figuras[0] !is Triangulo)
                                            }
                                            """.trimIndent(),
                                        ),
                                    note = richText(regular("Muestra "), inlineCode("Ancho: 3"), regular(", "), inlineCode("No es un rectángulo"), regular(" y "), inlineCode("true"), regular(". Solo el primer elemento pasa el "), inlineCode("is"), regular("; el segundo es un triángulo.")),
                                ),
                                LessonCodeExample(
                                    title = "La variable limita lo que se ve",
                                    lines =
                                        kotlinCode(
                                            """
                                            abstract class Figura(val nombre: String) {
                                                abstract fun area(): Int
                                            }

                                            class Rectangulo(val ancho: Int, val alto: Int) : Figura("Rectángulo") {
                                                override fun area(): Int {
                                                    return ancho * alto
                                                }
                                            }

                                            fun main() {
                                                val figuras: List<Figura> = listOf(Rectangulo(3, 4))
                                                for (figura in figuras) {
                                                    println(figura.⟦ancho⟧)
                                                }
                                            }
                                            """.trimIndent(),
                                        ),
                                    note = richText(regular("IntelliJ IDEA lo marca como error: "), inlineCode("figura"), regular(" es de tipo "), inlineCode("Figura"), regular(", que no tiene "), inlineCode("ancho"), regular(". Hay que preguntar con "), inlineCode("is"), regular(" antes.")),
                                ),
                                LessonCodeExample(
                                    title = "De la base a la hija no se pasa solo",
                                    lines =
                                        kotlinCode(
                                            """
                                            abstract class Figura(val nombre: String) {
                                                abstract fun area(): Int
                                            }

                                            class Rectangulo(val ancho: Int, val alto: Int) : Figura("Rectángulo") {
                                                override fun area(): Int {
                                                    return ancho * alto
                                                }
                                            }

                                            fun main() {
                                                val figuras: List<Figura> = listOf(Rectangulo(3, 4))
                                                val rectangulo: Rectangulo = ⟦figuras[0]⟧
                                            }
                                            """.trimIndent(),
                                        ),
                                    note = richText(regular("IntelliJ IDEA lo marca como error: de hija a base se pasa solo, pero de base a hija no, porque la figura podría ser de otra clase.")),
                                ),
                            ),
                        ),
                ),
                LessonStep(
                    title = "Interfaces",
                    explanation =
                        listOf(
                            paragraph(regular("Una "), highlight("interfaz"), regular(" es un contrato: declara qué sabe hacer una clase, sin decir cómo. No tiene constructor ni guarda estado, pero puede declarar propiedades abstractas y métodos con cuerpo, que las clases heredan si no los cambian.")),
                            paragraph(regular("Una clase la implementa tras los dos puntos, como "), inlineCode(": Encendible"), regular(" en el ejemplo, y está obligada a dar lo que falte. Como con las clases, una variable o una lista puede tener como tipo la interfaz: vale cualquier objeto que cumpla el contrato.")),
                        ),
                    visual =
                        LessonVisualContent.CodeExamples(
                            listOf(
                                LessonCodeExample(
                                    title = "Aparatos que se encienden y se apagan",
                                    lines =
                                        kotlinCode(
                                            $$"""
                                            interface Encendible {
                                                val nombre: String

                                                fun encender()

                                                fun apagar() {
                                                    println("$nombre se apaga")
                                                }
                                            }

                                            class Lampara : Encendible {
                                                override val nombre = "Lámpara"

                                                override fun encender() {
                                                    println("Luz encendida")
                                                }
                                            }

                                            class Movil : Encendible {
                                                override val nombre = "Móvil"

                                                override fun encender() {
                                                    println("Pantalla de inicio")
                                                }

                                                override fun apagar() {
                                                    println("$nombre guarda los cambios y se apaga")
                                                }
                                            }

                                            fun main() {
                                                val aparatos: List<Encendible> = listOf(Lampara(), Movil())
                                                for (aparato in aparatos) {
                                                    aparato.encender()
                                                    aparato.apagar()
                                                }
                                            }
                                            """.trimIndent(),
                                        ),
                                    note = richText(regular("Muestra cuatro líneas. Las dos clases están obligadas a dar "), inlineCode("nombre"), regular(" y "), inlineCode("encender()"), regular("; "), inlineCode("Lampara"), regular(" usa el "), inlineCode("apagar()"), regular(" de la interfaz y "), inlineCode("Movil"), regular(" lo cambia.")),
                                ),
                                LessonCodeExample(
                                    title = "Una interfaz no guarda estado",
                                    lines =
                                        kotlinCode(
                                            """
                                            interface Encendible {
                                                val nombre: String = ⟦"Aparato"⟧
                                            }
                                            """.trimIndent(),
                                        ),
                                    note = richText(regular("IntelliJ IDEA lo marca como error: una interfaz no puede guardar el valor. Lo da cada clase que la implementa.")),
                                ),
                            ),
                        ),
                ),
                LessonStep(
                    title = "Una clase y varias interfaces",
                    links = listOf(LessonLink("El problema del diamante", "https://es.wikipedia.org/wiki/Problema_del_diamante")),
                    explanation =
                        listOf(
                            paragraph(regular("Una clase hereda de "), emphasis("una sola"), regular(" clase, pero implementa todas las interfaces que quiera. En el ejemplo, "), inlineCode("Pato"), regular(" hereda de "), inlineCode("Animal"), regular(" e implementa "), inlineCode("Nadador"), regular(" y "), inlineCode("Volador"), regular(". Kotlin no tiene herencia múltiple de clases.")),
                            paragraph(regular("La razón es el «problema del diamante»: si dos bases guardan su estado y heredan de una misma abuela, la hija tendría dos copias de un dato y no sabría cuál usar. Las interfaces no guardan estado, y ese problema casi desaparece.")),
                        ),
                    visual =
                        LessonVisualContent.CodeExamples(
                            listOf(
                                LessonCodeExample(
                                    title = "Un pato que nada y vuela",
                                    lines =
                                        kotlinCode(
                                            """
                                            open class Animal(val nombre: String)

                                            interface Nadador {
                                                fun nadar() {
                                                    println("Nadando")
                                                }
                                            }

                                            interface Volador {
                                                fun volar() {
                                                    println("Volando")
                                                }
                                            }

                                            class Pato(nombre: String) : Animal(nombre), Nadador, Volador

                                            fun main() {
                                                val pato = Pato("Donald")
                                                println(pato.nombre)
                                                pato.nadar()
                                                pato.volar()
                                            }
                                            """.trimIndent(),
                                        ),
                                    note = richText(regular("Muestra "), inlineCode("Donald"), regular(", "), inlineCode("Nadando"), regular(" y "), inlineCode("Volando"), regular(". Un solo animal, dos capacidades.")),
                                ),
                                LessonCodeExample(
                                    title = "Dos clases no caben",
                                    lines =
                                        kotlinCode(
                                            """
                                            open class Animal(val nombre: String)

                                            open class Mascota(val familia: String)

                                            class Pato(nombre: String, familia: String) : Animal(nombre), ⟦Mascota⟧(familia)
                                            """.trimIndent(),
                                        ),
                                    note = richText(regular("IntelliJ IDEA lo marca como error: aunque un pato pueda ser animal y mascota, en la lista de supertipos solo cabe una clase. Lo demás tienen que ser interfaces.")),
                                ),
                            ),
                        ),
                ),
                LessonStep(
                    title = "Cuando dos interfaces chocan",
                    links = listOf(LessonLink("Reglas de sobrescritura en Kotlin", "https://kotlinlang.org/docs/inheritance.html#overriding-rules")),
                    explanation =
                        listOf(
                            paragraph(regular("Queda un caso: dos interfaces con un método con cuerpo y el mismo nombre. La clase hereda dos versiones y el compilador no adivina cuál quieres, así que obliga a sobrescribirlo.")),
                            paragraph(regular("En el segundo ejemplo, dentro de "), inlineCode("moverse()"), regular(" se elige con "), inlineCode("super<Nadador>.moverse()"), regular(": el nombre de la interfaz entre "), inlineCode("<>"), regular(" indica de cuál de las dos se toma la versión. Se puede llamar a una, a las dos o a ninguna.")),
                            callout(
                                "Si chocan a menudo, hacen demasiado",
                                richText(regular("Dos contratos tuyos que se pisan con frecuencia suelen ser demasiado grandes. Separa cada uno en piezas más pequeñas o dales nombres que no coincidan.")),
                                LessonCalloutTone.Tip,
                            ),
                        ),
                    visual =
                        LessonVisualContent.CodeExamples(
                            listOf(
                                LessonCodeExample(
                                    title = "Dos versiones de moverse",
                                    lines =
                                        kotlinCode(
                                            """
                                            interface Nadador {
                                                fun moverse() {
                                                    println("Nadando")
                                                }
                                            }

                                            interface Volador {
                                                fun moverse() {
                                                    println("Volando")
                                                }
                                            }

                                            class ⟦Pato⟧ : Nadador, Volador
                                            """.trimIndent(),
                                        ),
                                    note = richText(regular("IntelliJ IDEA lo marca como error: "), inlineCode("Pato"), regular(" hereda "), inlineCode("moverse()"), regular(" de las dos interfaces y debe decidir qué hacer.")),
                                ),
                                LessonCodeExample(
                                    title = "Elegir con super<Interfaz>",
                                    lines =
                                        kotlinCode(
                                            """
                                            interface Nadador {
                                                fun moverse() {
                                                    println("Nadando")
                                                }
                                            }

                                            interface Volador {
                                                fun moverse() {
                                                    println("Volando")
                                                }
                                            }

                                            class Pato : Nadador, Volador {
                                                override fun moverse() {
                                                    super<Nadador>.moverse()
                                                    super<Volador>.moverse()
                                                }
                                            }

                                            fun main() {
                                                Pato().moverse()
                                            }
                                            """.trimIndent(),
                                        ),
                                    note = richText(regular("Muestra "), inlineCode("Nadando"), regular(" y "), inlineCode("Volando"), regular(". El pato se mueve usando la versión de cada interfaz, una detrás de otra.")),
                                ),
                            ),
                        ),
                ),
                LessonStep(
                    title = "¿Clase abstracta o interfaz?",
                    explanation =
                        listOf(
                            paragraph(regular("Las dos fijan lo que las hijas deben ofrecer, pero sirven para cosas distintas. La clase abstracta comparte "), emphasis("lo que son"), regular(": puede tener constructor y estado, pero se hereda de una sola. La interfaz comparte "), emphasis("lo que saben hacer"), regular(" y se combina con otras.")),
                            paragraph(regular("Para decidir: si las clases son variantes de lo mismo y comparten datos, una base. Si no tienen parentesco pero deben poder usarse del mismo modo, una interfaz. Con dudas, la interfaz compromete menos y es más fácil de cambiar.")),
                        ),
                    visual =
                        LessonVisualContent.CodeExamples(
                            listOf(
                                LessonCodeExample(
                                    title = "Variantes de lo mismo: una base",
                                    lines =
                                        kotlinCode(
                                            """
                                            abstract class Figura(val nombre: String) {
                                                abstract fun area(): Int
                                            }

                                            class Rectangulo(val ancho: Int, val alto: Int) : Figura("Rectángulo") {
                                                override fun area(): Int {
                                                    return ancho * alto
                                                }
                                            }

                                            class Triangulo(val base: Int, val alto: Int) : Figura("Triángulo") {
                                                override fun area(): Int {
                                                    return base * alto / 2
                                                }
                                            }
                                            """.trimIndent(),
                                        ),
                                    note = richText(regular("Todas son figuras y comparten el nombre: es estado común, así que van bajo una clase base.")),
                                ),
                                LessonCodeExample(
                                    title = "Cosas distintas con un contrato común",
                                    lines =
                                        kotlinCode(
                                            $$"""
                                            interface Dibujable {
                                                fun dibujar()
                                            }

                                            class Rectangulo(val ancho: Int, val alto: Int) : Dibujable {
                                                override fun dibujar() {
                                                    println("Rectángulo de ${ancho}x$alto")
                                                }
                                            }

                                            class Etiqueta(val texto: String) : Dibujable {
                                                override fun dibujar() {
                                                    println("Etiqueta: $texto")
                                                }
                                            }

                                            fun main() {
                                                val escena: List<Dibujable> = listOf(Rectangulo(3, 2), Etiqueta("Hola"))
                                                for (elemento in escena) {
                                                    elemento.dibujar()
                                                }
                                            }
                                            """.trimIndent(),
                                        ),
                                    note = richText(regular("Muestra "), inlineCode("Rectángulo de 3x2"), regular(" y "), inlineCode("Etiqueta: Hola"), regular(". Un rectángulo y una etiqueta no se parecen, pero los dos se pueden dibujar.")),
                                ),
                            ),
                        ),
                ),
                LessonStep(
                    title = "Jerarquías cerradas",
                    explanation =
                        listOf(
                            paragraph(regular("A veces las variantes están decididas de antemano: un pago es con tarjeta, en efectivo o por Bizum, y nada más. Una clase "), inlineCode("sealed"), regular(" solo admite hijas declaradas en su mismo paquete, así que el compilador conoce todas las posibilidades.")),
                            paragraph(regular("Con "), inlineCode("when"), regular(" se nota: si cubres todas las hijas no hace falta "), inlineCode("else"), regular(", y si mañana añades otra, cada "), inlineCode("when"), regular(" que la ignore deja de compilar. Un "), inlineCode("enum"), regular(" fija un conjunto de valores; "), inlineCode("sealed"), regular(", de tipos con datos distintos.")),
                        ),
                    visual =
                        LessonVisualContent.CodeExamples(
                            listOf(
                                LessonCodeExample(
                                    title = "Tres formas de pagar",
                                    lines =
                                        kotlinCode(
                                            $$"""
                                            sealed class Pago(val importe: Int)

                                            class Tarjeta(importe: Int, val numero: String) : Pago(importe)

                                            class Efectivo(importe: Int, val billete: Int) : Pago(importe)

                                            class Bizum(importe: Int, val telefono: String) : Pago(importe)

                                            fun describir(pago: Pago) = when (pago) {
                                                is Tarjeta -> "Tarjeta ${pago.numero}"
                                                is Efectivo -> "Billete ${pago.billete}"
                                                is Bizum -> "Bizum ${pago.telefono}"
                                            }

                                            fun main() {
                                                val pagos: List<Pago> = listOf(Tarjeta(30, "4242"), Efectivo(12, 20), Bizum(8, "600123456"))
                                                for (pago in pagos) {
                                                    println(describir(pago))
                                                }
                                            }
                                            """.trimIndent(),
                                        ),
                                    note = richText(regular("Muestra "), inlineCode("Tarjeta 4242"), regular(", "), inlineCode("Billete 20"), regular(" y "), inlineCode("Bizum 600123456"), regular(". El "), inlineCode("when"), regular(" no necesita "), inlineCode("else"), regular(", y en cada rama el smart cast da acceso a los datos de esa hija.")),
                                ),
                                LessonCodeExample(
                                    title = "Falta un caso",
                                    lines =
                                        kotlinCode(
                                            $$"""
                                            sealed class Pago(val importe: Int)

                                            class Tarjeta(importe: Int, val numero: String) : Pago(importe)

                                            class Efectivo(importe: Int, val billete: Int) : Pago(importe)

                                            class Bizum(importe: Int, val telefono: String) : Pago(importe)

                                            fun describir(pago: Pago) = ⟦when⟧ (pago) {
                                                is Tarjeta -> "Tarjeta ${pago.numero}"
                                                is Efectivo -> "Billete ${pago.billete}"
                                            }
                                            """.trimIndent(),
                                        ),
                                    note = richText(regular("IntelliJ IDEA lo marca como error: falta tratar "), inlineCode("Bizum"), regular(". Un "), inlineCode("else"), regular(" lo callaría, pero se perdería este aviso cuando aparezca otra forma de pago.")),
                                ),
                            ),
                        ),
                ),
                LessonStep(
                    title = "Herencia o composición",
                    explanation =
                        listOf(
                            paragraph(regular("Heredar es la relación más fuerte entre dos clases: la hija queda atada a todo lo que la base haga hoy y haga mañana. Úsala solo si la hija "), emphasis("es un"), regular(" tipo de la base y puede ocupar su lugar en cualquier sitio.")),
                            paragraph(regular("Un coche no es un motor: tiene uno. Un pingüino es un ave, pero no vuela: si "), inlineCode("Ave"), regular(" prometiera "), inlineCode("volar()"), regular(", el pingüino rompería esa promesa. El segundo ejemplo lo arregla dejando en la base lo que todas hacen y poniendo "), inlineCode("volar()"), regular(" en una interfaz para quien vuela.")),
                            callout(
                                "Heredar no es copiar",
                                richText(regular("Heredar solo para aprovechar un método ya escrito, sin que haya un «es un», suele acabar mal. Para reutilizar código basta con tener un objeto dentro y pedirle lo que sabe hacer, como en la lección anterior.")),
                                LessonCalloutTone.Warning,
                            ),
                        ),
                    visual =
                        LessonVisualContent.CodeExamples(
                            listOf(
                                LessonCodeExample(
                                    title = "Un coche que tiene un motor",
                                    lines =
                                        kotlinCode(
                                            $$"""
                                            class Motor {
                                                fun arrancar() {
                                                    println("Motor en marcha")
                                                }
                                            }

                                            class Coche(val matricula: String, private val motor: Motor) {
                                                fun arrancar() {
                                                    println("Coche $matricula")
                                                    motor.arrancar()
                                                }
                                            }

                                            fun main() {
                                                val coche = Coche("1234 ABC", Motor())
                                                coche.arrancar()
                                            }
                                            """.trimIndent(),
                                        ),
                                    note = richText(regular("Muestra "), inlineCode("Coche 1234 ABC"), regular(" y "), inlineCode("Motor en marcha"), regular(". El coche le pide al motor que arranque; no hereda de él.")),
                                ),
                                LessonCodeExample(
                                    title = "Solo vuela quien puede volar",
                                    lines =
                                        kotlinCode(
                                            $$"""
                                            open class Ave(val nombre: String)

                                            interface Volador {
                                                fun volar()
                                            }

                                            class Gorrion(nombre: String) : Ave(nombre), Volador {
                                                override fun volar() {
                                                    println("$nombre vuela")
                                                }
                                            }

                                            class Pinguino(nombre: String) : Ave(nombre)

                                            fun main() {
                                                val aves: List<Ave> = listOf(Gorrion("Pepe"), Pinguino("Pingu"))
                                                for (ave in aves) {
                                                    if (ave is Volador) {
                                                        ave.volar()
                                                    } else {
                                                        println("${ave.nombre} no vuela")
                                                    }
                                                }
                                            }
                                            """.trimIndent(),
                                        ),
                                    note = richText(regular("Muestra "), inlineCode("Pepe vuela"), regular(" y "), inlineCode("Pingu no vuela"), regular(". "), inlineCode("Ave"), regular(" no promete volar; "), inlineCode("Volador"), regular(" sí, y solo lo cumplen quienes vuelan.")),
                                ),
                            ),
                        ),
                ),
                LessonStep(
                    title = "Probar con interfaces",
                    explanation =
                        listOf(
                            paragraph(regular("Un pedido que avisa al cliente no debería enviar un mensaje de verdad cada vez que se prueba. En el ejemplo, "), inlineCode("Pedido"), regular(" recibe un "), inlineCode("Notificador"), regular(", así que la prueba le entrega uno falso que solo apunta lo que le piden.")),
                            paragraph(regular("Así la prueba comprueba lo importante: qué mensajes se habrían enviado y cuántos. Es la idea de «Recibir lo que se necesita» con una interfaz en medio: el programa usa el notificador real y las pruebas, el falso.")),
                        ),
                    visual =
                        LessonVisualContent.CodeExamples(
                            listOf(
                                LessonCodeExample(
                                    title = "src/main/kotlin/Pedido.kt",
                                    fileName = "Pedido.kt",
                                    lines =
                                        kotlinCode(
                                            $$"""
                                            interface Notificador {
                                                fun avisar(mensaje: String)
                                            }

                                            class NotificadorConsola : Notificador {
                                                override fun avisar(mensaje: String) {
                                                    println(mensaje)
                                                }
                                            }

                                            class Pedido(val cliente: String, private val notificador: Notificador) {
                                                private var enviado = false

                                                fun enviar() {
                                                    if (enviado) {
                                                        return
                                                    }
                                                    enviado = true
                                                    notificador.avisar("Pedido de $cliente enviado")
                                                }
                                            }
                                            """.trimIndent(),
                                        ),
                                    note = richText(regular("El pedido solo conoce el contrato: no sabe si el aviso va a la consola o a un correo. Avisa una sola vez, aunque se envíe dos.")),
                                ),
                                LessonCodeExample(
                                    title = "src/test/kotlin/PedidoTest.kt",
                                    fileName = "PedidoTest.kt",
                                    lines =
                                        kotlinCode(
                                            """
                                            import kotlin.test.Test
                                            import kotlin.test.assertEquals

                                            class NotificadorFalso : Notificador {
                                                val mensajes = mutableListOf<String>()

                                                override fun avisar(mensaje: String) {
                                                    mensajes.add(mensaje)
                                                }
                                            }

                                            class PedidoTest {
                                                @Test
                                                fun `enviar un pedido avisa al cliente`() {
                                                    val notificador = NotificadorFalso()
                                                    val pedido = Pedido("Ana", notificador)
                                                    pedido.enviar()
                                                    assertEquals(listOf("Pedido de Ana enviado"), notificador.mensajes)
                                                }

                                                @Test
                                                fun `enviar dos veces avisa una sola vez`() {
                                                    val notificador = NotificadorFalso()
                                                    val pedido = Pedido("Ana", notificador)
                                                    pedido.enviar()
                                                    pedido.enviar()
                                                    assertEquals(1, notificador.mensajes.size)
                                                }
                                            }
                                            """.trimIndent(),
                                        ),
                                    note = richText(regular("Las dos pruebas pasan. El notificador falso es una clase más del archivo de pruebas: implementa la interfaz y guarda los mensajes en una lista.")),
                                ),
                            ),
                        ),
                ),
                LessonStep(
                    title = "Ahora te toca: la nómina",
                    explanation =
                        listOf(
                            paragraph(regular("Este programa lee empleados de tres tipos y calcula lo que cobra cada uno y el total de la nómina. Todo está en un solo "), inlineCode("main"), regular(" con un código de tipo: añadir un cuarto tipo obliga a tocar varios "), inlineCode("if"), regular(", y el cálculo de cada empleado no se puede probar por separado.")),
                            paragraph(regular("Conviértelo en una jerarquía en la que "), highlight("cada tipo de empleado calcula su propio pago"), regular(". Además, las ventas altas de un comercial dan un bonus, y solo a él.")),
                        ),
                    visual =
                        LessonVisualContent.CodeExamples(
                            listOf(
                                LessonCodeExample(
                                    title = "Nómina",
                                    lines =
                                        kotlinCode(
                                            $$"""
                                                fun main() {
                                                    var total = 0
                                                    println("Tipo (1 asalariado, 2 por horas, 3 comercial, 0 terminar):")
                                                    var tipo = readln().toInt()
                                                    while (tipo != 0) {
                                                        if (tipo < 1 || tipo > 3) {
                                                            println("Tipo desconocido: $tipo")
                                                        } else {
                                                            println("Nombre:")
                                                            val nombre = readln()
                                                            var pago = 0
                                                            var bonus = 0
                                                            if (tipo == 1) {
                                                                println("Sueldo:")
                                                                pago = readln().toInt()
                                                            } else if (tipo == 2) {
                                                                println("Horas:")
                                                                val horas = readln().toInt()
                                                                println("Precio por hora:")
                                                                val precio = readln().toInt()
                                                                pago = horas * precio
                                                            } else {
                                                                println("Sueldo base:")
                                                                val base = readln().toInt()
                                                                println("Ventas:")
                                                                val ventas = readln().toInt()
                                                                pago = base + ventas * 10 / 100
                                                                if (ventas > 1000) {
                                                                    bonus = 100
                                                                }
                                                            }
                                                            println("$nombre cobra $pago")
                                                            if (bonus > 0) {
                                                                println("$nombre recibe un bonus de $bonus")
                                                            }
                                                            total += pago + bonus
                                                        }
                                                        println("Tipo (1 asalariado, 2 por horas, 3 comercial, 0 terminar):")
                                                        tipo = readln().toInt()
                                                    }
                                                    println("Total: $total")
                                                }
                                            """.trimIndent(),
                                        ),
                                    note = richText(regular("Con una asalariada de 1500, un comercial de 1000 y 2000 en ventas, y un empleado por horas de 20 horas a 12, el total es "), inlineCode("3040"), regular(": el comercial suma 1200 de pago y 100 de bonus.")),
                                    isCopyable = true,
                                ),
                            ),
                        ),
                    links =
                        listOf(
                            LessonLink("Herencia y sobrescritura", "https://kotlinlang.org/docs/inheritance.html"),
                            LessonLink("Interfaces", "https://kotlinlang.org/docs/interfaces.html"),
                            LessonLink("Clases e interfaces selladas", "https://kotlinlang.org/docs/sealed-classes.html"),
                            LessonLink("Comprobaciones de tipo y conversiones", "https://kotlinlang.org/docs/typecasts.html"),
                            LessonLink("Modificadores de visibilidad", "https://kotlinlang.org/docs/visibility-modifiers.html"),
                        ),
                    practice =
                        LessonPractice(
                            id = "oop-inheritance-interfaces-payroll",
                            objective = richText(highlight("Diseñar una jerarquía de clases que se pueda ampliar sin tocar el código que la usa.")),
                            statement = richText(regular("Reorganiza el programa en clases para que "), inlineCode("main"), regular(" deje de depender del código de tipo, y comprueba la nómina con pruebas.")),
                            requirements =
                                listOf(
                                    richText(regular("Cada empleado tiene un "), inlineCode("nombre"), regular(" y calcula lo que cobra con "), inlineCode("pago(): Int"), regular(". Al mostrarlo con "), inlineCode("println"), regular(" sale "), inlineCode("Ana cobra 1500"), regular(".")),
                                    richText(regular("Hay tres tipos de empleado, "), inlineCode("Asalariado"), regular(", "), inlineCode("PorHoras"), regular(" y "), inlineCode("Comercial"), regular(", y cada uno cobra como en el programa original.")),
                                    richText(regular("Un comercial con más de 1000 en ventas tiene un bonus de 100, que se consulta con "), inlineCode("bonus(): Int"), regular(". Los otros tipos de empleado no tienen bonus.")),
                                    richText(inlineCode("Nomina"), regular(": "), inlineCode("agregar(empleado: Empleado)"), regular(" y "), inlineCode("total(): Int"), regular(", que suma los pagos y los bonus.")),
                                    richText(inlineCode("main"), regular(" lee igual que antes. Si el tipo no existe avisa con "), inlineCode("Tipo desconocido: 7"), regular(", y si un empleado tiene bonus avisa con "), inlineCode("Luis recibe un bonus de 100"), regular(".")),
                                    richText(regular("Cada tipo va en su archivo. Crea "), inlineCode("NominaTest"), regular(" y decide qué casos necesitas para fiarte de la nómina.")),
                                ),
                            expectedOutput =
                                listOf(
                                    consoleRun(
                                        consoleOutput("Tipo (1 asalariado, 2 por horas, 3 comercial, 0 terminar):"),
                                        consoleInput("1"),
                                        consoleOutput("Nombre:"),
                                        consoleInput("Ana"),
                                        consoleOutput("Sueldo:"),
                                        consoleInput("1500"),
                                        consoleOutput("Ana cobra 1500"),
                                        consoleOutput("Tipo (1 asalariado, 2 por horas, 3 comercial, 0 terminar):"),
                                        consoleInput("3"),
                                        consoleOutput("Nombre:"),
                                        consoleInput("Luis"),
                                        consoleOutput("Sueldo base:"),
                                        consoleInput("1000"),
                                        consoleOutput("Ventas:"),
                                        consoleInput("2000"),
                                        consoleOutput("Luis cobra 1200"),
                                        consoleOutput("Luis recibe un bonus de 100"),
                                        consoleOutput("Tipo (1 asalariado, 2 por horas, 3 comercial, 0 terminar):"),
                                        consoleInput("2"),
                                        consoleOutput("Nombre:"),
                                        consoleInput("Eva"),
                                        consoleOutput("Horas:"),
                                        consoleInput("20"),
                                        consoleOutput("Precio por hora:"),
                                        consoleInput("12"),
                                        consoleOutput("Eva cobra 240"),
                                        consoleOutput("Tipo (1 asalariado, 2 por horas, 3 comercial, 0 terminar):"),
                                        consoleInput("7"),
                                        consoleOutput("Tipo desconocido: 7"),
                                        consoleOutput("Tipo (1 asalariado, 2 por horas, 3 comercial, 0 terminar):"),
                                        consoleInput("0"),
                                        consoleOutput("Total: 3040"),
                                    ),
                                    consoleRun(
                                        consoleOutput("Tipo (1 asalariado, 2 por horas, 3 comercial, 0 terminar):"),
                                        consoleInput("3"),
                                        consoleOutput("Nombre:"),
                                        consoleInput("Marta"),
                                        consoleOutput("Sueldo base:"),
                                        consoleInput("900"),
                                        consoleOutput("Ventas:"),
                                        consoleInput("500"),
                                        consoleOutput("Marta cobra 950"),
                                        consoleOutput("Tipo (1 asalariado, 2 por horas, 3 comercial, 0 terminar):"),
                                        consoleInput("0"),
                                        consoleOutput("Total: 950"),
                                    ),
                                ),
                            hints =
                                listOf(
                                    richText(regular("Piensa qué comparten los tres empleados y qué cambia en cada uno. Lo común va en la base y lo que cambia, en un método que cada hija implementa. Repasa «Clases abstractas».")),
                                    richText(regular("El total no necesita saber si un empleado es asalariado, por horas o comercial: le basta con pedirle el pago. Repasa «Polimorfismo».")),
                                    richText(regular("El bonus solo existe en algunos empleados. Piensa qué construcción añade una capacidad a una clase sin tocar a las demás y cómo preguntar si un empleado la tiene: «Interfaces», «Preguntar el tipo» y «Una clase y varias interfaces».")),
                                    richText(regular("Para mostrar un empleado con "), inlineCode("println"), regular(", repasa «Mostrar un objeto» y «Sobrescribir comportamiento». En las pruebas, cada una crea sus propios empleados: no hace falta leer nada de la consola.")),
                                ),
                            extension = richText(regular("Añade un "), inlineCode("Becario"), regular(" que no cobra. ¿Cuántos archivos has tenido que tocar? Prueba después a hacer "), inlineCode("Empleado"), regular(" una clase "), inlineCode("sealed"), regular(" y a escribir un "), inlineCode("when"), regular(" sin "), inlineCode("else"), regular(" que dé el nombre del tipo de cada empleado.")),
                            confirmation = richText(regular("Confirma que las dos ejecuciones dan la salida esperada y que "), inlineCode("./gradlew test"), regular(" termina con "), inlineCode("BUILD SUCCESSFUL"), regular(".")),
                        ),
                ),
            ),
    )
