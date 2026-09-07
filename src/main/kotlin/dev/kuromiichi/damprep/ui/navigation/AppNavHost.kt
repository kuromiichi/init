package dev.kuromiichi.damprep.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import dev.kuromiichi.damprep.data.lessons.LessonCatalog
import dev.kuromiichi.damprep.data.preferences.PronounPreference
import dev.kuromiichi.damprep.data.preferences.TargetOperatingSystem
import dev.kuromiichi.damprep.data.preferences.ThemePreference
import dev.kuromiichi.damprep.ui.home.HomeScreen
import dev.kuromiichi.damprep.ui.home.HomeViewModel
import dev.kuromiichi.damprep.ui.lessons.CurrentLessonScreen
import dev.kuromiichi.damprep.ui.lessons.LessonListScreen
import dev.kuromiichi.damprep.ui.lessons.LessonListViewModel
import dev.kuromiichi.damprep.ui.notes.NotesScreen
import dev.kuromiichi.damprep.ui.onboarding.OnboardingScreen
import dev.kuromiichi.damprep.ui.settings.SettingsScreen

@Composable
fun AppNavHost(
    navController: NavHostController,
    startDestination: AppRoute,
    studentName: String,
    pronounPreference: PronounPreference,
    targetOperatingSystem: TargetOperatingSystem?,
    themePreference: ThemePreference,
    completedLessonIds: Set<String>,
    onThemePreferenceChanged: (ThemePreference) -> Unit,
    onStudentNameChanged: (String) -> Unit,
    onPronounPreferenceChanged: (PronounPreference) -> Unit,
    onTargetOperatingSystemChanged: (TargetOperatingSystem) -> Unit,
    onOnboardingFinished: (String, PronounPreference, TargetOperatingSystem) -> Unit,
    onLessonFinished: (String) -> Unit,
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
    ) {
        composable<AppRoute.Onboarding> {
            OnboardingScreen(
                themePreference = themePreference,
                detectedOperatingSystem = targetOperatingSystem ?: TargetOperatingSystem.detect(),
                onThemePreferenceChanged = onThemePreferenceChanged,
                onFinished = { name, selectedPronounPreference, selectedOperatingSystem ->
                    onOnboardingFinished(name, selectedPronounPreference, selectedOperatingSystem)
                    navController.navigate(AppRoute.Home) {
                        popUpTo<AppRoute.Onboarding> {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                },
            )
        }

        composable<AppRoute.Home> {
            val homeViewModel = remember { HomeViewModel() }
            val homeUiState =
                homeViewModel.createUiState(
                    studentName = studentName,
                    pronounPreference = pronounPreference,
                    completedLessonIds = completedLessonIds,
                )

            HomeScreen(
                uiState = homeUiState,
                onContinueLesson = {
                    homeUiState.continueLessonId
                        ?.let { lessonId -> navController.navigate(AppRoute.Lesson(lessonId)) }
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

        composable<AppRoute.Lesson> { backStackEntry ->
            val route = backStackEntry.toRoute<AppRoute.Lesson>()

            CurrentLessonScreen(
                lessonId = route.lessonId,
                lessonTitle =
                    LessonCatalog.lessons
                        .firstOrNull { lesson -> lesson.id == route.lessonId }
                        ?.title
                        ?: "Lección",
                targetOperatingSystem = targetOperatingSystem ?: TargetOperatingSystem.detect(),
                onTargetOperatingSystemChanged = onTargetOperatingSystemChanged,
                onBack = { navController.popBackStack() },
                onLessonFinished = {
                    onLessonFinished(route.lessonId)
                    navController.popBackStack()
                },
            )
        }

        composable<AppRoute.Lessons> {
            val lessonListViewModel = remember { LessonListViewModel() }

            LessonListScreen(
                uiState = lessonListViewModel.createUiState(completedLessonIds),
                onBack = { navController.popBackStack() },
                onLessonSelected = { lessonId ->
                    navController.navigate(AppRoute.Lesson(lessonId))
                },
            )
        }

        composable<AppRoute.Notes> {
            NotesScreen(onBack = { navController.popBackStack() })
        }

        composable<AppRoute.Settings> {
            SettingsScreen(
                studentName = studentName,
                pronounPreference = pronounPreference,
                targetOperatingSystem = targetOperatingSystem,
                themePreference = themePreference,
                onStudentNameChanged = onStudentNameChanged,
                onPronounPreferenceChanged = onPronounPreferenceChanged,
                onTargetOperatingSystemChanged = onTargetOperatingSystemChanged,
                onThemePreferenceChanged = onThemePreferenceChanged,
                onBack = { navController.popBackStack() },
            )
        }
    }
}
