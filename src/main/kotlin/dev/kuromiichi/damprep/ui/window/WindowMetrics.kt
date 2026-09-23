package dev.kuromiichi.damprep.ui.window

import androidx.compose.ui.unit.dp
import java.awt.Dimension
import java.awt.Insets
import kotlin.math.roundToInt

object WindowMetrics {
    val MinimumUsableWidth = 1040.dp
    val MinimumUsableHeight = 680.dp
    val PreferredWidth = 1120.dp
    val PreferredHeight = 760.dp

    fun minimumAwtSize(insets: Insets): Dimension =
        Dimension(
            MinimumUsableWidth.value.roundToInt() + insets.left + insets.right,
            MinimumUsableHeight.value.roundToInt() + insets.top + insets.bottom,
        )
}
