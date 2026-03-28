package com.aiservice.backend.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ChatController {

    private final ChatClient chatClient;

    public ChatController(ChatClient.Builder chatClient) {
        this.chatClient = chatClient
                .build();
    }

    /**
     * Chat Model bean needs OllamaApi (http://localhost:11343) bean.
     * OllamaOptions bean (configure temperature) , most of these beans are
     * created by Spring itself ,using ConditionalOnMissingBean annotations
     */

    /**
     * When we use ollama-ai package, ChatModel bean is configured by default with Mistral model
     * our Task is to use ChatClient bean to interact with the llm model.
     */


    /**
     * A Simple Get API with a prompt from user
     * @param input
     * @return
     */
    @GetMapping("/chat")
    public String getChat(@RequestParam("message") String input) {

        return chatClient
                .prompt(input)
                .call()
                .content();
    }

}
