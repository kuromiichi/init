package dev.kuromiichi.init.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import dev.kuromiichi.init.ui.common.LoadingScreen
import dev.kuromiichi.init.ui.home.HomeScreen
import dev.kuromiichi.init.ui.home.HomeViewModel
import dev.kuromiichi.init.ui.lessons.CurrentLessonScreen
import dev.kuromiichi.init.ui.lessons.LessonListScreen
import dev.kuromiichi.init.ui.lessons.LessonListViewModel
import dev.kuromiichi.init.ui.notes.NotesScreen
import dev.kuromiichi.init.ui.onboarding.OnboardingScreen
import dev.kuromiichi.init.ui.settings.SettingsScreen
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun AppNavHost(
    navController: NavHostController,
    startDestination: AppRoute,
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
    ) {
        composable<AppRoute.Onboarding> {
            OnboardingScreen(
                onFinished = {
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
            val homeViewModel = koinViewModel<HomeViewModel>()
            val homeUiState by homeViewModel.uiState.collectAsStateWithLifecycle()
            val uiState = homeUiState

            if (uiState == null) {
                LoadingScreen()
            } else {
                HomeScreen(
                    uiState = uiState,
                    onContinueLesson = {
                        uiState.continueLessonId?.let { lessonId ->
                            navController.navigate(AppRoute.Lesson(lessonId))
                        }
                    },
                    onShowLessons = { navController.navigate(AppRoute.Lessons) },
                    onShowNotes = { navController.navigate(AppRoute.Notes) },
                    onShowSettings = { navController.navigate(AppRoute.Settings) },
                )
            }
        }

        composable<AppRoute.Lesson> {
            CurrentLessonScreen(
                onBack = { navController.popBackStack() },
                onLessonFinished = { navController.popBackStack() },
            )
        }

        composable<AppRoute.Lessons> {
            val lessonListViewModel = koinViewModel<LessonListViewModel>()
            val lessonListUiState by lessonListViewModel.uiState.collectAsStateWithLifecycle()
            val uiState = lessonListUiState

            if (uiState == null) {
                LoadingScreen()
            } else {
                LessonListScreen(
                    uiState = uiState,
                    onBack = { navController.popBackStack() },
                    onLessonSelected = { lessonId ->
                        navController.navigate(AppRoute.Lesson(lessonId))
                    },
                )
            }
        }

        composable<AppRoute.Notes> {
            NotesScreen(onBack = { navController.popBackStack() })
        }

        composable<AppRoute.Settings> {
            SettingsScreen(onBack = { navController.popBackStack() })
        }
    }
}
