package dev.kuromiichi.damprep.testing

import dev.kuromiichi.damprep.data.backup.BackupRepository
import dev.kuromiichi.damprep.data.preferences.UserPreferencesRepository
import dev.kuromiichi.damprep.data.progress.LessonProgressRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import kotlin.test.AfterTest
import kotlin.test.BeforeTest

/**
 * Ejecuta `viewModelScope` y el ámbito de aplicación de forma inmediata y ofrece repositorios
 * en memoria, de modo que cada cambio se refleja en el estado sin esperas.
 */
@OptIn(ExperimentalCoroutinesApi::class)
abstract class ViewModelTest {
    private val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.Unconfined)
    private val uiScope = CoroutineScope(SupervisorJob() + Dispatchers.Unconfined)

    protected val lessonProgressDao = FakeLessonProgressDao()
    protected val preferencesRepository = UserPreferencesRepository(InMemoryPreferencesDataStore(), applicationScope)
    protected val lessonProgressRepository = LessonProgressRepository(lessonProgressDao, applicationScope)
    protected val backupRepository =
        BackupRepository(preferencesRepository, lessonProgressRepository, ioDispatcher = Dispatchers.Unconfined)

    @BeforeTest
    fun setMainDispatcher() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
    }

    @AfterTest
    fun resetMainDispatcher() {
        uiScope.cancel()
        applicationScope.cancel()
        Dispatchers.resetMain()
    }

    /** Simula la pantalla observando el estado, que solo se produce mientras alguien lo recoge. */
    protected fun <T> StateFlow<T>.observed(): StateFlow<T> = also { state -> uiScope.launch { state.collect {} } }
}
