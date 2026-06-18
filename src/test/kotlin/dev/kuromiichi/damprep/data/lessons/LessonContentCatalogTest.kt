package dev.kuromiichi.damprep.data.lessons

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class LessonContentCatalogTest {
    @Test
    fun `current lesson content belongs to first catalog lesson`() {
        assertEquals(
            LessonCatalog.lessons.first().id,
            LessonContentCatalog.currentLesson.lessonId,
        )
    }

    @Test
    fun `initial catalog lessons have content`() {
        val lessonIdsWithContent = LessonContentCatalog.lessons.map { it.lessonId }

        assertEquals(LessonCatalog.lessons.take(2).map { it.id }, lessonIdsWithContent)
    }

    @Test
    fun `lesson content can be found by lesson id`() {
        val softwareLesson = LessonContentCatalog.findByLessonId("software-development")

        assertNotNull(softwareLesson)
        assertEquals("software-development", softwareLesson.lessonId)
        assertTrue(LessonContentCatalog.hasContent("welcome-roadmap"))
        assertTrue(LessonContentCatalog.hasContent("software-development"))
        assertFalse(LessonContentCatalog.hasContent("development-environment"))
    }

    @Test
    fun `current lesson has navigable steps with visual content`() {
        val steps = LessonContentCatalog.currentLesson.steps

        assertEquals(6, steps.size)
        assertTrue(steps.all { it.title.isNotBlank() })
        assertTrue(steps.all { it.body.isNotBlank() })
        assertTrue(steps.all { it.visual.hasReadableContent() })
    }

    @Test
    fun `current lesson introduces kotlin and intellij idea`() {
        val toolStep = LessonContentCatalog.currentLesson.steps[2]

        assertEquals("Usaremos Kotlin e IntelliJ IDEA", toolStep.title)
        assertTrue(toolStep.body.contains("Kotlin"))
        assertTrue(toolStep.body.contains("IntelliJ IDEA"))
        val visual = toolStep.visual
        assertTrue(visual is LessonVisualContent.ToolCards)
        assertEquals(
            listOf(LessonToolLogo.Kotlin, LessonToolLogo.IntellijIdea),
            visual.tools.map { it.logo },
        )
    }

    @Test
    fun `software development lesson introduces the development process`() {
        val softwareLesson = LessonContentCatalog.lessons[1]
        val combinedText = softwareLesson.steps.joinToString(" ") {
            "${it.title} ${it.body} ${it.visual.readableText()}"
        }

        assertEquals("software-development", softwareLesson.lessonId)
        assertEquals(7, softwareLesson.steps.size)
        assertTrue(combinedText.contains("software", ignoreCase = true))
        assertTrue(combinedText.contains("algoritmo", ignoreCase = true))
        assertTrue(combinedText.contains("paradigmas", ignoreCase = true))
        assertTrue(combinedText.contains("sintácticos", ignoreCase = true))
        assertTrue(combinedText.contains("Kotlin"))
        assertTrue(combinedText.contains("bytecode", ignoreCase = true))
        assertTrue(combinedText.contains("JVM"))
        assertTrue(combinedText.contains("traducción", ignoreCase = true))
    }

    @Test
    fun `lesson steps can provide flow visuals`() {
        val softwareLesson = LessonContentCatalog.findByLessonId("software-development")
        val flowVisual = softwareLesson?.steps?.map { it.visual }
            ?.filterIsInstance<LessonVisualContent.Flow>()
            ?.firstOrNull()

        assertNotNull(flowVisual)
        assertEquals(listOf("Problema", "Algoritmo", "Programa"), flowVisual.nodes)
    }

    @Test
    fun `translation step explains kotlin to jvm execution flow`() {
        val softwareLesson = LessonContentCatalog.findByLessonId("software-development")
        val translationStep = softwareLesson?.steps?.firstOrNull { it.title == "Lenguajes y traducción" }
        val visual = translationStep?.visual

        assertNotNull(translationStep)
        assertTrue(translationStep.body.contains("bytecode", ignoreCase = true))
        assertTrue(translationStep.body.contains("JVM"))
        assertTrue(visual is LessonVisualContent.Flow)
        assertEquals(
            listOf("Kotlin", "Bytecode JVM", "Código máquina"),
            visual.nodes,
        )
    }

    @Test
    fun `development cycle step provides a cycle visual`() {
        val softwareLesson = LessonContentCatalog.findByLessonId("software-development")
        val cycleStep = softwareLesson?.steps?.firstOrNull { it.title == "El ciclo básico de desarrollo" }
        val visual = cycleStep?.visual

        assertNotNull(cycleStep)
        assertTrue(visual is LessonVisualContent.Cycle)
        assertEquals(
            listOf("Planificar", "Analizar", "Diseñar", "Codificar", "Probar", "Mantener"),
            visual.steps,
        )
    }

    @Test
    fun `error explanation step provides syntax and semantic code examples`() {
        val softwareLesson = LessonContentCatalog.findByLessonId("software-development")
        val errorStep = softwareLesson?.steps?.firstOrNull { it.title == "Errores: reglas y significado" }
        val visual = errorStep?.visual

        assertNotNull(errorStep)
        assertTrue(visual is LessonVisualContent.CodeExamples)
        assertEquals(listOf("Error sintáctico", "Error semántico"), visual.examples.map { it.title })
        val syntaxExampleText = visual.examples.first().lines.joinToString("\n") { line ->
            line.tokens.joinToString("") { token -> token.text }
        }
        assertTrue(syntaxExampleText.contains("println"))
        assertFalse(syntaxExampleText.contains("val"))
        assertTrue(visual.examples.all { example ->
            example.lines.any { line -> line.tokens.any { token -> token.hasErrorUnderline } }
        })
    }

}

private fun LessonVisualContent.hasReadableContent(): Boolean =
    readableText().isNotBlank()

private fun LessonVisualContent.readableText(): String =
    when (this) {
        is LessonVisualContent.CodeExamples -> examples.joinToString(" ") { example ->
            "${example.title} ${example.note} " +
                example.lines.joinToString(" ") { line -> line.tokens.joinToString("") { token -> token.text } }
        }
        is LessonVisualContent.Cycle -> steps.joinToString(" ")
        is LessonVisualContent.Flow -> nodes.joinToString(" ")
        is LessonVisualContent.IconText -> "$title $body"
        is LessonVisualContent.ToolCards -> tools.joinToString(" ") { "${it.name} ${it.kind}" }
    }
