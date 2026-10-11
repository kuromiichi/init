package dev.kuromiichi.init.data.preferences

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class TargetOperatingSystemTest {
    @Test
    fun `detects supported desktop operating systems`() {
        assertEquals(TargetOperatingSystem.Windows, TargetOperatingSystem.detect("Windows 11"))
        assertEquals(TargetOperatingSystem.MacOS, TargetOperatingSystem.detect("Mac OS X"))
        assertEquals(TargetOperatingSystem.MacOS, TargetOperatingSystem.detect("Darwin"))
        assertEquals(TargetOperatingSystem.Linux, TargetOperatingSystem.detect("Linux"))
    }

    @Test
    fun `unknown operating system has no automatic selection`() {
        assertNull(TargetOperatingSystem.detect("Solaris"))
    }
}
