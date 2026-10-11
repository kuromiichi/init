package dev.kuromiichi.init.data.update

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

class AppVersionTest {
    @Test
    fun `tags are parsed with or without the leading v`() {
        assertEquals(SemanticVersion(0, 1, 0), SemanticVersion.parse("v0.1.0"))
        assertEquals(SemanticVersion(1, 12, 3), SemanticVersion.parse("1.12.3"))
    }

    @Test
    fun `development builds and suffixed tags are not release versions`() {
        assertNull(SemanticVersion.parse("dev"))
        assertNull(SemanticVersion.parse("v0.3.0-beta.1"))
        assertNull(SemanticVersion.parse("1.0"))
    }

    @Test
    fun `versions are compared numerically`() {
        assertTrue(SemanticVersion(0, 10, 0) > SemanticVersion(0, 9, 9))
        assertTrue(SemanticVersion(1, 0, 0) > SemanticVersion(0, 99, 99))
        assertTrue(SemanticVersion(0, 1, 1) > SemanticVersion(0, 1, 0))
    }

    @Test
    fun `major version zero is a beta`() {
        assertTrue(SemanticVersion(0, 4, 2).isBeta)
        assertFalse(SemanticVersion(1, 0, 0).isBeta)
    }

    @Test
    fun `label shows the version and the commit`() {
        assertEquals("0.1.0 (a1b2c3d)", AppVersion(SemanticVersion(0, 1, 0), "a1b2c3d").label)
        assertEquals("dev (a1b2c3d)", AppVersion(release = null, commit = "a1b2c3d").label)
        assertEquals("1.0.0", AppVersion(SemanticVersion(1, 0, 0), commit = "").label)
    }
}
