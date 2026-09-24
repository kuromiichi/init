package dev.kuromiichi.damprep.ui.lessons

import dev.kuromiichi.damprep.data.lessons.LessonSummary
import dev.kuromiichi.damprep.data.lessons.SubjectArea
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

class LessonListUiModelTest {
    @Test
    fun `lesson summary maps to list item ui model`() {
        val lesson =
            LessonSummary(
                id = "software-development",
                title = "Qué es el software y cómo se construye",
                subjectArea = SubjectArea.DevelopmentEnvironments,
            )

        val uiModel =
            lesson.toLessonListItemUiModel(
                index = 1,
                isUnlocked = true,
                isAvailable = true,
                isCompleted = false,
            )

        assertEquals(1, uiModel.index)
        assertEquals("software-development", uiModel.id)
        assertEquals("Qué es el software y cómo se construye", uiModel.title)
        assertEquals(SubjectArea.DevelopmentEnvironments, uiModel.subjectArea)
        assertTrue(uiModel.isUnlocked)
        assertTrue(uiModel.isAvailable)
    }

    @Test
    fun `unavailable lessons explain why they cannot be opened`() {
        val lesson = LessonSummary(id = "loops", title = "Bucles", subjectArea = SubjectArea.Programming)

        val open = lesson.toLessonListItemUiModel(index = 1, isUnlocked = true, isAvailable = true, isCompleted = false)
        val locked =
            lesson.toLessonListItemUiModel(
                index = 1,
                isUnlocked = false,
                isAvailable = true,
                isCompleted = false,
                previousLessonTitle = "Decisiones",
            )
        val upcoming = lesson.toLessonListItemUiModel(index = 1, isUnlocked = true, isAvailable = false, isCompleted = false)

        assertNull(open.unavailableReason)
        assertEquals("Completa «Decisiones» para desbloquear esta lección.", locked.unavailableReason)
        assertEquals("Esta lección estará disponible próximamente.", upcoming.unavailableReason)
    }
}
