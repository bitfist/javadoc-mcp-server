package io.github.bitfist.javadoc_mcp_server.maven

class MavenArtifactNotFoundException(mavenArtifactCoordinates: MavenArtifactCoordinates) : RuntimeException("Artifact $mavenArtifactCoordinates not found")