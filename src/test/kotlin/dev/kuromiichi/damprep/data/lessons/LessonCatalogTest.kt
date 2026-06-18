package dev.kuromiichi.damprep.data.lessons

import kotlin.test.Test
import kotlin.test.assertEquals

class LessonCatalogTest {
    @Test
    fun `catalog exposes provisional continuous syllabus`() {
        val lessons = LessonCatalog.lessons

        assertEquals(24, lessons.size)
        assertEquals("welcome-roadmap", lessons.first().id)
        assertEquals("applied-project", lessons.last().id)
        assertEquals(
            listOf(
                SubjectArea.Mixed,
                SubjectArea.DevelopmentEnvironments,
                SubjectArea.DevelopmentEnvironments,
                SubjectArea.Programming,
            ),
            lessons.take(4).map { it.subjectArea },
        )
    }

    @Test
    fun `catalog lesson ids are unique`() {
        val lessonIds = LessonCatalog.lessons.map { it.id }

        assertEquals(lessonIds.size, lessonIds.toSet().size)
    }
}
