package io.github.bitfist.javadoc_mcp_server.javadoc.internal.infrastructure.file

import io.github.bitfist.javadoc_mcp_server.javadoc.JavadocProvider
import io.github.bitfist.javadoc_mcp_server.maven.ArtifactCoordinates
import io.github.bitfist.javadoc_mcp_server.maven.MavenArtifactRepository
import org.springframework.stereotype.Component
import java.io.File
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.util.zip.ZipInputStream
import kotlin.io.path.exists
import kotlin.io.path.readText

@Component
private class DefaultJavadocProvider(
    private val mavenArtifactRepository: MavenArtifactRepository,
    private val configurationProperties: JavadocProviderConfigurationProperties
) : JavadocProvider {

    override fun getJavadoc(
        artifactCoordinates: ArtifactCoordinates,
        fullyQualifiedClassName: String
    ): String {
        val artifactFolder = Paths.get(
            configurationProperties.repositoryPath,
            artifactCoordinates.groupId,
            artifactCoordinates.artifactId,
            artifactCoordinates.version
        )

        // If the artifact folder doesn't exist, download and extract the Javadoc jar
        if (!artifactFolder.exists()) {
            val javaDocJar = mavenArtifactRepository.getJavaDocJar(artifactCoordinates)
            extractJavaDocJar(javaDocJar, artifactFolder)
        }

        // Find the JavaDoc file for the specified class
        val packagePath = fullyQualifiedClassName.substringBeforeLast(".").replace(".", "/")
        val simpleClassName = fullyQualifiedClassName.substringAfterLast(".")
        val javaDocFile = artifactFolder.resolve("$packagePath/$simpleClassName.html")

        if (!javaDocFile.exists()) {
            return "Javadoc not found for class $fullyQualifiedClassName in artifact $artifactCoordinates"
        }

        return javaDocFile.readText()
    }

    private fun extractJavaDocJar(jarPath: File, destinationFolder: Path) {
        Files.createDirectories(destinationFolder)

        jarPath.inputStream().use { fileInputStream ->
            ZipInputStream(fileInputStream).use { zipInputStream ->
                var entry = zipInputStream.nextEntry
                while (entry != null) {
                    val entryPath = destinationFolder.resolve(entry.name)
                    if (entry.isDirectory) {
                        Files.createDirectories(entryPath)
                    } else {
                        Files.createDirectories(entryPath.parent)
                        Files.copy(zipInputStream, entryPath)
                    }
                    zipInputStream.closeEntry()
                    entry = zipInputStream.nextEntry
                }
            }
        }
    }

}