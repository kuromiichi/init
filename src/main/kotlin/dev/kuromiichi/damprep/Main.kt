package dev.kuromiichi.damprep

import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import dev.kuromiichi.damprep.ui.window.WindowMetrics

fun main() {
    System.setProperty("apple.awt.application.name", "DAM Prep")
    System.setProperty("apple.awt.application.appearance", "system")

    application {
        val windowState = rememberWindowState(
            size = DpSize(
                width = WindowMetrics.PreferredWidth,
                height = WindowMetrics.PreferredHeight,
            ),
        )

        Window(
            onCloseRequest = ::exitApplication,
            state = windowState,
            title = "DAM Prep",
        ) {
            DisposableEffect(window) {
                val previousMinimumSize = window.minimumSize
                window.minimumSize = WindowMetrics.minimumAwtSize

                if (
                    window.width < WindowMetrics.minimumAwtSize.width ||
                    window.height < WindowMetrics.minimumAwtSize.height
                ) {
                    window.setSize(
                        window.width.coerceAtLeast(WindowMetrics.minimumAwtSize.width),
                        window.height.coerceAtLeast(WindowMetrics.minimumAwtSize.height),
                    )
                }

                onDispose {
                    window.minimumSize = previousMinimumSize
                }
            }
            App()
        }
    }
}
