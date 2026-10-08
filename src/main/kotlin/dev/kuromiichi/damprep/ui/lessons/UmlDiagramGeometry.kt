package dev.kuromiichi.damprep.ui.lessons

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import kotlin.math.abs
import kotlin.math.max

/** Un nodo ya medido, con la celda que ocupa en la rejilla del diagrama. */
internal data class UmlGridItem(
    val row: Int,
    val column: Int,
    val columnSpan: Int,
    val size: Size,
)

/**
 * Coloca los nodos en la rejilla: cada columna es tan ancha como su nodo más ancho y cada fila tan alta como el más alto.
 * Un nodo que abarca varias columnas se centra sobre ellas y, si no cabe, las ensancha por igual.
 * Devuelve el rectángulo de cada nodo en el mismo orden que [items].
 */
internal fun umlGridRects(
    items: List<UmlGridItem>,
    columnGap: Float,
    rowGap: Float,
): List<Rect> {
    if (items.isEmpty()) return emptyList()
    val columnCount = items.maxOf { it.column + it.columnSpan }
    val rowCount = items.maxOf { it.row + 1 }
    val columnWidths = FloatArray(columnCount)
    val rowHeights = FloatArray(rowCount)
    items.forEach { item ->
        if (item.columnSpan == 1) columnWidths[item.column] = max(columnWidths[item.column], item.size.width)
        rowHeights[item.row] = max(rowHeights[item.row], item.size.height)
    }
    items.filter { it.columnSpan > 1 }.forEach { item ->
        val columns = item.column until item.column + item.columnSpan
        val available = columns.sumOf { columnWidths[it].toDouble() }.toFloat() + columnGap * (item.columnSpan - 1)
        if (item.size.width > available) {
            val extra = (item.size.width - available) / item.columnSpan
            columns.forEach { columnWidths[it] += extra }
        }
    }
    val columnLefts = columnWidths.runningStarts(columnGap)
    val rowTops = rowHeights.runningStarts(rowGap)

    return items.map { item ->
        val lastColumn = item.column + item.columnSpan - 1
        val spanLeft = columnLefts[item.column]
        val spanRight = columnLefts[lastColumn] + columnWidths[lastColumn]
        val left = spanLeft + (spanRight - spanLeft - item.size.width) / 2f
        val top = rowTops[item.row] + (rowHeights[item.row] - item.size.height) / 2f
        Rect(Offset(left, top), item.size)
    }
}

private fun FloatArray.runningStarts(gap: Float): List<Float> {
    var start = 0f
    return map { length ->
        start.also { start += length + gap }
    }
}

/** Punto del borde de este rectángulo por el que sale la línea que va de su centro hacia [target]. */
internal fun Rect.boundaryPointToward(target: Offset): Offset {
    val direction = target - center
    if (direction == Offset.Zero) return center
    val scale = 1f / max(abs(direction.x) / (width / 2f), abs(direction.y) / (height / 2f))
    return center + direction * scale
}

/** Vector de longitud 1 en la dirección de [this]. */
internal fun Offset.unit(): Offset = if (this == Offset.Zero) Offset.Zero else this / getDistance()

/** Perpendicular a una dirección: a la izquierda según avanza, hacia arriba si la línea va de izquierda a derecha. */
internal fun Offset.normal(): Offset = Offset(y, -x)

/**
 * Centro de una etiqueta junto a un extremo de la línea: se aleja del nodo [along] a lo largo de la línea y se aparta
 * [aside] hacia un lado, contando el tamaño de la etiqueta para que no toque ni la línea ni el nodo.
 * [direction] apunta desde el nodo hacia el otro extremo; con [onLeft] falso la etiqueta va al otro lado de la línea.
 */
internal fun endLabelCenter(
    endpoint: Offset,
    direction: Offset,
    labelSize: Size,
    along: Float,
    aside: Float,
    onLeft: Boolean = true,
): Offset {
    val unit = direction.unit()
    val normal = if (onLeft) unit.normal() else -unit.normal()
    return endpoint + unit * (along + labelSize.extentAlong(unit)) + normal * (aside + labelSize.extentAlong(normal))
}

/** Centro de una etiqueta junto al punto medio de la línea, apartada [aside] hacia un lado. */
internal fun middleLabelCenter(
    start: Offset,
    end: Offset,
    labelSize: Size,
    aside: Float,
): Offset {
    val normal = (end - start).unit().normal()
    return (start + end) / 2f + normal * (aside + labelSize.extentAlong(normal))
}

/** Media anchura de un rectángulo de este tamaño medida en la dirección [unit]. */
private fun Size.extentAlong(unit: Offset): Float = abs(unit.x) * width / 2f + abs(unit.y) * height / 2f

/**
 * Vértices de una punta cuyo extremo está en [tip] y que llega en la dirección [direction]:
 * los dos de la base de un triángulo o una flecha abierta.
 */
internal fun arrowHeadBase(
    tip: Offset,
    direction: Offset,
    length: Float,
    halfWidth: Float,
): Pair<Offset, Offset> {
    val unit = direction.unit()
    val base = tip - unit * length
    val normal = unit.normal() * halfWidth
    return (base + normal) to (base - normal)
}

/** Los cuatro vértices de un rombo apoyado en [tip], en orden: punta, lado, punta opuesta, lado. */
internal fun diamondPoints(
    tip: Offset,
    direction: Offset,
    length: Float,
    halfWidth: Float,
): List<Offset> {
    val unit = direction.unit()
    val middle = tip - unit * (length / 2f)
    val normal = unit.normal() * halfWidth
    return listOf(tip, middle + normal, tip - unit * length, middle - normal)
}
