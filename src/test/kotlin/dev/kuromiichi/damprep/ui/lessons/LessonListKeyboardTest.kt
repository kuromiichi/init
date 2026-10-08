package dev.kuromiichi.damprep.ui.lessons

import androidx.compose.ui.input.key.Key
import androidx.compose.ui.test.ComposeUiTest
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsFocused
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.v2.runComposeUiTest
import dev.kuromiichi.damprep.data.lessons.SubjectArea
import dev.kuromiichi.damprep.testing.press
import dev.kuromiichi.damprep.testing.setWindowContent
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalTestApi::class)
class LessonListKeyboardTest {
    private val selectedLessons = mutableListOf<String>()
    private var leftList = false

    @Test
    fun `focus starts on the current lesson and enter opens it`() =
        runComposeUiTest {
            showList()

            onNodeWithText("En curso").assertIsFocused()
            press(Key.Enter)
            assertEquals(listOf("current"), selectedLessons)
        }

    @Test
    fun `vertical arrows move between lessons and horizontal ones do nothing`() =
        runComposeUiTest {
            showList()

            press(Key.DirectionDown)
            onNodeWithText("En curso").assertIsFocused()

            press(Key.DirectionDown)
            onNodeWithText("Desbloqueada").assertIsFocused()

            press(Key.DirectionRight)
            onNodeWithText("Desbloqueada").assertIsFocused()

            press(Key.DirectionUp)
            onNodeWithText("En curso").assertIsFocused()

            press(Key.DirectionUp)
            onNodeWithText("Completada").assertIsFocused()

            press(Key.Enter)
            assertEquals(listOf("completed"), selectedLessons)
        }

    @Test
    fun `escape goes back`() =
        runComposeUiTest {
            showList()

            press(Key.Escape)
            assertEquals(true, leftList)
        }

    private fun ComposeUiTest.showList() {
        setWindowContent {
            LessonListScreen(
                uiState =
                    LessonListUiState(
                        lessons =
                            listOf(
                                lesson(0, "completed", "Completada", isCompleted = true),
                                lesson(1, "current", "En curso"),
                                lesson(2, "unlocked", "Desbloqueada"),
                            ),
                    ),
                onBack = { leftList = true },
                onLessonSelected = { selectedLessons += it },
            )
        }
    }

    private fun lesson(
        index: Int,
        id: String,
        title: String,
        isCompleted: Boolean = false,
    ) = LessonListItemUiModel(
        index = index,
        id = id,
        title = title,
        subjectArea = SubjectArea.Programming,
        isUnlocked = true,
        isAvailable = true,
        isCompleted = isCompleted,
    )
}
