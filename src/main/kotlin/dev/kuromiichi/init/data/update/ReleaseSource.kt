package dev.kuromiichi.init.data.update

import kotlinx.serialization.json.Json
import java.io.File
import java.io.IOException
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.time.Duration

/** Origen de las releases publicadas. */
interface ReleaseSource {
    /** Última release publicada, o `null` si aún no hay ninguna. */
    fun latestRelease(): GitHubRelease?

    fun readText(url: String): String

    /** Descarga [url] en [target] e informa de los bytes recibidos hasta el momento. */
    fun download(
        url: String,
        target: File,
        onProgress: (bytesRead: Long) -> Unit,
    )
}

/** Releases del repositorio público de init en GitHub, consultadas sin autenticación. Las llamadas bloquean. */
class GitHubReleaseSource(
    private val repository: String = "kuromiichi/init",
    private val client: HttpClient =
        HttpClient
            .newBuilder()
            .connectTimeout(Duration.ofSeconds(10))
            // Los archivos de una release redirigen a otro dominio.
            .followRedirects(HttpClient.Redirect.NORMAL)
            .build(),
) : ReleaseSource {
    private val json = Json { ignoreUnknownKeys = true }

    override fun latestRelease(): GitHubRelease? {
        val response =
            client.send(
                request("https://api.github.com/repos/$repository/releases/latest")
                    .header("Accept", "application/vnd.github+json")
                    .build(),
                HttpResponse.BodyHandlers.ofString(),
            )

        return when (response.statusCode()) {
            200 -> json.decodeFromString<GitHubRelease>(response.body())
            404 -> null
            else -> throw IOException("GitHub ha respondido con el código ${response.statusCode()}.")
        }
    }

    override fun readText(url: String): String {
        val response = client.send(request(url).build(), HttpResponse.BodyHandlers.ofString())
        response.requireSuccess()
        return response.body()
    }

    override fun download(
        url: String,
        target: File,
        onProgress: (bytesRead: Long) -> Unit,
    ) {
        val response = client.send(request(url).build(), HttpResponse.BodyHandlers.ofInputStream())
        response.body().use { input ->
            response.requireSuccess()
            target.outputStream().use { output ->
                val buffer = ByteArray(DEFAULT_BUFFER_SIZE * 8)
                var bytesRead = 0L
                while (true) {
                    if (Thread.currentThread().isInterrupted) throw IOException("Descarga cancelada.")
                    val count = input.read(buffer)
                    if (count < 0) break
                    output.write(buffer, 0, count)
                    bytesRead += count
                    onProgress(bytesRead)
                }
            }
        }
    }

    private fun request(url: String): HttpRequest.Builder =
        HttpRequest
            .newBuilder(URI.create(url))
            .timeout(Duration.ofSeconds(30))
            .header("User-Agent", "init-updater")

    private fun HttpResponse<*>.requireSuccess() {
        if (statusCode() !in 200..299) throw IOException("La descarga ha respondido con el código ${statusCode()}.")
    }
}
