package dev.kuromiichi.init.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.kuromiichi.init.data.preferences.ThemePreference
import dev.kuromiichi.init.data.preferences.UserPreferences
import dev.kuromiichi.init.data.preferences.UserPreferencesRepository
import dev.kuromiichi.init.data.update.UpdateRepository
import dev.kuromiichi.init.data.update.UpdateState
import dev.kuromiichi.init.ui.navigation.AppRoute
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
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
    private val updateRepository: UpdateRepository,
) : ViewModel() {
    val uiState: StateFlow<AppUiState> =
        preferencesRepository.preferences
            .map(::AppUiState)
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), AppUiState())

    /** `true` cuando el instalador espera a que la app se cierre para sustituirla. */
    val isRestartRequested: StateFlow<Boolean> =
        updateRepository.state
            .map { it is UpdateState.RestartPending }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), false)

    /** Al abrir la app: borra descargas antiguas y, si está activado, busca una versión nueva. */
    fun onAppStarted() {
        updateRepository.cleanUpDownloads()
        viewModelScope.launch {
            if (preferencesRepository.preferences.first().checkForUpdates) updateRepository.checkForUpdates()
        }
    }
}
