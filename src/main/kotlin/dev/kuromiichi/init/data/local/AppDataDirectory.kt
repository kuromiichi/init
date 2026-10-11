package dev.kuromiichi.init.data.local

import dev.kuromiichi.init.data.progress.DATABASE_FILE_NAME
import java.io.File
import java.io.IOException
import java.nio.file.Files

private const val APP_DATA_DIRECTORY_NAME = "dev.kuromiichi.init"

/** Nombres que usaban las versiones publicadas como DAM Prep. */
private const val LEGACY_APP_DATA_DIRECTORY_NAME = "DAM Prep"
private const val LEGACY_LINUX_APP_DATA_DIRECTORY_NAME = "dam-prep"
private const val LEGACY_DATABASE_FILE_NAME = "dam-prep.db"

/** Archivos auxiliares de SQLite y el bloqueo de Room antes que el principal, para que este se mueva el último. */
private val DatabaseFileSuffixes = listOf("-wal", "-shm", ".lck", "")

fun resolveAppDataDirectory(): File = resolveAppDataDirectory(APP_DATA_DIRECTORY_NAME, APP_DATA_DIRECTORY_NAME)

/**
 * Traslada los datos guardados por DAM Prep a la carpeta actual. Debe ejecutarse antes de abrir la base de datos y las
 * preferencias. Si algo falla, los datos antiguos se quedan donde estaban y la app arranca igualmente.
 */
fun migrateLegacyAppData() {
    migrateLegacyAppData(
        legacyDirectory = resolveAppDataDirectory(LEGACY_APP_DATA_DIRECTORY_NAME, LEGACY_LINUX_APP_DATA_DIRECTORY_NAME),
        targetDirectory = resolveAppDataDirectory(),
    )
}

internal fun migrateLegacyAppData(
    legacyDirectory: File,
    targetDirectory: File,
) {
    try {
        if (legacyDirectory.isDirectory && !targetDirectory.exists()) {
            targetDirectory.parentFile?.mkdirs()
            Files.move(legacyDirectory.toPath(), targetDirectory.toPath())
        }
        if (!targetDirectory.resolve(DATABASE_FILE_NAME).exists()) {
            DatabaseFileSuffixes.forEach { suffix ->
                val legacyFile = targetDirectory.resolve(LEGACY_DATABASE_FILE_NAME + suffix)
                val targetFile = targetDirectory.resolve(DATABASE_FILE_NAME + suffix)
                if (legacyFile.exists() && !targetFile.exists()) {
                    Files.move(legacyFile.toPath(), targetFile.toPath())
                }
            }
        }
    } catch (exception: IOException) {
        System.err.println("No se pudieron trasladar los datos de DAM Prep: ${exception.message}")
    }
}

private fun resolveAppDataDirectory(
    name: String,
    linuxName: String,
): File {
    val osName = System.getProperty("os.name").lowercase()
    val userHome = File(System.getProperty("user.home"))

    return when {
        osName.contains("mac") -> userHome.resolve("Library/Application Support/$name")
        osName.contains("win") -> {
            val appData = System.getenv("APPDATA")
            if (appData != null) {
                File(appData).resolve(name)
            } else {
                userHome.resolve("AppData/Roaming/$name")
            }
        }
        else -> {
            val configHome = System.getenv("XDG_CONFIG_HOME")
            if (configHome != null) {
                File(configHome).resolve(linuxName)
            } else {
                userHome.resolve(".config/$linuxName")
            }
        }
    }
}
