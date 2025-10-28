package io.github.bitfist.javadoc_mcp_server.maven.internal.infrastructure.file

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.github.bitfist.javadoc_mcp_server.maven.MavenRepositories
import io.github.bitfist.javadoc_mcp_server.maven.MavenRepository
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Repository
import java.io.File

private val logger = KotlinLogging.logger {}

@Repository
internal class MavenRepositoriesJsonFileRepository(
    @Value("\${javadoc-mcp.repositories.configuration-file:#{systemProperties['user.home']}/.javadoc-mcp/repositories.json}") val configurationFilePath: String
) : MavenRepositories {

    private val mavenRepositories = parseConfiguration()

    override fun getAll() = mavenRepositories

    private fun parseConfiguration(): List<MavenRepository> {
        val configurationFile = File(configurationFilePath)
        if (!configurationFile.exists()) {
            return emptyList()
        }

        try {
            val payload = ObjectMapper().readValue<Map<String, MavenRepositoryJsonPayload>>(configurationFile)
            return payload.map { MavenRepository(it.key, it.value.url, it.value.username, it.value.password) }
        } catch (t: Throwable) {
            logger.error(t) { "Failed to load Maven repositories from $configurationFilePath" }
            return emptyList()
        }
    }
}

private class MavenRepositoryJsonPayload {
    var url: String = ""
    var username: String? = null
    var password: String? = null
}