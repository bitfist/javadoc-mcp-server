package io.github.bitfist.javadoc_mcp_server.maven.internal.infrastructure.eclipse

import io.github.bitfist.javadoc_mcp_server.maven.ArtifactCoordinates
import io.github.bitfist.javadoc_mcp_server.maven.MavenArtifacts
import io.github.oshai.kotlinlogging.KotlinLogging
import org.apache.maven.repository.internal.MavenRepositorySystemUtils
import org.eclipse.aether.DefaultRepositorySystemSession
import org.eclipse.aether.RepositorySystem
import org.eclipse.aether.RepositorySystemSession
import org.eclipse.aether.artifact.DefaultArtifact
import org.eclipse.aether.connector.basic.BasicRepositoryConnectorFactory
import org.eclipse.aether.repository.LocalRepository
import org.eclipse.aether.repository.RemoteRepository
import org.eclipse.aether.resolution.ArtifactRequest
import org.eclipse.aether.resolution.ArtifactResolutionException
import org.eclipse.aether.resolution.ArtifactResult
import org.eclipse.aether.spi.connector.RepositoryConnectorFactory
import org.eclipse.aether.spi.connector.transport.TransporterFactory
import org.eclipse.aether.supplier.RepositorySystemSupplier
import org.eclipse.aether.transport.file.FileTransporterFactory
import org.eclipse.aether.transport.http.HttpTransporterFactory
import org.eclipse.aether.util.repository.SimpleArtifactDescriptorPolicy
import org.eclipse.aether.util.repository.SimpleResolutionErrorPolicy
import org.springframework.stereotype.Repository
import java.io.File
import java.nio.file.Files

private val logger = KotlinLogging.logger {}

@Repository
internal class AetherMavenArtifacts : MavenArtifacts {

    private val localRepositoryDirectory: File = Files.createTempDirectory("maven-javadoc").toFile()
    private val system: RepositorySystem = newRepositorySystem()
    private val session: RepositorySystemSession = newRepositorySystemSession(system, localRepositoryDirectory)
    private val repositories = listOf(
        RemoteRepository.Builder("central", "default", "https://repo1.maven.org/maven2/").build(),
        RemoteRepository.Builder("local", "default", "file://${System.getProperty("user.home")}/.m2/repository").build()
    )

    /**
     * Downloads the Javadoc JAR for a given artifact coordinate.
     * Returns the local file path of the downloaded JAR.
     */
    override fun getJavaDocJar(artifactCoordinates: ArtifactCoordinates): File {
        val groupId = artifactCoordinates.groupId
        val artifactId = artifactCoordinates.artifactId
        val version = artifactCoordinates.version

        val javadocArtifact = DefaultArtifact("$groupId:$artifactId:jar:javadoc:$version")
        val request = ArtifactRequest(javadocArtifact, repositories, null)

        try {
            val result: ArtifactResult = system.resolveArtifact(session, request)
            logger.info { "Downloaded Javadoc JAR for $artifactCoordinates to ${result.artifact.file}" }
            return result.artifact.file
        } catch (_: ArtifactResolutionException) {
            logger.error { "Failed to download Javadoc JAR for $artifactCoordinates" }
            throw IllegalArgumentException("Javadoc JAR not found for $groupId:$artifactId:$version")
        }
    }

    private fun newRepositorySystem(): RepositorySystem {
        val locator = MavenRepositorySystemUtils.newServiceLocator()
        locator.addService(RepositoryConnectorFactory::class.java, BasicRepositoryConnectorFactory::class.java)
        locator.addService(TransporterFactory::class.java, FileTransporterFactory::class.java)
        locator.addService(TransporterFactory::class.java, HttpTransporterFactory::class.java)
        return RepositorySystemSupplier().get()
    }

    private fun newRepositorySystemSession(system: RepositorySystem, localRepoDir: File): RepositorySystemSession {
        val session = DefaultRepositorySystemSession()
        val localRepo = LocalRepository(localRepoDir)
        session.localRepositoryManager = system.newLocalRepositoryManager(session, localRepo)
        session.setArtifactDescriptorPolicy(SimpleArtifactDescriptorPolicy(true, true))
        session.setResolutionErrorPolicy(SimpleResolutionErrorPolicy(true, true))
        return session
    }
}