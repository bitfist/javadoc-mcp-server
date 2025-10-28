package io.github.bitfist.javadoc_mcp_server

import org.junit.jupiter.api.Test
import org.springframework.modulith.core.ApplicationModules
import org.springframework.modulith.docs.Documenter

class ArchitectureDocumentationTest {
    @Test
    fun writeDocumentationSnippets() {
        val modules = ApplicationModules.of(JavadocMcpServerApplication::class.java).verify();
        Documenter(modules)
            .writeDocumentation()
            .writeModulesAsPlantUml()
            .writeIndividualModulesAsPlantUml();
    }

}