package dev.kuromiichi.damprep.testing

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.test.ComposeUiTest
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.isFocused
import androidx.compose.ui.test.performKeyInput
import androidx.compose.ui.test.pressKey
import dev.kuromiichi.damprep.ui.common.KeyboardFocusVisibility
import dev.kuromiichi.damprep.ui.common.LocalKeyboardFocusVisibility
import dev.kuromiichi.damprep.ui.common.hideKeyboardFocusOnPointer

/** Muestra [content] como lo hace la ventana de la app: con su propio estado del resaltado de teclado. */
@OptIn(ExperimentalTestApi::class)
fun ComposeUiTest.setWindowContent(content: @Composable () -> Unit) {
    setContent {
        val keyboardFocusVisibility = remember { KeyboardFocusVisibility() }
        CompositionLocalProvider(LocalKeyboardFocusVisibility provides keyboardFocusVisibility) {
            Box(modifier = Modifier.fillMaxSize().hideKeyboardFocusOnPointer(keyboardFocusVisibility)) {
                content()
            }
        }
    }
    waitForIdle()
}

/**
 * Pulsa [key] sobre el elemento enfocado, que es quien la recibe en la ventana real, y espera a que la interfaz se
 * estabilice. Funciona aunque haya ventanas emergentes abiertas, como un tooltip.
 */
@OptIn(ExperimentalTestApi::class)
fun ComposeUiTest.press(key: Key) {
    onNode(isFocused()).performKeyInput { pressKey(key) }
    waitForIdle()
}
