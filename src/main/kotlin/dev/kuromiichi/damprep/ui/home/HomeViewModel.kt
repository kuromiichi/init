package dev.kuromiichi.damprep.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.kuromiichi.damprep.data.lessons.LessonCatalog
import dev.kuromiichi.damprep.data.lessons.LessonContentCatalog
import dev.kuromiichi.damprep.data.lessons.LessonProgressState
import dev.kuromiichi.damprep.data.lessons.LessonSummary
import dev.kuromiichi.damprep.data.preferences.PronounPreference
import dev.kuromiichi.damprep.data.preferences.UserPreferencesRepository
import dev.kuromiichi.damprep.data.progress.LessonProgressRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

data class HomeUiState(
    val studentName: String,
    val welcomeMessage: String,
    val completedLessons: Int,
    val totalLessons: Int,
    val progress: Float,
    val progressLabel: String,
    val continueLessonId: String?,
    val continueLessonTitle: String?,
    val continueLessonSubject: String?,
    val isContinueLessonStarted: Boolean,
)

class HomeViewModel(
    preferencesRepository: UserPreferencesRepository,
    lessonProgressRepository: LessonProgressRepository,
    lessonCatalog: List<LessonSummary> = LessonCatalog.lessons,
    hasContent: (String) -> Boolean = LessonContentCatalog::hasContent,
) : ViewModel() {
    val uiState: StateFlow<HomeUiState?> =
        combine(
            preferencesRepository.preferences,
            lessonProgressRepository.completedLessonIds,
            lessonProgressRepository.lessonStepIndices,
        ) { preferences, completedLessonIds, lessonStepIndices ->
            createHomeUiState(
                studentName = preferences.studentName,
                pronounPreference = preferences.pronounPreference,
                completedLessonIds = completedLessonIds,
                lessonStepIndices = lessonStepIndices,
                unlockAllLessons = preferences.unlockAllLessons,
                lessonCatalog = lessonCatalog,
                hasContent = hasContent,
            )
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)
}

internal fun createHomeUiState(
    studentName: String,
    pronounPreference: PronounPreference,
    completedLessonIds: Set<String>,
    lessonStepIndices: Map<String, Int> = emptyMap(),
    unlockAllLessons: Boolean = false,
    lessonCatalog: List<LessonSummary> = LessonCatalog.lessons,
    hasContent: (String) -> Boolean = LessonContentCatalog::hasContent,
): HomeUiState {
    val lessonProgress =
        LessonProgressState(
            lessons = lessonCatalog,
            completedLessonIds = completedLessonIds,
            unlockAllLessons = unlockAllLessons,
        )

    val continueLessonId = lessonProgress.nextAvailableLessonId(hasContent)
    val continueLesson = lessonCatalog.firstOrNull { it.id == continueLessonId }

    return HomeUiState(
        studentName = studentName,
        welcomeMessage = "${pronounPreference.welcomeWord}, $studentName",
        completedLessons = lessonProgress.completedLessonCount,
        totalLessons = lessonProgress.totalLessonCount,
        progress = lessonProgress.progress,
        progressLabel =
            "${lessonProgress.completedLessonCount} de ${lessonProgress.totalLessonCount} lecciones completadas",
        continueLessonId = continueLessonId,
        continueLessonTitle = continueLesson?.title,
        continueLessonSubject = continueLesson?.subjectArea?.label,
        isContinueLessonStarted = continueLessonId in lessonStepIndices,
    )
}
