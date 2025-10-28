# 📚 Javadoc MCP Server

> A Spring AI-powered Model Context Protocol (MCP) server that provides AI assistants with seamless access to Java
> library documentation

[![Kotlin](https://img.shields.io/badge/Kotlin-1.9+-7F52FF.svg?logo=kotlin)](https://kotlinlang.org/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-6DB33F.svg?logo=spring)](https://spring.io/projects/spring-boot)
![License](https://img.shields.io/badge/License-Apache%20License%20Version%202.0-blue)
[![Gradle build](https://github.com/bitfist/javadoc-mcp-server/actions/workflows/test.yml/badge.svg)](https://github.com/bitfist/javadoc-mcp-server/actions/workflows/test.yml)
![Coverage](.github/badges/jacoco.svg)
![Branches](.github/badges/branches.svg)

## 🎯 What is this?

Javadoc MCP Server is a tool that bridges the gap between AI assistants and Java documentation. It automatically
fetches, extracts, and converts Javadoc documentation from Maven artifacts into markdown format, making it easy for AI
models to understand and explain Java APIs.

### Key Features

- 🔍 **Automatic Javadoc Retrieval** - Fetches Javadoc JARs directly from Maven Central or your local Maven repository
- 📦 **Smart Caching** - Downloads and extracts documentation once, then serves it from local storage
- 🔄 **HTML to Markdown Conversion** - Transforms Javadoc HTML into clean, AI-friendly markdown
- 🚀 **Spring AI Integration** - Seamlessly integrates with Spring AI's tool calling framework
- 🛠️ **MCP Protocol** - Implements the Model Context Protocol for standardized AI assistant integration

## 🚀 Getting Started

### Prerequisites

- ☕ Java 21 or higher
- 🔧 Maven or Gradle
- 🌐 Internet connection (for downloading Javadoc JARs)

### Installation

> [!IMPORTANT]  
> This project uses dependencies provided from GitHub. You therefore need to set your GitHub user `GPR_USER` and
> personal access token `GPR_TOKEN` in your `~/.gradle/gradle.properties`

1. **Clone the repository**
   ```bash
   git clone https://github.com/yourusername/javadoc-mcp-server.git
   cd javadoc-mcp-server
   ```

2. **Build the project**
   ```bash
   ./gradlew build
   ```

3. **Run the server**
   ```bash
   ./gradlew bootRun
   ```
### Configuration

Configure additional repositories in `~/.javadoc-mcp/repositories.json`:

```json
{
    "mavenCentral": {
        "url": "https://repo1.maven.org/maven2/"
    },
    "myPrivateRepository": {
        "url": "https://your-private-repo.com/repository/maven-public/",
        "username": "your-username",
        "password": "your-password"
    }
}
```

### Docker

To build the docker image, run:
   ```bash
   ./gradlew bootBuildImage
   ```
The created image runs the application using the `container` profile, which means that additional repository configuration
is loaded from `/opt/javadoc-mcp/init.d/repositories.json` instead of `~/.javadoc-mcp/repositories.json`.

## 🔧 How It Works

### 1. Tool Invocation

AI assistants call the `javadoc` tool with Maven coordinates and a class name:

```json
{
  "tool": "javadoc",
  "parameters": {
    "groupId": "org.springframework",
    "artifactId": "spring-context",
    "version": "6.1.0",
    "fullyQualifiedClassName": "org.springframework.context.ApplicationContext"
  }
}
```

### 2. Artifact Resolution

The server uses Eclipse Aether to:

- 🔍 Locate the Javadoc JAR in Maven Central or local repository
- ⬇️ Download the artifact if not cached
- 📂 Extract the HTML files to the local cache

### 3. Documentation Extraction

For the requested class:

- 📄 Finds the corresponding HTML file (e.g., `org/springframework/context/ApplicationContext.html`)
- 🔄 Converts HTML to markdown using Flexmark
- 📤 Returns formatted documentation to the AI

### 4. Caching Strategy

- ✅ First request: Downloads and extracts the entire Javadoc JAR
- ⚡ Subsequent requests: Serves directly from local cache
- 💾 Cache location: `javadoc-mcp.local-repository` configuration

## 📋 API Reference

### MCP Tool: `javadoc`

Retrieves Javadoc documentation for a specific class from a Maven artifact.

**Parameters:**

| Parameter                 | Type   | Description                       | Example                                          |
|---------------------------|--------|-----------------------------------|--------------------------------------------------|
| `groupId`                 | String | Maven Group ID                    | `org.springframework`                            |
| `artifactId`              | String | Maven Artifact ID                 | `spring-context`                                 |
| `version`                 | String | Artifact version                  | `6.1.0`                                          |
| `fullyQualifiedClassName` | String | Full class name including package | `org.springframework.context.ApplicationContext` |

**Returns:** Markdown-formatted Javadoc documentation

## 🛠️ Technology Stack

- **🔷 Kotlin** - Modern JVM language with concise syntax
- **🍃 Spring Boot** - Application framework and dependency injection
- **🤖 Spring AI** - AI integration and tool calling framework
- **📦 Eclipse Aether** - Maven artifact resolution and downloading
- **📝 Flexmark** - HTML to Markdown conversion
- **🔧 MCP Protocol** - Standardized AI assistant communication

## 🛠️ Tools

- [MCP Inspector](https://github.com/modelcontextprotocol/inspector)
  ```bash
  npx @modelcontextprotocol/inspector
  ```

## 🤝 Use Cases

- 💡 Explain Java library APIs in natural language
- 📖 Provide context-aware documentation during coding
- 🔍 Help developers understand unfamiliar libraries
- ✅ Verify correct API usage and parameters

## 🙏 Acknowledgments

- 🍃 Spring AI team for the excellent AI integration framework
- 📦 Eclipse Aether for robust Maven artifact resolution
- 📝 Flexmark for reliable HTML to Markdown conversion
- 🤖 MCP Protocol for standardizing AI assistant interactions

## ✅ Todo

- [ ] Add native support

## 📧 Contact

For questions, issues, or suggestions, please open an issue on GitHub.

---

**Made with ❤️ for the Java and AI communities**