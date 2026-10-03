package dev.kuromiichi.damprep.data.lessons.contents

import dev.kuromiichi.damprep.data.lessons.LessonCalloutTone
import dev.kuromiichi.damprep.data.lessons.LessonCodeExample
import dev.kuromiichi.damprep.data.lessons.LessonContent
import dev.kuromiichi.damprep.data.lessons.LessonLink
import dev.kuromiichi.damprep.data.lessons.LessonPractice
import dev.kuromiichi.damprep.data.lessons.LessonScreenshot
import dev.kuromiichi.damprep.data.lessons.LessonStep
import dev.kuromiichi.damprep.data.lessons.LessonStepContent
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
import dev.kuromiichi.damprep.data.lessons.operatingSystemStep
import dev.kuromiichi.damprep.data.lessons.paragraph
import dev.kuromiichi.damprep.data.lessons.regular
import dev.kuromiichi.damprep.data.lessons.richText
import dev.kuromiichi.damprep.data.lessons.steps
import dev.kuromiichi.damprep.data.preferences.TargetOperatingSystem

private val calificarSpecification =
    kotlinCode(
        """
        /**
         * Califica una nota:
         * 0-4 Suspenso, 5-6 Aprobado,
         * 7-8 Notable,
         * 9-10 Sobresaliente.
         * Otra nota: "Nota no válida".
         */
        fun calificar(nota: Int): String
        """.trimIndent(),
    )

