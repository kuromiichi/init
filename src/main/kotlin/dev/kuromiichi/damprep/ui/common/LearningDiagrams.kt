package dev.kuromiichi.damprep.ui.common

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.ArrowDownward
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlin.math.cos
import kotlin.math.sin

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
        if (maxWidth < 480.dp) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                steps.forEachIndexed { index, step ->
                    CycleStepNode(
                        index = index + 1,
                        text = step,
                    )
                    if (index < steps.lastIndex) {
                        Icon(
                            imageVector = Icons.Filled.ArrowDownward,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(20.dp),
                        )
                    }
                }
            }
            return@BoxWithConstraints
        }

        val diagramSize = 480.dp
        val nodeRadius = 164.dp

        Box(
            modifier = Modifier.size(diagramSize),
            contentAlignment = Alignment.Center,
        ) {
            val ringColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.42f)

            Canvas(modifier = Modifier.matchParentSize()) {
                val center = Offset(size.width / 2f, size.height / 2f)
                drawCircle(
                    color = ringColor,
                    radius = nodeRadius.toPx(),
                    center = center,
                    style =
                        Stroke(
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
                modifier =
                    Modifier
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
                modifier =
                    Modifier
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
