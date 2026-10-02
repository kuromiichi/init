package dev.kuromiichi.damprep.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
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
    val columnWeights = remember(table) { table.columnWeights() }

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
        TraceRow(cells = table.columns, weights = columnWeights, style = cellStyle.copy(fontWeight = FontWeight.SemiBold), color = flavor.blue)
        HorizontalDivider(color = flavor.surface1)
        table.rows.forEach { row ->
            TraceRow(cells = row, weights = columnWeights, style = cellStyle, color = flavor.text)
        }
    }
}

@Composable
private fun TraceRow(
    cells: List<String>,
    weights: List<Float>,
    style: TextStyle,
    color: Color,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        cells.forEachIndexed { index, cell ->
            Text(text = cell, style = style, color = color, modifier = Modifier.weight(weights[index]))
        }
    }
}

/**
 * Reparte el ancho según el texto más largo de cada columna. Al ser monoespaciado, el número de
 * caracteres se aproxima a su anchura, y el mínimo evita que una columna corta quede ilegible.
 */
internal fun LearningTraceTable.columnWeights(): List<Float> =
    columns.indices.map { index ->
        (rows.map { it[index] } + columns[index])
            .maxOf(String::length)
            .coerceAtLeast(MIN_COLUMN_CHARACTERS)
            .toFloat()
    }

private const val MIN_COLUMN_CHARACTERS = 4
