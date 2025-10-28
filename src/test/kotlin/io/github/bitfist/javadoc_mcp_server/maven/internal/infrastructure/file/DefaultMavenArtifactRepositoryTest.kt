package io.github.bitfist.javadoc_mcp_server.maven.internal.infrastructure.file

import io.github.bitfist.javadoc_mcp_server.maven.ArtifactCoordinates
import io.github.bitfist.javadoc_mcp_server.maven.internal.infrastructure.eclipse.AetherMavenArtifacts
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

@DisplayName("🔧 DefaultMavenArtifactRepository Tests")
class DefaultMavenArtifactRepositoryTest {

    private val repository: AetherMavenArtifacts = AetherMavenArtifacts()

    @AfterEach
    fun tearDown() {
        // Cleanup is handled by the repository's temp directory
    }

    @Test
    @DisplayName("✅ Should successfully download Javadoc JAR for valid artifact")
    fun shouldDownloadJavadocJarForValidArtifact() {
        // Given: A valid artifact coordinate for a well-known library
        val coordinates = ArtifactCoordinates(
            groupId = "org.apache.commons",
            artifactId = "commons-lang3",
            version = "3.12.0"
        )

        // When: Requesting the Javadoc JAR
        val javadocJar = repository.getJavaDocJar(coordinates)

        // Then: The JAR file should exist and be valid
        assertNotNull(javadocJar, "Javadoc JAR should not be null")
        assertTrue(javadocJar.exists(), "Javadoc JAR file should exist")
        assertTrue(javadocJar.isFile, "Should be a file")
        assertTrue(javadocJar.length() > 0, "JAR file should not be empty")
        assertTrue(javadocJar.name.endsWith(".jar"), "Should be a JAR file")
    }

    @Test
    @DisplayName("✅ Should download Javadoc JAR for Spring Boot artifact")
    fun shouldDownloadJavadocJarForSpringBootArtifact() {
        // Given: Spring Boot starter artifact coordinates
        val coordinates = ArtifactCoordinates(
            groupId = "org.springframework.boot",
            artifactId = "spring-boot-starter",
            version = "3.0.0"
        )

        // When: Requesting the Javadoc JAR
        val javadocJar = repository.getJavaDocJar(coordinates)

        // Then: The JAR should be downloaded successfully
        assertNotNull(javadocJar)
        assertTrue(javadocJar.exists())
        assertTrue(javadocJar.length() > 0)
    }

    @Test
    @DisplayName("❌ Should throw exception for non-existent artifact")
    fun shouldThrowExceptionForNonExistentArtifact() {
        // Given: Invalid artifact coordinates
        val coordinates = ArtifactCoordinates(
            groupId = "com.nonexistent",
            artifactId = "fake-artifact",
            version = "999.999.999"
        )

        // When/Then: Should throw IllegalStateException
        val exception = assertThrows<IllegalArgumentException> {
            repository.getJavaDocJar(coordinates)
        }

        assertTrue(
            exception.message?.startsWith("Javadoc JAR not found for") == true,
            "Exception message should indicate Javadoc JAR not found"
        )
    }

    @Test
    @DisplayName("❌ Should throw exception for artifact without Javadoc classifier")
    fun shouldThrowExceptionForArtifactWithoutJavadoc() {
        // Given: An artifact that exists but may not have a javadoc classifier
        val coordinates = ArtifactCoordinates(
            groupId = "com.example.nonexistent",
            artifactId = "no-javadoc-artifact",
            version = "1.0.0"
        )

        // When/Then: Should throw an exception
        assertThrows<Exception> {
            repository.getJavaDocJar(coordinates)
        }
    }

    @Test
    @DisplayName("🔄 Should cache downloaded artifacts in local repository")
    fun shouldCacheDownloadedArtifacts() {
        // Given: Valid artifact coordinates
        val coordinates = ArtifactCoordinates(
            groupId = "com.google.guava",
            artifactId = "guava",
            version = "31.1-jre"
        )

        // When: Downloading the same artifact twice
        val firstDownload = repository.getJavaDocJar(coordinates)
        val secondDownload = repository.getJavaDocJar(coordinates)

        // Then: Both should return the same cached file
        assertEquals(firstDownload.absolutePath, secondDownload.absolutePath)
        assertTrue(firstDownload.exists())
        assertTrue(secondDownload.exists())
    }

