package dev.kuromiichi.damprep.ui.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.AutoStories
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.kuromiichi.damprep.ui.theme.CatppuccinLatte
import dev.kuromiichi.damprep.ui.theme.CatppuccinMacchiato
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import kotlin.math.cos
import kotlin.math.sin

data class LearningToolCard(
    val title: String,
    val subtitle: String,
    val logo: DrawableResource,
)

data class LearningCodeExample(
    val title: String,
    val lines: List<LearningCodeLine>,
    val note: String,
)

data class LearningCodeLine(
    val tokens: List<LearningCodeToken>,
)

data class LearningCodeToken(
    val text: String,
    val kind: LearningCodeTokenKind = LearningCodeTokenKind.Plain,
    val hasErrorUnderline: Boolean = false,
)

enum class LearningCodeTokenKind {
    Plain,
    Keyword,
    Function,
    Number,
    Operator,
    Punctuation,
    StringLiteral,
    Interpolation,
    Comment,
}

@Composable
fun LearningVisualSurface(
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit,
) {
    Surface(
        modifier = modifier,
        shape = MaterialTheme.shapes.large,
        color = MaterialTheme.colorScheme.surfaceVariant,
        contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(28.dp),
            contentAlignment = Alignment.Center,
            content = content,
        )
    }
}

