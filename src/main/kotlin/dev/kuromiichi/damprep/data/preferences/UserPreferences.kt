package dev.kuromiichi.damprep.data.preferences

import dev.kuromiichi.damprep.ui.theme.ThemePreference

data class UserPreferences(
    val studentName: String = "",
    val themePreference: ThemePreference = ThemePreference.System,
    val onboardingCompleted: Boolean = false,
)
