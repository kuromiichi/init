package dev.kuromiichi.init.data.lessons

import kotlin.test.Test
import kotlin.test.assertEquals

class KotlinCodeHighlighterTest {
    @Test
    fun `highlighting keeps the exact source text line by line`() {
        val source =
            $$"""
            fun main() {
                val nombre = readln()
                println("Hola, $nombre")
            }
            """.trimIndent()

        assertEquals(source.lines(), kotlinCode(source).map { line -> line.sourceText() })
    }

    @Test
    fun `keywords functions numbers and operators get their own kinds`() {
        val tokens = kotlinCode("val total = suma(30, 2) >= 18").single().tokens

        assertEquals(LessonCodeTokenKind.Keyword, tokens.kindOf("val"))
        assertEquals(LessonCodeTokenKind.Function, tokens.kindOf("suma"))
        assertEquals(LessonCodeTokenKind.Number, tokens.kindOf("30"))
        assertEquals(LessonCodeTokenKind.Operator, tokens.kindOf(">="))
        assertEquals(LessonCodeTokenKind.Bracket0, tokens.kindOf("("))
        assertEquals(LessonCodeTokenKind.Plain, tokens.kindOf("total"))
    }

    @Test
    fun `range operators are not confused with decimal numbers`() {
        val closed = kotlinCode("for (i in 1..5 step 2)").single().tokens
        val open = kotlinCode("val decimal = 1.5 + (0..<3).count()").single().tokens

        assertEquals(LessonCodeTokenKind.Number, closed.kindOf("1"))
        assertEquals(LessonCodeTokenKind.Operator, closed.kindOf(".."))
        assertEquals(LessonCodeTokenKind.Number, closed.kindOf("5"))
        assertEquals(LessonCodeTokenKind.Operator, open.kindOf("..<"))
        assertEquals(LessonCodeTokenKind.Number, open.kindOf("1.5"))
    }

    @Test
    fun `string templates colour the dollar as an escape and the expression as code`() {
        val tokens = kotlinCode($$"""fun saludar(nombre: String) = println("Hola, $nombre y ${otro.length}")""").single().tokens
        val texts = tokens.map { it.text }

        assertEquals(LessonCodeTokenKind.StringLiteral, tokens.kindOf("\"Hola,"))
        assertEquals(LessonCodeTokenKind.Interpolation, tokens.kindOf("$"))
        assertEquals(LessonCodeTokenKind.Parameter, tokens.last { it.text == "nombre" }.kind)
        assertEquals(LessonCodeTokenKind.Bracket1, tokens[texts.indexOf("\${")].kind)
        assertEquals(LessonCodeTokenKind.Plain, tokens.kindOf("otro"))
        assertEquals(LessonCodeTokenKind.Punctuation, tokens.kindOf("."))
        assertEquals(LessonCodeTokenKind.Bracket1, tokens[texts.lastIndexOf("}")].kind)
    }

    @Test
    fun `it inside a template keeps its colour`() {
        val tokens = kotlinCode($$"""lista.forEach { println("${it.nombre}: $it") }""").single().tokens

        assertEquals(2, tokens.count { it.kind == LessonCodeTokenKind.ImplicitParameter })
    }

    @Test
    fun `comments are recognised on one or several lines`() {
        val lines =
            kotlinCode(
                """
                // Una línea
                /* Varias
                   líneas */
                val x = 1
                """.trimIndent(),
            )

        assertEquals(LessonCodeTokenKind.Comment, lines[0].tokens.single().kind)
        assertEquals(LessonCodeTokenKind.Comment, lines[1].tokens.single().kind)
        assertEquals(LessonCodeTokenKind.Comment, lines[2].tokens.single().kind)
        assertEquals(LessonCodeTokenKind.Keyword, lines[3].tokens.first().kind)
    }

    @Test
    fun `infix calls are coloured as functions`() {
        val pair = kotlinCode("""val notas = mapOf("Ana" to 7)""").single().tokens
        val sets = kotlinCode("println(a union b)").single().tokens
        val range = kotlinCode("for (i in 3 downTo 0)").single().tokens
        val step = kotlinCode("for (i in 1..5 step 2)").single().tokens

        assertEquals(LessonCodeTokenKind.Function, pair.kindOf("to"))
        assertEquals(LessonCodeTokenKind.Function, sets.kindOf("union"))
        assertEquals(LessonCodeTokenKind.Function, range.kindOf("downTo"))
        assertEquals(LessonCodeTokenKind.Function, step.kindOf("step"))
    }

