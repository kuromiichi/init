package dev.kuromiichi.damprep

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.window.WindowDraggableArea
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import dev.kuromiichi.damprep.di.appModules
import dev.kuromiichi.damprep.ui.common.LocalAppWindow
import dev.kuromiichi.damprep.ui.window.WindowMetrics
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import java.awt.Toolkit

private val IsMacOs = System.getProperty("os.name").startsWith("Mac")

/** Altura de la barra de título nativa de macOS, que la app cubre con su propio fondo. */
private val MacTitleBarHeight = 28.dp

fun main() {
    System.setProperty("apple.awt.application.name", "DAM Prep")
    System.setProperty("apple.awt.application.appearance", "system")

    startKoin { modules(appModules) }

    try {
        application {
            val windowState =
                rememberWindowState(
                    size =
                        Toolkit.getDefaultToolkit().screenSize.let { screen ->
                            WindowMetrics.preferredSize(screen.width.dp, screen.height.dp)
                        },
                )

            Window(
                onCloseRequest = ::exitApplication,
                state = windowState,
                title = "DAM Prep",
            ) {
                if (IsMacOs) {
                    DisposableEffect(window) {
                        // La barra de título se funde con el fondo del tema y los semáforos quedan sobre la app.
                        window.rootPane.putClientProperty("apple.awt.fullWindowContent", true)
                        window.rootPane.putClientProperty("apple.awt.transparentTitleBar", true)
                        window.rootPane.putClientProperty("apple.awt.windowTitleVisible", false)
                        onDispose {}
                    }
                }
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
                CompositionLocalProvider(LocalAppWindow provides window) {
                    Box(modifier = Modifier.fillMaxSize()) {
                        App(titleBarInset = if (IsMacOs) MacTitleBarHeight else 0.dp)
                        if (IsMacOs) {
                            WindowDraggableArea(modifier = Modifier.fillMaxWidth().height(MacTitleBarHeight)) {
                                Spacer(modifier = Modifier.fillMaxSize())
                            }
                        }
                    }
                }
            }
        }
    } finally {
        stopKoin()
    }
}
