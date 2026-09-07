package dev.kuromiichi.damprep.ui.home

import dev.kuromiichi.damprep.data.lessons.LessonCatalog
import dev.kuromiichi.damprep.data.lessons.LessonContentCatalog
import dev.kuromiichi.damprep.data.lessons.LessonProgressState
import dev.kuromiichi.damprep.data.lessons.LessonSummary
import dev.kuromiichi.damprep.data.preferences.PronounPreference

data class HomeUiState(
    val studentName: String,
    val welcomeMessage: String,
    val completedLessons: Int,
    val totalLessons: Int,
    val progress: Float,
    val progressLabel: String,
    val continueLessonId: String?,
)

class HomeViewModel(
    private val lessonCatalog: List<LessonSummary> = LessonCatalog.lessons,
    private val hasContent: (String) -> Boolean = LessonContentCatalog::hasContent,
) {
    fun createUiState(
        studentName: String,
        pronounPreference: PronounPreference,
        completedLessonIds: Set<String>,
    ): HomeUiState {
        val lessonProgress = LessonProgressState(lessonCatalog, completedLessonIds)

        return HomeUiState(
            studentName = studentName,
            welcomeMessage = "${pronounPreference.welcomeWord}, $studentName",
            completedLessons = lessonProgress.completedLessonCount,
            totalLessons = lessonProgress.totalLessonCount,
            progress = lessonProgress.progress,
            progressLabel =
                "${lessonProgress.completedLessonCount} de ${lessonProgress.totalLessonCount} temas completados",
            continueLessonId = lessonProgress.nextAvailableLessonId(hasContent),
        )
    }
}
