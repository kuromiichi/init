package dev.kuromiichi.damprep.data.progress

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals

class LessonProgressRepositoryTest {
    @Test
    fun `completed lessons are exposed as unique ids`() =
        runBlocking {
            val dao = FakeLessonProgressDao()
            val repository = LessonProgressRepository(dao, currentTimeMillis = { 123L })

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
            val repository = LessonProgressRepository(dao, currentTimeMillis = { 123L })

            repository.saveLessonStep("welcome", 2)
            assertEquals(mapOf("welcome" to 2), repository.lessonStepIndices.first())

            repository.completeLesson("welcome")
            assertEquals(emptyMap(), repository.lessonStepIndices.first())
        }

    @Test
    fun `reset removes every completed lesson`() =
        runBlocking {
            val dao = FakeLessonProgressDao()
            val repository = LessonProgressRepository(dao)
            repository.completeLesson("welcome")
            repository.completeLesson("installation")
            repository.saveLessonStep("installation", 2)

            repository.resetProgress()

            assertEquals(emptySet(), repository.completedLessonIds.first())
            assertEquals(emptyMap(), repository.lessonStepIndices.first())
        }

    private class FakeLessonProgressDao : LessonProgressDao {
        val completedLessons = MutableStateFlow<List<CompletedLessonEntity>>(emptyList())
        val stepProgress = MutableStateFlow<List<LessonStepProgressEntity>>(emptyList())

        override fun observeCompletedLessons() = completedLessons

        override fun observeLessonStepProgress() = stepProgress

        override suspend fun insert(completedLesson: CompletedLessonEntity) {
            if (completedLessons.value.none { it.lessonId == completedLesson.lessonId }) {
                completedLessons.value = completedLessons.value + completedLesson
            }
        }

        override suspend fun upsertStepProgress(stepProgress: LessonStepProgressEntity) {
            this.stepProgress.value = this.stepProgress.value.filterNot { it.lessonId == stepProgress.lessonId } + stepProgress
        }

        override suspend fun deleteStepProgress(lessonId: String) {
            stepProgress.value = stepProgress.value.filterNot { it.lessonId == lessonId }
        }

        override suspend fun deleteAllCompletedLessons() {
            completedLessons.value = emptyList()
        }

        override suspend fun deleteAllStepProgress() {
            stepProgress.value = emptyList()
        }
    }
}
