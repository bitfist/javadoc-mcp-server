package io.github.bitfist.javadoc_mcp_server.maven.internal.infrastructure.eclipse

import io.github.bitfist.javadoc_mcp_server.maven.MavenRepositories
import io.github.bitfist.javadoc_mcp_server.maven.MavenRepository
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.mockito.kotlin.verify

class AetherMavenArtifactsTest {

    private val mavenRepositories = mock<MavenRepositories>()

    @Test
    fun testConstructorWorksWithList() {
        // given
        val mavenRepository = MavenRepository("name", "http://local.host", "username", "password")
        `when`(mavenRepositories.getAll()).thenReturn(listOf(mavenRepository))

        // when
        AetherMavenArtifacts(mavenRepositories)

        // then
        verify(mavenRepositories).getAll()
    }

    @Test
    fun testConstructorWorksWithEmptyList() {
        // given
        `when`(mavenRepositories.getAll()).thenReturn(emptyList())

        // when
        AetherMavenArtifacts(mavenRepositories)

        // then
        verify(mavenRepositories).getAll()
    }
}