package dev.kuromiichi.damprep

import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import dev.kuromiichi.damprep.di.appModules
import dev.kuromiichi.damprep.ui.window.WindowMetrics
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin

fun main() {
    System.setProperty("apple.awt.application.name", "DAM Prep")
    System.setProperty("apple.awt.application.appearance", "system")

    startKoin { modules(appModules) }

    try {
        application {
            val windowState =
                rememberWindowState(
                    size =
                        DpSize(
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
                    val minimumSize = WindowMetrics.minimumAwtSize(window.insets)
                    window.minimumSize = minimumSize

                    if (
                        window.width < minimumSize.width ||
                        window.height < minimumSize.height
                    ) {
                        window.setSize(
                            window.width.coerceAtLeast(minimumSize.width),
                            window.height.coerceAtLeast(minimumSize.height),
                        )
                    }

                    onDispose {
                        window.minimumSize = previousMinimumSize
                    }
                }
                App()
            }
        }
    } finally {
        stopKoin()
    }
}
