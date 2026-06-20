package dev.kuromiichi.damprep.ui.lessons

import dev.kuromiichi.damprep.data.lessons.LessonContent
import dev.kuromiichi.damprep.data.lessons.LessonStep
import dev.kuromiichi.damprep.data.lessons.LessonVisualContent
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

class CurrentLessonViewModelTest {
    @Test
    fun `current lesson state reports missing lessons`() {
        val viewModel = CurrentLessonViewModel(
            lessonId = "missing",
            findLessonById = { null },
        )

        val state = viewModel.uiState.value

        assertTrue(state.isMissing)
        assertNull(state.currentStep)
        assertEquals(0, state.stepCount)
        assertFalse(state.canGoBack)
        assertFalse(state.canGoForward)
    }

    @Test
    fun `current lesson navigation moves between steps and respects bounds`() {
        val viewModel = CurrentLessonViewModel(
            lessonId = "lesson",
            findLessonById = { id -> if (id == "lesson") sampleLessonContent() else null },
        )

        assertEquals(0, viewModel.uiState.value.currentStepIndex)
        assertFalse(viewModel.uiState.value.canGoBack)
        assertTrue(viewModel.uiState.value.canGoForward)

        viewModel.goToPreviousStep()
        assertEquals(0, viewModel.uiState.value.currentStepIndex)

        viewModel.goToNextStep()
        assertEquals(1, viewModel.uiState.value.currentStepIndex)
        assertTrue(viewModel.uiState.value.canGoBack)
        assertTrue(viewModel.uiState.value.canGoForward)

        viewModel.goToNextStep()
        assertEquals(2, viewModel.uiState.value.currentStepIndex)
        assertFalse(viewModel.uiState.value.canGoForward)

        viewModel.goToNextStep()
        assertEquals(2, viewModel.uiState.value.currentStepIndex)
    }

    private fun sampleLessonContent(): LessonContent =
        LessonContent(
            lessonId = "lesson",
            steps = listOf(
                sampleStep("Uno"),
                sampleStep("Dos"),
                sampleStep("Tres"),
            ),
        )

    private fun sampleStep(title: String): LessonStep =
        LessonStep(
            title = title,
            body = "Contenido",
            visual = LessonVisualContent.IconText(
                title = "Visual",
                body = "Detalle",
            ),
        )
}
