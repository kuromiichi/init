package dev.kuromiichi.damprep.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.kuromiichi.damprep.data.backup.BackupImportException
import dev.kuromiichi.damprep.data.backup.BackupRepository
import dev.kuromiichi.damprep.data.preferences.PronounPreference
import dev.kuromiichi.damprep.data.preferences.TargetOperatingSystem
import dev.kuromiichi.damprep.data.preferences.ThemePreference
import dev.kuromiichi.damprep.data.preferences.UserPreferencesRepository
import dev.kuromiichi.damprep.data.progress.LessonProgressRepository
import dev.kuromiichi.damprep.ui.common.BackupOperationState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.io.File
import java.io.IOException

data class SettingsUiState(
    val studentNameDraft: String,
    val pronounPreference: PronounPreference,
    val targetOperatingSystem: TargetOperatingSystem?,
    val themePreference: ThemePreference,
    val unlockAllLessons: Boolean,
    val hasLessonProgress: Boolean,
    val backupState: BackupOperationState,
)

class SettingsViewModel(
    private val preferencesRepository: UserPreferencesRepository,
    private val lessonProgressRepository: LessonProgressRepository,
    private val backupRepository: BackupRepository,
) : ViewModel() {
    /** Nombre que se está editando; `null` cuando el campo muestra el nombre guardado. */
    private val studentNameDraft = MutableStateFlow<String?>(null)

    private val backupState = MutableStateFlow<BackupOperationState>(BackupOperationState.Idle)

    val uiState: StateFlow<SettingsUiState?> =
        combine(
            preferencesRepository.preferences,
            lessonProgressRepository.completedLessonIds,
            lessonProgressRepository.lessonStepIndices,
            studentNameDraft,
            backupState,
        ) { preferences, completedLessonIds, lessonStepIndices, draft, backup ->
            SettingsUiState(
                studentNameDraft = draft ?: preferences.studentName,
                pronounPreference = preferences.pronounPreference,
                targetOperatingSystem = preferences.targetOperatingSystem,
                themePreference = preferences.themePreference,
                unlockAllLessons = preferences.unlockAllLessons,
                hasLessonProgress = completedLessonIds.isNotEmpty() || lessonStepIndices.isNotEmpty(),
                backupState = backup,
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

    fun exportBackup(file: File) {
        runBackupOperation(onSuccess = BackupOperationState.Exported) { backupRepository.exportTo(file) }
    }

    fun importBackup(file: File) {
        runBackupOperation(onSuccess = BackupOperationState.Imported) {
            backupRepository.importFrom(file)
            // El nombre importado sustituye cualquier edición sin guardar.
            studentNameDraft.value = null
        }
    }

    fun dismissBackupResult() {
        backupState.value = BackupOperationState.Idle
    }

    private fun runBackupOperation(
        onSuccess: BackupOperationState,
        operation: suspend () -> Unit,
    ) {
        if (backupState.value == BackupOperationState.InProgress) return
        backupState.value = BackupOperationState.InProgress

        viewModelScope.launch {
            backupState.value =
                try {
                    operation()
                    onSuccess
                } catch (exception: BackupImportException) {
                    BackupOperationState.Failed(exception.message.orEmpty())
                } catch (exception: IOException) {
                    BackupOperationState.Failed("No se ha podido escribir el archivo: ${exception.message.orEmpty()}")
                }
        }
    }
}
