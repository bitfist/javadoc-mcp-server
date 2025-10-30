package io.github.bitfist.javadoc_mcp_server.javadoc.internal.platform.graalvm;

import org.graalvm.nativeimage.hosted.Feature;

class FlexmarkSubstitutionsFeature implements Feature {

    @Override
    public String getDescription() {
        return "Flexmark substitutions for GraalVM native image";
    }

}
