package dev.kuromiichi.init.ui.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.kuromiichi.init.ui.theme.CatppuccinFlavor
import dev.kuromiichi.init.ui.theme.jetBrainsMono

/**
 * Muestra una o varias ejecuciones como aparecerían en la ventana Run de IntelliJ IDEA. Lo que
 * escribe la persona se resalta en verde y los mensajes de error en rojo, igual que en el IDE.
 */
@Composable
fun ConsoleOutputVisual(
    runs: List<LearningConsoleRun>,
    modifier: Modifier = Modifier,
    caption: String? = null,
) {
    val flavor = codeFlavor()

    Column(modifier = modifier.fillMaxWidth().codeBlockSurface(flavor)) {
        EditorTabBar(
            flavor = flavor,
            tabs = listOf(RUN_CONFIGURATION_NAME),
            leading = {
                Spacer(modifier = Modifier.width(14.dp))
                Icon(
                    imageVector = Icons.Filled.PlayArrow,
                    contentDescription = null,
                    tint = flavor.green,
                    modifier = Modifier.size(18.dp),
                )
                Text(
                    text = "Run",
                    style = MaterialTheme.typography.labelLarge,
                    color = flavor.subtext1,
                    modifier = Modifier.padding(start = 4.dp, end = 14.dp),
                )
            },
        )
        ConsoleRuns(
            runs = runs,
            flavor = flavor,
            caption = caption,
            modifier = Modifier.padding(horizontal = 18.dp, vertical = 16.dp),
        )
    }
}

/** Nombre de la configuración de ejecución que muestra la pestaña de la ventana Run (el `main` de `Main.kt`). */
private const val RUN_CONFIGURATION_NAME = "MainKt"

/** Líneas de las ejecuciones con su leyenda y la explicación de los colores, sobre cualquier fondo de consola. */
@Composable
internal fun ConsoleRuns(
    runs: List<LearningConsoleRun>,
    flavor: CatppuccinFlavor,
    modifier: Modifier = Modifier,
    caption: String? = null,
) {
    val hasInput = runs.any { run -> run.lines.any(LearningConsoleLine::isInput) }
    val hasError = runs.any { run -> run.lines.any(LearningConsoleLine::isError) }

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        caption?.let {
            Text(
                text = it,
                style = MaterialTheme.typography.labelLarge,
                color = flavor.subtext1,
            )
        }
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
                                fontFamily = jetBrainsMono(),
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
