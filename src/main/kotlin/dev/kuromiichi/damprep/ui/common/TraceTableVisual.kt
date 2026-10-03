package dev.kuromiichi.damprep.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Tabla de traza: cada fila muestra el valor de las variables al terminar una vuelta del bucle.
 * Usa la misma paleta que los bloques de código para leerse como parte del mismo ejemplo.
 */
@Composable
fun TraceTableVisual(
    table: LearningTraceTable,
    modifier: Modifier = Modifier,
) {
    val flavor = codeFlavor()
    val cellStyle =
        MaterialTheme.typography.bodyMedium.copy(
            fontFamily = FontFamily.Monospace,
            fontSize = 15.sp,
            lineHeight = 20.sp,
        )
    val textMeasurer = rememberTextMeasurer()
    val characterWidth =
        remember(cellStyle) {
            textMeasurer
                .measure("0", cellStyle)
                .size.width
                .coerceAtLeast(1)
        }
    val density = LocalDensity.current

    Column(
        modifier =
            modifier
                .widthIn(max = 500.dp)
                .fillMaxWidth()
                .background(color = flavor.base, shape = RoundedCornerShape(8.dp))
                .padding(horizontal = 18.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        Text(
            text = table.title,
            style = MaterialTheme.typography.labelLarge,
            color = flavor.subtext1,
        )
        BoxWithConstraints(modifier = Modifier.fillMaxWidth()) {
            val separatorPx = with(density) { COLUMN_SPACING.roundToPx() }
            val availableCharacters = (constraints.maxWidth - separatorPx * (table.columns.size - 1)) / characterWidth
            val columnWidths =
                remember(table, availableCharacters, characterWidth) {
                    table.columnCharacterWidths(availableCharacters).map { characters -> with(density) { (characters * characterWidth).toDp() } }
                }

            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                TraceRow(cells = table.columns, widths = columnWidths, style = cellStyle.copy(fontWeight = FontWeight.SemiBold), color = flavor.blue)
                HorizontalDivider(color = flavor.surface1)
                table.rows.forEach { row ->
                    TraceRow(cells = row, widths = columnWidths, style = cellStyle, color = flavor.text)
                }
            }
        }
    }
}

@Composable
private fun TraceRow(
    cells: List<String>,
    widths: List<Dp>,
    style: TextStyle,
    color: Color,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(COLUMN_SPACING),
    ) {
        cells.forEachIndexed { index, cell ->
            Text(text = cell, style = style, color = color, modifier = Modifier.width(widths[index]))
        }
    }
}

/**
 * Reparte el ancho, medido en caracteres al ser monoespaciado. Cada columna conserva al menos su
 * palabra más larga, para que un encabezado corto no se parta, y el espacio sobrante se reparte
 * entre las columnas cuyo texto aún no cabe en una sola línea.
 */
internal fun LearningTraceTable.columnCharacterWidths(availableCharacters: Int): List<Int> {
    val texts = columns.indices.map { index -> rows.map { it[index] } + columns[index] }
    val desired = texts.map { column -> column.maxOf(String::length).coerceAtLeast(MIN_COLUMN_CHARACTERS) }
    if (desired.sum() <= availableCharacters) return desired

    val widths = texts.map { column -> column.maxOf { cell -> cell.split(' ').maxOf(String::length) }.coerceAtLeast(MIN_COLUMN_CHARACTERS) }.toMutableList()
    var remaining = availableCharacters - widths.sum()
    while (remaining > 0) {
        val open = widths.indices.filter { widths[it] < desired[it] }
        if (open.isEmpty()) break
        val share = maxOf(1, remaining / open.size)
        open.forEach { index ->
            val added = minOf(share, desired[index] - widths[index], remaining)
            widths[index] += added
            remaining -= added
        }
    }
    return widths
}

private const val MIN_COLUMN_CHARACTERS = 4
private val COLUMN_SPACING = 12.dp
