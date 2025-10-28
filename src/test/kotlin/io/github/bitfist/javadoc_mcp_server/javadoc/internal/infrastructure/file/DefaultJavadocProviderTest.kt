package io.github.bitfist.javadoc_mcp_server.javadoc.internal.infrastructure.file

import io.github.bitfist.javadoc_mcp_server.JavadocMcpConfiguration
import io.github.bitfist.javadoc_mcp_server.javadoc.JavadocProvider
import io.github.bitfist.javadoc_mcp_server.maven.ArtifactCoordinates
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.io.File

@SpringBootTest
private class DefaultJavadocProviderTest {

    @Autowired
    private lateinit var target: JavadocProvider

    @Autowired
    private lateinit var javaDocMcpConfiguration: JavadocMcpConfiguration

    @BeforeEach
    fun setup() {
        File(javaDocMcpConfiguration.localRepository).deleteRecursively()
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

}