package dev.kuromiichi.init.ui.onboarding

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.progressSemantics
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FileOpen
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalMinimumInteractiveComponentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.kuromiichi.init.data.preferences.PronounPreference
import dev.kuromiichi.init.data.preferences.TargetOperatingSystem
import dev.kuromiichi.init.data.preferences.ThemePreference
import dev.kuromiichi.init.ui.common.BackupFileDialog
import dev.kuromiichi.init.ui.common.BackupFileDialogMode
import dev.kuromiichi.init.ui.common.BackupOperationState
import dev.kuromiichi.init.ui.common.KeyboardShortcutsVisual
import dev.kuromiichi.init.ui.common.OperatingSystemSelector
import dev.kuromiichi.init.ui.common.arrowKeyNavigation
import dev.kuromiichi.init.ui.common.focusHighlight
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OnboardingScreen(
    onFinished: () -> Unit,
    viewModel: OnboardingViewModel = koinViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var showImportDialog by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(uiState.isFinished) {
        if (uiState.isFinished) onFinished()
    }

    val focusManager = LocalFocusManager.current
    val rootFocusRequester = remember { FocusRequester() }
    val continueFocusRequester = remember { FocusRequester() }
    var hasFocus by remember { mutableStateOf(false) }
    var isRootFocused by remember { mutableStateOf(false) }
    var isNameFieldFocused by remember { mutableStateOf(false) }

    // En cada paso el foco empieza en «Siguiente», que Intro pulsa; si aún no se puede continuar (falta elegir el
    // sistema o escribir el nombre), en la pantalla, y la primera flecha entra al control que falta.
    LaunchedEffect(uiState.currentStepIndex) {
        withFrameNanos { }
        val focused = uiState.canContinue && runCatching { continueFocusRequester.requestFocus() }.getOrDefault(false)
        if (!focused) runCatching { rootFocusRequester.requestFocus() }
    }
    // Sin nada enfocado (al salir del campo con Escape, por ejemplo) las teclas no llegarían: la pantalla lo recupera.
    LaunchedEffect(hasFocus) {
        if (!hasFocus) {
            withFrameNanos { }
            if (!hasFocus) runCatching { rootFocusRequester.requestFocus() }
        }
    }

    // Los controles miden lo que se ve, para que el contorno de foco quede pegado a ellos y no a su área táctil.
    CompositionLocalProvider(LocalMinimumInteractiveComponentSize provides Dp.Unspecified) {
        Surface(
            modifier =
                Modifier
                    .fillMaxSize()
                    .onPreviewKeyEvent { event ->
                        if (event.type != KeyEventType.KeyDown || event.key != Key.Escape) return@onPreviewKeyEvent false
                        when {
                            // En el campo del nombre, Escape solo sale del campo.
                            isNameFieldFocused -> focusManager.clearFocus()
                            !uiState.isFirstStep -> viewModel.goToPreviousStep()
                        }
                        true
                    }.arrowKeyNavigation(
                        isRootFocused = { isRootFocused },
                        isEditingText = { isNameFieldFocused },
                    ).onFocusChanged {
                        hasFocus = it.hasFocus
                        isRootFocused = it.isFocused
                    }.focusRequester(rootFocusRequester)
                    .focusable(),
            color = MaterialTheme.colorScheme.background,
        ) {
            Column(
                modifier =
                    Modifier
                        .fillMaxSize()
                        .padding(horizontal = 40.dp, vertical = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Column(
                    modifier =
                        Modifier
                            .weight(1f)
                            .widthIn(max = 720.dp)
                            .fillMaxWidth()
                            .animateContentSize(
                                animationSpec =
                                    tween(
                                        durationMillis = 260,
                                        easing = FastOutSlowInEasing,
                                    ),
                            )
                            // La animación de tamaño recorta: el margen deja sitio al contorno de foco de los controles.
                            .padding(horizontal = 4.dp),
                    verticalArrangement = Arrangement.Center,
                ) {
                    AnimatedContent(
                        targetState = uiState.currentStepIndex,
                        transitionSpec = {
                            val direction = if (targetState > initialState) 1 else -1

                            (
                                fadeIn(animationSpec = tween(durationMillis = 220)) +
                                    slideInVertically(
                                        animationSpec =
                                            tween(
                                                durationMillis = 260,
                                                easing = FastOutSlowInEasing,
                                            ),
                                        initialOffsetY = { it / 10 * direction },
                                    )
                            ).togetherWith(
                                fadeOut(animationSpec = tween(durationMillis = 160)) +
                                    slideOutVertically(
                                        animationSpec =
                                            tween(
                                                durationMillis = 180,
                                                easing = FastOutSlowInEasing,
                                            ),
                                        targetOffsetY = { -it / 10 * direction },
                                    ),
                            ).using(
                                SizeTransform(clip = false),
                            )
                        },
                        label = "Onboarding step",
                    ) { animatedStepIndex ->
                        val animatedStep = uiState.steps[animatedStepIndex]

                        OnboardingStepContent(
                            step = animatedStep,
                            themePreference = uiState.themePreference,
                            pronounPreference = uiState.pronounPreference,
                            targetOperatingSystem = uiState.targetOperatingSystem,
                            isTargetOperatingSystemConfirmed = uiState.isTargetOperatingSystemConfirmed,
                            studentName = uiState.studentName,
                            backupState = uiState.backupState,
                            onImportRequested = { showImportDialog = true },
                            onThemePreferenceChanged = viewModel::updateThemePreference,
                            onPronounPreferenceChanged = viewModel::updatePronounPreference,
                            onTargetOperatingSystemConfirmed = viewModel::confirmTargetOperatingSystem,
                            onStudentNameChanged = viewModel::updateStudentName,
                            onNameFieldFocusChanged = { isNameFieldFocused = it },
                            onDone = viewModel::continueOnboarding,
                        )
                    }
                }

                Row(
                    modifier =
                        Modifier
                            .widthIn(max = 720.dp)
                            .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    if (!uiState.isFirstStep) {
                        TextButton(
                            onClick = viewModel::goToPreviousStep,
                            modifier = Modifier.focusHighlight(ButtonDefaults.textShape),
                        ) {
                            Text("Atrás")
                        }
                    } else {
                        Spacer(modifier = Modifier)
                    }

                    Button(
                        onClick = viewModel::continueOnboarding,
                        enabled = uiState.canContinue,
                        modifier = Modifier.focusRequester(continueFocusRequester).focusHighlight(ButtonDefaults.shape),
                    ) {
                        Text(if (uiState.isLastStep) "Entrar" else "Siguiente")
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
                OnboardingProgressDots(
                    currentStepIndex = uiState.currentStepIndex,
                    stepCount = uiState.steps.size,
                )
            }
        }
    }

    if (showImportDialog) {
        BackupFileDialog(
            mode = BackupFileDialogMode.Open,
            title = "Importar datos de init",
            onResult = { file ->
                showImportDialog = false
                if (file != null) viewModel.importBackup(file)
            },
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun OnboardingStepContent(
    step: OnboardingStep,
    themePreference: ThemePreference,
    pronounPreference: PronounPreference,
    targetOperatingSystem: TargetOperatingSystem?,
    isTargetOperatingSystemConfirmed: Boolean,
    studentName: String,
    backupState: BackupOperationState,
    onImportRequested: () -> Unit,
    onThemePreferenceChanged: (ThemePreference) -> Unit,
    onPronounPreferenceChanged: (PronounPreference) -> Unit,
    onTargetOperatingSystemConfirmed: (TargetOperatingSystem) -> Unit,
    onStudentNameChanged: (String) -> Unit,
    onNameFieldFocusChanged: (Boolean) -> Unit,
    onDone: () -> Unit,
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = step.title,
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.onBackground,
        )
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = step.body,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )

        AnimatedVisibility(visible = step.kind == OnboardingStepKind.Import) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Spacer(modifier = Modifier.height(28.dp))
                OutlinedButton(
                    onClick = onImportRequested,
                    modifier = Modifier.focusHighlight(ButtonDefaults.outlinedShape),
                    enabled = backupState != BackupOperationState.InProgress,
                ) {
                    Icon(imageVector = Icons.Filled.FileOpen, contentDescription = null)
                    Text(text = "Importar copia de seguridad", modifier = Modifier.padding(start = 8.dp))
                }
                if (backupState is BackupOperationState.Failed) {
                    Text(
                        text = backupState.message,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.error,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(top = 12.dp),
                    )
                }
            }
        }

        AnimatedVisibility(visible = step.kind == OnboardingStepKind.Shortcuts) {
            Column {
                Spacer(modifier = Modifier.height(24.dp))
                KeyboardShortcutsVisual()
            }
        }

        AnimatedVisibility(visible = step.kind == OnboardingStepKind.Theme) {
            Column {
                Spacer(modifier = Modifier.height(28.dp))
                SingleChoiceSegmentedButtonRow(
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    ThemePreference.entries.forEachIndexed { index, preference ->
                        SegmentedButton(
                            modifier = Modifier.focusHighlight(SegmentedButtonDefaults.itemShape(index = index, count = ThemePreference.entries.size), outset = 3.dp),
                            selected = preference == themePreference,
                            onClick = {
                                onThemePreferenceChanged(preference)
                            },
                            shape =
                                SegmentedButtonDefaults.itemShape(
                                    index = index,
                                    count = ThemePreference.entries.size,
                                ),
                        ) {
                            Text(preference.label)
                        }
                    }
                }
            }
        }

        AnimatedVisibility(visible = step.kind == OnboardingStepKind.OperatingSystem) {
            Column {
                Spacer(modifier = Modifier.height(28.dp))
                OperatingSystemSelector(
                    selectedOperatingSystem = targetOperatingSystem,
                    onOperatingSystemSelected = onTargetOperatingSystemConfirmed,
                    modifier = Modifier.fillMaxWidth(),
                )

                Text(
                    text =
                        when {
                            targetOperatingSystem == null -> "No hemos podido detectar el sistema actual. Elige una opción."
                            isTargetOperatingSystemConfirmed ->
                                "Usaremos instrucciones para ${targetOperatingSystem.label}."
                            else ->
                                "Hemos detectado ${targetOperatingSystem.label}. Pulsa una opción para confirmarla."
                        },
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = 12.dp),
                )
            }
        }

        AnimatedVisibility(visible = step.kind == OnboardingStepKind.Personalization) {
            Column {
                Spacer(modifier = Modifier.height(28.dp))
                SingleChoiceSegmentedButtonRow(
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    PronounPreference.entries.forEachIndexed { index, preference ->
                        SegmentedButton(
                            modifier = Modifier.focusHighlight(SegmentedButtonDefaults.itemShape(index = index, count = PronounPreference.entries.size), outset = 3.dp),
                            selected = preference == pronounPreference,
                            onClick = {
                                onPronounPreferenceChanged(preference)
                            },
                            shape =
                                SegmentedButtonDefaults.itemShape(
                                    index = index,
                                    count = PronounPreference.entries.size,
                                ),
                        ) {
                            Text(preference.label)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(28.dp))
                OutlinedTextField(
                    value = studentName,
                    onValueChange = onStudentNameChanged,
                    modifier = Modifier.fillMaxWidth().onFocusChanged { onNameFieldFocusChanged(it.isFocused) },
                    label = { Text("Nombre") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    keyboardActions =
                        KeyboardActions(
                            onDone = {
                                onDone()
                            },
                        ),
                )
            }
        }
    }
}

@Composable
private fun OnboardingProgressDots(
    currentStepIndex: Int,
    stepCount: Int,
) {
    Row(
        modifier =
            Modifier.progressSemantics(
                value = (currentStepIndex + 1).toFloat(),
                valueRange = 0f..stepCount.toFloat(),
                steps = (stepCount - 1).coerceAtLeast(0),
            ),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        repeat(stepCount) { index ->
            val isCurrent = index == currentStepIndex
            val dotColor by animateColorAsState(
                targetValue =
                    if (isCurrent) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        MaterialTheme.colorScheme.outlineVariant
                    },
                animationSpec = tween(durationMillis = 240),
                label = "Onboarding dot color",
            )
            val dotWidth by animateDpAsState(
                targetValue = if (isCurrent) 24.dp else 8.dp,
                animationSpec =
                    tween(
                        durationMillis = 260,
                        easing = FastOutSlowInEasing,
                    ),
                label = "Onboarding dot width",
            )

            Box(
                modifier =
                    Modifier
                        .size(height = 8.dp, width = dotWidth)
                        .clip(MaterialTheme.shapes.extraLarge)
                        .background(dotColor),
            )
        }
    }
}
