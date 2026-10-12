package dev.kuromiichi.init.data.lessons.contents

import dev.kuromiichi.init.data.lessons.LessonCalloutTone
import dev.kuromiichi.init.data.lessons.LessonCodeExample
import dev.kuromiichi.init.data.lessons.LessonConsoleKind
import dev.kuromiichi.init.data.lessons.LessonContent
import dev.kuromiichi.init.data.lessons.LessonLink
import dev.kuromiichi.init.data.lessons.LessonPractice
import dev.kuromiichi.init.data.lessons.LessonStep
import dev.kuromiichi.init.data.lessons.LessonTraceTable
import dev.kuromiichi.init.data.lessons.LessonVisualContent
import dev.kuromiichi.init.data.lessons.callout
import dev.kuromiichi.init.data.lessons.consoleError
import dev.kuromiichi.init.data.lessons.consoleInput
import dev.kuromiichi.init.data.lessons.consoleOutput
import dev.kuromiichi.init.data.lessons.consoleRun
import dev.kuromiichi.init.data.lessons.emphasis
import dev.kuromiichi.init.data.lessons.explanationCode
import dev.kuromiichi.init.data.lessons.highlight
import dev.kuromiichi.init.data.lessons.inlineCode
import dev.kuromiichi.init.data.lessons.kotlinCode
import dev.kuromiichi.init.data.lessons.paragraph
import dev.kuromiichi.init.data.lessons.regular
import dev.kuromiichi.init.data.lessons.richText

