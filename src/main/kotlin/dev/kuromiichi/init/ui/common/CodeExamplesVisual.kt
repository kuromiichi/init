package dev.kuromiichi.init.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ClipEntry
import androidx.compose.ui.platform.LocalClipboard
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.kuromiichi.init.ui.theme.CatppuccinFlavor
import dev.kuromiichi.init.ui.theme.LocalCatppuccinFlavor
import dev.kuromiichi.init.ui.theme.jetBrainsMono
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.awt.datatransfer.StringSelection
import kotlin.time.Duration.Companion.seconds

@Composable
fun CodeExamplesVisual(
    examples: List<LearningCodeExample>,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier =
            modifier
                .widthIn(max = 500.dp)
                .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        examples.forEach { example ->
            CodeExampleCard(example = example)
        }
    }
}

@Composable
private fun CodeExampleCard(example: LearningCodeExample) {
    val flavor = codeFlavor()

    Column(modifier = Modifier.fillMaxWidth().codeBlockSurface(flavor)) {
        EditorTabBar(
            flavor = flavor,
            tabs = listOf(example.fileName),
            trailing = {
                example.copyText?.let { text ->
                    CopyCodeButton(text = text, color = flavor.subtext1)
                }
            },
        )
        Column(
            modifier = Modifier.padding(horizontal = 18.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            example.title?.let { title ->
                Text(
                    text = title,
                    style = MaterialTheme.typography.labelLarge,
                    color = flavor.subtext1,
                )
            }
            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                example.lines.forEachIndexed { index, line ->
                    CodeLine(
                        number = index + 1,
                        line = line,
                        flavor = flavor,
                    )
                }
            }
            Text(
                text = example.note,
                style = MaterialTheme.typography.labelMedium,
                color = flavor.subtext0,
            )
        }
    }
}

/**
 * Barra de pestañas del editor de IntelliJ IDEA (nueva interfaz): la pestaña activa lleva el fondo del
 * editor y un subrayado de acento. [leading] y [trailing] añaden contenido antes y después de las pestañas.
 */
@Composable
internal fun EditorTabBar(
    flavor: CatppuccinFlavor,
    tabs: List<String>,
    modifier: Modifier = Modifier,
    leading: @Composable RowScope.() -> Unit = {},
    trailing: @Composable RowScope.() -> Unit = {},
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth().background(flavor.mantle).height(EditorTabBarHeight),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            leading()
            tabs.forEach { tab -> EditorTab(label = tab, flavor = flavor, modifier = Modifier.widthIn(max = EditorTabMaxWidth)) }
            Spacer(modifier = Modifier.weight(1f))
            trailing()
        }
        HorizontalDivider(color = flavor.surface1)
    }
}

@Composable
private fun EditorTab(
    label: String,
    flavor: CatppuccinFlavor,
    modifier: Modifier = Modifier,
) {
    val indicatorColor = flavor.blue
    Box(
        modifier =
            modifier
                .fillMaxHeight()
                .background(flavor.base)
                .drawBehind {
                    val thickness = 2.dp.toPx()
                    drawRect(indicatorColor, topLeft = Offset(0f, size.height - thickness), size = Size(size.width, thickness))
                },
        contentAlignment = Alignment.CenterStart,
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelLarge,
            color = flavor.text,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(horizontal = 16.dp),
        )
    }
}

private val EditorTabBarHeight = 40.dp
private val EditorTabMaxWidth = 260.dp

/** Copia el código al portapapeles y confirma la copia durante unos segundos. */
@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun CopyCodeButton(
    text: String,
    color: Color,
) {
    val clipboard = LocalClipboard.current
    val scope = rememberCoroutineScope()
    var isCopied by remember { mutableStateOf(false) }

    LaunchedEffect(isCopied) {
        if (isCopied) {
            delay(2.seconds)
            isCopied = false
        }
    }

    TextButton(
        onClick = {
            scope.launch {
                clipboard.setClipEntry(ClipEntry(StringSelection(text)))
                isCopied = true
            }
        },
        colors = ButtonDefaults.textButtonColors(contentColor = color),
        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
    ) {
        Icon(
            imageVector = if (isCopied) Icons.Filled.Check else Icons.Filled.ContentCopy,
            contentDescription = null,
            modifier = Modifier.size(16.dp),
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            text = if (isCopied) "Copiado" else "Copiar código",
            style = MaterialTheme.typography.labelMedium,
        )
    }
}

