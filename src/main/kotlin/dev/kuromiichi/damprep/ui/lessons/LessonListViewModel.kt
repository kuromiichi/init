package dev.kuromiichi.damprep.ui.lessons

import dev.kuromiichi.damprep.data.lessons.LessonCatalog
import dev.kuromiichi.damprep.data.lessons.LessonContentCatalog
import dev.kuromiichi.damprep.data.lessons.LessonSummary
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class LessonListUiState(
    val lessons: List<LessonListItemUiModel>,
)

class LessonListViewModel(
    lessonCatalog: List<LessonSummary> = LessonCatalog.lessons,
    hasContent: (String) -> Boolean = LessonContentCatalog::hasContent,
) {
    private val _uiState = MutableStateFlow(
        LessonListUiState(
            lessons = lessonCatalog.mapIndexed { index, lesson ->
                lesson.toLessonListItemUiModel(
                    index = index,
                    isUnlocked = hasContent(lesson.id),
                )
            },
        ),
    )
    val uiState: StateFlow<LessonListUiState> = _uiState.asStateFlow()
}
