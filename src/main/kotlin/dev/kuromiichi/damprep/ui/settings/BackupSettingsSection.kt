package dev.kuromiichi.damprep.ui.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FileOpen
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.kuromiichi.damprep.ui.common.BackupFileDialog
import dev.kuromiichi.damprep.ui.common.BackupFileDialogMode
import dev.kuromiichi.damprep.ui.common.BackupOperationState
import dev.kuromiichi.damprep.ui.common.SettingControlMaxWidth
import dev.kuromiichi.damprep.ui.common.SettingRow
import java.io.File
import java.time.LocalDate

@Composable
fun BackupSettingsSection(
    backupState: BackupOperationState,
    onExport: (File) -> Unit,
    onImport: (File) -> Unit,
    onDismissResult: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var showExportDialog by rememberSaveable { mutableStateOf(false) }
    var showImportDialog by rememberSaveable { mutableStateOf(false) }

    /** Ruta del archivo elegido que espera confirmación, porque importar sustituye los datos actuales. */
    var pendingImportPath by rememberSaveable { mutableStateOf<String?>(null) }
    val isBusy = backupState == BackupOperationState.InProgress

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(28.dp),
    ) {
        SettingRow(
            title = "Exportar datos",
            description = "Guarda tu nombre, tus ajustes y tu progreso en un archivo para hacer una copia de seguridad o llevarlos a otro equipo.",
        ) {
            OutlinedButton(
                onClick = { showExportDialog = true },
                enabled = !isBusy,
                modifier = Modifier.widthIn(max = SettingControlMaxWidth),
            ) {
                Icon(imageVector = Icons.Filled.Save, contentDescription = null)
                Text(text = "Exportar datos", modifier = Modifier.padding(start = 8.dp))
            }
        }

        SettingRow(
            title = "Importar datos",
            description = "Recupera una copia exportada desde DAM Prep. Sustituye tu nombre, tus ajustes y tu progreso actuales.",
        ) {
            OutlinedButton(
                onClick = { showImportDialog = true },
                enabled = !isBusy,
                modifier = Modifier.widthIn(max = SettingControlMaxWidth),
            ) {
                Icon(imageVector = Icons.Filled.FileOpen, contentDescription = null)
                Text(text = "Importar datos", modifier = Modifier.padding(start = 8.dp))
            }
        }
    }

    if (showExportDialog) {
        BackupFileDialog(
            mode = BackupFileDialogMode.Save,
            title = "Exportar datos de DAM Prep",
            defaultFileName = "dam-prep-copia-${LocalDate.now()}.json",
            onResult = { file ->
                showExportDialog = false
                if (file != null) onExport(file)
            },
        )
    }

    if (showImportDialog) {
        BackupFileDialog(
            mode = BackupFileDialogMode.Open,
            title = "Importar datos de DAM Prep",
            onResult = { file ->
                showImportDialog = false
                pendingImportPath = file?.path
            },
        )
    }

    pendingImportPath?.let { path ->
        val file = File(path)
        AlertDialog(
            onDismissRequest = { pendingImportPath = null },
            title = { Text("¿Importar esta copia de seguridad?") },
            text = {
                Text(
                    "Tu nombre, tus ajustes y tu progreso actuales se sustituirán por los de «${file.name}».",
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        pendingImportPath = null
                        onImport(file)
                    },
                ) {
                    Text(text = "Importar", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { pendingImportPath = null }) {
                    Text("Cancelar")
                }
            },
        )
    }

    val resultMessage =
        when (backupState) {
            BackupOperationState.Exported -> "Exportación" to "Tus datos se han guardado correctamente."
            BackupOperationState.Imported -> "Importación" to "Tus datos se han importado correctamente."
            is BackupOperationState.Failed -> "No se ha podido completar la operación" to backupState.message
            else -> null
        }
    if (resultMessage != null) {
        AlertDialog(
            onDismissRequest = onDismissResult,
            title = { Text(resultMessage.first) },
            text = { Text(resultMessage.second) },
            confirmButton = {
                TextButton(onClick = onDismissResult) {
                    Text("Aceptar")
                }
            },
        )
    }
}
