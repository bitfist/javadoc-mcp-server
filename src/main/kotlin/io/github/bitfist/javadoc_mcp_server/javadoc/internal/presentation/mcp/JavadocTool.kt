package io.github.bitfist.javadoc_mcp_server.javadoc.internal.presentation.mcp

import io.github.bitfist.javadoc_mcp_server.javadoc.JavadocConverter
import io.github.bitfist.javadoc_mcp_server.javadoc.JavadocNotFoundException
import io.github.bitfist.javadoc_mcp_server.javadoc.JavadocProvider
import io.github.bitfist.javadoc_mcp_server.maven.MavenArtifactCoordinates
import io.github.bitfist.javadoc_mcp_server.maven.MavenArtifactNotFoundException
import io.github.bitfist.javadoc_mcp_server.mcp.ToolProvider
import org.springframework.ai.tool.annotation.Tool
import org.springframework.ai.tool.annotation.ToolParam
import org.springframework.stereotype.Service

@Service
internal class JavadocTool(
    private val javadocProvider: JavadocProvider,
    private val javadocConverter: JavadocConverter
) : ToolProvider {

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
        try {
            val html = javadocProvider.getJavadoc(MavenArtifactCoordinates(groupId, artifactId, version), fullyQualifiedClassName)
            return javadocConverter.convert(html)
        } catch (e: MavenArtifactNotFoundException) {
            return e.message!!
        } catch (e: JavadocNotFoundException) {
            return e.message!!
        }
    }

}