package dev.kuromiichi.init.data.update

/**
 * Sistema y arquitectura para los que se publica un instalador. El sufijo coincide con el nombre de los archivos que
 * sube el workflow de releases: `init-<versión>-<sufijo>`.
 */
enum class UpdatePlatform(
    val assetSuffix: String,
) {
    MacOsArm64("macos-arm64.dmg"),
    MacOsX64("macos-x64.dmg"),
    WindowsX64("windows-x64.msi"),
    LinuxX64("linux-x64.deb"),
    ;

    companion object {
        fun current(): UpdatePlatform? = from(System.getProperty("os.name"), System.getProperty("os.arch"))

        internal fun from(
            osName: String,
            osArch: String,
        ): UpdatePlatform? {
            val os = osName.lowercase()
            val isArm64 = osArch.lowercase() in setOf("aarch64", "arm64")
            val isX64 = osArch.lowercase() in setOf("amd64", "x86_64")

            return when {
                os.contains("mac") && isArm64 -> MacOsArm64
                os.contains("mac") && isX64 -> MacOsX64
                os.contains("win") && isX64 -> WindowsX64
                os.contains("linux") && isX64 -> LinuxX64
                else -> null
            }
        }
    }
}
