package dev.kuromiichi.damprep.data.progress

import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import androidx.sqlite.execSQL
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import java.nio.file.Files
import kotlin.test.Test
import kotlin.test.assertEquals

class DamPrepDatabaseTest {
    @Test
    fun `room persists and reads completed lessons`() =
        runBlocking {
            val databaseFile =
                Files
                    .createTempDirectory("dam-prep-room-test")
                    .resolve("progress.db")
                    .toFile()
            val writerDatabase = createDamPrepDatabase(databaseFile)

            try {
                val dao = writerDatabase.lessonProgressDao()
                dao.insert(CompletedLessonEntity(lessonId = "welcome", completedAt = 123L))
            } finally {
                writerDatabase.close()
            }

            val readerDatabase = createDamPrepDatabase(databaseFile)
            try {
                val completedLessonIds =
                    readerDatabase
                        .lessonProgressDao()
                        .observeCompletedLessons()
                        .first()
                        .map(CompletedLessonEntity::lessonId)

                assertEquals(listOf("welcome"), completedLessonIds)
            } finally {
                readerDatabase.close()
            }
        }

    @Test
    fun `migration from version one preserves completed lessons`() =
        runBlocking {
            val databaseFile =
                Files
                    .createTempDirectory("dam-prep-room-migration-test")
                    .resolve("progress.db")
                    .toFile()
            BundledSQLiteDriver().open(databaseFile.absolutePath).use { connection ->
                connection.execSQL(
                    "CREATE TABLE lesson_progress (lesson_id TEXT NOT NULL, completed_at INTEGER NOT NULL, PRIMARY KEY(lesson_id))",
                )
                connection.execSQL(
                    "CREATE TABLE room_master_table (id INTEGER PRIMARY KEY, identity_hash TEXT)",
                )
                connection.execSQL(
                    "INSERT INTO room_master_table (id, identity_hash) VALUES (42, 'd745335792d5af9057f99ebaa6714d88')",
                )
                connection.execSQL(
                    "INSERT INTO lesson_progress (lesson_id, completed_at) VALUES ('welcome', 123)",
                )
                connection.execSQL("PRAGMA user_version = 1")
            }

            val migratedDatabase = createDamPrepDatabase(databaseFile)
            try {
                val dao = migratedDatabase.lessonProgressDao()

                assertEquals(
                    listOf("welcome"),
                    dao.observeCompletedLessons().first().map(CompletedLessonEntity::lessonId),
                )
                assertEquals(emptyList(), dao.observeLessonStepProgress().first())
            } finally {
                migratedDatabase.close()
            }
        }
}
