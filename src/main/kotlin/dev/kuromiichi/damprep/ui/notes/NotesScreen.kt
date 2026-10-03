package dev.kuromiichi.damprep.ui.notes

import androidx.compose.runtime.Composable
import dev.kuromiichi.damprep.ui.common.PlaceholderScreen

@Composable
fun NotesScreen(onBack: () -> Unit) {
    PlaceholderScreen(
        title = "Mis apuntes",
        body = "Aquí podrás escribir tus propios apuntes de cada tema. Esta sección estará disponible próximamente.",
        onBack = onBack,
    )
}
