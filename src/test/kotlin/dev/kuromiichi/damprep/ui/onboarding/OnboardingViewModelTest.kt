package dev.kuromiichi.damprep.ui.onboarding

import dev.kuromiichi.damprep.data.preferences.PronounPreference
import dev.kuromiichi.damprep.ui.theme.ThemePreference
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
        val viewModel = OnboardingViewModel(
            initialThemePreference = ThemePreference.System,
            steps = listOf(
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
    fun `last onboarding step returns trimmed completion data`() {
        val viewModel = OnboardingViewModel(
            initialThemePreference = ThemePreference.System,
            steps = listOf(step()),
        )

        viewModel.updateStudentName(" Emma ")
        viewModel.updatePronounPreference(PronounPreference.Feminine)

        val completion = viewModel.continueOnboarding()

        assertEquals("Emma", completion?.studentName)
        assertEquals(PronounPreference.Feminine, completion?.pronounPreference)
        assertEquals(0, viewModel.uiState.value.currentStepIndex)
    }

    @Test
    fun `onboarding navigation respects the first step`() {
        val viewModel = OnboardingViewModel(
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
