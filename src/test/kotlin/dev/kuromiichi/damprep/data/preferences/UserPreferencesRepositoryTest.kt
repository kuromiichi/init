package dev.kuromiichi.damprep.data.preferences

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import java.io.IOException
import java.nio.file.Files
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class UserPreferencesRepositoryTest {
    @Test
    fun `empty store exposes default preferences`() =
        runBlocking {
            val repository = createRepository()

            val preferences = repository.preferences.first()

            assertEquals("", preferences.studentName)
            assertEquals(ThemePreference.System, preferences.themePreference)
            assertEquals(PronounPreference.Neutral, preferences.pronounPreference)
            assertEquals(null, preferences.targetOperatingSystem)
            assertFalse(preferences.onboardingCompleted)
            assertFalse(preferences.unlockAllLessons)
        }

    @Test
    fun `theme preference is persisted`() =
        runBlocking {
            val repository = createRepository()

            repository.setThemePreference(ThemePreference.Dark)

            assertEquals(ThemePreference.Dark, repository.preferences.first().themePreference)
        }

    @Test
    fun `student name is persisted`() =
        runBlocking {
            val repository = createRepository()

            repository.setStudentName("Emma")

            assertEquals("Emma", repository.preferences.first().studentName)
        }

    @Test
    fun `pronoun preference is persisted`() =
        runBlocking {
            val repository = createRepository()

            repository.setPronounPreference(PronounPreference.Masculine)

            assertEquals(PronounPreference.Masculine, repository.preferences.first().pronounPreference)
        }

    @Test
    fun `completed onboarding stores student name and completion flag`() =
        runBlocking {
            val repository = createRepository()

            repository.completeOnboarding(
                studentName = "Ada",
                pronounPreference = PronounPreference.Feminine,
                targetOperatingSystem = TargetOperatingSystem.Linux,
            )

            val preferences = repository.preferences.first()
            assertEquals("Ada", preferences.studentName)
            assertEquals(PronounPreference.Feminine, preferences.pronounPreference)
            assertEquals(TargetOperatingSystem.Linux, preferences.targetOperatingSystem)
            assertTrue(preferences.onboardingCompleted)
        }

    @Test
    fun `completing onboarding keeps existing theme preference`() =
        runBlocking {
            val repository = createRepository()

            repository.setThemePreference(ThemePreference.Dark)
            repository.completeOnboarding(
                studentName = "Ada",
                pronounPreference = PronounPreference.Feminine,
                targetOperatingSystem = TargetOperatingSystem.MacOS,
            )

            assertEquals(ThemePreference.Dark, repository.preferences.first().themePreference)
        }

    @Test
    fun `changing theme keeps existing onboarding data`() =
        runBlocking {
            val repository = createRepository()

            repository.completeOnboarding(
                studentName = "Ada",
                pronounPreference = PronounPreference.Feminine,
                targetOperatingSystem = TargetOperatingSystem.Windows,
            )
            repository.setThemePreference(ThemePreference.Light)

            val preferences = repository.preferences.first()
            assertEquals("Ada", preferences.studentName)
            assertEquals(PronounPreference.Feminine, preferences.pronounPreference)
            assertTrue(preferences.onboardingCompleted)
            assertEquals(ThemePreference.Light, preferences.themePreference)
            assertEquals(TargetOperatingSystem.Windows, preferences.targetOperatingSystem)
        }

    @Test
    fun `target operating system is persisted`() =
        runBlocking {
            val repository = createRepository()

            repository.setTargetOperatingSystem(TargetOperatingSystem.MacOS)

            assertEquals(TargetOperatingSystem.MacOS, repository.preferences.first().targetOperatingSystem)
        }

    @Test
    fun `unlock all lessons preference is persisted`() =
        runBlocking {
            val repository = createRepository()

            repository.setUnlockAllLessons(true)

            assertTrue(repository.preferences.first().unlockAllLessons)
        }

    @Test
    fun `invalid stored theme falls back to system`() =
        runBlocking {
            val dataStore = createDataStore()
            dataStore.edit { preferences ->
                preferences[stringPreferencesKey("theme_preference")] = "Solarized"
            }
            val repository = UserPreferencesRepository(dataStore)

            assertEquals(ThemePreference.System, repository.preferences.first().themePreference)
        }

    @Test
    fun `invalid stored pronoun preference falls back to neutral`() =
        runBlocking {
            val dataStore = createDataStore()
            dataStore.edit { preferences ->
                preferences[stringPreferencesKey("pronoun_preference")] = "Royal"
            }
            val repository = UserPreferencesRepository(dataStore)

            assertEquals(PronounPreference.Neutral, repository.preferences.first().pronounPreference)
        }

    @Test
    fun `invalid stored target operating system falls back to no selection`() =
        runBlocking {
            val dataStore = createDataStore()
            dataStore.edit { preferences ->
                preferences[stringPreferencesKey("target_operating_system")] = "Solaris"
            }
            val repository = UserPreferencesRepository(dataStore)

            assertEquals(null, repository.preferences.first().targetOperatingSystem)
        }

    @Test
    fun `io error while reading exposes default preferences`() =
        runBlocking {
            val failingDataStore =
                object : DataStore<Preferences> {
                    override val data = flow<Preferences> { throw IOException("Unreadable preferences") }

                    override suspend fun updateData(
                        transform: suspend (Preferences) -> Preferences,
                    ): Preferences = error("Updates are not expected in this test")
                }
            val repository = UserPreferencesRepository(failingDataStore)

            assertEquals(UserPreferences(), repository.preferences.first())
        }

    private fun createRepository(): UserPreferencesRepository = UserPreferencesRepository(createDataStore())

    private fun createDataStore() =
        PreferenceDataStoreFactory.create(
            produceFile = {
                val directory = Files.createTempDirectory("dam-prep-test")
                directory.resolve("user.preferences_pb").toFile()
            },
        )
}
