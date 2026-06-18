package dev.kuromiichi.damprep.data.lessons

data class LessonStep(
    val title: String,
    val body: String,
    val visual: LessonVisualContent,
)

sealed interface LessonVisualContent {
    data class IconText(
        val title: String,
        val body: String,
    ) : LessonVisualContent

    data class ToolCards(
        val tools: List<LessonTool>,
    ) : LessonVisualContent

    data class Flow(
        val nodes: List<String>,
    ) : LessonVisualContent

    data class Cycle(
        val steps: List<String>,
    ) : LessonVisualContent

    data class CodeExamples(
        val examples: List<LessonCodeExample>,
    ) : LessonVisualContent
}

data class LessonCodeExample(
    val title: String,
    val lines: List<LessonCodeLine>,
    val note: String,
)

data class LessonCodeLine(
    val tokens: List<LessonCodeToken>,
)

data class LessonCodeToken(
    val text: String,
    val kind: LessonCodeTokenKind = LessonCodeTokenKind.Plain,
    val hasErrorUnderline: Boolean = false,
)

enum class LessonCodeTokenKind {
    Plain,
    Keyword,
    Function,
    Number,
    Operator,
    Punctuation,
    StringLiteral,
    Interpolation,
    Comment,
}

data class LessonTool(
    val name: String,
    val kind: String,
    val logo: LessonToolLogo,
)

enum class LessonToolLogo {
    Kotlin,
    IntellijIdea,
}

data class LessonContent(
    val lessonId: String,
    val steps: List<LessonStep>,
)

