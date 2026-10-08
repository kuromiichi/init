package dev.kuromiichi.damprep.data.lessons

enum class SubjectArea(
    val label: String,
) {
    Mixed("Mixto"),
    Programming("Programación"),
    DevelopmentEnvironments("Entornos de desarrollo"),
    ComputerSystems("Sistemas informáticos"),
    MarkupLanguages("Lenguajes de marcas"),
    Databases("Bases de datos"),
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
                title = "Repetir acciones y construir algoritmos",
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
                id = "terminal-file-system",
                title = "La terminal y el sistema de ficheros",
                subjectArea = SubjectArea.ComputerSystems,
            ),
            LessonSummary(
                id = "git-version-control",
                title = "Control de versiones con Git",
                subjectArea = SubjectArea.DevelopmentEnvironments,
            ),
            LessonSummary(
                id = "basic-testing",
                title = "Pruebas básicas de software",
                subjectArea = SubjectArea.DevelopmentEnvironments,
            ),
            LessonSummary(
                id = "nullability-exceptions",
                title = "Valores nulos, errores y excepciones",
                subjectArea = SubjectArea.Programming,
            ),
            LessonSummary(
                id = "arrays-lists",
                title = "Estructuras de almacenamiento I: arrays y listas",
                subjectArea = SubjectArea.Programming,
            ),
            LessonSummary(
                id = "sets-maps",
                title = "Estructuras de almacenamiento II: conjuntos y mapas",
                subjectArea = SubjectArea.Programming,
            ),
            LessonSummary(
                id = "oop-classes-objects",
                title = "Programación orientada a objetos I: clases y objetos",
                subjectArea = SubjectArea.Programming,
            ),
            LessonSummary(
                id = "oop-relationships",
                title = "Programación orientada a objetos II: relaciones y composición",
                subjectArea = SubjectArea.Programming,
            ),
            LessonSummary(
                id = "oop-inheritance-interfaces",
                title = "Programación orientada a objetos III: herencia e interfaces",
                subjectArea = SubjectArea.Programming,
            ),
            LessonSummary(
                id = "basic-uml",
                title = "UML I: diagramas de clases",
                subjectArea = SubjectArea.DevelopmentEnvironments,
            ),
            LessonSummary(
                id = "uml-behavior",
                title = "UML II: diagramas de comportamiento",
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
                id = "clean-code-refactoring",
                title = "Código limpio y refactorización",
                subjectArea = SubjectArea.DevelopmentEnvironments,
            ),
            LessonSummary(
                id = "solid-patterns-architecture",
                title = "SOLID, patrones y arquitectura básica",
                subjectArea = SubjectArea.DevelopmentEnvironments,
            ),
            LessonSummary(
                id = "gradle-dependencies",
                title = "Gradle y dependencias",
                subjectArea = SubjectArea.DevelopmentEnvironments,
            ),
            LessonSummary(
                id = "files-directories",
                title = "Leer y escribir ficheros",
                subjectArea = SubjectArea.Programming,
            ),
            LessonSummary(
                id = "data-formats",
                title = "JSON, XML y formatos de intercambio",
                subjectArea = SubjectArea.MarkupLanguages,
            ),
            LessonSummary(
                id = "relational-data-modeling",
                title = "Diseñar datos: modelo E/R y normalización",
                subjectArea = SubjectArea.Databases,
            ),
            LessonSummary(
                id = "sql-basics",
                title = "SQL I: crear tablas y manipular datos",
                subjectArea = SubjectArea.Databases,
            ),
            LessonSummary(
                id = "sql-queries",
                title = "SQL II: consultas con varias tablas",
                subjectArea = SubjectArea.Databases,
            ),
            LessonSummary(
                id = "database-access",
                title = "Usar una base de datos desde Kotlin",
                subjectArea = SubjectArea.Databases,
            ),
            LessonSummary(
                id = "graphical-interfaces",
                title = "Interfaces gráficas",
                subjectArea = SubjectArea.Programming,
            ),
            LessonSummary(
                id = "iterative-development",
                title = "Desarrollo iterativo y organización del proyecto",
                subjectArea = SubjectArea.DevelopmentEnvironments,
            ),
            LessonSummary(
                id = "applied-project",
                title = "Proyecto aplicado",
                subjectArea = SubjectArea.Mixed,
            ),
        )
}
