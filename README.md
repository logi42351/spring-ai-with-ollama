# Spring AI Chat Controller (Ollama Integration)

This document explains the `ChatController.java` class, which serves as the primary REST entry point for interacting with a local Large Language Model (LLM) using **Spring AI** and **Ollama**.

---



##  Overview

The `ChatController` is a Spring Boot `@RestController` mapped to the `/api` path. It exposes a simple GET endpoint that accepts a user prompt, sends it to a local Ollama instance, and returns the generated text response.

It leverages Spring AI's modern, fluent `ChatClient` API rather than interacting with the low-level `ChatModel` directly.

---

##  Architecture & Bean Lifecycle

Spring Boot's powerful autoconfiguration handles the heavy lifting behind the scenes. Based on the presence of the `spring-ai-ollama-spring-boot-starter` dependency, Spring creates several beans conditionally (using `@ConditionalOnMissingBean`):

1. **`OllamaApi` Bean**: The foundational HTTP client. It is automatically configured to point to your local Ollama server (usually `http://localhost:11434`).
2. **`OllamaOptions` Bean**: Holds configuration like `temperature`, `top_p`, etc.
3. **`ChatModel` Bean**: The low-level interface connecting Spring AI to Ollama. By default, the Ollama starter configures this to use the **Mistral** model (unless overridden in `application.yml`).
4. **`ChatClient.Builder` Bean**: The high-level API builder that wraps the `ChatModel`.

---

## 🧱 Under the Hood: The Bean Stack

Spring AI autoconfigures three main layers to make the `/api/chat` endpoint work:

1. **OllamaApi**: The low-level HTTP client. It targets the `base-url` defined in your `.yml` and handles raw JSON exchange with the Ollama server.
2. **OllamaChatModel**: The bridge between Spring AI and Ollama. It implements the `ChatModel` interface, allowing you to switch to OpenAI or Anthropic later just by changing a dependency.
3. **ChatClient.Builder**: The developer-friendly wrapper. We inject this into our `ChatController` to use the fluent API (`.prompt().call()`).

**Note:** If you want to use multiple models (e.g. Llama for chat and Mistral for summaries), you would manually define two `OllamaChatModel` beans and use `@Qualifier` to distinguish them.

## 💻 Code Breakdown

### 1. Constructor Injection & `ChatClient`
```java
private final ChatClient chatClient;

public ChatController(ChatClient.Builder chatClientBuilder) {
    this.chatClient = chatClientBuilder.build();
}
```
# 2. Chat API Endpoint

This document provides a detailed breakdown of the `/chat` REST endpoint. This endpoint serves as the primary interface for users to send prompts to the underlying AI model using Spring AI's fluent `ChatClient`.

## 📌 Endpoint Overview

* **URL:** `/chat` (or `/api/chat` depending on your class-level `@RequestMapping`)
* **Method:** `GET`
* **Description:** Accepts a user-provided text message, processes it through the configured AI model (e.g., Ollama), and returns the generated text response.

---

## 💻 Code Implementation

```java
@GetMapping("/chat")
public String getChat(@RequestParam("message") String input) {
    return chatClient
            .prompt(input)
            .call()
            .content();
}
```

## 🦙 Prerequisites: Installing Ollama & Pulling Models

Because this application runs a Large Language Model (LLM) completely locally, you need to install the Ollama engine and download the specific model referenced in the `application.yml` file before starting the Spring Boot server.

### 1. Download and Install Ollama
Ollama is a lightweight, extensible framework for running LLMs on your local machine.

*   **macOS / Windows:** Download the installer directly from the [official Ollama website](https://ollama.com/download) and run it.
*   **Linux:** Install it via your terminal using the following command:
    ```bash
    curl -fsSL [https://ollama.com/install.sh](https://ollama.com/install.sh) | sh
    ```

Once installed, the Ollama service usually starts automatically in the background. It exposes its API on port `11434` by default.

### 2. Pull the Required Model
Even with Ollama installed, it doesn't come with any models out of the box to save disk space. You must explicitly download the model you want to use.

Open your terminal or command prompt and run the following command to pull **Llama 3.2** (the model specified in our `application.yml`):

```bash
ollama pull llama3.2