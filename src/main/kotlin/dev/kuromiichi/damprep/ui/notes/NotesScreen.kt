package dev.kuromiichi.damprep.ui.notes

import androidx.compose.runtime.Composable
import dev.kuromiichi.damprep.ui.common.PlaceholderScreen

@Composable
fun NotesScreen(onBack: () -> Unit) {
    PlaceholderScreen(
        title = "Mis apuntes",
        body = "Aquí podrás tomar apuntes propios dentro de cada tema. En una iteración posterior decidiremos cómo organizarlos y persistirlos.",
        onBack = onBack,
    )
}
