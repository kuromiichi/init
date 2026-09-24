package dev.kuromiichi.damprep.ui.lessons

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.kuromiichi.damprep.data.lessons.LessonCatalog
import dev.kuromiichi.damprep.data.lessons.LessonContentCatalog
import dev.kuromiichi.damprep.data.lessons.LessonProgressState
import dev.kuromiichi.damprep.data.lessons.LessonSummary
import dev.kuromiichi.damprep.data.preferences.UserPreferencesRepository
import dev.kuromiichi.damprep.data.progress.LessonProgressRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

data class LessonListUiState(
    val lessons: List<LessonListItemUiModel>,
)

class LessonListViewModel(
    preferencesRepository: UserPreferencesRepository,
    lessonProgressRepository: LessonProgressRepository,
    lessonCatalog: List<LessonSummary> = LessonCatalog.lessons,
    hasContent: (String) -> Boolean = LessonContentCatalog::hasContent,
) : ViewModel() {
    val uiState: StateFlow<LessonListUiState?> =
        combine(
            preferencesRepository.preferences,
            lessonProgressRepository.completedLessonIds,
        ) { preferences, completedLessonIds ->
            createLessonListUiState(
                completedLessonIds = completedLessonIds,
                unlockAllLessons = preferences.unlockAllLessons,
                lessonCatalog = lessonCatalog,
                hasContent = hasContent,
            )
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)
}

internal fun createLessonListUiState(
    completedLessonIds: Set<String>,
    unlockAllLessons: Boolean = false,
    lessonCatalog: List<LessonSummary> = LessonCatalog.lessons,
    hasContent: (String) -> Boolean = LessonContentCatalog::hasContent,
): LessonListUiState {
    val lessonProgress =
        LessonProgressState(
            lessons = lessonCatalog,
            completedLessonIds = completedLessonIds,
            unlockAllLessons = unlockAllLessons,
        )

    return LessonListUiState(
        lessons =
            lessonCatalog.mapIndexed { index, lesson ->
                lesson.toLessonListItemUiModel(
                    index = index,
                    isUnlocked = lessonProgress.isUnlocked(index),
                    isAvailable = hasContent(lesson.id),
                    isCompleted = lessonProgress.isCompleted(lesson.id),
                    previousLessonTitle = lessonCatalog.getOrNull(index - 1)?.title,
                )
            },
    )
}
