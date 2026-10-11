package dev.kuromiichi.init.di

import dev.kuromiichi.init.data.backup.BackupRepository
import dev.kuromiichi.init.data.preferences.UserPreferencesRepository
import dev.kuromiichi.init.data.preferences.createUserPreferencesDataStore
import dev.kuromiichi.init.data.progress.InitDatabase
import dev.kuromiichi.init.data.progress.LessonProgressRepository
import dev.kuromiichi.init.data.progress.createInitDatabase
import dev.kuromiichi.init.ui.AppViewModel
import dev.kuromiichi.init.ui.home.HomeViewModel
import dev.kuromiichi.init.ui.lessons.CurrentLessonViewModel
import dev.kuromiichi.init.ui.lessons.LessonListViewModel
import dev.kuromiichi.init.ui.onboarding.OnboardingViewModel
import dev.kuromiichi.init.ui.settings.SettingsViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.koin.core.module.dsl.viewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import org.koin.dsl.onClose

val dataModule =
    module {
        // Ámbito de aplicación para escrituras que no deben cancelarse al cerrar una pantalla.
        single<CoroutineScope> { CoroutineScope(SupervisorJob() + Dispatchers.IO) }

        single { createUserPreferencesDataStore() }
        single { UserPreferencesRepository(get(), get()) }

        single { createInitDatabase() } onClose { database -> database?.close() }
        single { get<InitDatabase>().lessonProgressDao() }
        single { LessonProgressRepository(get(), get()) }
        single { BackupRepository(get(), get()) }
    }

val viewModelModule =
    module {
        viewModelOf(::AppViewModel)
        viewModelOf(::SettingsViewModel)
        viewModel { HomeViewModel(get(), get()) }
        viewModel { LessonListViewModel(get(), get()) }
        viewModel { OnboardingViewModel(get(), get()) }
        viewModel { CurrentLessonViewModel(get(), get(), get()) }
    }

val appModules = listOf(dataModule, viewModelModule)
