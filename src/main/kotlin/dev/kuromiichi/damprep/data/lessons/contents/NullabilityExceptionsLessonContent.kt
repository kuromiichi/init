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
import dev.kuromiichi.damprep.data.lessons.steps

val nullabilityExceptionsLessonContent =
    LessonContent(
        lessonId = "nullability-exceptions",
        steps =
            listOf(
                LessonStep(
                    title = "Cuando falta un valor",
                    explanation =
                        listOf(
                            paragraph(regular("A veces un dato simplemente no existe: el usuario escribe texto donde se esperaba un número, o una búsqueda no encuentra nada. Ya lo has visto con "), inlineCode("toIntOrNull()"), regular(", que devuelve "), inlineCode("null"), regular(" si no puede convertir.")),
                            paragraph(inlineCode("null"), regular(" significa "), highlight("ausencia de valor"), regular(". No es el número "), inlineCode("0"), regular(" ni el texto vacío "), inlineCode("\"\""), regular(": un "), inlineCode("0"), regular(" es una respuesta, y "), inlineCode("null"), regular(" es la falta de respuesta.")),
                            paragraph(regular("En esta lección aprenderás a trabajar con valores que pueden faltar y a reaccionar cuando algo sale mal en mitad de la ejecución, sin que el programa se detenga de golpe.")),
                        ),
                    visual = LessonVisualContent.Flow(listOf("Dato que puede faltar", "null", "Comprobarlo", "El programa sigue")),
                ),
                LessonStep(
                    title = "Tipos que admiten nulo",
                    explanation =
                        listOf(
                            paragraph(regular("En Kotlin, un tipo normal como "), inlineCode("String"), regular(" nunca puede ser "), inlineCode("null"), regular(". Para permitirlo se añade un signo de interrogación al tipo: "), inlineCode("String?"), regular(" es un texto que puede faltar.")),
                            paragraph(regular("Por eso "), inlineCode("toIntOrNull()"), regular(" devuelve un "), inlineCode("Int?"), regular(": o un número, o "), inlineCode("null"), regular(". El compilador te obliga a decidir qué hacer con ese posible vacío antes de usarlo.")),
                            paragraph(regular("Así, muchos errores se detectan al compilar y no cuando el programa ya está en marcha. Estos son los dos errores más habituales.")),
                        ),
                    visual =
                        LessonVisualContent.CodeExamples(
                            listOf(
                                LessonCodeExample(
                                    title = "Dos errores de compilación",
                                    lines =
                                        kotlinCode(
                                            """
                                            fun main() {
                                                var nombre: String = "Lucía"
                                                nombre = null
                                                val texto: String? = readlnOrNull()
                                                println(texto.length)
                                            }
                                            """.trimIndent(),
                                        ),
                                    note = richText(regular("La línea 3 da "), inlineCode("Null cannot be a value of a non-null type 'String'"), regular(". La 5, "), inlineCode("Only safe (?.) or non-null asserted (!!.) calls are allowed on a nullable receiver of type 'String?'"), regular(".")),
                                ),
                            ),
                        ),
                ),
                LessonStep(
                    title = "Comprobar antes de usar",
                    explanation =
                        listOf(
                            paragraph(regular("La forma más directa de trabajar con un valor que puede ser nulo es comprobarlo con "), inlineCode("if"), regular(". Dentro del bloque donde ya sabes que "), inlineCode("edad != null"), regular(", Kotlin lo trata como un "), inlineCode("Int"), regular(" normal: es la "), highlight("conversión inteligente"), regular(".")),
                            paragraph(regular("Otra opción es descartar pronto el caso nulo: si es "), inlineCode("null"), regular(", muestras un aviso y terminas la función con "), inlineCode("return"), regular(". Desde ahí hasta el final, el valor ya es seguro.")),
                        ),
                    visual =
                        LessonVisualContent.CodeExamples(
                            listOf(
                                LessonCodeExample(
                                    title = "Comprobar con if",
                                    lines =
                                        kotlinCode(
                                            $$"""
                                                fun main() {
                                                    println("Edad:")
                                                    val edad = readln().toIntOrNull()
                                                    if (edad != null) {
                                                        println("Dentro de un año tendrás ${edad + 1}")
                                                    } else {
                                                        println("No es un número")
                                                    }
                                                }
                                            """.trimIndent(),
                                        ),
                                    note = richText(regular("Con "), inlineCode("17"), regular(" muestra "), inlineCode("Dentro de un año tendrás 18"), regular(". Con texto, "), inlineCode("No es un número"), regular(".")),
                                ),
                                LessonCodeExample(
                                    title = "Salir pronto con return",
                                    lines =
                                        kotlinCode(
                                            """
                                            fun mostrarDoble(texto: String) {
                                                val numero = texto.toIntOrNull()
                                                if (numero == null) {
                                                    println("No es un número")
                                                    return
                                                }
                                                println(numero * 2)
                                            }
                                            """.trimIndent(),
                                        ),
                                    note = richText(regular("Tras el "), inlineCode("if"), regular(", "), inlineCode("numero"), regular(" ya es un "), inlineCode("Int"), regular(" y se puede multiplicar sin más comprobaciones.")),
                                ),
                            ),
                        ),
                ),
                LessonStep(
                    title = "Llamada segura con ?.",
                    explanation =
                        listOf(
                            paragraph(regular("El operador "), inlineCode("?."), regular(" es la llamada segura: si el valor es "), inlineCode("null"), regular(", no ejecuta la operación y el resultado es "), inlineCode("null"), regular("; si no, funciona como el punto de siempre.")),
                            paragraph(regular("El resultado de "), inlineCode("a?.length"), regular(" es un "), inlineCode("Int?"), regular(", porque puede faltar. Puedes encadenar varias llamadas seguras: en cuanto una encuentra "), inlineCode("null"), regular(", todo lo que sigue se salta.")),
                        ),
                    visual =
                        LessonVisualContent.Trace(
                            example =
                                LessonCodeExample(
                                    title = "Llamadas seguras",
                                    lines =
                                        kotlinCode(
                                            """
                                            fun main() {
                                                val a: String? = "Lucía"
                                                val b: String? = null
                                                println(a?.length)
                                                println(b?.length)
                                                println(b?.trim()?.length)
                                            }
                                            """.trimIndent(),
                                        ),
                                    note = richText(regular("Muestra "), inlineCode("5"), regular(", "), inlineCode("null"), regular(" y "), inlineCode("null"), regular(". Ninguna línea detiene el programa.")),
                                ),
                            table =
                                LessonTraceTable(
                                    title = "Qué devuelve cada línea",
                                    columns = listOf("Línea", "Valor", "Resultado"),
                                    rows =
                                        listOf(
                                            listOf("4", "\"Lucía\"", "5"),
                                            listOf("5", "null", "null"),
                                            listOf("6", "null", "null"),
                                        ),
                                ),
                        ),
                ),
                LessonStep(
                    title = "Valor por defecto con ?:",
                    explanation =
                        listOf(
                            paragraph(regular("El operador "), inlineCode("?:"), regular(", llamado "), highlight("Elvis"), regular(" por su parecido con el peinado del cantante, da una alternativa: usa el valor de la izquierda si no es "), inlineCode("null"), regular(" y, si lo es, el de la derecha.")),
                            paragraph(regular("A la derecha puede ir un valor por defecto, como "), inlineCode("0"), regular(", o un "), inlineCode("return"), regular(" para salir de la función cuando falta el dato. Elige el valor por defecto solo si tiene sentido para el problema.")),
                        ),
                    visual =
                        LessonVisualContent.CodeExamples(
                            listOf(
                                LessonCodeExample(
                                    title = "Alternativa con Elvis",
                                    lines =
                                        kotlinCode(
                                            $$"""
                                                fun main() {
                                                    println("Horas de estudio:")
                                                    val horas = readln().toIntOrNull() ?: 0
                                                    println("Has estudiado $horas h")
                                                    println("Edad:")
                                                    val edad = readln().toIntOrNull() ?: return
                                                    println("Tienes $edad años")
                                                }
                                            """.trimIndent(),
                                        ),
                                    note = richText(regular("Si escribes texto en las horas, se usa "), inlineCode("0"), regular(". Si lo escribes en la edad, el programa termina sin llegar a la última línea.")),
                                ),
                            ),
                        ),
                ),
                LessonStep(
                    title = "El operador !! y sus riesgos",
                    explanation =
                        listOf(
                            paragraph(regular("El operador "), inlineCode("!!"), regular(" afirma: «sé que esto no es nulo». Kotlin te cree y no comprueba nada al compilar. Si te equivocas, el programa se detiene con una "), inlineCode("NullPointerException"), regular(".")),
                            explanationCode(
                                "Main.kt",
                                richText(regular("Si escribes "), inlineCode("hola"), regular(", la línea 3 falla, pero la traza no dice qué dato faltaba.")),
                                "fun main() {",
                                "    val minutos = readln().toIntOrNull()",
                                "    println(minutos!! + 5)",
                                "}",
                            ),
                            callout(
                                "Evítalo",
                                richText(regular("Casi siempre hay una alternativa mejor: "), inlineCode("if"), regular(", "), inlineCode("?."), regular(" o "), inlineCode("?:"), regular(". Con "), inlineCode("!!"), regular(" dejas el error sin gestionar.")),
                                LessonCalloutTone.Warning,
                            ),
                        ),
                    visual =
                        LessonVisualContent.Console(
                            title = "Consola de IntelliJ IDEA",
                            runs =
                                listOf(
                                    consoleRun(
                                        consoleInput("hola"),
                                        consoleError("Exception in thread \"main\" java.lang.NullPointerException"),
                                        consoleError("    at MainKt.main(Main.kt:3)"),
                                        consoleError("    at MainKt.main(Main.kt)"),
                                        consoleOutput(""),
                                        consoleOutput("Process finished with exit code 1"),
                                    ),
                                ),
                        ),
                ),
                LessonStep(
                    title = "Funciones que pueden no tener respuesta",
                    explanation =
                        listOf(
                            paragraph(regular("Una función también puede devolver un tipo que admite nulo. Si no puede dar una respuesta válida, devuelve "), inlineCode("null"), regular(" y deja que quien la llama decida qué hacer.")),
                            paragraph(regular("Aquí "), inlineCode("convertirHora"), regular(" devuelve "), inlineCode("Int?"), regular(": el número si es una hora entre 0 y 23, y "), inlineCode("null"), regular(" en cualquier otro caso. La condición "), inlineCode("hora == null ||"), regular(" va primero para que las comparaciones siguientes ya vean un "), inlineCode("Int"), regular(".")),
                        ),
                    visual =
                        LessonVisualContent.Trace(
                            example =
                                LessonCodeExample(
                                    title = "Convertir una hora",
                                    lines =
                                        kotlinCode(
                                            """
                                            fun convertirHora(texto: String): Int? {
                                                val hora = texto.toIntOrNull()
                                                if (hora == null || hora < 0 || hora > 23) {
                                                    return null
                                                }
                                                return hora
                                            }
                                            """.trimIndent(),
                                        ),
                                    note = richText(regular("El tipo de retorno "), inlineCode("Int?"), regular(" avisa a quien llama de que puede no haber resultado.")),
                                ),
                            table =
                                LessonTraceTable(
                                    title = "Qué devuelve",
                                    columns = listOf("texto", "hora", "Devuelve"),
                                    rows =
                                        listOf(
                                            listOf("\"9\"", "9", "9"),
                                            listOf("\"23\"", "23", "23"),
                                            listOf("\"24\"", "24", "null"),
                                            listOf("\"tarde\"", "null", "null"),
                                        ),
                                ),
                        ),
                ),
                LessonStep(
                    title = "Qué es una excepción",
                    explanation =
                        listOf(
                            paragraph(regular("Una "), highlight("excepción"), regular(" es un aviso de que algo ha salido mal. Se "), emphasis("lanza"), regular(" donde ocurre el fallo y sube de función en función; si nadie la gestiona, el programa se detiene.")),
                            explanationCode(
                                "Main.kt",
                                richText(inlineCode("repartir"), regular(" falla en la línea 10, llamada desde la 3.")),
                                "fun main() {",
                                "    println(\"Reparto:\")",
                                "    println(repartir(30, 0))",
                                "}",
                                "",
                                "fun repartir(",
                                "    total: Int,",
                                "    personas: Int,",
                                "): Int {",
                                "    return total / personas",
                                "}",
                            ),
                        ),
                    visual =
                        LessonVisualContent.Console(
                            title = "Consola de IntelliJ IDEA",
                            runs =
                                listOf(
                                    consoleRun(
                                        consoleOutput("Reparto:"),
                                        consoleError("Exception in thread \"main\" java.lang.ArithmeticException: / by zero"),
                                        consoleError("    at MainKt.repartir(Main.kt:10)"),
                                        consoleError("    at MainKt.main(Main.kt:3)"),
                                        consoleError("    at MainKt.main(Main.kt)"),
                                        consoleOutput(""),
                                        consoleOutput("Process finished with exit code 1"),
                                    ),
                                ),
                        ),
                ),
                LessonStep(
                    title = "Tipos de excepciones habituales",
                    explanation =
                        listOf(
                            paragraph(regular("Cada excepción tiene un tipo que dice qué ha pasado. Estos son los que más verás al empezar; el mensaje que sigue al tipo, cuando existe, da el detalle.")),
                            steps(
                                richText(inlineCode("NumberFormatException"), regular(": un texto que no se puede convertir en número.")),
                                richText(inlineCode("ArithmeticException"), regular(": una operación imposible, como dividir enteros entre cero.")),
                                richText(inlineCode("NullPointerException"), regular(": usar "), inlineCode("!!"), regular(" sobre un valor nulo.")),
                                richText(inlineCode("IllegalArgumentException"), regular(": una función recibe un argumento que no acepta.")),
                            ),
                            paragraph(regular("Un "), inlineCode("Error"), regular(", como "), inlineCode("StackOverflowError"), regular(", indica un fallo grave que no se intenta gestionar.")),
                        ),
                    visual =
                        LessonVisualContent.Console(
                            title = "Primera línea de cada traza",
                            runs =
                                listOf(
                                    consoleRun(
                                        consoleError("java.lang.NumberFormatException: For input string: \"hola\""),
                                        consoleError("java.lang.ArithmeticException: / by zero"),
                                        consoleError("java.lang.NullPointerException"),
                                        consoleError("java.lang.IllegalArgumentException: La nota debe estar entre 0 y 10"),
                                    ),
                                ),
                        ),
                ),
                LessonStep(
                    title = "Capturar con try y catch",
                    explanation =
                        listOf(
                            paragraph(regular("Para gestionar una excepción, pon el código que puede fallar dentro de "), inlineCode("try"), regular(" y, en "), inlineCode("catch"), regular(", indica qué tipo capturas y qué hacer. Si todo va bien, el "), inlineCode("catch"), regular(" se ignora; si falla, se salta el resto del "), inlineCode("try"), regular(" y el programa continúa después.")),
                            paragraph(regular("Captura el tipo "), emphasis("concreto"), regular(" que esperas, no uno genérico. "), inlineCode("e"), regular(" es la excepción capturada y "), inlineCode("e.message"), regular(" su mensaje, que puede faltar: es un "), inlineCode("String?"), regular(".")),
                        ),
                    visual =
                        LessonVisualContent.Trace(
                            example =
                                LessonCodeExample(
                                    title = "Capturar un número mal escrito",
                                    lines =
                                        kotlinCode(
                                            $$"""
                                                fun main() {
                                                    println("Edad:")
                                                    try {
                                                        val edad = readln().toInt()
                                                        println("Dentro de un año tendrás ${edad + 1}")
                                                    } catch (e: NumberFormatException) {
                                                        println("Eso no es un número: ${e.message}")
                                                    }
                                                    println("Fin")
                                                }
                                            """.trimIndent(),
                                        ),
                                    note = richText(regular("Con "), inlineCode("hola"), regular(", "), inlineCode("toInt()"), regular(" lanza la excepción en la línea 4 y la línea 5 no se ejecuta.")),
                                ),
                            table =
                                LessonTraceTable(
                                    title = "Dos ejecuciones",
                                    columns = listOf("Dato", "¿catch?", "Salida"),
                                    rows =
                                        listOf(
                                            listOf("17", "No", "Dentro de un año tendrás 18"),
                                            listOf("hola", "Sí", "Eso no es un número: For input string: \"hola\""),
                                        ),
                                ),
                        ),
                ),
                LessonStep(
                    title = "Un try que devuelve valor y finally",
                    explanation =
                        listOf(
                            paragraph(inlineCode("try"), regular(" también es una expresión: su valor es el de la última línea del bloque que se ejecute. Así puedes devolver un resultado normal o uno alternativo desde el "), inlineCode("catch"), regular(".")),
                            paragraph(regular("El bloque "), inlineCode("finally"), regular(" es opcional y se ejecuta "), highlight("siempre"), regular(", haya o no excepción. Sirve para tareas que deben hacerse en cualquier caso, como cerrar un archivo.")),
                        ),
                    visual =
                        LessonVisualContent.CodeExamples(
                            listOf(
                                LessonCodeExample(
                                    title = "try como expresión",
                                    lines =
                                        kotlinCode(
                                            """
                                            fun main() {
                                                println(dividir(10, 2))
                                                println(dividir(10, 0))
                                            }

                                            fun dividir(
                                                a: Int,
                                                b: Int,
                                            ): Int {
                                                return try {
                                                    a / b
                                                } catch (e: ArithmeticException) {
                                                    0
                                                } finally {
                                                    println("Operación terminada")
                                                }
                                            }
                                            """.trimIndent(),
                                        ),
                                    note = richText(regular("Muestra "), inlineCode("Operación terminada"), regular(", "), inlineCode("5"), regular(", "), inlineCode("Operación terminada"), regular(" y "), inlineCode("0"), regular(": el "), inlineCode("finally"), regular(" se ejecuta antes de devolver.")),
                                ),
                            ),
                        ),
                ),
                LessonStep(
                    title = "Lanzar tus propias excepciones",
                    explanation =
                        listOf(
                            paragraph(regular("Una función también puede lanzar excepciones. Si recibe un argumento que no acepta, es mejor avisar enseguida y con un mensaje claro. Se hace con "), inlineCode("throw"), regular(".")),
                            paragraph(regular("Para validar argumentos, "), inlineCode("require"), regular(" lanza una "), inlineCode("IllegalArgumentException"), regular(" si la condición es falsa. Lo que va entre llaves es el mensaje; esa sintaxis la verás con la programación funcional.")),
                        ),
                    visual =
                        LessonVisualContent.CodeExamples(
                            listOf(
                                LessonCodeExample(
                                    title = "Validar con require",
                                    lines =
                                        kotlinCode(
                                            """
                                            fun calificar(nota: Int): String {
                                                require(nota >= 0 && nota <= 10) { "La nota debe estar entre 0 y 10" }
                                                if (nota < 5) {
                                                    return "Suspenso"
                                                }
                                                if (nota < 7) {
                                                    return "Aprobado"
                                                }
                                                if (nota < 9) {
                                                    return "Notable"
                                                }
                                                return "Sobresaliente"
                                            }
                                            """.trimIndent(),
                                        ),
                                    note = richText(regular("Con "), inlineCode("calificar(11)"), regular(" el programa se detiene con "), inlineCode("IllegalArgumentException: La nota debe estar entre 0 y 10"), regular(".")),
                                ),
                                LessonCodeExample(
                                    title = "Lo mismo con throw",
                                    lines =
                                        kotlinCode(
                                            """
                                            if (nota < 0 || nota > 10) {
                                                throw IllegalArgumentException("La nota debe estar entre 0 y 10")
                                            }
                                            """.trimIndent(),
                                        ),
                                    note = richText(inlineCode("require"), regular(" hace exactamente esto en una sola línea.")),
                                ),
                            ),
                        ),
                ),
                LessonStep(
                    title = "¿Nulo o excepción?",
                    explanation =
                        listOf(
                            paragraph(regular("Usa "), inlineCode("null"), regular(" cuando que falte el dato es algo normal y quien llama puede seguir sin él: un texto que no es número, una búsqueda sin resultado. "), inlineCode("toIntOrNull()"), regular(" es el ejemplo.")),
                            paragraph(regular("Usa una "), emphasis("excepción"), regular(" cuando alguien usa mal una función y no hay forma razonable de continuar: pedir una nota de 11 o repartir entre cero personas. "), inlineCode("toInt()"), regular(" es el ejemplo.")),
                            paragraph(regular("Y no captures excepciones para esconderlas. Un "), inlineCode("catch"), regular(" vacío oculta el error y deja un programa que parece funcionar pero da resultados falsos.")),
                        ),
                    visual = LessonVisualContent.Flow(listOf("¿Es normal que falte?", "Sí: devolver null", "No: lanzar excepción", "Quien llama decide")),
                ),
                LessonStep(
                    title = "Probar nulos y excepciones",
                    explanation =
                        listOf(
                            paragraph(regular("Los fallos también se prueban. Para un resultado nulo existe "), inlineCode("assertNull"), regular("; para un valor que admite nulo, "), inlineCode("assertEquals"), regular(" funciona igual.")),
                            paragraph(inlineCode("assertFailsWith"), regular(" comprueba que el código de las llaves lanza la excepción indicada; si no la lanza, la prueba falla.")),
                        ),
                    visual =
                        LessonVisualContent.CodeExamples(
                            listOf(
                                LessonCodeExample(
                                    title = "src/test/kotlin/NotasTest.kt",
                                    lines =
                                        kotlinCode(
                                            """
                                            import kotlin.test.Test
                                            import kotlin.test.assertEquals
                                            import kotlin.test.assertFailsWith
                                            import kotlin.test.assertNull

                                            class NotasTest {
                                                @Test
                                                fun `una nota de 7 es notable`() {
                                                    assertEquals("Notable", calificar(7))
                                                }

                                                @Test
                                                fun `una nota de 11 lanza una excepcion`() {
                                                    assertFailsWith<IllegalArgumentException> { calificar(11) }
                                                }

                                                @Test
                                                fun `una hora de 24 no es valida`() {
                                                    assertNull(convertirHora("24"))
                                                }

                                                @Test
                                                fun `una hora de 9 es valida`() {
                                                    assertEquals(9, convertirHora("9"))
                                                }
                                            }
                                            """.trimIndent(),
                                        ),
                                    note = richText(regular("Las cuatro pruebas pasan con las funciones de esta lección. Lo que va entre "), inlineCode("<>"), regular(" es el tipo de excepción que se espera.")),
                                ),
                            ),
                        ),
                ),
                LessonStep(
                    title = "Ahora te toca: repartir la cuenta",
                    explanation =
                        listOf(
                            paragraph(regular("Este programa reparte una cuenta entre varias personas. Con "), inlineCode("60"), regular(" euros y "), inlineCode("4"), regular(" personas funciona, pero se rompe de dos maneras distintas: si escribes texto en lugar de un número y si escribes "), inlineCode("0"), regular(" personas.")),
                            paragraph(regular("Pruébalo con esos datos y lee cada traza antes de tocar nada. Luego haz que el programa sea "), highlight("robusto"), regular(": que avise de lo que ocurre y no se detenga con una excepción.")),
                        ),
                    visual =
                        LessonVisualContent.CodeExamples(
                            listOf(
                                LessonCodeExample(
                                    title = "Repartir la cuenta",
                                    lines =
                                        kotlinCode(
                                            $$"""
                                                fun main() {
                                                    println("Total de la cuenta:")
                                                    val total = readln().toIntOrNull()!!
                                                    println("Número de personas:")
                                                    val personas = readln().toIntOrNull()!!
                                                    println("Cada persona paga ${repartir(total, personas)} €")
                                                }

                                                fun repartir(
                                                    total: Int,
                                                    personas: Int,
                                                ): Int {
                                                    return total / personas
                                                }
                                            """.trimIndent(),
                                        ),
                                    note = richText(regular("Con texto lanza una "), inlineCode("NullPointerException"), regular(" y con cero personas, una "), inlineCode("ArithmeticException"), regular(".")),
                                    isCopyable = true,
                                ),
                            ),
                        ),
                    links =
                        listOf(
                            LessonLink("Seguridad frente a nulos en Kotlin", "https://kotlinlang.org/docs/null-safety.html"),
                            LessonLink("Excepciones en Kotlin", "https://kotlinlang.org/docs/exceptions.html"),
                            LessonLink("Referencia de require y check", "https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/require.html"),
                            LessonLink("Referencia de kotlin.test", "https://kotlinlang.org/api/core/kotlin-test/"),
                        ),
                    practice =
                        LessonPractice(
                            id = "nullability-exceptions-split-bill",
                            objective = richText(highlight("Gestionar datos ausentes y errores previsibles para que un programa no se detenga de golpe.")),
                            statement = richText(regular("Reescribe el programa para que no use "), inlineCode("!!"), regular(" y responda con claridad a los dos fallos, y comprueba el resultado con pruebas.")),
                            requirements =
                                listOf(
                                    richText(regular("Crea "), inlineCode("leerEntero(mensaje: String): Int"), regular(", que muestre el mensaje y repita la lectura con "), inlineCode("toIntOrNull"), regular(" hasta recibir un número. Ya no queda ningún "), inlineCode("!!"), regular(".")),
                                    richText(inlineCode("repartir"), regular(" valida con "), inlineCode("require"), regular(" que haya al menos una persona, con un mensaje claro.")),
                                    richText(inlineCode("main"), regular(" captura "), inlineCode("IllegalArgumentException"), regular(" y muestra "), inlineCode("No se puede repartir:"), regular(" seguido del mensaje, sin detenerse con una traza.")),
                                    richText(regular("Crea "), inlineCode("RepartirTest"), regular(" con una prueba de un reparto correcto y otra con "), inlineCode("assertFailsWith"), regular(" para cero personas.")),
                                ),
                            expectedOutput =
                                listOf(
                                    consoleRun(
                                        consoleOutput("Total de la cuenta:"),
                                        consoleInput("60"),
                                        consoleOutput("Número de personas:"),
                                        consoleInput("4"),
                                        consoleOutput("Cada persona paga 15 €"),
                                    ),
                                    consoleRun(
                                        consoleOutput("Total de la cuenta:"),
                                        consoleInput("sesenta"),
                                        consoleOutput("Escribe un número"),
                                        consoleInput("60"),
                                        consoleOutput("Número de personas:"),
                                        consoleInput("0"),
                                        consoleOutput("No se puede repartir: Debe haber al menos una persona"),
                                    ),
                                ),
                            hints =
                                listOf(
                                    richText(regular("Empieza por "), inlineCode("leerEntero"), regular(": es el mismo bucle de lectura que ya has escrito, ahora dentro de una función que termina con "), inlineCode("return"), regular(".")),
                                    richText(regular("Pon "), inlineCode("require(personas > 0) { \"…\" }"), regular(" como primera línea de "), inlineCode("repartir"), regular(". El texto entre comillas es lo que luego mostrará "), inlineCode("e.message"), regular(".")),
                                    richText(regular("Rodea solo la llamada a "), inlineCode("repartir"), regular(" con "), inlineCode("try"), regular(", no todo "), inlineCode("main"), regular(". Las lecturas ya se protegen solas con el bucle.")),
                                ),
                            extension = richText(regular("Haz que "), inlineCode("leerEntero"), regular(" reciba un "), inlineCode("minimo"), regular(" y repita también si el número es menor. Si pides las personas con mínimo "), inlineCode("1"), regular(", ¿sigue haciendo falta el "), inlineCode("try"), regular("? ¿Y el "), inlineCode("require"), regular("?")),
                            confirmation = richText(regular("Confirma que el programa ya no usa "), inlineCode("!!"), regular(", que las dos ejecuciones dan la salida esperada y que "), inlineCode("./gradlew test"), regular(" termina con "), inlineCode("BUILD SUCCESSFUL"), regular(".")),
                        ),
                ),
            ),
    )
