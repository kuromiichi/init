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
) {
    val currentStep: LessonStep? = lessonContent?.steps?.getOrNull(currentStepIndex)
    val stepCount: Int = lessonContent?.steps?.size ?: 0
    val isMissing: Boolean = lessonContent == null
    val canGoBack: Boolean = currentStepIndex > 0
    val isLastStep: Boolean = lessonContent != null && currentStepIndex == stepCount - 1
    val isCurrentCheckpointConfirmed: Boolean
        get() = currentStep?.checkpoint?.id?.let(confirmedCheckpointIds::contains) ?: true
    val canGoForward: Boolean
        get() =
            lessonContent != null &&
                currentStepIndex < stepCount - 1 &&
                isCurrentCheckpointConfirmed
    val canFinish: Boolean
        get() = isLastStep && isCurrentCheckpointConfirmed
}

class CurrentLessonViewModel(
    lessonId: String,
    findLessonById: (String) -> LessonContent? = LessonContentCatalog::findByLessonId,
) {
    private val _uiState =
        MutableStateFlow(
            CurrentLessonUiState(
                lessonContent = findLessonById(lessonId),
            ),
        )
    val uiState: StateFlow<CurrentLessonUiState> = _uiState.asStateFlow()

    fun goToPreviousStep() {
        _uiState.update { state ->
            if (state.canGoBack) {
                state.copy(currentStepIndex = state.currentStepIndex - 1)
            } else {
                state
            }
        }
    }

    fun goToNextStep() {
        _uiState.update { state ->
            if (state.canGoForward) {
                state.copy(currentStepIndex = state.currentStepIndex + 1)
            } else {
                state
            }
        }
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