    @Test
    fun `identifiers that are not infix calls stay plain`() {
        val declaration = kotlinCode("val total = 3").single().tokens
        val statements = kotlinCode("var a = 1\nfoo\nbar").flatMap(LessonCodeLine::tokens)
        val conditional = kotlinCode("val x = if (ok) valor else otro").single().tokens

        assertEquals(LessonCodeTokenKind.Plain, declaration.kindOf("total"))
        assertEquals(LessonCodeTokenKind.Plain, statements.kindOf("foo"))
        assertEquals(LessonCodeTokenKind.Plain, conditional.kindOf("valor"))
    }

    @Test
    fun `keywords inside strings and comments are not highlighted as keywords`() {
        val tokens = kotlinCode("""println("if val") // when""").single().tokens

        assertEquals(listOf("println", "(", "\"if val\"", ")", " ", "// when"), tokens.map(LessonCodeToken::text))
    }

    @Test
    fun `annotations and backtick names are single tokens`() {
        val lines =
            kotlinCode(
                """
                @Test
                fun `un 7 es notable`() {
                """.trimIndent(),
            )

        assertEquals(LessonCodeTokenKind.ClassName, lines[0].tokens.single().kind)
        assertEquals(LessonCodeTokenKind.Function, lines[1].tokens.kindOf("`un 7 es notable`"))
        assertEquals(LessonCodeTokenKind.Keyword, lines[1].tokens.kindOf("fun"))
    }

    @Test
    fun `null safety operators follow the IDE colour scheme`() {
        val tokens = kotlinCode("val n: Int? = texto?.length ?: texto!!.length").single().tokens

        assertEquals(LessonCodeTokenKind.Constant, tokens.kindOf("?"))
        assertEquals(LessonCodeTokenKind.Punctuation, tokens.kindOf("?."))
        assertEquals(LessonCodeTokenKind.Operator, tokens.kindOf("?:"))
        assertEquals(LessonCodeTokenKind.Operator, tokens.kindOf("!!"))
    }

    @Test
    fun `brackets are coloured by nesting like IntelliJ IDEA`() {
        val lines =
            kotlinCode(
                """
                fun main() {
                    for (i in 1..4) {
                        println(i)
                    }
                }
                """.trimIndent(),
            )

        assertEquals(LessonCodeTokenKind.Bracket0, lines[0].tokens.kindOf("()"))
        assertEquals(LessonCodeTokenKind.Bracket0, lines[0].tokens.last { it.text.trim() == "{" }.kind)
        assertEquals(LessonCodeTokenKind.Bracket1, lines[1].tokens.first { it.text.trim() == "(" }.kind)
        assertEquals(LessonCodeTokenKind.Bracket2, lines[1].tokens.last { it.text.trim() == "{" }.kind)
        assertEquals(LessonCodeTokenKind.Bracket3, lines[2].tokens.first { it.text.trim() == "(" }.kind)
        assertEquals(LessonCodeTokenKind.Bracket2, lines[3].tokens.single { it.text.trim() == "}" }.kind)
        assertEquals(LessonCodeTokenKind.Bracket0, lines[4].tokens.single().kind)
    }

    @Test
    fun `parameters types and mutable variables follow the IDE colour scheme`() {
        val lines =
            kotlinCode(
                """
                fun calificar(nota: Int): String {
                    var total = 0
                    total += nota
                    return "x"
                }
                """.trimIndent(),
            )

        assertEquals(LessonCodeTokenKind.Parameter, lines[0].tokens.kindOf("nota"))
        assertEquals(LessonCodeTokenKind.ClassName, lines[0].tokens.kindOf("Int"))
        assertEquals(LessonCodeTokenKind.ClassName, lines[0].tokens.kindOf("String"))
        assertEquals(LessonCodeTokenKind.Plain, lines[0].tokens.first { ":" in it.text }.kind)
        assertEquals(LessonCodeTokenKind.MutableVariable, lines[1].tokens.kindOf("total"))
        assertEquals(LessonCodeTokenKind.MutableVariable, lines[2].tokens.kindOf("total"))
        assertEquals(LessonCodeTokenKind.Parameter, lines[2].tokens.kindOf("nota"))
    }

    @Test
    fun `constructor parameters without val are parameters and properties are not`() {
        val lines =
            kotlinCode(
                """
                class Perro(nombre: String, val raza: String) : Animal(nombre)

                class Pato : Nadador, Volador
                """.trimIndent(),
            )

        assertEquals(LessonCodeTokenKind.Parameter, lines[0].tokens.first { it.text.trim() == "nombre" }.kind)
        assertEquals(LessonCodeTokenKind.Parameter, lines[0].tokens.last { it.text.trim() == "nombre" }.kind)
        assertEquals(LessonCodeTokenKind.Plain, lines[0].tokens.first { it.text.trim().startsWith("raza") }.kind)
        assertEquals(LessonCodeTokenKind.ClassName, lines[2].tokens.kindOf("Nadador"))
    }

