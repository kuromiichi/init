package dev.kuromiichi.init.data.backup

import dev.kuromiichi.init.data.preferences.PronounPreference
import dev.kuromiichi.init.data.preferences.TargetOperatingSystem
import dev.kuromiichi.init.data.preferences.ThemePreference
import dev.kuromiichi.init.data.preferences.UserPreferencesRepository
import dev.kuromiichi.init.data.progress.LessonProgressRepository
import dev.kuromiichi.init.testing.FakeLessonProgressDao
import dev.kuromiichi.init.testing.InMemoryPreferencesDataStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import java.io.File
import java.nio.file.Files
import java.time.Instant
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class BackupRepositoryTest {
    @Test
    fun `export and import restore preferences and progress`() =
        runBlocking {
            val source = Fixture(this)
            source.preferences.completeOnboarding("Emma", PronounPreference.Feminine, TargetOperatingSystem.Windows)
            source.preferences.setThemePreference(ThemePreference.Dark)
            source.preferences.setUnlockAllLessons(true)
            source.progress.completeLesson("welcome")
            source.progress.saveLessonStep("software", 3)
            val file = tempFile()

            source.backup.exportTo(file)

            val target = Fixture(this)
            target.progress.completeLesson("old-lesson")
            target.backup.importFrom(file)

            val restored = target.preferences.preferences.first()
            assertEquals("Emma", restored.studentName)
            assertEquals(PronounPreference.Feminine, restored.pronounPreference)
            assertEquals(TargetOperatingSystem.Windows, restored.targetOperatingSystem)
            assertEquals(ThemePreference.Dark, restored.themePreference)
            assertTrue(restored.unlockAllLessons)
            assertTrue(restored.onboardingCompleted)
            assertEquals(setOf("welcome"), target.progress.completedLessonIds.first())
            assertEquals(mapOf("software" to 3), target.progress.lessonStepIndices.first())
        }

    @Test
    fun `export writes versioned json with the export date`() =
        runBlocking {
            val fixture = Fixture(this)
            val file = tempFile()

            fixture.backup.exportTo(file)

            val text = file.readText()
            assertTrue("\"formatVersion\": 1" in text)
            assertTrue("2026-10-06T10:15:00Z" in text)
            assertTrue("\"unlockAllLessons\": false" in text)
        }

    @Test
    fun `unknown fields are ignored`() =
        runBlocking {
            val fixture = Fixture(this)
            val file = tempFile(validJson(extra = "\"futureField\": 42,"))

            fixture.backup.importFrom(file)

            assertEquals(
                "Emma",
                fixture.preferences.preferences
                    .first()
                    .studentName,
            )
        }

    @Test
    fun `invalid files are rejected without touching current data`() =
        runBlocking {
            val fixture = Fixture(this)
            fixture.preferences.completeOnboarding("Actual", PronounPreference.Neutral, TargetOperatingSystem.Linux)
            fixture.progress.completeLesson("welcome")

            val invalidContents =
                listOf(
                    "no es json",
                    "{}",
                    validJson(formatVersion = BACKUP_FORMAT_VERSION + 1),
                    validJson(formatVersion = 0),
                    validJson(theme = "Fucsia"),
                    validJson(pronoun = "Otro"),
                    validJson(operatingSystem = "\"BeOS\""),
                    validJson(steps = """[{"lessonId": "welcome", "stepIndex": -1}]"""),
                )

            invalidContents.forEach { contents ->
                assertFailsWith<BackupImportException>(contents) { fixture.backup.importFrom(tempFile(contents)) }
            }
            assertFailsWith<BackupImportException> { fixture.backup.importFrom(File("no-existe.json")) }

            assertEquals(
                "Actual",
                fixture.preferences.preferences
                    .first()
                    .studentName,
            )
            assertEquals(setOf("welcome"), fixture.progress.completedLessonIds.first())
        }

    @Test
    fun `a backup from a newer version names the reason`() =
        runBlocking {
            val fixture = Fixture(this)

            val error =
                assertFailsWith<BackupImportException> {
                    fixture.backup.importFrom(tempFile(validJson(formatVersion = BACKUP_FORMAT_VERSION + 1)))
                }

            assertTrue("más reciente" in error.message.orEmpty())
        }

    @Test
    fun `export overwrites an existing file and leaves no temporary files`() =
        runBlocking {
            val fixture = Fixture(this)
            val directory = Files.createTempDirectory("init-backup").toFile().apply { deleteOnExit() }
            val file = File(directory, "copia.json").apply { writeText("antiguo") }

            fixture.backup.exportTo(file)

            assertFalse("antiguo" in file.readText())
            assertEquals(listOf("copia.json"), directory.list()?.toList())
        }

    private class Fixture(
        scope: CoroutineScope,
    ) {
        val preferences = UserPreferencesRepository(InMemoryPreferencesDataStore(), scope)
        val progress = LessonProgressRepository(FakeLessonProgressDao(), scope)
        val backup =
            BackupRepository(
                preferences,
                progress,
                now = { Instant.parse("2026-10-06T10:15:00Z") },
            )
    }

    private fun tempFile(contents: String? = null): File =
        Files.createTempFile("init", ".json").toFile().apply {
            deleteOnExit()
            contents?.let(::writeText)
        }

    private fun validJson(
        formatVersion: Int = BACKUP_FORMAT_VERSION,
        theme: String = "Light",
        pronoun: String = "Feminine",
        operatingSystem: String = "null",
        steps: String = "[]",
        extra: String = "",
    ) = """
        {
          $extra
          "formatVersion": $formatVersion,
          "exportedAt": "2026-10-06T10:15:00Z",
          "preferences": {
            "studentName": "Emma",
            "pronounPreference": "$pronoun",
            "targetOperatingSystem": $operatingSystem,
            "themePreference": "$theme"
          },
          "progress": { "completedLessons": [], "lessonSteps": $steps }
        }
        """.trimIndent()
}
