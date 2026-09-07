package dev.kuromiichi.damprep.data.progress

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

class LessonProgressRepository(
    private val lessonProgressDao: LessonProgressDao,
    private val currentTimeMillis: () -> Long = System::currentTimeMillis,
) {
    val completedLessonIds: Flow<Set<String>> =
        lessonProgressDao
            .observeCompletedLessons()
            .map { lessons -> lessons.mapTo(linkedSetOf(), CompletedLessonEntity::lessonId) }
            .distinctUntilChanged()

    suspend fun completeLesson(lessonId: String) {
        lessonProgressDao.insert(
            CompletedLessonEntity(
                lessonId = lessonId,
                completedAt = currentTimeMillis(),
            ),
        )
    }
}
