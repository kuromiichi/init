package dev.kuromiichi.damprep.ui.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.kuromiichi.damprep.data.backup.BackupImportException
import dev.kuromiichi.damprep.data.backup.BackupRepository
import dev.kuromiichi.damprep.data.preferences.PronounPreference
import dev.kuromiichi.damprep.data.preferences.TargetOperatingSystem
import dev.kuromiichi.damprep.data.preferences.ThemePreference
import dev.kuromiichi.damprep.data.preferences.UserPreferencesRepository
import dev.kuromiichi.damprep.ui.common.BackupOperationState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.File

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
    val backupState: BackupOperationState = BackupOperationState.Idle,
    val isFinished: Boolean = false,
) {
    val currentStep: OnboardingStep = steps[currentStepIndex]
    val isLastStep: Boolean = currentStepIndex == steps.lastIndex
    val isFirstStep: Boolean = currentStepIndex == 0
    val normalizedName: String = studentName.trim()
    val canContinue: Boolean =
        when (currentStep.kind) {
            OnboardingStepKind.Import -> backupState != BackupOperationState.InProgress
            OnboardingStepKind.OperatingSystem ->
                targetOperatingSystem != null && isTargetOperatingSystemConfirmed
            OnboardingStepKind.Personalization -> normalizedName.isNotEmpty()
            else -> true
        }
}

class OnboardingViewModel(
    private val preferencesRepository: UserPreferencesRepository,
    private val backupRepository: BackupRepository,
    initialTargetOperatingSystem: TargetOperatingSystem? = TargetOperatingSystem.detect(),
    private val steps: List<OnboardingStep> = onboardingSteps,
) : ViewModel() {
    private val _uiState =
        MutableStateFlow(
            OnboardingUiState(
                steps = steps,
                targetOperatingSystem = initialTargetOperatingSystem,
            ),
        )

    /** El tema procede siempre de las preferencias guardadas, porque se aplica a toda la app. */
    val uiState: StateFlow<OnboardingUiState> =
        combine(_uiState, preferencesRepository.preferences) { state, preferences ->
            state.copy(themePreference = preferences.themePreference)
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), _uiState.value)

    fun updateThemePreference(themePreference: ThemePreference) {
        viewModelScope.launch { preferencesRepository.setThemePreference(themePreference) }
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

    fun importBackup(file: File) {
        if (_uiState.value.backupState == BackupOperationState.InProgress) return
        _uiState.update { state -> state.copy(backupState = BackupOperationState.InProgress) }

        viewModelScope.launch {
            try {
                backupRepository.importFrom(file)
                _uiState.update { state -> state.copy(backupState = BackupOperationState.Imported, isFinished = true) }
            } catch (exception: BackupImportException) {
                _uiState.update { state -> state.copy(backupState = BackupOperationState.Failed(exception.message.orEmpty())) }
            }
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

    fun continueOnboarding() {
        val state = _uiState.value
        if (!state.canContinue || state.isFinished) {
            return
        }

        if (!state.isLastStep) {
            _uiState.value = state.copy(currentStepIndex = state.currentStepIndex + 1)
            return
        }

        val completion = state.toCompletion() ?: return
        viewModelScope.launch {
            preferencesRepository.completeOnboarding(
                studentName = completion.studentName,
                pronounPreference = completion.pronounPreference,
                targetOperatingSystem = completion.targetOperatingSystem,
            )
            _uiState.update { current -> current.copy(isFinished = true) }
        }
    }

    private fun OnboardingUiState.toCompletion(): OnboardingCompletion? {
        if (!isTargetOperatingSystemConfirmed) return null
        val targetOperatingSystem = targetOperatingSystem ?: return null

        return OnboardingCompletion(
            studentName = normalizedName,
            pronounPreference = pronounPreference,
            targetOperatingSystem = targetOperatingSystem,
        )
    }
}
