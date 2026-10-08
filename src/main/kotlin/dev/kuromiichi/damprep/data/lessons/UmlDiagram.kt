package dev.kuromiichi.damprep.data.lessons

/**
 * Posición de un nodo en la rejilla del diagrama. Un nodo que abarca varias columnas se centra sobre ellas,
 * p. ej. una clase base encima de sus hijas.
 */
data class UmlCell(
    val row: Int,
    val column: Int,
    val columnSpan: Int = 1,
)

sealed interface UmlNode {
    val id: String
    val cell: UmlCell

    /** Clase, clase abstracta, interfaz o enumeración, con sus compartimentos de atributos y operaciones. */
    data class ClassBox(
        override val id: String,
        override val cell: UmlCell,
        val name: String,
        val stereotype: String? = null,
        val isAbstract: Boolean = false,
        val attributes: List<UmlMember> = emptyList(),
        val operations: List<UmlMember> = emptyList(),
        /** Sin compartimentos la caja muestra solo el nombre, para diagramas que hablan de relaciones. */
        val showsCompartments: Boolean = true,
    ) : UmlNode

    /** Un objeto concreto (`ana: Alumno`) con el valor de sus atributos. */
    data class ObjectBox(
        override val id: String,
        override val cell: UmlCell,
        val name: String,
        val values: List<String> = emptyList(),
    ) : UmlNode
}

enum class UmlVisibility(
    val symbol: Char,
) {
    Public('+'),
    Private('-'),
    Protected('#'),
    Package('~'),
}

data class UmlMember(
    val text: String,
    val visibility: UmlVisibility? = null,
    /** Miembro de clase: UML lo subraya. */
    val isStatic: Boolean = false,
    /** Operación abstracta: UML la escribe en cursiva. */
    val isAbstract: Boolean = false,
) {
    val label: String
        get() = visibility?.let { "${it.symbol} $text" } ?: text
}

enum class UmlRelationKind {
    /** Línea simple, sin flecha. */
    Association,

    /** Asociación que solo se recorre de [UmlRelation.from] a [UmlRelation.to]. */
    NavigableAssociation,

    /** Rombo hueco en [UmlRelation.from], el todo. */
    Aggregation,

    /** Rombo relleno en [UmlRelation.from], el todo. */
    Composition,

    /** Triángulo hueco en [UmlRelation.to], la clase base. */
    Inheritance,

    /** Línea discontinua con triángulo hueco en [UmlRelation.to], la interfaz. */
    Realization,

    /** Línea discontinua con flecha abierta en [UmlRelation.to], la clase usada. */
    Dependency,

    /** Enlace entre dos objetos. */
    Link,
}

data class UmlRelation(
    val from: String,
    val to: String,
    val kind: UmlRelationKind,
    /** Multiplicidad junto a [from]. */
    val fromLabel: String? = null,
    /** Multiplicidad junto a [to]. */
    val toLabel: String? = null,
    /** Rol de [to] en la relación (el nombre de la propiedad), al otro lado de la línea que la multiplicidad. */
    val toRole: String? = null,
    /** Nombre de la relación, en el centro de la línea. */
    val label: String? = null,
)

/**
 * Miembro escrito como en el diagrama: `"- nombre: String"`. El primer carácter, si es `+ - # ~`, es la visibilidad.
 * Como en Mermaid, un `$` final lo marca como miembro de clase y un `*` final como abstracto.
 */
internal fun umlMember(text: String): UmlMember {
    val visibility = UmlVisibility.entries.firstOrNull { text.startsWith("${it.symbol} ") }
    val withoutVisibility = if (visibility == null) text else text.drop(2)
    return UmlMember(
        text = withoutVisibility.removeSuffix("$").removeSuffix("*"),
        visibility = visibility,
        isStatic = withoutVisibility.endsWith("$"),
        isAbstract = withoutVisibility.endsWith("*"),
    )
}

internal fun umlClass(
    id: String,
    cell: UmlCell,
    attributes: List<String> = emptyList(),
    operations: List<String> = emptyList(),
    name: String = id,
    stereotype: String? = null,
    isAbstract: Boolean = false,
): UmlNode.ClassBox =
    UmlNode.ClassBox(
        id = id,
        cell = cell,
        name = name,
        stereotype = stereotype,
        isAbstract = isAbstract,
        attributes = attributes.map { umlMember(it) },
        operations = operations.map { umlMember(it) },
    )

/** Clase que solo muestra su nombre. */
internal fun umlClassName(
    id: String,
    cell: UmlCell,
    stereotype: String? = null,
    isAbstract: Boolean = false,
): UmlNode.ClassBox =
    UmlNode.ClassBox(
        id = id,
        cell = cell,
        name = id,
        stereotype = stereotype,
        isAbstract = isAbstract,
        showsCompartments = false,
    )

internal fun cell(
    row: Int,
    column: Int,
    columnSpan: Int = 1,
): UmlCell = UmlCell(row = row, column = column, columnSpan = columnSpan)

/** Texto que lee un lector de pantalla en lugar del dibujo. */
val LessonVisualContent.UmlDiagram.description: String
    get() {
        val names = nodes.associate { it.id to it.displayName }
        val nodeSentences =
            nodes.map { node ->
                when (node) {
                    is UmlNode.ClassBox -> {
                        val kind =
                            when {
                                node.stereotype != null -> "${node.stereotype} ${node.name}"
                                node.isAbstract -> "clase abstracta ${node.name}"
                                else -> "clase ${node.name}"
                            }
                        val members = (node.attributes + node.operations).joinToString(", ") { it.label }
                        if (members.isEmpty()) kind else "$kind: $members"
                    }

                    is UmlNode.ObjectBox ->
                        if (node.values.isEmpty()) "objeto ${node.name}" else "objeto ${node.name}: ${node.values.joinToString(", ")}"
                }
            }
        val relationSentences =
            relations.map { relation ->
                val from = names.getValue(relation.from)
                val to = names.getValue(relation.to)
                val sentence =
                    when (relation.kind) {
                        UmlRelationKind.Association -> "$from se relaciona con $to"
                        UmlRelationKind.NavigableAssociation -> "$from conoce a $to"
                        UmlRelationKind.Aggregation -> "$from agrega $to"
                        UmlRelationKind.Composition -> "$from se compone de $to"
                        UmlRelationKind.Inheritance -> "$from hereda de $to"
                        UmlRelationKind.Realization -> "$from implementa $to"
                        UmlRelationKind.Dependency -> "$from usa $to"
                        UmlRelationKind.Link -> "$from está enlazado con $to"
                    }
                listOfNotNull(
                    sentence,
                    relation.label?.let { "«$it»" },
                    relation.fromLabel?.let { "junto a $from: $it" },
                    relation.toLabel?.let { "junto a $to: $it" },
                    relation.toRole?.let { "rol de $to: $it" },
                ).joinToString(", ")
            }
        return (listOfNotNull(caption) + nodeSentences + relationSentences).joinToString(". ")
    }

private val UmlNode.displayName: String
    get() =
        when (this) {
            is UmlNode.ClassBox -> name
            is UmlNode.ObjectBox -> name
        }
