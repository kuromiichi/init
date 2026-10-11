package dev.kuromiichi.init.ui.update

import androidx.lifecycle.ViewModel
import dev.kuromiichi.init.data.update.AppVersion
import dev.kuromiichi.init.data.update.UpdateRepository
import dev.kuromiichi.init.data.update.UpdateState
import kotlinx.coroutines.flow.StateFlow

/** Estado del actualizador para la portada y Configuración; vive en el repositorio y es común a ambas. */
class UpdateViewModel(
    private val updateRepository: UpdateRepository,
) : ViewModel() {
    val installedVersion: AppVersion = updateRepository.installedVersion

    val state: StateFlow<UpdateState> = updateRepository.state

    fun checkForUpdates() = updateRepository.checkForUpdates()

    fun installUpdate() = updateRepository.installUpdate()
}
