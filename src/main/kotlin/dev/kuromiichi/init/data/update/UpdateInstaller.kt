package dev.kuromiichi.init.data.update

import java.io.File
import java.io.IOException

/** Qué ocurre después de lanzar el instalador. */
enum class InstallLaunch {
    /** Un proceso aparte instala la versión en cuanto la app se cierra y la vuelve a abrir. */
    RestartToFinish,

    /** Se ha abierto el instalador del sistema; la persona termina la instalación y reabre la app. */
    InstallerOpened,
}

interface UpdateInstaller {
    /** `false` cuando la app no se ejecuta desde una instalación (por ejemplo, con `./gradlew run`). */
    val isAvailable: Boolean

    /** Prepara la instalación de [installerFile] y la deja en marcha. Lanza [IOException] si no puede. */
    fun launch(installerFile: File): InstallLaunch
}

/**
 * Instala con las herramientas de cada sistema. [appPath] es el lanzador de la app instalada, que jpackage publica en la
 * propiedad `jpackage.app-path`.
 */
class SystemUpdateInstaller(
    private val platform: UpdatePlatform?,
    private val workDirectory: File,
    private val appPath: String? = System.getProperty("jpackage.app-path"),
    private val processId: Long = ProcessHandle.current().pid(),
) : UpdateInstaller {
    override val isAvailable: Boolean = platform != null && appPath != null

    override fun launch(installerFile: File): InstallLaunch {
        val launcher = File(checkNotNull(appPath) { "La app no se está ejecutando desde una instalación." })

        return when (checkNotNull(platform)) {
            UpdatePlatform.MacOsArm64, UpdatePlatform.MacOsX64 -> launchOnMacOs(installerFile, launcher)
            UpdatePlatform.WindowsX64 -> launchOnWindows(installerFile, launcher)
            UpdatePlatform.LinuxX64 -> {
                start("xdg-open", installerFile.absolutePath)
                InstallLaunch.InstallerOpened
            }
        }
    }

    private fun launchOnMacOs(
        dmg: File,
        launcher: File,
    ): InstallLaunch {
        // El lanzador está en init.app/Contents/MacOS/init.
        val appBundle = launcher.parentFile?.parentFile?.parentFile
        if (appBundle == null || appBundle.extension != "app" || !appBundle.parentFile.canWrite()) {
            // Sin permiso para sustituir la app, se abre la imagen para arrastrarla a mano.
            start("open", dmg.absolutePath)
            return InstallLaunch.InstallerOpened
        }

        val script = writeScript("install-update.sh", MacOsInstallScript)
        script.setExecutable(true)
        start("/bin/sh", script.absolutePath, processId.toString(), dmg.absolutePath, appBundle.absolutePath)
        return InstallLaunch.RestartToFinish
    }

    private fun launchOnWindows(
        msi: File,
        launcher: File,
    ): InstallLaunch {
        val script = writeScript("install-update.ps1", WindowsInstallScript)
        start(
            "powershell.exe",
            "-NoProfile",
            "-ExecutionPolicy",
            "Bypass",
            "-WindowStyle",
            "Hidden",
            "-File",
            script.absolutePath,
            "-ProcessId",
            processId.toString(),
            "-Msi",
            msi.absolutePath,
            "-App",
            launcher.absolutePath,
        )
        return InstallLaunch.RestartToFinish
    }

    private fun writeScript(
        name: String,
        content: String,
    ): File {
        workDirectory.mkdirs()
        return workDirectory.resolve(name).apply { writeText(content) }
    }

    /** Lanza un proceso independiente que sigue vivo cuando la app se cierra; su salida queda en un log. */
    private fun start(vararg command: String) {
        workDirectory.mkdirs()
        ProcessBuilder(*command)
            .redirectErrorStream(true)
            .redirectOutput(workDirectory.resolve("install-update.log"))
            .start()
    }

    private companion object {
        /** Espera a que la app se cierre, sustituye el paquete por el de la imagen y la vuelve a abrir. */
        val MacOsInstallScript =
            """
            #!/bin/sh
            pid="${'$'}1"; dmg="${'$'}2"; app="${'$'}3"
            while kill -0 "${'$'}pid" 2>/dev/null; do sleep 0.5; done
            mount_point="${'$'}(mktemp -d)"
            hdiutil attach -nobrowse -readonly -mountpoint "${'$'}mount_point" "${'$'}dmg" || { open "${'$'}app"; exit 1; }
            source_app="${'$'}(find "${'$'}mount_point" -maxdepth 1 -name '*.app' | head -n 1)"
            if [ -n "${'$'}source_app" ] && ditto "${'$'}source_app" "${'$'}app.new"; then
                rm -rf "${'$'}app" && mv "${'$'}app.new" "${'$'}app"
            else
                rm -rf "${'$'}app.new"
            fi
            hdiutil detach "${'$'}mount_point" -quiet && rmdir "${'$'}mount_point"
            xattr -dr com.apple.quarantine "${'$'}app" 2>/dev/null
            open "${'$'}app"
            """.trimIndent()

        /** Espera a que la app se cierre, instala el MSI con progreso y sin preguntas, y la vuelve a abrir. */
        val WindowsInstallScript =
            """
            param([int]${'$'}ProcessId, [string]${'$'}Msi, [string]${'$'}App)
            Wait-Process -Id ${'$'}ProcessId -ErrorAction SilentlyContinue
            Start-Process -FilePath 'msiexec.exe' -ArgumentList @('/i', "`"${'$'}Msi`"", '/passive', '/norestart') -Wait
            Start-Process -FilePath ${'$'}App
            """.trimIndent()
    }
}
