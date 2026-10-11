package dev.kuromiichi.init.data.update

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class UpdatePlatformTest {
    @Test
    fun `each published installer matches its system and architecture`() {
        assertEquals(UpdatePlatform.MacOsArm64, UpdatePlatform.from("Mac OS X", "aarch64"))
        assertEquals(UpdatePlatform.MacOsX64, UpdatePlatform.from("Mac OS X", "x86_64"))
        assertEquals(UpdatePlatform.WindowsX64, UpdatePlatform.from("Windows 11", "amd64"))
        assertEquals(UpdatePlatform.LinuxX64, UpdatePlatform.from("Linux", "amd64"))
    }

    @Test
    fun `systems without a published installer are not supported`() {
        assertNull(UpdatePlatform.from("Windows 11", "aarch64"))
        assertNull(UpdatePlatform.from("Linux", "aarch64"))
        assertNull(UpdatePlatform.from("FreeBSD", "amd64"))
    }
}
