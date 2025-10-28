package io.github.bitfist.javadoc_mcp_server.javadoc.internal.infrastructure.file

import org.springframework.context.annotation.Configuration

@Configuration
internal class JavadocProviderConfigurationProperties {

    val repositoryPath = System.getProperty("user.home") + "/.javadoc-mcp/repository"

}