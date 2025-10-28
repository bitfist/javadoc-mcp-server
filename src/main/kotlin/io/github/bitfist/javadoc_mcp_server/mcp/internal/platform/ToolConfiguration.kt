package io.github.bitfist.javadoc_mcp_server.mcp.internal.platform

import io.github.bitfist.javadoc_mcp_server.mcp.ToolProvider
import org.springframework.ai.tool.ToolCallbackProvider
import org.springframework.ai.tool.method.MethodToolCallbackProvider
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
private class ToolConfiguration {

    @Bean
    fun toolCallbackProvider(toolProviders: List<ToolProvider>): ToolCallbackProvider {
        return MethodToolCallbackProvider.builder()
            .toolObjects(*toolProviders.toTypedArray())
            .build()
    }

}