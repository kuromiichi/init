package dev.kuromiichi.damprep.data.lessons

import kotlin.test.Test
import kotlin.test.assertEquals
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
