package dev.kuromiichi.init.data.update

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.runInterruptible
import kotlinx.serialization.SerializationException
import java.io.File
import java.io.IOException
import java.security.MessageDigest
import kotlin.time.Duration
import kotlin.time.Duration.Companion.days

sealed interface UpdateState {
    /** Ejecución de desarrollo o sistema sin instalador publicado: no se buscan actualizaciones. */
    data object Unsupported : UpdateState

    data object Idle : UpdateState

    data object Checking : UpdateState

    data object UpToDate : UpdateState

    data class CheckFailed(
        val message: String,
    ) : UpdateState

    data class Available(
        override val update: AvailableUpdate,
    ) : UpdateState,
        HasUpdate

    /** [progress] entre 0 y 1, o `null` si no se conoce el tamaño. */
    data class Downloading(
        override val update: AvailableUpdate,
        val progress: Float?,
    ) : UpdateState,
        HasUpdate

    /** El instalador espera a que la app se cierre para sustituirla y volver a abrirla. */
    data class RestartPending(
        override val update: AvailableUpdate,
    ) : UpdateState,
        HasUpdate

    /** Se ha abierto el instalador del sistema y la persona termina la instalación. */
    data class InstallerOpened(
        override val update: AvailableUpdate,
    ) : UpdateState,
        HasUpdate

    data class InstallFailed(
        override val update: AvailableUpdate,
        val message: String,
    ) : UpdateState,
        HasUpdate

    /** Estados en los que hay una versión nueva que mostrar. */
    sealed interface HasUpdate {
        val update: AvailableUpdate
    }
}

/**
 * Busca, descarga e instala versiones nuevas publicadas en GitHub. El estado es de toda la app, así que las operaciones
 * se lanzan en [externalScope] y siguen aunque se cierre la pantalla que las pidió.
 */
class UpdateRepository(
    val installedVersion: AppVersion,
    private val platform: UpdatePlatform?,
    private val releaseSource: ReleaseSource,
    private val installer: UpdateInstaller,
    private val downloadsDirectory: File,
    private val externalScope: CoroutineScope,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
) {
    private val installedRelease = installedVersion.release

    private val _state =
        MutableStateFlow(
            if (installedRelease != null && platform != null && installer.isAvailable) UpdateState.Idle else UpdateState.Unsupported,
        )
    val state: StateFlow<UpdateState> = _state.asStateFlow()

    private var operation: Job? = null

    /** Consulta la última release. No hace nada sin soporte o si ya hay una consulta o una instalación en marcha. */
    fun checkForUpdates() {
        val current = _state.value
        if (current == UpdateState.Unsupported || current is UpdateState.RestartPending || operation?.isActive == true) return
        val installed = checkNotNull(installedRelease)
        val platform = checkNotNull(platform)

        _state.value = UpdateState.Checking
        operation =
            externalScope.launch {
                _state.value =
                    try {
                        val release = runInterruptible(ioDispatcher) { releaseSource.latestRelease() }
                        release?.toAvailableUpdate(installed, platform)?.let(UpdateState::Available) ?: UpdateState.UpToDate
                    } catch (exception: IOException) {
                        UpdateState.CheckFailed("No se ha podido consultar si hay versiones nuevas. Comprueba la conexión.")
                    } catch (exception: SerializationException) {
                        UpdateState.CheckFailed("La respuesta de GitHub no tiene el formato esperado.")
                    }
            }
    }

    /** Descarga el instalador de la versión disponible, comprueba su integridad y lo lanza. */
    fun installUpdate() {
        val update =
            when (val current = _state.value) {
                is UpdateState.Available -> current.update
                is UpdateState.InstallFailed -> current.update
                else -> return
            }
        if (operation?.isActive == true) return

        _state.value = UpdateState.Downloading(update, progress = 0f.takeIf { update.installer.size > 0 })
        operation =
            externalScope.launch {
                _state.value =
                    try {
                        val installerFile = download(update)
                        when (runInterruptible(ioDispatcher) { installer.launch(installerFile) }) {
                            InstallLaunch.RestartToFinish -> UpdateState.RestartPending(update)
                            InstallLaunch.InstallerOpened -> UpdateState.InstallerOpened(update)
                        }
                    } catch (exception: IOException) {
                        UpdateState.InstallFailed(update, exception.message ?: "No se ha podido instalar la actualización.")
                    }
            }
    }

    /** Borra las descargas antiguas, que ya no hacen falta una vez instalada la versión. */
    fun cleanUpDownloads(maxAge: Duration = 1.days) {
        externalScope.launch(ioDispatcher) {
            val limit = System.currentTimeMillis() - maxAge.inWholeMilliseconds
            downloadsDirectory.listFiles()?.filter { it.lastModified() < limit }?.forEach(File::deleteRecursively)
        }
    }

    private suspend fun download(update: AvailableUpdate): File {
        val checksums =
            update.checksums ?: throw IOException("La versión ${update.version} no publica la suma de comprobación del instalador.")

        return runInterruptible(ioDispatcher) {
            downloadsDirectory.mkdirs()
            val expectedHash =
                parseSha256Sums(releaseSource.readText(checksums.downloadUrl), update.installer.name)
                    ?: throw IOException("No se encuentra la suma de comprobación de ${update.installer.name}.")

            val target = downloadsDirectory.resolve(update.installer.name)
            val partial = downloadsDirectory.resolve("${update.installer.name}.part")
            releaseSource.download(update.installer.downloadUrl, partial) { bytesRead ->
                val size = update.installer.size
                if (size > 0) {
                    // En centésimas: el estado solo cambia, y la interfaz solo se recompone, cuando avanza un 1 %.
                    val progress = (bytesRead * 100 / size).coerceIn(0, 100) / 100f
                    _state.update { state -> if (state is UpdateState.Downloading) state.copy(progress = progress) else state }
                }
            }

            if (partial.sha256() != expectedHash) {
                partial.delete()
                throw IOException("El archivo descargado está dañado. Vuelve a intentarlo.")
            }
            target.delete()
            if (!partial.renameTo(target)) throw IOException("No se ha podido guardar el instalador.")
            target
        }
    }

    private fun File.sha256(): String {
        val digest = MessageDigest.getInstance("SHA-256")
        inputStream().use { input ->
            val buffer = ByteArray(DEFAULT_BUFFER_SIZE * 8)
            while (true) {
                val count = input.read(buffer)
                if (count < 0) break
                digest.update(buffer, 0, count)
            }
        }
        return digest.digest().joinToString("") { "%02x".format(it) }
    }
}
