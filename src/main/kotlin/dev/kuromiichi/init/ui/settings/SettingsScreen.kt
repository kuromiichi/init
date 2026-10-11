package dev.kuromiichi.init.ui.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalMinimumInteractiveComponentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.kuromiichi.init.data.preferences.PronounPreference
import dev.kuromiichi.init.data.preferences.ThemePreference
import dev.kuromiichi.init.ui.common.AppScreen
import dev.kuromiichi.init.ui.common.AppScreenContentMaxWidth
import dev.kuromiichi.init.ui.common.KeyboardShortcutsVisual
import dev.kuromiichi.init.ui.common.LoadingScreen
import dev.kuromiichi.init.ui.common.OperatingSystemSelector
import dev.kuromiichi.init.ui.common.SettingControlMaxWidth
import dev.kuromiichi.init.ui.common.SettingRow
import dev.kuromiichi.init.ui.common.focusHighlight
import dev.kuromiichi.init.ui.common.verticalFadingEdges
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onBack: () -> Unit,
    viewModel: SettingsViewModel = koinViewModel(),
) {
    val focusManager = LocalFocusManager.current
    val uiState = viewModel.uiState.collectAsStateWithLifecycle().value

    if (uiState == null) {
        LoadingScreen()
        return
    }

    var isNameFieldFocused by remember { mutableStateOf(false) }

    // Los controles miden lo que se ve, para que el contorno de foco quede pegado a ellos y no a su área táctil.
    CompositionLocalProvider(LocalMinimumInteractiveComponentSize provides Dp.Unspecified) {
        AppScreen(
            onBack = {
                viewModel.commitStudentName()
                onBack()
            },
            arrowKeyNavigation = true,
            isEditingText = { isNameFieldFocused },
            modifier =
                Modifier.onPreviewKeyEvent { event ->
                    // En el campo del nombre, Escape descarta lo escrito y sale del campo; un segundo Escape sale de la pantalla.
                    if (event.type == KeyEventType.KeyDown && event.key == Key.Escape && isNameFieldFocused) {
                        viewModel.discardStudentNameDraft()
                        focusManager.clearFocus()
                        true
                    } else {
                        false
                    }
                },
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
                Column(
                    // El desplazamiento recorta: el margen deja sitio al contorno de foco de los controles del borde.
                    modifier = Modifier.fillMaxWidth().verticalScroll(scrollState).padding(horizontal = 4.dp),
                ) {
                    Text(
                        text = "Configuración",
                        style = MaterialTheme.typography.headlineLarge,
                        color = MaterialTheme.colorScheme.onBackground,
                    )
                    Text(
                        text = "Ajusta cómo quieres que la aplicación se dirija a ti, qué instrucciones muestra y cómo se ve mientras estudias.",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(top = 12.dp),
                    )

                    Column(
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .padding(top = 40.dp),
                        verticalArrangement = Arrangement.spacedBy(28.dp),
                    ) {
                        SettingRow(
                            title = "Nombre",
                            description = "Se usa en saludos y mensajes de progreso.",
                        ) {
                            // El campo guarda la selección: al entrar con el teclado el cursor va al final del nombre.
                            var nameFieldValue by remember { mutableStateOf(TextFieldValue(uiState.studentNameDraft)) }
                            if (nameFieldValue.text != uiState.studentNameDraft) {
                                nameFieldValue = TextFieldValue(uiState.studentNameDraft, TextRange(uiState.studentNameDraft.length))
                            }
                            OutlinedTextField(
                                value = nameFieldValue,
                                onValueChange = { value ->
                                    nameFieldValue = value
                                    viewModel.updateStudentNameDraft(value.text)
                                },
                                modifier =
                                    Modifier
                                        .widthIn(max = SettingControlMaxWidth)
                                        .fillMaxWidth()
                                        .onFocusChanged { focusState ->
                                            if (focusState.isFocused && !isNameFieldFocused) {
                                                nameFieldValue = nameFieldValue.copy(selection = TextRange(nameFieldValue.text.length))
                                            }
                                            isNameFieldFocused = focusState.isFocused
                                            if (!focusState.isFocused) viewModel.commitStudentName()
                                        },
                                label = { Text("Nombre") },
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                                keyboardActions =
                                    KeyboardActions(
                                        // Intro guarda el nombre (al salir del campo) y pasa al siguiente ajuste.
                                        onDone = {
                                            focusManager.moveFocus(FocusDirection.Down)
                                        },
                                    ),
                            )
                        }

                        SettingRow(
                            title = "Tratamiento",
                            description = "Define el lenguaje del saludo principal.",
                        ) {
                            SingleChoiceSegmentedButtonRow(
                                modifier =
                                    Modifier
                                        .widthIn(max = SettingControlMaxWidth)
                                        .fillMaxWidth(),
                            ) {
                                PronounPreference.entries.forEachIndexed { index, preference ->
                                    SegmentedButton(
                                        selected = preference == uiState.pronounPreference,
                                        onClick = { viewModel.setPronounPreference(preference) },
                                        modifier = Modifier.focusHighlight(SegmentedButtonDefaults.itemShape(index = index, count = PronounPreference.entries.size), outset = 3.dp),
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
                        }

                        SettingRow(
                            title = "Sistema operativo",
                            description = "Elige el sistema cuyas instrucciones y comandos quieres seguir.",
                        ) {
                            OperatingSystemSelector(
                                selectedOperatingSystem = uiState.targetOperatingSystem,
                                onOperatingSystemSelected = viewModel::setTargetOperatingSystem,
                                modifier =
                                    Modifier
                                        .widthIn(max = SettingControlMaxWidth)
                                        .fillMaxWidth(),
                            )
                        }

                        SettingRow(
                            title = "Tema",
                            description = "Elige colores claros, oscuros o deja que lo decida el sistema.",
                        ) {
                            SingleChoiceSegmentedButtonRow(
                                modifier =
                                    Modifier
                                        .widthIn(max = SettingControlMaxWidth)
                                        .fillMaxWidth(),
                            ) {
                                ThemePreference.entries.forEachIndexed { index, preference ->
                                    SegmentedButton(
                                        selected = preference == uiState.themePreference,
                                        onClick = { viewModel.setThemePreference(preference) },
                                        modifier = Modifier.focusHighlight(SegmentedButtonDefaults.itemShape(index = index, count = ThemePreference.entries.size), outset = 3.dp),
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

                        SettingRow(
                            title = "Atajos de teclado",
                            description = "Todo se puede hacer sin ratón.",
                            // El visual ocupa todo el ancho: la etiqueta queda encima, como en el diseño compacto.
                            compactBreakpoint = Dp.Infinity,
                        ) {
                            KeyboardShortcutsVisual()
                        }

                        BackupSettingsSection(
                            backupState = uiState.backupState,
                            onExport = viewModel::exportBackup,
                            onImport = viewModel::importBackup,
                            onDismissResult = viewModel::dismissBackupResult,
                        )

                        AdvancedSettingsSection(
                            unlockAllLessons = uiState.unlockAllLessons,
                            hasLessonProgress = uiState.hasLessonProgress,
                            onUnlockAllLessonsChanged = viewModel::setUnlockAllLessons,
                            onResetLessonProgress = viewModel::resetLessonProgress,
                        )
                    }
                }
            }
        }
    }
}
