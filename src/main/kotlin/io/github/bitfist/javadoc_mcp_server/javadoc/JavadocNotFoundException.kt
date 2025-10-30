package io.github.bitfist.javadoc_mcp_server.javadoc

import io.github.bitfist.javadoc_mcp_server.maven.MavenArtifactCoordinates

class JavadocNotFoundException(mavenArtifactCoordinates: MavenArtifactCoordinates, fullyQualifiedClassName: String) : RuntimeException("Artifact $mavenArtifactCoordinates does not contain a Javadoc for $fullyQualifiedClassName")