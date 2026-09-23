package dev.kuromiichi.damprep.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.kuromiichi.damprep.data.preferences.ThemePreference
import dev.kuromiichi.damprep.data.preferences.UserPreferences
import dev.kuromiichi.damprep.data.preferences.UserPreferencesRepository
import dev.kuromiichi.damprep.ui.navigation.AppRoute
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

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
    preferencesRepository: UserPreferencesRepository,
) : ViewModel() {
    val uiState: StateFlow<AppUiState> =
        preferencesRepository.preferences
            .map(::AppUiState)
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), AppUiState())
}
