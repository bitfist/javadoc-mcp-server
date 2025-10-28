package io.github.bitfist.javadoc_mcp_server

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication


@SpringBootApplication
@EnableConfigurationProperties(JavadocMcpConfiguration::class)
class JavadocMcpServerApplication

fun main(args: Array<String>) {
	runApplication<JavadocMcpServerApplication>(*args)
}