package dev.kuromiichi.damprep.data.preferences

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import dev.kuromiichi.damprep.ui.theme.ThemePreference
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserPreferencesRepository(
    private val dataStore: DataStore<Preferences>,
) {
    val preferences: Flow<UserPreferences> = dataStore.data.map { preferences ->
        UserPreferences(
            studentName = preferences[StudentNameKey].orEmpty(),
            themePreference = preferences[ThemePreferenceKey].toThemePreference(),
            onboardingCompleted = preferences[OnboardingCompletedKey] ?: false,
        )
    }

    suspend fun setThemePreference(themePreference: ThemePreference) {
        dataStore.edit { preferences ->
            preferences[ThemePreferenceKey] = themePreference.name
        }
    }

    suspend fun completeOnboarding(studentName: String) {
        dataStore.edit { preferences ->
            preferences[StudentNameKey] = studentName
            preferences[OnboardingCompletedKey] = true
        }
    }

    private fun String?.toThemePreference(): ThemePreference {
        return ThemePreference.entries.firstOrNull { it.name == this } ?: ThemePreference.System
    }

    private companion object {
        val StudentNameKey = stringPreferencesKey("student_name")
        val ThemePreferenceKey = stringPreferencesKey("theme_preference")
        val OnboardingCompletedKey = booleanPreferencesKey("onboarding_completed")
    }
}
