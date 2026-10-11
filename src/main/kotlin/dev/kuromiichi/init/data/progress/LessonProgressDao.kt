package dev.kuromiichi.init.data.progress

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface LessonProgressDao {
    @Query("SELECT * FROM lesson_progress ORDER BY completed_at")
    fun observeCompletedLessons(): Flow<List<CompletedLessonEntity>>

    @Query("SELECT * FROM lesson_step_progress")
    fun observeLessonStepProgress(): Flow<List<LessonStepProgressEntity>>

    @Query("SELECT * FROM lesson_progress ORDER BY completed_at")
    suspend fun getCompletedLessons(): List<CompletedLessonEntity>

    @Query("SELECT * FROM lesson_step_progress")
    suspend fun getLessonStepProgress(): List<LessonStepProgressEntity>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(completedLesson: CompletedLessonEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCompletedLessons(completedLessons: List<CompletedLessonEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStepProgress(stepProgress: List<LessonStepProgressEntity>)

    @Upsert
    suspend fun upsertStepProgress(stepProgress: LessonStepProgressEntity)

    @Query("DELETE FROM lesson_step_progress WHERE lesson_id = :lessonId")
    suspend fun deleteStepProgress(lessonId: String)

    @Query("DELETE FROM lesson_progress")
    suspend fun deleteAllCompletedLessons()

    @Query("DELETE FROM lesson_step_progress")
    suspend fun deleteAllStepProgress()

    @Transaction
    suspend fun completeLesson(completedLesson: CompletedLessonEntity) {
        insert(completedLesson)
        deleteStepProgress(completedLesson.lessonId)
    }

    @Transaction
    suspend fun resetProgress() {
        deleteAllCompletedLessons()
        deleteAllStepProgress()
    }

    @Transaction
    suspend fun readProgress(): LessonProgressSnapshot = LessonProgressSnapshot(getCompletedLessons(), getLessonStepProgress())

    @Transaction
    suspend fun replaceProgress(snapshot: LessonProgressSnapshot) {
        resetProgress()
        insertCompletedLessons(snapshot.completedLessons)
        insertStepProgress(snapshot.stepProgress)
    }
}
