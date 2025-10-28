package io.github.bitfist.javadoc_mcp_server.javadoc.internal.presentation.mcp

import com.vladsch.flexmark.html2md.converter.FlexmarkHtmlConverter
import io.github.bitfist.javadoc_mcp_server.javadoc.JavadocProvider
import io.github.bitfist.javadoc_mcp_server.maven.ArtifactCoordinates
import io.github.bitfist.javadoc_mcp_server.mcp.ToolProvider
import org.springframework.ai.tool.annotation.Tool
import org.springframework.ai.tool.annotation.ToolParam
import org.springframework.stereotype.Service

@Service
internal class JavadocTool(private val javadocProvider: JavadocProvider) : ToolProvider {

    private val html2markdownConverter = FlexmarkHtmlConverter.builder().build()

    @Tool(
        name = "javadoc",
        description = "Get JavaDoc for a given artifact coordinates and fully qualified class name"
    )
    fun getJavadoc(
        @ToolParam(description = "Maven Group ID") groupId: String,
        @ToolParam(description = "Maven Artifact ID") artifactId: String,
        @ToolParam(description = "Maven Artifact Version") version: String,
        @ToolParam(description = "Fully qualified name of the class the JavaDoc should be retrieved for") fullyQualifiedClassName: String
    ): String {
        val html = javadocProvider.getJavadoc(ArtifactCoordinates(groupId, artifactId, version), fullyQualifiedClassName)
        return html2markdownConverter.convert(html)
    }

}