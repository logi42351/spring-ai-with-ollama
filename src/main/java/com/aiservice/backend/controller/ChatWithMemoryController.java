package com.aiservice.backend.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/chat/mem")
public class ChatWithMemoryController {

    private final ChatClient chatClient;

    /**
     * Qualifier annotation is used to instruct spring boot to load which bean in case of multiple bean of same type exists
     * In our case we have two beans of ChatClient
     * @param chatClient
     */
    public ChatWithMemoryController(@Qualifier("chatWithMemory") ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    @GetMapping("/")
    public String getChat(@RequestParam("message") String message)
    {
        return chatClient.prompt(message)
                .call()
                .content();
    }
}
