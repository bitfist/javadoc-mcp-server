package io.github.bitfist.javadoc_mcp_server.javadoc.internal.presentation.mcp

import io.github.bitfist.javadoc_mcp_server.javadoc.internal.infrastructure.file.JavadocProviderConfigurationProperties
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.bean.override.mockito.MockitoBean
import java.nio.file.Files

@SpringBootTest
class JavadocToolIT {

    private val repositoryPath = Files.createTempDirectory("javadoc-mcp").toFile().absolutePath

    @Autowired
    private lateinit var target: JavadocTool

    @MockitoBean
    private lateinit var javadocProviderConfigurationProperties : JavadocProviderConfigurationProperties

    @BeforeEach
    fun setup() {
        `when`(javadocProviderConfigurationProperties.repositoryPath).thenReturn(repositoryPath)
    }

    @Test
    fun testGetJavadoc() {
        // when
        val javadocMarkdown = target.getJavadoc("org.jspecify", "jspecify", "1.0.0", "org.jspecify.annotations.Nullable")
        
        // then
        assertThat(javadocMarkdown).doesNotContain("Javadoc not found for class")
    }

}