package dev.kuromiichi.damprep.data.local

import java.io.File

fun resolveAppDataDirectory(): File {
    val osName = System.getProperty("os.name").lowercase()
    val userHome = File(System.getProperty("user.home"))

    return when {
        osName.contains("mac") -> userHome.resolve("Library/Application Support/DAM Prep")
        osName.contains("win") -> {
            val appData = System.getenv("APPDATA")
            if (appData != null) {
                File(appData).resolve("DAM Prep")
            } else {
                userHome.resolve("AppData/Roaming/DAM Prep")
            }
        }
        else -> {
            val configHome = System.getenv("XDG_CONFIG_HOME")
            if (configHome != null) {
                File(configHome).resolve("dam-prep")
            } else {
                userHome.resolve(".config/dam-prep")
            }
        }
    }
}
