package dev.kuromiichi.damprep.ui.settings

import dev.kuromiichi.damprep.data.preferences.PronounPreference
import dev.kuromiichi.damprep.data.preferences.TargetOperatingSystem
import dev.kuromiichi.damprep.data.preferences.ThemePreference
import dev.kuromiichi.damprep.testing.ViewModelTest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class SettingsViewModelTest : ViewModelTest() {
    @Test
    fun `settings exposes saved preferences and progress`() {
        runBlocking {
            preferencesRepository.completeOnboarding("Emma", PronounPreference.Feminine, TargetOperatingSystem.MacOS)
            lessonProgressRepository.saveLessonStep("welcome", 1)
        }
        val viewModel = createViewModel()

        val state = assertNotNull(viewModel.uiState.value)

        assertEquals("Emma", state.studentNameDraft)
        assertEquals(PronounPreference.Feminine, state.pronounPreference)
        assertEquals(TargetOperatingSystem.MacOS, state.targetOperatingSystem)
        assertTrue(state.hasLessonProgress)
    }

    @Test
    fun `name draft survives external changes until it is committed`() {
        runBlocking { preferencesRepository.setStudentName("Emma") }
        val viewModel = createViewModel()

        viewModel.updateStudentNameDraft("Em")
        runBlocking { preferencesRepository.setStudentName("Emma externa") }

        assertEquals("Em", viewModel.uiState.value?.studentNameDraft)
    }

    @Test
    fun `committing the name saves it and follows the saved value again`() {
        runBlocking { preferencesRepository.setStudentName("Emma") }
        val viewModel = createViewModel()

        viewModel.updateStudentNameDraft("Ari")
        viewModel.commitStudentName()

        assertEquals("Ari", savedPreferences().studentName)
        runBlocking { preferencesRepository.setStudentName("Noa") }
        assertEquals("Noa", viewModel.uiState.value?.studentNameDraft)
    }

    @Test
    fun `preference choices are saved`() {
        val viewModel = createViewModel()

        viewModel.setPronounPreference(PronounPreference.Masculine)
        viewModel.setTargetOperatingSystem(TargetOperatingSystem.Linux)
        viewModel.setThemePreference(ThemePreference.Dark)
        viewModel.setUnlockAllLessons(true)

        val state = assertNotNull(viewModel.uiState.value)
        assertEquals(PronounPreference.Masculine, state.pronounPreference)
        assertEquals(TargetOperatingSystem.Linux, state.targetOperatingSystem)
        assertEquals(ThemePreference.Dark, state.themePreference)
        assertTrue(state.unlockAllLessons)
    }

    @Test
    fun `resetting progress clears completed lessons and saved steps`() {
        runBlocking {
            lessonProgressRepository.completeLesson("welcome")
            lessonProgressRepository.saveLessonStep("software", 2)
        }
        val viewModel = createViewModel()

        viewModel.resetLessonProgress()

        assertFalse(assertNotNull(viewModel.uiState.value).hasLessonProgress)
    }

    private fun createViewModel() = SettingsViewModel(preferencesRepository, lessonProgressRepository).also { it.uiState.observed() }

    private fun savedPreferences() = runBlocking { preferencesRepository.preferences.first() }
}
