package dev.kuromiichi.damprep.ui.lessons

import dev.kuromiichi.damprep.data.lessons.LessonSummary
import dev.kuromiichi.damprep.data.lessons.SubjectArea
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class LessonListUiStateTest {
    @Test
    fun `lesson list unlocks only the first lesson initially`() {
        val lessons =
            createLessonListUiState(
                completedLessonIds = emptySet(),
                lessonCatalog =
                    listOf(
                        LessonSummary(
                            id = "available",
                            title = "Disponible",
                            subjectArea = SubjectArea.Programming,
                        ),
                        LessonSummary(
                            id = "next",
                            title = "Siguiente",
                            subjectArea = SubjectArea.DevelopmentEnvironments,
                        ),
                    ),
                hasContent = { true },
            ).lessons

        assertEquals(2, lessons.size)
        assertTrue(lessons[0].isUnlocked)
        assertFalse(lessons[1].isUnlocked)
        assertTrue(lessons.all(LessonListItemUiModel::isAvailable))
        assertEquals(0, lessons[0].index)
        assertEquals(1, lessons[1].index)
    }

    @Test
    fun `completing a lesson unlocks the next available lesson`() {
        val lessons =
            createLessonListUiState(
                completedLessonIds = setOf("first"),
                lessonCatalog =
                    listOf(
                        lesson("first"),
                        lesson("second"),
                        lesson("third"),
                    ),
                hasContent = { true },
            ).lessons

        assertTrue(lessons[0].isCompleted)
        assertTrue(lessons[1].isUnlocked)
        assertFalse(lessons[2].isUnlocked)
        assertEquals(lessons[1].title, lessons[2].previousLessonTitle)
    }

    @Test
    fun `unlocked lesson without content remains unavailable`() {
        val lessons =
            createLessonListUiState(
                completedLessonIds = setOf("first"),
                lessonCatalog = listOf(lesson("first"), lesson("second")),
                hasContent = { lessonId -> lessonId == "first" },
            ).lessons

        assertTrue(lessons[1].isUnlocked)
        assertFalse(lessons[1].isAvailable)
    }

    @Test
    fun `debug override unlocks every lesson but keeps missing content unavailable`() {
        val lessons =
            createLessonListUiState(
                completedLessonIds = emptySet(),
                unlockAllLessons = true,
                lessonCatalog = listOf(lesson("first"), lesson("second"), lesson("third")),
                hasContent = { lessonId -> lessonId != "third" },
            ).lessons

        assertTrue(lessons.all(LessonListItemUiModel::isUnlocked))
        assertFalse(lessons[2].isAvailable)
        assertTrue(lessons.none(LessonListItemUiModel::isCompleted))
    }

    private fun lesson(id: String) =
        LessonSummary(
            id = id,
            title = id,
            subjectArea = SubjectArea.Programming,
        )
}
