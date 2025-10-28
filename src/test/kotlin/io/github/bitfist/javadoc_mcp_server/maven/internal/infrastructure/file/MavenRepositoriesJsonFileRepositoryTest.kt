package io.github.bitfist.javadoc_mcp_server.maven.internal.infrastructure.file

import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import java.io.File
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@DisplayName("📄 MavenRepositoriesJsonFileRepository")
class MavenRepositoriesJsonFileRepositoryTest {

    @TempDir
    lateinit var tempDir: File

    @Test
    @DisplayName("✅ Should load repositories from valid JSON file")
    fun shouldLoadRepositoriesFromValidJsonFile() {
        // Given
        val configFile = File(tempDir, "repositories.json")
        configFile.writeText("""
            {
                "central": {
                    "url": "https://repo.maven.apache.org/maven2"
                },
                "private": {
                    "url": "https://private.repo.com/maven",
                    "username": "admin",
                    "password": "secret"
                }
            }
        """.trimIndent())

        // When
        val repository = MavenRepositoriesJsonFileRepository(configFile.absolutePath)
        val repositories = repository.getAll()

        // Then
        assertEquals(2, repositories.size)

        val central = repositories.find { it.name == "central" }!!
        assertEquals("central", central.name)
        assertEquals("https://repo.maven.apache.org/maven2", central.url)
        assertEquals(null, central.username)
        assertEquals(null, central.password)

        val private = repositories.find { it.name == "private" }!!
        assertEquals("private", private.name)
        assertEquals("https://private.repo.com/maven", private.url)
        assertEquals("admin", private.username)
        assertEquals("secret", private.password)
    }

    @Test
    @DisplayName("📭 Should return empty list when configuration file does not exist")
    fun shouldReturnEmptyListWhenFileDoesNotExist() {
        // Given
        val nonExistentPath = File(tempDir, "non-existent.json").absolutePath

        // When
        val repository = MavenRepositoriesJsonFileRepository(nonExistentPath)
        val repositories = repository.getAll()

        // Then
        assertTrue(repositories.isEmpty())
    }

    @Test
    @DisplayName("⚠️ Should return empty list when JSON is invalid")
    fun shouldReturnEmptyListWhenJsonIsInvalid() {
        // Given
        val configFile = File(tempDir, "invalid.json")
        configFile.writeText("{ invalid json content")

        // When
        val repository = MavenRepositoriesJsonFileRepository(configFile.absolutePath)
        val repositories = repository.getAll()

        // Then
        assertTrue(repositories.isEmpty())
    }

    @Test
    @DisplayName("🔍 Should return empty list when JSON is empty object")
    fun shouldReturnEmptyListWhenJsonIsEmptyObject() {
        // Given
        val configFile = File(tempDir, "empty.json")
        configFile.writeText("{}")

        // When
        val repository = MavenRepositoriesJsonFileRepository(configFile.absolutePath)
        val repositories = repository.getAll()

        // Then
        assertTrue(repositories.isEmpty())
    }

    @Test
    @DisplayName("🔐 Should handle repository without credentials")
    fun shouldHandleRepositoryWithoutCredentials() {
        // Given
        val configFile = File(tempDir, "repositories.json")
        configFile.writeText("""
            {
                "public": {
                    "url": "https://public.repo.com/maven"
                }
            }
        """.trimIndent())

        // When
        val repository = MavenRepositoriesJsonFileRepository(configFile.absolutePath)
        val repositories = repository.getAll()

        // Then
        assertEquals(1, repositories.size)
        val public = repositories[0]
        assertEquals("public", public.name)
        assertEquals("https://public.repo.com/maven", public.url)
        assertEquals(null, public.username)
        assertEquals(null, public.password)
    }

    @Test
    @DisplayName("🔢 Should handle multiple repositories")
    fun shouldHandleMultipleRepositories() {
        // Given
        val configFile = File(tempDir, "repositories.json")
        configFile.writeText("""
            {
                "repo1": { "url": "https://repo1.com" },
                "repo2": { "url": "https://repo2.com" },
                "repo3": { "url": "https://repo3.com" }
            }
        """.trimIndent())

        // When
        val repository = MavenRepositoriesJsonFileRepository(configFile.absolutePath)
        val repositories = repository.getAll()

        // Then
        assertEquals(3, repositories.size)
        assertTrue(repositories.any { it.name == "repo1" })
        assertTrue(repositories.any { it.name == "repo2" })
        assertTrue(repositories.any { it.name == "repo3" })
    }
}