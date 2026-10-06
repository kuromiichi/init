package dev.kuromiichi.damprep.ui.common

/** Estado de una exportación o importación de datos, compartido por Configuración y el onboarding. */
sealed interface BackupOperationState {
    data object Idle : BackupOperationState

    data object InProgress : BackupOperationState

    data object Exported : BackupOperationState

    data object Imported : BackupOperationState

    data class Failed(
        val message: String,
    ) : BackupOperationState
}
