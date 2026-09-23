package dev.kuromiichi.damprep.ui.window

import java.awt.Insets
import kotlin.test.Test
import kotlin.test.assertEquals

class WindowMetricsTest {
    @Test
    fun `minimum outer window size includes native decorations`() {
        val minimumSize = WindowMetrics.minimumAwtSize(Insets(28, 2, 2, 2))

        assertEquals(1044, minimumSize.width)
        assertEquals(710, minimumSize.height)
    }
}
