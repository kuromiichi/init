package dev.kuromiichi.damprep.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.platform.ClipEntry
import androidx.compose.ui.platform.LocalClipboard
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.kuromiichi.damprep.ui.theme.CatppuccinFlavor
import dev.kuromiichi.damprep.ui.theme.CatppuccinLatte
import dev.kuromiichi.damprep.ui.theme.CatppuccinMacchiato
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

    Column(
        modifier =
            Modifier
                .fillMaxWidth()
                .background(
                    color = flavor.base,
                    shape = RoundedCornerShape(8.dp),
                ).padding(horizontal = 18.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = example.title,
                style = MaterialTheme.typography.labelLarge,
                color = flavor.subtext1,
                modifier = Modifier.weight(1f),
            )
            example.copyText?.let { text ->
                CopyCodeButton(text = text, color = flavor.subtext1)
            }
        }
        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
            example.lines.forEachIndexed { index, line ->
                CodeLine(
                    number = index + 1,
                    line = line,
                    colors =
                        CodeColors(
                            default = flavor.text,
                            lineNumber = flavor.overlay1,
                            keyword = flavor.mauve,
                            function = flavor.blue,
                            number = flavor.peach,
                            operator = flavor.sky,
                            punctuation = flavor.peach,
                            stringLiteral = flavor.green,
                            interpolation = flavor.pink,
                            comment = flavor.overlay1,
                            error = flavor.red,
                        ),
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

/** Paleta de los bloques de código y consola, acorde con el tema claro u oscuro activo. */
@Composable
internal fun codeFlavor(): CatppuccinFlavor =
    if (MaterialTheme.colorScheme.background.luminance() > 0.5f) {
        CatppuccinLatte
    } else {
        CatppuccinMacchiato
    }

@Composable
private fun CodeLine(
    number: Int,
    line: LearningCodeLine,
    colors: CodeColors,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top,
    ) {
        Text(
            text = number.toString(),
            style =
                MaterialTheme.typography.labelMedium.copy(
                    fontFamily = FontFamily.Monospace,
                    fontSize = 13.sp,
                    lineHeight = 20.sp,
                ),
            color = colors.lineNumber,
            textAlign = TextAlign.End,
            modifier = Modifier.width(28.dp),
        )
        Spacer(modifier = Modifier.width(14.dp))
        Text(
            text =
                buildAnnotatedString {
                    line.tokens.forEach { token ->
                        withStyle(
                            SpanStyle(
                                color = if (token.hasErrorUnderline) colors.error else colors.colorFor(token.kind),
                                textDecoration = if (token.hasErrorUnderline) TextDecoration.Underline else null,
                            ),
                        ) {
                            append(token.text)
                        }
                    }
                },
            style =
                MaterialTheme.typography.bodyMedium.copy(
                    fontFamily = FontFamily.Monospace,
                    fontSize = 15.sp,
                    lineHeight = 20.sp,
                ),
            softWrap = true,
            overflow = TextOverflow.Clip,
            modifier = Modifier.weight(1f),
        )
    }
}

private data class CodeColors(
    val default: androidx.compose.ui.graphics.Color,
    val lineNumber: androidx.compose.ui.graphics.Color,
    val keyword: androidx.compose.ui.graphics.Color,
    val function: androidx.compose.ui.graphics.Color,
    val number: androidx.compose.ui.graphics.Color,
    val operator: androidx.compose.ui.graphics.Color,
    val punctuation: androidx.compose.ui.graphics.Color,
    val stringLiteral: androidx.compose.ui.graphics.Color,
    val interpolation: androidx.compose.ui.graphics.Color,
    val comment: androidx.compose.ui.graphics.Color,
    val error: androidx.compose.ui.graphics.Color,
) {
    fun colorFor(kind: LearningCodeTokenKind): androidx.compose.ui.graphics.Color =
        when (kind) {
            LearningCodeTokenKind.Plain -> default
            LearningCodeTokenKind.Keyword -> keyword
            LearningCodeTokenKind.Function -> function
            LearningCodeTokenKind.Number -> number
            LearningCodeTokenKind.Operator -> operator
            LearningCodeTokenKind.Punctuation -> punctuation
            LearningCodeTokenKind.StringLiteral -> stringLiteral
            LearningCodeTokenKind.Interpolation -> interpolation
            LearningCodeTokenKind.Comment -> comment
        }
}
