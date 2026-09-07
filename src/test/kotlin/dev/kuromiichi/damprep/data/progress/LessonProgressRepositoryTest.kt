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

    private class FakeLessonProgressDao : LessonProgressDao {
        val completedLessons = MutableStateFlow<List<CompletedLessonEntity>>(emptyList())

        override fun observeCompletedLessons() = completedLessons

        override suspend fun insert(completedLesson: CompletedLessonEntity) {
            if (completedLessons.value.none { it.lessonId == completedLesson.lessonId }) {
                completedLessons.value = completedLessons.value + completedLesson
            }
        }
    }
}
