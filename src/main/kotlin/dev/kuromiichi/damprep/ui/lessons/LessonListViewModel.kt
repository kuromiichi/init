package dev.kuromiichi.damprep.ui.lessons

import dev.kuromiichi.damprep.data.lessons.LessonCatalog
import dev.kuromiichi.damprep.data.lessons.LessonContentCatalog
import dev.kuromiichi.damprep.data.lessons.LessonProgressState
import dev.kuromiichi.damprep.data.lessons.LessonSummary

data class LessonListUiState(
    val lessons: List<LessonListItemUiModel>,
)

class LessonListViewModel(
    private val lessonCatalog: List<LessonSummary> = LessonCatalog.lessons,
    private val hasContent: (String) -> Boolean = LessonContentCatalog::hasContent,
) {
    fun createUiState(completedLessonIds: Set<String>): LessonListUiState {
        val lessonProgress = LessonProgressState(lessonCatalog, completedLessonIds)

        return LessonListUiState(
            lessons =
                lessonCatalog.mapIndexed { index, lesson ->
                    lesson.toLessonListItemUiModel(
                        index = index,
                        isUnlocked = lessonProgress.isUnlocked(index),
                        isAvailable = hasContent(lesson.id),
                        isCompleted = lessonProgress.isCompleted(lesson.id),
                    )
                },
        )
    }
}
