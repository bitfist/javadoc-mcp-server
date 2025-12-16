import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import java.net.URI

plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.spring)
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.graalvm)
    id("io.github.bitfist.gradle-github-support.release")
    idea
}

// region Setup

group = "io.github.bitfist"
description = "MCP Server for Javadoc"

idea {
    module {
        isDownloadJavadoc = true
        isDownloadSources = true
    }
}

// endregion

// region Build

repositories {
    mavenLocal()
    mavenCentral()
    maven(url = "https://oss.sonatype.org/content/repositories/snapshots")
}

dependencies {
    // region BOMs
    implementation(platform(org.springframework.boot.gradle.plugin.SpringBootPlugin.BOM_COORDINATES))
    implementation(platform(libs.spring.ai.bom))
    implementation(platform(libs.spring.modulith.bom))
    // endregion

    // region Kotlin specific
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation(libs.kotlin.logging)
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    // endregion

    // region Spring
    implementation("org.springframework.modulith:spring-modulith-starter-core")
    implementation("org.springframework.ai:spring-ai-starter-mcp-server")
    implementation("org.springframework.ai:spring-ai-starter-mcp-server-webflux")
    // endregion

    // region Converter
    implementation(libs.converter.html2markdown)
    // endregion

    // region Maven resolver
    implementation(libs.maven.resolver.provider)
    implementation(libs.maven.resolver.supplier)
    // region

    // region Test
    testImplementation(libs.test.assertj)
    testImplementation(libs.test.mockitoKotlin)
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.modulith:spring-modulith-starter-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    // endregion
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(25)
    }
    sourceCompatibility = JavaVersion.VERSION_25
}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict")
        jvmTarget.set(JvmTarget.JVM_25)
    }
}

springBoot {
    buildInfo()
}

tasks.bootBuildImage {
    environment.put("BPE_SPRING_PROFILES_ACTIVE", "container")
    environment.put("BP_NATIVE_IMAGE_BUILD_ARGUMENTS", "--features=io.github.bitfist.javadoc_mcp_server.javadoc.internal.platform.graalvm.FlexmarkSubstitutionsFeature")
}

graalvmNative {
    toolchainDetection.set(true)
}

tasks.withType<Test> {
    useJUnitPlatform()
}

// endregion

// region Release

gitHubRelease {
    projectName.set("Javadoc MCP Server")
    projectDescription.set("MCP Server for Javadoc")
    developer.set("bitfist")
    licenseFile.set(projectDir.resolve("LICENSE.txt"))
    license.set("The Apache License, Version 2.0")
    licenseUri.set(URI("https://www.apache.org/licenses/LICENSE-2.0"))
}

// endregion