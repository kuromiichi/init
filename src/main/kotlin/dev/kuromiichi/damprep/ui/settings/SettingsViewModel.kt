package dev.kuromiichi.damprep.ui.settings

import dev.kuromiichi.damprep.data.preferences.PronounPreference
import dev.kuromiichi.damprep.ui.theme.ThemePreference
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class SettingsUiState(
    val savedStudentName: String,
    val studentNameDraft: String,
    val isStudentNameFocused: Boolean = false,
    val pronounPreference: PronounPreference,
    val themePreference: ThemePreference,
)

class SettingsViewModel(
    studentName: String,
    pronounPreference: PronounPreference,
    themePreference: ThemePreference,
) {
    private val _uiState = MutableStateFlow(
        SettingsUiState(
            savedStudentName = studentName,
            studentNameDraft = studentName,
            pronounPreference = pronounPreference,
            themePreference = themePreference,
        ),
    )
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()

    fun syncPreferences(
        studentName: String,
        pronounPreference: PronounPreference,
        themePreference: ThemePreference,
    ) {
        _uiState.update { state ->
            state.copy(
                savedStudentName = studentName,
                studentNameDraft = if (state.isStudentNameFocused) {
                    state.studentNameDraft
                } else {
                    studentName
                },
                pronounPreference = pronounPreference,
                themePreference = themePreference,
            )
        }
    }

    fun updateStudentNameDraft(studentName: String) {
        _uiState.update { state ->
            state.copy(studentNameDraft = studentName)
        }
    }

    fun updateStudentNameFocus(isFocused: Boolean): String? {
        val shouldCommit = _uiState.value.isStudentNameFocused && !isFocused
        _uiState.update { state ->
            state.copy(isStudentNameFocused = isFocused)
        }
        return if (shouldCommit) {
            commitStudentName()
        } else {
            null
        }
    }

    fun updatePronounPreference(pronounPreference: PronounPreference) {
        _uiState.update { state ->
            state.copy(pronounPreference = pronounPreference)
        }
    }

    fun updateThemePreference(themePreference: ThemePreference) {
        _uiState.update { state ->
            state.copy(themePreference = themePreference)
        }
    }

    fun commitStudentName(): String? {
        val state = _uiState.value
        return if (state.studentNameDraft != state.savedStudentName) {
            _uiState.value = state.copy(savedStudentName = state.studentNameDraft)
            state.studentNameDraft
        } else {
            null
        }
    }
}
