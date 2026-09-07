package dev.kuromiichi.damprep.ui

import dev.kuromiichi.damprep.data.preferences.PronounPreference
import dev.kuromiichi.damprep.data.preferences.TargetOperatingSystem
import dev.kuromiichi.damprep.data.preferences.ThemePreference
import dev.kuromiichi.damprep.data.preferences.UserPreferences
import dev.kuromiichi.damprep.data.preferences.UserPreferencesRepository
import dev.kuromiichi.damprep.ui.navigation.AppRoute
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class AppUiState(
    val preferences: UserPreferences? = null,
) {
    val isLoading: Boolean = preferences == null
    val themePreference: ThemePreference = preferences?.themePreference ?: ThemePreference.System
    val startDestination: AppRoute =
        if (preferences?.onboardingCompleted == true) {
            AppRoute.Home
        } else {
            AppRoute.Onboarding
        }
}

class AppViewModel(
    private val preferencesRepository: UserPreferencesRepository,
    private val scope: CoroutineScope,
) {
    private val _uiState = MutableStateFlow(AppUiState())
    val uiState: StateFlow<AppUiState> = _uiState.asStateFlow()

    init {
        scope.launch {
            preferencesRepository.preferences.collect { preferences ->
                _uiState.value = AppUiState(preferences = preferences)
            }
        }
    }

    fun setThemePreference(themePreference: ThemePreference) {
        scope.launch {
            preferencesRepository.setThemePreference(themePreference)
        }
    }

    fun setStudentName(studentName: String) {
        scope.launch {
            preferencesRepository.setStudentName(studentName)
        }
    }

    fun setPronounPreference(pronounPreference: PronounPreference) {
        scope.launch {
            preferencesRepository.setPronounPreference(pronounPreference)
        }
    }

    fun setTargetOperatingSystem(targetOperatingSystem: TargetOperatingSystem) {
        scope.launch {
            preferencesRepository.setTargetOperatingSystem(targetOperatingSystem)
        }
    }

    fun completeOnboarding(
        studentName: String,
        pronounPreference: PronounPreference,
        targetOperatingSystem: TargetOperatingSystem,
    ) {
        scope.launch {
            preferencesRepository.completeOnboarding(
                studentName = studentName,
                pronounPreference = pronounPreference,
                targetOperatingSystem = targetOperatingSystem,
            )
        }
    }
}
