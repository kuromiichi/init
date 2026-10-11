package dev.kuromiichi.init.ui.onboarding

import dev.kuromiichi.init.data.preferences.PronounPreference
import dev.kuromiichi.init.data.preferences.TargetOperatingSystem
import dev.kuromiichi.init.data.preferences.ThemePreference
import dev.kuromiichi.init.data.preferences.UserPreferences
import dev.kuromiichi.init.testing.ViewModelTest
import dev.kuromiichi.init.ui.common.BackupOperationState
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import java.nio.file.Files
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertIs
import kotlin.test.assertTrue

class OnboardingViewModelTest : ViewModelTest() {
    @Test
    fun `onboarding starts with the saved theme preference`() {
        runBlocking { preferencesRepository.setThemePreference(ThemePreference.Dark) }
        val viewModel = OnboardingViewModel(preferencesRepository, backupRepository).also { it.uiState.observed() }

        val state = viewModel.uiState.value

        assertEquals(0, state.currentStepIndex)
        assertEquals(ThemePreference.Dark, state.themePreference)
        assertTrue(state.isFirstStep)
        assertTrue(state.canContinue)
    }

    @Test
    fun `personalization step requires a non blank student name`() {
        val viewModel =
            OnboardingViewModel(
                preferencesRepository = preferencesRepository,
                backupRepository = backupRepository,
                steps =
                    listOf(
                        step(kind = OnboardingStepKind.Personalization),
                        step(),
                    ),
            ).also { it.uiState.observed() }

        assertFalse(viewModel.uiState.value.canContinue)
        viewModel.continueOnboarding()
        assertEquals(0, viewModel.uiState.value.currentStepIndex)

        viewModel.updateStudentName(" Emma ")
        assertTrue(viewModel.uiState.value.canContinue)

        viewModel.continueOnboarding()
        assertEquals(1, viewModel.uiState.value.currentStepIndex)
    }

    @Test
    fun `detected operating system is preselected but requires confirmation`() {
        val viewModel =
            OnboardingViewModel(
                preferencesRepository = preferencesRepository,
                backupRepository = backupRepository,
                initialTargetOperatingSystem = TargetOperatingSystem.MacOS,
                steps =
                    listOf(
                        step(kind = OnboardingStepKind.OperatingSystem),
                        step(),
                    ),
            ).also { it.uiState.observed() }

        assertEquals(TargetOperatingSystem.MacOS, viewModel.uiState.value.targetOperatingSystem)
        assertFalse(viewModel.uiState.value.isTargetOperatingSystemConfirmed)
        assertFalse(viewModel.uiState.value.canContinue)
        viewModel.continueOnboarding()
        assertEquals(0, viewModel.uiState.value.currentStepIndex)

        viewModel.confirmTargetOperatingSystem(TargetOperatingSystem.MacOS)

        assertTrue(viewModel.uiState.value.isTargetOperatingSystemConfirmed)
        assertTrue(viewModel.uiState.value.canContinue)
        viewModel.continueOnboarding()
        assertEquals(1, viewModel.uiState.value.currentStepIndex)
    }

    @Test
    fun `user can confirm a different operating system from the detected one`() {
        val viewModel =
            OnboardingViewModel(
                preferencesRepository = preferencesRepository,
                backupRepository = backupRepository,
                initialTargetOperatingSystem = TargetOperatingSystem.MacOS,
                steps = listOf(step(kind = OnboardingStepKind.OperatingSystem)),
            ).also { it.uiState.observed() }

        viewModel.confirmTargetOperatingSystem(TargetOperatingSystem.Windows)
        viewModel.continueOnboarding()

        assertEquals(TargetOperatingSystem.Windows, savedPreferences().targetOperatingSystem)
    }

