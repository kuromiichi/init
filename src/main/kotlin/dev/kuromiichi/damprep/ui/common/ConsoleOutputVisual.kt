package dev.kuromiichi.damprep.ui.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Muestra una o varias ejecuciones como aparecerían en la consola. Lo que escribe la persona
 * se resalta en verde y los mensajes de error en rojo, igual que en la consola de IntelliJ IDEA.
 */
@Composable
fun ConsoleOutputVisual(
    title: String,
    runs: List<LearningConsoleRun>,
    modifier: Modifier = Modifier,
) {
    val flavor = codeFlavor()
    val hasInput = runs.any { run -> run.lines.any(LearningConsoleLine::isInput) }
    val hasError = runs.any { run -> run.lines.any(LearningConsoleLine::isError) }

    Column(
        modifier =
            modifier
                .fillMaxWidth()
                .codeBlockSurface(flavor)
                .padding(horizontal = 18.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.labelLarge,
            color = flavor.subtext1,
        )
        runs.forEachIndexed { index, run ->
            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                if (runs.size > 1) {
                    Text(
                        text = "Ejecución ${index + 1}",
                        style = MaterialTheme.typography.labelMedium,
                        color = flavor.overlay1,
                    )
                }
                run.lines.forEach { line ->
                    Text(
                        text = line.text,
                        style =
                            MaterialTheme.typography.bodyMedium.copy(
                                fontFamily = FontFamily.Monospace,
                                fontSize = 15.sp,
                                lineHeight = 20.sp,
                                fontWeight = if (line.isInput) FontWeight.SemiBold else null,
                            ),
                        color =
                            when {
                                line.isInput -> flavor.green
                                line.isError -> flavor.red
                                else -> flavor.text
                            },
                    )
                }
            }
        }
        if (hasInput) {
            Text(
                text = "En verde, lo que escribes tú.",
                style = MaterialTheme.typography.labelMedium,
                color = flavor.subtext0,
            )
        }
        if (hasError) {
            Text(
                text = "En rojo, los mensajes de error.",
                style = MaterialTheme.typography.labelMedium,
                color = flavor.subtext0,
            )
        }
    }
}
