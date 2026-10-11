package dev.kuromiichi.init.ui.lessons

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.layout.layout
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.kuromiichi.init.data.lessons.LessonVisualContent
import dev.kuromiichi.init.data.lessons.UmlMember
import dev.kuromiichi.init.data.lessons.UmlNode
import dev.kuromiichi.init.data.lessons.UmlRelationKind
import dev.kuromiichi.init.data.lessons.description
import kotlin.math.ceil
import kotlin.math.roundToInt

/**
 * Diagrama UML dibujado con la notación estándar. Si no cabe a lo ancho del panel se reduce entero,
 * para que el alumno vea siempre el diagrama completo.
 */
@Composable
internal fun UmlDiagramVisual(
    diagram: LessonVisualContent.UmlDiagram,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        UmlDiagramLayout(
            diagram = diagram,
            modifier =
                Modifier
                    .clearAndSetSemantics { contentDescription = diagram.description }
                    .scaleDownToFitWidth(),
        )
        diagram.caption?.let { caption ->
            Text(
                text = caption,
                style = MaterialTheme.typography.labelLarge,
                textAlign = TextAlign.Center,
            )
        }
    }
}

private val UmlColumnGap = 64.dp
private val UmlRowGap = 72.dp
private val UmlStrokeWidth = 1.5.dp
private val UmlArrowLength = 14.dp
private val UmlArrowHalfWidth = 7.dp
private val UmlDiamondLength = 20.dp
private val UmlLabelAlong = 6.dp
private val UmlLabelAside = 4.dp

private class RelationLabels(
    val fromLabel: TextLayoutResult?,
    val toLabel: TextLayoutResult?,
    val toRole: TextLayoutResult?,
    val label: TextLayoutResult?,
)

/** Lo que hay que dibujar de una relación, calculado al medir para no repetirlo en cada fotograma. */
private class UmlRelationShape(
    val kind: UmlRelationKind,
    val start: Offset,
    val end: Offset,
    val lineStart: Offset,
    val lineEnd: Offset,
    val labels: List<Pair<TextLayoutResult, Offset>>,
)

@Composable
private fun UmlDiagramLayout(
    diagram: LessonVisualContent.UmlDiagram,
    modifier: Modifier = Modifier,
) {
    val textMeasurer = rememberTextMeasurer()
    val lineColor = MaterialTheme.colorScheme.onSurfaceVariant
    val panelColor = MaterialTheme.colorScheme.surfaceContainerLow
    val labelStyle = MaterialTheme.typography.labelMedium.copy(color = lineColor)

    SubcomposeLayout(modifier = modifier) { _ ->
        val nodePlaceables =
            diagram.nodes.map { node ->
                subcompose(node.id) { UmlNodeBox(node) }.single().measure(Constraints())
            }
        val measuredLabels =
            diagram.relations.map { relation ->
                RelationLabels(
                    fromLabel = relation.fromLabel?.let { textMeasurer.measure(it, labelStyle) },
                    toLabel = relation.toLabel?.let { textMeasurer.measure(it, labelStyle) },
                    toRole = relation.toRole?.let { textMeasurer.measure(it, labelStyle) },
                    label = relation.label?.let { textMeasurer.measure(it, labelStyle) },
                )
            }
        // En una relación horizontal, las etiquetas de los dos extremos tienen que caber entre las cajas.
        val cellsById = diagram.nodes.associate { it.id to it.cell }
        val labelledGap =
            diagram.relations.zip(measuredLabels).maxOfOrNull { (relation, labels) ->
                if (cellsById.getValue(relation.from).row != cellsById.getValue(relation.to).row) return@maxOfOrNull 0f
                val fromWidth = labels.fromLabel?.size?.width ?: 0
                val toWidth = maxOf(labels.toLabel?.size?.width ?: 0, labels.toRole?.size?.width ?: 0)
                fromWidth + toWidth + UmlDiamondLength.toPx() + UmlArrowLength.toPx() + UmlLabelAlong.toPx() * 4
            } ?: 0f
        val nodeRects =
            umlGridRects(
                items =
                    diagram.nodes.zip(nodePlaceables) { node, placeable ->
                        UmlGridItem(
                            row = node.cell.row,
                            column = node.cell.column,
                            columnSpan = node.cell.columnSpan,
                            size = Size(placeable.width.toFloat(), placeable.height.toFloat()),
                        )
                    },
                columnGap = maxOf(UmlColumnGap.toPx(), labelledGap),
                rowGap = UmlRowGap.toPx(),
            )
        val rectsById =
            diagram.nodes
                .map(UmlNode::id)
                .zip(nodeRects)
                .toMap()
        val shapes =
            diagram.relations.zip(measuredLabels) { relation, labels ->
                relationShape(
                    kind = relation.kind,
                    from = rectsById.getValue(relation.from),
                    to = rectsById.getValue(relation.to),
                    fromLabel = labels.fromLabel,
                    toLabel = labels.toLabel,
                    toRole = labels.toRole,
                    label = labels.label,
                )
            }

        // Las etiquetas pueden asomar por fuera de los nodos: el diagrama abarca todo lo que se dibuja.
        val bounds =
            (nodeRects + shapes.flatMap { shape -> shape.labels.map { (text, topLeft) -> Rect(topLeft, text.size.toSize()) } })
                .reduce { a, b -> Rect(minOf(a.left, b.left), minOf(a.top, b.top), maxOf(a.right, b.right), maxOf(a.bottom, b.bottom)) }
        val origin = -bounds.topLeft
        val width = ceil(bounds.width).toInt()
        val height = ceil(bounds.height).toInt()

        val relationsPlaceable =
            subcompose("relations") {
                Canvas(Modifier.fillMaxSize()) {
                    translate(origin.x, origin.y) {
                        shapes.forEach { shape -> drawRelation(shape, lineColor, panelColor) }
                    }
                }
            }.single().measure(Constraints.fixed(width, height))

        layout(width, height) {
            relationsPlaceable.place(0, 0)
            nodePlaceables.zip(nodeRects) { placeable, rect ->
                placeable.place((rect.left + origin.x).roundToInt(), (rect.top + origin.y).roundToInt())
            }
        }
    }
}

