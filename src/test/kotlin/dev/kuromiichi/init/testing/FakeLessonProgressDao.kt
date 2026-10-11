package dev.kuromiichi.init.testing

import dev.kuromiichi.init.data.progress.CompletedLessonEntity
import dev.kuromiichi.init.data.progress.LessonProgressDao
import dev.kuromiichi.init.data.progress.LessonProgressSnapshot
import dev.kuromiichi.init.data.progress.LessonStepProgressEntity
import kotlinx.coroutines.flow.MutableStateFlow

class FakeLessonProgressDao : LessonProgressDao {
    val completedLessons = MutableStateFlow<List<CompletedLessonEntity>>(emptyList())
    val stepProgress = MutableStateFlow<List<LessonStepProgressEntity>>(emptyList())

    override fun observeCompletedLessons() = completedLessons

    override fun observeLessonStepProgress() = stepProgress

    override suspend fun getCompletedLessons() = completedLessons.value

    override suspend fun getLessonStepProgress() = stepProgress.value

    override suspend fun insertCompletedLessons(completedLessons: List<CompletedLessonEntity>) {
        this.completedLessons.value =
            this.completedLessons.value.filterNot { existing -> completedLessons.any { it.lessonId == existing.lessonId } } + completedLessons
    }

    override suspend fun insertStepProgress(stepProgress: List<LessonStepProgressEntity>) {
        stepProgress.forEach { upsertStepProgress(it) }
    }

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

    override suspend fun readProgress() = LessonProgressSnapshot(completedLessons.value, stepProgress.value)

    override suspend fun replaceProgress(snapshot: LessonProgressSnapshot) {
        completedLessons.value = snapshot.completedLessons
        stepProgress.value = snapshot.stepProgress
    }
}
