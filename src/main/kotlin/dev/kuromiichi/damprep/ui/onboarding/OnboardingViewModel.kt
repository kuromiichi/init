package dev.kuromiichi.damprep.ui.onboarding

import dev.kuromiichi.damprep.data.preferences.PronounPreference
import dev.kuromiichi.damprep.ui.theme.ThemePreference
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class OnboardingCompletion(
    val studentName: String,
    val pronounPreference: PronounPreference,
)

data class OnboardingUiState(
    val steps: List<OnboardingStep> = onboardingSteps,
    val currentStepIndex: Int = 0,
    val studentName: String = "",
    val pronounPreference: PronounPreference = PronounPreference.Neutral,
    val themePreference: ThemePreference = ThemePreference.System,
) {
    val currentStep: OnboardingStep = steps[currentStepIndex]
    val isLastStep: Boolean = currentStepIndex == steps.lastIndex
    val isFirstStep: Boolean = currentStepIndex == 0
    val normalizedName: String = studentName.trim()
    val canContinue: Boolean =
        currentStep.kind != OnboardingStepKind.Personalization || normalizedName.isNotEmpty()
}

class OnboardingViewModel(
    initialThemePreference: ThemePreference,
    private val steps: List<OnboardingStep> = onboardingSteps,
) {
    private val _uiState = MutableStateFlow(
        OnboardingUiState(
            steps = steps,
            themePreference = initialThemePreference,
        ),
    )
    val uiState: StateFlow<OnboardingUiState> = _uiState.asStateFlow()

    fun syncThemePreference(themePreference: ThemePreference) {
        _uiState.update { state ->
            state.copy(themePreference = themePreference)
        }
    }

    fun updateThemePreference(themePreference: ThemePreference) {
        _uiState.update { state ->
            state.copy(themePreference = themePreference)
        }
    }

    fun updateStudentName(studentName: String) {
        _uiState.update { state ->
            state.copy(studentName = studentName)
        }
    }

    fun updatePronounPreference(pronounPreference: PronounPreference) {
        _uiState.update { state ->
            state.copy(pronounPreference = pronounPreference)
        }
    }

    fun goToPreviousStep() {
        _uiState.update { state ->
            if (state.isFirstStep) {
                state
            } else {
                state.copy(currentStepIndex = state.currentStepIndex - 1)
            }
        }
    }

    fun continueOnboarding(): OnboardingCompletion? {
        val state = _uiState.value
        if (!state.canContinue) {
            return null
        }

        if (state.isLastStep) {
            return OnboardingCompletion(
                studentName = state.normalizedName,
                pronounPreference = state.pronounPreference,
            )
        }

        _uiState.value = state.copy(currentStepIndex = state.currentStepIndex + 1)
        return null
    }
}
