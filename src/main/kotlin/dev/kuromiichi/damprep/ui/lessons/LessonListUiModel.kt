package dev.kuromiichi.damprep.ui.lessons

import dev.kuromiichi.damprep.data.lessons.LessonSummary
import dev.kuromiichi.damprep.data.lessons.SubjectArea

data class LessonListItemUiModel(
    val index: Int,
    val id: String,
    val title: String,
    val subjectArea: SubjectArea,
    val isUnlocked: Boolean,
)

fun LessonSummary.toLessonListItemUiModel(
    index: Int,
    isUnlocked: Boolean,
): LessonListItemUiModel =
    LessonListItemUiModel(
        index = index,
        id = id,
        title = title,
        subjectArea = subjectArea,
        isUnlocked = isUnlocked,
    )
