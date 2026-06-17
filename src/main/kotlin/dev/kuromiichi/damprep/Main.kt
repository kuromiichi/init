package dev.kuromiichi.damprep

import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import java.awt.Dimension

fun main() {
    System.setProperty("apple.awt.application.name", "DAM Prep")
    System.setProperty("apple.awt.application.appearance", "system")

    application {
        Window(
            onCloseRequest = ::exitApplication,
            title = "DAM Prep",
        ) {
            DisposableEffect(window) {
                val previousMinimumSize = window.minimumSize
                window.minimumSize = Dimension(480, 360)

                onDispose {
                    window.minimumSize = previousMinimumSize
                }
            }
            App()
        }
    }
}
