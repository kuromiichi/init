package dev.kuromiichi.damprep

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import dev.kuromiichi.damprep.data.preferences.UserPreferencesRepository
import dev.kuromiichi.damprep.data.preferences.createUserPreferencesDataStore
import dev.kuromiichi.damprep.ui.AppViewModel
import dev.kuromiichi.damprep.ui.navigation.AppNavHost
import dev.kuromiichi.damprep.ui.theme.DamPrepTheme
import dev.kuromiichi.damprep.ui.window.WindowSizeGuard

@Composable
fun App() {
    val navController = rememberNavController()
    val coroutineScope = rememberCoroutineScope()
    val appViewModel =
        remember {
            AppViewModel(
                preferencesRepository = UserPreferencesRepository(createUserPreferencesDataStore()),
                scope = coroutineScope,
            )
        }
    val uiState by appViewModel.uiState.collectAsState()

    DamPrepTheme(themePreference = uiState.themePreference) {
        WindowSizeGuard {
            val preferences = uiState.preferences

            if (uiState.isLoading || preferences == null) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background,
                ) {}
            } else {
                AppNavHost(
                    navController = navController,
                    startDestination = uiState.startDestination,
                    studentName = preferences.studentName,
                    pronounPreference = preferences.pronounPreference,
                    targetOperatingSystem = preferences.targetOperatingSystem,
                    themePreference = preferences.themePreference,
                    onThemePreferenceChanged = appViewModel::setThemePreference,
                    onStudentNameChanged = appViewModel::setStudentName,
                    onPronounPreferenceChanged = appViewModel::setPronounPreference,
                    onTargetOperatingSystemChanged = appViewModel::setTargetOperatingSystem,
                    onOnboardingFinished = appViewModel::completeOnboarding,
                )
            }
        }
    }
}
