package dev.kuromiichi.init.data.progress

import dev.kuromiichi.init.testing.FakeLessonProgressDao
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals

class LessonProgressRepositoryTest {
    @Test
    fun `completed lessons are exposed as unique ids`() =
        runBlocking {
            val dao = FakeLessonProgressDao()
            val repository = LessonProgressRepository(dao, externalScope = this, currentTimeMillis = { 123L })

            repository.completeLesson("welcome")
            repository.completeLesson("welcome")

            assertEquals(setOf("welcome"), repository.completedLessonIds.first())
            assertEquals(
                123L,
                dao.completedLessons.value
                    .single()
                    .completedAt,
            )
        }

    @Test
    fun `lesson step is saved until lesson is completed`() =
        runBlocking {
            val dao = FakeLessonProgressDao()
            val repository = LessonProgressRepository(dao, externalScope = this, currentTimeMillis = { 123L })

            repository.saveLessonStep("welcome", 2)
            assertEquals(mapOf("welcome" to 2), repository.lessonStepIndices.first())

            repository.completeLesson("welcome")
            assertEquals(emptyMap(), repository.lessonStepIndices.first())
        }

    @Test
    fun `reset removes every completed lesson`() =
        runBlocking {
            val dao = FakeLessonProgressDao()
            val repository = LessonProgressRepository(dao, externalScope = this)
            repository.completeLesson("welcome")
            repository.completeLesson("installation")
            repository.saveLessonStep("installation", 2)

            repository.resetProgress()

            assertEquals(emptySet(), repository.completedLessonIds.first())
            assertEquals(emptyMap(), repository.lessonStepIndices.first())
        }
}
