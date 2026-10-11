package dev.kuromiichi.init.ui.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.SystemUpdateAlt
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import dev.kuromiichi.init.data.update.AppVersion
import dev.kuromiichi.init.data.update.UpdateState
import dev.kuromiichi.init.ui.common.SettingControlMaxWidth
import dev.kuromiichi.init.ui.common.SettingRow
import dev.kuromiichi.init.ui.common.focusHighlight
import dev.kuromiichi.init.ui.update.UpdateDialog

@Composable
fun UpdateSettingsSection(
    installedVersion: AppVersion,
    updateState: UpdateState,
    checkForUpdates: Boolean,
    onCheckForUpdatesChanged: (Boolean) -> Unit,
    onCheckNow: () -> Unit,
    onInstall: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var showUpdateDialog by rememberSaveable { mutableStateOf(false) }
    val isSupported = updateState != UpdateState.Unsupported
    val availableUpdate = updateState as? UpdateState.HasUpdate

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(28.dp),
    ) {
        SettingRow(
            title = "Versión",
            description =
                buildString {
                    append(installedVersion.label)
                    if (installedVersion.isBeta) append(" · Beta: puede tener errores y cambiar con frecuencia.")
                },
        ) {
            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                if (availableUpdate != null) {
                    OutlinedButton(
                        onClick = { showUpdateDialog = true },
                        modifier = Modifier.widthIn(max = SettingControlMaxWidth).focusHighlight(ButtonDefaults.outlinedShape),
                    ) {
                        Icon(imageVector = Icons.Filled.SystemUpdateAlt, contentDescription = null)
                        Text(text = "Actualizar a la ${availableUpdate.update.version}", modifier = Modifier.padding(start = 8.dp))
                    }
                } else {
                    OutlinedButton(
                        onClick = onCheckNow,
                        enabled = isSupported && updateState != UpdateState.Checking,
                        modifier = Modifier.widthIn(max = SettingControlMaxWidth).focusHighlight(ButtonDefaults.outlinedShape),
                    ) {
                        Icon(imageVector = Icons.Filled.Refresh, contentDescription = null)
                        Text(text = "Buscar actualizaciones", modifier = Modifier.padding(start = 8.dp))
                    }
                }
                updateState.statusMessage()?.let { (message, isError) ->
                    Text(
                        text = message,
                        style = MaterialTheme.typography.bodyMedium,
                        color = if (isError) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.End,
                    )
                }
            }
        }

        SettingRow(
            title = "Buscar actualizaciones al iniciar",
            description = "Al abrir la app comprueba si hay una versión nueva y la indica en la portada.",
        ) {
            Switch(
                checked = checkForUpdates,
                onCheckedChange = onCheckForUpdatesChanged,
                enabled = isSupported,
                modifier = Modifier.focusHighlight(RoundedCornerShape(percent = 50)),
            )
        }
    }

    if (showUpdateDialog && availableUpdate != null) {
        UpdateDialog(
            state = availableUpdate,
            installedVersion = installedVersion,
            onInstall = onInstall,
            onDismiss = { showUpdateDialog = false },
        )
    }
}

/** Texto bajo el botón y si es un error. */
private fun UpdateState.statusMessage(): Pair<String, Boolean>? =
    when (this) {
        UpdateState.Unsupported -> "Las actualizaciones solo están disponibles en la app instalada." to false
        UpdateState.Checking -> "Buscando…" to false
        UpdateState.UpToDate -> "Tienes la versión más reciente." to false
        is UpdateState.CheckFailed -> message to true
        is UpdateState.Downloading -> "Descargando la actualización…" to false
        is UpdateState.InstallFailed -> "No se ha podido instalar la actualización." to true
        else -> null
    }