    @Test
    fun `named arguments get their own kind and lambda braces are coloured by nesting`() {
        val named = kotlinCode("assertEquals(expected = 1, actual = suma(3))").single().tokens
        val lambda =
            kotlinCode(
                """
                fun main() {
                    lista.forEach { println(it) }
                }
                """.trimIndent(),
            )[1].tokens

        assertEquals(LessonCodeTokenKind.Constant, named.kindOf("expected"))
        assertEquals(LessonCodeTokenKind.Constant, named.kindOf("actual"))
        assertEquals(LessonCodeTokenKind.Bracket1, lambda.kindOf("{"))
        assertEquals(LessonCodeTokenKind.Bracket2, lambda.first { it.text == "(" }.kind)
        assertEquals(LessonCodeTokenKind.Bracket1, lambda.kindOf("}"))
        assertEquals(LessonCodeTokenKind.ImplicitParameter, lambda.kindOf("it"))
    }

    @Test
    fun `only the lambda arrow is red`() {
        val lines =
            kotlinCode(
                """
                val doble: (Int) -> Int = { n -> n * 2 }
                val texto = when (x) {
                    1 -> "uno"
                    else -> "otro"
                }
                """.trimIndent(),
            )

        val arrows = lines[0].tokens.filter { "->" in it.text }
        assertEquals(LessonCodeTokenKind.Plain, arrows.first().kind)
        assertEquals(LessonCodeTokenKind.LambdaDelimiter, arrows.last().kind)
        assertEquals(LessonCodeTokenKind.Plain, lines[2].tokens.first { "->" in it.text }.kind)
        assertEquals(LessonCodeTokenKind.Plain, lines[3].tokens.first { "->" in it.text }.kind)
        val typeInsideLambda = kotlinCode("repeat(2) { val f: (Int) -> Int = it }").single().tokens
        assertEquals(LessonCodeTokenKind.Plain, typeInsideLambda.first { "->" in it.text }.kind)
    }

    @Test
    fun `lambda parameters are parameters but when branches are not`() {
        val lines =
            kotlinCode(
                """
                val doble = { n: Int -> n * 2 }
                notas.forEach { (nombre, nota) -> println(nombre) }
                val texto = when (x) {
                    a -> "a"
                    else -> "b"
                }
                """.trimIndent(),
            )

        assertEquals(LessonCodeTokenKind.Parameter, lines[0].tokens.first { it.text.trim() == "n" }.kind)
        assertEquals(LessonCodeTokenKind.Parameter, lines[0].tokens.last { it.text.trim() == "n" }.kind)
        assertEquals(LessonCodeTokenKind.ClassName, lines[0].tokens.kindOf("Int"))
        assertEquals(LessonCodeTokenKind.Parameter, lines[1].tokens.kindOf("nota"))
        assertEquals(LessonCodeTokenKind.Parameter, lines[1].tokens.last { it.text.trim() == "nombre" }.kind)
        assertEquals(LessonCodeTokenKind.Plain, lines[3].tokens.first { it.text.trim().startsWith("a") }.kind)
    }

    @Test
    fun `function references are plain text like in IntelliJ IDEA`() {
        val tokens = kotlinCode("val pares = numeros.filter(::esPar)").single().tokens

        assertEquals(LessonCodeTokenKind.Plain, tokens.kindOf("::esPar"))
    }

    @Test
    fun `variables and parameters that hold a function keep their colour when called`() {
        val lines =
            kotlinCode(
                """
                fun aplicar(n: Int, operacion: (Int) -> Int): Int = operacion(n)
                fun main() {
                    val doble = { n: Int -> n * 2 }
                    println(doble(4))
                }
                """.trimIndent(),
            )

        assertEquals(LessonCodeTokenKind.Parameter, lines[0].tokens.last { it.text.trim() == "operacion" }.kind)
        assertEquals(LessonCodeTokenKind.Plain, lines[3].tokens.kindOf("doble"))
        assertEquals(LessonCodeTokenKind.Function, lines[3].tokens.kindOf("println"))
    }

    @Test
    fun `enum entries and standard extension properties have their own colours`() {
        val lines =
            kotlinCode(
                """
                enum class Prioridad { ALTA, BAJA }
                val p = Prioridad.ALTA
                val minima = Alumno.NOTA_MINIMA
                for (i in notas.indices) println(notas.size)
                """.trimIndent(),
            )

        assertEquals(LessonCodeTokenKind.Constant, lines[0].tokens.kindOf("ALTA"))
        assertEquals(LessonCodeTokenKind.Constant, lines[1].tokens.kindOf("ALTA"))
        assertEquals(LessonCodeTokenKind.Plain, lines[2].tokens.kindOf("NOTA_MINIMA"))
        assertEquals(LessonCodeTokenKind.ExtensionProperty, lines[3].tokens.kindOf("indices"))
        assertEquals(LessonCodeTokenKind.Plain, lines[3].tokens.kindOf("size"))
    }

