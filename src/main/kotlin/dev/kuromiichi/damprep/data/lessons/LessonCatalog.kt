package dev.kuromiichi.damprep.data.lessons

enum class SubjectArea(
    val label: String,
) {
    Mixed("Mixto"),
    Programming("Programación"),
    DevelopmentEnvironments("Entornos de desarrollo"),
}

data class LessonSummary(
    val id: String,
    val title: String,
    val subjectArea: SubjectArea,
)

object LessonCatalog {
    val lessons =
        listOf(
            LessonSummary(
                id = "welcome-roadmap",
                title = "Bienvenida y mapa del camino",
                subjectArea = SubjectArea.Mixed,
            ),
            LessonSummary(
                id = "software-development",
                title = "Qué es el software y cómo se construye",
                subjectArea = SubjectArea.DevelopmentEnvironments,
            ),
            LessonSummary(
                id = "development-environment",
                title = "Preparar el entorno de trabajo",
                subjectArea = SubjectArea.DevelopmentEnvironments,
            ),
            LessonSummary(
                id = "first-programs",
                title = "Primeros programas",
                subjectArea = SubjectArea.Programming,
            ),
            LessonSummary(
                id = "input-decisions",
                title = "Leer datos y tomar decisiones",
                subjectArea = SubjectArea.Programming,
            ),
            LessonSummary(
                id = "loops",
                title = "Repetir acciones",
                subjectArea = SubjectArea.Programming,
            ),
            LessonSummary(
                id = "debugging",
                title = "Entender errores y depurar",
                subjectArea = SubjectArea.DevelopmentEnvironments,
            ),
            LessonSummary(
                id = "functions-modularity",
                title = "Funciones y modularidad",
                subjectArea = SubjectArea.Programming,
            ),
            LessonSummary(
                id = "git-version-control",
                title = "Control de versiones con Git",
                subjectArea = SubjectArea.DevelopmentEnvironments,
            ),
            LessonSummary(
                id = "arrays-lists",
                title = "Estructuras de almacenamiento I: arrays y listas",
                subjectArea = SubjectArea.Programming,
            ),
            LessonSummary(
                id = "maps-simple-models",
                title = "Estructuras de almacenamiento II: mapas y modelos simples",
                subjectArea = SubjectArea.Programming,
            ),
            LessonSummary(
                id = "basic-testing",
                title = "Pruebas básicas de software",
                subjectArea = SubjectArea.DevelopmentEnvironments,
            ),
            LessonSummary(
                id = "oop-classes-objects",
                title = "Programación orientada a objetos I: clases y objetos",
                subjectArea = SubjectArea.Programming,
            ),
            LessonSummary(
                id = "basic-uml",
                title = "UML mínimo para pensar clases",
                subjectArea = SubjectArea.DevelopmentEnvironments,
            ),
            LessonSummary(
                id = "oop-relationships",
                title = "Programación orientada a objetos II: relaciones",
                subjectArea = SubjectArea.Programming,
            ),
            LessonSummary(
                id = "clean-code-refactoring",
                title = "Clean code y refactorización",
                subjectArea = SubjectArea.DevelopmentEnvironments,
            ),
            LessonSummary(
                id = "functional-programming",
                title = "Programación funcional aplicada",
                subjectArea = SubjectArea.Programming,
            ),
            LessonSummary(
                id = "generics-collections",
                title = "Genéricos, abstracciones y colecciones avanzadas",
                subjectArea = SubjectArea.Programming,
            ),
            LessonSummary(
                id = "files-formats",
                title = "Ficheros y formatos de intercambio",
                subjectArea = SubjectArea.Programming,
            ),
            LessonSummary(
                id = "databases",
                title = "Bases de datos",
                subjectArea = SubjectArea.Programming,
            ),
            LessonSummary(
                id = "graphical-interfaces",
                title = "Interfaces gráficas",
                subjectArea = SubjectArea.Programming,
            ),
            LessonSummary(
                id = "solid-patterns-architecture",
                title = "SOLID, patrones y arquitectura ligera",
                subjectArea = SubjectArea.DevelopmentEnvironments,
            ),
            LessonSummary(
                id = "agile-methodologies",
                title = "Metodologías ágiles y trabajo por iteraciones",
                subjectArea = SubjectArea.DevelopmentEnvironments,
            ),
            LessonSummary(
                id = "applied-project",
                title = "Proyecto aplicado",
                subjectArea = SubjectArea.Mixed,
            ),
        )
}
