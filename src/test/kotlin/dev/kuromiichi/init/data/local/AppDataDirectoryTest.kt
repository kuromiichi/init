package dev.kuromiichi.init.data.local

import java.io.File
import java.nio.file.Files
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class AppDataDirectoryTest {
    private val root = Files.createTempDirectory("init-app-data").toFile().apply { deleteOnExit() }
    private val legacyDirectory = root.resolve("DAM Prep")
    private val targetDirectory = root.resolve("dev.kuromiichi.init")

    @Test
    fun `migration moves the legacy directory and renames the database`() {
        legacyDirectory.mkdirs()
        legacyDirectory.resolve("dam-prep.db").writeText("db")
        legacyDirectory.resolve("dam-prep.db-wal").writeText("wal")
        legacyDirectory.resolve("user.preferences_pb").writeText("prefs")

        migrateLegacyAppData(legacyDirectory, targetDirectory)

        assertFalse(legacyDirectory.exists())
        assertEquals("db", targetDirectory.resolve("init.db").readText())
        assertEquals("wal", targetDirectory.resolve("init.db-wal").readText())
        assertEquals("prefs", targetDirectory.resolve("user.preferences_pb").readText())
        assertFalse(targetDirectory.resolve("dam-prep.db").exists())
    }

    @Test
    fun `migration keeps existing data in the new directory`() {
        legacyDirectory.mkdirs()
        legacyDirectory.resolve("dam-prep.db").writeText("old")
        targetDirectory.mkdirs()
        targetDirectory.resolve("init.db").writeText("new")

        migrateLegacyAppData(legacyDirectory, targetDirectory)

        assertTrue(legacyDirectory.resolve("dam-prep.db").exists())
        assertEquals("new", targetDirectory.resolve("init.db").readText())
    }

    @Test
    fun `migration does nothing without legacy data`() {
        migrateLegacyAppData(legacyDirectory, targetDirectory)

        assertFalse(targetDirectory.exists())
        assertEquals(emptyList(), root.listFiles().orEmpty().map(File::getName))
    }
}
