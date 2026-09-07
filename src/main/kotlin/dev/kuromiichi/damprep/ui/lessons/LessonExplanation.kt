package dev.kuromiichi.damprep.ui.lessons

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.OpenInNew
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.kuromiichi.damprep.data.lessons.LessonCalloutTone
import dev.kuromiichi.damprep.data.lessons.LessonExplanationBlock
import dev.kuromiichi.damprep.data.lessons.LessonLink
import dev.kuromiichi.damprep.data.lessons.LessonRichText
import dev.kuromiichi.damprep.data.lessons.LessonTextSpanStyle
import dev.kuromiichi.damprep.ui.common.CodeExamplesVisual
import dev.kuromiichi.damprep.ui.common.LearningCodeExample
import dev.kuromiichi.damprep.ui.common.LearningCodeLine
import dev.kuromiichi.damprep.ui.common.LearningCodeToken
import dev.kuromiichi.damprep.ui.common.hoverScale

@Composable
internal fun LessonExplanation(
    blocks: List<LessonExplanationBlock>,
    links: List<LessonLink>,
    modifier: Modifier = Modifier,
) {
    val uriHandler = LocalUriHandler.current

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(18.dp),
    ) {
        blocks.forEach { block ->
            when (block) {
                is LessonExplanationBlock.Callout -> LessonCallout(block)
                is LessonExplanationBlock.Code -> LessonExplanationCode(block)
                is LessonExplanationBlock.Paragraph -> LessonParagraph(block.text)
                is LessonExplanationBlock.Steps -> LessonSteps(block.items)
            }
        }

        links.forEach { link ->
            TextButton(
                onClick = { uriHandler.openUri(link.url) },
                modifier = Modifier.hoverScale(),
            ) {
                Text(link.label)
                Spacer(modifier = Modifier.size(8.dp))
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.OpenInNew,
                    contentDescription = "Abrir enlace externo",
                    modifier = Modifier.size(18.dp),
                )
            }
        }
    }
}

@Composable
private fun LessonParagraph(text: LessonRichText) {
    Text(
        text = text.toAnnotatedString(),
        style = MaterialTheme.typography.bodyLarge.copy(lineHeight = 28.sp),
        color = MaterialTheme.colorScheme.onSurfaceVariant,
    )
}

@Composable
private fun LessonSteps(items: List<LessonRichText>) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        items.forEachIndexed { index, item ->
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.Top,
            ) {
                Surface(
                    shape = MaterialTheme.shapes.extraLarge,
                    color = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                ) {
                    Box(
                        modifier = Modifier.size(28.dp),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            text = (index + 1).toString(),
                            style = MaterialTheme.typography.labelMedium,
                        )
                    }
                }
                Text(
                    text = item.toAnnotatedString(),
                    style = MaterialTheme.typography.bodyLarge.copy(lineHeight = 26.sp),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.weight(1f),
                )
            }
        }
    }
}

@Composable
private fun LessonCallout(callout: LessonExplanationBlock.Callout) {
    val containerColor =
        when (callout.tone) {
            LessonCalloutTone.Information -> MaterialTheme.colorScheme.primaryContainer
            LessonCalloutTone.Tip -> MaterialTheme.colorScheme.secondaryContainer
            LessonCalloutTone.Warning -> MaterialTheme.colorScheme.errorContainer
        }
    val contentColor =
        when (callout.tone) {
            LessonCalloutTone.Information -> MaterialTheme.colorScheme.onPrimaryContainer
            LessonCalloutTone.Tip -> MaterialTheme.colorScheme.onSecondaryContainer
            LessonCalloutTone.Warning -> MaterialTheme.colorScheme.onErrorContainer
        }
    val icon =
        when (callout.tone) {
            LessonCalloutTone.Information -> Icons.Filled.Info
            LessonCalloutTone.Tip -> Icons.Filled.Lightbulb
            LessonCalloutTone.Warning -> Icons.Filled.Warning
        }

    Card(
        colors =
            CardDefaults.cardColors(
                containerColor = containerColor,
                contentColor = contentColor,
            ),
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.Top,
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(20.dp),
            )
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = callout.title,
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.SemiBold,
                )
                Text(
                    text = callout.text.toAnnotatedString(),
                    style = MaterialTheme.typography.bodyMedium.copy(lineHeight = 22.sp),
                )
            }
        }
    }
}

@Composable
private fun LessonExplanationCode(code: LessonExplanationBlock.Code) {
    CodeExamplesVisual(
        examples =
            listOf(
                LearningCodeExample(
                    title = code.title,
                    lines =
                        code.lines.map { line ->
                            LearningCodeLine(
                                tokens = listOf(LearningCodeToken(text = line)),
                            )
                        },
                    note = code.note,
                ),
            ),
    )
}

@Composable
private fun LessonRichText.toAnnotatedString() =
    buildAnnotatedString {
        spans.forEach { span ->
            val style =
                when (span.style) {
                    LessonTextSpanStyle.Regular -> SpanStyle()
                    LessonTextSpanStyle.Emphasis ->
                        SpanStyle(
                            color = MaterialTheme.colorScheme.onSurface,
                            fontWeight = FontWeight.SemiBold,
                        )
                    LessonTextSpanStyle.Highlight ->
                        SpanStyle(
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.SemiBold,
                        )
                    LessonTextSpanStyle.InlineCode ->
                        SpanStyle(
                            background = MaterialTheme.colorScheme.surfaceVariant,
                            color = MaterialTheme.colorScheme.primary,
                            fontFamily = FontFamily.Monospace,
                        )
                }

            withStyle(style) {
                append(span.text)
            }
        }
    }
