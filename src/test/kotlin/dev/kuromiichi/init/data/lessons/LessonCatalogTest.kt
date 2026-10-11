package dev.kuromiichi.init.data.lessons

import kotlin.test.Test
import kotlin.test.assertEquals

class LessonCatalogTest {
    @Test
    fun `catalog exposes provisional continuous syllabus`() {
        val lessons = LessonCatalog.lessons

        assertEquals(32, lessons.size)
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

    @Test
    fun `catalog preserves the linear learning path`() {
        assertEquals(
            listOf(
                "welcome-roadmap",
                "software-development",
                "development-environment",
                "first-programs",
                "input-decisions",
                "loops",
                "debugging",
                "functions-modularity",
                "terminal-file-system",
                "git-version-control",
                "basic-testing",
                "nullability-exceptions",
                "arrays-lists",
                "sets-maps",
                "oop-classes-objects",
                "oop-relationships",
                "oop-inheritance-interfaces",
                "basic-uml",
                "functional-programming",
                "generics-collections",
                "clean-code-refactoring",
                "solid-patterns-architecture",
                "gradle-dependencies",
                "files-directories",
                "data-formats",
                "relational-data-modeling",
                "sql-basics",
                "sql-queries",
                "database-access",
                "graphical-interfaces",
                "iterative-development",
                "applied-project",
            ),
            LessonCatalog.lessons.map { it.id },
        )
    }

    @Test
    fun `catalog covers the technical supporting areas`() {
        val subjectAreas = LessonCatalog.lessons.mapTo(mutableSetOf(), LessonSummary::subjectArea)

        assertEquals(SubjectArea.entries.toSet(), subjectAreas)
    }
}
