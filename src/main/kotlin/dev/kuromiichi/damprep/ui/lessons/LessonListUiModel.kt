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
    val previousLessonTitle: String? = null,
) {
    val canOpen: Boolean = isUnlocked && isAvailable

    /** Explica por qué no se puede abrir la lección, o `null` si se puede. */
    val unavailableReason: String? =
        when {
            canOpen -> null
            !isUnlocked && previousLessonTitle != null -> "Completa «$previousLessonTitle» para desbloquear esta lección."
            !isUnlocked -> "Completa la lección anterior para desbloquear esta lección."
            else -> "Esta lección estará disponible próximamente."
        }
}

fun LessonSummary.toLessonListItemUiModel(
    index: Int,
    isUnlocked: Boolean,
    isAvailable: Boolean,
    isCompleted: Boolean,
    previousLessonTitle: String? = null,
): LessonListItemUiModel =
    LessonListItemUiModel(
        index = index,
        id = id,
        title = title,
        subjectArea = subjectArea,
        isUnlocked = isUnlocked,
        isAvailable = isAvailable,
        isCompleted = isCompleted,
        previousLessonTitle = previousLessonTitle,
    )
