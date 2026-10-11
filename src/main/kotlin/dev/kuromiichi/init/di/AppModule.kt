package dev.kuromiichi.init.di

import dev.kuromiichi.init.data.backup.BackupRepository
import dev.kuromiichi.init.data.local.resolveAppDataDirectory
import dev.kuromiichi.init.data.preferences.UserPreferencesRepository
import dev.kuromiichi.init.data.preferences.createUserPreferencesDataStore
import dev.kuromiichi.init.data.progress.InitDatabase
import dev.kuromiichi.init.data.progress.LessonProgressRepository
import dev.kuromiichi.init.data.progress.createInitDatabase
import dev.kuromiichi.init.data.update.AppVersion
import dev.kuromiichi.init.data.update.GitHubReleaseSource
import dev.kuromiichi.init.data.update.SystemUpdateInstaller
import dev.kuromiichi.init.data.update.UpdatePlatform
import dev.kuromiichi.init.data.update.UpdateRepository
import dev.kuromiichi.init.ui.AppViewModel
import dev.kuromiichi.init.ui.home.HomeViewModel
import dev.kuromiichi.init.ui.lessons.CurrentLessonViewModel
import dev.kuromiichi.init.ui.lessons.LessonListViewModel
import dev.kuromiichi.init.ui.onboarding.OnboardingViewModel
import dev.kuromiichi.init.ui.settings.SettingsViewModel
import dev.kuromiichi.init.ui.update.UpdateViewModel
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

        single {
            val platform = UpdatePlatform.current()
            val updatesDirectory = resolveAppDataDirectory().resolve("updates")
            UpdateRepository(
                installedVersion = AppVersion.current(),
                platform = platform,
                releaseSource = GitHubReleaseSource(),
                installer = SystemUpdateInstaller(platform, workDirectory = updatesDirectory),
                downloadsDirectory = updatesDirectory,
                externalScope = get(),
            )
        }
    }

val viewModelModule =
    module {
        viewModelOf(::AppViewModel)
        viewModelOf(::SettingsViewModel)
        viewModelOf(::UpdateViewModel)
        viewModel { HomeViewModel(get(), get()) }
        viewModel { LessonListViewModel(get(), get()) }
        viewModel { OnboardingViewModel(get(), get()) }
        viewModel { CurrentLessonViewModel(get(), get(), get()) }
    }

val appModules = listOf(dataModule, viewModelModule)
