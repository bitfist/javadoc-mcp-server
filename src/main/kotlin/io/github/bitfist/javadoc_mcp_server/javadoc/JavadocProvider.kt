package io.github.bitfist.javadoc_mcp_server.javadoc

import io.github.bitfist.javadoc_mcp_server.maven.ArtifactCoordinates

interface JavadocProvider {

    fun getJavadoc(artifactCoordinates: ArtifactCoordinates, fullyQualifiedClassName: String) : String

}