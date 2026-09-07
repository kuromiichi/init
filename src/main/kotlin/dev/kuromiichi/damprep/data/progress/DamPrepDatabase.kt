package dev.kuromiichi.damprep.data.progress

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import dev.kuromiichi.damprep.data.local.resolveAppDataDirectory
import kotlinx.coroutines.Dispatchers
import java.io.File

private const val DATABASE_FILE_NAME = "dam-prep.db"

@Database(
    entities = [CompletedLessonEntity::class],
    version = 1,
    exportSchema = true,
)
abstract class DamPrepDatabase : RoomDatabase() {
    abstract fun lessonProgressDao(): LessonProgressDao
}

fun createDamPrepDatabase(
    databaseFile: File = resolveAppDataDirectory().resolve(DATABASE_FILE_NAME),
): DamPrepDatabase {
    databaseFile.parentFile?.mkdirs()

    return Room
        .databaseBuilder<DamPrepDatabase>(name = databaseFile.absolutePath)
        .setDriver(BundledSQLiteDriver())
        .setQueryCoroutineContext(Dispatchers.IO)
        .build()
}
