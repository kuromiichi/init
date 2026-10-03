package dev.kuromiichi.damprep.ui.window

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import java.awt.Dimension
import java.awt.Insets
import kotlin.math.roundToInt

object WindowMetrics {
    val MinimumUsableWidth = 800.dp
    val MinimumUsableHeight = 680.dp
    val PreferredWidth = 1280.dp
    val PreferredHeight = 860.dp

    /** Fracción máxima de la pantalla que ocupa la ventana al abrirse. */
    private const val MAX_SCREEN_FRACTION = 0.92f

    /** Tamaño inicial: el preferido, salvo que no quepa en la pantalla (nunca menos que el mínimo útil). */
    fun preferredSize(
        screenWidth: Dp,
        screenHeight: Dp,
    ): DpSize =
        DpSize(
            width = PreferredWidth.coerceAtMost(screenWidth * MAX_SCREEN_FRACTION).coerceAtLeast(MinimumUsableWidth),
            height = PreferredHeight.coerceAtMost(screenHeight * MAX_SCREEN_FRACTION).coerceAtLeast(MinimumUsableHeight),
        )

    fun minimumAwtSize(insets: Insets): Dimension =
        Dimension(
            MinimumUsableWidth.value.roundToInt() + insets.left + insets.right,
            MinimumUsableHeight.value.roundToInt() + insets.top + insets.bottom,
        )
}
