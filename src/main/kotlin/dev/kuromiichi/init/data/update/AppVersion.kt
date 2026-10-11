package dev.kuromiichi.init.data.update

import java.util.Properties

/** Versión semántica `MAJOR.MINOR.PATCH` de una release; la `v` inicial de los tags es opcional. */
data class SemanticVersion(
    val major: Int,
    val minor: Int,
    val patch: Int,
) : Comparable<SemanticVersion> {
    /** Mientras la versión mayor sea 0 la app se presenta como beta. */
    val isBeta: Boolean = major == 0

    override fun compareTo(other: SemanticVersion): Int = compareValuesBy(this, other, { it.major }, { it.minor }, { it.patch })

    override fun toString(): String = "$major.$minor.$patch"

    companion object {
        private val Pattern = Regex("""v?(\d+)\.(\d+)\.(\d+)""")

        fun parse(text: String): SemanticVersion? =
            Pattern.matchEntire(text.trim())?.destructured?.let { (major, minor, patch) ->
                SemanticVersion(major.toInt(), minor.toInt(), patch.toInt())
            }
    }
}

/**
 * Versión con la que se ha compilado la app. La escribe Gradle en `init-version.properties`; las ejecuciones de
 * desarrollo no tienen [release].
 */
data class AppVersion(
    val release: SemanticVersion?,
    val commit: String,
) {
    val label: String =
        buildString {
            append(release?.toString() ?: "dev")
            if (commit.isNotBlank()) append(" ($commit)")
        }

    val isBeta: Boolean = release?.isBeta ?: true

    companion object {
        private const val RESOURCE_NAME = "/init-version.properties"

        fun current(): AppVersion {
            val properties = Properties()
            AppVersion::class.java.getResourceAsStream(RESOURCE_NAME)?.use(properties::load)

            return AppVersion(
                release = properties.getProperty("version")?.let(SemanticVersion::parse),
                commit = properties.getProperty("commit").orEmpty(),
            )
        }
    }
}
