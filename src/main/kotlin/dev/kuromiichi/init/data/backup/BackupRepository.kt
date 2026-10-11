package dev.kuromiichi.init.data.backup

import dev.kuromiichi.init.data.preferences.PronounPreference
import dev.kuromiichi.init.data.preferences.TargetOperatingSystem
import dev.kuromiichi.init.data.preferences.ThemePreference
import dev.kuromiichi.init.data.preferences.UserPreferences
import dev.kuromiichi.init.data.preferences.UserPreferencesRepository
import dev.kuromiichi.init.data.progress.CompletedLessonEntity
import dev.kuromiichi.init.data.progress.LessonProgressRepository
import dev.kuromiichi.init.data.progress.LessonProgressSnapshot
import dev.kuromiichi.init.data.progress.LessonStepProgressEntity
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import java.io.File
import java.io.IOException
import java.nio.file.Files
import java.nio.file.StandardCopyOption
import java.time.Instant

/** Error al leer una copia de seguridad; [message] está pensado para mostrarse al alumno. */
class BackupImportException(
    message: String,
    cause: Throwable? = null,
) : Exception(message, cause)

/**
 * Exporta e importa los datos del alumno (preferencias y progreso) como un único archivo JSON.
 * Importar sustituye los datos actuales; si el archivo no es válido no se modifica nada.
 */
class BackupRepository(
    private val preferencesRepository: UserPreferencesRepository,
    private val lessonProgressRepository: LessonProgressRepository,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
    private val now: () -> Instant = Instant::now,
) {
    suspend fun exportTo(file: File) {
        val backup = createBackup()
        withContext(ioDispatcher) {
            val temporaryFile = File.createTempFile("init-copia", ".tmp", file.absoluteFile.parentFile)
            try {
                temporaryFile.writeText(BackupJson.encodeToString(InitBackup.serializer(), backup))
                Files.move(temporaryFile.toPath(), file.toPath(), StandardCopyOption.REPLACE_EXISTING)
            } finally {
                temporaryFile.delete()
            }
        }
    }

    suspend fun importFrom(file: File) {
        val backup = withContext(ioDispatcher) { readBackup(file) }
        val (preferences, snapshot) = backup.toDomain()

        lessonProgressRepository.replaceProgress(snapshot)
        // Las preferencias se escriben al final: marcan el onboarding como completado y disparan la navegación.
        preferencesRepository.restore(preferences)
    }

    private suspend fun createBackup(): InitBackup {
        val preferences = preferencesRepository.preferences.first()
        val progress = lessonProgressRepository.snapshot()

        return InitBackup(
            formatVersion = BACKUP_FORMAT_VERSION,
            exportedAt = now().toString(),
            preferences =
                BackupPreferences(
                    studentName = preferences.studentName,
                    pronounPreference = preferences.pronounPreference.name,
                    targetOperatingSystem = preferences.targetOperatingSystem?.name,
                    themePreference = preferences.themePreference.name,
                    unlockAllLessons = preferences.unlockAllLessons,
                ),
            progress =
                BackupProgress(
                    completedLessons = progress.completedLessons.map { BackupCompletedLesson(it.lessonId, it.completedAt) },
                    lessonSteps = progress.stepProgress.map { BackupLessonStep(it.lessonId, it.stepIndex) },
                ),
        )
    }

    private fun readBackup(file: File): InitBackup {
        val text =
            try {
                file.readText()
            } catch (exception: IOException) {
                throw BackupImportException("No se ha podido leer el archivo.", exception)
            }

        val backup =
            try {
                BackupJson.decodeFromString(InitBackup.serializer(), text)
            } catch (exception: SerializationException) {
                throw BackupImportException("El archivo no es una copia de seguridad de init válida.", exception)
            } catch (exception: IllegalArgumentException) {
                throw BackupImportException("El archivo no es una copia de seguridad de init válida.", exception)
            }

        if (backup.formatVersion > BACKUP_FORMAT_VERSION) {
            throw BackupImportException(
                "La copia se creó con una versión más reciente de init. Actualiza la aplicación para importarla.",
            )
        }
        if (backup.formatVersion < 1) {
            throw BackupImportException("El archivo no es una copia de seguridad de init válida.")
        }
        return backup
    }

    private fun InitBackup.toDomain(): Pair<UserPreferences, LessonProgressSnapshot> {
        val invalid = { BackupImportException("La copia de seguridad contiene datos no válidos.") }

        val pronoun = PronounPreference.entries.firstOrNull { it.name == preferences.pronounPreference } ?: throw invalid()
        val theme = ThemePreference.entries.firstOrNull { it.name == preferences.themePreference } ?: throw invalid()
        val operatingSystem =
            preferences.targetOperatingSystem?.let { value ->
                TargetOperatingSystem.entries.firstOrNull { it.name == value } ?: throw invalid()
            }
        if (progress.lessonSteps.any { it.stepIndex < 0 }) throw invalid()

        return UserPreferences(
            studentName = preferences.studentName,
            themePreference = theme,
            pronounPreference = pronoun,
            targetOperatingSystem = operatingSystem,
            onboardingCompleted = true,
            unlockAllLessons = preferences.unlockAllLessons,
        ) to
            LessonProgressSnapshot(
                completedLessons = progress.completedLessons.map { CompletedLessonEntity(it.lessonId, it.completedAt) },
                stepProgress = progress.lessonSteps.map { LessonStepProgressEntity(it.lessonId, it.stepIndex) },
            )
    }

    private companion object {
        val BackupJson =
            Json {
                ignoreUnknownKeys = true
                prettyPrint = true
                encodeDefaults = true
            }
    }
}
