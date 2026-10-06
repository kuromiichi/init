package dev.kuromiichi.damprep.data.progress

data class LessonProgressSnapshot(
    val completedLessons: List<CompletedLessonEntity>,
    val stepProgress: List<LessonStepProgressEntity>,
)
