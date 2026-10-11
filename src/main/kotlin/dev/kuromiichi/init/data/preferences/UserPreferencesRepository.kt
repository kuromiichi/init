package dev.kuromiichi.init.data.preferences

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.MutablePreferences
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.io.IOException

/**
 * Las escrituras se lanzan en [externalScope], de ámbito de aplicación, para que terminen
 * aunque la pantalla que las pidió se cierre antes.
 */
class UserPreferencesRepository(
    private val dataStore: DataStore<Preferences>,
    private val externalScope: CoroutineScope,
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
                    unlockAllLessons = preferences[UnlockAllLessonsKey] ?: false,
                    checkForUpdates = preferences[CheckForUpdatesKey] ?: true,
                )
            }

    suspend fun setThemePreference(themePreference: ThemePreference) {
        edit { preferences ->
            preferences[ThemePreferenceKey] = themePreference.name
        }
    }

    suspend fun setStudentName(studentName: String) {
        edit { preferences ->
            preferences[StudentNameKey] = studentName
        }
    }

    suspend fun setPronounPreference(pronounPreference: PronounPreference) {
        edit { preferences ->
            preferences[PronounPreferenceKey] = pronounPreference.name
        }
    }

    suspend fun setTargetOperatingSystem(targetOperatingSystem: TargetOperatingSystem) {
        edit { preferences ->
            preferences[TargetOperatingSystemKey] = targetOperatingSystem.name
        }
    }

    suspend fun setUnlockAllLessons(unlockAllLessons: Boolean) {
        edit { preferences ->
            preferences[UnlockAllLessonsKey] = unlockAllLessons
        }
    }

    suspend fun setCheckForUpdates(checkForUpdates: Boolean) {
        edit { preferences ->
            preferences[CheckForUpdatesKey] = checkForUpdates
        }
    }

    suspend fun completeOnboarding(
        studentName: String,
        pronounPreference: PronounPreference,
        targetOperatingSystem: TargetOperatingSystem,
    ) {
        edit { preferences ->
            preferences[StudentNameKey] = studentName
            preferences[PronounPreferenceKey] = pronounPreference.name
            preferences[TargetOperatingSystemKey] = targetOperatingSystem.name
            preferences[OnboardingCompletedKey] = true
        }
    }

    /** Sustituye todas las preferencias por las de una copia de seguridad y da el onboarding por completado. */
    suspend fun restore(restored: UserPreferences) {
        edit { preferences ->
            preferences[StudentNameKey] = restored.studentName
            preferences[ThemePreferenceKey] = restored.themePreference.name
            preferences[PronounPreferenceKey] = restored.pronounPreference.name
            val targetOperatingSystem = restored.targetOperatingSystem
            if (targetOperatingSystem != null) {
                preferences[TargetOperatingSystemKey] = targetOperatingSystem.name
            } else {
                preferences.remove(TargetOperatingSystemKey)
            }
            preferences[UnlockAllLessonsKey] = restored.unlockAllLessons
            preferences[OnboardingCompletedKey] = true
        }
    }

    private suspend fun edit(transform: (MutablePreferences) -> Unit) {
        externalScope.launch { dataStore.edit(transform) }.join()
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
        val UnlockAllLessonsKey = booleanPreferencesKey("unlock_all_lessons")
        val CheckForUpdatesKey = booleanPreferencesKey("check_for_updates")
    }
}
