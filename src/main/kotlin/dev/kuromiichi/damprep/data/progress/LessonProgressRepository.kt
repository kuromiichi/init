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

    val lessonStepIndices: Flow<Map<String, Int>> =
        lessonProgressDao
            .observeLessonStepProgress()
            .map { progress -> progress.associate { it.lessonId to it.stepIndex } }
            .distinctUntilChanged()

    suspend fun completeLesson(lessonId: String) {
        lessonProgressDao.completeLesson(
            CompletedLessonEntity(
                lessonId = lessonId,
                completedAt = currentTimeMillis(),
            ),
        )
    }

    suspend fun saveLessonStep(
        lessonId: String,
        stepIndex: Int,
    ) {
        lessonProgressDao.upsertStepProgress(
            LessonStepProgressEntity(
                lessonId = lessonId,
                stepIndex = stepIndex,
            ),
        )
    }

    suspend fun resetProgress() {
        lessonProgressDao.resetProgress()
    }
}
