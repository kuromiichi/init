package dev.kuromiichi.init.ui.home

import androidx.compose.ui.input.key.Key
import androidx.compose.ui.test.ComposeUiTest
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsFocused
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.v2.runComposeUiTest
import dev.kuromiichi.init.testing.press
import dev.kuromiichi.init.testing.setWindowContent
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalTestApi::class)
class HomeKeyboardTest {
    private val actions = mutableListOf<String>()

    @Test
    fun `focus starts on continue lesson and enter opens it`() =
        runComposeUiTest {
            showHome()

            onNodeWithText("Continuar lección").assertIsFocused()
            press(Key.Enter)
            assertEquals(listOf("continue"), actions)
        }

    @Test
    fun `the first arrow only reveals the focus and the next ones move it`() =
        runComposeUiTest {
            showHome()

            press(Key.DirectionDown)
            onNodeWithText("Continuar lección").assertIsFocused()

            press(Key.DirectionDown)
            onNodeWithText("Ver lecciones").assertIsFocused()

            press(Key.DirectionRight)
            onNodeWithText("Mis apuntes").assertIsFocused()

            press(Key.DirectionDown)
            onNodeWithText("Configuración").assertIsFocused()

            press(Key.Enter)
            assertEquals(listOf("settings"), actions)
        }

    @Test
    fun `tab follows the normal order once the focus is visible`() =
        runComposeUiTest {
            showHome()

            press(Key.Tab)
            onNodeWithText("Continuar lección").assertIsFocused()

            press(Key.Tab)
            onNodeWithText("Ver lecciones").assertIsFocused()

            press(Key.Enter)
            assertEquals(listOf("lessons"), actions)
        }

    private fun ComposeUiTest.showHome() {
        setWindowContent {
            HomeScreen(
                uiState =
                    HomeUiState(
                        studentName = "QA",
                        welcomeMessage = "Bienvenide, QA",
                        completedLessons = 1,
                        totalLessons = 3,
                        progress = 1 / 3f,
                        progressLabel = "1 de 3 lecciones completadas",
                        continueLessonId = "segunda",
                        continueLessonTitle = "Segunda lección",
                        continueLessonSubject = "Programación",
                        isContinueLessonStarted = true,
                    ),
                onContinueLesson = { actions += "continue" },
                onShowLessons = { actions += "lessons" },
                onShowNotes = { actions += "notes" },
                onShowSettings = { actions += "settings" },
            )
        }
    }
}
