package dev.kuromiichi.init.data.progress

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import dev.kuromiichi.init.data.local.resolveAppDataDirectory
import kotlinx.coroutines.Dispatchers
import java.io.File

internal const val DATABASE_FILE_NAME = "init.db"

@Database(
    entities = [CompletedLessonEntity::class, LessonStepProgressEntity::class],
    version = 2,
    autoMigrations = [AutoMigration(from = 1, to = 2)],
    exportSchema = true,
)
abstract class InitDatabase : RoomDatabase() {
    abstract fun lessonProgressDao(): LessonProgressDao
}

fun createInitDatabase(
    databaseFile: File = resolveAppDataDirectory().resolve(DATABASE_FILE_NAME),
): InitDatabase {
    databaseFile.parentFile?.mkdirs()

    return Room
        .databaseBuilder<InitDatabase>(name = databaseFile.absolutePath)
        .setDriver(BundledSQLiteDriver())
        .setQueryCoroutineContext(Dispatchers.IO)
        .build()
}
