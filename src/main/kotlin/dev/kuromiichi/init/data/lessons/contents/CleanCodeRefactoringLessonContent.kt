package dev.kuromiichi.init.data.lessons.contents

import dev.kuromiichi.init.data.lessons.LessonCalloutTone
import dev.kuromiichi.init.data.lessons.LessonCodeExample
import dev.kuromiichi.init.data.lessons.LessonContent
import dev.kuromiichi.init.data.lessons.LessonLink
import dev.kuromiichi.init.data.lessons.LessonPractice
import dev.kuromiichi.init.data.lessons.LessonScreenshot
import dev.kuromiichi.init.data.lessons.LessonStep
import dev.kuromiichi.init.data.lessons.LessonStepContent
import dev.kuromiichi.init.data.lessons.LessonTraceTable
import dev.kuromiichi.init.data.lessons.LessonVisualContent
import dev.kuromiichi.init.data.lessons.callout
import dev.kuromiichi.init.data.lessons.consoleOutput
import dev.kuromiichi.init.data.lessons.consoleRun
import dev.kuromiichi.init.data.lessons.emphasis
import dev.kuromiichi.init.data.lessons.highlight
import dev.kuromiichi.init.data.lessons.inlineCode
import dev.kuromiichi.init.data.lessons.kotlinCode
import dev.kuromiichi.init.data.lessons.operatingSystemStep
import dev.kuromiichi.init.data.lessons.paragraph
import dev.kuromiichi.init.data.lessons.regular
import dev.kuromiichi.init.data.lessons.richText
import dev.kuromiichi.init.data.lessons.steps
import dev.kuromiichi.init.data.preferences.TargetOperatingSystem

/** Atajo de IntelliJ IDEA según el sistema; sin sistema elegido se muestran los dos. */
private fun TargetOperatingSystem?.shortcut(
    windowsAndLinux: String,
    macOs: String,
): String =
    when (this) {
        TargetOperatingSystem.MacOS -> macOs
        TargetOperatingSystem.Windows, TargetOperatingSystem.Linux -> windowsAndLinux
        null -> "$windowsAndLinux ($macOs en macOS)"
    }

private fun TargetOperatingSystem?.settingsPath(): String =
    when (this) {
        TargetOperatingSystem.MacOS -> "IntelliJ IDEA › Settings (⌘ ,)"
        TargetOperatingSystem.Windows, TargetOperatingSystem.Linux -> "File › Settings (Ctrl+Alt+S)"
        null -> "File › Settings (IntelliJ IDEA › Settings en macOS)"
    }

private const val REFACTORING_URL = "https://www.jetbrains.com/help/idea/refactoring-source-code.html"
private const val REFORMAT_URL = "https://www.jetbrains.com/help/idea/reformat-and-rearrange-code.html"
private const val INSPECTIONS_URL = "https://www.jetbrains.com/help/idea/code-inspection.html"

private val smellyPercentage =
    """
    fun calc(l: List<Int>): Int {
        var r = 0
        for (x in l) {
            if (x >= 5) {
                r = r + 1
            }
        }
        return r * 100 / l.size
    }
    """.trimIndent()

