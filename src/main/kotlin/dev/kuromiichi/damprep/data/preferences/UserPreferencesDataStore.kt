package dev.kuromiichi.damprep.data.preferences

import androidx.datastore.core.DataStore
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.emptyPreferences
import java.io.File

private const val DATA_STORE_FILE_NAME = "user.preferences_pb"

fun createUserPreferencesDataStore(): DataStore<Preferences> {
    val file = File(resolveAppDataDirectory(), DATA_STORE_FILE_NAME)

    return PreferenceDataStoreFactory.create(
        corruptionHandler =
            ReplaceFileCorruptionHandler {
                emptyPreferences()
            },
        produceFile = {
            file.parentFile?.mkdirs()
            file
        },
    )
}

private fun resolveAppDataDirectory(): File {
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
