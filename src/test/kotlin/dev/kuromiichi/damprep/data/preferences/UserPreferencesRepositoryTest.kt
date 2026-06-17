package dev.kuromiichi.damprep.data.preferences

import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import dev.kuromiichi.damprep.ui.theme.ThemePreference
import java.nio.file.Files
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

class UserPreferencesRepositoryTest {
    @Test
    fun `empty store exposes default preferences`() = runBlocking {
        val repository = createRepository()

        val preferences = repository.preferences.first()

        assertEquals("", preferences.studentName)
        assertEquals(ThemePreference.System, preferences.themePreference)
        assertFalse(preferences.onboardingCompleted)
    }

    @Test
    fun `theme preference is persisted`() = runBlocking {
        val repository = createRepository()

        repository.setThemePreference(ThemePreference.Dark)

        assertEquals(ThemePreference.Dark, repository.preferences.first().themePreference)
    }

    @Test
    fun `completed onboarding stores student name and completion flag`() = runBlocking {
        val repository = createRepository()

        repository.completeOnboarding("Ada")

        val preferences = repository.preferences.first()
        assertEquals("Ada", preferences.studentName)
        assertTrue(preferences.onboardingCompleted)
    }

    @Test
    fun `completing onboarding keeps existing theme preference`() = runBlocking {
        val repository = createRepository()

        repository.setThemePreference(ThemePreference.Dark)
        repository.completeOnboarding("Ada")

        assertEquals(ThemePreference.Dark, repository.preferences.first().themePreference)
    }

    @Test
    fun `changing theme keeps existing onboarding data`() = runBlocking {
        val repository = createRepository()

        repository.completeOnboarding("Ada")
        repository.setThemePreference(ThemePreference.Light)

        val preferences = repository.preferences.first()
        assertEquals("Ada", preferences.studentName)
        assertTrue(preferences.onboardingCompleted)
        assertEquals(ThemePreference.Light, preferences.themePreference)
    }

    @Test
    fun `invalid stored theme falls back to system`() = runBlocking {
        val dataStore = createDataStore()
        dataStore.edit { preferences ->
            preferences[stringPreferencesKey("theme_preference")] = "Solarized"
        }
        val repository = UserPreferencesRepository(dataStore)

        assertEquals(ThemePreference.System, repository.preferences.first().themePreference)
    }

    private fun createRepository(): UserPreferencesRepository {
        return UserPreferencesRepository(createDataStore())
    }

    private fun createDataStore() = PreferenceDataStoreFactory.create(
        produceFile = {
            val directory = Files.createTempDirectory("dam-prep-test")
            directory.resolve("user.preferences_pb").toFile()
        },
    )
}
