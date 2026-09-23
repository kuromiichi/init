package dev.kuromiichi.damprep.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.kuromiichi.damprep.data.preferences.PronounPreference
import dev.kuromiichi.damprep.data.preferences.TargetOperatingSystem
import dev.kuromiichi.damprep.data.preferences.ThemePreference
import dev.kuromiichi.damprep.data.preferences.UserPreferencesRepository
import dev.kuromiichi.damprep.data.progress.LessonProgressRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class SettingsUiState(
    val studentNameDraft: String,
    val pronounPreference: PronounPreference,
    val targetOperatingSystem: TargetOperatingSystem?,
    val themePreference: ThemePreference,
    val unlockAllLessons: Boolean,
    val hasLessonProgress: Boolean,
)

class SettingsViewModel(
    private val preferencesRepository: UserPreferencesRepository,
    private val lessonProgressRepository: LessonProgressRepository,
) : ViewModel() {
    /** Nombre que se está editando; `null` cuando el campo muestra el nombre guardado. */
    private val studentNameDraft = MutableStateFlow<String?>(null)

    val uiState: StateFlow<SettingsUiState?> =
        combine(
            preferencesRepository.preferences,
            lessonProgressRepository.completedLessonIds,
            lessonProgressRepository.lessonStepIndices,
            studentNameDraft,
        ) { preferences, completedLessonIds, lessonStepIndices, draft ->
            SettingsUiState(
                studentNameDraft = draft ?: preferences.studentName,
                pronounPreference = preferences.pronounPreference,
                targetOperatingSystem = preferences.targetOperatingSystem,
                themePreference = preferences.themePreference,
                unlockAllLessons = preferences.unlockAllLessons,
                hasLessonProgress = completedLessonIds.isNotEmpty() || lessonStepIndices.isNotEmpty(),
            )
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)

    fun updateStudentNameDraft(studentName: String) {
        studentNameDraft.value = studentName
    }

    fun commitStudentName() {
        val draft = studentNameDraft.value ?: return

        viewModelScope.launch {
            preferencesRepository.setStudentName(draft)
            studentNameDraft.compareAndSet(draft, null)
        }
    }

    fun setPronounPreference(pronounPreference: PronounPreference) {
        viewModelScope.launch { preferencesRepository.setPronounPreference(pronounPreference) }
    }

    fun setTargetOperatingSystem(targetOperatingSystem: TargetOperatingSystem) {
        viewModelScope.launch { preferencesRepository.setTargetOperatingSystem(targetOperatingSystem) }
    }

    fun setThemePreference(themePreference: ThemePreference) {
        viewModelScope.launch { preferencesRepository.setThemePreference(themePreference) }
    }

    fun setUnlockAllLessons(unlockAllLessons: Boolean) {
        viewModelScope.launch { preferencesRepository.setUnlockAllLessons(unlockAllLessons) }
    }

    fun resetLessonProgress() {
        viewModelScope.launch { lessonProgressRepository.resetProgress() }
    }
}
