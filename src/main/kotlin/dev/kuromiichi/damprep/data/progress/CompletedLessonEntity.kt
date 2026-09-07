package dev.kuromiichi.damprep.data.progress

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "lesson_progress")
data class CompletedLessonEntity(
    @PrimaryKey
    @ColumnInfo(name = "lesson_id")
    val lessonId: String,
    @ColumnInfo(name = "completed_at")
    val completedAt: Long,
)
