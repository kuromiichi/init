package dev.kuromiichi.damprep.ui.lessons

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material3.Checkbox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.kuromiichi.damprep.data.lessons.LessonPractice
import dev.kuromiichi.damprep.data.lessons.LessonStep
import dev.kuromiichi.damprep.data.lessons.LessonVisualContent
import dev.kuromiichi.damprep.data.preferences.TargetOperatingSystem
import dev.kuromiichi.damprep.ui.common.focusOutline
import dev.kuromiichi.damprep.ui.common.verticalFadingEdges

@Composable
internal fun LessonStepContent(
    step: LessonStep,
    targetOperatingSystem: TargetOperatingSystem?,
    isLastStep: Boolean,
    isPracticeConfirmed: Boolean,
    onPracticeConfirmationChanged: (Boolean) -> Unit,
    viewportHeight: Dp,
    scrollController: LessonStepScrollController,
) {
    val content = step.contentFor(targetOperatingSystem)
    val textScrollState = rememberScrollState()
    val visualScrollState = rememberScrollState()
    DisposableEffect(scrollController, textScrollState) {
        scrollController.register(owner = textScrollState, text = textScrollState, visual = visualScrollState)
        onDispose { scrollController.unregister(textScrollState) }
    }

    BoxWithConstraints(modifier = Modifier.fillMaxWidth()) {
        val useCompactLayout = maxWidth < LessonCompactBreakpoint
        // En el diseño ancho el paso no pasa del alto de la ventana y el texto y el visual se desplazan por separado.
        // Con práctica, el paso se recorre de una vez hasta su tarjeta, sin desplazamientos anidados.
        val limitToViewport = !useCompactLayout && step.practice == null
        SideEffect { scrollController.updateSplit(textScrollState, limitToViewport) }
        val hasRichVisual = content.visual.isRich
        val isScreenshotVisual = content.visual is LessonVisualContent.Screenshot
        val visualMaxWidth =
            when {
                isScreenshotVisual -> LessonScreenshotVisualMaxWidth
                hasRichVisual -> LessonRichVisualMaxWidth
                else -> LessonVisualMaxWidth
            }
        val compactVisualMaxWidth =
            when {
                isScreenshotVisual -> LessonCompactScreenshotVisualMaxWidth
                hasRichVisual -> LessonCompactRichVisualMaxWidth
                else -> LessonCompactVisualMaxWidth
            }

        Column(
            modifier =
                Modifier
                    .fillMaxWidth()
                    // El margen lateral deja sitio al contorno de la zona resaltada, que no puede salirse del paso.
                    .padding(horizontal = LessonSectionOutlineMargin)
                    .then(if (limitToViewport) Modifier.heightIn(max = viewportHeight) else Modifier),
            verticalArrangement = Arrangement.spacedBy(40.dp),
        ) {
            Text(
                text = step.title,
                style = MaterialTheme.typography.displaySmall,
                color = MaterialTheme.colorScheme.onBackground,
            )

            if (useCompactLayout) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(24.dp),
                ) {
                    LessonExplanation(
                        blocks = content.explanation,
                        links = content.links,
                        targetOperatingSystem = targetOperatingSystem,
                    )
                    LessonVisual(
                        visual = content.visual,
                        targetOperatingSystem = targetOperatingSystem,
                        modifier =
                            Modifier
                                .widthIn(max = compactVisualMaxWidth)
                                .fillMaxWidth()
                                .align(Alignment.CenterHorizontally),
                    )
                }
            } else {
                Row(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .then(
                                if (limitToViewport) {
                                    Modifier.weight(1f, fill = false).padding(vertical = LessonSectionOutlineMargin)
                                } else {
                                    Modifier
                                },
                            ),
                    horizontalArrangement = Arrangement.spacedBy(56.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    LessonExplanation(
                        blocks = content.explanation,
                        links = content.links,
                        targetOperatingSystem = targetOperatingSystem,
                        modifier =
                            Modifier
                                .weight(
                                    when {
                                        isScreenshotVisual -> 0.85f
                                        hasRichVisual -> 1f
                                        else -> 1.25f
                                    },
                                ).then(
                                    if (limitToViewport) {
                                        Modifier
                                            .focusOutline(
                                                active = scrollController.focusedSection == LessonScrollSection.Text,
                                                outset = LessonTextOutlineOutset,
                                            ).verticalFadingEdges(
                                                scrollState = textScrollState,
                                                color = MaterialTheme.colorScheme.background,
                                                length = LessonScrollFadeHeight,
                                            ).verticalScroll(textScrollState)
                                    } else {
                                        Modifier
                                    },
                                ),
                    )
                    Box(
                        modifier =
                            Modifier.weight(
                                when {
                                    isScreenshotVisual -> 1.15f
                                    hasRichVisual -> 1f
                                    else -> 0.75f
                                },
                            ),
                        contentAlignment = Alignment.Center,
                    ) {
                        LessonVisual(
                            visual = content.visual,
                            targetOperatingSystem = targetOperatingSystem,
                            modifier =
                                Modifier
                                    .widthIn(max = visualMaxWidth)
                                    .fillMaxWidth(),
                            scrollState = visualScrollState.takeIf { limitToViewport },
                            isOutlined = scrollController.focusedSection == LessonScrollSection.Visual,
                        )
                    }
                }
            }

            step.practice?.let { practice ->
                LessonPracticeCard(
                    practice = practice,
                    targetOperatingSystem = targetOperatingSystem,
                    checked = isPracticeConfirmed,
                    isLessonCompletion = isLastStep,
                    onCheckedChange = onPracticeConfirmationChanged,
                    scrollController = scrollController,
                )
            }
        }
    }
}

