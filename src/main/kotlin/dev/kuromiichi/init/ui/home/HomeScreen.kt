package dev.kuromiichi.init.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.SystemUpdateAlt
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.LocalMinimumInteractiveComponentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.PlainTooltip
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TooltipAnchorPosition
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import dev.kuromiichi.init.ui.common.AppTopBar
import dev.kuromiichi.init.ui.common.InitLogo
import dev.kuromiichi.init.ui.common.arrowKeyNavigation
import dev.kuromiichi.init.ui.common.focusHighlight
import dev.kuromiichi.init.ui.common.hoverScale

private val HomeContentMaxWidth = 720.dp
private val HomeControlsMaxWidth = 640.dp
private val HomeProgressMaxWidth = 560.dp
private val HomeSettingsMaxWidth = 320.dp
private val HomeCompactBreakpoint = 560.dp

@Composable
fun HomeScreen(
    uiState: HomeUiState,
    onContinueLesson: () -> Unit,
    onShowLessons: () -> Unit,
    onShowNotes: () -> Unit,
    onShowSettings: () -> Unit,
    availableUpdateVersion: String? = null,
    onShowUpdate: () -> Unit = {},
) {
    val initialFocusRequester = remember { FocusRequester() }
    LaunchedEffect(Unit) {
        // El foco empieza en el botón principal, que Intro pulsa y las flechas dejan atrás.
        runCatching { initialFocusRequester.requestFocus() }
    }

    Surface(
        modifier =
            Modifier
                .fillMaxSize()
                .arrowKeyNavigation(),
        color = MaterialTheme.colorScheme.background,
    ) {
        // Los botones miden lo que se ve: sin el tamaño táctil mínimo su contorno de foco no queda holgado por arriba y por abajo.
        CompositionLocalProvider(LocalMinimumInteractiveComponentSize provides Dp.Unspecified) {
            Box(modifier = Modifier.fillMaxSize()) {
                Column(
                    modifier =
                        Modifier
                            .fillMaxSize()
                            .padding(horizontal = 40.dp, vertical = 32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Spacer(modifier = Modifier.weight(1f))

                    Column(
                        modifier =
                            Modifier
                                .widthIn(max = HomeContentMaxWidth)
                                .fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(28.dp),
                    ) {
                        InitLogo()

                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(12.dp),
                        ) {
                            Text(
                                text = uiState.welcomeMessage,
                                style = MaterialTheme.typography.headlineLarge,
                                color = MaterialTheme.colorScheme.onBackground,
                                textAlign = TextAlign.Center,
                            )
                            Text(
                                text = "Continúa aprendiendo a tu ritmo.",
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                textAlign = TextAlign.Center,
                            )
                        }

                        Column(
                            modifier =
                                Modifier
                                    .widthIn(max = HomeProgressMaxWidth)
                                    .fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                        ) {
                            Text(
                                text = uiState.progressLabel,
                                style = MaterialTheme.typography.labelLarge,
                                color = MaterialTheme.colorScheme.primary,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.fillMaxWidth(),
                            )
                            LinearProgressIndicator(
                                progress = { uiState.progress },
                                drawStopIndicator = {},
                                modifier =
                                    Modifier
                                        .fillMaxWidth()
                                        .height(8.dp),
                            )
                        }

                        Column(
                            modifier =
                                Modifier
                                    .widthIn(max = HomeControlsMaxWidth)
                                    .fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(12.dp),
                        ) {
                            Button(
                                onClick = onContinueLesson,
                                enabled = uiState.continueLessonId != null,
                                modifier =
                                    Modifier
                                        .fillMaxWidth()
                                        .then(if (uiState.continueLessonId != null) Modifier.focusRequester(initialFocusRequester) else Modifier)
                                        .focusHighlight(ButtonDefaults.shape)
                                        .hoverScale(enabled = uiState.continueLessonId != null),
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.PlayArrow,
                                    contentDescription = null,
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Column(
                                    modifier = Modifier.padding(vertical = 4.dp),
                                    horizontalAlignment = Alignment.Start,
                                ) {
                                    Text(
                                        if (uiState.continueLessonId == null) {
                                            "Siguiente lección próximamente"
                                        } else if (uiState.isContinueLessonStarted) {
                                            "Continuar lección"
                                        } else {
                                            "Comenzar siguiente lección"
                                        },
                                    )
                                    if (uiState.continueLessonId != null) {
                                        Text(
                                            text = "${uiState.continueLessonSubject} · ${uiState.continueLessonTitle}",
                                            style = MaterialTheme.typography.labelSmall,
                                        )
                                    }
                                }
                            }
                            BoxWithConstraints(modifier = Modifier.fillMaxWidth()) {
                                val useCompactLayout = maxWidth < HomeCompactBreakpoint

                                if (useCompactLayout) {
                                    Column(
                                        modifier = Modifier.fillMaxWidth(),
                                        verticalArrangement = Arrangement.spacedBy(12.dp),
                                    ) {
                                        SecondaryHomeButton(
                                            text = "Ver lecciones",
                                            icon = {
                                                Icon(
                                                    imageVector = Icons.AutoMirrored.Filled.List,
                                                    contentDescription = null,
                                                )
                                            },
                                            onClick = onShowLessons,
                                            modifier = Modifier.fillMaxWidth(),
                                            focusRequester = initialFocusRequester.takeIf { uiState.continueLessonId == null },
                                        )
                                        SecondaryHomeButton(
                                            text = "Mis apuntes",
                                            icon = {
                                                Icon(
                                                    imageVector = Icons.Filled.EditNote,
                                                    contentDescription = null,
                                                )
                                            },
                                            onClick = onShowNotes,
                                            modifier = Modifier.fillMaxWidth(),
                                        )
                                    }
                                } else {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                                    ) {
                                        SecondaryHomeButton(
                                            text = "Ver lecciones",
                                            icon = {
                                                Icon(
                                                    imageVector = Icons.AutoMirrored.Filled.List,
                                                    contentDescription = null,
                                                )
                                            },
                                            onClick = onShowLessons,
                                            modifier = Modifier.weight(1f),
                                            focusRequester = initialFocusRequester.takeIf { uiState.continueLessonId == null },
                                        )
                                        SecondaryHomeButton(
                                            text = "Mis apuntes",
                                            icon = {
                                                Icon(
                                                    imageVector = Icons.Filled.EditNote,
                                                    contentDescription = null,
                                                )
                                            },
                                            onClick = onShowNotes,
                                            modifier = Modifier.weight(1f),
                                        )
                                    }
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    OutlinedButton(
                        onClick = onShowSettings,
                        modifier =
                            Modifier
                                .widthIn(min = 220.dp, max = HomeSettingsMaxWidth)
                                .focusHighlight(ButtonDefaults.shape)
                                .hoverScale(),
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Settings,
                            contentDescription = null,
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Configuración")
                    }
                }

                if (availableUpdateVersion != null) {
                    // Misma barra y posición que «Volver» en el resto de pantallas, sin desplazar el contenido centrado.
                    Column(
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 40.dp, vertical = 32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        AppTopBar(
                            navigationIcon = {
                                UpdateButton(version = availableUpdateVersion, onClick = onShowUpdate)
                            },
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun UpdateButton(
    version: String,
    onClick: () -> Unit,
) {
    val description = "Actualizar a la versión $version"
    TooltipBox(
        positionProvider = TooltipDefaults.rememberTooltipPositionProvider(TooltipAnchorPosition.Below),
        tooltip = { PlainTooltip { Text(description) } },
        state = rememberTooltipState(),
    ) {
        IconButton(
            onClick = onClick,
            modifier = Modifier.focusHighlight(CircleShape).hoverScale(),
        ) {
            BadgedBox(badge = { Badge() }) {
                Icon(
                    imageVector = Icons.Filled.SystemUpdateAlt,
                    contentDescription = description,
                    tint = MaterialTheme.colorScheme.primary,
                )
            }
        }
    }
}

@Composable
private fun SecondaryHomeButton(
    text: String,
    icon: @Composable () -> Unit,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    focusRequester: FocusRequester? = null,
) {
    OutlinedButton(
        onClick = onClick,
        modifier =
            modifier
                .then(if (focusRequester != null) Modifier.focusRequester(focusRequester) else Modifier)
                .focusHighlight(ButtonDefaults.shape)
                .hoverScale(),
    ) {
        icon()
        Spacer(modifier = Modifier.width(8.dp))
        Text(text)
    }
}
