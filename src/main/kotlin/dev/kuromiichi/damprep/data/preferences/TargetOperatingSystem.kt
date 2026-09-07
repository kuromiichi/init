package dev.kuromiichi.damprep.data.preferences

enum class TargetOperatingSystem(
    val label: String,
) {
    Windows("Windows"),
    MacOS("macOS"),
    Linux("Linux"),
    ;

    companion object {
        fun detect(osName: String = System.getProperty("os.name").orEmpty()): TargetOperatingSystem? {
            val normalizedName = osName.lowercase()

            return when {
                normalizedName.contains("mac") || normalizedName.contains("darwin") -> MacOS
                normalizedName.contains("win") -> Windows
                normalizedName.contains("linux") -> Linux
                else -> null
            }
        }
    }
}
