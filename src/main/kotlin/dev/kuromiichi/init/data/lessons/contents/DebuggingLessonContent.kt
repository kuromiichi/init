package dev.kuromiichi.init.data.lessons.contents

import dev.kuromiichi.init.data.lessons.LessonCodeExample
import dev.kuromiichi.init.data.lessons.LessonConsoleKind
import dev.kuromiichi.init.data.lessons.LessonContent
import dev.kuromiichi.init.data.lessons.LessonLink
import dev.kuromiichi.init.data.lessons.LessonPractice
import dev.kuromiichi.init.data.lessons.LessonScreenshot
import dev.kuromiichi.init.data.lessons.LessonStep
import dev.kuromiichi.init.data.lessons.LessonVisualContent
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
import dev.kuromiichi.init.data.lessons.steps

val debuggingLessonContent =
    LessonContent(
        lessonId = "debugging",
        steps =
            listOf(
                LessonStep(
                    title = "Un método para los errores",
                    explanation =
                        listOf(
                            paragraph(regular("Ya sabes que hay errores de sintaxis, de ejecución y lógicos. Ahora toca aprender a encontrarlos sin cambiar cosas al azar.")),
                            paragraph(regular("Primero "), emphasis("reproduce"), regular(" el fallo: averigua con qué datos ocurre. Después "), emphasis("localiza"), regular(" la línea, "), emphasis("entiende"), regular(" por qué falla y "), emphasis("corrige"), regular(" solo eso.")),
                            paragraph(regular("Por último, "), emphasis("comprueba"), regular(" con los mismos datos que el fallo ha desaparecido y que lo demás sigue funcionando.")),
                        ),
                    visual = LessonVisualContent.Flow(listOf("Reproducir", "Localizar", "Entender y corregir", "Comprobar")),
                ),
                LessonStep(
                    title = "Errores de compilación",
                    explanation =
                        listOf(
                            paragraph(regular("IntelliJ IDEA subraya en rojo lo que no compila antes de que ejecutes. Si pasas el ratón por encima, muestra el mensaje; al compilar, la ventana "), emphasis("Build"), regular(" los lista con su línea.")),
                            paragraph(regular("Los mensajes están en inglés, pero se repiten. "), inlineCode("Unresolved reference"), regular(" indica un nombre que no existe, muchas veces por una errata. "), inlineCode("type mismatch"), regular(" indica que el tipo no es el esperado.")),
                            paragraph(regular("Corrige siempre el "), highlight("primer error"), regular(": a veces provoca los siguientes, como un paréntesis sin cerrar.")),
                        ),
                    visual =
                        LessonVisualContent.CodeExamples(
                            listOf(
                                LessonCodeExample(
                                    title = "Dos errores subrayados",
                                    lines =
                                        kotlinCode(
                                            """
                                            ⟦prinln⟧("Hola")
                                            val edad: Int = ⟦"17"⟧
                                            """.trimIndent(),
                                        ),
                                    note =
                                        richText(
                                            regular("Línea 1: "),
                                            inlineCode("Unresolved reference 'prinln'"),
                                            regular(". Línea 2: "),
                                            inlineCode("Initializer type mismatch: expected 'Int', actual 'String'"),
                                            regular("."),
                                        ),
                                ),
                            ),
                        ),
                ),
                LessonStep(
                    title = "Leer la traza de un error",
                    explanation =
                        listOf(
                            explanationCode(
                                "Main.kt",
                                richText(regular("La línea 3 intenta convertir "), inlineCode("\"hola\""), regular(" en un número.")),
                                "fun main() {",
                                "    val texto = \"hola\"",
                                "    val minutos = texto.toInt()",
                                "    println(minutos + 5)",
                                "}",
                            ),
                            paragraph(regular("Un error de ejecución detiene el programa y muestra una "), highlight("traza"), regular(". La primera línea dice qué ha pasado: el tipo de error, "), inlineCode("NumberFormatException"), regular(", y el dato que falló.")),
                            paragraph(regular("Las líneas con "), inlineCode("at"), regular(" indican dónde. Salta las de "), inlineCode("java.base"), regular(", que son de Java, y busca la primera con tu archivo: "), inlineCode("Main.kt:3"), regular(" es la línea 3. En IntelliJ IDEA puedes hacer clic en ella.")),
                        ),
                    visual =
                        LessonVisualContent.Console(
                            kind = LessonConsoleKind.IntelliJRun,
                            runs =
                                listOf(
                                    consoleRun(
                                        consoleError("Exception in thread \"main\" java.lang.NumberFormatException: For input string: \"hola\""),
                                        consoleError("    at java.base/java.lang.NumberFormatException.forInputString(NumberFormatException.java:67)"),
                                        consoleError("    at java.base/java.lang.Integer.parseInt(Integer.java:565)"),
                                        consoleError("    at java.base/java.lang.Integer.parseInt(Integer.java:662)"),
                                        consoleError("    at MainKt.main(Main.kt:3)"),
                                        consoleError("    at MainKt.main(Main.kt)"),
                                        consoleOutput(""),
                                        consoleOutput("Process finished with exit code 1"),
                                    ),
                                ),
                        ),
                ),
                LessonStep(
                    title = "Errores sin mensaje",
                    explanation =
                        listOf(
                            paragraph(regular("Un error lógico no avisa: el programa termina con normalidad, pero el resultado no es el esperado. Para detectarlo tienes que saber de antemano qué debería salir.")),
                            paragraph(regular("Aquí la media de "), inlineCode("6"), regular(" y "), inlineCode("9"), regular(" debería dar "), inlineCode("7"), regular(" con enteros, pero sale "), inlineCode("10"), regular(". Una hipótesis: como en matemáticas, la división se calcula antes que la suma.")),
                            paragraph(regular("Los paréntesis fijan el orden: en el ejemplo, "), inlineCode("(nota1 + nota2) / 2"), regular(" suma primero y después divide.")),
                        ),
                    visual =
                        LessonVisualContent.CodeExamples(
                            listOf(
                                LessonCodeExample(
                                    title = "Una media que no cuadra",
                                    lines =
                                        kotlinCode(
                                            """
                                            val nota1 = 6
                                            val nota2 = 9
                                            val media = nota1 + nota2 / 2
                                            println(media)
                                            """.trimIndent(),
                                        ),
                                    note = richText(regular("Muestra "), inlineCode("10"), regular(" porque primero hace "), inlineCode("9 / 2"), regular(", que da "), inlineCode("4"), regular(". Con paréntesis muestra "), inlineCode("7"), regular(".")),
                                ),
                            ),
                        ),
                ),
                LessonStep(
                    title = "Preguntar al programa con println",
                    explanation =
                        listOf(
                            paragraph(regular("La forma más sencilla de investigar es mostrar valores intermedios. Este bucle debería sumar del 1 al 5, que da "), inlineCode("15"), regular(", pero muestra "), inlineCode("10"), regular(".")),
                            paragraph(regular("El "), inlineCode("println"), regular(" temporal revela que "), inlineCode("i"), regular(" nunca llega a "), inlineCode("5"), regular(": el rango debería ser "), inlineCode("1..5"), regular(".")),
                            paragraph(regular("Marca estas líneas con un comentario y bórralas al terminar. Si necesitas muchas, es mejor usar el depurador.")),
                        ),
                    visual =
                        LessonVisualContent.CodeExamples(
                            listOf(
                                LessonCodeExample(
                                    title = "Una suma que se queda corta",
                                    lines =
                                        kotlinCode(
                                            $$"""
                                                var total = 0
                                                for (i in 1..<5) {
                                                    total += i
                                                    // Temporal
                                                    println("i=$i total=$total")
                                                }
                                                println(total)
                                            """.trimIndent(),
                                        ),
                                    note = richText(regular("La última línea temporal es "), inlineCode("i=4 total=10"), regular(": falta la vuelta con "), inlineCode("5"), regular(".")),
                                ),
                            ),
                        ),
                ),
                LessonStep(
                    title = "Detenerse en un breakpoint",
                    explanation =
                        listOf(
                            paragraph(regular("Haz clic junto al número de línea para poner un "), highlight("breakpoint"), regular(" y lanza el programa con "), emphasis("Debug"), regular(", el icono del insecto junto al triángulo verde.")),
                            paragraph(regular("El programa se detiene "), emphasis("antes"), regular(" de ejecutar la línea marcada, que queda resaltada. Junto al código aparecen en gris los valores actuales: en la tercera vuelta, "), inlineCode("suma"), regular(" aún vale "), inlineCode("3"), regular(".")),
                            paragraph(regular("Los signos "), inlineCode("≤"), regular(" grises del rango los añade IntelliJ IDEA para recordarte que incluye los dos extremos; no forman parte del código.")),
                        ),
                    visual =
                        LessonVisualContent.Screenshot(
                            image = LessonScreenshot.IntellijDebuggerPaused,
                            description = "Editor de IntelliJ IDEA detenido en la línea 4, dentro del bucle, con los valores suma: 3 e i: 3 en gris junto al código.",
                        ),
                ),
                LessonStep(
                    title = "Mirar las variables y avanzar",
                    explanation =
                        listOf(
                            paragraph(regular("Abajo se abre la ventana "), emphasis("Debug"), regular(". En "), emphasis("Threads & Variables"), regular(" ves dónde está parado el programa, "), inlineCode("main:4"), regular(", y el valor de cada variable. En "), emphasis("Console"), regular(" está la salida, y ahí escribes si el programa usa "), inlineCode("readln()"), regular(".")),
                            steps(
                                richText(emphasis("Step Over"), regular(", la flecha curva, ejecuta la línea resaltada y se detiene en la siguiente.")),
                                richText(emphasis("Resume Program"), regular(", el triángulo con una barra, continúa hasta el siguiente breakpoint.")),
                                richText(emphasis("Stop"), regular(", el cuadrado rojo, termina el programa.")),
                            ),
                        ),
                    visual =
                        LessonVisualContent.Screenshot(
                            image = LessonScreenshot.IntellijDebuggerPanel,
                            description = "Ventana Debug de IntelliJ IDEA con la barra de botones del depurador y la pestaña Threads & Variables, que muestra suma = 3 e i = 3.",
                        ),
                ),
                LessonStep(
                    title = "Depurar un bucle",
                    explanation =
                        listOf(
                            paragraph(regular("Un breakpoint dentro de un bucle detiene el programa en cada vuelta. Cada "), emphasis("Resume Program"), regular(" avanza una vuelta y los valores se actualizan: el IDE rellena por ti la tabla de traza.")),
                            paragraph(regular("Con cien vueltas, pararse en todas es lento. Un "), highlight("breakpoint condicional"), regular(" solo se detiene cuando su condición es verdadera.")),
                            paragraph(regular("Haz clic derecho sobre el punto rojo y escribe la condición en la ventana que aparece, por ejemplo "), inlineCode("i == 50"), regular(".")),
                        ),
                    visual =
                        LessonVisualContent.CodeExamples(
                            listOf(
                                LessonCodeExample(
                                    title = "Sumar del 1 al 100",
                                    lines =
                                        kotlinCode(
                                            """
                                            var suma = 0
                                            for (i in 1..100) {
                                                suma += i
                                            }
                                            println(suma)
                                            """.trimIndent(),
                                        ),
                                    note = richText(regular("Con la condición "), inlineCode("i == 50"), regular(" en la línea 3, el programa se detiene una sola vez, con "), inlineCode("suma"), regular(" en "), inlineCode("1225"), regular(". Al final muestra "), inlineCode("5050"), regular(".")),
                                ),
                            ),
                        ),
                ),
                LessonStep(
                    title = "Ahora te toca: encontrar los fallos",
                    explanation =
                        listOf(
                            paragraph(regular("Este programa debería leer cinco notas y contar cuántas son 5 o más, pero tiene dos errores lógicos.")),
                            paragraph(regular("Copia el código en un proyecto, ejecútalo con los datos del resultado esperado y compara. Después busca la causa con el depurador, no a ojo.")),
                            paragraph(regular("Mientras depuras, escribe las notas en la pestaña "), emphasis("Console"), regular(" de la ventana "), emphasis("Debug"), regular(".")),
                        ),
                    visual =
                        LessonVisualContent.CodeExamples(
                            listOf(
                                LessonCodeExample(
                                    title = "Contar aprobados",
                                    lines =
                                        kotlinCode(
                                            $$"""
                                                fun main() {
                                                    var aprobados = 0
                                                    for (i in 1..<5) {
                                                        val nota = readln().toInt()
                                                        if (nota > 5) {
                                                            aprobados++
                                                        }
                                                    }
                                                    println("Aprobados: $aprobados")
                                                }
                                            """.trimIndent(),
                                        ),
                                    note = richText(regular("Compila y se ejecuta, pero no cuenta bien.")),
                                    isCopyable = true,
                                ),
                            ),
                        ),
                    links =
                        listOf(
                            LessonLink("Depurar código en IntelliJ IDEA", "https://www.jetbrains.com/help/idea/debugging-code.html"),
                            LessonLink("Breakpoints en IntelliJ IDEA", "https://www.jetbrains.com/help/idea/using-breakpoints.html"),
                            LessonLink("Avanzar paso a paso en IntelliJ IDEA", "https://www.jetbrains.com/help/idea/stepping-through-the-program.html"),
                        ),
                    practice =
                        LessonPractice(
                            id = "debugging-grades",
                            objective = richText(highlight("Encontrar y corregir errores lógicos con el depurador.")),
                            statement = richText(regular("Corrige el programa de ejemplo para que cuente bien los aprobados. Solo hay que cambiar dos detalles.")),
                            requirements =
                                listOf(
                                    richText(regular("Antes de tocar el código, ejecútalo con los datos de ejemplo y anota qué resultado da y cuál esperabas.")),
                                    richText(regular("Pon un breakpoint dentro del bucle y avanza con "), emphasis("Resume Program"), regular(" observando "), inlineCode("i"), regular(" y "), inlineCode("nota"), regular(" en cada vuelta.")),
                                    richText(regular("Corrige un error cada vez y vuelve a ejecutar para comprobarlo.")),
                                    richText(regular("Borra los "), inlineCode("println"), regular(" temporales que hayas añadido.")),
                                ),
                            expectedOutput =
                                listOf(
                                    consoleRun(
                                        consoleInput("5"),
                                        consoleInput("7"),
                                        consoleInput("3"),
                                        consoleInput("9"),
                                        consoleInput("5"),
                                        consoleOutput("Aprobados: 4"),
                                    ),
                                    consoleRun(
                                        consoleInput("5"),
                                        consoleInput("5"),
                                        consoleInput("5"),
                                        consoleInput("5"),
                                        consoleInput("5"),
                                        consoleOutput("Aprobados: 5"),
                                    ),
                                ),
                            hints =
                                listOf(
                                    richText(regular("Cuenta cuántas veces se detiene el breakpoint: ¿llega el programa a pedir la quinta nota?")),
                                    richText(regular("Fíjate en qué ocurre cuando la nota es exactamente "), inlineCode("5"), regular(".")),
                                    richText(regular("Revisa el rango del "), inlineCode("for"), regular(" y el operador de la comparación.")),
                                ),
                            extension = richText(regular("Pon en la línea del "), inlineCode("if"), regular(" un breakpoint condicional que solo se detenga con los suspensos, con la condición "), inlineCode("nota < 5"), regular(".")),
                            confirmation = richText(regular("Confirma que las dos ejecuciones de ejemplo muestran lo esperado sin cambiar el resto del programa.")),
                        ),
                ),
            ),
    )
