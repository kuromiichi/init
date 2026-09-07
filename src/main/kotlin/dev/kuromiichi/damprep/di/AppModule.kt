package dev.kuromiichi.damprep.di

import dev.kuromiichi.damprep.data.preferences.UserPreferencesRepository
import dev.kuromiichi.damprep.data.preferences.createUserPreferencesDataStore
import dev.kuromiichi.damprep.data.progress.DamPrepDatabase
import dev.kuromiichi.damprep.data.progress.LessonProgressRepository
import dev.kuromiichi.damprep.data.progress.createDamPrepDatabase
import org.koin.dsl.module
import org.koin.dsl.onClose

val appModule =
    module {
        single { createUserPreferencesDataStore() }
        single { UserPreferencesRepository(get()) }

        single { createDamPrepDatabase() } onClose { database -> database?.close() }
        single { get<DamPrepDatabase>().lessonProgressDao() }
        single { LessonProgressRepository(get()) }
    }
