package dev.kuromiichi.damprep.data.lessons

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
    fun `string templates are separated from literal text`() {
        val tokens = kotlinCode($$"""println("Hola, $nombre y ${otro.length}")""").single().tokens

        assertEquals(LessonCodeTokenKind.StringLiteral, tokens.kindOf("\"Hola,"))
        assertEquals(LessonCodeTokenKind.Interpolation, tokens.kindOf("\$nombre"))
        assertEquals(LessonCodeTokenKind.Interpolation, tokens.kindOf("\${otro.length}"))
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
    fun `null safety operators are single operator tokens`() {
        val tokens = kotlinCode("val n = texto?.length ?: texto!!.length").single().tokens

        listOf("?.", "?:", "!!").forEach { operator ->
            assertEquals(LessonCodeTokenKind.Operator, tokens.single { it.text.trim() == operator }.kind, operator)
        }
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
        assertEquals(LessonCodeTokenKind.Punctuation, lines[0].tokens.kindOf(":"))
        assertEquals(LessonCodeTokenKind.MutableVariable, lines[1].tokens.kindOf("total"))
        assertEquals(LessonCodeTokenKind.MutableVariable, lines[2].tokens.kindOf("total"))
        assertEquals(LessonCodeTokenKind.Parameter, lines[2].tokens.kindOf("nota"))
    }

    @Test
    fun `named arguments and lambda delimiters get their own kinds`() {
        val named = kotlinCode("assertEquals(expected = 1, actual = suma(3))").single().tokens
        val lambda = kotlinCode("lista.forEach { println(it) }").single().tokens

        assertEquals(LessonCodeTokenKind.Constant, named.kindOf("expected"))
        assertEquals(LessonCodeTokenKind.Constant, named.kindOf("actual"))
        assertEquals(LessonCodeTokenKind.LambdaDelimiter, lambda.kindOf("{"))
        assertEquals(LessonCodeTokenKind.LambdaDelimiter, lambda.kindOf("}"))
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
