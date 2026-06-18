package dev.kuromiichi.damprep

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import dev.kuromiichi.damprep.data.preferences.UserPreferences
import dev.kuromiichi.damprep.data.preferences.UserPreferencesRepository
import dev.kuromiichi.damprep.data.preferences.createUserPreferencesDataStore
import dev.kuromiichi.damprep.ui.navigation.AppNavHost
import dev.kuromiichi.damprep.ui.navigation.AppRoute
import dev.kuromiichi.damprep.ui.theme.DamPrepTheme
import dev.kuromiichi.damprep.ui.theme.ThemePreference
import dev.kuromiichi.damprep.ui.window.WindowSizeGuard
import kotlinx.coroutines.launch

@Composable
fun App() {
    val navController = rememberNavController()
    val preferencesRepository = remember {
        UserPreferencesRepository(createUserPreferencesDataStore())
    }
    val coroutineScope = rememberCoroutineScope()
    val userPreferences by produceState<UserPreferences?>(initialValue = null) {
        preferencesRepository.preferences.collect { value = it }
    }
    val themePreference = userPreferences?.themePreference ?: ThemePreference.System

    DamPrepTheme(themePreference = themePreference) {
        WindowSizeGuard {
            val preferences = userPreferences

            if (preferences == null) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background,
                ) {}
            } else {
                val startDestination = if (preferences.onboardingCompleted) {
                    AppRoute.Home
                } else {
                    AppRoute.Onboarding
                }

                AppNavHost(
                    navController = navController,
                    startDestination = startDestination,
                    studentName = preferences.studentName,
                    pronounPreference = preferences.pronounPreference,
                    themePreference = preferences.themePreference,
                    onThemePreferenceChanged = { nextThemePreference ->
                        coroutineScope.launch {
                            preferencesRepository.setThemePreference(nextThemePreference)
                        }
                    },
                    onStudentNameChanged = { studentName ->
                        coroutineScope.launch {
                            preferencesRepository.setStudentName(studentName)
                        }
                    },
                    onPronounPreferenceChanged = { pronounPreference ->
                        coroutineScope.launch {
                            preferencesRepository.setPronounPreference(pronounPreference)
                        }
                    },
                    onOnboardingFinished = { studentName, pronounPreference ->
                        coroutineScope.launch {
                            preferencesRepository.completeOnboarding(
                                studentName = studentName,
                                pronounPreference = pronounPreference,
                            )
                        }
                    },
                )
            }
        }
    }
}
