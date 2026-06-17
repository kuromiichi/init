package dev.kuromiichi.damprep.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import dev.kuromiichi.damprep.data.preferences.PronounPreference
import dev.kuromiichi.damprep.ui.home.HomeScreen
import dev.kuromiichi.damprep.ui.onboarding.OnboardingScreen
import dev.kuromiichi.damprep.ui.theme.ThemePreference

@Composable
fun AppNavHost(
    navController: NavHostController,
    startDestination: String,
    studentName: String,
    pronounPreference: PronounPreference,
    themePreference: ThemePreference,
    onThemePreferenceChanged: (ThemePreference) -> Unit,
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
            )
        }
    }
}
