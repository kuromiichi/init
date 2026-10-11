package dev.kuromiichi.init.ui

import dev.kuromiichi.init.data.preferences.PronounPreference
import dev.kuromiichi.init.data.preferences.ThemePreference
import dev.kuromiichi.init.data.preferences.UserPreferences
import dev.kuromiichi.init.ui.navigation.AppRoute
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class AppUiStateTest {
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
            )

        assertEquals(ThemePreference.Light, state.themePreference)
        assertEquals(AppRoute.Home, state.startDestination)
    }
}
