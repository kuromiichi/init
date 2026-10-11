package dev.kuromiichi.init.ui.settings

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import dev.kuromiichi.init.ui.common.SettingControlMaxWidth
import dev.kuromiichi.init.ui.common.SettingRow
import dev.kuromiichi.init.ui.common.focusHighlight
import dev.kuromiichi.init.ui.common.focusOutline

@Composable
fun AdvancedSettingsSection(
    unlockAllLessons: Boolean,
    hasLessonProgress: Boolean,
    onUnlockAllLessonsChanged: (Boolean) -> Unit,
    onResetLessonProgress: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var isExpanded by rememberSaveable { mutableStateOf(false) }
    var showResetConfirmation by rememberSaveable { mutableStateOf(false) }
    val indicatorRotation by animateFloatAsState(
        targetValue = if (isExpanded) 180f else 0f,
        label = "Rotación del indicador de configuración avanzada",
    )

    var isHeaderFocused by remember { mutableStateOf(false) }

    OutlinedCard(
        modifier =
            modifier
                .fillMaxWidth()
                // El contorno de foco de la cabecera rodea la tarjeta entera, que es lo que se ve como botón.
                .focusOutline(active = isHeaderFocused, outset = 3.dp, shape = CardDefaults.outlinedShape),
    ) {
        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .onFocusChanged { isHeaderFocused = it.hasFocus }
                    .clickable(
                        role = Role.Button,
                        onClick = { isExpanded = !isExpanded },
                    ).padding(horizontal = 20.dp, vertical = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                Text(
                    text = "Configuración avanzada",
                    style = MaterialTheme.typography.titleMedium,
                )
                Text(
                    text =
                        if (unlockAllLessons) {
                            "El desbloqueo de lecciones está activado."
                        } else {
                            "Herramientas para probar y depurar el progreso."
                        },
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
            Icon(
                imageVector = Icons.Filled.ExpandMore,
                contentDescription = if (isExpanded) "Contraer" else "Expandir",
                modifier = Modifier.rotate(indicatorRotation),
            )
        }

        AnimatedVisibility(visible = isExpanded) {
            Column {
                HorizontalDivider()
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(24.dp),
                ) {
                    SettingRow(
                        title = "Desbloquear todas las lecciones",
                        description = "Ignora temporalmente el orden para probar cualquier lección disponible.",
                    ) {
                        Switch(
                            checked = unlockAllLessons,
                            onCheckedChange = onUnlockAllLessonsChanged,
                            modifier = Modifier.focusHighlight(RoundedCornerShape(percent = 50)),
                        )
                    }

                    SettingRow(
                        title = "Restablecer progreso",
                        description = "Elimina las lecciones completadas, el avance parcial y vuelve al inicio del recorrido.",
                    ) {
                        OutlinedButton(
                            onClick = { showResetConfirmation = true },
                            enabled = hasLessonProgress,
                            modifier = Modifier.widthIn(max = SettingControlMaxWidth).focusHighlight(ButtonDefaults.outlinedShape),
                        ) {
                            Icon(
                                imageVector = Icons.Filled.DeleteOutline,
                                contentDescription = null,
                            )
                            Text(
                                text = "Restablecer progreso",
                                modifier = Modifier.padding(start = 8.dp),
                            )
                        }
                    }
                }
            }
        }
    }

    if (showResetConfirmation) {
        AlertDialog(
            onDismissRequest = { showResetConfirmation = false },
            title = { Text("¿Restablecer el progreso?") },
            text = {
                Text(
                    "Se borrarán las lecciones completadas y el avance parcial. El resto de tu configuración se conservará.",
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showResetConfirmation = false
                        onResetLessonProgress()
                    },
                ) {
                    Text(
                        text = "Restablecer",
                        color = MaterialTheme.colorScheme.error,
                    )
                }
            },
            dismissButton = {
                TextButton(onClick = { showResetConfirmation = false }) {
                    Text("Cancelar")
                }
            },
        )
    }
}