/** Margen del paso reservado al contorno de la zona resaltada, que se dibuja hacia fuera de la zona. */
private val LessonSectionOutlineMargin = 12.dp
private val LessonTextOutlineOutset = 10.dp

@Composable
private fun LessonPracticeCard(
    practice: LessonPractice,
    targetOperatingSystem: TargetOperatingSystem?,
    checked: Boolean,
    isLessonCompletion: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    scrollController: LessonStepScrollController,
) {
    var revealedHintCount by remember(practice.id) { mutableIntStateOf(0) }
    val currentHintCount by rememberUpdatedState(revealedHintCount)
    val hintOwner = remember(practice.id) { Any() }
    // La tecla P pide la siguiente pista igual que el botón «Otra pista», pero sin volver a ocultarlas al llegar a la última.
    DisposableEffect(scrollController, hintOwner) {
        scrollController.registerHintAction(hintOwner) {
            val hasMore = currentHintCount < practice.hints.size
            if (hasMore) revealedHintCount = currentHintCount + 1
            hasMore
        }
        onDispose { scrollController.unregisterHintAction(hintOwner) }
    }

    OutlinedCard(
        modifier = Modifier.fillMaxWidth(),
    ) {
        Column(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp),
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Surface(
                    shape = MaterialTheme.shapes.medium,
                    color = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                ) {
                    Icon(
                        imageVector = Icons.Filled.EditNote,
                        contentDescription = null,
                        modifier = Modifier.padding(10.dp).size(22.dp),
                    )
                }
                Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                    Text(
                        text = "Práctica guiada",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                    )
                    Text(
                        text = "Comprueba lo aprendido antes de terminar.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }

            Text(
                text = practice.objective.toAnnotatedString(),
                style = MaterialTheme.typography.bodyLarge.copy(lineHeight = 26.sp),
                color = MaterialTheme.colorScheme.onSurface,
            )

            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)

            Text(
                text = practice.statement.toAnnotatedString(),
                style = MaterialTheme.typography.bodyMedium.copy(lineHeight = 22.sp),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )

            if (practice.requirements.isNotEmpty()) {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    PracticeSectionTitle("Qué debes comprobar")
                    practice.requirements.forEach { requirement ->
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalAlignment = Alignment.Top,
                        ) {
                            Box(
                                modifier =
                                    Modifier
                                        .padding(start = 6.dp, top = 8.dp)
                                        .size(6.dp)
                                        .background(MaterialTheme.colorScheme.primary, CircleShape),
                            )
                            Text(
                                text = requirement.toAnnotatedString(),
                                style = MaterialTheme.typography.bodyMedium.copy(lineHeight = 22.sp),
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.weight(1f),
                            )
                        }
                    }
                }
            }

            if (practice.expectedOutput.isNotEmpty()) {
                ConsoleVisual(
                    kind = practice.expectedOutputKind,
                    runs = practice.expectedOutput.map { it.toLearningConsoleRun() },
                    targetOperatingSystem = targetOperatingSystem,
                    caption = "Resultado esperado",
                )
            }

            if (practice.hints.isNotEmpty()) {
                val allHintsRevealed = revealedHintCount == practice.hints.size

                Column(
                    modifier = Modifier.animateContentSize(),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    practice.hints.take(revealedHintCount).forEachIndexed { index, hint ->
                        Column(
                            modifier = Modifier.padding(horizontal = 12.dp),
                            verticalArrangement = Arrangement.spacedBy(2.dp),
                        ) {
                            Text(
                                text = "Pista ${index + 1} de ${practice.hints.size}",
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.primary,
                            )
                            Text(
                                text = hint.toAnnotatedString(),
                                style = MaterialTheme.typography.bodyMedium.copy(lineHeight = 22.sp),
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                        }
                    }
                    TextButton(
                        onClick = { revealedHintCount = if (allHintsRevealed) 0 else revealedHintCount + 1 },
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Lightbulb,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp),
                        )
                        Spacer(modifier = Modifier.size(8.dp))
                        Text(
                            when {
                                allHintsRevealed -> "Ocultar pistas"
                                revealedHintCount == 0 -> "Necesito una pista"
                                else -> "Otra pista"
                            },
                        )
                    }
                }
            }

            practice.extension?.let { extension ->
                Surface(
                    shape = MaterialTheme.shapes.medium,
                    color = MaterialTheme.colorScheme.secondaryContainer,
                    contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth().padding(14.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp),
                    ) {
                        Text(
                            text = "Si quieres ir un paso más allá",
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.SemiBold,
                        )
                        Text(
                            text = extension.toAnnotatedString(tintedContentColor = LocalContentColor.current),
                            style = MaterialTheme.typography.bodyMedium.copy(lineHeight = 22.sp),
                        )
                    }
                }
            }

            Surface(
                shape = MaterialTheme.shapes.medium,
                color =
                    if (checked) {
                        MaterialTheme.colorScheme.secondaryContainer
                    } else {
                        MaterialTheme.colorScheme.surfaceVariant
                    },
                contentColor =
                    if (checked) {
                        MaterialTheme.colorScheme.onSecondaryContainer
                    } else {
                        MaterialTheme.colorScheme.onSurfaceVariant
                    },
            ) {
                Row(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .toggleable(
                                value = checked,
                                role = Role.Checkbox,
                                onValueChange = onCheckedChange,
                            ).padding(14.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Checkbox(
                        checked = checked,
                        onCheckedChange = null,
                    )
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(
                            text =
                                if (isLessonCompletion) {
                                    "Listo para terminar"
                                } else {
                                    "Listo para continuar"
                                },
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.SemiBold,
                        )
                        Text(
                            text = practice.confirmation.toAnnotatedString(tintedContentColor = LocalContentColor.current),
                            style = MaterialTheme.typography.bodyMedium.copy(lineHeight = 22.sp),
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun PracticeSectionTitle(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.labelLarge,
        fontWeight = FontWeight.SemiBold,
    )
}
