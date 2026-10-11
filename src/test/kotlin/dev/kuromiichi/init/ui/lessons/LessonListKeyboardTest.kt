package dev.kuromiichi.init.ui.lessons

import androidx.compose.ui.input.key.Key
import androidx.compose.ui.test.ComposeUiTest
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsFocused
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.v2.runComposeUiTest
import dev.kuromiichi.init.data.lessons.SubjectArea
import dev.kuromiichi.init.testing.press
import dev.kuromiichi.init.testing.setWindowContent
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
    fun `locked lessons take the focus and enter explains why they cannot be opened`() =
        runComposeUiTest {
            showList(lockedAfterCurrent = true)

            press(Key.DirectionDown)
            press(Key.DirectionDown)
            onNodeWithText("Bloqueada").assertIsFocused()

            press(Key.Enter)
            onNodeWithText("Completa «En curso» para desbloquear esta lección.").assertExists()
            assertEquals(emptyList(), selectedLessons)
        }

    @Test
    fun `escape goes back`() =
        runComposeUiTest {
            showList()

            press(Key.Escape)
            assertEquals(true, leftList)
        }

    private fun ComposeUiTest.showList(lockedAfterCurrent: Boolean = false) {
        setWindowContent {
            LessonListScreen(
                uiState =
                    LessonListUiState(
                        lessons =
                            listOf(
                                lesson(0, "completed", "Completada", isCompleted = true),
                                lesson(1, "current", "En curso"),
                                if (lockedAfterCurrent) {
                                    lesson(2, "locked", "Bloqueada", isUnlocked = false, previousLessonTitle = "En curso")
                                } else {
                                    lesson(2, "unlocked", "Desbloqueada")
                                },
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
        isUnlocked: Boolean = true,
        isCompleted: Boolean = false,
        previousLessonTitle: String? = null,
    ) = LessonListItemUiModel(
        index = index,
        id = id,
        title = title,
        subjectArea = SubjectArea.Programming,
        isUnlocked = isUnlocked,
        isAvailable = true,
        isCompleted = isCompleted,
        previousLessonTitle = previousLessonTitle,
    )
}
