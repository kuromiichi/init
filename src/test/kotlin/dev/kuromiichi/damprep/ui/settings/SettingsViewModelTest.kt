package dev.kuromiichi.damprep.ui.settings

import dev.kuromiichi.damprep.data.preferences.PronounPreference
import dev.kuromiichi.damprep.data.preferences.TargetOperatingSystem
import dev.kuromiichi.damprep.data.preferences.ThemePreference
import dev.kuromiichi.damprep.testing.ViewModelTest
import dev.kuromiichi.damprep.ui.common.BackupOperationState
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import java.nio.file.Files
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertIs
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
    fun `discarding the name draft keeps the saved name`() {
        val viewModel = createViewModel()
        val savedName = savedPreferences().studentName

        viewModel.updateStudentNameDraft("Borrador")
        viewModel.discardStudentNameDraft()
        viewModel.commitStudentName()

        assertEquals(savedName, viewModel.uiState.value?.studentNameDraft)
        assertEquals(savedName, savedPreferences().studentName)
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

    @Test
    fun `importing a backup replaces preferences and progress and discards the name draft`() {
        val file = Files.createTempFile("dam-prep", ".json").toFile().apply { deleteOnExit() }
        runBlocking {
            preferencesRepository.completeOnboarding("Emma", PronounPreference.Feminine, TargetOperatingSystem.MacOS)
            lessonProgressRepository.completeLesson("welcome")
            backupRepository.exportTo(file)
            preferencesRepository.setStudentName("Otra persona")
            lessonProgressRepository.resetProgress()
        }
        val viewModel = createViewModel()
        viewModel.updateStudentNameDraft("Borrador")

        viewModel.importBackup(file)

        val state = assertNotNull(viewModel.uiState.value)
        assertEquals(BackupOperationState.Imported, state.backupState)
        assertEquals("Emma", state.studentNameDraft)
        assertTrue(state.hasLessonProgress)
    }

    @Test
    fun `an invalid backup reports the error and keeps the current data`() {
        val file = Files.createTempFile("dam-prep", ".json").toFile().apply { deleteOnExit() }
        file.writeText("no es json")
        runBlocking { preferencesRepository.setStudentName("Emma") }
        val viewModel = createViewModel()

        viewModel.importBackup(file)

        val state = assertNotNull(viewModel.uiState.value)
        assertIs<BackupOperationState.Failed>(state.backupState)
        assertEquals("Emma", state.studentNameDraft)

        viewModel.dismissBackupResult()
        assertEquals(BackupOperationState.Idle, viewModel.uiState.value?.backupState)
    }

    private fun createViewModel() = SettingsViewModel(preferencesRepository, lessonProgressRepository, backupRepository).also { it.uiState.observed() }

    private fun savedPreferences() = runBlocking { preferencesRepository.preferences.first() }
}
