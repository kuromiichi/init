package dev.kuromiichi.damprep.data.lessons

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
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

        assertEquals(LessonCatalog.lessons.take(1).map { it.id }, lessonIdsWithContent)
    }

    @Test
    fun `lesson content can be found by lesson id`() {
        val welcomeLesson = LessonContentCatalog.findByLessonId("welcome-roadmap")

        assertEquals("welcome-roadmap", welcomeLesson?.lessonId)
        assertTrue(LessonContentCatalog.hasContent("welcome-roadmap"))
        assertFalse(LessonContentCatalog.hasContent("software-development"))
    }

    @Test
    fun `current lesson has navigable steps with visual content`() {
        val steps = LessonContentCatalog.currentLesson.steps

        assertEquals(6, steps.size)
        assertTrue(steps.all { it.title.isNotBlank() })
        assertTrue(steps.all { it.body.isNotBlank() })
        assertTrue(steps.all { it.visualTitle.isNotBlank() })
        assertTrue(steps.all { it.visualBody.isNotBlank() })
    }

    @Test
    fun `current lesson introduces kotlin and intellij idea`() {
        val toolStep = LessonContentCatalog.currentLesson.steps[2]

        assertEquals("Usaremos Kotlin e IntelliJ IDEA", toolStep.title)
        assertTrue(toolStep.body.contains("Kotlin"))
        assertTrue(toolStep.body.contains("IntelliJ IDEA"))
    }

}
