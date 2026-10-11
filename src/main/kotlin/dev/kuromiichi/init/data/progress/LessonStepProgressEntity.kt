package dev.kuromiichi.init.data.progress

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "lesson_step_progress")
data class LessonStepProgressEntity(
    @PrimaryKey
    @ColumnInfo(name = "lesson_id")
    val lessonId: String,
    @ColumnInfo(name = "step_index")
    val stepIndex: Int,
)
