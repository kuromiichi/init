package dev.kuromiichi.damprep.data.lessons

import dev.kuromiichi.damprep.data.preferences.TargetOperatingSystem
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/**
 * Reglas que debe cumplir cualquier lección. Comprueban la estructura y el alcance del contenido,
 * no su redacción, para que reescribir un texto no obligue a tocar las pruebas.
 */
class LessonContentRulesTest {
    private val lessons = LessonContentCatalog.lessons

    @Test
    fun `every step and platform variant has readable content`() {
        lessons.forEach { lesson ->
            lesson.steps.forEach { step ->
                val where = "${lesson.lessonId} › ${step.title}"
                assertTrue(step.title.isNotBlank(), "Paso sin título en ${lesson.lessonId}")
                step.allContents().forEach { content ->
                    assertTrue(content.body.isNotBlank(), "Paso sin texto: $where")
                    assertTrue(content.visual.readableText().isNotBlank(), "Visual vacío: $where")
                    assertTrue(content.explanation.all { it.readableText().isNotBlank() }, "Bloque vacío: $where")
                    assertTrue(content.body.length < 240 || content.explanation.size > 1, "Explicación sin dividir: $where")
                }
            }
        }
    }

    @Test
    fun `platform sensitive steps cover every operating system`() {
        lessons.flatMap(LessonContent::steps).filter(LessonStep::isOperatingSystemSensitive).forEach { step ->
            assertEquals(TargetOperatingSystem.entries.toSet(), step.operatingSystemContent.keys, step.title)
            val variants = TargetOperatingSystem.entries.map(step::contentFor)
            assertTrue(variants.all { it.links.isNotEmpty() }, "Variante sin enlaces: ${step.title}")
            assertEquals(variants.size, variants.map(LessonStepContent::body).toSet().size, "Variantes repetidas: ${step.title}")
        }
    }

    @Test
    fun `practices are complete and verifiable`() {
        val practices = lessons.flatMap(LessonContent::steps).mapNotNull(LessonStep::practice)

        assertEquals(practices.size, practices.map(LessonPractice::id).toSet().size, "Identificadores de práctica repetidos")
        practices.forEach { practice ->
            assertTrue(practice.objective.plainText.isNotBlank(), practice.id)
            assertTrue(practice.statement.plainText.isNotBlank(), practice.id)
            assertTrue(practice.requirements.isNotEmpty(), practice.id)
            assertTrue(practice.hints.isNotEmpty(), practice.id)
            assertTrue(practice.confirmation.plainText.isNotBlank(), practice.id)
            assertTrue(practice.expectedOutput.all { run -> run.lines.any { !it.isInput } }, "Ejecución sin salida: ${practice.id}")
        }
    }

    @Test
    fun `lessons with a practice end with it`() {
        lessons.filter { lesson -> lesson.steps.any { it.practice != null } }.forEach { lesson ->
            assertEquals(listOf(lesson.steps.last()), lesson.steps.filter { it.practice != null }, lesson.lessonId)
        }
    }

    @Test
    fun `code examples and links are well formed`() {
        lessons.flatMap(LessonContent::steps).flatMap(LessonStep::allContents).forEach { content ->
            (content.visual as? LessonVisualContent.CodeExamples)?.examples?.forEach { example ->
                assertTrue(example.title.isNotBlank())
                assertTrue(example.lines.isNotEmpty(), example.title)
                assertTrue(example.note.plainText.isNotBlank(), example.title)
            }
            content.links.forEach { link ->
                assertTrue(link.label.isNotBlank())
                assertTrue(link.url.startsWith("https://"), link.url)
            }
        }
    }

    @Test
    fun `learners never see curricular traceability`() {
        val forbidden = listOf(Regex("""\bRA\d"""), Regex("(?i)resultados? de aprendizaje"), Regex("(?i)currícul"), Regex("(?i)\\bBOE\\b|boe\\.es"))

        lessons.forEach { lesson ->
            val text = lesson.allVisibleText()
            forbidden.forEach { pattern ->
                assertFalse(pattern.containsMatchIn(text), "${lesson.lessonId} muestra trazabilidad curricular: $pattern")
            }
        }
    }

    @Test
    fun `lessons do not use constructs taught later`() {
        val laterConstructs =
            mapOf(
                "first-programs" to listOf("""\breadln\b""", """\bif \(""", """\bwhen \(""", """\bfor \(""", """\bwhile \("""),
                "input-decisions" to listOf("""\bfor \(""", """\bwhile \(""", """\bfun (?!main\b)""", """\blistOf\b"""),
            )

        laterConstructs.forEach { (lessonId, patterns) ->
            val source = LessonContentCatalog.findByLessonId(lessonId)?.sourceCode().orEmpty()
            assertTrue(source.isNotBlank(), "Sin código en $lessonId")
            patterns.forEach { pattern ->
                assertFalse(Regex(pattern).containsMatchIn(source), "$lessonId usa $pattern antes de tiempo")
            }
        }
    }
}

private fun LessonStep.allContents(): List<LessonStepContent> = listOf(contentFor(null)) + operatingSystemContent.values

private fun LessonContent.sourceCode(): String =
    steps
        .flatMap(LessonStep::allContents)
        .map(LessonStepContent::visual)
        .filterIsInstance<LessonVisualContent.CodeExamples>()
        .flatMap(LessonVisualContent.CodeExamples::examples)
        .flatMap(LessonCodeExample::lines)
        .joinToString("\n") { line -> line.tokens.joinToString("", transform = LessonCodeToken::text) }

private fun LessonContent.allVisibleText(): String =
    steps.joinToString(" ") { step ->
        buildList {
            add(step.title)
            step.allContents().forEach { content ->
                add(content.body)
                add(content.visual.readableText())
                addAll(content.explanation.map(LessonExplanationBlock::readableText))
                addAll(content.links.map(LessonLink::label))
            }
            step.practice?.let { practice ->
                add(practice.objective.plainText)
                add(practice.statement.plainText)
                addAll(practice.requirements.map(LessonRichText::plainText))
                addAll(practice.expectedOutput.flatMap(LessonConsoleRun::lines).map(LessonConsoleLine::text))
                addAll(practice.hints.map(LessonRichText::plainText))
                practice.extension?.plainText?.let(::add)
                add(practice.confirmation.plainText)
            }
        }.joinToString(" ")
    }

private fun LessonExplanationBlock.readableText(): String =
    when (this) {
        is LessonExplanationBlock.Callout -> "$title ${text.plainText}"
        is LessonExplanationBlock.Code -> "$title ${lines.joinToString(" ")} ${note.plainText}"
        is LessonExplanationBlock.Paragraph -> text.plainText
        is LessonExplanationBlock.Steps -> items.joinToString(" ", transform = LessonRichText::plainText)
    }

private fun LessonVisualContent.readableText(): String =
    when (this) {
        is LessonVisualContent.CodeExamples ->
            examples.joinToString(" ") { example ->
                "${example.title} ${example.note.plainText} " +
                    example.lines.joinToString(" ") { line -> line.tokens.joinToString("") { token -> token.text } }
            }
        is LessonVisualContent.Cycle -> steps.joinToString(" ")
        is LessonVisualContent.Flow -> nodes.joinToString(" ")
        is LessonVisualContent.IconText -> "$title $body"
        is LessonVisualContent.Screenshot -> description
        is LessonVisualContent.ToolCards -> tools.joinToString(" ") { "${it.name} ${it.kind}" }
    }
