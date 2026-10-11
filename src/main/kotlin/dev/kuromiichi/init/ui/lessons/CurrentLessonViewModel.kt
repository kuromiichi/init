package dev.kuromiichi.init.ui.lessons

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import dev.kuromiichi.init.data.lessons.LessonCatalog
import dev.kuromiichi.init.data.lessons.LessonContent
import dev.kuromiichi.init.data.lessons.LessonContentCatalog
import dev.kuromiichi.init.data.lessons.LessonStep
import dev.kuromiichi.init.data.lessons.LessonSummary
import dev.kuromiichi.init.data.preferences.TargetOperatingSystem
import dev.kuromiichi.init.data.preferences.UserPreferencesRepository
import dev.kuromiichi.init.data.progress.LessonProgressRepository
import dev.kuromiichi.init.ui.navigation.AppRoute
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class CurrentLessonUiState(
    val lessonContent: LessonContent?,
    val lessonSummary: LessonSummary? = null,
    val isLoading: Boolean = false,
    val currentStepIndex: Int = 0,
    val confirmedPracticeIds: Set<String> = emptySet(),
    val isCompletionVisible: Boolean = false,
    val targetOperatingSystem: TargetOperatingSystem? = null,
    val isFinished: Boolean = false,
) {
    val currentStep: LessonStep? = lessonContent?.steps?.getOrNull(currentStepIndex)
    val stepCount: Int = lessonContent?.steps?.size ?: 0
    val isMissing: Boolean = lessonContent == null
    val canGoBack: Boolean = !isLoading && (isCompletionVisible || currentStepIndex > 0)
    val isLastStep: Boolean = lessonContent != null && currentStepIndex == stepCount - 1
    val isCurrentPracticeConfirmed: Boolean
        get() = currentStep?.practice?.id?.let(confirmedPracticeIds::contains) ?: true
    val canGoForward: Boolean
        get() =
            lessonContent != null &&
                !isLoading &&
                currentStepIndex < stepCount - 1 &&
                isCurrentPracticeConfirmed &&
                !isCompletionVisible
    val canShowCompletion: Boolean
        get() = !isLoading && isLastStep && isCurrentPracticeConfirmed && !isCompletionVisible
}

/** Estado de la visita actual a la lección, que se decide al abrirla. */
private data class LessonSession(
    val isReview: Boolean,
    val stepIndex: Int,
    val confirmedPracticeIds: Set<String> = emptySet(),
    val isCompletionVisible: Boolean = false,
    val isFinished: Boolean = false,
)

/**
 * Una lección sin completar se retoma en el último paso guardado y sigue guardando el avance.
 * Una lección completada se repasa desde el principio sin modificar el progreso guardado.
 */
class CurrentLessonViewModel(
    savedStateHandle: SavedStateHandle,
    private val lessonProgressRepository: LessonProgressRepository,
    private val preferencesRepository: UserPreferencesRepository,
    findLessonById: (String) -> LessonContent? = LessonContentCatalog::findByLessonId,
    findLessonSummary: (String) -> LessonSummary? = { id -> LessonCatalog.lessons.firstOrNull { it.id == id } },
) : ViewModel() {
    private val lessonId = savedStateHandle.toRoute<AppRoute.Lesson>().lessonId
    private val lessonContent = findLessonById(lessonId)
    private val lessonSummary = findLessonSummary(lessonId)
    private val session = MutableStateFlow<LessonSession?>(null)

    val uiState: StateFlow<CurrentLessonUiState> =
        combine(
            session.onStart { startSession() },
            preferencesRepository.preferences,
        ) { session, preferences ->
            session.toUiState(
                targetOperatingSystem = preferences.targetOperatingSystem ?: TargetOperatingSystem.detect(),
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue =
                CurrentLessonUiState(
                    lessonContent = lessonContent,
                    lessonSummary = lessonSummary,
                    isLoading = lessonContent != null,
                ),
        )

    fun goToPreviousStep() {
        val state = session.value.toUiState()
        when {
            state.isCompletionVisible -> updateSession { it.copy(isCompletionVisible = false) }
            state.canGoBack -> moveToStep(state.currentStepIndex - 1)
        }
    }

    fun goToNextStep() {
        val state = session.value.toUiState()
        when {
            state.canGoForward -> moveToStep(state.currentStepIndex + 1)
            state.canShowCompletion -> updateSession { it.copy(isCompletionVisible = true) }
        }
    }

    fun setCurrentPracticeConfirmed(confirmed: Boolean) {
        val practiceId =
            session.value
                .toUiState()
                .currentStep
                ?.practice
                ?.id ?: return
        updateSession { session ->
            session.copy(
                confirmedPracticeIds =
                    if (confirmed) {
                        session.confirmedPracticeIds + practiceId
                    } else {
                        session.confirmedPracticeIds - practiceId
                    },
            )
        }
    }

    fun setTargetOperatingSystem(targetOperatingSystem: TargetOperatingSystem) {
        viewModelScope.launch { preferencesRepository.setTargetOperatingSystem(targetOperatingSystem) }
    }

    fun finishLesson() {
        val currentSession = session.value ?: return
        if (!currentSession.isCompletionVisible) return

        viewModelScope.launch {
            if (!currentSession.isReview) lessonProgressRepository.completeLesson(lessonId)
            updateSession { it.copy(isFinished = true) }
        }
    }

    /** Se ejecuta al empezar a observar el estado; solo decide la sesión la primera vez. */
    private suspend fun startSession() {
        if (lessonContent == null || session.value != null) return

        val isReview = lessonId in lessonProgressRepository.completedLessonIds.first()
        val stepIndex =
            if (isReview) {
                0
            } else {
                lessonProgressRepository.lessonStepIndices
                    .first()[lessonId]
                    ?.coerceIn(0, lessonContent.steps.lastIndex)
                    ?: 0
            }

        session.value = LessonSession(isReview = isReview, stepIndex = stepIndex)
        if (!isReview) lessonProgressRepository.saveLessonStep(lessonId, stepIndex)
    }

    private fun moveToStep(stepIndex: Int) {
        val currentSession = session.value ?: return
        updateSession { it.copy(stepIndex = stepIndex) }

        if (!currentSession.isReview) {
            viewModelScope.launch { lessonProgressRepository.saveLessonStep(lessonId, stepIndex) }
        }
    }

    /** Las acciones leen la sesión directamente para no depender de la emisión del estado observado. */
    private fun LessonSession?.toUiState(targetOperatingSystem: TargetOperatingSystem? = null): CurrentLessonUiState =
        CurrentLessonUiState(
            lessonContent = lessonContent,
            lessonSummary = lessonSummary,
            isLoading = lessonContent != null && this == null,
            currentStepIndex = this?.stepIndex ?: 0,
            confirmedPracticeIds = this?.confirmedPracticeIds.orEmpty(),
            isCompletionVisible = this?.isCompletionVisible ?: false,
            targetOperatingSystem = targetOperatingSystem,
            isFinished = this?.isFinished ?: false,
        )

    private fun updateSession(transform: (LessonSession) -> LessonSession) {
        session.update { current -> current?.let(transform) }
    }
}
