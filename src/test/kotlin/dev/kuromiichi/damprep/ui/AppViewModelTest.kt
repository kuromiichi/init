package dev.kuromiichi.damprep.ui

import dev.kuromiichi.damprep.data.preferences.PronounPreference
import dev.kuromiichi.damprep.data.preferences.ThemePreference
import dev.kuromiichi.damprep.data.preferences.UserPreferences
import dev.kuromiichi.damprep.ui.navigation.AppRoute
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class AppViewModelTest {
    @Test
    fun `app state is loading until preferences arrive`() {
        val state = AppUiState()

        assertTrue(state.isLoading)
        assertEquals(ThemePreference.System, state.themePreference)
        assertEquals(AppRoute.Onboarding, state.startDestination)
    }

    @Test
    fun `app state routes to onboarding until it is completed`() {
        val state =
            AppUiState(
                preferences =
                    UserPreferences(
                        studentName = "Emma",
                        pronounPreference = PronounPreference.Feminine,
                        themePreference = ThemePreference.Dark,
                        onboardingCompleted = false,
                    ),
                completedLessonIds = emptySet(),
            )

        assertEquals(ThemePreference.Dark, state.themePreference)
        assertEquals(AppRoute.Onboarding, state.startDestination)
    }

    @Test
    fun `app state routes to home after onboarding`() {
        val state =
            AppUiState(
                preferences =
                    UserPreferences(
                        studentName = "Emma",
                        pronounPreference = PronounPreference.Feminine,
                        themePreference = ThemePreference.Light,
                        onboardingCompleted = true,
                    ),
                completedLessonIds = emptySet(),
            )

        assertEquals(ThemePreference.Light, state.themePreference)
        assertEquals(AppRoute.Home, state.startDestination)
    }
}
