package dev.kuromiichi.damprep.ui.common

import androidx.compose.foundation.ScrollState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import kotlin.math.min

/**
 * Draws over a scroll viewport. Keep this outside verticalScroll in the modifier chain.
 * Each edge keeps a fixed gradient and fades out as its scroll limit approaches.
 */
fun Modifier.verticalFadingEdges(
    scrollState: ScrollState,
    color: Color,
    length: Dp,
): Modifier = drawWithCache {
    val fadeLength = length.toPx().coerceIn(0f, size.height / 2f)
    val revealDistance = fadeLength / 2f
    val topBrush = Brush.verticalGradient(
        colors = listOf(color, color.copy(alpha = 0f)),
        endY = fadeLength.coerceAtLeast(1f),
    )
    val bottomBrush = Brush.verticalGradient(
        colors = listOf(color.copy(alpha = 0f), color),
        startY = size.height - fadeLength,
        endY = size.height,
    )

    onDrawWithContent {
        drawContent()

        if (fadeLength > 0f && scrollState.maxValue != Int.MAX_VALUE) {
            val topAlpha = fadingEdgeAlpha(
                distanceFromEdge = scrollState.value.toFloat(),
                revealDistance = revealDistance,
            )
            val bottomAlpha = fadingEdgeAlpha(
                distanceFromEdge = (scrollState.maxValue - scrollState.value).toFloat(),
                revealDistance = revealDistance,
            )

            if (topAlpha > 0f) {
                drawRect(
                    brush = topBrush,
                    alpha = topAlpha,
                    size = Size(size.width, fadeLength),
                )
            }

            if (bottomAlpha > 0f) {
                drawRect(
                    brush = bottomBrush,
                    alpha = bottomAlpha,
                    topLeft = Offset(0f, size.height - fadeLength),
                    size = Size(size.width, fadeLength),
                )
            }
        }
    }
}

internal fun fadingEdgeAlpha(
    distanceFromEdge: Float,
    revealDistance: Float,
): Float {
    if (revealDistance <= 0f) return 0f
    return min(distanceFromEdge / revealDistance, 1f).coerceAtLeast(0f)
}
