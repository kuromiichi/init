package dev.kuromiichi.init.ui.common

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

private data class KeyboardShortcut(
    val keys: List<String>,
    val action: String,
)

private data class KeyboardShortcutGroup(
    val title: String,
    val shortcuts: List<KeyboardShortcut>,
)

private val shortcutGroups =
    listOf(
        KeyboardShortcutGroup(
            title = "En las lecciones",
            shortcuts =
                listOf(
                    KeyboardShortcut(listOf("←", "→"), "Paso anterior y siguiente"),
                    KeyboardShortcut(listOf("↑", "↓"), "Desplazar el texto o el código"),
                    KeyboardShortcut(listOf("Tab"), "Elegir qué se desplaza"),
                    KeyboardShortcut(listOf("Espacio"), "Ir al final"),
                    KeyboardShortcut(listOf("P"), "Pedir una pista"),
                    KeyboardShortcut(listOf("Intro"), "Confirmar la práctica y terminar"),
                    KeyboardShortcut(listOf("Esc"), "Salir de la lección"),
                ),
        ),
        KeyboardShortcutGroup(
            title = "En los menús",
            shortcuts =
                listOf(
                    KeyboardShortcut(listOf("↑", "↓", "←", "→"), "Elegir un botón"),
                    KeyboardShortcut(listOf("Intro"), "Pulsar el botón elegido"),
                    KeyboardShortcut(listOf("Esc"), "Volver a la pantalla anterior"),
                ),
        ),
    )

private val TwoColumnsMinWidth = 600.dp
private val KeysColumnWidth = 144.dp

/**
 * Los atajos de teclado de la app, agrupados por dónde se usan, con cada tecla dibujada como una tecla.
 * Sigue la guía visual de las lecciones: panel oscuro y teclas más claras con borde fino.
 */
@Composable
fun KeyboardShortcutsVisual(modifier: Modifier = Modifier) {
    LearningVisualSurface(modifier = modifier.fillMaxWidth(), contentPadding = 24.dp) {
        BoxWithConstraints(modifier = Modifier.fillMaxWidth()) {
            if (maxWidth >= TwoColumnsMinWidth) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(32.dp),
                ) {
                    shortcutGroups.forEach { group ->
                        ShortcutGroup(group = group, modifier = Modifier.weight(1f))
                    }
                }
            } else {
                Column(verticalArrangement = Arrangement.spacedBy(24.dp)) {
                    shortcutGroups.forEach { group ->
                        ShortcutGroup(group = group, modifier = Modifier.fillMaxWidth())
                    }
                }
            }
        }
    }
}

@Composable
private fun ShortcutGroup(
    group: KeyboardShortcutGroup,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(
            text = group.title,
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.primary,
        )
        group.shortcuts.forEach { shortcut ->
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Row(
                    modifier = Modifier.width(KeysColumnWidth),
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                ) {
                    shortcut.keys.forEach { key -> KeyCap(key) }
                }
                Text(
                    text = shortcut.action,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}

@Composable
private fun KeyCap(label: String) {
    Surface(
        shape = RoundedCornerShape(8.dp),
        color = MaterialTheme.colorScheme.surfaceContainerHigh,
        contentColor = MaterialTheme.colorScheme.onSurface,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
        )
    }
}
