package dev.kuromiichi.damprep.data.preferences

import androidx.datastore.core.DataStore
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.emptyPreferences
import dev.kuromiichi.damprep.data.local.resolveAppDataDirectory
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
