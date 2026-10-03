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
    fun `preferred size is used on large screens and shrinks to fit small ones`() {
        val large = WindowMetrics.preferredSize(2560.dp, 1440.dp)
        val laptop = WindowMetrics.preferredSize(1366.dp, 768.dp)
        val tiny = WindowMetrics.preferredSize(700.dp, 500.dp)

        assertEquals(WindowMetrics.PreferredWidth, large.width)
        assertEquals(WindowMetrics.PreferredHeight, large.height)
        assertTrue(laptop.width < 1366.dp)
        assertTrue(laptop.height < 768.dp)
        assertEquals(WindowMetrics.MinimumUsableWidth, tiny.width)
        assertEquals(WindowMetrics.MinimumUsableHeight, tiny.height)
    }

    @Test
    fun `minimum window width reaches compact lesson layout`() {
        // AppScreen uses 40 dp of horizontal padding on each side.
        assertTrue(WindowMetrics.MinimumUsableWidth - 80.dp < LessonCompactBreakpoint)
    }
}
