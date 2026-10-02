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
        assertEquals(LessonCodeTokenKind.Punctuation, tokens.kindOf("("))
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

        assertEquals(LessonCodeTokenKind.Keyword, lines[0].tokens.single().kind)
        assertEquals(LessonCodeTokenKind.Function, lines[1].tokens.kindOf("`un 7 es notable`"))
        assertEquals(LessonCodeTokenKind.Keyword, lines[1].tokens.kindOf("fun"))
    }

    private fun List<LessonCodeToken>.kindOf(text: String): LessonCodeTokenKind = first { it.text.trim() == text }.kind

    private fun LessonCodeLine.sourceText(): String = tokens.joinToString("") { it.text }
}
