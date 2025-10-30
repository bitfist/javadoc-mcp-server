package io.github.bitfist.javadoc_mcp_server.javadoc.internal.platform.graalvm;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ConcurrentHashMap;

import static org.assertj.core.api.Assertions.assertThat;

class FlexmarkUniverseLoaderTest {

    @BeforeEach
    void setUp() {
        FlexmarkUniverseLoader.enumUniverseMap = new ConcurrentHashMap<>();
    }

    @Test
    void testWithEmptyEnum() {
        // when
        Enum[] result = FlexmarkUniverseLoader.getUniverseSlow(EmptyEnum.class);

        // then
        assertThat(result).isEmpty();
    }

    @Test
    void testWithTestEnum() {
        // when
        Enum[] result = FlexmarkUniverseLoader.getUniverseSlow(TestEnum.class);

        // then
        assertThat(result).hasSize(3);
    }

    @Test
    void testCacheHit() {
        FlexmarkUniverseLoader.getUniverseSlow(TestEnum.class);
        FlexmarkUniverseLoader.getUniverseSlow(TestEnum.class);
    }

    enum EmptyEnum {}

    enum TestEnum {
        A, B, C;
    }
}