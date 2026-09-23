package dev.kuromiichi.damprep.ui.window

import androidx.compose.ui.unit.dp
import dev.kuromiichi.damprep.ui.lessons.LessonCompactBreakpoint
import java.awt.Insets
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class WindowMetricsTest {
    @Test
    fun `minimum outer window size includes native decorations`() {
        val minimumSize = WindowMetrics.minimumAwtSize(Insets(28, 2, 2, 2))

        assertEquals(804, minimumSize.width)
        assertEquals(710, minimumSize.height)
    }

    @Test
    fun `minimum window width reaches compact lesson layout`() {
        // AppScreen uses 40 dp of horizontal padding on each side.
        assertTrue(WindowMetrics.MinimumUsableWidth - 80.dp < LessonCompactBreakpoint)
    }
}
