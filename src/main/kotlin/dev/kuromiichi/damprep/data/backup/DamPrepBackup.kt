package dev.kuromiichi.damprep.data.backup

import kotlinx.serialization.Serializable

/** Versión del formato de archivo que escribe esta versión de la app y la máxima que sabe leer. */
const val BACKUP_FORMAT_VERSION = 1

@Serializable
data class DamPrepBackup(
    val formatVersion: Int,
    val exportedAt: String,
    val preferences: BackupPreferences,
    val progress: BackupProgress,
)

@Serializable
data class BackupPreferences(
    val studentName: String,
    val pronounPreference: String,
    val targetOperatingSystem: String? = null,
    val themePreference: String,
    val unlockAllLessons: Boolean = false,
)

@Serializable
data class BackupProgress(
    val completedLessons: List<BackupCompletedLesson> = emptyList(),
    val lessonSteps: List<BackupLessonStep> = emptyList(),
)

@Serializable
data class BackupCompletedLesson(
    val lessonId: String,
    val completedAt: Long,
)

@Serializable
data class BackupLessonStep(
    val lessonId: String,
    val stepIndex: Int,
)
