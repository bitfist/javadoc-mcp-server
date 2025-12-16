package io.github.bitfist.javadoc_mcp_server.javadoc.internal.infrastructure.markdown

import com.vladsch.flexmark.html2md.converter.FlexmarkHtmlConverter
import io.github.bitfist.javadoc_mcp_server.javadoc.JavadocConverter
import org.springframework.stereotype.Service

@Service
private class MarkdownJavadocConverter : JavadocConverter {

    private val garbageMarker = "\nPackage ["

    private val html2markdownConverter = FlexmarkHtmlConverter.builder().build()

    override fun convert(html: String): String {
        val markdown = html2markdownConverter.convert(html)
        // remove garbage
        val cleanMarkdown = markdown.substring(markdown.indexOf(garbageMarker) + 1)
        return cleanMarkdown
    }

}