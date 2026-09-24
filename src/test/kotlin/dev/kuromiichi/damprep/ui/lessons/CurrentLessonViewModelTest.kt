package dev.kuromiichi.damprep.ui.lessons

import androidx.lifecycle.SavedStateHandle
import dev.kuromiichi.damprep.data.lessons.LessonContent
import dev.kuromiichi.damprep.data.lessons.LessonPractice
import dev.kuromiichi.damprep.data.lessons.LessonStep
import dev.kuromiichi.damprep.data.lessons.LessonVisualContent
import dev.kuromiichi.damprep.data.lessons.paragraphs
import dev.kuromiichi.damprep.data.lessons.richText
import dev.kuromiichi.damprep.data.preferences.TargetOperatingSystem
import dev.kuromiichi.damprep.testing.ViewModelTest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

class CurrentLessonViewModelTest : ViewModelTest() {
    @Test
    fun `current lesson state reports missing lessons`() {
        val viewModel =
            createViewModel(lessonId = "missing", findLessonById = { null })

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
            createViewModel()

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
    fun `unfinished lesson resumes at its saved step`() {
        saveStep(1)

        val viewModel = createViewModel()

        assertEquals(1, viewModel.uiState.value.currentStepIndex)
        assertFalse(viewModel.uiState.value.isLoading)
    }

    @Test
    fun `saved step is clamped to lesson bounds`() {
        saveStep(99)

        val viewModel = createViewModel()

        assertEquals(2, viewModel.uiState.value.currentStepIndex)
    }

    @Test
    fun `opening a new lesson marks it as started`() {
        createViewModel()

        assertEquals(mapOf("lesson" to 0), savedSteps())
    }

    @Test
    fun `step changes are saved while the lesson is unfinished`() {
        val viewModel = createViewModel()

        viewModel.goToNextStep()
        viewModel.goToNextStep()
        viewModel.goToPreviousStep()

        assertEquals(mapOf("lesson" to 1), savedSteps())
    }

    @Test
    fun `completed lesson is reviewed from the start without saving steps`() {
        runBlocking { lessonProgressRepository.completeLesson("lesson") }

        val viewModel = createViewModel()
        viewModel.goToNextStep()

        assertEquals(1, viewModel.uiState.value.currentStepIndex)
        assertEquals(emptyMap(), savedSteps())
    }

    @Test
    fun `finishing the lesson completes it and clears its saved step`() {
        val viewModel = createViewModel()

        viewModel.goToNextStep()
        viewModel.goToNextStep()
        viewModel.finishLesson()
        assertFalse(viewModel.uiState.value.isFinished)

        viewModel.goToNextStep()
        viewModel.finishLesson()

        assertTrue(viewModel.uiState.value.isFinished)
        assertEquals(setOf("lesson"), runBlocking { lessonProgressRepository.completedLessonIds.first() })
        assertEquals(emptyMap(), savedSteps())
    }

    @Test
    fun `lesson follows the saved operating system`() {
        val viewModel = createViewModel()

        viewModel.setTargetOperatingSystem(TargetOperatingSystem.Windows)

        assertEquals(TargetOperatingSystem.Windows, viewModel.uiState.value.targetOperatingSystem)
    }

    @Test
    fun `practice blocks navigation until it is manually confirmed`() {
        val practice =
            LessonPractice(
                id = "hello-world",
                objective = richText("Ejecutar un programa."),
                statement = richText("Ejecuta el ejemplo."),
                requirements = listOf(richText("El programa termina sin errores.")),
                confirmation = richText("He ejecutado el programa."),
            )
        val viewModel =
            createViewModel(
                findLessonById = { id ->
                    if (id == "lesson") {
                        LessonContent(
                            lessonId = id,
                            steps =
                                listOf(
                                    sampleStep("Uno"),
                                    sampleStep("Dos", practice),
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
        assertFalse(viewModel.uiState.value.isCurrentPracticeConfirmed)
        assertFalse(viewModel.uiState.value.canGoForward)

        viewModel.goToNextStep()
        assertEquals(1, viewModel.uiState.value.currentStepIndex)

        viewModel.setCurrentPracticeConfirmed(true)
        assertTrue(viewModel.uiState.value.isCurrentPracticeConfirmed)
        assertTrue(viewModel.uiState.value.canGoForward)

        viewModel.goToNextStep()
        viewModel.goToPreviousStep()
        assertEquals(1, viewModel.uiState.value.currentStepIndex)
        assertTrue(viewModel.uiState.value.isCurrentPracticeConfirmed)

        viewModel.setCurrentPracticeConfirmed(false)
        assertFalse(viewModel.uiState.value.canGoForward)
    }

    @Test
    fun `last practice must be confirmed before finishing the lesson`() {
        val practice =
            LessonPractice(
                id = "final-check",
                objective = richText("Aplicar la lección."),
                statement = richText("Completa la práctica."),
                requirements = listOf(richText("Cumple el enunciado.")),
                confirmation = richText("He completado la práctica."),
            )
        val viewModel =
            createViewModel(
                findLessonById = { id ->
                    if (id == "lesson") {
                        LessonContent(
                            lessonId = id,
                            steps =
                                listOf(
                                    sampleStep("Uno"),
                                    sampleStep("Práctica final", practice),
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

        viewModel.setCurrentPracticeConfirmed(true)

        assertTrue(viewModel.uiState.value.canShowCompletion)

        viewModel.goToNextStep()

        assertTrue(viewModel.uiState.value.isCompletionVisible)
        assertFalse(viewModel.uiState.value.canShowCompletion)
    }

    private fun createViewModel(
        lessonId: String = "lesson",
        findLessonById: (String) -> LessonContent? = { id -> if (id == "lesson") sampleLessonContent() else null },
    ): CurrentLessonViewModel =
        CurrentLessonViewModel(
            savedStateHandle = SavedStateHandle(mapOf("lessonId" to lessonId)),
            lessonProgressRepository = lessonProgressRepository,
            preferencesRepository = preferencesRepository,
            findLessonById = findLessonById,
        ).also { it.uiState.observed() }

    private fun saveStep(stepIndex: Int) {
        runBlocking { lessonProgressRepository.saveLessonStep("lesson", stepIndex) }
    }

    private fun savedSteps(): Map<String, Int> = runBlocking { lessonProgressRepository.lessonStepIndices.first() }

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
        practice: LessonPractice? = null,
    ): LessonStep =
        LessonStep(
            title = title,
            explanation = paragraphs("Contenido"),
            visual =
                LessonVisualContent.IconText(
                    title = "Visual",
                    body = "Detalle",
                ),
            practice = practice,
        )
}
