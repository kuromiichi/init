package dev.kuromiichi.damprep.data.progress

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

/**
 * Las escrituras se lanzan en [externalScope], de ámbito de aplicación, para que el progreso
 * se guarde aunque la lección se cierre justo después.
 */
class LessonProgressRepository(
    private val lessonProgressDao: LessonProgressDao,
    private val externalScope: CoroutineScope,
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

    suspend fun completeLesson(lessonId: String) =
        write {
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
    ) = write {
        lessonProgressDao.upsertStepProgress(
            LessonStepProgressEntity(
                lessonId = lessonId,
                stepIndex = stepIndex,
            ),
        )
    }

    suspend fun snapshot(): LessonProgressSnapshot = lessonProgressDao.readProgress()

    suspend fun replaceProgress(snapshot: LessonProgressSnapshot) = write { lessonProgressDao.replaceProgress(snapshot) }

    suspend fun resetProgress() = write { lessonProgressDao.resetProgress() }

    private suspend fun write(block: suspend () -> Unit) {
        externalScope.launch { block() }.join()
    }
}
