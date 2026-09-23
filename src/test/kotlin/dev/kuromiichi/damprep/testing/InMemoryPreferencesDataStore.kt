package dev.kuromiichi.damprep.testing

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.emptyPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

/** DataStore síncrono para probar ViewModels sin hilos de E/S. */
class InMemoryPreferencesDataStore : DataStore<Preferences> {
    private val state = MutableStateFlow(emptyPreferences())

    override val data: StateFlow<Preferences> = state

    override suspend fun updateData(transform: suspend (Preferences) -> Preferences): Preferences {
        val updated = transform(state.value)
        state.value = updated
        return updated
    }
}