private fun IntSize.toSize(): Size = Size(width.toFloat(), height.toFloat())

private fun Density.relationShape(
    kind: UmlRelationKind,
    from: Rect,
    to: Rect,
    fromLabel: TextLayoutResult?,
    toLabel: TextLayoutResult?,
    toRole: TextLayoutResult?,
    label: TextLayoutResult?,
): UmlRelationShape {
    val start = from.boundaryPointToward(to.center)
    val end = to.boundaryPointToward(from.center)
    val direction = (end - start).unit()
    // La línea se detiene en la base de las puntas cerradas para no atravesarlas.
    val lineStart =
        when (kind) {
            UmlRelationKind.Aggregation, UmlRelationKind.Composition -> start + direction * UmlDiamondLength.toPx()
            else -> start
        }
    val lineEnd =
        when (kind) {
            UmlRelationKind.Inheritance, UmlRelationKind.Realization -> end - direction * UmlArrowLength.toPx()
            else -> end
        }
    // Las etiquetas del extremo con flecha abierta empiezan tras la punta, para que no la tapen.
    val toLabelAnchor =
        when (kind) {
            UmlRelationKind.NavigableAssociation, UmlRelationKind.Dependency -> end - direction * UmlArrowLength.toPx()
            else -> lineEnd
        }
    val labels =
        buildList {
            fromLabel?.let { text ->
                val center = endLabelCenter(lineStart, direction, text.size.toSize(), UmlLabelAlong.toPx(), UmlLabelAside.toPx())
                add(text to center.topLeftFor(text))
            }
            toLabel?.let { text ->
                val center = endLabelCenter(toLabelAnchor, -direction, text.size.toSize(), UmlLabelAlong.toPx(), UmlLabelAside.toPx())
                add(text to center.topLeftFor(text))
            }
            toRole?.let { text ->
                val center = endLabelCenter(toLabelAnchor, -direction, text.size.toSize(), UmlLabelAlong.toPx(), UmlLabelAside.toPx(), onLeft = false)
                add(text to center.topLeftFor(text))
            }
            label?.let { text ->
                val center = middleLabelCenter(start, end, text.size.toSize(), UmlLabelAside.toPx())
                add(text to center.topLeftFor(text))
            }
        }
    return UmlRelationShape(kind, start, end, lineStart, lineEnd, labels)
}

private fun Offset.topLeftFor(text: TextLayoutResult): Offset = this - Offset(text.size.width / 2f, text.size.height / 2f)

