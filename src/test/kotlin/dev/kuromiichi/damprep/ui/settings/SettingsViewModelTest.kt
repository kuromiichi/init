package dev.kuromiichi.damprep.ui.settings

import dev.kuromiichi.damprep.data.preferences.PronounPreference
import dev.kuromiichi.damprep.data.preferences.TargetOperatingSystem
import dev.kuromiichi.damprep.ui.theme.ThemePreference
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class SettingsViewModelTest {
    @Test
    fun `settings sync keeps focused name draft intact`() {
        val viewModel = SettingsViewModel(
            studentName = "Emma",
            pronounPreference = PronounPreference.Feminine,
            targetOperatingSystem = TargetOperatingSystem.MacOS,
            themePreference = ThemePreference.Dark,
        )

        viewModel.updateStudentNameFocus(isFocused = true)
        viewModel.updateStudentNameDraft("Em")
        viewModel.syncPreferences(
            studentName = "Emma externa",
            pronounPreference = PronounPreference.Neutral,
            targetOperatingSystem = TargetOperatingSystem.Linux,
            themePreference = ThemePreference.Light,
        )

        val state = viewModel.uiState.value

        assertEquals("Emma externa", state.savedStudentName)
        assertEquals("Em", state.studentNameDraft)
        assertEquals(PronounPreference.Neutral, state.pronounPreference)
        assertEquals(TargetOperatingSystem.Linux, state.targetOperatingSystem)
        assertEquals(ThemePreference.Light, state.themePreference)
    }

    @Test
    fun `settings sync refreshes name draft when it is not focused`() {
        val viewModel = SettingsViewModel(
            studentName = "Emma",
            pronounPreference = PronounPreference.Feminine,
            targetOperatingSystem = TargetOperatingSystem.MacOS,
            themePreference = ThemePreference.Dark,
        )

        viewModel.syncPreferences(
            studentName = "Ari",
            pronounPreference = PronounPreference.Neutral,
            targetOperatingSystem = TargetOperatingSystem.Windows,
            themePreference = ThemePreference.Light,
        )

        val state = viewModel.uiState.value

        assertEquals("Ari", state.savedStudentName)
        assertEquals("Ari", state.studentNameDraft)
        assertEquals(TargetOperatingSystem.Windows, state.targetOperatingSystem)
    }

    @Test
    fun `settings commits changed name once`() {
        val viewModel = SettingsViewModel(
            studentName = "Emma",
            pronounPreference = PronounPreference.Feminine,
            targetOperatingSystem = TargetOperatingSystem.MacOS,
            themePreference = ThemePreference.Dark,
        )

        viewModel.updateStudentNameDraft("Ari")

        assertEquals("Ari", viewModel.commitStudentName())
        assertNull(viewModel.commitStudentName())
        assertEquals("Ari", viewModel.uiState.value.savedStudentName)
    }

    @Test
    fun `settings commits draft when name field loses focus`() {
        val viewModel = SettingsViewModel(
            studentName = "Emma",
            pronounPreference = PronounPreference.Feminine,
            targetOperatingSystem = TargetOperatingSystem.MacOS,
            themePreference = ThemePreference.Dark,
        )

        viewModel.updateStudentNameFocus(isFocused = true)
        viewModel.updateStudentNameDraft("Ari")

        assertEquals("Ari", viewModel.updateStudentNameFocus(isFocused = false))
        assertEquals("Ari", viewModel.uiState.value.savedStudentName)
    }

    @Test
    fun `target operating system can be changed`() {
        val viewModel = SettingsViewModel(
            studentName = "Emma",
            pronounPreference = PronounPreference.Feminine,
            targetOperatingSystem = TargetOperatingSystem.MacOS,
            themePreference = ThemePreference.Dark,
        )

        viewModel.updateTargetOperatingSystem(TargetOperatingSystem.Linux)

        assertEquals(TargetOperatingSystem.Linux, viewModel.uiState.value.targetOperatingSystem)
    }
}