val cleanCodeRefactoringLessonContent =
    LessonContent(
        lessonId = "clean-code-refactoring",
        steps =
            listOf(
                LessonStep(
                    title = "Código que se lee",
                    explanation =
                        listOf(
                            paragraph(regular("Un programa se escribe una vez, pero se lee muchas: cuando buscas un error, cuando añades algo o cuando lo retoma otra persona, o tú dentro de unos meses. El "), highlight("código limpio"), regular(" es el que se entiende rápido y se cambia sin miedo.")),
                            paragraph(regular("En esta lección aprenderás a reconocer el código difícil de mantener y a mejorarlo con "), highlight("refactorizaciones"), regular(": cambios pequeños que no alteran lo que hace el programa, protegidos por pruebas y hechos con las herramientas de IntelliJ IDEA.")),
                        ),
                    visual = LessonVisualContent.Flow(listOf("Detectar un problema", "Asegurarlo con pruebas", "Hacer un cambio pequeño", "Volver a probar")),
                ),
                LessonStep(
                    title = "Malos olores",
                    explanation =
                        listOf(
                            paragraph(regular("Un "), highlight("mal olor"), regular(" ("), emphasis("code smell"), regular(") es una señal de que el código será difícil de entender o de cambiar. No es un error: el programa funciona, pero algo invita a revisarlo. Los más habituales son estos:")),
                            steps(
                                richText(regular("Nombres que no dicen nada, como "), inlineCode("x"), regular(", "), inlineCode("datos2"), regular(" o "), inlineCode("calc"), regular(".")),
                                richText(regular("Números sueltos cuyo significado hay que adivinar.")),
                                richText(regular("Funciones largas que hacen varias cosas a la vez.")),
                                richText(regular("Código repetido en varios sitios.")),
                                richText(regular("Condiciones anidadas, listas largas de parámetros y comentarios que explican lo que el código debería decir por sí solo.")),
                            ),
                        ),
                    visual =
                        LessonVisualContent.CodeExamples(
                            listOf(
                                LessonCodeExample(
                                    title = "¿Qué calcula esta función?",
                                    lines = kotlinCode(smellyPercentage),
                                    note = richText(inlineCode("calc(listOf(4, 7, 9, 3))"), regular(" devuelve "), inlineCode("50"), regular(": es el porcentaje de aprobados. Para saberlo hay que leer cada línea y adivinar qué son "), inlineCode("5"), regular(" y "), inlineCode("r"), regular(".")),
                                ),
                            ),
                        ),
                ),
                LessonStep(
                    title = "Nombres que explican",
                    explanation =
                        listOf(
                            paragraph(regular("Un buen nombre ahorra un comentario. Debe decir qué es o qué hace, aunque sea más largo: "), inlineCode("tienePocasFaltas"), regular(" se entiende sin mirar el cuerpo de la función; "), inlineCode("chk"), regular(", no. Estas son las convenciones de Kotlin:")),
                            steps(
                                richText(regular("Clases e interfaces en "), emphasis("UpperCamelCase"), regular(" y con un sustantivo: "), inlineCode("Alumno"), regular(", "), inlineCode("PedidoOnline"), regular(".")),
                                richText(regular("Funciones, variables y propiedades en "), emphasis("lowerCamelCase"), regular("; las funciones, con un verbo: "), inlineCode("calcularMedia"), regular(".")),
                                richText(regular("Lo que responde sí o no empieza por "), inlineCode("es"), regular(", "), inlineCode("tiene"), regular(" o "), inlineCode("puede"), regular(": "), inlineCode("esMayorDeEdad"), regular(".")),
                                richText(regular("Las constantes, en mayúsculas con guiones bajos: "), inlineCode("NOTA_APROBADO"), regular(".")),
                            ),
                        ),
                    visual =
                        LessonVisualContent.CodeExamples(
                            listOf(
                                LessonCodeExample(
                                    title = "Abreviaturas",
                                    lines =
                                        kotlinCode(
                                            """
                                            data class Alu(val n: String, val f: Int)

                                            fun chk(a: Alu): Boolean = a.f < 3
                                            """.trimIndent(),
                                        ),
                                    note = richText(regular("¿"), inlineCode("f"), regular(" es la nota, las faltas o la fecha? Hay que buscar dónde se usa para saberlo.")),
                                ),
                                LessonCodeExample(
                                    title = "Nombres que se leen",
                                    lines =
                                        kotlinCode(
                                            """
                                            data class Alumno(
                                                val nombre: String,
                                                val faltas: Int,
                                            )

                                            fun tienePocasFaltas(
                                                alumno: Alumno,
                                            ): Boolean = alumno.faltas < 3
                                            """.trimIndent(),
                                        ),
                                    note = richText(regular("El mismo código, pero ahora se lee como una frase. Queda el "), inlineCode("3"), regular(", del que se ocupa el paso siguiente.")),
                                ),
                            ),
                        ),
                ),
                LessonStep(
                    title = "Números mágicos",
                    explanation =
                        listOf(
                            paragraph(regular("Un "), highlight("número mágico"), regular(" es un valor escrito tal cual cuyo significado no se ve. En el primer ejemplo, "), inlineCode("0.21"), regular(" aparece dos veces: si cambia el IVA, hay que encontrar todas sus apariciones y no confundirlo con otro "), inlineCode("0.21"), regular(" que signifique otra cosa.")),
                            paragraph(regular("Con una constante, el valor tiene nombre y se cambia en un solo sitio. Una "), inlineCode("const val"), regular(" se declara fuera de las funciones, o en un "), inlineCode("companion object"), regular(", y guarda valores sencillos que se conocen al compilar, como números y textos.")),
                            callout(
                                "No todos los números son mágicos",
                                richText(regular("Un "), inlineCode("0"), regular(" para empezar a contar, el "), inlineCode("100"), regular(" de un porcentaje o el "), inlineCode("2"), regular(" de "), inlineCode("n % 2 == 0"), regular(" se entienden solos. Ponles nombre cuando haya que pensar qué significan.")),
                                LessonCalloutTone.Tip,
                            ),
                        ),
                    visual =
                        LessonVisualContent.CodeExamples(
                            listOf(
                                LessonCodeExample(
                                    title = "El mismo valor en dos sitios",
                                    lines =
                                        kotlinCode(
                                            """
                                            fun iva(base: Double): Double = base * 0.21

                                            fun precioFinal(base: Double): Double =
                                                base + base * 0.21
                                            """.trimIndent(),
                                        ),
                                    note = richText(regular("Si el IVA pasa a ser otro, hay que acordarse de cambiar las dos funciones.")),
                                ),
                                LessonCodeExample(
                                    title = "Un valor con nombre",
                                    lines =
                                        kotlinCode(
                                            """
                                            const val IVA = 0.21

                                            fun iva(base: Double): Double = base * IVA

                                            fun precioFinal(base: Double): Double =
                                                base + iva(base)
                                            """.trimIndent(),
                                        ),
                                    note = richText(regular("El valor está en un único sitio, y "), inlineCode("precioFinal"), regular(" reutiliza "), inlineCode("iva"), regular(" en lugar de repetir la cuenta.")),
                                ),
                            ),
                        ),
                ),
                LessonStep(
                    title = "Funciones cortas, una sola tarea",
                    explanation =
                        listOf(
                            paragraph(regular("En la lección de funciones ya separaste un programa largo en funciones. La regla sigue siendo la misma: cada función hace "), highlight("una sola cosa"), regular(", y su nombre dice cuál. Si para describirla necesitas decir «y», probablemente son dos.")),
                            paragraph(regular("En el primer ejemplo, "), inlineCode("mostrarInforme"), regular(" calcula la media, cuenta los aprobados y escribe. En el segundo, cada cálculo es una función que se puede probar por separado, y "), inlineCode("mostrarInforme"), regular(" se lee como un índice.")),
                        ),
                    visual =
                        LessonVisualContent.CodeExamples(
                            listOf(
                                LessonCodeExample(
                                    title = "Una función que lo hace todo",
                                    lines =
                                        kotlinCode(
                                            $$"""
                                                const val NOTA_APROBADO = 5

                                                fun mostrarInforme(notas: List<Int>) {
                                                    var suma = 0
                                                    for (nota in notas) {
                                                        suma += nota
                                                    }
                                                    val media = suma / notas.size
                                                    var aprobados = 0
                                                    for (nota in notas) {
                                                        if (nota >= NOTA_APROBADO) {
                                                            aprobados++
                                                        }
                                                    }
                                                    println("Media: $media")
                                                    println("Aprobados: $aprobados")
                                                }
                                            """.trimIndent(),
                                        ),
                                    note = richText(regular("Para cambiar cómo se calcula la media hay que leer toda la función.")),
                                ),
                                LessonCodeExample(
                                    title = "Cada tarea en su función",
                                    lines =
                                        kotlinCode(
                                            $$"""
                                                const val NOTA_APROBADO = 5

                                                fun mostrarInforme(notas: List<Int>) {
                                                    val aprobados = contarAprobados(notas)
                                                    println("Media: ${media(notas)}")
                                                    println("Aprobados: $aprobados")
                                                }

                                                fun media(notas: List<Int>): Int =
                                                    notas.sum() / notas.size

                                                fun contarAprobados(notas: List<Int>): Int =
                                                    notas.count { it >= NOTA_APROBADO }
                                            """.trimIndent(),
                                        ),
                                    note = richText(regular("Muestra lo mismo. "), inlineCode("media"), regular(" y "), inlineCode("contarAprobados"), regular(" devuelven su resultado y no escriben nada, así que se pueden probar.")),
                                ),
                            ),
                        ),
                ),
                LessonStep(
                    title = "No te repitas",
                    explanation =
                        listOf(
                            paragraph(regular("Copiar y pegar es rápido, pero cada copia es un sitio más que hay que cambiar, y tarde o temprano una se queda sin cambiar. La regla "), highlight("DRY"), regular(" ("), emphasis("Don't Repeat Yourself"), regular(") pide que cada idea esté escrita una sola vez.")),
                            paragraph(regular("En el ejemplo, las tres líneas del título se repiten con otro texto. Al llevarlas a una función con el texto como parámetro, cambiar el aspecto de todos los títulos es tocar una sola función.")),
                            paragraph(regular("También se repiten las reglas: si "), inlineCode("nota >= 5"), regular(" aparece en varias funciones, dale nombre una vez.")),
                        ),
                    visual =
                        LessonVisualContent.CodeExamples(
                            listOf(
                                LessonCodeExample(
                                    title = "El mismo bloque dos veces",
                                    lines =
                                        kotlinCode(
                                            """
                                            fun main() {
                                                println("==========")
                                                println("Notas")
                                                println("==========")
                                                println("Ana: 7")

                                                println("==========")
                                                println("Asistencia")
                                                println("==========")
                                                println("Ana: 2 faltas")
                                            }
                                            """.trimIndent(),
                                        ),
                                    note = richText(regular("Si los títulos pasan a ir entre "), inlineCode("-"), regular(", hay que cambiar cuatro líneas.")),
                                ),
                                LessonCodeExample(
                                    title = "Una función para el bloque",
                                    lines =
                                        kotlinCode(
                                            """
                                            fun main() {
                                                mostrarTitulo("Notas")
                                                println("Ana: 7")

                                                mostrarTitulo("Asistencia")
                                                println("Ana: 2 faltas")
                                            }

                                            fun mostrarTitulo(texto: String) {
                                                val linea = "=".repeat(10)
                                                println(linea)
                                                println(texto)
                                                println(linea)
                                            }
                                            """.trimIndent(),
                                        ),
                                    note = richText(regular("Muestra exactamente lo mismo, y el aspecto de los títulos se decide en un único sitio.")),
                                ),
                            ),
                        ),
                ),
                LessonStep(
                    title = "Condiciones más claras",
                    explanation =
                        listOf(
                            paragraph(regular("Cada "), inlineCode("if"), regular(" anidado obliga a recordar en qué rama estás. Con "), highlight("cláusulas de guarda"), regular(" se descartan primero los casos que no siguen, cada uno con su "), inlineCode("return"), regular(", y lo importante queda al final, sin sangrar.")),
                            paragraph(regular("Una cadena de "), inlineCode("if"), regular(" y "), inlineCode("else if"), regular(" que compara el mismo valor se lee mejor como "), inlineCode("when"), regular(". Y una condición larga se entiende antes si la guardas en una variable con nombre, como "), inlineCode("val esMayorDeEdad = edad >= 18"), regular(".")),
                        ),
                    visual =
                        LessonVisualContent.CodeExamples(
                            listOf(
                                LessonCodeExample(
                                    title = "Tres niveles de if",
                                    lines =
                                        kotlinCode(
                                            """
                                            fun puedeMatricularse(
                                                edad: Int,
                                                tieneTitulo: Boolean,
                                                plazasLibres: Int,
                                            ): Boolean {
                                                if (plazasLibres > 0) {
                                                    if (edad >= 16) {
                                                        if (tieneTitulo) {
                                                            return true
                                                        }
                                                    }
                                                }
                                                return false
                                            }
                                            """.trimIndent(),
                                        ),
                                    note = richText(regular("Para saber cuándo devuelve "), inlineCode("true"), regular(" hay que seguir los tres niveles.")),
                                ),
                                LessonCodeExample(
                                    title = "Guardas y when",
                                    lines =
                                        kotlinCode(
                                            """
                                            const val EDAD_MINIMA = 16

                                            fun puedeMatricularse(
                                                edad: Int,
                                                tieneTitulo: Boolean,
                                                plazasLibres: Int,
                                            ): Boolean {
                                                if (plazasLibres == 0) return false
                                                if (edad < EDAD_MINIMA) return false
                                                return tieneTitulo
                                            }

                                            fun calificar(nota: Int): String =
                                                when {
                                                    nota < 5 -> "Suspenso"
                                                    nota < 7 -> "Aprobado"
                                                    nota < 9 -> "Notable"
                                                    else -> "Sobresaliente"
                                                }
                                            """.trimIndent(),
                                        ),
                                    note = richText(regular("Cada guarda descarta un caso; lo que queda es la regla principal. "), inlineCode("calificar"), regular(" sustituye a una cadena de cuatro "), inlineCode("if"), regular(".")),
                                ),
                            ),
                        ),
                ),
                LessonStep(
                    title = "Parámetros que confunden",
                    explanation =
                        listOf(
                            paragraph(regular("En "), inlineCode("Usuario(\"Ana\", true, false)"), regular(", ¿qué significan "), inlineCode("true"), regular(" y "), inlineCode("false"), regular("? Los "), highlight("argumentos con nombre"), regular(" lo dicen en la propia llamada, y los "), highlight("valores por defecto"), regular(" permiten omitir lo habitual.")),
                            paragraph(regular("Un parámetro "), inlineCode("Boolean"), regular(" que cambia lo que hace una función suele esconder dos funciones: "), inlineCode("imprimir(informe, true)"), regular(" se entiende peor que "), inlineCode("imprimirResumen(informe)"), regular(". Y si una función recibe muchos datos que van juntos, agrúpalos en una "), inlineCode("data class"), regular(".")),
                        ),
                    visual =
                        LessonVisualContent.CodeExamples(
                            listOf(
                                LessonCodeExample(
                                    title = "Llamadas que se explican solas",
                                    lines =
                                        kotlinCode(
                                            """
                                            data class Usuario(
                                                val nombre: String,
                                                val esAdministrador: Boolean = false,
                                                val recibeAvisos: Boolean = true,
                                            )

                                            fun main() {
                                                val ana = Usuario("Ana", true, false)
                                                val luis =
                                                    Usuario(
                                                        nombre = "Luis",
                                                        recibeAvisos = false,
                                                    )
                                            }
                                            """.trimIndent(),
                                        ),
                                    note = richText(regular("Las dos llamadas son válidas, pero la de "), inlineCode("luis"), regular(" se entiende sin abrir "), inlineCode("Usuario"), regular(" y no repite el valor por defecto de "), inlineCode("esAdministrador"), regular(".")),
                                ),
                            ),
                        ),
                ),
                LessonStep(
                    title = "Comentarios que sobran y comentarios que ayudan",
                    explanation =
                        listOf(
                            paragraph(regular("Un comentario que repite el código sobra, y además envejece: si el código cambia y el comentario no, miente. Antes de comentar, prueba a cambiar un nombre o extraer una función. El código comentado tampoco se guarda: para recuperarlo está Git.")),
                            paragraph(regular("Sí ayudan los comentarios que explican el "), highlight("porqué"), regular(": una decisión que no se deduce del código. Y en las funciones que usan otros, el KDoc explica qué hace, sus parámetros y, con "), inlineCode("@throws"), regular(", qué excepción puede lanzar.")),
                        ),
                    visual =
                        LessonVisualContent.CodeExamples(
                            listOf(
                                LessonCodeExample(
                                    title = "Comentarios que repiten el código",
                                    lines =
                                        kotlinCode(
                                            """
                                            // comprueba la nota
                                            fun c(n: Int): Boolean {
                                                // si n es mayor o igual que 5
                                                return n >= 5
                                                // return n > 4
                                            }
                                            """.trimIndent(),
                                        ),
                                    note = richText(regular("Los comentarios dicen lo mismo que el código, y la última línea es código muerto que nadie se atreve a borrar.")),
                                ),
                                LessonCodeExample(
                                    title = "Nombres claros y un porqué",
                                    lines =
                                        kotlinCode(
                                            $$"""
                                                const val NOTA_APROBADO = 5

                                                /**
                                                 * Indica si una nota de 0 a 10 aprueba.
                                                 *
                                                 * @throws IllegalArgumentException si no
                                                 * está entre 0 y 10.
                                                 */
                                                fun estaAprobada(nota: Int): Boolean {
                                                    require(nota in 0..10) {
                                                        "Nota no válida: $nota"
                                                    }
                                                    return nota >= NOTA_APROBADO
                                                }

                                                // Secretaría trunca: un 4,9 es un 4.
                                                fun media(notas: List<Int>): Int =
                                                    notas.sum() / notas.size
                                            """.trimIndent(),
                                        ),
                                    note = richText(regular("El KDoc documenta lo que no se ve desde fuera, y el comentario de "), inlineCode("media"), regular(" explica por qué se pierden los decimales.")),
                                ),
                            ),
                        ),
                ),
                LessonStep(
                    title = "Refactorizar con red de seguridad",
                    explanation =
                        listOf(
                            paragraph(regular("Refactorizar es cambiar la forma sin cambiar el comportamiento. Para asegurarte de que no cambia, antes de tocar nada escribe pruebas que fijen lo que el código hace "), emphasis("ahora"), regular(". Después, avanza así:")),
                            steps(
                                richText(regular("Comprueba que las pruebas pasan.")),
                                richText(regular("Haz un único cambio pequeño.")),
                                richText(regular("Vuelve a pasar las pruebas. Si fallan, deshaz el cambio en lugar de buscar el error.")),
                                richText(regular("Haz un commit y repite.")),
                            ),
                            paragraph(regular("Las pruebas del ejemplo son de "), inlineCode("calc"), regular(", la función de «Malos olores» que calcula el porcentaje de aprobados, guardada en "), inlineCode("src/main/kotlin/Calc.kt"), regular(". Comprueban lo que devuelve ahora con tres listas de notas.")),
                            callout(
                                "Refactorizar no es arreglar",
                                richText(inlineCode("calc(listOf())"), regular(" divide entre cero y lanza una excepción. Mientras refactorizas, ese comportamiento se mantiene; corregirlo es otro cambio, con su propia prueba y su propio commit.")),
                                LessonCalloutTone.Warning,
                            ),
                        ),
                    visual =
                        LessonVisualContent.CodeExamples(
                            listOf(
                                LessonCodeExample(
                                    title = "src/test/kotlin/CalcTest.kt",
                                    fileName = "CalcTest.kt",
                                    lines =
                                        kotlinCode(
                                            """
                                            import kotlin.test.Test
                                            import kotlin.test.assertEquals

                                            class CalcTest {
                                                @Test
                                                fun `la mitad de aprobados es un 50`() {
                                                    val notas = listOf(4, 7, 9, 3)
                                                    assertEquals(50, calc(notas))
                                                }

                                                @Test
                                                fun `un 5 cuenta como aprobado`() {
                                                    val notas = listOf(5, 10)
                                                    assertEquals(100, calc(notas))
                                                }

                                                @Test
                                                fun `sin aprobados es un 0`() {
                                                    val notas = listOf(2, 4)
                                                    assertEquals(0, calc(notas))
                                                }
                                            }
                                            """.trimIndent(),
                                        ),
                                    note = richText(regular("Las tres pruebas pasan. Si renombras "), inlineCode("calc"), regular(" con IntelliJ IDEA, sus llamadas en este archivo cambian solas.")),
                                ),
                            ),
                        ),
                ),
                operatingSystemStep("Refactorizar con IntelliJ IDEA") { system ->
                    LessonStepContent(
                        explanation =
                            listOf(
                                paragraph(regular("Cambiar un nombre a mano obliga a buscar todos sus usos. Las "), highlight("refactorizaciones automáticas"), regular(" de IntelliJ IDEA entienden el código: renombran solo ese elemento, actualizan todas sus llamadas, también las de las pruebas, y avisan si el cambio provoca un conflicto.")),
                                paragraph(regular("Están en el menú "), emphasis("Refactor"), regular(", o en el menú contextual del editor. Si no recuerdas un atajo, coloca el cursor en el código y pulsa "), inlineCode(system.shortcut("Ctrl+Alt+Shift+T", "⌃ T")), regular(" ("), emphasis("Refactor This"), regular("): verás las refactorizaciones posibles en ese punto.")),
                                paragraph(regular("Si el resultado no te convence, "), inlineCode(system.shortcut("Ctrl+Z", "⌘ Z")), regular(" deshace la refactorización entera.")),
                            ),
                        visual =
                            LessonVisualContent.Trace(
                                example =
                                    LessonCodeExample(
                                        title = "Para practicar los atajos",
                                        lines = kotlinCode(smellyPercentage),
                                        note = richText(regular("Coloca el cursor en "), inlineCode("calc"), regular(" y pulsa "), inlineCode(system.shortcut("Shift+F6", "⇧ F6")), regular(", o selecciona "), inlineCode("x >= 5"), regular(" y pulsa "), inlineCode(system.shortcut("Ctrl+Alt+V", "⌘ ⌥ V")), regular(".")),
                                    ),
                                table =
                                    LessonTraceTable(
                                        title = "Refactorizaciones más usadas",
                                        columns = listOf("Refactorización", "Qué hace", "Atajo"),
                                        rows =
                                            listOf(
                                                listOf("Rename", "Cambia un nombre y todos sus usos", system.shortcut("Shift+F6", "⇧ F6")),
                                                listOf("Introduce Variable", "Guarda una expresión en una variable", system.shortcut("Ctrl+Alt+V", "⌘ ⌥ V")),
                                                listOf("Introduce Constant", "Convierte un valor en una constante", system.shortcut("Ctrl+Alt+C", "⌘ ⌥ C")),
                                                listOf("Extract Function", "Lleva el código seleccionado a una función nueva", system.shortcut("Ctrl+Alt+M", "⌘ ⌥ M")),
                                                listOf("Inline", "Lo contrario: sustituye una variable o función por su contenido", system.shortcut("Ctrl+Alt+N", "⌘ ⌥ N")),
                                                listOf("Change Signature", "Cambia los parámetros y actualiza las llamadas", system.shortcut("Ctrl+F6", "⌘ F6")),
                                            ),
                                    ),
                            ),
                        links = listOf(LessonLink("Refactorizar código en IntelliJ IDEA", REFACTORING_URL)),
                    )
                },
                LessonStep(
                    title = "Paso a paso",
                    explanation =
                        listOf(
                            paragraph(regular("Así queda "), inlineCode("calc"), regular(" después de cuatro pasos pequeños. Tras cada uno se pasan las pruebas de «Refactorizar con red de seguridad», y las tres siguen en verde: el comportamiento no ha cambiado en ningún momento.")),
                            paragraph(regular("Los tres primeros pasos los hace IntelliJ IDEA. El último es a mano: sustituir el bucle por "), inlineCode("count"), regular(" es posible porque, tras el tercer paso, la condición ya tiene nombre.")),
                        ),
                    visual =
                        LessonVisualContent.Trace(
                            example =
                                LessonCodeExample(
                                    title = "El porcentaje de aprobados, refactorizado",
                                    lines =
                                        kotlinCode(
                                            """
                                            const val NOTA_APROBADO = 5

                                            fun porcentajeAprobados(
                                                notas: List<Int>,
                                            ): Int {
                                                val aprobados =
                                                    notas.count { estaAprobada(it) }
                                                return aprobados * 100 / notas.size
                                            }

                                            fun estaAprobada(nota: Int): Boolean =
                                                nota >= NOTA_APROBADO
                                            """.trimIndent(),
                                        ),
                                    note = richText(inlineCode("porcentajeAprobados(listOf(4, 7, 9, 3))"), regular(" sigue devolviendo "), inlineCode("50"), regular(".")),
                                ),
                            table =
                                LessonTraceTable(
                                    title = "Qué cambia en cada paso",
                                    columns = listOf("Paso", "Refactorización", "Qué cambia"),
                                    rows =
                                        listOf(
                                            listOf("1", "Rename", "calc, l, x y r pasan a ser porcentajeAprobados, notas, nota y aprobados"),
                                            listOf("2", "Introduce Constant", "El 5 pasa a ser NOTA_APROBADO"),
                                            listOf("3", "Extract Function", "La condición del if pasa a estaAprobada"),
                                            listOf("4", "A mano", "El bucle que cuenta se convierte en count"),
                                        ),
                                ),
                        ),
                ),
                operatingSystemStep("Formato automático") { system ->
                    LessonStepContent(
                        explanation =
                            listOf(
                                paragraph(regular("Sangrías, espacios y saltos de línea no cambian lo que hace el programa, pero un formato uniforme hace que todo el código se lea igual. No hace falta cuidarlo a mano: "), emphasis("Code › Reformat Code"), regular(" ("), inlineCode(system.shortcut("Ctrl+Alt+L", "⌘ ⌥ L")), regular(") aplica las convenciones oficiales de Kotlin.")),
                                paragraph(emphasis("Code › Optimize Imports"), regular(" ("), inlineCode(system.shortcut("Ctrl+Alt+O", "⌃ ⌥ O")), regular(") quita los "), inlineCode("import"), regular(" que ya no se usan y ordena el resto.")),
                                paragraph(regular("Para no olvidarlo, en "), emphasis(system.settingsPath()), regular(", dentro de "), emphasis("Tools › Actions on Save"), regular(", puedes activar "), emphasis("Reformat code"), regular(" y "), emphasis("Optimize imports"), regular(" para que se apliquen cada vez que se guarda un archivo.")),
                            ),
                        visual =
                            LessonVisualContent.CodeExamples(
                                listOf(
                                    LessonCodeExample(
                                        title = "Antes de formatear",
                                        lines =
                                            kotlinCode(
                                                """
                                                fun media(notas:List<Int>):Int{
                                                return notas.sum()/notas.size}
                                                """.trimIndent(),
                                            ),
                                        note = richText(regular("Compila y funciona, pero cuesta leerlo.")),
                                    ),
                                    LessonCodeExample(
                                        title = "Después de Reformat Code",
                                        lines =
                                            kotlinCode(
                                                """
                                                fun media(notas: List<Int>): Int {
                                                    return notas.sum() / notas.size
                                                }
                                                """.trimIndent(),
                                            ),
                                        note = richText(regular("Espacios tras los dos puntos y alrededor de los operadores, sangría de cuatro espacios y la llave de cierre en su propia línea.")),
                                    ),
                                ),
                            ),
                        links =
                            listOf(
                                LessonLink("Formatear código en IntelliJ IDEA", REFORMAT_URL),
                                LessonLink("Convenciones de código de Kotlin", "https://kotlinlang.org/docs/coding-conventions.html"),
                            ),
                    )
                },
                operatingSystemStep("Avisos del IDE") { system ->
                    LessonStepContent(
                        explanation =
                            listOf(
                                paragraph(regular("IntelliJ IDEA revisa el código mientras escribes con sus "), highlight("inspecciones"), regular(". Lo que no impide compilar, pero conviene revisar, aparece subrayado o atenuado, y una marca en el margen derecho indica dónde está cada aviso.")),
                                paragraph(regular("En la captura, "), inlineCode("nombre"), regular(" no cambia y puede ser "), inlineCode("val"), regular(" (línea 2), "), inlineCode("edad"), regular(" no se usa (línea 3) y la línea 8 se puede escribir con "), inlineCode("+="), regular(". El recuento de arriba a la derecha resume los avisos del archivo.")),
                                paragraph(regular("Coloca el cursor en el aviso y pulsa "), inlineCode(system.shortcut("Alt+Enter", "⌥ ⏎")), regular(": IntelliJ IDEA explica el problema y propone corregirlo. Para revisar todo el proyecto a la vez, usa "), emphasis("Code › Inspect Code"), regular(".")),
                            ),
                        visual =
                            LessonVisualContent.Screenshot(
                                image = LessonScreenshot.IntellijInspections,
                                description = "Editor de IntelliJ IDEA con avisos: var nombre subrayado, edad atenuada, la línea 8 subrayada y, arriba a la derecha, el recuento de tres avisos y uno leve.",
                            ),
                        links = listOf(LessonLink("Inspecciones de código en IntelliJ IDEA", INSPECTIONS_URL)),
                    )
                },
                LessonStep(
                    title = "Optimizar, pero después",
                    explanation =
                        listOf(
                            paragraph(regular("Optimizar es hacer que el programa gaste menos tiempo o memoria. El orden importa: primero que funcione, después que se entienda y, solo si de verdad va lento, que sea rápido. Optimizar sin haber medido suele complicar el código para ganar nada.")),
                            paragraph(regular("Lo que sí conviene evitar desde el principio es el trabajo repetido. En el primer ejemplo, "), inlineCode("average()"), regular(" recorre toda la lista para cada nota; en el segundo, una sola vez. Del mismo modo, para comprobar muchas veces si algo está en una colección, un "), inlineCode("Set"), regular(" es más rápido que una lista.")),
                        ),
                    visual =
                        LessonVisualContent.CodeExamples(
                            listOf(
                                LessonCodeExample(
                                    title = "La media, una vez por nota",
                                    lines =
                                        kotlinCode(
                                            """
                                            fun sobreLaMedia(
                                                notas: List<Int>,
                                            ): List<Int> =
                                                notas.filter { it > notas.average() }
                                            """.trimIndent(),
                                        ),
                                    note = richText(regular("Con 1000 notas, calcula la media 1000 veces y recorre la lista un millón de veces en total.")),
                                ),
                                LessonCodeExample(
                                    title = "La media, una sola vez",
                                    lines =
                                        kotlinCode(
                                            """
                                            fun sobreLaMedia(
                                                notas: List<Int>,
                                            ): List<Int> {
                                                val media = notas.average()
                                                return notas.filter { it > media }
                                            }
                                            """.trimIndent(),
                                        ),
                                    note = richText(regular("Mismo resultado, y además se lee mejor: la media tiene nombre.")),
                                ),
                            ),
                        ),
                ),
                LessonStep(
                    title = "Ahora te toca: la factura de la cafetería",
                    explanation =
                        listOf(
                            paragraph(regular("Este programa calcula lo que paga cada cliente de la cafetería del instituto, con un descuento para estudiantes y otro para los pedidos grandes. Funciona, pero cuesta entenderlo y cualquier cambio da miedo.")),
                            paragraph(regular("Déjalo limpio "), highlight("sin cambiar lo que hace"), regular(". Antes de tocar nada, ejecútalo y asegura su comportamiento con pruebas; después, avanza en pasos pequeños.")),
                        ),
                    visual =
                        LessonVisualContent.CodeExamples(
                            listOf(
                                LessonCodeExample(
                                    title = "Facturas de la cafetería",
                                    lines =
                                        kotlinCode(
                                            """
                                            fun main() {
                                                val x =
                                                    listOf(
                                                        Pedido(
                                                            "Ana",
                                                            listOf("café", "tostada"),
                                                            true,
                                                        ),
                                                        Pedido(
                                                            "Luis",
                                                            listOf("menú", "menú"),
                                                            false,
                                                        ),
                                                        Pedido(
                                                            "Marta",
                                                            listOf("zumo", "zumo"),
                                                            true,
                                                        ),
                                                    )
                                                f(x)
                                            }

                                            data class Pedido(
                                                val c: String,
                                                val l: List<String>,
                                                val e: Boolean,
                                            )

                                            // calcula el total
                                            fun t(p: Pedido): Int {
                                                var s = 0
                                                // suma los precios
                                                for (i in p.l) {
                                                    if (i == "café") {
                                                        s = s + 2
                                                    } else if (i == "tostada") {
                                                        s = s + 3
                                                    } else if (i == "zumo") {
                                                        s = s + 5
                                                    } else if (i == "menú") {
                                                        s = s + 10
                                                    }
                                                }
                                                if (p.e) {
                                                    s = s - s * 20 / 100
                                                }
                                                if (s >= 20) {
                                                    s = s - s * 10 / 100
                                                }
                                                return s
                                            }

                                            // muestra la factura
                                            fun f(x: List<Pedido>) {
                                                if (x.isNotEmpty()) {
                                                    var tot = 0
                                                    for (p in x) {
                                                        val s = t(p)
                                                        // suma al total
                                                        tot = tot + s
                                                        println(p.c + ": " + s + " €")
                                                    }
                                                    // println("Pedidos: " + x.size)
                                                    println("Total: " + tot + " €")
                                                } else {
                                                    println("No hay pedidos")
                                                }
                                            }
                                            """.trimIndent(),
                                        ),
                                    note = richText(regular("Muestra lo que paga cada cliente, de "), inlineCode("Ana: 4 €"), regular(" a "), inlineCode("Marta: 8 €"), regular(", y el total.")),
                                    isCopyable = true,
                                ),
                            ),
                        ),
                    links =
                        listOf(
                            LessonLink("Convenciones de código de Kotlin", "https://kotlinlang.org/docs/coding-conventions.html"),
                            LessonLink("Refactorizar código en IntelliJ IDEA", REFACTORING_URL),
                            LessonLink("Inspecciones de código en IntelliJ IDEA", INSPECTIONS_URL),
                            LessonLink("Catálogo de malos olores", "https://refactoring.guru/es/refactoring/smells"),
                        ),
                    practice =
                        LessonPractice(
                            id = "clean-code-refactoring-cafeteria",
                            objective = richText(highlight("Mejorar un programa que ya funciona sin cambiar lo que hace, apoyándote en pruebas y en las herramientas de IntelliJ IDEA.")),
                            statement = richText(regular("Refactoriza el programa de la cafetería para que se entienda y se pueda cambiar con facilidad, manteniendo exactamente su comportamiento.")),
                            requirements =
                                listOf(
                                    richText(regular("Antes de cambiar el programa, crea "), inlineCode("FacturaTest"), regular(" con pruebas que comprueben lo que paga ahora cada tipo de cliente. Al terminar deben seguir pasando sin haber cambiado ningún importe esperado.")),
                                    richText(regular("Al terminar, el programa tiene "), inlineCode("precio(producto: String): Int"), regular(" y "), inlineCode("totalPedido(pedido: Pedido): Int"), regular(", que devuelven el precio de un producto y lo que paga un pedido con sus descuentos.")),
                                    richText(regular("Todos los nombres dicen qué son o qué hacen, y ninguna función pasa de 10 líneas, sin contar la lista de pedidos de ejemplo.")),
                                    richText(regular("Cambiar un precio, un porcentaje de descuento o el importe a partir del cual un pedido es grande obliga a tocar una sola línea.")),
                                    richText(regular("Con los mismos datos, la salida es idéntica a la del programa original, también sin pedidos (con "), inlineCode("listOf<Pedido>()"), regular(").")),
                                    richText(emphasis("Code › Inspect Code"), regular(" no muestra avisos en tu archivo, y "), emphasis("Reformat Code"), regular(" no cambia nada.")),
                                ),
                            expectedOutput =
                                listOf(
                                    consoleRun(
                                        consoleOutput("Ana: 4 €"),
                                        consoleOutput("Luis: 18 €"),
                                        consoleOutput("Marta: 8 €"),
                                        consoleOutput("Total: 30 €"),
                                    ),
                                    consoleRun(
                                        consoleOutput("No hay pedidos"),
                                    ),
                                ),
                            hints =
                                listOf(
                                    richText(regular("Empieza por las pruebas, llamando a "), inlineCode("t"), regular(" con su nombre actual: un pedido sin descuentos, uno de estudiante y uno grande. Cuando renombres "), inlineCode("t"), regular(" con "), emphasis("Rename"), regular(", las pruebas se actualizan solas.")),
                                    richText(regular("Renombra "), inlineCode("x"), regular(", "), inlineCode("c"), regular(", "), inlineCode("l"), regular(", "), inlineCode("e"), regular(", "), inlineCode("s"), regular(" y "), inlineCode("f"), regular(", y convierte en constantes los porcentajes y el importe mínimo. Ojo: hay dos "), inlineCode("20"), regular(" que significan cosas distintas.")),
                                    richText(regular("La cuenta del descuento se repite: llévala a una función con "), emphasis("Extract Function"), regular(". La cadena de "), inlineCode("if"), regular(" de los precios cabe en un "), inlineCode("when"), regular(" dentro de "), inlineCode("precio"), regular(", y el bucle de la suma, en "), inlineCode("sumOf"), regular(".")),
                                    richText(regular("En la función que muestra la factura, una cláusula de guarda para la lista vacía elimina el "), inlineCode("else"), regular(". Las plantillas de texto sustituyen a las concatenaciones con "), inlineCode("+"), regular(", y los comentarios que repiten el código sobran.")),
                                ),
                            extension = richText(regular("Haz un commit por cada refactorización y añade después un bocadillo de 4 €. ¿Cuántas líneas has tenido que tocar? Documenta con KDoc "), inlineCode("totalPedido"), regular(", incluido en qué orden se aplican los descuentos.")),
                            confirmation = richText(regular("Confirma que las dos ejecuciones dan la salida esperada, que "), inlineCode("./gradlew test"), regular(" termina con "), inlineCode("BUILD SUCCESSFUL"), regular(" sin haber cambiado ningún importe esperado de las pruebas y que "), emphasis("Inspect Code"), regular(" no encuentra avisos en tu archivo.")),
                        ),
                ),
            ),
    )
