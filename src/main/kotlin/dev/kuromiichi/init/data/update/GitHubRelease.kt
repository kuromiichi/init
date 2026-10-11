package dev.kuromiichi.init.data.update

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** Campos que usa el actualizador de la respuesta de `GET /repos/{owner}/{repo}/releases/latest`. */
@Serializable
data class GitHubRelease(
    @SerialName("tag_name") val tagName: String,
    @SerialName("html_url") val htmlUrl: String,
    val body: String? = null,
    val draft: Boolean = false,
    val prerelease: Boolean = false,
    val assets: List<GitHubReleaseAsset> = emptyList(),
)

@Serializable
data class GitHubReleaseAsset(
    val name: String,
    @SerialName("browser_download_url") val downloadUrl: String,
    val size: Long = 0,
)

/** Versión publicada más nueva que la instalada y con instalador para este equipo. */
data class AvailableUpdate(
    val version: SemanticVersion,
    val releaseNotes: String,
    val releasePageUrl: String,
    val installer: GitHubReleaseAsset,
    val checksums: GitHubReleaseAsset?,
)

internal const val CHECKSUMS_ASSET_NAME = "SHA256SUMS"

/**
 * Devuelve la actualización si [release] es una versión publicada (ni borrador ni prerelease), más nueva que
 * [installed] y con instalador para [platform].
 */
internal fun GitHubRelease.toAvailableUpdate(
    installed: SemanticVersion,
    platform: UpdatePlatform,
): AvailableUpdate? {
    if (draft || prerelease) return null
    val version = SemanticVersion.parse(tagName)?.takeIf { it > installed } ?: return null
    val installer = assets.firstOrNull { it.name.endsWith("-${platform.assetSuffix}") } ?: return null

    return AvailableUpdate(
        version = version,
        releaseNotes = body.orEmpty().trim(),
        releasePageUrl = htmlUrl,
        installer = installer,
        checksums = assets.firstOrNull { it.name == CHECKSUMS_ASSET_NAME },
    )
}

/** Busca el hash de [fileName] en un archivo con el formato de `sha256sum`: `<hash>  <archivo>` por línea. */
internal fun parseSha256Sums(
    content: String,
    fileName: String,
): String? =
    content
        .lineSequence()
        .map { it.trim().split(Regex("""\s+\*?"""), limit = 2) }
        .firstOrNull { it.size == 2 && it[1] == fileName }
        ?.first()
        ?.lowercase()
