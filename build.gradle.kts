plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.spring)
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.graalvm)
    id("io.github.bitfist.gradle-github-support.release")
    idea
}

group = "io.github.bitfist"
version = "0.0.1-SNAPSHOT"
description = "MCP Server for JavaDoc"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(21)
	}
}

repositories {
    mavenLocal()
	mavenCentral()
}

extra["springAiVersion"] = "1.0.3"
extra["springModulithVersion"] = "1.4.3"

dependencies {
    // region BOMs
    implementation(platform(org.springframework.boot.gradle.plugin.SpringBootPlugin.BOM_COORDINATES))
    implementation(platform(libs.spring.ai.bom))
    implementation(platform(libs.spring.modulith.bom))
    // endregion

    // region Kotlin specific
    implementation("org.jetbrains.kotlin:kotlin-reflect")
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

    // region Maven
    implementation(libs.maven.resolver.provider)
    implementation(libs.maven.resolver.supplier)
    // region

    // region Test
    testImplementation(libs.test.assertj)
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.modulith:spring-modulith-starter-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    // endregion
}

idea {
    module {
        isDownloadJavadoc = true
        isDownloadSources = true
    }
}

kotlin {
	compilerOptions {
		freeCompilerArgs.addAll("-Xjsr305=strict")
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}


gitHubRelease {
    projectName.set("JCEF Spring Boot Starter")
    projectDescription.set("Spring Boot Starter for JCEF")
    developer.set("bitfist")
    licenseFile.set(projectDir.resolve("LICENSE.txt"))
    license.set("The Apache License, Version 2.0")
    licenseUri.set(URI("https://www.apache.org/licenses/LICENSE-2.0"))
}