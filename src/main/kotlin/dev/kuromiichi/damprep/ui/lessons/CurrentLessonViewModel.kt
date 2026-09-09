package dev.kuromiichi.damprep.ui.lessons

import dev.kuromiichi.damprep.data.lessons.LessonContent
import dev.kuromiichi.damprep.data.lessons.LessonContentCatalog
import dev.kuromiichi.damprep.data.lessons.LessonStep
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class CurrentLessonUiState(
    val lessonContent: LessonContent?,
    val currentStepIndex: Int = 0,
    val confirmedCheckpointIds: Set<String> = emptySet(),
    val isCompletionVisible: Boolean = false,
) {
    val currentStep: LessonStep? = lessonContent?.steps?.getOrNull(currentStepIndex)
    val stepCount: Int = lessonContent?.steps?.size ?: 0
    val isMissing: Boolean = lessonContent == null
    val canGoBack: Boolean = isCompletionVisible || currentStepIndex > 0
    val isLastStep: Boolean = lessonContent != null && currentStepIndex == stepCount - 1
    val isCurrentCheckpointConfirmed: Boolean
        get() = currentStep?.checkpoint?.id?.let(confirmedCheckpointIds::contains) ?: true
    val canGoForward: Boolean
        get() =
            lessonContent != null &&
                currentStepIndex < stepCount - 1 &&
                isCurrentCheckpointConfirmed &&
                !isCompletionVisible
    val canShowCompletion: Boolean
        get() = isLastStep && isCurrentCheckpointConfirmed && !isCompletionVisible
}

class CurrentLessonViewModel(
    lessonId: String,
    initialStepIndex: Int = 0,
    findLessonById: (String) -> LessonContent? = LessonContentCatalog::findByLessonId,
) {
    private val lessonContent = findLessonById(lessonId)
    private val _uiState =
        MutableStateFlow(
            CurrentLessonUiState(
                lessonContent = lessonContent,
                currentStepIndex = initialStepIndex.coerceIn(0, lessonContent?.steps?.lastIndex ?: 0),
            ),
        )
    val uiState: StateFlow<CurrentLessonUiState> = _uiState.asStateFlow()

    fun goToPreviousStep(): Int {
        _uiState.update { state ->
            when {
                state.isCompletionVisible -> state.copy(isCompletionVisible = false)
                state.canGoBack -> state.copy(currentStepIndex = state.currentStepIndex - 1)
                else -> state
            }
        }
        return _uiState.value.currentStepIndex
    }

    fun goToNextStep(): Int {
        _uiState.update { state ->
            when {
                state.canGoForward -> state.copy(currentStepIndex = state.currentStepIndex + 1)
                state.canShowCompletion -> state.copy(isCompletionVisible = true)
                else -> state
            }
        }
        return _uiState.value.currentStepIndex
    }

    fun setCurrentCheckpointConfirmed(confirmed: Boolean) {
        _uiState.update { state ->
            val checkpointId = state.currentStep?.checkpoint?.id ?: return@update state
            state.copy(
                confirmedCheckpointIds =
                    if (confirmed) {
                        state.confirmedCheckpointIds + checkpointId
                    } else {
                        state.confirmedCheckpointIds - checkpointId
                    },
            )
        }
    }
}
