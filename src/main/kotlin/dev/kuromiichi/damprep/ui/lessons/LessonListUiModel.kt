package dev.kuromiichi.damprep.ui.lessons

import dev.kuromiichi.damprep.data.lessons.LessonSummary
import dev.kuromiichi.damprep.data.lessons.SubjectArea

data class LessonListItemUiModel(
    val index: Int,
    val id: String,
    val title: String,
    val subjectArea: SubjectArea,
    val isUnlocked: Boolean,
    val isAvailable: Boolean,
    val isCompleted: Boolean,
)

fun LessonSummary.toLessonListItemUiModel(
    index: Int,
    isUnlocked: Boolean,
    isAvailable: Boolean,
    isCompleted: Boolean,
): LessonListItemUiModel =
    LessonListItemUiModel(
        index = index,
        id = id,
        title = title,
        subjectArea = subjectArea,
        isUnlocked = isUnlocked,
        isAvailable = isAvailable,
        isCompleted = isCompleted,
    )
