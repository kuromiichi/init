package dev.kuromiichi.damprep.data.lessons

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class LessonProgressStateTest {
    private val lessons = listOf(lesson("first"), lesson("second"), lesson("third"))

    @Test
    fun `only first lesson is unlocked without progress`() {
        val progress = LessonProgressState(lessons, emptySet())

        assertTrue(progress.isUnlocked(0))
        assertFalse(progress.isUnlocked(1))
        assertFalse(progress.isUnlocked(2))
        assertEquals("first", progress.nextAvailableLessonId { true })
    }

    @Test
    fun `completed lesson unlocks its immediate successor`() {
        val progress = LessonProgressState(lessons, setOf("first"))

        assertTrue(progress.isCompleted("first"))
        assertTrue(progress.isUnlocked(1))
        assertFalse(progress.isUnlocked(2))
        assertEquals("second", progress.nextAvailableLessonId { true })
    }

    @Test
    fun `unknown completion ids do not affect progress`() {
        val progress = LessonProgressState(lessons, setOf("unknown"))

        assertEquals(0, progress.completedLessonCount)
        assertEquals(0f, progress.progress)
    }

    @Test
    fun `next lesson must be unlocked and have content`() {
        val progress = LessonProgressState(lessons, setOf("first"))

        assertEquals(null, progress.nextAvailableLessonId { lessonId -> lessonId == "first" })
    }

    private fun lesson(id: String) =
        LessonSummary(
            id = id,
            title = id,
            subjectArea = SubjectArea.Programming,
        )
}
