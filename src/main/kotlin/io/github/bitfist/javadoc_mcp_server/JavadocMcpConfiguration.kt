package io.github.bitfist.javadoc_mcp_server

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "javadoc-mcp")
class JavadocMcpConfiguration(
    val localRepository: String = System.getProperty("user.home") + "/.javadoc-mcp/repository"
)