private fun DrawScope.drawRelation(
    shape: UmlRelationShape,
    lineColor: Color,
    panelColor: Color,
) {
    val strokeWidth = UmlStrokeWidth.toPx()
    val isDashed = shape.kind == UmlRelationKind.Realization || shape.kind == UmlRelationKind.Dependency
    drawLine(
        color = lineColor,
        start = shape.lineStart,
        end = shape.lineEnd,
        strokeWidth = strokeWidth,
        pathEffect = if (isDashed) PathEffect.dashPathEffect(floatArrayOf(7.dp.toPx(), 5.dp.toPx())) else null,
    )
    val direction = shape.end - shape.start
    when (shape.kind) {
        UmlRelationKind.Association, UmlRelationKind.Link -> Unit

        UmlRelationKind.NavigableAssociation, UmlRelationKind.Dependency -> {
            val (left, right) = arrowHeadBase(shape.end, direction, UmlArrowLength.toPx(), UmlArrowHalfWidth.toPx())
            drawLine(lineColor, left, shape.end, strokeWidth)
            drawLine(lineColor, right, shape.end, strokeWidth)
        }

        UmlRelationKind.Inheritance, UmlRelationKind.Realization -> {
            val (left, right) = arrowHeadBase(shape.end, direction, UmlArrowLength.toPx(), UmlArrowHalfWidth.toPx())
            drawClosedHead(listOf(shape.end, left, right), lineColor, fill = panelColor, strokeWidth = strokeWidth)
        }

        UmlRelationKind.Aggregation, UmlRelationKind.Composition -> {
            val points = diamondPoints(shape.start, -direction, UmlDiamondLength.toPx(), UmlArrowHalfWidth.toPx())
            val fill = if (shape.kind == UmlRelationKind.Composition) lineColor else panelColor
            drawClosedHead(points, lineColor, fill = fill, strokeWidth = strokeWidth)
        }
    }
    shape.labels.forEach { (text, topLeft) -> drawText(text, topLeft = topLeft) }
}

private fun DrawScope.drawClosedHead(
    points: List<Offset>,
    lineColor: Color,
    fill: Color,
    strokeWidth: Float,
) {
    val path =
        Path().apply {
            moveTo(points.first().x, points.first().y)
            points.drop(1).forEach { lineTo(it.x, it.y) }
            close()
        }
    drawPath(path, fill)
    drawPath(path, lineColor, style = Stroke(width = strokeWidth))
}

@Composable
private fun UmlNodeBox(node: UmlNode) {
    Surface(
        shape = MaterialTheme.shapes.extraSmall,
        color = MaterialTheme.colorScheme.surfaceContainerHigh,
        contentColor = MaterialTheme.colorScheme.onSurface,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
    ) {
        Column(modifier = Modifier.width(IntrinsicSize.Max).widthIn(min = 112.dp)) {
            when (node) {
                is UmlNode.ClassBox -> {
                    UmlNodeHeader(
                        name = node.name,
                        stereotype = node.stereotype,
                        isItalic = node.isAbstract,
                    )
                    if (node.showsCompartments) {
                        UmlCompartment(node.attributes)
                        UmlCompartment(node.operations)
                    }
                }

                is UmlNode.ObjectBox -> {
                    UmlNodeHeader(name = node.name, isUnderlined = true)
                    UmlCompartment(node.values.map { UmlMember(it) })
                }
            }
        }
    }
}

@Composable
private fun UmlNodeHeader(
    name: String,
    stereotype: String? = null,
    isItalic: Boolean = false,
    isUnderlined: Boolean = false,
) {
    Column(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 14.dp, vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        stereotype?.let {
            Text(
                text = "«$it»",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
        Text(
            text = name,
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.SemiBold,
            fontStyle = if (isItalic) FontStyle.Italic else FontStyle.Normal,
            textDecoration = if (isUnderlined) TextDecoration.Underline else null,
        )
    }
}

private val UmlMemberStyle =
    TextStyle(
        fontFamily = FontFamily.Monospace,
        fontSize = 13.sp,
        lineHeight = 19.sp,
    )

@Composable
private fun UmlCompartment(members: List<UmlMember>) {
    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
    Column(modifier = Modifier.fillMaxWidth().heightIn(min = 10.dp).padding(horizontal = 12.dp, vertical = if (members.isEmpty()) 0.dp else 6.dp)) {
        members.forEach { member ->
            Text(
                text = member.label,
                style = UmlMemberStyle,
                fontStyle = if (member.isAbstract) FontStyle.Italic else FontStyle.Normal,
                textDecoration = if (member.isStatic) TextDecoration.Underline else null,
            )
        }
    }
}

/** Mide el contenido a su tamaño natural y, si es más ancho que el sitio disponible, lo reduce entero. */
private fun Modifier.scaleDownToFitWidth(): Modifier =
    layout { measurable, constraints ->
        val placeable = measurable.measure(Constraints())
        val scale =
            if (constraints.hasBoundedWidth && placeable.width > constraints.maxWidth) {
                constraints.maxWidth / placeable.width.toFloat()
            } else {
                1f
            }
        layout((placeable.width * scale).roundToInt(), (placeable.height * scale).roundToInt()) {
            placeable.placeWithLayer(0, 0) {
                scaleX = scale
                scaleY = scale
                transformOrigin = TransformOrigin(0f, 0f)
            }
        }
    }
