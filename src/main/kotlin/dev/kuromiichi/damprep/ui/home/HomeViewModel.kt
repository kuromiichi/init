package dev.kuromiichi.damprep.ui.home

import dev.kuromiichi.damprep.data.lessons.LessonCatalog
import dev.kuromiichi.damprep.data.preferences.PronounPreference

data class HomeUiState(
    val studentName: String,
    val welcomeMessage: String,
    val completedLessons: Int,
    val totalLessons: Int,
    val progress: Float,
    val progressLabel: String,
)

class HomeViewModel(
    private val totalLessonsProvider: () -> Int = { LessonCatalog.lessons.size },
) {
    fun createUiState(
        studentName: String,
        pronounPreference: PronounPreference,
        completedLessons: Int,
    ): HomeUiState {
        val totalLessons = totalLessonsProvider()
        val clampedCompletedLessons = completedLessons.coerceIn(0, totalLessons)
        val progress = if (totalLessons == 0) {
            0f
        } else {
            clampedCompletedLessons.toFloat() / totalLessons
        }

        return HomeUiState(
            studentName = studentName,
            welcomeMessage = "${pronounPreference.welcomeWord}, $studentName",
            completedLessons = clampedCompletedLessons,
            totalLessons = totalLessons,
            progress = progress,
            progressLabel = "$clampedCompletedLessons de $totalLessons temas completados",
        )
    }
}
