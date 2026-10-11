package dev.kuromiichi.init.ui.common

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.drawOutline
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex

/**
 * Contorno del color de acento dibujado hacia fuera de los límites del elemento, con un fundido al aparecer.
 * Sirve para marcar qué elemento recibe el teclado; se oculta mientras se usa el ratón. [outset] es lo que se separa del elemento y [shape] la forma del
 * propio elemento: el contorno la sigue, con cada esquina ensanchada en [outset] para que quede paralelo al borde.
 */
@Composable
fun Modifier.focusOutline(
    active: Boolean,
    outset: Dp,
    shape: Shape = MaterialTheme.shapes.large,
): Modifier {
    val color = MaterialTheme.colorScheme.primary
    val isVisible = active && LocalKeyboardFocusVisibility.current.isVisible
    val alpha by animateFloatAsState(targetValue = if (isVisible) 1f else 0f, label = "Focus outline")
    // Encima del propio contenido, para que ningún fondo lo tape.
    return drawWithContent {
        drawContent()
        if (alpha > 0f) {
            val outsetPx = outset.toPx()
            val outlineSize = Size(size.width + 2 * outsetPx, size.height + 2 * outsetPx)
            val outlineShape =
                if (shape is CornerBasedShape) {
                    shape.copy(
                        topStart = CornerSize(shape.topStart.toPx(size, this) + outsetPx),
                        topEnd = CornerSize(shape.topEnd.toPx(size, this) + outsetPx),
                        bottomEnd = CornerSize(shape.bottomEnd.toPx(size, this) + outsetPx),
                        bottomStart = CornerSize(shape.bottomStart.toPx(size, this) + outsetPx),
                    )
                } else {
                    shape
                }
            translate(left = -outsetPx, top = -outsetPx) {
                drawOutline(
                    outline = outlineShape.createOutline(outlineSize, layoutDirection, this),
                    color = color.copy(alpha = alpha),
                    style = Stroke(width = 2.dp.toPx()),
                )
            }
        }
    }
}

/** [focusOutline] que aparece cuando el elemento, o algo dentro de él, tiene el foco. */
@Composable
fun Modifier.focusHighlight(
    shape: Shape,
    outset: Dp = 4.dp,
): Modifier {
    var hasFocus by remember { mutableStateOf(false) }
    // El elemento enfocado se dibuja por encima de sus vecinos (los segmentos de un selector se solapan), para que el
    // fondo del segmento de al lado no tape su contorno.
    return onFocusChanged { hasFocus = it.hasFocus }
        .zIndex(if (hasFocus) 1f else 0f)
        .focusOutline(hasFocus, outset, shape)
}
