package dev.kuromiichi.damprep.ui.common

import kotlin.test.Test
import kotlin.test.assertEquals

class FadingEdgesTest {

    @Test
    fun `edge is transparent at the scroll limit`() {
        assertEquals(0f, fadingEdgeAlpha(distanceFromEdge = 0f, revealDistance = 24f))
    }

    @Test
    fun `edge fades linearly near the scroll limit`() {
        assertEquals(0.5f, fadingEdgeAlpha(distanceFromEdge = 12f, revealDistance = 24f))
    }

    @Test
    fun `edge is fully visible beyond the reveal distance`() {
        assertEquals(1f, fadingEdgeAlpha(distanceFromEdge = 48f, revealDistance = 24f))
    }

    @Test
    fun `invalid distances cannot produce invalid opacity`() {
        assertEquals(0f, fadingEdgeAlpha(distanceFromEdge = -1f, revealDistance = 24f))
        assertEquals(0f, fadingEdgeAlpha(distanceFromEdge = 10f, revealDistance = 0f))
    }
}
