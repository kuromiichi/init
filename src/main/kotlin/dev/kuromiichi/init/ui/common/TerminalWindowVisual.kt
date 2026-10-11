package dev.kuromiichi.init.ui.common

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import dev.kuromiichi.init.data.preferences.TargetOperatingSystem
import dev.kuromiichi.init.ui.theme.CatppuccinFlavor
import dev.kuromiichi.init.ui.theme.CatppuccinLatteTerminal
import dev.kuromiichi.init.ui.theme.CatppuccinMacchiato
import dev.kuromiichi.init.ui.theme.CatppuccinMocha
import dev.kuromiichi.init.ui.theme.LocalCatppuccinFlavor

/**
 * Salida de una terminal dentro de una ventana con las decoraciones del sistema operativo elegido:
 * semáforo en macOS, botones de Windows 11 a la derecha y botones circulares de GNOME en Linux.
 */
@Composable
fun TerminalWindowVisual(
    runs: List<LearningConsoleRun>,
    operatingSystem: TargetOperatingSystem?,
    modifier: Modifier = Modifier,
    caption: String? = null,
) {
    TerminalWindow(operatingSystem = operatingSystem, modifier = modifier) { flavor ->
        ConsoleRuns(runs = runs, flavor = flavor, caption = caption)
    }
}

/** Comandos que la persona debe escribir en su terminal, con una nota debajo. Las líneas que empiezan por `#` son comentarios. */
@Composable
fun TerminalCommandsVisual(
    commands: List<String>,
    note: AnnotatedString,
    operatingSystem: TargetOperatingSystem?,
    modifier: Modifier = Modifier,
    caption: String? = null,
) {
    TerminalWindow(operatingSystem = operatingSystem, modifier = modifier) { flavor ->
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            ConsoleRuns(
                runs =
                    listOf(
                        LearningConsoleRun(
                            lines = commands.map { command -> LearningConsoleLine(text = command, isInput = !command.startsWith("#")) },
                        ),
                    ),
                flavor = flavor,
                caption = caption,
            )
            Text(
                text = note,
                style = MaterialTheme.typography.labelMedium,
                color = flavor.subtext0,
            )
        }
    }
}

/** Marco de ventana de terminal: barra de título con «Terminal» centrado y fondo oscuro tipo pantalla negra. */
@Composable
internal fun TerminalWindow(
    operatingSystem: TargetOperatingSystem?,
    modifier: Modifier = Modifier,
    content: @Composable (CatppuccinFlavor) -> Unit,
) {
    val flavor = terminalFlavor()
    val shape = RoundedCornerShape(10.dp)

    Column(
        modifier =
            modifier
                .fillMaxWidth()
                .clip(shape)
                .background(flavor.crust)
                .border(1.dp, if (flavor == CatppuccinMocha) flavor.surface0 else flavor.surface1, shape),
    ) {
        TerminalTitleBar(operatingSystem = operatingSystem, flavor = flavor)
        HorizontalDivider(color = if (flavor == CatppuccinMocha) flavor.surface0 else flavor.surface1)
        Box(modifier = Modifier.padding(horizontal = 18.dp, vertical = 16.dp)) {
            content(flavor)
        }
    }
}

/**
 * Paleta de la terminal: Catppuccin Mocha con el tema oscuro y Latte con el claro, siempre con el
 * fondo `crust`, el más oscuro de la paleta, que la distingue del panel y de los bloques de código.
 */
@Composable
internal fun terminalFlavor(): CatppuccinFlavor = if (LocalCatppuccinFlavor.current == CatppuccinMacchiato) CatppuccinMocha else CatppuccinLatteTerminal

@Composable
private fun TerminalTitleBar(
    operatingSystem: TargetOperatingSystem?,
    flavor: CatppuccinFlavor,
) {
    Box(
        modifier = Modifier.fillMaxWidth().height(TitleBarHeight).background(flavor.mantle),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = "Terminal",
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.SemiBold,
            color = flavor.subtext1,
        )
        when (operatingSystem) {
            TargetOperatingSystem.MacOS -> TrafficLights(modifier = Modifier.align(Alignment.CenterStart).padding(start = 14.dp))
            TargetOperatingSystem.Windows -> WindowsCaptionButtons(flavor = flavor, modifier = Modifier.align(Alignment.CenterEnd))
            TargetOperatingSystem.Linux, null -> GnomeWindowButtons(flavor = flavor, modifier = Modifier.align(Alignment.CenterEnd).padding(end = 10.dp))
        }
    }
}

private val TitleBarHeight = 36.dp

/** Semáforo de macOS: cerrar, minimizar y ampliar. */
@Composable
private fun TrafficLights(modifier: Modifier = Modifier) {
    Row(modifier = modifier, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        listOf(Color(0xFFFF5F57), Color(0xFFFEBC2E), Color(0xFF28C840)).forEach { color ->
            Box(modifier = Modifier.size(12.dp).background(color, CircleShape))
        }
    }
}

/** Minimizar, maximizar y cerrar de Windows 11: glifos finos, sin fondo, en toda la altura de la barra. */
@Composable
private fun WindowsCaptionButtons(
    flavor: CatppuccinFlavor,
    modifier: Modifier = Modifier,
) {
    Row(modifier = modifier.height(TitleBarHeight)) {
        listOf(CaptionGlyph.Minimize, CaptionGlyph.Maximize, CaptionGlyph.Close).forEach { glyph ->
            Box(modifier = Modifier.width(44.dp).height(TitleBarHeight), contentAlignment = Alignment.Center) {
                GlyphCanvas(glyph = glyph, color = flavor.text, size = 10.dp)
            }
        }
    }
}

/** Botones redondos de GNOME (libadwaita): un círculo translúcido por acción. */
@Composable
private fun GnomeWindowButtons(
    flavor: CatppuccinFlavor,
    modifier: Modifier = Modifier,
) {
    Row(modifier = modifier, horizontalArrangement = Arrangement.spacedBy(6.dp), verticalAlignment = Alignment.CenterVertically) {
        listOf(CaptionGlyph.Minimize, CaptionGlyph.Maximize, CaptionGlyph.Close).forEach { glyph ->
            Box(
                modifier = Modifier.size(22.dp).background(flavor.text.copy(alpha = 0.1f), CircleShape),
                contentAlignment = Alignment.Center,
            ) {
                GlyphCanvas(glyph = glyph, color = flavor.text, size = 8.dp)
            }
        }
    }
}

private enum class CaptionGlyph { Minimize, Maximize, Close }

@Composable
private fun GlyphCanvas(
    glyph: CaptionGlyph,
    color: Color,
    size: Dp,
) {
    Canvas(modifier = Modifier.size(size)) {
        val stroke = Stroke(width = 1.dp.toPx(), cap = StrokeCap.Round)
        drawGlyph(glyph, color, stroke)
    }
}

private fun DrawScope.drawGlyph(
    glyph: CaptionGlyph,
    color: Color,
    stroke: Stroke,
) {
    when (glyph) {
        CaptionGlyph.Minimize -> drawLine(color, Offset(0f, size.height / 2), Offset(size.width, size.height / 2), stroke.width, stroke.cap)
        CaptionGlyph.Maximize -> drawRect(color, topLeft = Offset.Zero, size = Size(size.width, size.height), style = stroke)
        CaptionGlyph.Close -> {
            drawLine(color, Offset.Zero, Offset(size.width, size.height), stroke.width, stroke.cap)
            drawLine(color, Offset(size.width, 0f), Offset(0f, size.height), stroke.width, stroke.cap)
        }
    }
}
