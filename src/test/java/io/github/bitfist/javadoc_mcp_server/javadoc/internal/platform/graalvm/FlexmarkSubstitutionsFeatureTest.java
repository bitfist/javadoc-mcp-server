package io.github.bitfist.javadoc_mcp_server.javadoc.internal.platform.graalvm;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class FlexmarkSubstitutionsFeatureTest {

    @Test
    void testDescription() {
        assertThat(new FlexmarkSubstitutionsFeature().getDescription()).isNotBlank();
    }

}