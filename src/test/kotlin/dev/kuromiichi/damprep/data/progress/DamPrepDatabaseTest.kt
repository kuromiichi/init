package dev.kuromiichi.damprep.data.progress

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
}
