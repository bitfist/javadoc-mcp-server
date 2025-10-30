package io.github.bitfist.javadoc_mcp_server.javadoc

interface JavadocConverter {

    fun convert(html: String) : String

}