package dev.kuromiichi.damprep.data.progress

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface LessonProgressDao {
    @Query("SELECT * FROM lesson_progress ORDER BY completed_at")
    fun observeCompletedLessons(): Flow<List<CompletedLessonEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(completedLesson: CompletedLessonEntity)
}
