package dev.kuromiichi.damprep.ui.onboarding

import dev.kuromiichi.damprep.data.preferences.PronounPreference
import dev.kuromiichi.damprep.data.preferences.TargetOperatingSystem
import dev.kuromiichi.damprep.data.preferences.ThemePreference
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class OnboardingCompletion(
    val studentName: String,
    val pronounPreference: PronounPreference,
    val targetOperatingSystem: TargetOperatingSystem,
)

data class OnboardingUiState(
    val steps: List<OnboardingStep> = onboardingSteps,
    val currentStepIndex: Int = 0,
    val studentName: String = "",
    val pronounPreference: PronounPreference = PronounPreference.Neutral,
    val themePreference: ThemePreference = ThemePreference.System,
    val targetOperatingSystem: TargetOperatingSystem? = null,
    val isTargetOperatingSystemConfirmed: Boolean = false,
) {
    val currentStep: OnboardingStep = steps[currentStepIndex]
    val isLastStep: Boolean = currentStepIndex == steps.lastIndex
    val isFirstStep: Boolean = currentStepIndex == 0
    val normalizedName: String = studentName.trim()
    val canContinue: Boolean =
        when (currentStep.kind) {
            OnboardingStepKind.OperatingSystem ->
                targetOperatingSystem != null && isTargetOperatingSystemConfirmed
            OnboardingStepKind.Personalization -> normalizedName.isNotEmpty()
            else -> true
        }
}

class OnboardingViewModel(
    initialThemePreference: ThemePreference,
    initialTargetOperatingSystem: TargetOperatingSystem? = TargetOperatingSystem.detect(),
    private val steps: List<OnboardingStep> = onboardingSteps,
) {
    private val _uiState =
        MutableStateFlow(
            OnboardingUiState(
                steps = steps,
                themePreference = initialThemePreference,
                targetOperatingSystem = initialTargetOperatingSystem,
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

    fun confirmTargetOperatingSystem(targetOperatingSystem: TargetOperatingSystem) {
        _uiState.update { state ->
            state.copy(
                targetOperatingSystem = targetOperatingSystem,
                isTargetOperatingSystemConfirmed = true,
            )
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
            if (!state.isTargetOperatingSystemConfirmed) {
                return null
            }
            val targetOperatingSystem = state.targetOperatingSystem ?: return null
            return OnboardingCompletion(
                studentName = state.normalizedName,
                pronounPreference = state.pronounPreference,
                targetOperatingSystem = targetOperatingSystem,
            )
        }

        _uiState.value = state.copy(currentStepIndex = state.currentStepIndex + 1)
        return null
    }
}
