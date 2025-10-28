package io.github.bitfist.javadoc_mcp_server.javadoc.internal.presentation.mcp

import io.github.bitfist.javadoc_mcp_server.javadoc.JavadocProvider
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.mockito.kotlin.any

class JavadocToolTest {

    private val javadocProvider: JavadocProvider = mock(JavadocProvider::class.java)

    private val target = JavadocTool(javadocProvider)

    @Test
    fun testGetJavadoc() {
        // given
        `when`(javadocProvider.getJavadoc(any(), any())).thenReturn("payload")
        
        // when
        val javadocMarkdown = target.getJavadoc("test", "test", "test", "test")
        
        // then
        assertThat(javadocMarkdown).isEqualTo("payload\n")
    }

}