    @Test
    fun `last onboarding step saves trimmed preferences and finishes`() {
        val viewModel =
            OnboardingViewModel(
                preferencesRepository = preferencesRepository,
                backupRepository = backupRepository,
                initialTargetOperatingSystem = TargetOperatingSystem.Linux,
                steps = listOf(step()),
            ).also { it.uiState.observed() }

        viewModel.updateStudentName(" Emma ")
        viewModel.updatePronounPreference(PronounPreference.Feminine)
        viewModel.confirmTargetOperatingSystem(TargetOperatingSystem.Linux)

        viewModel.continueOnboarding()

        val preferences = savedPreferences()
        assertEquals("Emma", preferences.studentName)
        assertEquals(PronounPreference.Feminine, preferences.pronounPreference)
        assertEquals(TargetOperatingSystem.Linux, preferences.targetOperatingSystem)
        assertTrue(preferences.onboardingCompleted)
        assertTrue(viewModel.uiState.value.isFinished)
        assertEquals(0, viewModel.uiState.value.currentStepIndex)
    }

    @Test
    fun `onboarding navigation respects the first step`() {
        val viewModel =
            OnboardingViewModel(
                preferencesRepository = preferencesRepository,
                backupRepository = backupRepository,
                steps = listOf(step(), step(), step()),
            ).also { it.uiState.observed() }

        viewModel.goToPreviousStep()
        assertEquals(0, viewModel.uiState.value.currentStepIndex)

        viewModel.continueOnboarding()
        viewModel.continueOnboarding()
        assertEquals(2, viewModel.uiState.value.currentStepIndex)

        viewModel.goToPreviousStep()
        assertEquals(1, viewModel.uiState.value.currentStepIndex)
    }

    @Test
    fun `theme preference is saved immediately and follows external changes`() {
        val viewModel = OnboardingViewModel(preferencesRepository, backupRepository).also { it.uiState.observed() }

        viewModel.updateThemePreference(ThemePreference.Dark)
        assertEquals(ThemePreference.Dark, viewModel.uiState.value.themePreference)
        assertEquals(ThemePreference.Dark, savedPreferences().themePreference)

        runBlocking { preferencesRepository.setThemePreference(ThemePreference.Light) }
        assertEquals(ThemePreference.Light, viewModel.uiState.value.themePreference)
    }

    @Test
    fun `importing a backup finishes onboarding with the imported data`() {
        val file = backupFile()
        runBlocking {
            preferencesRepository.completeOnboarding("Emma", PronounPreference.Feminine, TargetOperatingSystem.Linux)
            preferencesRepository.setThemePreference(ThemePreference.Dark)
            lessonProgressRepository.completeLesson("welcome")
            backupRepository.exportTo(file)
            preferencesRepository.restore(UserPreferences())
            lessonProgressRepository.resetProgress()
        }
        val viewModel = OnboardingViewModel(preferencesRepository, backupRepository).also { it.uiState.observed() }

        viewModel.importBackup(file)

        assertTrue(viewModel.uiState.value.isFinished)
        val saved = savedPreferences()
        assertEquals("Emma", saved.studentName)
        assertEquals(ThemePreference.Dark, saved.themePreference)
        assertTrue(saved.onboardingCompleted)
        assertEquals(setOf("welcome"), runBlocking { lessonProgressRepository.completedLessonIds.first() })
    }

    @Test
    fun `a failed import keeps onboarding open and shows the error`() {
        val file = backupFile().apply { writeText("{}") }
        val viewModel = OnboardingViewModel(preferencesRepository, backupRepository).also { it.uiState.observed() }

        viewModel.importBackup(file)

        val state = viewModel.uiState.value
        assertFalse(state.isFinished)
        assertIs<BackupOperationState.Failed>(state.backupState)
        assertFalse(savedPreferences().onboardingCompleted)
    }

    private fun backupFile() = Files.createTempFile("init", ".json").toFile().apply { deleteOnExit() }

    private fun savedPreferences() = runBlocking { preferencesRepository.preferences.first() }

    private fun step(kind: OnboardingStepKind = OnboardingStepKind.Info): OnboardingStep =
        OnboardingStep(
            kind = kind,
            title = "Paso",
            body = "Contenido",
        )
}
