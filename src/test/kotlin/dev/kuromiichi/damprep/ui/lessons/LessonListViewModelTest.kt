package dev.kuromiichi.damprep.ui.lessons

import dev.kuromiichi.damprep.data.lessons.LessonSummary
import dev.kuromiichi.damprep.data.lessons.SubjectArea
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class LessonListViewModelTest {
    @Test
    fun `lesson list state marks lessons with content as unlocked`() {
        val viewModel =
            LessonListViewModel(
                lessonCatalog =
                    listOf(
                        LessonSummary(
                            id = "available",
                            title = "Disponible",
                            subjectArea = SubjectArea.Programming,
                        ),
                        LessonSummary(
                            id = "locked",
                            title = "Bloqueada",
                            subjectArea = SubjectArea.DevelopmentEnvironments,
                        ),
                    ),
                hasContent = { lessonId -> lessonId == "available" },
            )

        val lessons = viewModel.uiState.value.lessons

        assertEquals(2, lessons.size)
        assertTrue(lessons[0].isUnlocked)
        assertFalse(lessons[1].isUnlocked)
        assertEquals(0, lessons[0].index)
        assertEquals(1, lessons[1].index)
    }
}
