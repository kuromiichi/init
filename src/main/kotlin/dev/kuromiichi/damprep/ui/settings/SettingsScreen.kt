package dev.kuromiichi.damprep.ui.settings

import androidx.compose.runtime.Composable
import dev.kuromiichi.damprep.ui.common.PlaceholderScreen

@Composable
fun SettingsScreen(onBack: () -> Unit) {
    PlaceholderScreen(
        title = "Configuración",
        body = "Aquí agruparemos preferencias como tema visual, tratamiento, datos locales y opciones de la aplicación.",
        onBack = onBack,
    )
}
