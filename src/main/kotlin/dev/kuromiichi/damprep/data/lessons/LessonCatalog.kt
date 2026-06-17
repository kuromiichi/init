package dev.kuromiichi.damprep.data.lessons

data class LessonSummary(
    val id: String,
    val title: String,
)

object LessonCatalog {
    val lessons = listOf(
        LessonSummary("intro", "Introducción a la programación"),
        LessonSummary("structured", "Programación estructurada y modular"),
        LessonSummary("storage", "Estructuras de almacenamiento"),
        LessonSummary("oop", "Programación orientada a objetos"),
        LessonSummary("functional", "Programación funcional"),
        LessonSummary("collections", "Genéricos y colecciones"),
        LessonSummary("files", "Ficheros y formatos"),
        LessonSummary("databases", "Bases de datos"),
        LessonSummary("ui", "Interfaces gráficas"),
        LessonSummary("project", "Proyecto aplicado"),
    )
}