object LessonContentCatalog {
    val lessons = listOf(
        LessonContent(
            lessonId = LessonCatalog.lessons[0].id,
            steps = listOf(
                LessonStep(
                    title = "Bienvenida y mapa del camino",
                    body = "Esta app está pensada como una tutoría guiada. Irás avanzando poco a poco, con pantallas breves y una idea importante cada vez.",
                    visual = LessonVisualContent.IconText(
                        title = "Ruta",
                        body = "Primero entenderás el camino, después escribirás código y más adelante usarás herramientas profesionales.",
                    ),
                ),
                LessonStep(
                    title = "No necesitas saber programar todavía",
                    body = "Empezamos desde cero. No pasa nada si aún no sabes qué es una variable, un proyecto o un error de compilación. Lo iremos construyendo paso a paso.",
                    visual = LessonVisualContent.IconText(
                        title = "Punto de partida",
                        body = "La idea no es correr, sino entender qué estás haciendo y por qué.",
                    ),
                ),
                LessonStep(
                    title = "Usaremos Kotlin e IntelliJ IDEA",
                    body = "Kotlin será el lenguaje principal: la forma en la que escribiremos instrucciones para el ordenador. IntelliJ IDEA será el entorno donde escribiremos, ejecutaremos y revisaremos esos programas.",
                    visual = LessonVisualContent.ToolCards(
                        tools = listOf(
                            LessonTool(
                                name = "Kotlin",
                                kind = "Lenguaje",
                                logo = LessonToolLogo.Kotlin,
                            ),
                            LessonTool(
                                name = "IntelliJ IDEA",
                                kind = "Entorno",
                                logo = LessonToolLogo.IntellijIdea,
                            ),
                        ),
                    ),
                ),
                LessonStep(
                    title = "Programar es aprender a pensar con orden",
                    body = "Programar no va de memorizar comandos. Va de observar un problema, dividirlo en partes pequeñas y explicar una solución con claridad.",
                    visual = LessonVisualContent.IconText(
                        title = "Forma de pensar",
                        body = "Problema, datos, pasos y resultado. Esa estructura aparecerá una y otra vez.",
                    ),
                ),
                LessonStep(
                    title = "Las herramientas también cuentan",
                    body = "También aprenderás a trabajar con un entorno real: ejecutar proyectos, leer errores, depurar, guardar versiones y comprobar que tu código hace lo esperado.",
                    visual = LessonVisualContent.IconText(
                        title = "Herramientas",
                        body = "El editor, el compilador, Git y las pruebas serán parte natural del aprendizaje.",
                    ),
                ),
                LessonStep(
                    title = "Cómo avanzar",
                    body = "Lee cada pantalla con calma. Más adelante, cada tema tendrá actividades para confirmar que puedes aplicar lo aprendido antes de continuar.",
                    visual = LessonVisualContent.IconText(
                        title = "Progreso",
                        body = "Cada paso debe dejarte un poco más situado que el anterior.",
                    ),
                ),
            ),
        ),
        LessonContent(
            lessonId = LessonCatalog.lessons[1].id,
            steps = listOf(
                LessonStep(
                    title = "Software: de una idea a algo que funciona",
                    body = "El software no aparece cuando alguien escribe la primera línea de código. Empieza antes: cuando hay una necesidad, una idea o un problema que queremos resolver con ayuda de un ordenador. Puede ser una app de móvil, una web, un videojuego, un cajero automático o un programa pequeño que ordena archivos. Lo importante es que hay una intención: queremos que la máquina haga algo útil, de forma repetible y con unas reglas claras.",
                    visual = LessonVisualContent.IconText(
                        title = "Camino",
                        body = "Primero aparece una necesidad. Después la entendemos, pensamos una solución y la convertimos en algo que se pueda ejecutar.",
                    ),
                ),
                LessonStep(
                    title = "Problemas, algoritmos y programas",
                    body = "Antes de programar conviene saber qué queremos conseguir. Un algoritmo es una secuencia ordenada de pasos para resolver un problema, y no tiene por qué empezar en Kotlin ni en ningún lenguaje concreto. Puedes pensar un algoritmo para preparar una receta, cruzar una calle o calcular el precio final de una compra. Un programa aparece cuando expresamos esos pasos en un lenguaje que el ordenador puede traducir y ejecutar.",
                    visual = LessonVisualContent.Flow(
                        nodes = listOf("Problema", "Algoritmo", "Programa"),
                    ),
                ),
                LessonStep(
                    title = "Hay varias formas de programar",
                    body = "No todos los lenguajes ni todos los programas se organizan igual. A esas formas de plantear soluciones las llamamos paradigmas: programación estructurada, modular, orientada a objetos, funcional y otras. Ahora no necesitas aprenderlas todas. Quédate con la idea de que un lenguaje no solo aporta palabras: también propone una forma de ordenar el pensamiento.",
                    visual = LessonVisualContent.IconText(
                        title = "Paradigmas",
                        body = "Distintas reglas para expresar una solución. Más adelante veremos cuáles usa Kotlin y cuándo nos conviene cada una.",
                    ),
                ),
                LessonStep(
                    title = "El ciclo básico de desarrollo",
                    body = "Para construir software con cabeza solemos pasar por varias fases: planificar, analizar, diseñar, codificar, probar, desplegar y mantener. No hace falta memorizar esos nombres ahora, pero sí entender la idea: no se trata de lanzarse a escribir código a ciegas. Primero aclaramos qué hay que hacer, luego pensamos cómo hacerlo, después lo construimos y finalmente comprobamos si realmente funciona.",
                    visual = LessonVisualContent.Cycle(
                        steps = listOf(
                            "Planificar",
                            "Analizar",
                            "Diseñar",
                            "Codificar",
                            "Probar",
                            "Mantener",
                        ),
                    ),
                ),
                LessonStep(
                    title = "Errores: reglas y significado",
                    body = "Al programar aparecerán errores, y eso no significa que estés haciendo algo mal: significa que estás trabajando con un sistema preciso. Algunos errores son sintácticos: el código no respeta las reglas del lenguaje, como una frase con palabras mal colocadas. Otros son semánticos: el código se puede ejecutar, pero la solución no hace lo que queríamos. Aprender a distinguirlos te ahorrará mucha frustración.",
                    visual = LessonVisualContent.CodeExamples(
                        examples = listOf(
                            LessonCodeExample(
                                title = "Error sintáctico",
                                lines = listOf(
                                    codeLine(
                                        function("println"),
                                        punctuation("("),
                                        string("\"Hola", hasErrorUnderline = true),
                                    ),
                                ),
                                note = "Faltan comillas y paréntesis de cierre: la frase queda incompleta.",
                            ),
                            LessonCodeExample(
                                title = "Error semántico",
                                lines = listOf(
                                    codeLine(
                                        keyword("val"),
                                        plain(" edad "),
                                        operator("="),
                                        plain(" "),
                                        number("16"),
                                    ),
                                    codeLine(
                                        keyword("val"),
                                        plain(" puedeEntrar "),
                                        operator("="),
                                        plain(" "),
                                        plain("edad", hasErrorUnderline = true),
                                        plain(" ", hasErrorUnderline = true),
                                        operator("<", hasErrorUnderline = true),
                                        plain(" ", hasErrorUnderline = true),
                                        number("18", hasErrorUnderline = true),
                                    ),
                                ),
                                note = "Compila, pero permite entrar justo a quien no debería.",
                            ),
                        ),
                    ),
                ),
                LessonStep(
                    title = "Lenguajes y traducción",
                    body = "Nosotros escribiremos en Kotlin, un lenguaje de alto nivel. Eso significa que está pensado para que las personas podamos leerlo y escribirlo con cierta comodidad. En escritorio, lo habitual es que Kotlin se compile a bytecode, una forma intermedia que la máquina virtual de Java entiende. Después, la JVM se encarga de convertir y ejecutar esas instrucciones en una forma que el ordenador pueda realizar.",
                    visual = LessonVisualContent.Flow(
                        nodes = listOf(
                            "Kotlin",
                            "Bytecode JVM",
                            "Código máquina",
                        ),
                    ),
                ),
                LessonStep(
                    title = "Construir software es iterar",
                    body = "El software casi nunca queda terminado para siempre. Cambia porque encontramos fallos, porque entendemos mejor el problema o porque aparecen nuevas necesidades. Por eso aprenderemos a avanzar en pasos pequeños: escribir algo, probarlo, leer qué ocurre, corregir y mejorar. Esa forma de trabajar es más importante que acertar a la primera, porque en programación rara vez se acierta todo a la primera.",
                    visual = LessonVisualContent.IconText(
                        title = "Evolución",
                        body = "Versión inicial, prueba, feedback, mejora y nueva versión. Programar también es revisar.",
                    ),
                ),
            ),
        ),
    )

