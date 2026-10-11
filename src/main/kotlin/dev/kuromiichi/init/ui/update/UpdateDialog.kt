package dev.kuromiichi.init.ui.update

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.unit.dp
import dev.kuromiichi.init.data.update.AppVersion
import dev.kuromiichi.init.data.update.UpdatePlatform
import dev.kuromiichi.init.data.update.UpdateState

/** Novedades de la versión disponible y su instalación, con la página de la release como alternativa manual. */
@Composable
fun UpdateDialog(
    state: UpdateState.HasUpdate,
    installedVersion: AppVersion,
    onInstall: () -> Unit,
    onDismiss: () -> Unit,
) {
    val uriHandler = LocalUriHandler.current
    val update = state.update
    val restartsApp = UpdatePlatform.current() != UpdatePlatform.LinuxX64

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Nueva versión: ${update.version}") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                Text(
                    text = "Tienes instalada la ${installedVersion.label}. Tu progreso y tus ajustes se conservan al actualizar.",
                    style = MaterialTheme.typography.bodyMedium,
                )
                if (update.releaseNotes.isNotBlank()) {
                    Text(
                        text = update.releaseNotes,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.heightIn(max = 220.dp).verticalScroll(rememberScrollState()),
                    )
                }
                when (state) {
                    is UpdateState.Downloading -> {
                        val progress = state.progress
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text(
                                text = if (progress == null) "Descargando…" else "Descargando… ${(progress * 100).toInt()} %",
                                style = MaterialTheme.typography.labelLarge,
                                color = MaterialTheme.colorScheme.primary,
                            )
                            if (progress == null) {
                                LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
                            } else {
                                LinearProgressIndicator(progress = { progress }, drawStopIndicator = {}, modifier = Modifier.fillMaxWidth())
                            }
                        }
                    }
                    is UpdateState.InstallerOpened ->
                        Text(
                            text = "Se ha abierto el instalador del sistema. Cuando termine, cierra init y vuelve a abrirla.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.primary,
                        )
                    is UpdateState.InstallFailed ->
                        Text(
                            text = "${state.message} También puedes descargarla desde la página de la versión.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.error,
                        )
                    else -> Unit
                }
            }
        },
        confirmButton = {
            when (state) {
                is UpdateState.Available ->
                    TextButton(onClick = onInstall) {
                        Text(if (restartsApp) "Actualizar y reiniciar" else "Descargar e instalar")
                    }
                is UpdateState.InstallFailed ->
                    TextButton(onClick = onInstall) {
                        Text("Reintentar")
                    }
                is UpdateState.Downloading, is UpdateState.RestartPending ->
                    TextButton(onClick = {}, enabled = false) {
                        Text(if (restartsApp) "Actualizar y reiniciar" else "Descargar e instalar")
                    }
                is UpdateState.InstallerOpened ->
                    TextButton(onClick = onDismiss) {
                        Text("Aceptar")
                    }
            }
        },
        dismissButton = {
            if (state is UpdateState.InstallFailed) {
                TextButton(onClick = { uriHandler.openUri(update.releasePageUrl) }) {
                    Text("Abrir página de descarga")
                }
            } else if (state !is UpdateState.InstallerOpened) {
                TextButton(onClick = onDismiss) {
                    Text("Ahora no")
                }
            }
        },
    )
}