val oopClassesObjectsLessonContent =
    LessonContent(
        lessonId = "oop-classes-objects",
        steps =
            listOf(
                LessonStep(
                    title = "Datos que van juntos",
                    explanation =
                        listOf(
                            paragraph(regular("Hasta ahora has guardado el nombre de un alumno en una variable, su nota en otra y, para varios alumnos, listas paralelas. Funciona, pero nada garantiza que la posición 2 de una lista corresponda a la misma persona que la posición 2 de la otra.")),
                            paragraph(regular("La programación orientada a objetos agrupa los datos de una misma cosa y las acciones que se hacen con ellos. Una "), highlight("clase"), regular(" es la plantilla que describe cómo es esa cosa; un "), highlight("objeto"), regular(" es cada ejemplar concreto creado a partir de ella.")),
                            paragraph(regular("«Alumno» es una clase; Ana con un 7 y Luis con un 4 son dos objetos de esa clase. Los dos tienen la misma forma, pero cada uno guarda sus propios valores.")),
                        ),
                    visual = LessonVisualContent.Flow(listOf("Clase: la plantilla", "Se crea un objeto", "Cada objeto guarda su estado", "Los objetos tienen métodos")),
                ),
                LessonStep(
                    title = "Tu primera clase",
                    explanation =
                        listOf(
                            paragraph(regular("Se declara con "), inlineCode("class"), regular(" y un nombre que empieza por mayúscula. Lo que va entre paréntesis es el "), emphasis("constructor"), regular(": los datos que hay que dar para crear un objeto. Con "), inlineCode("val"), regular(" o "), inlineCode("var"), regular(" cada dato se convierte en una "), emphasis("propiedad"), regular(" del objeto.")),
                            paragraph(regular("Se crea un objeto escribiendo el nombre de la clase como si fuera una función, como "), inlineCode("Alumno(\"Ana\", 7)"), regular(" en el ejemplo. No hace falta nada más. Las propiedades se leen y se cambian con un punto, y una propiedad "), inlineCode("val"), regular(" no se puede reasignar.")),
                        ),
                    visual =
                        LessonVisualContent.CodeExamples(
                            listOf(
                                LessonCodeExample(
                                    title = "Crear un objeto y usar sus propiedades",
                                    lines =
                                        kotlinCode(
                                            """
                                            class Alumno(val nombre: String, var nota: Int)

                                            fun main() {
                                                val alumno = Alumno("Ana", 7)
                                                println(alumno.nombre)
                                                println(alumno.nota)
                                                alumno.nota = 8
                                                println(alumno.nota)
                                            }
                                            """.trimIndent(),
                                        ),
                                    note = richText(regular("Muestra "), inlineCode("Ana"), regular(", "), inlineCode("7"), regular(" y "), inlineCode("8"), regular(". La clase se escribe una vez; "), inlineCode("main"), regular(" la usa.")),
                                ),
                                LessonCodeExample(
                                    title = "Un val no se reasigna",
                                    lines =
                                        kotlinCode(
                                            """
                                            class Alumno(val nombre: String, var nota: Int)

                                            fun main() {
                                                val alumno = Alumno("Ana", 7)
                                                alumno.⟦nombre⟧ = "Luis"
                                            }
                                            """.trimIndent(),
                                        ),
                                    note = richText(regular("IntelliJ IDEA lo marca como error: "), inlineCode("nombre"), regular(" es "), inlineCode("val"), regular(" y no cambia después de crear el objeto.")),
                                ),
                            ),
                        ),
                ),
                LessonStep(
                    title = "Cada objeto, su estado",
                    explanation =
                        listOf(
                            paragraph(regular("Cada llamada al constructor crea un objeto nuevo e independiente: cambiar la nota de uno no toca la del otro. El conjunto de valores que guarda un objeto en un momento dado se llama su "), highlight("estado"), regular(".")),
                            paragraph(regular("Pero asignar un objeto a otra variable no crea otro objeto: las dos variables apuntan al mismo. Es la misma idea de «Copiar no es duplicar» con los arrays, y aquí también cambia lo que ven las dos.")),
                        ),
                    visual =
                        LessonVisualContent.Trace(
                            example =
                                LessonCodeExample(
                                    title = "Dos objetos y una variable más",
                                    lines =
                                        kotlinCode(
                                            """
                                            class Alumno(val nombre: String, var nota: Int)

                                            fun main() {
                                                val a = Alumno("Ana", 7)
                                                val b = Alumno("Luis", 4)
                                                val c = a
                                                c.nota = 9
                                                println(a.nota)
                                                println(b.nota)
                                            }
                                            """.trimIndent(),
                                        ),
                                    note = richText(regular("Muestra "), inlineCode("9"), regular(" y "), inlineCode("4"), regular(". Con "), inlineCode("c.nota = 9"), regular(" cambia el objeto de Ana, porque "), inlineCode("c"), regular(" y "), inlineCode("a"), regular(" son el mismo.")),
                                ),
                            table =
                                LessonTraceTable(
                                    title = "Qué hay en cada variable",
                                    columns = listOf("Línea", "a", "b", "c", "Muestra"),
                                    rows =
                                        listOf(
                                            listOf("4", "Ana, 7", "–", "–", "–"),
                                            listOf("5", "Ana, 7", "Luis, 4", "–", "–"),
                                            listOf("6", "Ana, 7", "Luis, 4", "el mismo que a", "–"),
                                            listOf("7", "Ana, 9", "Luis, 4", "el mismo que a", "–"),
                                            listOf("8", "Ana, 9", "Luis, 4", "el mismo que a", "9"),
                                            listOf("9", "Ana, 9", "Luis, 4", "el mismo que a", "4"),
                                        ),
                                ),
                        ),
                ),
                LessonStep(
                    title = "Métodos y this",
                    explanation =
                        listOf(
                            paragraph(regular("Una clase también puede incluir funciones: los "), highlight("métodos"), regular(". Se escriben dentro de las llaves de la clase y se llaman sobre un objeto, como "), inlineCode("luis.subirNota(2)"), regular(" en el ejemplo. Dentro del método se usan las propiedades de ese objeto por su nombre.")),
                            paragraph(inlineCode("this"), regular(" es el propio objeto sobre el que se llama al método. Casi nunca hace falta escribirlo; se necesita cuando un parámetro tiene el mismo nombre que una propiedad. En el ejemplo, "), inlineCode("this.valor"), regular(" es la propiedad y "), inlineCode("valor"), regular(" a secas, el parámetro.")),
                        ),
                    visual =
                        LessonVisualContent.CodeExamples(
                            listOf(
                                LessonCodeExample(
                                    title = "Un método que consulta y otro que modifica",
                                    lines =
                                        kotlinCode(
                                            """
                                            class Alumno(val nombre: String, var nota: Int) {
                                                fun aprobado(): Boolean {
                                                    return nota >= 5
                                                }

                                                fun subirNota(puntos: Int) {
                                                    nota += puntos
                                                    if (nota > 10) {
                                                        nota = 10
                                                    }
                                                }
                                            }

                                            fun main() {
                                                val luis = Alumno("Luis", 4)
                                                println(luis.aprobado())
                                                luis.subirNota(2)
                                                println(luis.nota)
                                                println(luis.aprobado())
                                            }
                                            """.trimIndent(),
                                        ),
                                    note = richText(regular("Muestra "), inlineCode("false"), regular(", "), inlineCode("6"), regular(" y "), inlineCode("true"), regular(". Cada método trabaja con la nota del objeto al que se llama.")),
                                ),
                                LessonCodeExample(
                                    title = "Cuándo hace falta this",
                                    lines =
                                        kotlinCode(
                                            """
                                            class Contador(var valor: Int) {
                                                fun reiniciar(valor: Int) {
                                                    this.valor = valor
                                                }
                                            }

                                            fun main() {
                                                val contador = Contador(5)
                                                contador.reiniciar(0)
                                                println(contador.valor)
                                            }
                                            """.trimIndent(),
                                        ),
                                    note = richText(regular("Muestra "), inlineCode("0"), regular(". Sin "), inlineCode("this"), regular(", la línea 3 sería "), inlineCode("valor = valor"), regular(": intentaría reasignar el parámetro, que es de solo lectura, y no compilaría.")),
                                ),
                            ),
                        ),
                ),
                LessonStep(
                    title = "Construir con garantías",
                    explanation =
                        listOf(
                            paragraph(regular("Un parámetro del constructor puede tener un valor por defecto, y al crear el objeto se pueden nombrar los argumentos: "), inlineCode("Alumno(nombre = \"Luis\", nota = 6)"), regular(". Así se evitan varios constructores distintos.")),
                            paragraph(regular("El bloque "), inlineCode("init"), regular(" se ejecuta al crear cada objeto. Es el sitio para validar con "), inlineCode("require"), regular(": si los datos no son válidos, el objeto no llega a existir. La excepción sale en la línea que intentó crearlo.")),
                            explanationCode(
                                "Main.kt",
                                richText(regular("Las líneas 8 y 9 usan el valor por defecto y el argumento con nombre; la 12 crea un alumno sin nombre.")),
                                "class Alumno(val nombre: String, var nota: Int = 0) {",
                                "    init {",
                                "        require(nombre != \"\") { \"El nombre no puede estar vacío\" }",
                                "    }",
                                "}",
                                "",
                                "fun main() {",
                                "    val ana = Alumno(\"Ana\")",
                                "    val luis = Alumno(nombre = \"Luis\", nota = 6)",
                                "    println(\"\${ana.nombre}: \${ana.nota}\")",
                                "    println(\"\${luis.nombre}: \${luis.nota}\")",
                                "    val sinNombre = Alumno(\"\")",
                                "}",
                            ),
                            callout(
                                "Por qué las llaves en la plantilla",
                                richText(inlineCode("\"\$nombre\""), regular(" solo sirve para un nombre suelto. Con una propiedad, "), inlineCode("\"\$ana.nombre\""), regular(" insertaría "), inlineCode("ana"), regular(" y dejaría "), inlineCode(".nombre"), regular(" como texto. Las llaves, "), inlineCode("\"\${ana.nombre}\""), regular(", delimitan la expresión entera.")),
                                LessonCalloutTone.Tip,
                            ),
                        ),
                    visual =
                        LessonVisualContent.Console(
                            kind = LessonConsoleKind.IntelliJRun,
                            runs =
                                listOf(
                                    consoleRun(
                                        consoleOutput("Ana: 0"),
                                        consoleOutput("Luis: 6"),
                                        consoleError("Exception in thread \"main\" java.lang.IllegalArgumentException: El nombre no puede estar vacío"),
                                        consoleError("    at Alumno.<init>(Main.kt:3)"),
                                        consoleError("    at Alumno.<init>(Main.kt:1)"),
                                        consoleError("    at MainKt.main(Main.kt:12)"),
                                        consoleError("    at MainKt.main(Main.kt)"),
                                        consoleOutput(""),
                                        consoleOutput("Process finished with exit code 1"),
                                    ),
                                ),
                        ),
                ),
                LessonStep(
                    title = "Proteger el estado",
                    explanation =
                        listOf(
                            paragraph(regular("Por defecto, todo lo de una clase es público: cualquiera puede leerlo y cambiarlo. A menudo no conviene. Con "), inlineCode("private"), regular(" un miembro solo se usa desde dentro de la clase. Es lo que se llama "), highlight("encapsulación"), regular(".")),
                            paragraph(regular("Lo más habitual es dejar una propiedad legible desde fuera pero modificable solo por la clase, con "), inlineCode("private set"), regular(" justo debajo de la propiedad. Desde fuera se cambia llamando a un método, que puede comprobar antes que el cambio tenga sentido.")),
                            callout(
                                "Por qué importa",
                                richText(regular("Si cualquiera puede asignar a "), inlineCode("valor"), regular(", como en el ejemplo, la clase no puede garantizar nada sobre él. Si solo lo cambian sus métodos, basta revisar esos métodos cuando algo falla.")),
                                LessonCalloutTone.Tip,
                            ),
                        ),
                    visual =
                        LessonVisualContent.CodeExamples(
                            listOf(
                                LessonCodeExample(
                                    title = "Se lee desde fuera, se cambia desde dentro",
                                    lines =
                                        kotlinCode(
                                            """
                                            class Contador {
                                                var valor = 0
                                                    private set

                                                fun incrementar() {
                                                    valor++
                                                }
                                            }

                                            fun main() {
                                                val contador = Contador()
                                                contador.incrementar()
                                                contador.incrementar()
                                                println(contador.valor)
                                            }
                                            """.trimIndent(),
                                        ),
                                    note = richText(regular("Muestra "), inlineCode("2"), regular(". Una clase sin datos en el constructor se crea con "), inlineCode("Contador()"), regular(".")),
                                ),
                                LessonCodeExample(
                                    title = "Desde fuera no se puede asignar",
                                    lines =
                                        kotlinCode(
                                            """
                                            class Contador {
                                                var valor = 0
                                                    private set
                                            }

                                            fun main() {
                                                val contador = Contador()
                                                contador.⟦valor⟧ = 100
                                            }
                                            """.trimIndent(),
                                        ),
                                    note = richText(regular("IntelliJ IDEA lo marca como error: el "), inlineCode("set"), regular(" de "), inlineCode("valor"), regular(" es privado en "), inlineCode("Contador"), regular(".")),
                                ),
                            ),
                        ),
                ),
                LessonStep(
                    title = "Getters y setters propios",
                    explanation =
                        listOf(
                            paragraph(regular("Cada propiedad tiene un "), emphasis("getter"), regular(" que la lee y, si es "), inlineCode("var"), regular(", un "), emphasis("setter"), regular(" que la cambia. Kotlin los crea solos, pero puedes escribir los tuyos justo debajo de la propiedad.")),
                            paragraph(regular("En un setter, "), inlineCode("field"), regular(" es el valor guardado. En el ejemplo, hay que escribir "), inlineCode("field = nueva"), regular(", porque escribir "), inlineCode("nota = nueva"), regular(" volvería a llamar al setter sin fin. Una propiedad con "), inlineCode("get()"), regular(" y sin "), inlineCode("field"), regular(" no guarda nada: se calcula cada vez.")),
                            callout(
                                "El valor inicial no pasa por el setter",
                                richText(regular("La asignación "), inlineCode("= 0"), regular(" de la propiedad escribe directamente en "), inlineCode("field"), regular(". Solo se valida lo que se asigna después.")),
                                LessonCalloutTone.Warning,
                            ),
                        ),
                    visual =
                        LessonVisualContent.Trace(
                            example =
                                LessonCodeExample(
                                    title = "Una nota que se valida y un aprobado que se calcula",
                                    lines =
                                        kotlinCode(
                                            """
                                            class Alumno(val nombre: String) {
                                                var nota: Int = 0
                                                    set(nueva) {
                                                        require(nueva >= 0 && nueva <= 10) {
                                                            "La nota debe estar entre 0 y 10"
                                                        }
                                                        field = nueva
                                                    }

                                                val aprobado: Boolean
                                                    get() = nota >= 5
                                            }

                                            fun main() {
                                                val ana = Alumno("Ana")
                                                println(ana.aprobado)
                                                ana.nota = 7
                                                println(ana.aprobado)
                                                ana.nota = 3
                                                println(ana.aprobado)
                                            }
                                            """.trimIndent(),
                                        ),
                                    note = richText(regular("Muestra "), inlineCode("false"), regular(", "), inlineCode("true"), regular(" y "), inlineCode("false"), regular(". Con "), inlineCode("ana.nota = 11"), regular(" el setter lanzaría una "), inlineCode("IllegalArgumentException"), regular(" y la nota seguiría igual.")),
                                ),
                            table =
                                LessonTraceTable(
                                    title = "Después de cada línea de main",
                                    columns = listOf("Línea", "nota", "aprobado", "Muestra"),
                                    rows =
                                        listOf(
                                            listOf("15", "0", "false", "–"),
                                            listOf("16", "0", "false", "false"),
                                            listOf("17", "7", "true", "–"),
                                            listOf("18", "7", "true", "true"),
                                            listOf("19", "3", "false", "–"),
                                            listOf("20", "3", "false", "false"),
                                        ),
                                ),
                        ),
                ),
                LessonStep(
                    title = "Mostrar un objeto",
                    explanation =
                        listOf(
                            paragraph(regular("Si muestras un objeto con "), inlineCode("println"), regular(", Kotlin usa su método "), inlineCode("toString()"), regular(". El que trae cualquier clase solo da su nombre, una arroba y un número que cambia de una ejecución a otra: no sirve para nada.")),
                            paragraph(regular("Para decidir cómo se muestra, escribe tu propio "), inlineCode("toString()"), regular(" precedido de "), inlineCode("override"), regular(", que significa «sustituyo la versión que ya existe». Devuelve un "), inlineCode("String"), regular(", y también se usa dentro de las plantillas como "), inlineCode("\"\$alumno\""), regular(".")),
                        ),
                    visual =
                        LessonVisualContent.CodeExamples(
                            listOf(
                                LessonCodeExample(
                                    title = "Sin toString propio",
                                    lines =
                                        kotlinCode(
                                            """
                                            class Alumno(val nombre: String, var nota: Int)

                                            fun main() {
                                                println(Alumno("Ana", 7))
                                            }
                                            """.trimIndent(),
                                        ),
                                    note = richText(regular("Muestra algo como "), inlineCode("Alumno@6d06d69c"), regular(". El número cambia en cada ejecución.")),
                                ),
                                LessonCodeExample(
                                    title = "Con toString propio",
                                    lines =
                                        kotlinCode(
                                            $$"""
                                                class Alumno(val nombre: String, var nota: Int) {
                                                    override fun toString(): String {
                                                        return "$nombre ($nota)"
                                                    }
                                                }

                                                fun main() {
                                                    val alumno = Alumno("Ana", 7)
                                                    println(alumno)
                                                    println("Alumno: $alumno")
                                                }
                                            """.trimIndent(),
                                        ),
                                    note = richText(regular("Muestra "), inlineCode("Ana (7)"), regular(" y "), inlineCode("Alumno: Ana (7)"), regular(".")),
                                ),
                            ),
                        ),
                ),
                LessonStep(
                    title = "Clases de datos",
                    explanation =
                        listOf(
                            paragraph(regular("Cuando una clase solo sirve para guardar datos, se declara con "), inlineCode("data class"), regular(". Kotlin le añade un "), inlineCode("toString()"), regular(" legible y hace que dos objetos con las mismas propiedades del constructor se consideren iguales para "), inlineCode("=="), regular(".")),
                            paragraph(regular("Por eso funcionan bien en conjuntos: añadir un alumno con los mismos datos que otro ya guardado no lo duplica, y "), inlineCode("add"), regular(" devuelve "), inlineCode("false"), regular(", como viste en la lección anterior.")),
                        ),
                    visual =
                        LessonVisualContent.CodeExamples(
                            listOf(
                                LessonCodeExample(
                                    title = "Una clase de datos en un conjunto",
                                    lines =
                                        kotlinCode(
                                            """
                                            data class Alumno(val nombre: String, val nota: Int)

                                            fun main() {
                                                val a = Alumno("Ana", 7)
                                                val b = Alumno("Ana", 7)
                                                println(a)
                                                println(a == b)
                                                val matriculados = mutableSetOf(a)
                                                println(matriculados.add(b))
                                                println(matriculados.size)
                                            }
                                            """.trimIndent(),
                                        ),
                                    note = richText(regular("Muestra "), inlineCode("Alumno(nombre=Ana, nota=7)"), regular(", "), inlineCode("true"), regular(", "), inlineCode("false"), regular(" y "), inlineCode("1"), regular(". "), inlineCode("a"), regular(" y "), inlineCode("b"), regular(" son dos objetos, pero con los mismos datos.")),
                                ),
                            ),
                        ),
                ),
                LessonStep(
                    title = "Igualdad e identidad",
                    explanation =
                        listOf(
                            paragraph(regular("Con dos variables se pueden hacer dos preguntas distintas. ¿Tienen los mismos datos? Es la "), highlight("igualdad"), regular(" y se pregunta con "), inlineCode("=="), regular(". ¿Son el mismo objeto? Es la "), highlight("identidad"), regular(" y se pregunta con "), inlineCode("==="), regular(".")),
                            paragraph(regular("Dos fotocopias de un mismo folleto son iguales, pero no son el mismo ejemplar. Si dos variables son el mismo objeto, siempre son iguales; al revés no.")),
                            callout(
                                "Sin data class",
                                richText(regular("En una clase normal, "), inlineCode("=="), regular(" también compara identidad: dos alumnos creados por separado nunca son iguales, aunque tengan los mismos datos. Por eso las clases de datos son tan útiles.")),
                                LessonCalloutTone.Information,
                            ),
                        ),
                    visual =
                        LessonVisualContent.Trace(
                            example =
                                LessonCodeExample(
                                    title = "Dos objetos iguales y una variable más",
                                    lines =
                                        kotlinCode(
                                            """
                                            data class Alumno(val nombre: String, val nota: Int)

                                            fun main() {
                                                val a = Alumno("Ana", 7)
                                                val b = Alumno("Ana", 7)
                                                val c = a
                                                println(a == b)
                                                println(a === b)
                                                println(a == c)
                                                println(a === c)
                                            }
                                            """.trimIndent(),
                                        ),
                                    note = richText(regular("Muestra "), inlineCode("true"), regular(", "), inlineCode("false"), regular(", "), inlineCode("true"), regular(" y "), inlineCode("true"), regular(". Solo "), inlineCode("a === b"), regular(" da "), inlineCode("false"), regular(".")),
                                ),
                            table =
                                LessonTraceTable(
                                    title = "Qué responde cada pregunta",
                                    columns = listOf("Línea", "Comparación", "Resultado", "Por qué"),
                                    rows =
                                        listOf(
                                            listOf("7", "a == b", "true", "Mismos datos"),
                                            listOf("8", "a === b", "false", "Objetos distintos"),
                                            listOf("9", "a == c", "true", "Mismos datos"),
                                            listOf("10", "a === c", "true", "El mismo objeto"),
                                        ),
                                ),
                        ),
                ),
                LessonStep(
                    title = "Copiar y desestructurar",
                    explanation =
                        listOf(
                            paragraph(regular("En una clase de datos conviene que las propiedades sean "), inlineCode("val"), regular(": el objeto no cambia después de crearse y nadie lo modifica por sorpresa. Para obtener una versión distinta, "), inlineCode("copy"), regular(" crea otro objeto, y solo hay que indicar lo que cambia.")),
                            paragraph(regular("Una clase de datos también se puede desestructurar, como las parejas que recorriste en un mapa: en el ejemplo, "), inlineCode("val (nombre, nota) = alumno"), regular(" saca las propiedades por orden de declaración.")),
                        ),
                    visual =
                        LessonVisualContent.CodeExamples(
                            listOf(
                                LessonCodeExample(
                                    title = "copy y desestructuración",
                                    lines =
                                        kotlinCode(
                                            $$"""
                                                data class Alumno(val nombre: String, val nota: Int)

                                                fun main() {
                                                    val ana = Alumno("Ana", 7)
                                                    val recuperada = ana.copy(nota = 8)
                                                    println(ana)
                                                    println(recuperada)
                                                    val (nombre, nota) = recuperada
                                                    println("$nombre sacó un $nota")
                                                }
                                            """.trimIndent(),
                                        ),
                                    note = richText(regular("Muestra "), inlineCode("Alumno(nombre=Ana, nota=7)"), regular(", "), inlineCode("Alumno(nombre=Ana, nota=8)"), regular(" y "), inlineCode("Ana sacó un 8"), regular(". El original no cambia.")),
                                ),
                                LessonCodeExample(
                                    title = "Un val no se modifica",
                                    lines =
                                        kotlinCode(
                                            """
                                            data class Alumno(val nombre: String, val nota: Int)

                                            fun main() {
                                                val ana = Alumno("Ana", 7)
                                                ana.⟦nota⟧ = 8
                                            }
                                            """.trimIndent(),
                                        ),
                                    note = richText(regular("IntelliJ IDEA lo marca como error. Para otra nota, se crea otro objeto con "), inlineCode("copy"), regular(".")),
                                ),
                            ),
                        ),
                ),
                LessonStep(
                    title = "Lo que es de la clase",
                    explanation =
                        listOf(
                            paragraph(regular("Algunos datos pertenecen a la clase entera y no a cada objeto: una constante, o cuántos objetos se han creado. Se declaran dentro de un "), inlineCode("companion object"), regular(" y se usan con el nombre de la clase, como "), inlineCode("Alumno.NOTA_MINIMA"), regular(" en el ejemplo.")),
                            paragraph(regular("Las constantes fijas se escriben con "), inlineCode("const val"), regular(" y en mayúsculas separadas por guiones bajos. El contador de ejemplo usa "), inlineCode("private set"), regular(" para que solo la clase pueda cambiarlo, y "), inlineCode("init"), regular(" lo incrementa en cada objeto creado.")),
                        ),
                    visual =
                        LessonVisualContent.CodeExamples(
                            listOf(
                                LessonCodeExample(
                                    title = "Constante y contador compartidos",
                                    lines =
                                        kotlinCode(
                                            """
                                            class Alumno(val nombre: String, val nota: Int) {
                                                companion object {
                                                    const val NOTA_MINIMA = 5
                                                    var creados = 0
                                                        private set
                                                }

                                                init {
                                                    creados++
                                                }

                                                fun aprobado(): Boolean {
                                                    return nota >= NOTA_MINIMA
                                                }
                                            }

                                            fun main() {
                                                val ana = Alumno("Ana", 7)
                                                val luis = Alumno("Luis", 4)
                                                println(ana.aprobado())
                                                println(luis.aprobado())
                                                println(Alumno.creados)
                                                println(Alumno.NOTA_MINIMA)
                                            }
                                            """.trimIndent(),
                                        ),
                                    note = richText(regular("Muestra "), inlineCode("true"), regular(", "), inlineCode("false"), regular(", "), inlineCode("2"), regular(" y "), inlineCode("5"), regular(". Dentro de la clase se usa "), inlineCode("NOTA_MINIMA"), regular(" a secas; desde fuera, con el nombre de la clase.")),
                                ),
                            ),
                        ),
                ),
                LessonStep(
                    title = "Enumeraciones",
                    explanation =
                        listOf(
                            paragraph(regular("Cuando un valor solo puede ser uno de unos pocos conocidos, como una prioridad o un día de la semana, se usa una "), inlineCode("enum class"), regular(". Cada valor es una constante de la clase y se escribe en mayúsculas, sin tildes ni eñes, como "), inlineCode("Prioridad.ALTA"), regular(" en el ejemplo.")),
                            paragraph(regular("Una enumeración puede llevar propiedades en el constructor, como cualquier clase. Con "), inlineCode("entries"), regular(" se recorren todos los valores, y un "), inlineCode("when"), regular(" sobre ella no necesita "), inlineCode("else"), regular(" si cubre todos: el compilador avisa si falta alguno.")),
                        ),
                    visual =
                        LessonVisualContent.CodeExamples(
                            listOf(
                                LessonCodeExample(
                                    title = "Una enumeración con propiedad",
                                    lines =
                                        kotlinCode(
                                            $$"""
                                                enum class Prioridad(val etiqueta: String) {
                                                    BAJA("Baja"),
                                                    MEDIA("Media"),
                                                    ALTA("Alta"),
                                                }

                                                fun urgencia(nivel: Prioridad): Int =
                                                    when (nivel) {
                                                        Prioridad.BAJA -> 1
                                                        Prioridad.MEDIA -> 2
                                                        Prioridad.ALTA -> 3
                                                    }

                                                fun main() {
                                                    val prioridad = Prioridad.ALTA
                                                    println(prioridad)
                                                    println(prioridad.etiqueta)
                                                    println(urgencia(prioridad))
                                                    for (p in Prioridad.entries) {
                                                        println("${p.name}: ${p.etiqueta}")
                                                    }
                                                }
                                            """.trimIndent(),
                                        ),
                                    note = richText(regular("Muestra "), inlineCode("ALTA"), regular(", "), inlineCode("Alta"), regular(", "), inlineCode("3"), regular(" y luego "), inlineCode("BAJA: Baja"), regular(", "), inlineCode("MEDIA: Media"), regular(" y "), inlineCode("ALTA: Alta"), regular(".")),
                                ),
                            ),
                        ),
                ),
                LessonStep(
                    title = "Una clase en su archivo y sus pruebas",
                    explanation =
                        listOf(
                            paragraph(regular("En un proyecto real cada clase vive en su propio archivo con el nombre de la clase, y las pruebas en otro dentro de "), inlineCode("src/test/kotlin"), regular(". Probar una clase es crear objetos, llamar a sus métodos y comprobar el estado que queda.")),
                            paragraph(regular("Prueba el caso normal, la frontera (un 5 aprueba y un 4 no) y el caso inválido: tras un "), inlineCode("assertFailsWith"), regular(", comprueba también que la nota anterior sigue intacta.")),
                        ),
                    visual =
                        LessonVisualContent.CodeExamples(
                            listOf(
                                LessonCodeExample(
                                    title = "src/main/kotlin/Alumno.kt",
                                    fileName = "Alumno.kt",
                                    lines =
                                        kotlinCode(
                                            """
                                            class Alumno(val nombre: String) {
                                                var nota: Int = 0
                                                    set(nueva) {
                                                        require(nueva >= 0 && nueva <= 10) {
                                                            "La nota debe estar entre 0 y 10"
                                                        }
                                                        field = nueva
                                                    }

                                                val aprobado: Boolean
                                                    get() = nota >= 5
                                            }
                                            """.trimIndent(),
                                        ),
                                    note = richText(regular("Un archivo, una clase, y el archivo se llama como la clase.")),
                                ),
                                LessonCodeExample(
                                    title = "src/test/kotlin/AlumnoTest.kt",
                                    fileName = "AlumnoTest.kt",
                                    lines =
                                        kotlinCode(
                                            """
                                            import kotlin.test.Test
                                            import kotlin.test.assertEquals
                                            import kotlin.test.assertFailsWith
                                            import kotlin.test.assertFalse
                                            import kotlin.test.assertTrue

                                            class AlumnoTest {
                                                @Test
                                                fun `una nota de cinco aprueba`() {
                                                    val alumno = Alumno("Ana")
                                                    alumno.nota = 5
                                                    assertTrue(alumno.aprobado)
                                                }

                                                @Test
                                                fun `una nota de cuatro suspende`() {
                                                    val alumno = Alumno("Ana")
                                                    alumno.nota = 4
                                                    assertFalse(alumno.aprobado)
                                                }

                                                @Test
                                                fun `una nota fuera de rango se rechaza y no cambia la anterior`() {
                                                    val alumno = Alumno("Ana")
                                                    alumno.nota = 6
                                                    assertFailsWith<IllegalArgumentException> {
                                                        alumno.nota = 11
                                                    }
                                                    assertEquals(6, alumno.nota)
                                                }
                                            }
                                            """.trimIndent(),
                                        ),
                                    note = richText(regular("Las tres pruebas pasan. Cada una crea su propio "), inlineCode("Alumno"), regular(", así que no dependen unas de otras.")),
                                ),
                            ),
                        ),
                ),
                LessonStep(
                    title = "Ahora te toca: el monedero",
                    explanation =
                        listOf(
                            paragraph(regular("Este programa lee el titular y una lista de movimientos, uno por línea, hasta una línea vacía: un número positivo ingresa y uno negativo paga. El saldo es una variable suelta, y nada impide que quede en negativo.")),
                            paragraph(regular("Conviértelo en una clase "), inlineCode("Monedero"), regular(" que proteja su saldo, y haz que "), highlight("un pago sin saldo suficiente se rechace sin cambiar nada"), regular(". Los movimientos que escribe el usuario son números distintos de cero.")),
                        ),
                    visual =
                        LessonVisualContent.CodeExamples(
                            listOf(
                                LessonCodeExample(
                                    title = "Monedero",
                                    lines =
                                        kotlinCode(
                                            $$"""
                                                fun main() {
                                                    println("Titular:")
                                                    val titular = readln()
                                                    println("Movimientos (+20 ingresa, -5 paga, línea vacía para terminar):")
                                                    var saldo = 0
                                                    var linea = readln()
                                                    while (linea != "") {
                                                        val cantidad = linea.toInt()
                                                        saldo += cantidad
                                                        linea = readln()
                                                    }
                                                    println("Monedero de $titular: $saldo €")
                                                }
                                            """.trimIndent(),
                                        ),
                                    note = richText(regular("Con "), inlineCode("Ana"), regular(", "), inlineCode("+20"), regular(", "), inlineCode("-5"), regular(" y una línea vacía muestra "), inlineCode("Monedero de Ana: 15 €"), regular(".")),
                                    isCopyable = true,
                                ),
                            ),
                        ),
                    links =
                        listOf(
                            LessonLink("Clases e instancias", "https://kotlinlang.org/docs/classes.html"),
                            LessonLink("Propiedades", "https://kotlinlang.org/docs/properties.html"),
                            LessonLink("Modificadores de visibilidad", "https://kotlinlang.org/docs/visibility-modifiers.html"),
                            LessonLink("Clases de datos", "https://kotlinlang.org/docs/data-classes.html"),
                            LessonLink("Clases de enumeración", "https://kotlinlang.org/docs/enum-classes.html"),
                        ),
                    practice =
                        LessonPractice(
                            id = "oop-classes-objects-wallet",
                            objective = richText(highlight("Modelar un dato con una clase que proteja su estado y rechace las operaciones que no tienen sentido.")),
                            statement = richText(regular("Crea la clase "), inlineCode("Monedero"), regular(" con las operaciones "), inlineCode("ingresar"), regular(" y "), inlineCode("pagar"), regular(", haz que "), inlineCode("main"), regular(" trabaje con un objeto suyo en lugar de con la variable "), inlineCode("saldo"), regular(", y comprueba la clase con pruebas.")),
                            requirements =
                                listOf(
                                    richText(inlineCode("Monedero(val titular: String)"), regular(": no se puede crear un monedero sin titular.")),
                                    richText(inlineCode("saldo"), regular(": empieza en "), inlineCode("0"), regular(". Quien use el monedero puede consultarlo, pero no asignarlo; solo cambia con las operaciones de abajo.")),
                                    richText(inlineCode("ingresar(cantidad: Int)"), regular(": suma la cantidad al saldo.")),
                                    richText(inlineCode("pagar(cantidad: Int): Boolean"), regular(": resta la cantidad del saldo e indica si se ha podido pagar. Si no hay saldo suficiente, el monedero queda como estaba.")),
                                    richText(regular("Ni "), inlineCode("ingresar"), regular(" ni "), inlineCode("pagar"), regular(" tienen sentido con una cantidad que no sea positiva: en ese caso el programa se detiene con un mensaje claro.")),
                                    richText(regular("Al mostrar un monedero con "), inlineCode("println"), regular(" sale "), inlineCode("Monedero de Ana: 15 €"), regular(".")),
                                    richText(inlineCode("main"), regular(" ingresa los movimientos positivos y paga los negativos. Si un pago no se puede hacer, avisa con "), inlineCode("Pago de 30 € rechazado: saldo insuficiente"), regular(" y sigue leyendo.")),
                                    richText(regular("Crea "), inlineCode("MonederoTest"), regular(" con pruebas de un ingreso, un pago correcto, un pago rechazado que deja el saldo igual y una cantidad no válida.")),
                                ),
                            expectedOutput =
                                listOf(
                                    consoleRun(
                                        consoleOutput("Titular:"),
                                        consoleInput("Ana"),
                                        consoleOutput("Movimientos (+20 ingresa, -5 paga, línea vacía para terminar):"),
                                        consoleInput("+20"),
                                        consoleInput("-5"),
                                        consoleInput("-30"),
                                        consoleOutput("Pago de 30 € rechazado: saldo insuficiente"),
                                        consoleInput("+10"),
                                        consoleInput(""),
                                        consoleOutput("Monedero de Ana: 25 €"),
                                    ),
                                    consoleRun(
                                        consoleOutput("Titular:"),
                                        consoleInput("Luis"),
                                        consoleOutput("Movimientos (+20 ingresa, -5 paga, línea vacía para terminar):"),
                                        consoleInput(""),
                                        consoleOutput("Monedero de Luis: 0 €"),
                                    ),
                                ),
                            hints =
                                listOf(
                                    richText(regular("Antes de escribir, decide qué datos se dan al crear el monedero y cuáles solo debe cambiar él mismo. Repasa «Construir con garantías» y «Proteger el estado».")),
                                    richText(regular("Si "), inlineCode("pagar"), regular(" devuelve un "), inlineCode("Boolean"), regular(", "), inlineCode("main"), regular(" puede saber si el pago se hizo. Piensa qué hay que comprobar antes de tocar el saldo.")),
                                    richText(regular("Las comprobaciones de la cantidad y del titular son validaciones de datos, como las del paso de "), inlineCode("init"), regular(". El signo del número leído decide qué operación llamar.")),
                                    richText(regular("Para que "), inlineCode("println(monedero)"), regular(" muestre algo útil, repasa «Mostrar un objeto». En las pruebas, un pago rechazado debe dejar el saldo como estaba.")),
                                ),
                            extension = richText(regular("Guarda un historial de movimientos en una lista, con una "), inlineCode("enum class TipoMovimiento { INGRESO, PAGO }"), regular(" y una "), inlineCode("data class Movimiento"), regular(". ¿Qué cambia si el historial es "), inlineCode("private"), regular(" y solo se puede ver una copia?")),
                            confirmation = richText(regular("Confirma que las dos ejecuciones dan la salida esperada, que "), inlineCode("saldo"), regular(" no se puede asignar desde "), inlineCode("main"), regular(" y que "), inlineCode("./gradlew test"), regular(" termina con "), inlineCode("BUILD SUCCESSFUL"), regular(".")),
                        ),
                ),
            ),
    )
