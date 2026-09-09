package dev.kuromiichi.damprep.ui.lessons

import dev.kuromiichi.damprep.data.lessons.LessonCheckpoint
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
        val viewModel =
            CurrentLessonViewModel(
                lessonId = "missing",
                findLessonById = { null },
            )

        val state = viewModel.uiState.value

        assertTrue(state.isMissing)
        assertNull(state.currentStep)
        assertEquals(0, state.stepCount)
        assertFalse(state.canGoBack)
        assertFalse(state.canGoForward)
        assertFalse(state.canShowCompletion)
    }

    @Test
    fun `current lesson navigation moves between steps and respects bounds`() {
        val viewModel =
            CurrentLessonViewModel(
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
        assertTrue(viewModel.uiState.value.canShowCompletion)

        viewModel.goToNextStep()
        assertEquals(2, viewModel.uiState.value.currentStepIndex)
        assertTrue(viewModel.uiState.value.isCompletionVisible)

        viewModel.goToPreviousStep()
        assertEquals(2, viewModel.uiState.value.currentStepIndex)
        assertFalse(viewModel.uiState.value.isCompletionVisible)
    }

    @Test
    fun `saved step is restored and clamped to lesson bounds`() {
        val restored =
            CurrentLessonViewModel(
                lessonId = "lesson",
                initialStepIndex = 1,
                findLessonById = { sampleLessonContent() },
            )
        val outOfBounds =
            CurrentLessonViewModel(
                lessonId = "lesson",
                initialStepIndex = 99,
                findLessonById = { sampleLessonContent() },
            )

        assertEquals(1, restored.uiState.value.currentStepIndex)
        assertEquals(2, outOfBounds.uiState.value.currentStepIndex)
    }

    @Test
    fun `checkpoint blocks navigation until it is manually confirmed`() {
        val checkpoint =
            LessonCheckpoint(
                id = "hello-world",
                prompt = "He ejecutado el programa.",
            )
        val viewModel =
            CurrentLessonViewModel(
                lessonId = "lesson",
                findLessonById = { id ->
                    if (id == "lesson") {
                        LessonContent(
                            lessonId = id,
                            steps =
                                listOf(
                                    sampleStep("Uno"),
                                    sampleStep("Dos", checkpoint),
                                    sampleStep("Tres"),
                                ),
                        )
                    } else {
                        null
                    }
                },
            )

        viewModel.goToNextStep()

        assertEquals(1, viewModel.uiState.value.currentStepIndex)
        assertFalse(viewModel.uiState.value.isCurrentCheckpointConfirmed)
        assertFalse(viewModel.uiState.value.canGoForward)

        viewModel.goToNextStep()
        assertEquals(1, viewModel.uiState.value.currentStepIndex)

        viewModel.setCurrentCheckpointConfirmed(true)
        assertTrue(viewModel.uiState.value.isCurrentCheckpointConfirmed)
        assertTrue(viewModel.uiState.value.canGoForward)

        viewModel.goToNextStep()
        viewModel.goToPreviousStep()
        assertEquals(1, viewModel.uiState.value.currentStepIndex)
        assertTrue(viewModel.uiState.value.isCurrentCheckpointConfirmed)

        viewModel.setCurrentCheckpointConfirmed(false)
        assertFalse(viewModel.uiState.value.canGoForward)
    }

    @Test
    fun `last checkpoint must be confirmed before finishing the lesson`() {
        val checkpoint =
            LessonCheckpoint(
                id = "final-check",
                prompt = "He completado la práctica.",
            )
        val viewModel =
            CurrentLessonViewModel(
                lessonId = "lesson",
                findLessonById = { id ->
                    if (id == "lesson") {
                        LessonContent(
                            lessonId = id,
                            steps =
                                listOf(
                                    sampleStep("Uno"),
                                    sampleStep("Práctica final", checkpoint),
                                ),
                        )
                    } else {
                        null
                    }
                },
            )

        viewModel.goToNextStep()

        assertTrue(viewModel.uiState.value.isLastStep)
        assertFalse(viewModel.uiState.value.canShowCompletion)

        viewModel.setCurrentCheckpointConfirmed(true)

        assertTrue(viewModel.uiState.value.canShowCompletion)

        viewModel.goToNextStep()

        assertTrue(viewModel.uiState.value.isCompletionVisible)
        assertFalse(viewModel.uiState.value.canShowCompletion)
    }

    private fun sampleLessonContent(): LessonContent =
        LessonContent(
            lessonId = "lesson",
            steps =
                listOf(
                    sampleStep("Uno"),
                    sampleStep("Dos"),
                    sampleStep("Tres"),
                ),
        )

    private fun sampleStep(
        title: String,
        checkpoint: LessonCheckpoint? = null,
    ): LessonStep =
        LessonStep(
            title = title,
            body = "Contenido",
            visual =
                LessonVisualContent.IconText(
                    title = "Visual",
                    body = "Detalle",
                ),
            checkpoint = checkpoint,
        )
}
