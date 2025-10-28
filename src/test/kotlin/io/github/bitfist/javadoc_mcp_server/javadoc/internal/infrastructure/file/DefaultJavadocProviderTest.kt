package io.github.bitfist.javadoc_mcp_server.javadoc.internal.infrastructure.file

import io.github.bitfist.javadoc_mcp_server.javadoc.JavadocProvider
import io.github.bitfist.javadoc_mcp_server.maven.ArtifactCoordinates
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.bean.override.mockito.MockitoBean
import java.nio.file.Files

@SpringBootTest
private class DefaultJavadocProviderTest {

    private val repositoryPath = Files.createTempDirectory("javadoc-mcp").toFile().absolutePath

    @Autowired
    private lateinit var target: JavadocProvider

    @MockitoBean
    private lateinit var javadocProviderConfigurationProperties : JavadocProviderConfigurationProperties

    @BeforeEach
    fun setup() {
        `when`(javadocProviderConfigurationProperties.repositoryPath).thenReturn(repositoryPath)
    }

    @Test
    fun testGetJavaDoc() {
        // given
        val artifactCoordinates = ArtifactCoordinates("org.jspecify", "jspecify", "1.0.0")
        val file = "org.jspecify.annotations.Nullable"

        // when
        val javaDoc = target.getJavadoc(artifactCoordinates, file)

        // then
        assertThat(javaDoc).doesNotStartWith("JavaDoc not found for class")
    }

    @Test
    fun testWithInvalidCoordinates() {
        // given
        val artifactCoordinates = ArtifactCoordinates("org.jspecify", "jspecify", "1.0.0")
        val file = "org.jspecify.annotations.Nullable"

        // when
        val javaDoc = target.getJavadoc(artifactCoordinates, file)

        // then
        assertThat(javaDoc).doesNotStartWith("JavaDoc not found for class")
    }

    @Test
    fun testWithValidArtifactAndInvalidFile() {
        // given
        val artifactCoordinates = ArtifactCoordinates("org.jspecify", "jspecify", "1.0.0")
        val file = "org.jspecify.annotations.blah"

        // when
        val javaDoc = target.getJavadoc(artifactCoordinates, file)

        // then
        assertThat(javaDoc).doesNotStartWith("JavaDoc not found for class")
    }

}