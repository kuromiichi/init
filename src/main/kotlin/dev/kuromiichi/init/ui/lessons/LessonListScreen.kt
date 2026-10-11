package dev.kuromiichi.init.ui.lessons

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PlainTooltip
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TooltipAnchorPosition
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import dev.kuromiichi.init.ui.common.AppScreen
import dev.kuromiichi.init.ui.common.AppScreenContentMaxWidth
import dev.kuromiichi.init.ui.common.focusHighlight
import dev.kuromiichi.init.ui.common.verticalFadingEdges
import kotlinx.coroutines.launch

@Composable
fun LessonListScreen(
    uiState: LessonListUiState,
    onBack: () -> Unit,
    onLessonSelected: (String) -> Unit,
) {
    val initialFocusRequester = remember { FocusRequester() }
    // El foco empieza en la lección en curso: la primera sin completar o, si no queda ninguna, la primera abierta.
    val initialLessonId =
        (
            uiState.lessons.firstOrNull { it.canOpen && !it.isCompleted }
                ?: uiState.lessons.firstOrNull { it.canOpen }
        )?.id
    LaunchedEffect(initialLessonId) {
        if (initialLessonId != null) runCatching { initialFocusRequester.requestFocus() }
    }

    AppScreen(
        onBack = onBack,
        arrowKeyNavigation = true,
        horizontalArrowKeys = false,
    ) {
        val scrollState = rememberScrollState()
        Box(
            modifier =
                Modifier
                    .weight(1f)
                    .widthIn(max = AppScreenContentMaxWidth)
                    .fillMaxWidth()
                    .verticalFadingEdges(
                        scrollState = scrollState,
                        color = MaterialTheme.colorScheme.background,
                        length = 32.dp,
                    ),
        ) {
            // El margen lateral deja sitio al contorno de la lección enfocada, que si no se cortaría en el borde del desplazamiento.
            Column(modifier = Modifier.fillMaxWidth().verticalScroll(scrollState).padding(horizontal = 4.dp)) {
                Spacer(modifier = Modifier.height(48.dp))
                Text(
                    text = "Todas las lecciones",
                    style = MaterialTheme.typography.headlineLarge,
                    color = MaterialTheme.colorScheme.onBackground,
                )
                Spacer(modifier = Modifier.height(24.dp))
                uiState.lessons.forEach { lesson ->
                    LessonListItem(
                        lesson = lesson,
                        onClick = { onLessonSelected(lesson.id) },
                        focusRequester = initialFocusRequester.takeIf { lesson.id == initialLessonId },
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun LessonListItem(
    lesson: LessonListItemUiModel,
    onClick: () -> Unit,
    focusRequester: FocusRequester? = null,
) {
    val canOpen = lesson.canOpen
    val contentColor =
        if (canOpen) {
            MaterialTheme.colorScheme.onSurface
        } else {
            MaterialTheme.colorScheme.onSurfaceVariant
        }
    val containerColor =
        if (canOpen) {
            MaterialTheme.colorScheme.surfaceVariant
        } else {
            MaterialTheme.colorScheme.surface
        }

    val itemContent: @Composable () -> Unit = {
        Row(
            modifier = Modifier.padding(18.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                Text(
                    text = "Lección ${lesson.index + 1}",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Text(
                    text = lesson.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = contentColor,
                )
            }
            SubjectAreaTag(subjectArea = lesson.subjectArea)
            Icon(
                imageVector =
                    when {
                        lesson.isCompleted -> Icons.Filled.CheckCircle
                        canOpen -> Icons.Filled.PlayArrow
                        lesson.isUnlocked -> Icons.Filled.Schedule
                        else -> Icons.Filled.Lock
                    },
                contentDescription =
                    when {
                        lesson.isCompleted -> "Lección completada"
                        canOpen -> "Abrir lección"
                        lesson.isUnlocked -> "Contenido próximamente"
                        else -> "Lección bloqueada"
                    },
                tint =
                    if (lesson.isCompleted || canOpen) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        MaterialTheme.colorScheme.outline
                    },
            )
        }
    }

    if (canOpen) {
        val interactionSource = remember { MutableInteractionSource() }
        val isHovered by interactionSource.collectIsHoveredAsState()
        val scale by animateFloatAsState(
            targetValue = if (isHovered) 1.006f else 1f,
            animationSpec =
                tween(
                    durationMillis = 140,
                    easing = FastOutSlowInEasing,
                ),
            label = "Lesson item hover scale",
        )
        val tonalElevation by animateDpAsState(
            targetValue = if (isHovered) 3.dp else 0.dp,
            animationSpec =
                tween(
                    durationMillis = 140,
                    easing = FastOutSlowInEasing,
                ),
            label = "Lesson item hover elevation",
        )

        Surface(
            onClick = onClick,
            modifier =
                Modifier
                    .fillMaxWidth()
                    .then(if (focusRequester != null) Modifier.focusRequester(focusRequester) else Modifier)
                    .lessonItemKeyboardFocus()
                    .graphicsLayer {
                        scaleX = scale
                        scaleY = scale
                    },
            shape = MaterialTheme.shapes.medium,
            color = containerColor,
            contentColor = contentColor,
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
            tonalElevation = tonalElevation,
            interactionSource = interactionSource,
        ) {
            itemContent()
        }
    } else {
        val tooltipState = rememberTooltipState()
        val coroutineScope = rememberCoroutineScope()

        TooltipBox(
            positionProvider = TooltipDefaults.rememberTooltipPositionProvider(TooltipAnchorPosition.Above),
            tooltip = { PlainTooltip { Text(lesson.unavailableReason.orEmpty()) } },
            state = tooltipState,
        ) {
            // También se llega con el teclado: Intro muestra por qué no se puede abrir.
            Surface(
                onClick = { coroutineScope.launch { tooltipState.show() } },
                modifier = Modifier.fillMaxWidth().lessonItemKeyboardFocus(),
                shape = MaterialTheme.shapes.medium,
                color = containerColor,
                contentColor = contentColor,
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
            ) {
                itemContent()
            }
        }
    }
}

/** Con el teclado, el elemento enfocado se lleva a la vista con su contorno incluido y muestra el resaltado. */
@Composable
private fun Modifier.lessonItemKeyboardFocus(): Modifier {
    val bringIntoViewRequester = remember { BringIntoViewRequester() }
    val coroutineScope = rememberCoroutineScope()
    return bringIntoViewRequester(bringIntoViewRequester)
        .onFocusChanged { if (it.hasFocus) coroutineScope.launch { bringIntoViewRequester.bringIntoView() } }
        .focusHighlight(shape = MaterialTheme.shapes.medium, outset = 3.dp)
}
