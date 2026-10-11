package dev.kuromiichi.init.ui.lessons

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class LessonStepScrollingTest {
    private val both = listOf(LessonScrollSection.Text, LessonScrollSection.Visual)

    @Test
    fun `tab starts at the first scrollable section and alternates between the two`() {
        assertEquals(LessonScrollSection.Text, cycleLessonScrollSection(both, current = null, backwards = false))
        assertEquals(LessonScrollSection.Visual, cycleLessonScrollSection(both, LessonScrollSection.Text, backwards = false))
        assertEquals(LessonScrollSection.Text, cycleLessonScrollSection(both, LessonScrollSection.Visual, backwards = false))
    }

    @Test
    fun `shift tab starts at the last section and alternates in the other direction`() {
        assertEquals(LessonScrollSection.Visual, cycleLessonScrollSection(both, current = null, backwards = true))
        assertEquals(LessonScrollSection.Text, cycleLessonScrollSection(both, LessonScrollSection.Visual, backwards = true))
        assertEquals(LessonScrollSection.Visual, cycleLessonScrollSection(both, LessonScrollSection.Text, backwards = true))
    }

    @Test
    fun `a single scrollable section is highlighted and stays highlighted`() {
        val onlyVisual = listOf(LessonScrollSection.Visual)

        assertEquals(LessonScrollSection.Visual, cycleLessonScrollSection(onlyVisual, current = null, backwards = false))
        assertEquals(LessonScrollSection.Visual, cycleLessonScrollSection(onlyVisual, LessonScrollSection.Visual, backwards = false))
        assertEquals(LessonScrollSection.Visual, cycleLessonScrollSection(onlyVisual, LessonScrollSection.Visual, backwards = true))
    }

    @Test
    fun `a section that stopped being scrollable is replaced by the one that still is`() {
        assertEquals(
            LessonScrollSection.Visual,
            cycleLessonScrollSection(listOf(LessonScrollSection.Visual), LessonScrollSection.Text, backwards = false),
        )
    }

    @Test
    fun `without scrollable sections there is nothing to focus`() {
        assertNull(cycleLessonScrollSection(emptyList(), current = null, backwards = false))
        assertNull(cycleLessonScrollSection(emptyList(), current = LessonScrollSection.Text, backwards = true))
    }
}
