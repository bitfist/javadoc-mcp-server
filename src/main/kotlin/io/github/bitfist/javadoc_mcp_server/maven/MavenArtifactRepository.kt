package io.github.bitfist.javadoc_mcp_server.maven

import java.io.File

interface MavenArtifactRepository {

    fun getJavaDocJar(artifactCoordinates: ArtifactCoordinates) : File
}