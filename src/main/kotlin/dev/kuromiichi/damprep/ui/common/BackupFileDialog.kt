package dev.kuromiichi.damprep.ui.common

import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.awt.AwtWindow
import java.awt.FileDialog
import java.awt.Frame
import java.io.File

/** Ventana principal de la app, para que los diálogos nativos de archivo se asocien a ella. */
val LocalAppWindow = compositionLocalOf<Frame?> { null }

const val BACKUP_FILE_EXTENSION = ".json"

enum class BackupFileDialogMode {
    Save,
    Open,
}

/**
 * Diálogo nativo para elegir el archivo de la copia de seguridad. Se muestra mientras está en la
 * composición y llama a [onResult] con el archivo elegido, o `null` si se cancela.
 */
@Composable
fun BackupFileDialog(
    mode: BackupFileDialogMode,
    title: String,
    onResult: (File?) -> Unit,
    defaultFileName: String = "",
    parent: Frame? = LocalAppWindow.current,
) {
    val currentOnResult by rememberUpdatedState(onResult)

    AwtWindow(
        visible = true,
        create = {
            object : FileDialog(
                parent,
                title,
                if (mode == BackupFileDialogMode.Save) SAVE else LOAD,
            ) {
                init {
                    file = defaultFileName.ifEmpty { null }
                    setFilenameFilter { _, name -> name.endsWith(BACKUP_FILE_EXTENSION, ignoreCase = true) }
                }

                override fun setVisible(value: Boolean) {
                    super.setVisible(value)
                    if (value) {
                        // `setVisible(true)` bloquea hasta que el diálogo se cierra.
                        currentOnResult(selectedFile(mode))
                    }
                }
            }
        },
        dispose = FileDialog::dispose,
    )
}

private fun FileDialog.selectedFile(mode: BackupFileDialogMode): File? {
    val directory = directory ?: return null
    val name = file ?: return null
    val selected = File(directory, name)

    return if (mode == BackupFileDialogMode.Save && !name.endsWith(BACKUP_FILE_EXTENSION, ignoreCase = true)) {
        File(directory, name + BACKUP_FILE_EXTENSION)
    } else {
        selected
    }
}
