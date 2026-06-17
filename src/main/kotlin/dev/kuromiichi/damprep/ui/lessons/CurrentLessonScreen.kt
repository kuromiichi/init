package dev.kuromiichi.damprep.ui.lessons

import androidx.compose.runtime.Composable
import dev.kuromiichi.damprep.data.lessons.LessonCatalog
import dev.kuromiichi.damprep.ui.common.PlaceholderScreen

@Composable
fun CurrentLessonScreen(onBack: () -> Unit) {
    val lesson = LessonCatalog.lessons.first()

    PlaceholderScreen(
        title = lesson.title,
        body = "Aquí empezará la lección actual. Más adelante mostraremos contenido teórico, ejemplos y el acceso al ejercicio de desbloqueo.",
        onBack = onBack,
    )
}
