package dev.kuromiichi.damprep.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import dev.kuromiichi.damprep.data.preferences.PronounPreference
import dev.kuromiichi.damprep.ui.home.HomeScreen
import dev.kuromiichi.damprep.ui.lessons.CurrentLessonScreen
import dev.kuromiichi.damprep.ui.lessons.LessonListScreen
import dev.kuromiichi.damprep.ui.notes.NotesScreen
import dev.kuromiichi.damprep.ui.onboarding.OnboardingScreen
import dev.kuromiichi.damprep.ui.settings.SettingsScreen
import dev.kuromiichi.damprep.ui.theme.ThemePreference

@Composable
fun AppNavHost(
    navController: NavHostController,
    startDestination: String,
    studentName: String,
    pronounPreference: PronounPreference,
    themePreference: ThemePreference,
    onThemePreferenceChanged: (ThemePreference) -> Unit,
    onStudentNameChanged: (String) -> Unit,
    onPronounPreferenceChanged: (PronounPreference) -> Unit,
    onOnboardingFinished: (String, PronounPreference) -> Unit,
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
    ) {
        composable(AppRoute.Onboarding) {
            OnboardingScreen(
                themePreference = themePreference,
                onThemePreferenceChanged = onThemePreferenceChanged,
                onFinished = { name, selectedPronounPreference ->
                    onOnboardingFinished(name, selectedPronounPreference)
                    navController.navigate(AppRoute.Home) {
                        popUpTo(AppRoute.Onboarding) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                },
            )
        }

        composable(AppRoute.Home) {
            HomeScreen(
                studentName = studentName,
                pronounPreference = pronounPreference,
                completedLessons = 0,
                onContinueLesson = {
                    navController.navigate(AppRoute.CurrentLesson)
                },
                onShowLessons = {
                    navController.navigate(AppRoute.Lessons)
                },
                onShowNotes = {
                    navController.navigate(AppRoute.Notes)
                },
                onShowSettings = {
                    navController.navigate(AppRoute.Settings)
                },
            )
        }

        composable(AppRoute.CurrentLesson) {
            CurrentLessonScreen(onBack = { navController.popBackStack() })
        }

        composable(AppRoute.Lessons) {
            LessonListScreen(onBack = { navController.popBackStack() })
        }

        composable(AppRoute.Notes) {
            NotesScreen(onBack = { navController.popBackStack() })
        }

        composable(AppRoute.Settings) {
            SettingsScreen(
                studentName = studentName,
                pronounPreference = pronounPreference,
                themePreference = themePreference,
                onStudentNameChanged = onStudentNameChanged,
                onPronounPreferenceChanged = onPronounPreferenceChanged,
                onThemePreferenceChanged = onThemePreferenceChanged,
                onBack = { navController.popBackStack() },
            )
        }
    }
}
