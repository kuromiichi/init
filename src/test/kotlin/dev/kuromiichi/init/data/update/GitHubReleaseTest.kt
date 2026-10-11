package dev.kuromiichi.init.data.update

import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class GitHubReleaseTest {
    private val json = Json { ignoreUnknownKeys = true }

    @Test
    fun `api response is decoded ignoring unused fields`() {
        val release =
            json.decodeFromString<GitHubRelease>(
                """
                {
                  "id": 1,
                  "tag_name": "v0.2.0",
                  "name": "init 0.2.0",
                  "html_url": "https://github.com/kuromiichi/init/releases/tag/v0.2.0",
                  "body": "- feat: nueva lección",
                  "draft": false,
                  "prerelease": false,
                  "assets": [
                    {
                      "name": "init-0.2.0-macos-arm64.dmg",
                      "size": 1024,
                      "browser_download_url": "https://github.com/kuromiichi/init/releases/download/v0.2.0/init-0.2.0-macos-arm64.dmg",
                      "content_type": "application/x-apple-diskimage"
                    }
                  ]
                }
                """.trimIndent(),
            )

        assertEquals("v0.2.0", release.tagName)
        assertEquals("- feat: nueva lección", release.body)
        assertEquals(1024, release.assets.single().size)
    }

    @Test
    fun `newer release with an installer for this system is an update`() {
        val update = release("v0.2.0").toAvailableUpdate(SemanticVersion(0, 1, 0), UpdatePlatform.WindowsX64)

        assertNotNull(update)
        assertEquals(SemanticVersion(0, 2, 0), update.version)
        assertEquals("init-0.2.0-windows-x64.msi", update.installer.name)
        assertEquals(CHECKSUMS_ASSET_NAME, update.checksums?.name)
    }

    @Test
    fun `same or older releases are not updates`() {
        assertNull(release("v0.2.0").toAvailableUpdate(SemanticVersion(0, 2, 0), UpdatePlatform.WindowsX64))
        assertNull(release("v0.1.9").toAvailableUpdate(SemanticVersion(0, 2, 0), UpdatePlatform.WindowsX64))
    }

    @Test
    fun `releases without an installer for this system are ignored`() {
        val release = release("v0.2.0").copy(assets = listOf(asset("init-0.2.0-linux-x64.deb")))

        assertNull(release.toAvailableUpdate(SemanticVersion(0, 1, 0), UpdatePlatform.MacOsArm64))
    }

    @Test
    fun `drafts and prereleases are ignored`() {
        assertNull(release("v0.2.0").copy(draft = true).toAvailableUpdate(SemanticVersion(0, 1, 0), UpdatePlatform.WindowsX64))
        assertNull(release("v0.2.0").copy(prerelease = true).toAvailableUpdate(SemanticVersion(0, 1, 0), UpdatePlatform.WindowsX64))
    }

    @Test
    fun `checksum is found in sha256sum output`() {
        val content =
            """
            ABC123  init-0.2.0-macos-arm64.dmg
            def456 *init-0.2.0-windows-x64.msi
            """.trimIndent()

        assertEquals("abc123", parseSha256Sums(content, "init-0.2.0-macos-arm64.dmg"))
        assertEquals("def456", parseSha256Sums(content, "init-0.2.0-windows-x64.msi"))
        assertNull(parseSha256Sums(content, "init-0.2.0-linux-x64.deb"))
    }

    private fun release(tag: String): GitHubRelease {
        val version = tag.removePrefix("v")
        return GitHubRelease(
            tagName = tag,
            htmlUrl = "https://github.com/kuromiichi/init/releases/tag/$tag",
            body = "Notas",
            assets =
                listOf(
                    asset("init-$version-macos-arm64.dmg"),
                    asset("init-$version-windows-x64.msi"),
                    asset(CHECKSUMS_ASSET_NAME),
                ),
        )
    }

    private fun asset(name: String) = GitHubReleaseAsset(name = name, downloadUrl = "https://example.invalid/$name", size = 10)
}
