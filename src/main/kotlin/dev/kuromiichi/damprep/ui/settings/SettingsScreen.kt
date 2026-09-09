package dev.kuromiichi.damprep.ui.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import dev.kuromiichi.damprep.data.preferences.PronounPreference
import dev.kuromiichi.damprep.data.preferences.TargetOperatingSystem
import dev.kuromiichi.damprep.data.preferences.ThemePreference
import dev.kuromiichi.damprep.ui.common.AppScreen
import dev.kuromiichi.damprep.ui.common.AppScreenContentMaxWidth
import dev.kuromiichi.damprep.ui.common.OperatingSystemSelector
import dev.kuromiichi.damprep.ui.common.SettingControlMaxWidth
import dev.kuromiichi.damprep.ui.common.SettingRow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    studentName: String,
    pronounPreference: PronounPreference,
    targetOperatingSystem: TargetOperatingSystem?,
    themePreference: ThemePreference,
    unlockAllLessons: Boolean,
    hasLessonProgress: Boolean,
    onStudentNameChanged: (String) -> Unit,
    onPronounPreferenceChanged: (PronounPreference) -> Unit,
    onTargetOperatingSystemChanged: (TargetOperatingSystem) -> Unit,
    onThemePreferenceChanged: (ThemePreference) -> Unit,
    onUnlockAllLessonsChanged: (Boolean) -> Unit,
    onResetLessonProgress: () -> Unit,
    onBack: () -> Unit,
) {
    val focusManager = LocalFocusManager.current
    val viewModel =
        remember {
            SettingsViewModel(
                studentName = studentName,
                pronounPreference = pronounPreference,
                targetOperatingSystem = targetOperatingSystem,
                themePreference = themePreference,
            )
        }
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(studentName, pronounPreference, targetOperatingSystem, themePreference) {
        viewModel.syncPreferences(
            studentName = studentName,
            pronounPreference = pronounPreference,
            targetOperatingSystem = targetOperatingSystem,
            themePreference = themePreference,
        )
    }

    AppScreen(
        onBack = {
            viewModel.commitStudentName()?.let(onStudentNameChanged)
            onBack()
        },
    ) {
        Column(
            modifier =
                Modifier
                    .weight(1f)
                    .widthIn(max = AppScreenContentMaxWidth)
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Center,
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
                    OutlinedTextField(
                        value = uiState.studentNameDraft,
                        onValueChange = viewModel::updateStudentNameDraft,
                        modifier =
                            Modifier
                                .widthIn(max = SettingControlMaxWidth)
                                .fillMaxWidth()
                                .onFocusChanged { focusState ->
                                    viewModel
                                        .updateStudentNameFocus(focusState.isFocused)
                                        ?.let(onStudentNameChanged)
                                },
                        label = { Text("Nombre") },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                        keyboardActions =
                            KeyboardActions(
                                onDone = {
                                    focusManager.clearFocus()
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
                                onClick = {
                                    viewModel.updatePronounPreference(preference)
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
                }

                SettingRow(
                    title = "Sistema operativo",
                    description = "Elige el sistema cuyas instrucciones y comandos quieres seguir.",
                ) {
                    OperatingSystemSelector(
                        selectedOperatingSystem = uiState.targetOperatingSystem,
                        onOperatingSystemSelected = { operatingSystem ->
                            viewModel.updateTargetOperatingSystem(operatingSystem)
                            onTargetOperatingSystemChanged(operatingSystem)
                        },
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
                                onClick = {
                                    viewModel.updateThemePreference(preference)
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

                AdvancedSettingsSection(
                    unlockAllLessons = unlockAllLessons,
                    hasLessonProgress = hasLessonProgress,
                    onUnlockAllLessonsChanged = onUnlockAllLessonsChanged,
                    onResetLessonProgress = onResetLessonProgress,
                )
            }
        }
    }
}
