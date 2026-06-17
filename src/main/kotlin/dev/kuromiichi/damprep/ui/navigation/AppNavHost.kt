package dev.kuromiichi.damprep.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import dev.kuromiichi.damprep.ui.home.HomeScreen
import dev.kuromiichi.damprep.ui.onboarding.OnboardingScreen
import dev.kuromiichi.damprep.ui.theme.ThemePreference

@Composable
fun AppNavHost(
    navController: NavHostController,
    studentName: String,
    themePreference: ThemePreference,
    onThemePreferenceChanged: (ThemePreference) -> Unit,
    onStudentNameReceived: (String) -> Unit,
) {
    NavHost(
        navController = navController,
        startDestination = AppRoute.Onboarding,
    ) {
        composable(AppRoute.Onboarding) {
            OnboardingScreen(
                themePreference = themePreference,
                onThemePreferenceChanged = onThemePreferenceChanged,
                onFinished = { name ->
                    onStudentNameReceived(name)
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
            HomeScreen(studentName = studentName)
        }
    }
}
