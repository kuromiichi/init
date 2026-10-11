package dev.kuromiichi.init.data.update

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import java.io.File
import java.io.IOException
import java.nio.file.Files
import java.security.MessageDigest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertTrue

class UpdateRepositoryTest {
    private val installerBytes = "instalador".toByteArray()
    private val installerName = "init-0.2.0-macos-arm64.dmg"
    private val downloadsDirectory: File = Files.createTempDirectory("init-updates").toFile().apply { deleteOnExit() }

    @Test
    fun `development builds do not look for updates`() {
        val source = FakeReleaseSource(release())
        val repository = createRepository(source, installed = AppVersion(release = null, commit = "abc"))

        repository.checkForUpdates()

        assertEquals(UpdateState.Unsupported, repository.state.value)
        assertEquals(0, source.latestReleaseCalls)
    }

    @Test
    fun `apps not run from an installation do not look for updates`() {
        val repository = createRepository(FakeReleaseSource(release()), installer = FakeInstaller(isAvailable = false))

        assertEquals(UpdateState.Unsupported, repository.state.value)
    }

    @Test
    fun `newer release is offered`() {
        val repository = createRepository(FakeReleaseSource(release()))

        repository.checkForUpdates()

        val state = assertIs<UpdateState.Available>(repository.state.value)
        assertEquals(SemanticVersion(0, 2, 0), state.update.version)
    }

    @Test
    fun `no release or an older one means the app is up to date`() {
        val withoutReleases = createRepository(FakeReleaseSource(release = null))
        withoutReleases.checkForUpdates()
        assertEquals(UpdateState.UpToDate, withoutReleases.state.value)

        val withOlderRelease = createRepository(FakeReleaseSource(release()), installed = version(0, 2, 0))
        withOlderRelease.checkForUpdates()
        assertEquals(UpdateState.UpToDate, withOlderRelease.state.value)
    }

    @Test
    fun `connection errors are reported`() {
        val repository = createRepository(FakeReleaseSource(release(), failCheck = true))

        repository.checkForUpdates()

        assertIs<UpdateState.CheckFailed>(repository.state.value)
    }

    @Test
    fun `verified installer is launched and the app restarts`() {
        val installer = FakeInstaller()
        val repository = createRepository(FakeReleaseSource(release()), installer = installer)
        repository.checkForUpdates()

        repository.installUpdate()

        assertIs<UpdateState.RestartPending>(repository.state.value)
        val installed = installer.launched.single()
        assertEquals(installerName, installed.name)
        assertTrue(installed.readBytes().contentEquals(installerBytes))
    }

    @Test
    fun `system installers that need the user keep the app open`() {
        val repository =
            createRepository(FakeReleaseSource(release()), installer = FakeInstaller(result = InstallLaunch.InstallerOpened))
        repository.checkForUpdates()

        repository.installUpdate()

        assertIs<UpdateState.InstallerOpened>(repository.state.value)
    }

    @Test
    fun `damaged downloads are not installed`() {
        val installer = FakeInstaller()
        val repository = createRepository(FakeReleaseSource(release(), checksum = "0".repeat(64)), installer = installer)
        repository.checkForUpdates()

        repository.installUpdate()

        assertIs<UpdateState.InstallFailed>(repository.state.value)
        assertTrue(installer.launched.isEmpty())
        assertTrue(downloadsDirectory.listFiles().orEmpty().isEmpty())
    }

    @Test
    fun `releases without checksums are not installed`() {
        val release = release().let { it.copy(assets = it.assets.filterNot { asset -> asset.name == CHECKSUMS_ASSET_NAME }) }
        val installer = FakeInstaller()
        val repository = createRepository(FakeReleaseSource(release), installer = installer)
        repository.checkForUpdates()

        repository.installUpdate()

        assertIs<UpdateState.InstallFailed>(repository.state.value)
        assertTrue(installer.launched.isEmpty())
    }

    @Test
    fun `failed installs can be retried`() {
        val installer = FakeInstaller(failures = 1)
        val repository = createRepository(FakeReleaseSource(release()), installer = installer)
        repository.checkForUpdates()

        repository.installUpdate()
        assertIs<UpdateState.InstallFailed>(repository.state.value)
        repository.installUpdate()

        assertIs<UpdateState.RestartPending>(repository.state.value)
    }

    private fun createRepository(
        source: ReleaseSource,
        installed: AppVersion = version(0, 1, 0),
        installer: UpdateInstaller = FakeInstaller(),
    ) = UpdateRepository(
        installedVersion = installed,
        platform = UpdatePlatform.MacOsArm64,
        releaseSource = source,
        installer = installer,
        downloadsDirectory = downloadsDirectory,
        externalScope = CoroutineScope(SupervisorJob() + Dispatchers.Unconfined),
        ioDispatcher = Dispatchers.Unconfined,
    )

    private fun version(
        major: Int,
        minor: Int,
        patch: Int,
    ) = AppVersion(SemanticVersion(major, minor, patch), commit = "abc1234")

    private fun release() =
        GitHubRelease(
            tagName = "v0.2.0",
            htmlUrl = "https://github.com/kuromiichi/init/releases/tag/v0.2.0",
            assets =
                listOf(
                    GitHubReleaseAsset(installerName, "https://example.invalid/$installerName", installerBytes.size.toLong()),
                    GitHubReleaseAsset(CHECKSUMS_ASSET_NAME, "https://example.invalid/$CHECKSUMS_ASSET_NAME"),
                ),
        )

    private inner class FakeReleaseSource(
        private val release: GitHubRelease?,
        private val failCheck: Boolean = false,
        private val checksum: String = sha256(installerBytes),
    ) : ReleaseSource {
        var latestReleaseCalls = 0

        override fun latestRelease(): GitHubRelease? {
            latestReleaseCalls++
            if (failCheck) throw IOException("Sin conexión")
            return release
        }

        override fun readText(url: String): String = "$checksum  $installerName\n"

        override fun download(
            url: String,
            target: File,
            onProgress: (bytesRead: Long) -> Unit,
        ) {
            target.writeBytes(installerBytes)
            onProgress(installerBytes.size.toLong())
        }
    }

    private class FakeInstaller(
        override val isAvailable: Boolean = true,
        private val result: InstallLaunch = InstallLaunch.RestartToFinish,
        private var failures: Int = 0,
    ) : UpdateInstaller {
        val launched = mutableListOf<File>()

        override fun launch(installerFile: File): InstallLaunch {
            if (failures > 0) {
                failures--
                throw IOException("No se ha podido abrir el instalador.")
            }
            launched += installerFile
            return result
        }
    }

    private fun sha256(bytes: ByteArray): String = MessageDigest.getInstance("SHA-256").digest(bytes).joinToString("") { "%02x".format(it) }
}
