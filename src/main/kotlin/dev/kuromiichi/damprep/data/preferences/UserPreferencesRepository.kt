package dev.kuromiichi.damprep.data.preferences

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

class UserPreferencesRepository(
    private val dataStore: DataStore<Preferences>,
) {
    val preferences: Flow<UserPreferences> =
        dataStore.data
            .catch { exception ->
                if (exception is IOException) {
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }.map { preferences ->
                UserPreferences(
                    studentName = preferences[StudentNameKey].orEmpty(),
                    themePreference = preferences[ThemePreferenceKey].toThemePreference(),
                    pronounPreference = preferences[PronounPreferenceKey].toPronounPreference(),
                    targetOperatingSystem = preferences[TargetOperatingSystemKey].toTargetOperatingSystem(),
                    onboardingCompleted = preferences[OnboardingCompletedKey] ?: false,
                )
            }

    suspend fun setThemePreference(themePreference: ThemePreference) {
        dataStore.edit { preferences ->
            preferences[ThemePreferenceKey] = themePreference.name
        }
    }

    suspend fun setStudentName(studentName: String) {
        dataStore.edit { preferences ->
            preferences[StudentNameKey] = studentName
        }
    }

    suspend fun setPronounPreference(pronounPreference: PronounPreference) {
        dataStore.edit { preferences ->
            preferences[PronounPreferenceKey] = pronounPreference.name
        }
    }

    suspend fun setTargetOperatingSystem(targetOperatingSystem: TargetOperatingSystem) {
        dataStore.edit { preferences ->
            preferences[TargetOperatingSystemKey] = targetOperatingSystem.name
        }
    }

    suspend fun completeOnboarding(
        studentName: String,
        pronounPreference: PronounPreference,
        targetOperatingSystem: TargetOperatingSystem,
    ) {
        dataStore.edit { preferences ->
            preferences[StudentNameKey] = studentName
            preferences[PronounPreferenceKey] = pronounPreference.name
            preferences[TargetOperatingSystemKey] = targetOperatingSystem.name
            preferences[OnboardingCompletedKey] = true
        }
    }

    private fun String?.toThemePreference(): ThemePreference = ThemePreference.entries.firstOrNull { it.name == this } ?: ThemePreference.System

    private fun String?.toPronounPreference(): PronounPreference =
        PronounPreference.entries.firstOrNull {
            it.name == this
        } ?: PronounPreference.Neutral

    private fun String?.toTargetOperatingSystem(): TargetOperatingSystem? = TargetOperatingSystem.entries.firstOrNull { it.name == this }

    private companion object {
        val StudentNameKey = stringPreferencesKey("student_name")
        val ThemePreferenceKey = stringPreferencesKey("theme_preference")
        val PronounPreferenceKey = stringPreferencesKey("pronoun_preference")
        val TargetOperatingSystemKey = stringPreferencesKey("target_operating_system")
        val OnboardingCompletedKey = booleanPreferencesKey("onboarding_completed")
    }
}