    val currentLesson = lessons.first()

    fun findByLessonId(lessonId: String): LessonContent? =
        lessons.firstOrNull { it.lessonId == lessonId }

    fun hasContent(lessonId: String): Boolean =
        findByLessonId(lessonId) != null
}

private fun codeLine(vararg tokens: LessonCodeToken): LessonCodeLine =
    LessonCodeLine(tokens = tokens.toList())

private fun plain(text: String, hasErrorUnderline: Boolean = false): LessonCodeToken =
    LessonCodeToken(text = text, hasErrorUnderline = hasErrorUnderline)

private fun keyword(text: String): LessonCodeToken =
    LessonCodeToken(text = text, kind = LessonCodeTokenKind.Keyword)

private fun function(text: String): LessonCodeToken =
    LessonCodeToken(text = text, kind = LessonCodeTokenKind.Function)

private fun number(text: String, hasErrorUnderline: Boolean = false): LessonCodeToken =
    LessonCodeToken(
        text = text,
        kind = LessonCodeTokenKind.Number,
        hasErrorUnderline = hasErrorUnderline,
    )

private fun operator(text: String, hasErrorUnderline: Boolean = false): LessonCodeToken =
    LessonCodeToken(
        text = text,
        kind = LessonCodeTokenKind.Operator,
        hasErrorUnderline = hasErrorUnderline,
    )

private fun punctuation(text: String): LessonCodeToken =
    LessonCodeToken(text = text, kind = LessonCodeTokenKind.Punctuation)

private fun string(text: String, hasErrorUnderline: Boolean = false): LessonCodeToken =
    LessonCodeToken(
        text = text,
        kind = LessonCodeTokenKind.StringLiteral,
        hasErrorUnderline = hasErrorUnderline,
    )

private fun interpolation(text: String): LessonCodeToken =
    LessonCodeToken(text = text, kind = LessonCodeTokenKind.Interpolation)

private fun errorToken(text: String): LessonCodeToken =
    LessonCodeToken(text = text, hasErrorUnderline = true)