val basicTestingLessonContent =
    LessonContent(
        lessonId = "basic-testing",
        steps =
            listOf(
                LessonStep(
                    title = "Comprobar que el programa hace lo que debe",
                    explanation =
                        listOf(
                            paragraph(regular("Que un programa compile y se ejecute no significa que funcione. "), highlight("Probar"), regular(" es ejecutarlo con datos elegidos a propósito y comparar lo que hace con lo que debería hacer.")),
                            paragraph(regular("Probar y depurar son tareas distintas: las pruebas "), emphasis("descubren"), regular(" que hay un fallo; la depuración "), emphasis("localiza"), regular(" su causa para corregirlo. Después, se vuelve a probar.")),
                            paragraph(regular("Las pruebas demuestran que hay errores, nunca que no los hay: siempre quedan datos sin probar. Por eso conviene elegir bien los casos, como verás en esta lección.")),
                        ),
                    visual = LessonVisualContent.Flow(listOf("Probar", "Descubrir un fallo", "Depurar y corregir", "Volver a probar")),
                ),
                LessonStep(
                    title = "De una función al programa entero",
                    explanation =
                        listOf(
                            paragraph(regular("Las pruebas se organizan en niveles. Las "), highlight("unitarias"), regular(" comprueban una pieza pequeña, normalmente una función, sin el resto del programa. Son las que escribirás en esta lección.")),
                            paragraph(regular("Las de "), highlight("integración"), regular(" comprueban que varias piezas funcionan juntas; las de "), highlight("sistema"), regular(", el programa completo; y las de "), highlight("aceptación"), regular(", que hace lo que pidió quien lo encargó.")),
                            paragraph(regular("Puedes probar a mano, ejecutando y mirando la consola, pero es lento y fácil de olvidar. Las pruebas "), emphasis("automáticas"), regular(" son código que comprueba tu código y se repiten en segundos tras cada cambio.")),
                        ),
                    visual = LessonVisualContent.Flow(listOf("Unitarias", "Integración", "Sistema", "Aceptación")),
                ),
                LessonStep(
                    title = "Diseñar un caso de prueba",
                    explanation =
                        listOf(
                            paragraph(regular("Un "), highlight("caso de prueba"), regular(" indica qué datos de entrada usar y qué "), emphasis("resultado esperado"), regular(" deben producir. Ese resultado se decide antes de ejecutar, a partir de lo que debe hacer el programa, no de lo que hace.")),
                            paragraph(regular("Al ejecutar el caso, anotas el "), emphasis("resultado obtenido"), regular(". Si no coincide con el esperado, has encontrado un fallo. Aquí, el caso CP3 lo descubre: con un 7, "), inlineCode("calificar"), regular(" devuelve "), inlineCode("Aprobado"), regular(" en lugar de "), inlineCode("Notable"), regular(".")),
                            paragraph(regular("Guardar los casos en una tabla con un identificador permite repetirlos después de cada cambio y comprobar que el fallo no vuelve.")),
                        ),
                    visual =
                        LessonVisualContent.Trace(
                            example =
                                LessonCodeExample(
                                    title = "Calificar una nota",
                                    lines =
                                        kotlinCode(
                                            """
                                            fun calificar(nota: Int): String {
                                                if (nota < 0 || nota > 10) {
                                                    return "Nota no válida"
                                                }
                                                if (nota < 5) {
                                                    return "Suspenso"
                                                }
                                                if (nota <= 7) {
                                                    return "Aprobado"
                                                }
                                                if (nota < 9) {
                                                    return "Notable"
                                                }
                                                return "Sobresaliente"
                                            }
                                            """.trimIndent(),
                                        ),
                                    note = richText(regular("El fallo está en la línea 8: con "), inlineCode("<="), regular(", el 7 entra en "), inlineCode("Aprobado"), regular(".")),
                                ),
                            table =
                                LessonTraceTable(
                                    title = "Casos de prueba",
                                    columns = listOf("Caso", "Nota", "Esperado", "Obtenido"),
                                    rows =
                                        listOf(
                                            listOf("CP1", "3", "Suspenso", "Suspenso"),
                                            listOf("CP2", "6", "Aprobado", "Aprobado"),
                                            listOf("CP3", "7", "Notable", "Aprobado"),
                                            listOf("CP4", "12", "Nota no válida", "Nota no válida"),
                                        ),
                                ),
                        ),
                ),
                LessonStep(
                    title = "Caja negra: agrupar los datos",
                    explanation =
                        listOf(
                            paragraph(regular("En las pruebas de "), highlight("caja negra"), regular(" no miras el código: solo sabes qué debe hacer la función. Diseñas los casos a partir de esa especificación, como lo haría quien la va a usar.")),
                            paragraph(regular("No puedes probar todos los valores, pero sí agruparlos en "), highlight("particiones de equivalencia"), regular(": conjuntos de datos que la función debe tratar igual. Si un 2 da "), inlineCode("Suspenso"), regular(", lo normal es que un 3 también.")),
                            paragraph(regular("Elige un valor de cada partición, también de las "), emphasis("no válidas"), regular(": las notas por debajo de 0 y por encima de 10 deben dar "), inlineCode("Nota no válida"), regular(".")),
                        ),
                    visual =
                        LessonVisualContent.Trace(
                            example =
                                LessonCodeExample(
                                    title = "Solo la especificación",
                                    lines = calificarSpecification,
                                    note = richText(regular("En caja negra trabajas con lo que promete la función, no con su cuerpo.")),
                                ),
                            table =
                                LessonTraceTable(
                                    title = "Particiones de la nota",
                                    columns = listOf("Partición", "Rango", "Valor", "Esperado"),
                                    rows =
                                        listOf(
                                            listOf("No válida", "< 0", "-3", "Nota no válida"),
                                            listOf("Suspenso", "0-4", "2", "Suspenso"),
                                            listOf("Aprobado", "5-6", "6", "Aprobado"),
                                            listOf("Notable", "7-8", "8", "Notable"),
                                            listOf("Sobresaliente", "9-10", "10", "Sobresaliente"),
                                            listOf("No válida", "> 10", "15", "Nota no válida"),
                                        ),
                                ),
                        ),
                ),
                LessonStep(
                    title = "Caja negra: probar los límites",
                    explanation =
                        listOf(
                            paragraph(regular("Muchos fallos aparecen justo en la frontera entre dos particiones, al escribir "), inlineCode("<"), regular(" donde iba "), inlineCode("<="), regular(" o al revés. El valor 8 de la tabla anterior no habría descubierto el fallo del 7.")),
                            paragraph(regular("El "), highlight("análisis de valores límite"), regular(" añade casos a ambos lados de cada frontera: el último valor de una partición y el primero de la siguiente.")),
                            paragraph(regular("Con las particiones y los límites juntos tienes un conjunto pequeño de casos que cubre los datos con más probabilidad de fallar.")),
                        ),
                    visual =
                        LessonVisualContent.Trace(
                            example =
                                LessonCodeExample(
                                    title = "Solo la especificación",
                                    lines = calificarSpecification,
                                    note = richText(regular("Cada cambio de calificación es una frontera: entre 4 y 5, entre 6 y 7, entre 8 y 9, y en los extremos 0 y 10.")),
                                ),
                            table =
                                LessonTraceTable(
                                    title = "Valores a cada lado de la frontera",
                                    columns = listOf("Nota", "Esperado"),
                                    rows =
                                        listOf(
                                            listOf("-1", "Nota no válida"),
                                            listOf("0", "Suspenso"),
                                            listOf("4", "Suspenso"),
                                            listOf("5", "Aprobado"),
                                            listOf("6", "Aprobado"),
                                            listOf("7", "Notable"),
                                            listOf("8", "Notable"),
                                            listOf("9", "Sobresaliente"),
                                            listOf("10", "Sobresaliente"),
                                            listOf("11", "Nota no válida"),
                                        ),
                                ),
                        ),
                ),
                LessonStep(
                    title = "Caja blanca: recorrer todos los caminos",
                    explanation =
                        listOf(
                            paragraph(regular("En las pruebas de "), highlight("caja blanca"), regular(" sí miras el código y diseñas los casos para que cada instrucción y cada decisión se ejecuten al menos una vez.")),
                            paragraph(regular("La "), highlight("complejidad ciclomática"), regular(" indica cuántos caminos independientes tiene una función: se cuentan las condiciones simples y se suma 1. Aquí hay cinco, porque "), inlineCode("||"), regular(" separa dos, así que hacen falta seis casos.")),
                            paragraph(regular("IntelliJ IDEA mide la "), emphasis("cobertura"), regular(" con "), emphasis("Run with Coverage"), regular(": marca en verde las líneas que han ejecutado tus pruebas y en rojo las que no.")),
                        ),
                    visual =
                        LessonVisualContent.Trace(
                            example =
                                LessonCodeExample(
                                    title = "Calificar, ya corregida",
                                    lines =
                                        kotlinCode(
                                            """
                                            fun calificar(nota: Int): String {
                                                if (nota < 0 || nota > 10) {
                                                    return "Nota no válida"
                                                }
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
                                    note = richText(regular("Cinco condiciones simples más 1: complejidad ciclomática 6.")),
                                ),
                            table =
                                LessonTraceTable(
                                    title = "Un caso por camino",
                                    columns = listOf("Camino", "Se cumple", "Nota", "Devuelve"),
                                    rows =
                                        listOf(
                                            listOf("1", "nota < 0", "-1", "Nota no válida"),
                                            listOf("2", "nota > 10", "11", "Nota no válida"),
                                            listOf("3", "nota < 5", "3", "Suspenso"),
                                            listOf("4", "nota < 7", "6", "Aprobado"),
                                            listOf("5", "nota < 9", "8", "Notable"),
                                            listOf("6", "Ninguna", "10", "Sobresaliente"),
                                        ),
                                ),
                        ),
                    links =
                        listOf(
                            LessonLink("Medir la cobertura en IntelliJ IDEA", "https://www.jetbrains.com/help/idea/running-test-with-coverage.html"),
                        ),
                ),
                LessonStep(
                    title = "Tu primera prueba automática",
                    explanation =
                        listOf(
                            paragraph(regular("El proyecto que creaste con IntelliJ IDEA ya está preparado: tiene la carpeta "), inlineCode("src/test/kotlin"), regular(" y, en "), inlineCode("build.gradle.kts"), regular(", la biblioteca "), inlineCode("kotlin.test"), regular(".")),
                            explanationCode(
                                "build.gradle.kts",
                                richText(regular("No tienes que cambiar nada: estas líneas vienen en la plantilla.")),
                                "testImplementation(kotlin(\"test\"))",
                                "tasks.test {",
                                "    useJUnitPlatform()",
                                "}",
                            ),
                            paragraph(regular("Crea "), inlineCode("CalificarTest.kt"), regular(" en "), inlineCode("src/test/kotlin"), regular(". Cada prueba es una función marcada con "), inlineCode("@Test"), regular(" que llama a "), inlineCode("calificar"), regular(" y comprueba el resultado con "), inlineCode("assertEquals"), regular(": "), inlineCode("expected"), regular(" es el valor esperado y "), inlineCode("actual"), regular(", el obtenido.")),
                            callout(
                                "Una clase para agrupar las pruebas",
                                richText(regular("Las pruebas se escriben dentro de "), inlineCode("class CalificarTest { }"), regular(". Por ahora, tómala como el contenedor que agrupa las pruebas; las clases las estudiarás más adelante.")),
                            ),
                        ),
                    visual =
                        LessonVisualContent.CodeExamples(
                            listOf(
                                LessonCodeExample(
                                    title = "src/test/kotlin/CalificarTest.kt",
                                    lines =
                                        kotlinCode(
                                            """
                                            import kotlin.test.Test
                                            import kotlin.test.assertEquals

                                            class CalificarTest {
                                                @Test
                                                fun `un 3 es suspenso`() {
                                                    assertEquals(
                                                        expected = "Suspenso",
                                                        actual = calificar(3),
                                                    )
                                                }

                                                @Test
                                                fun `un 7 es notable`() {
                                                    assertEquals(
                                                        expected = "Notable",
                                                        actual = calificar(7),
                                                    )
                                                }
                                            }
                                            """.trimIndent(),
                                        ),
                                    note = richText(regular("Entre comillas invertidas, el nombre de la prueba puede llevar espacios: describe el caso que comprueba.")),
                                ),
                            ),
                        ),
                    links =
                        listOf(
                            LessonLink("Probar código Kotlin con JUnit", "https://kotlinlang.org/docs/jvm-test-using-junit.html"),
                            LessonLink("Referencia de kotlin.test", "https://kotlinlang.org/api/core/kotlin-test/"),
                        ),
                ),
                LessonStep(
                    title = "Ejecutar las pruebas en IntelliJ IDEA",
                    explanation =
                        listOf(
                            paragraph(regular("Junto a la clase y a cada prueba aparece un triángulo verde en el margen. Púlsalo y elige "), emphasis("Run"), regular(" para ejecutar todas las pruebas de la clase o solo esa.")),
                            paragraph(regular("La ventana "), emphasis("Run"), regular(" muestra un árbol con cada prueba: una marca verde si ha pasado y un aviso naranja si ha fallado. Al seleccionar una prueba fallida, verás a la derecha "), emphasis("Expected"), regular(", lo esperado, y "), emphasis("Actual"), regular(", lo obtenido.")),
                            paragraph(regular("Para ejecutar las pruebas de todo el proyecto, pulsa con el botón derecho sobre "), inlineCode("src/test/kotlin"), regular(" y elige "), emphasis("Run 'Tests in…'"), regular(".")),
                        ),
                    visual =
                        LessonVisualContent.Screenshot(
                            image = LessonScreenshot.IntellijTestResults,
                            description = "Resultados de CalificarTest en la ventana Run: un 3 es suspenso pasa y un 7 es notable falla con Expected: Notable y Actual: Aprobado.",
                        ),
                    links =
                        listOf(
                            LessonLink("Ejecutar pruebas en IntelliJ IDEA", "https://www.jetbrains.com/help/idea/performing-tests.html"),
                        ),
                ),
                operatingSystemStep("Leer una prueba que falla") { system ->
                    val gradleTest = if (system == TargetOperatingSystem.Windows) ".\\gradlew.bat test" else "./gradlew test"
                    LessonStepContent(
                        explanation =
                            listOf(
                                paragraph(regular("El mensaje "), inlineCode("expected: <Notable> but was: <Aprobado>"), regular(" se lee así: se esperaba "), inlineCode("Notable"), regular(", pero la función devolvió "), inlineCode("Aprobado"), regular(". La traza indica la línea de la prueba que ha fallado.")),
                                paragraph(regular("Por eso conviene nombrar los argumentos. Sin nombres, "), inlineCode("assertEquals"), regular(" espera primero el valor esperado y después el obtenido; si los inviertes, la prueba sigue fallando, pero el mensaje dice lo contrario y te despista.")),
                                when (system) {
                                    null -> paragraph(regular("También puedes ejecutar todas las pruebas desde la terminal con "), inlineCode("./gradlew test"), regular(" ("), inlineCode(".\\gradlew.bat test"), regular(" en Windows). Una prueba que falla hace que falle toda la construcción."))
                                    else -> paragraph(regular("También puedes ejecutar todas las pruebas desde la terminal, en la carpeta del proyecto, con "), inlineCode(gradleTest), regular(". Una prueba que falla hace que falle toda la construcción."))
                                },
                            ),
                        visual =
                            LessonVisualContent.Console(
                                title = "Terminal",
                                runs =
                                    listOf(
                                        consoleRun(
                                            consoleInput(gradleTest),
                                            consoleOutput("> Task :test FAILED"),
                                            consoleOutput("CalificarTest > un 7 es notable() FAILED"),
                                            consoleOutput("    org.opentest4j.AssertionFailedError at CalificarTest.kt:15"),
                                            consoleOutput("2 tests completed, 1 failed"),
                                            consoleError("BUILD FAILED in 1s"),
                                        ),
                                    ),
                            ),
                        links =
                            listOf(
                                LessonLink("Pruebas con Gradle", "https://docs.gradle.org/current/userguide/java_testing.html"),
                            ),
                    )
                },
                LessonStep(
                    title = "Preparar, actuar y comprobar",
                    explanation =
                        listOf(
                            paragraph(regular("Una prueba clara tiene tres partes: "), highlight("preparar"), regular(" los datos, "), highlight("actuar"), regular(" llamando a la función y "), highlight("comprobar"), regular(" el resultado. Cuando la prueba es corta, las tres caben en una línea.")),
                            paragraph(regular("Para funciones que devuelven "), inlineCode("Boolean"), regular(", usa "), inlineCode("assertTrue"), regular(" y "), inlineCode("assertFalse"), regular(". Todas las aserciones aceptan un "), inlineCode("message"), regular(" opcional que aparece si la prueba falla.")),
                            paragraph(regular("Comprueba una sola idea en cada prueba: si falla, su nombre ya te dice qué se ha roto, y las demás siguen informándote.")),
                        ),
                    visual =
                        LessonVisualContent.CodeExamples(
                            listOf(
                                LessonCodeExample(
                                    title = "Probar una función que responde sí o no",
                                    lines =
                                        kotlinCode(
                                            """
                                            @Test
                                            fun `un 5 aprueba`() {
                                                // Preparar
                                                val nota = 5
                                                // Actuar
                                                val aprobado = esAprobado(nota)
                                                // Comprobar
                                                assertTrue(
                                                    actual = aprobado,
                                                    message = "Un 5 aprueba",
                                                )
                                            }

                                            @Test
                                            fun `un 4 no aprueba`() {
                                                assertFalse(esAprobado(4))
                                            }
                                            """.trimIndent(),
                                        ),
                                    note = richText(regular("Prueban "), inlineCode("fun esAprobado(nota: Int): Boolean = nota >= 5"), regular(" en sus dos lados de la frontera. Importa "), inlineCode("assertTrue"), regular(" y "), inlineCode("assertFalse"), regular(" de "), inlineCode("kotlin.test"), regular(".")),
                                ),
                            ),
                        ),
                ),
                LessonStep(
                    title = "Qué hace buena a una prueba",
                    explanation =
                        listOf(
                            paragraph(regular("Las buenas pruebas unitarias se resumen en las siglas "), highlight("FIRST"), regular(", en inglés:")),
                            steps(
                                richText(emphasis("Fast"), regular(", rápidas: tardan tan poco que las ejecutas tras cada cambio.")),
                                richText(emphasis("Independent"), regular(", independientes: ninguna depende de otra ni del orden en que se ejecuten.")),
                                richText(emphasis("Repeatable"), regular(", repetibles: dan el mismo resultado siempre, sin depender de la hora ni de lo que escribas por teclado.")),
                                richText(emphasis("Self-validating"), regular(", autoverificables: pasan o fallan solas, sin que tengas que mirar la consola.")),
                                richText(emphasis("Timely"), regular(", oportunas: se escriben a la vez que el código, no al final.")),
                            ),
                            callout(
                                "Funciones fáciles de probar",
                                richText(regular("Una función que lee con "), inlineCode("readln"), regular(" o muestra con "), inlineCode("println"), regular(" es difícil de probar. Por eso conviene separar calcular de mostrar, como viste en la lección de funciones.")),
                                LessonCalloutTone.Tip,
                            ),
                        ),
                    visual = LessonVisualContent.Flow(listOf("Rápidas", "Independientes", "Repetibles", "Autoverificables", "Oportunas")),
                ),
                LessonStep(
                    title = "Escribir la prueba primero",
                    explanation =
                        listOf(
                            paragraph(regular("En el "), highlight("desarrollo guiado por pruebas"), regular(" (TDD, por sus siglas en inglés) escribes la prueba antes que el código, en ciclos muy cortos.")),
                            steps(
                                richText(emphasis("Rojo"), regular(": escribes una prueba para algo que la función aún no hace y compruebas que falla.")),
                                richText(emphasis("Verde"), regular(": escribes el código mínimo para que pase.")),
                                richText(emphasis("Refactorizar"), regular(": mejoras el código sin cambiar lo que hace; las pruebas avisan si rompes algo.")),
                            ),
                            paragraph(regular("Aunque no sigas TDD, las pruebas son tu red de seguridad: cuando reorganices un programa, como en la práctica de funciones, bastará con ejecutarlas para saber que sigue funcionando.")),
                        ),
                    visual = LessonVisualContent.Cycle(listOf("Rojo", "Verde", "Refactorizar")),
                ),
                LessonStep(
                    title = "Ahora te toca: probar las tarifas del cine",
                    explanation =
                        listOf(
                            paragraph(regular("Un cine cobra según la edad: de 0 a 3 años la entrada es "), inlineCode("Gratis"), regular("; de 4 a 17, "), inlineCode("Reducida"), regular("; de 18 a 64, "), inlineCode("General"), regular("; y de 65 a 120, "), inlineCode("Sénior"), regular(". Cualquier otra edad es "), inlineCode("Edad no válida"), regular(".")),
                            paragraph(regular("El programa de ejemplo parece funcionar: las cuatro edades de "), inlineCode("main"), regular(" dan la tarifa correcta. Aun así, "), inlineCode("calcularTarifa"), regular(" tiene "), highlight("dos fallos"), regular(". Encuéntralos con pruebas, no leyendo el código.")),
                            paragraph(regular("Pega el programa en "), inlineCode("Main.kt"), regular(" y crea las pruebas en "), inlineCode("src/test/kotlin"), regular(".")),
                        ),
                    visual =
                        LessonVisualContent.CodeExamples(
                            listOf(
                                LessonCodeExample(
                                    title = "Tarifas del cine",
                                    lines =
                                        kotlinCode(
                                            """
                                            fun main() {
                                                println(calcularTarifa(2))
                                                println(calcularTarifa(10))
                                                println(calcularTarifa(30))
                                                println(calcularTarifa(80))
                                            }

                                            fun calcularTarifa(
                                                edad: Int,
                                            ): String {
                                                if (edad < 0 || edad > 120) {
                                                    return "Edad no válida"
                                                }
                                                if (edad < 3) {
                                                    return "Gratis"
                                                }
                                                if (edad < 18) {
                                                    return "Reducida"
                                                }
                                                if (edad > 65) {
                                                    return "Sénior"
                                                }
                                                return "General"
                                            }
                                            """.trimIndent(),
                                        ),
                                    note = richText(regular("Muestra "), inlineCode("Gratis"), regular(", "), inlineCode("Reducida"), regular(", "), inlineCode("General"), regular(" y "), inlineCode("Sénior"), regular(", como debe. Los fallos están en otras edades.")),
                                    isCopyable = true,
                                ),
                            ),
                        ),
                    links =
                        listOf(
                            LessonLink("Probar código Kotlin con JUnit", "https://kotlinlang.org/docs/jvm-test-using-junit.html"),
                            LessonLink("Referencia de kotlin.test", "https://kotlinlang.org/api/core/kotlin-test/"),
                            LessonLink("Ejecutar pruebas en IntelliJ IDEA", "https://www.jetbrains.com/help/idea/performing-tests.html"),
                        ),
                    practice =
                        LessonPractice(
                            id = "basic-testing-cinema",
                            objective = richText(highlight("Diseñar casos de prueba de caja negra y automatizarlos para encontrar fallos.")),
                            statement = richText(regular("Diseña los casos de prueba de "), inlineCode("calcularTarifa"), regular(" a partir de las reglas del cine, conviértelos en pruebas automáticas y corrige los fallos que descubran.")),
                            requirements =
                                listOf(
                                    richText(regular("Haz una tabla con las particiones de la edad, incluidas las no válidas, y los valores límite de cada frontera.")),
                                    richText(regular("Crea "), inlineCode("TarifaTest.kt"), regular(" con al menos una prueba por partición y una por cada valor límite, con nombres que describan el caso.")),
                                    richText(regular("Ejecuta las pruebas antes de corregir nada y anota cuáles fallan y con qué mensaje.")),
                                    richText(regular("Corrige "), inlineCode("calcularTarifa"), regular(" sin modificar las pruebas hasta que todas pasen.")),
                                ),
                            expectedOutput =
                                listOf(
                                    consoleRun(
                                        consoleInput("./gradlew test"),
                                        consoleOutput("> Task :test"),
                                        consoleOutput("BUILD SUCCESSFUL in 1s"),
                                    ),
                                ),
                            hints =
                                listOf(
                                    richText(regular("Hay seis particiones: cuatro tarifas y dos zonas no válidas, por debajo de 0 y por encima de 120. Las fronteras están entre -1 y 0, 3 y 4, 17 y 18, 64 y 65, y 120 y 121.")),
                                    richText(regular("Si solo pruebas un valor de cada partición, como en "), inlineCode("main"), regular(", todo pasa. Los fallos aparecen en las pruebas de los límites.")),
                                    richText(regular("Deberían fallar dos pruebas, con mensajes como "), inlineCode("expected: <Gratis> but was: <Reducida>"), regular(". Mira qué comparación decide esa frontera y si le sobra o le falta un "), inlineCode("="), regular(".")),
                                ),
                            extension = richText(regular("Sigue el ciclo de TDD para añadir una regla: de 18 a 25 años, con carné joven, se paga "), inlineCode("Reducida"), regular(". Añade el parámetro "), inlineCode("carneJoven: Boolean = false"), regular(", escribe primero las pruebas que fallan y después el código.")),
                            confirmation = richText(regular("Confirma que todas tus pruebas pasan, que al menos dos fallaban antes de corregir y que no has cambiado ninguna para que pasara.")),
                        ),
                ),
            ),
    )
