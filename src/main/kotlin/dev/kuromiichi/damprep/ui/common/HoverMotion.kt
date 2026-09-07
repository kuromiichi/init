package dev.kuromiichi.damprep.ui.common

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer

@Composable
fun Modifier.hoverScale(
    enabled: Boolean = true,
    hoveredScale: Float = 1.015f,
): Modifier {
    if (!enabled) {
        return this
    }

    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()
    val scale by animateFloatAsState(
        targetValue = if (isHovered) hoveredScale else 1f,
        animationSpec =
            tween(
                durationMillis = 140,
                easing = FastOutSlowInEasing,
            ),
        label = "Hover scale",
    )

    return this
        .hoverable(interactionSource = interactionSource)
        .graphicsLayer {
            scaleX = scale
            scaleY = scale
        }
}
