package dev.kuromiichi.damprep.ui.onboarding

import dev.kuromiichi.damprep.data.preferences.PronounPreference
import dev.kuromiichi.damprep.data.preferences.TargetOperatingSystem
import dev.kuromiichi.damprep.data.preferences.ThemePreference
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

class OnboardingViewModelTest {
    @Test
    fun `onboarding starts with supplied theme preference`() {
        val viewModel = OnboardingViewModel(initialThemePreference = ThemePreference.Dark)

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
                initialThemePreference = ThemePreference.System,
                steps =
                    listOf(
                        step(kind = OnboardingStepKind.Personalization),
                        step(),
                    ),
            )

        assertFalse(viewModel.uiState.value.canContinue)
        assertNull(viewModel.continueOnboarding())
        assertEquals(0, viewModel.uiState.value.currentStepIndex)

        viewModel.updateStudentName(" Emma ")
        assertTrue(viewModel.uiState.value.canContinue)

        assertNull(viewModel.continueOnboarding())
        assertEquals(1, viewModel.uiState.value.currentStepIndex)
    }

    @Test
    fun `detected operating system is preselected but requires confirmation`() {
        val viewModel =
            OnboardingViewModel(
                initialThemePreference = ThemePreference.System,
                initialTargetOperatingSystem = TargetOperatingSystem.MacOS,
                steps =
                    listOf(
                        step(kind = OnboardingStepKind.OperatingSystem),
                        step(),
                    ),
            )

        assertEquals(TargetOperatingSystem.MacOS, viewModel.uiState.value.targetOperatingSystem)
        assertFalse(viewModel.uiState.value.isTargetOperatingSystemConfirmed)
        assertFalse(viewModel.uiState.value.canContinue)
        assertNull(viewModel.continueOnboarding())
        assertEquals(0, viewModel.uiState.value.currentStepIndex)

        viewModel.confirmTargetOperatingSystem(TargetOperatingSystem.MacOS)

        assertTrue(viewModel.uiState.value.isTargetOperatingSystemConfirmed)
        assertTrue(viewModel.uiState.value.canContinue)
        assertNull(viewModel.continueOnboarding())
        assertEquals(1, viewModel.uiState.value.currentStepIndex)
    }

    @Test
    fun `user can confirm a different operating system from the detected one`() {
        val viewModel =
            OnboardingViewModel(
                initialThemePreference = ThemePreference.System,
                initialTargetOperatingSystem = TargetOperatingSystem.MacOS,
                steps = listOf(step(kind = OnboardingStepKind.OperatingSystem)),
            )

        viewModel.confirmTargetOperatingSystem(TargetOperatingSystem.Windows)
        val completion = viewModel.continueOnboarding()

        assertEquals(TargetOperatingSystem.Windows, completion?.targetOperatingSystem)
    }

    @Test
    fun `last onboarding step returns trimmed completion data`() {
        val viewModel =
            OnboardingViewModel(
                initialThemePreference = ThemePreference.System,
                initialTargetOperatingSystem = TargetOperatingSystem.Linux,
                steps = listOf(step()),
            )

        viewModel.updateStudentName(" Emma ")
        viewModel.updatePronounPreference(PronounPreference.Feminine)
        viewModel.confirmTargetOperatingSystem(TargetOperatingSystem.Linux)

        val completion = viewModel.continueOnboarding()

        assertEquals("Emma", completion?.studentName)
        assertEquals(PronounPreference.Feminine, completion?.pronounPreference)
        assertEquals(TargetOperatingSystem.Linux, completion?.targetOperatingSystem)
        assertEquals(0, viewModel.uiState.value.currentStepIndex)
    }

    @Test
    fun `onboarding navigation respects the first step`() {
        val viewModel =
            OnboardingViewModel(
                initialThemePreference = ThemePreference.System,
                steps = listOf(step(), step(), step()),
            )

        viewModel.goToPreviousStep()
        assertEquals(0, viewModel.uiState.value.currentStepIndex)

        viewModel.continueOnboarding()
        viewModel.continueOnboarding()
        assertEquals(2, viewModel.uiState.value.currentStepIndex)

        viewModel.goToPreviousStep()
        assertEquals(1, viewModel.uiState.value.currentStepIndex)
    }

    @Test
    fun `theme preference can be updated and synchronized`() {
        val viewModel = OnboardingViewModel(initialThemePreference = ThemePreference.System)

        viewModel.updateThemePreference(ThemePreference.Dark)
        assertEquals(ThemePreference.Dark, viewModel.uiState.value.themePreference)

        viewModel.syncThemePreference(ThemePreference.Light)
        assertEquals(ThemePreference.Light, viewModel.uiState.value.themePreference)
    }

    private fun step(kind: OnboardingStepKind = OnboardingStepKind.Info): OnboardingStep =
        OnboardingStep(
            kind = kind,
            title = "Paso",
            body = "Contenido",
        )
}
