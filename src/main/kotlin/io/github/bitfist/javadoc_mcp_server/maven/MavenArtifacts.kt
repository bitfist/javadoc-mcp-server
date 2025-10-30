package io.github.bitfist.javadoc_mcp_server.maven

import java.io.File

interface MavenArtifacts {

    fun getJavaDocJar(mavenArtifactCoordinates: MavenArtifactCoordinates) : File
}