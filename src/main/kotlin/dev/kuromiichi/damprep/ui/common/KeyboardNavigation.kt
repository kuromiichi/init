package dev.kuromiichi.damprep.ui.common

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager

/**
 * Si se ve el resaltado de lo que maneja el teclado. Usar el ratón lo oculta; la siguiente tecla de navegación solo
 * lo vuelve a mostrar, sin moverse, para que se vea dónde está el foco antes de cambiarlo.
 */
@Stable
class KeyboardFocusVisibility {
    var isVisible by mutableStateOf(false)
        private set

    fun hide() {
        isVisible = false
    }

    /** Muestra el resaltado. Devuelve `true` si ya se veía, es decir, si la tecla puede hacer su acción. */
    fun reveal(): Boolean {
        val wasVisible = isVisible
        isVisible = true
        return wasVisible
    }
}

val LocalKeyboardFocusVisibility = staticCompositionLocalOf { KeyboardFocusVisibility() }

/** Oculta el resaltado de teclado al pulsar o desplazar con el ratón dentro de este elemento. */
fun Modifier.hideKeyboardFocusOnPointer(visibility: KeyboardFocusVisibility): Modifier =
    pointerInput(visibility) {
        awaitPointerEventScope {
            while (true) {
                val event = awaitPointerEvent(PointerEventPass.Initial)
                if (event.type == PointerEventType.Press || event.type == PointerEventType.Scroll) {
                    visibility.hide()
                }
            }
        }
    }

/**
 * Las flechas mueven el foco entre los controles de una pantalla e Intro pulsa el enfocado (eso ya lo hace cada control).
 * Con el resaltado oculto, la primera flecha o Tab solo lo muestran, salvo que el foco esté en la propia pantalla
 * ([isRootFocused]), donde no hay nada que mostrar y la flecha entra directamente al primer control.
 * Con [isEditingText], izquierda y derecha se dejan al campo de texto para mover el cursor.
 */
@Composable
fun Modifier.arrowKeyNavigation(
    horizontal: Boolean = true,
    isRootFocused: () -> Boolean = { false },
    isEditingText: () -> Boolean = { false },
): Modifier {
    val focusManager = LocalFocusManager.current
    val visibility = LocalKeyboardFocusVisibility.current
    return onPreviewKeyEvent { event ->
        if (event.type != KeyEventType.KeyDown) return@onPreviewKeyEvent false
        if (event.key == Key.Tab) {
            // Tab sigue su recorrido normal; con el resaltado oculto, la primera pulsación solo lo muestra.
            return@onPreviewKeyEvent !visibility.reveal() && !isRootFocused()
        }
        val direction =
            when (event.key) {
                Key.DirectionUp -> FocusDirection.Up
                Key.DirectionDown -> FocusDirection.Down
                Key.DirectionLeft -> FocusDirection.Left.takeIf { horizontal && !isEditingText() }
                Key.DirectionRight -> FocusDirection.Right.takeIf { horizontal && !isEditingText() }
                else -> null
            } ?: return@onPreviewKeyEvent false
        val wasVisible = visibility.reveal()
        if (!wasVisible && !isRootFocused()) return@onPreviewKeyEvent true
        // Con el foco en la pantalla misma no hay nada «debajo»: abajo y derecha entran al primer control (el primero en el
        // orden de Tab) y arriba e izquierda al último.
        if (isRootFocused()) {
            val entersFromStart = direction == FocusDirection.Down || direction == FocusDirection.Right
            focusManager.moveFocus(if (entersFromStart) FocusDirection.Next else FocusDirection.Previous)
        } else {
            focusManager.moveFocus(direction)
        }
    }
}