@Composable
fun CodeExamplesVisual(
    examples: List<LearningCodeExample>,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
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
fun IconTextLearningVisual(
    title: String,
    body: String,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.widthIn(max = 340.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Surface(
            modifier = Modifier.size(76.dp),
            shape = CircleShape,
            color = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = Icons.Filled.AutoStories,
                    contentDescription = null,
                    modifier = Modifier.size(38.dp),
                )
            }
        }
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center,
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = body,
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
private fun CodeExampleCard(example: LearningCodeExample) {
    val flavor = if (MaterialTheme.colorScheme.background.luminance() > 0.5f) {
        CatppuccinLatte
    } else {
        CatppuccinMacchiato
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = flavor.base,
                shape = RoundedCornerShape(8.dp),
            )
            .padding(horizontal = 18.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Text(
            text = example.title,
            style = MaterialTheme.typography.labelLarge,
            color = flavor.subtext1,
        )
        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
            example.lines.forEachIndexed { index, line ->
                CodeLine(
                    number = index + 1,
                    line = line,
                    colors = CodeColors(
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

@Composable
private fun CodeLine(
    number: Int,
    line: LearningCodeLine,
    colors: CodeColors,
) {
    Row(verticalAlignment = Alignment.Top) {
        Text(
            text = number.toString(),
            style = MaterialTheme.typography.labelMedium.copy(
                fontFamily = FontFamily.Monospace,
                fontSize = 13.sp,
                lineHeight = 20.sp,
            ),
            color = colors.lineNumber,
            textAlign = TextAlign.End,
            modifier = Modifier.width(28.dp),
        )
        Spacer(modifier = Modifier.width(14.dp))
        Row {
            line.tokens.forEach { token ->
                CodeTokenText(
                    token = token,
                    colors = colors,
                )
            }
        }
    }
}

@Composable
private fun CodeTokenText(
    token: LearningCodeToken,
    colors: CodeColors,
) {
    val textStyle = MaterialTheme.typography.bodyMedium.copy(
        fontFamily = FontFamily.Monospace,
        fontSize = 15.sp,
        lineHeight = 20.sp,
    )
    val textMeasurer = rememberTextMeasurer()
    val density = LocalDensity.current
    val underlineWidth = with(density) {
        textMeasurer.measure(
            text = token.text.ifEmpty { " " },
            style = textStyle,
        ).size.width.toDp()
    }

    Column {
        Text(
            text = token.text,
            style = textStyle,
            color = colors.colorFor(token.kind),
        )
        Box(
            modifier = Modifier
                .height(3.dp)
                .width(underlineWidth),
        ) {
            if (token.hasErrorUnderline) {
                Canvas(modifier = Modifier.matchParentSize()) {
                    drawLine(
                        color = colors.error,
                        start = Offset(0f, 1.dp.toPx()),
                        end = Offset(size.width, 1.dp.toPx()),
                        strokeWidth = 1.2.dp.toPx(),
                        cap = StrokeCap.Round,
                    )
                }
            }
        }
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

@Composable
fun FlowDiagram(
    nodes: List<String>,
    modifier: Modifier = Modifier,
) {
    BoxWithConstraints(
        modifier = modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center,
    ) {
        val useVerticalFlow = maxWidth < 520.dp

        if (useVerticalFlow) {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                nodes.forEachIndexed { index, node ->
                    FlowDiagramNode(
                        text = node,
                        modifier = Modifier.widthIn(min = 180.dp, max = 240.dp),
                    )
                    if (index < nodes.lastIndex) {
                        Icon(
                            imageVector = Icons.Filled.ArrowDownward,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(24.dp),
                        )
                    }
                }
            }
        } else {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(14.dp, Alignment.CenterHorizontally),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                nodes.forEachIndexed { index, node ->
                    FlowDiagramNode(
                        text = node,
                        modifier = Modifier.weight(1f),
                    )
                    if (index < nodes.lastIndex) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(28.dp),
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CycleDiagram(
    steps: List<String>,
    modifier: Modifier = Modifier,
) {
    BoxWithConstraints(
        modifier = modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center,
    ) {
        val diagramSize = if (maxWidth < 380.dp) 300.dp else 340.dp
        val nodeRadius = if (maxWidth < 380.dp) 112.dp else 128.dp

        Box(
            modifier = Modifier.size(diagramSize),
            contentAlignment = Alignment.Center,
        ) {
            val ringColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.42f)

            Canvas(modifier = Modifier.matchParentSize()) {
                val radius = size.minDimension * 0.34f
                val center = Offset(size.width / 2f, size.height / 2f)
                drawCircle(
                    color = ringColor,
                    radius = radius,
                    center = center,
                    style = Stroke(
                        width = 3.dp.toPx(),
                        cap = StrokeCap.Round,
                    ),
                )
            }

            steps.forEachIndexed { index, step ->
                val angle = -90.0 + (360.0 / steps.size) * index
                val x = (cos(Math.toRadians(angle)) * nodeRadius.value).dp
                val y = (sin(Math.toRadians(angle)) * nodeRadius.value).dp

                CycleStepNode(
                    index = index + 1,
                    text = step,
                    modifier = Modifier.offset(x = x, y = y),
                )
            }
        }
    }
}

@Composable
fun ToolLogoCards(
    tools: List<LearningToolCard>,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        tools.chunked(2).forEach { rowTools ->
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterHorizontally),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                rowTools.forEach { tool ->
                    ToolLogoCard(tool = tool)
                }
            }
        }
    }
}

@Composable
private fun CycleStepNode(
    index: Int,
    text: String,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier.widthIn(min = 128.dp, max = 140.dp),
        shape = MaterialTheme.shapes.medium,
        color = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.onSurface,
    ) {
        Box(contentAlignment = Alignment.CenterStart) {
            Surface(
                modifier = Modifier
                    .padding(start = 10.dp)
                    .size(20.dp),
                shape = CircleShape,
                color = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = index.toString(),
                        style = MaterialTheme.typography.labelSmall,
                        textAlign = TextAlign.Center,
                    )
                }
            }
            Text(
                text = text,
                style = MaterialTheme.typography.labelLarge,
                textAlign = TextAlign.Center,
                maxLines = 1,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 34.dp, end = 12.dp, top = 10.dp, bottom = 10.dp),
            )
        }
    }
}

@Composable
private fun FlowDiagramNode(
    text: String,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier,
        shape = MaterialTheme.shapes.medium,
        color = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.onSurface,
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 16.dp),
        )
    }
}

@Composable
private fun ToolLogoCard(
    tool: LearningToolCard,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier.widthIn(min = 136.dp, max = 164.dp),
        shape = MaterialTheme.shapes.medium,
        color = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.onSurface,
    ) {
        Column(
            modifier = Modifier.padding(18.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Image(
                painter = painterResource(tool.logo),
                contentDescription = tool.title,
                modifier = Modifier.size(72.dp),
            )
            Spacer(modifier = Modifier.height(14.dp))
            Text(
                text = tool.title,
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center,
            )
            Text(
                text = tool.subtitle,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
            )
        }
    }
}
