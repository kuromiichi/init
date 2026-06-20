package dev.kuromiichi.damprep.ui.home

import dev.kuromiichi.damprep.data.preferences.PronounPreference
import kotlin.test.Test
import kotlin.test.assertEquals

class HomeViewModelTest {
    @Test
    fun `home state adapts welcome message to pronoun preference`() {
        val viewModel = HomeViewModel(totalLessonsProvider = { 10 })

        val state = viewModel.createUiState(
            studentName = "Emma",
            pronounPreference = PronounPreference.Feminine,
            completedLessons = 3,
        )

        assertEquals("Bienvenida, Emma", state.welcomeMessage)
        assertEquals("3 de 10 temas completados", state.progressLabel)
        assertEquals(0.3f, state.progress)
    }

    @Test
    fun `home state clamps completed lessons into available range`() {
        val viewModel = HomeViewModel(totalLessonsProvider = { 4 })

        val state = viewModel.createUiState(
            studentName = "Ari",
            pronounPreference = PronounPreference.Neutral,
            completedLessons = 8,
        )

        assertEquals(4, state.completedLessons)
        assertEquals(4, state.totalLessons)
        assertEquals(1f, state.progress)
        assertEquals("4 de 4 temas completados", state.progressLabel)
    }

    @Test
    fun `home state handles an empty lesson catalog`() {
        val viewModel = HomeViewModel(totalLessonsProvider = { 0 })

        val state = viewModel.createUiState(
            studentName = "Ari",
            pronounPreference = PronounPreference.Neutral,
            completedLessons = 1,
        )

        assertEquals(0, state.completedLessons)
        assertEquals(0, state.totalLessons)
        assertEquals(0f, state.progress)
        assertEquals("0 de 0 temas completados", state.progressLabel)
    }
}
