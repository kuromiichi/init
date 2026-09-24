package dev.kuromiichi.damprep.ui.home

import dev.kuromiichi.damprep.data.lessons.LessonSummary
import dev.kuromiichi.damprep.data.lessons.SubjectArea
import dev.kuromiichi.damprep.data.preferences.PronounPreference
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class HomeUiStateTest {
    @Test
    fun `home state adapts welcome message to pronoun preference`() {
        val lessons = lessons(10)
        val state =
            createHomeUiState(
                studentName = "Emma",
                pronounPreference = PronounPreference.Feminine,
                completedLessonIds = lessons.take(3).mapTo(hashSetOf(), LessonSummary::id),
                lessonCatalog = lessons,
                hasContent = { true },
            )

        assertEquals("Bienvenida, Emma", state.welcomeMessage)
        assertEquals("3 de 10 lecciones completadas", state.progressLabel)
        assertEquals(0.3f, state.progress)
        assertEquals("lesson-3", state.continueLessonId)
    }

    @Test
    fun `home state clamps completed lessons into available range`() {
        val lessons = lessons(4)
        val state =
            createHomeUiState(
                studentName = "Ari",
                pronounPreference = PronounPreference.Neutral,
                completedLessonIds = lessons.mapTo(hashSetOf(), LessonSummary::id) + "unknown",
                lessonCatalog = lessons,
            )

        assertEquals(4, state.completedLessons)
        assertEquals(4, state.totalLessons)
        assertEquals(1f, state.progress)
        assertEquals("4 de 4 lecciones completadas", state.progressLabel)
        assertEquals(null, state.continueLessonId)
    }

    @Test
    fun `home state handles an empty lesson catalog`() {
        val state =
            createHomeUiState(
                studentName = "Ari",
                pronounPreference = PronounPreference.Neutral,
                completedLessonIds = setOf("unknown"),
                lessonCatalog = emptyList(),
            )

        assertEquals(0, state.completedLessons)
        assertEquals(0, state.totalLessons)
        assertEquals(0f, state.progress)
        assertEquals("0 de 0 lecciones completadas", state.progressLabel)
        assertEquals(null, state.continueLessonId)
    }

    @Test
    fun `home does not continue into a lesson without content`() {
        val lessons = lessons(2)
        val state =
            createHomeUiState(
                studentName = "Ari",
                pronounPreference = PronounPreference.Neutral,
                completedLessonIds = setOf("lesson-0"),
                lessonCatalog = lessons,
                hasContent = { lessonId -> lessonId == "lesson-0" },
            )

        assertEquals(null, state.continueLessonId)
    }

    @Test
    fun `debug override can continue into later available content`() {
        val lessons = lessons(3)
        val state =
            createHomeUiState(
                studentName = "Ari",
                pronounPreference = PronounPreference.Neutral,
                completedLessonIds = emptySet(),
                unlockAllLessons = true,
                lessonCatalog = lessons,
                hasContent = { lessonId -> lessonId == "lesson-2" },
            )

        assertEquals("lesson-2", state.continueLessonId)
        assertEquals(0, state.completedLessons)
    }

    @Test
    fun `home distinguishes a started lesson and exposes its subject`() {
        val lessons = lessons(2)
        val state =
            createHomeUiState(
                studentName = "Ari",
                pronounPreference = PronounPreference.Neutral,
                completedLessonIds = emptySet(),
                lessonStepIndices = mapOf("lesson-0" to 2),
                lessonCatalog = lessons,
                hasContent = { true },
            )

        assertTrue(state.isContinueLessonStarted)
        assertEquals("Lección 0", state.continueLessonTitle)
        assertEquals("Programación", state.continueLessonSubject)
    }

    private fun lessons(count: Int): List<LessonSummary> =
        List(count) { index ->
            LessonSummary(
                id = "lesson-$index",
                title = "Lección $index",
                subjectArea = SubjectArea.Programming,
            )
        }
}
