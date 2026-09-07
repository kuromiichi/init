package dev.kuromiichi.damprep

import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import dev.kuromiichi.damprep.data.preferences.UserPreferencesRepository
import dev.kuromiichi.damprep.data.progress.LessonProgressRepository
import dev.kuromiichi.damprep.di.appModule
import dev.kuromiichi.damprep.ui.window.WindowMetrics
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin

fun main() {
    System.setProperty("apple.awt.application.name", "DAM Prep")
    System.setProperty("apple.awt.application.appearance", "system")

    val koinApplication = startKoin { modules(appModule) }
    val preferencesRepository = koinApplication.koin.get<UserPreferencesRepository>()
    val lessonProgressRepository = koinApplication.koin.get<LessonProgressRepository>()

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
                App(
                    preferencesRepository = preferencesRepository,
                    lessonProgressRepository = lessonProgressRepository,
                )
            }
        }
    } finally {
        stopKoin()
    }
}