    @Test
    fun `calls with only a trailing lambda are coloured as functions`() {
        val lines =
            kotlinCode(
                """
                class Pedido {
                }
                val aprobadas = notas.filter { it >= 5 }
                repeat(3) { println(it) }
                """.trimIndent(),
            )

        assertEquals(LessonCodeTokenKind.ClassName, lines[0].tokens.kindOf("Pedido"))
        assertEquals(LessonCodeTokenKind.Function, lines[2].tokens.kindOf("filter"))
        assertEquals(LessonCodeTokenKind.Function, lines[3].tokens.kindOf("repeat"))
    }

    @Test
    fun `property accessors are keywords or plain text and their braces are blocks`() {
        val lines =
            kotlinCode(
                """
                var valor = 0
                    private set

                var nota: Int = 0
                    set(nueva) {
                        field = nueva
                    }

                val aprobado: Boolean
                    get() = nota >= 5
                """.trimIndent(),
            )

        assertEquals(LessonCodeTokenKind.Keyword, lines[1].tokens.kindOf("private"))
        assertEquals(LessonCodeTokenKind.Keyword, lines[1].tokens.kindOf("set"))
        assertEquals(LessonCodeTokenKind.Plain, lines[4].tokens.kindOf("set"))
        assertEquals(LessonCodeTokenKind.Parameter, lines[4].tokens.kindOf("nueva"))
        assertEquals(LessonCodeTokenKind.Bracket0, lines[4].tokens.kindOf("{"))
        assertEquals(LessonCodeTokenKind.MutableVariable, lines[5].tokens.kindOf("field"))
        assertEquals(LessonCodeTokenKind.Parameter, lines[5].tokens.kindOf("nueva"))
        assertEquals(LessonCodeTokenKind.Plain, lines[9].tokens.kindOf("get"))
        assertEquals(LessonCodeTokenKind.MutableVariable, lines[9].tokens.kindOf("nota"))
    }

    @Test
    fun `properties follow their declaration and constants are plain`() {
        val lines =
            kotlinCode(
                """
                class Contador(var valor: Int) {
                    fun reiniciar(valor: Int) {
                        this.valor = valor
                    }
                }

                val prioridad = Prioridad.ALTA
                println(contador.valor + Int.MAX_VALUE)
                """.trimIndent(),
            )

        assertEquals(LessonCodeTokenKind.MutableVariable, lines[0].tokens.kindOf("valor"))
        assertEquals(LessonCodeTokenKind.MutableVariable, lines[2].tokens.first { it.text == "valor" }.kind)
        assertEquals(LessonCodeTokenKind.Plain, lines[6].tokens.kindOf("prioridad"))
        assertEquals(LessonCodeTokenKind.Plain, lines[6].tokens.kindOf("ALTA"))
        assertEquals(LessonCodeTokenKind.MutableVariable, lines[7].tokens.first { it.text == "valor" }.kind)
        assertEquals(LessonCodeTokenKind.Plain, lines[7].tokens.kindOf("MAX_VALUE"))
    }

    @Test
    fun `named arguments of copy are plain like in IntelliJ IDEA`() {
        val copy = kotlinCode("val otra = ana.copy(edad = 21)").single().tokens
        val constructor = kotlinCode("val luis = Alumno(nombre = \"Luis\")").single().tokens

        assertEquals(LessonCodeTokenKind.Plain, copy.kindOf("edad"))
        assertEquals(LessonCodeTokenKind.Constant, constructor.kindOf("nombre"))
    }

    @Test
    fun `error marks underline the marked text and are not part of the code`() {
        val tokens = kotlinCode("⟦prinln⟧(\"Hola\")").single().tokens

        assertEquals("prinln(\"Hola\")", tokens.joinToString("") { it.text })
        assertEquals(true, tokens.first().hasErrorUnderline)
        assertEquals(false, tokens.last().hasErrorUnderline)
    }

    @Test
    fun `escapes in strings are separated like template parts`() {
        val tokens = kotlinCode("""println("a\nb")""").single().tokens

        assertEquals(LessonCodeTokenKind.Interpolation, tokens.kindOf("\\n"))
    }

    private fun List<LessonCodeToken>.kindOf(text: String): LessonCodeTokenKind = first { it.text.trim() == text }.kind

    private fun LessonCodeLine.sourceText(): String = tokens.joinToString("") { it.text }
}
