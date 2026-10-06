package dev.kuromiichi.damprep.di

import dev.kuromiichi.damprep.data.backup.BackupRepository
import dev.kuromiichi.damprep.data.preferences.UserPreferencesRepository
import dev.kuromiichi.damprep.data.preferences.createUserPreferencesDataStore
import dev.kuromiichi.damprep.data.progress.DamPrepDatabase
import dev.kuromiichi.damprep.data.progress.LessonProgressRepository
import dev.kuromiichi.damprep.data.progress.createDamPrepDatabase
import dev.kuromiichi.damprep.ui.AppViewModel
import dev.kuromiichi.damprep.ui.home.HomeViewModel
import dev.kuromiichi.damprep.ui.lessons.CurrentLessonViewModel
import dev.kuromiichi.damprep.ui.lessons.LessonListViewModel
import dev.kuromiichi.damprep.ui.onboarding.OnboardingViewModel
import dev.kuromiichi.damprep.ui.settings.SettingsViewModel
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

        single { createDamPrepDatabase() } onClose { database -> database?.close() }
        single { get<DamPrepDatabase>().lessonProgressDao() }
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
