package dev.kuromiichi.init.data.lessons

class LessonProgressState(
    private val lessons: List<LessonSummary>,
    completedLessonIds: Set<String>,
    private val unlockAllLessons: Boolean = false,
) {
    private val knownLessonIds = lessons.mapTo(hashSetOf(), LessonSummary::id)

    val completedLessonIds: Set<String> = completedLessonIds.intersect(knownLessonIds)
    val completedLessonCount: Int = this.completedLessonIds.size
    val totalLessonCount: Int = lessons.size
    val progress: Float =
        if (totalLessonCount == 0) {
            0f
        } else {
            completedLessonCount.toFloat() / totalLessonCount
        }

    fun isCompleted(lessonId: String): Boolean = lessonId in completedLessonIds

    fun isUnlocked(index: Int): Boolean {
        val lesson = lessons.getOrNull(index) ?: return false

        return unlockAllLessons || isCompleted(lesson.id) || index == 0 || isCompleted(lessons[index - 1].id)
    }

    fun nextAvailableLessonId(hasContent: (String) -> Boolean): String? =
        lessons
            .withIndex()
            .firstOrNull { (index, lesson) ->
                !isCompleted(lesson.id) && isUnlocked(index) && hasContent(lesson.id)
            }?.value
            ?.id
}
