package dev.kuromiichi.damprep.ui.lessons

import androidx.compose.ui.input.key.Key
import androidx.compose.ui.test.ComposeUiTest
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.getBoundsInRoot
import androidx.compose.ui.test.getUnclippedBoundsInRoot
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.v2.runComposeUiTest
import androidx.lifecycle.SavedStateHandle
import dev.kuromiichi.damprep.data.lessons.LessonContent
import dev.kuromiichi.damprep.data.lessons.LessonPractice
import dev.kuromiichi.damprep.data.lessons.LessonStep
import dev.kuromiichi.damprep.data.lessons.LessonVisualContent
import dev.kuromiichi.damprep.data.lessons.paragraphs
import dev.kuromiichi.damprep.data.lessons.richText
import dev.kuromiichi.damprep.testing.ViewModelTest
import dev.kuromiichi.damprep.testing.press
import dev.kuromiichi.damprep.testing.setWindowContent
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@OptIn(ExperimentalTestApi::class)
class CurrentLessonKeyboardTest : ViewModelTest() {
    @Test
    fun `left and right arrows move between steps within bounds`() =
        runComposeUiTest {
            showLesson(steps = listOf(step("Uno"), step("Dos")))

            onNodeWithText("Uno").assertExists()
            press(Key.DirectionLeft)
            onNodeWithText("Uno").assertExists()

            press(Key.DirectionRight)
            onNodeWithText("Dos").assertExists()

            press(Key.DirectionLeft)
            onNodeWithText("Uno").assertExists()
        }

    @Test
    fun `right arrow on the last step shows the completion and enter finishes the lesson`() =
        runComposeUiTest {
            var finished = false
            showLesson(steps = listOf(step("Uno")), onLessonFinished = { finished = true })

            press(Key.DirectionRight)
            onNodeWithText("Todo listo").assertExists()

            press(Key.Enter)
            waitUntil { finished }
        }

    @Test
    fun `a practice must be confirmed with enter before moving on`() =
        runComposeUiTest {
            showLesson(steps = listOf(step("Práctica", practice = practice()), step("Después")))

            press(Key.DirectionRight)
            onNodeWithText("Práctica").assertExists()

            press(Key.Enter)
            press(Key.DirectionRight)
            onNodeWithText("Después").assertExists()
        }

    @Test
    fun `p reveals the next hint until none are left`() =
        runComposeUiTest {
            showLesson(steps = listOf(step("Práctica", practice = practice())))

            onNodeWithText("Pista 1 de 2").assertDoesNotExist()
            press(Key.P)
            onNodeWithText("Pista 1 de 2").assertExists()
            onNodeWithText("Pista 2 de 2").assertDoesNotExist()

            press(Key.P)
            press(Key.P)
            onNodeWithText("Pista 1 de 2").assertExists()
            onNodeWithText("Pista 2 de 2").assertExists()
        }

    @Test
    fun `escape leaves the lesson`() =
        runComposeUiTest {
            var leftLesson = false
            showLesson(steps = listOf(step("Uno")), onBack = { leftLesson = true })

            press(Key.Escape)
            assertTrue(leftLesson)
        }

    @Test
    fun `the first scroll key only reveals the highlight and the next ones scroll a long step`() =
        runComposeUiTest {
            val paragraphs = List(60) { index -> "Párrafo ${index + 1}" }
            showLesson(steps = listOf(step("Largo", explanation = paragraphs)))

            fun firstParagraphTop() = onNodeWithText("Párrafo 1").getUnclippedBoundsInRoot().top

            val initialTop = firstParagraphTop()
            press(Key.DirectionDown)
            assertEquals(initialTop, firstParagraphTop(), "La primera tecla solo debería mostrar el resaltado")

            press(Key.DirectionDown)
            val afterArrow = firstParagraphTop()
            assertTrue(afterArrow < initialTop, "↓ debería desplazar el texto")

            press(Key.PageDown)
            val afterPage = firstParagraphTop()
            assertTrue(afterPage < afterArrow, "AvPág debería desplazar el texto")

            press(Key.Spacebar)
            assertTrue(firstParagraphTop() < afterPage, "Espacio debería bajar hasta el final")
        }

    @Test
    fun `a practice visual is as tall as its text at most and tab toggles scrolling it or the page`() =
        runComposeUiTest {
            val lines = (1..80).joinToString("\n") { index -> "Línea $index" }
            showLesson(
                steps =
                    listOf(
                        step(
                            "Práctica",
                            explanation = listOf("Párrafo 1", "Párrafo 2", "Párrafo 3"),
                            visual = LessonVisualContent.IconText(title = "Código", body = lines),
                            practice = practice(),
                        ),
                    ),
            )

            fun paragraphTop() = onNodeWithText("Párrafo 1").getUnclippedBoundsInRoot().top

            fun visualTop() = onNodeWithText(lines).getUnclippedBoundsInRoot().top

            val visibleVisualBottom = onNodeWithText(lines).getBoundsInRoot().bottom
            val textBottom = onNodeWithText("Párrafo 3").getUnclippedBoundsInRoot().bottom
            assertTrue(visibleVisualBottom <= textBottom, "El visual no debería pasar del alto del texto")

            val initialParagraphTop = paragraphTop()
            val initialVisualTop = visualTop()
            press(Key.Tab)
            press(Key.DirectionDown)
            assertTrue(visualTop() < initialVisualTop, "Con el visual resaltado, ↓ debería desplazarlo")
            assertEquals(initialParagraphTop, paragraphTop(), "Con el visual resaltado, la página no debería moverse")

            val visualTopAfterScroll = visualTop()
            press(Key.Tab)
            press(Key.DirectionDown)
            assertTrue(paragraphTop() < initialParagraphTop, "Sin resaltado, ↓ debería desplazar la página")
            assertEquals(
                visualTopAfterScroll - initialVisualTop,
                visualTop() - paragraphTop() - (initialVisualTop - initialParagraphTop),
                "Sin resaltado, el visual no debería desplazarse por dentro",
            )
        }

    private fun ComposeUiTest.showLesson(
        steps: List<LessonStep>,
        onBack: () -> Unit = {},
        onLessonFinished: () -> Unit = {},
    ) {
        val viewModel =
            CurrentLessonViewModel(
                savedStateHandle = SavedStateHandle(mapOf("lessonId" to "lesson")),
                lessonProgressRepository = lessonProgressRepository,
                preferencesRepository = preferencesRepository,
                findLessonById = { LessonContent(lessonId = "lesson", steps = steps) },
                findLessonSummary = { null },
            )
        setWindowContent {
            CurrentLessonScreen(onBack = onBack, onLessonFinished = onLessonFinished, viewModel = viewModel)
        }
    }

    private fun step(
        title: String,
        explanation: List<String> = listOf("Contenido"),
        visual: LessonVisualContent = LessonVisualContent.IconText(title = "Visual", body = "Detalle"),
        practice: LessonPractice? = null,
    ): LessonStep =
        LessonStep(
            title = title,
            explanation = paragraphs(*explanation.toTypedArray()),
            visual = visual,
            practice = practice,
        )

    private fun practice(): LessonPractice =
        LessonPractice(
            id = "practice",
            objective = richText("Objetivo"),
            statement = richText("Enunciado"),
            requirements = listOf(richText("Requisito")),
            hints = listOf(richText("Primera pista"), richText("Segunda pista")),
            confirmation = richText("Lo he comprobado"),
        )
}