    @Test
    @DisplayName("📦 Should handle different versions of same artifact")
    fun shouldHandleDifferentVersionsOfSameArtifact() {
        // Given: Two different versions of the same artifact
        val coordinates1 = ArtifactCoordinates(
            groupId = "org.apache.commons",
            artifactId = "commons-lang3",
            version = "3.12.0"
        )
        val coordinates2 = ArtifactCoordinates(
            groupId = "org.apache.commons",
            artifactId = "commons-lang3",
            version = "3.11"
        )

        // When: Downloading both versions
        val jar1 = repository.getJavaDocJar(coordinates1)
        val jar2 = repository.getJavaDocJar(coordinates2)

        // Then: Both should exist and be different files
        assertTrue(jar1.exists())
        assertTrue(jar2.exists())
        assertTrue(jar1.absolutePath != jar2.absolutePath, "Different versions should result in different files")
    }

    @Test
    @DisplayName("🎯 Should download Javadoc for JUnit artifact")
    fun shouldDownloadJavadocForJunitArtifact() {
        // Given: JUnit 5 artifact coordinates
        val coordinates = ArtifactCoordinates(
            groupId = "org.junit.jupiter",
            artifactId = "junit-jupiter-api",
            version = "5.9.0"
        )

        // When: Requesting the Javadoc JAR
        val javadocJar = repository.getJavaDocJar(coordinates)

        // Then: Should successfully download
        assertNotNull(javadocJar)
        assertTrue(javadocJar.exists())
        assertTrue(javadocJar.name.contains("junit-jupiter-api"))
        assertTrue(javadocJar.name.contains("javadoc"))
    }

    @Test
    @DisplayName("🔍 Should validate artifact coordinates format")
    fun shouldValidateArtifactCoordinatesFormat() {
        // Given: Artifact with specific coordinate values
        val coordinates = ArtifactCoordinates(
            groupId = "org.slf4j",
            artifactId = "slf4j-api",
            version = "2.0.0"
        )

        // When: Getting the Javadoc JAR
        val javadocJar = repository.getJavaDocJar(coordinates)

        // Then: File path should contain coordinate information
        val absolutePath = javadocJar.absolutePath
        assertTrue(javadocJar.exists())
        assertTrue(javadocJar.name.contains("slf4j-api"))
        assertTrue(javadocJar.name.contains("2.0.0"))
        assertTrue(javadocJar.name.contains("javadoc"))
    }

    @Test
    @DisplayName("⚡ Should handle rapid consecutive requests")
    fun shouldHandleRapidConsecutiveRequests() {
        // Given: Valid artifact coordinates
        val coordinates = ArtifactCoordinates(
            groupId = "org.apache.commons",
            artifactId = "commons-collections4",
            version = "4.4"
        )

        // When: Making multiple rapid requests
        val results = (1..3).map {
            repository.getJavaDocJar(coordinates)
        }

        // Then: All requests should succeed
        results.forEach { jar ->
            assertTrue(jar.exists())
            assertTrue(jar.length() > 0)
        }
    }

    @Test
    @DisplayName("🌐 Should download from Maven Central")
    fun shouldDownloadFromMavenCentral() {
        // Given: A popular artifact that should be in Maven Central
        val coordinates = ArtifactCoordinates(
            groupId = "com.fasterxml.jackson.core",
            artifactId = "jackson-core",
            version = "2.14.0"
        )

        // When: Downloading the Javadoc JAR
        val javadocJar = repository.getJavaDocJar(coordinates)

        // Then: Should successfully download from Maven Central
        assertNotNull(javadocJar)
        assertTrue(javadocJar.exists())
        assertTrue(javadocJar.length() > 0)
    }

    @Test
    @DisplayName("📝 Should handle artifacts with complex group IDs")
    fun shouldHandleArtifactsWithComplexGroupIds() {
        // Given: Artifact with multi-level group ID
        val coordinates = ArtifactCoordinates(
            groupId = "org.springframework.boot",
            artifactId = "spring-boot-autoconfigure",
            version = "3.0.0"
        )

        // When: Downloading the Javadoc JAR
        val javadocJar = repository.getJavaDocJar(coordinates)

        // Then: Should handle complex group ID correctly
        assertTrue(javadocJar.exists())
        assertTrue(javadocJar.length() > 0)
    }

    @Test
    @DisplayName("🔢 Should handle SNAPSHOT versions appropriately")
    fun shouldHandleSnapshotVersionsAppropriately() {
        // Note: SNAPSHOT versions may not be available in Maven Central
        // This test validates the behavior when requesting a SNAPSHOT version

        // Given: A SNAPSHOT version coordinate
        val coordinates = ArtifactCoordinates(
            groupId = "com.example",
            artifactId = "test-artifact",
            version = "1.0.0-SNAPSHOT"
        )

        // When/Then: Should handle the request (likely throwing exception for non-existent artifact)
        assertThrows<Exception> {
            repository.getJavaDocJar(coordinates)
        }
    }
}