/**
 * Fondo de los bloques de código, consola y tablas: el del editor de IntelliJ IDEA con un borde
 * fino, para que se distingan tanto sobre la página como sobre el panel de un visual. Recorta el
 * contenido a las esquinas redondeadas para que las barras de cabecera no sobresalgan.
 */
internal fun Modifier.codeBlockSurface(flavor: CatppuccinFlavor): Modifier {
    val shape = RoundedCornerShape(8.dp)
    return clip(shape).background(color = flavor.base, shape = shape).border(1.dp, flavor.surface1, shape)
}

/** Paleta de los bloques de código y consola, acorde con el tema claro u oscuro activo. */
@Composable
internal fun codeFlavor(): CatppuccinFlavor = LocalCatppuccinFlavor.current

@Composable
private fun CodeLine(
    number: Int,
    line: LearningCodeLine,
    flavor: CatppuccinFlavor,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top,
    ) {
        Text(
            text = number.toString(),
            style =
                MaterialTheme.typography.labelMedium.copy(
                    fontFamily = jetBrainsMono(),
                    fontSize = 13.sp,
                    lineHeight = 20.sp,
                ),
            color = flavor.overlay1,
            textAlign = TextAlign.End,
            modifier = Modifier.width(28.dp),
        )
        Spacer(modifier = Modifier.width(14.dp))
        Text(
            text =
                buildAnnotatedString {
                    line.tokens.forEach { token ->
                        withStyle(
                            token.kind.spanStyle(flavor).let { style ->
                                if (token.hasErrorUnderline) {
                                    style.copy(color = flavor.red, textDecoration = TextDecoration.Underline)
                                } else {
                                    style
                                }
                            },
                        ) {
                            append(token.text)
                        }
                    }
                },
            style =
                MaterialTheme.typography.bodyMedium.copy(
                    fontFamily = jetBrainsMono(),
                    fontSize = 15.sp,
                    lineHeight = 20.sp,
                ),
            softWrap = true,
            overflow = TextOverflow.Clip,
            modifier = Modifier.weight(1f),
        )
    }
}

/**
 * Colores y cursivas de cada tipo de token, los mismos que aplican los temas Catppuccin de
 * IntelliJ IDEA al código Kotlin (incluidos los corchetes de colores por nivel de anidamiento).
 */
private fun LearningCodeTokenKind.spanStyle(flavor: CatppuccinFlavor): SpanStyle =
    when (this) {
        LearningCodeTokenKind.Plain -> SpanStyle(color = flavor.text)
        LearningCodeTokenKind.Keyword -> SpanStyle(color = flavor.mauve)
        LearningCodeTokenKind.Function -> SpanStyle(color = flavor.blue, fontStyle = FontStyle.Italic)
        LearningCodeTokenKind.Number -> SpanStyle(color = flavor.peach)
        LearningCodeTokenKind.Operator -> SpanStyle(color = flavor.sky)
        LearningCodeTokenKind.Punctuation -> SpanStyle(color = flavor.overlay2)
        LearningCodeTokenKind.StringLiteral -> SpanStyle(color = flavor.green)
        LearningCodeTokenKind.Interpolation -> SpanStyle(color = flavor.pink)
        LearningCodeTokenKind.Comment -> SpanStyle(color = flavor.overlay2, fontStyle = FontStyle.Italic)
        LearningCodeTokenKind.ClassName -> SpanStyle(color = flavor.yellow, fontStyle = FontStyle.Italic)
        LearningCodeTokenKind.Parameter -> SpanStyle(color = flavor.maroon, fontStyle = FontStyle.Italic)
        LearningCodeTokenKind.ImplicitParameter -> SpanStyle(color = flavor.maroon)
        LearningCodeTokenKind.Constant -> SpanStyle(color = flavor.peach)
        LearningCodeTokenKind.MutableVariable -> SpanStyle(color = flavor.text, fontStyle = FontStyle.Italic)
        LearningCodeTokenKind.ExtensionProperty -> SpanStyle(color = flavor.teal)
        LearningCodeTokenKind.LambdaDelimiter, LearningCodeTokenKind.Bracket0 -> SpanStyle(color = flavor.red)
        LearningCodeTokenKind.Bracket1 -> SpanStyle(color = flavor.peach)
        LearningCodeTokenKind.Bracket2 -> SpanStyle(color = flavor.yellow)
        LearningCodeTokenKind.Bracket3 -> SpanStyle(color = flavor.green)
        LearningCodeTokenKind.Bracket4 -> SpanStyle(color = flavor.sapphire)
    }
