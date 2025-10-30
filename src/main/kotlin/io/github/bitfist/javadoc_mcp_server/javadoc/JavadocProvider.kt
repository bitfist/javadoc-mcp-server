package io.github.bitfist.javadoc_mcp_server.javadoc

import io.github.bitfist.javadoc_mcp_server.maven.MavenArtifactCoordinates

interface JavadocProvider {

    fun getJavadoc(mavenArtifactCoordinates: MavenArtifactCoordinates, fullyQualifiedClassName: String) : String

}