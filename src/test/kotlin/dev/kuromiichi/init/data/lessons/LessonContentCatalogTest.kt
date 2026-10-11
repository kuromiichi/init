package dev.kuromiichi.init.data.lessons

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

class LessonContentCatalogTest {
    @Test
    fun `lessons with content follow the catalog order without gaps`() {
        val contentIds = LessonContentCatalog.lessons.map(LessonContent::lessonId)

        assertEquals(LessonCatalog.lessons.take(contentIds.size).map(LessonSummary::id), contentIds)
    }

    @Test
    fun `every lesson content belongs to a different catalog lesson`() {
        val catalogIds = LessonCatalog.lessons.mapTo(hashSetOf(), LessonSummary::id)
        val contentIds = LessonContentCatalog.lessons.map(LessonContent::lessonId)

        assertEquals(contentIds.size, contentIds.toSet().size)
        assertTrue(catalogIds.containsAll(contentIds), "Contenido sin lección en el catálogo: ${contentIds - catalogIds}")
    }

    @Test
    fun `lesson content can be found by lesson id`() {
        LessonContentCatalog.lessons.forEach { lesson ->
            assertEquals(lesson, LessonContentCatalog.findByLessonId(lesson.lessonId))
            assertTrue(LessonContentCatalog.hasContent(lesson.lessonId))
        }
        assertNull(LessonContentCatalog.findByLessonId("missing"))
    }

    @Test
    fun `catalog titles are unique and not blank`() {
        val titles = LessonCatalog.lessons.map(LessonSummary::title)

        assertTrue(titles.all(String::isNotBlank))
        assertEquals(titles.size, titles.toSet().size)
    }
}
