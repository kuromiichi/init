package dev.kuromiichi.damprep.data.preferences

import dev.kuromiichi.damprep.ui.theme.ThemePreference

data class UserPreferences(
    val studentName: String = "",
    val themePreference: ThemePreference = ThemePreference.System,
    val pronounPreference: PronounPreference = PronounPreference.Neutral,
    val targetOperatingSystem: TargetOperatingSystem? = null,
    val onboardingCompleted: Boolean = false,
)
