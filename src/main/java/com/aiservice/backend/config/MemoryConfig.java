package com.aiservice.backend.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.client.advisor.api.Advisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.ChatMemoryRepository;
import org.springframework.ai.chat.memory.InMemoryChatMemoryRepository;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 *  This file includes the configurations for enabling chat memory [history of conversations]
 */
@Configuration
public class MemoryConfig {

    @Bean("chatWithMemory")
    ChatClient chatWithMem(ChatClient.Builder chatClientBuilder) {

        /**
         * MessageChatMemoryAdvisor helps the developers in intercepting the request from users attaches the
         * Chat messages in request and also updates the user/assistant messages in chat memory
         * Requires the ChatMemory Bean
         */
        Advisor memoryAdvisor = MessageChatMemoryAdvisor.builder(chatMemory())
                .build();

        /**
         * Simple logging advisor to get logs of request and response
         */
        Advisor loggerAdvisor = new SimpleLoggerAdvisor();

        return chatClientBuilder
                .defaultAdvisors(
                        memoryAdvisor, loggerAdvisor
                )
                .build();
    }

    /**
     * ChatMemory is an interface so look for implementation class  [MessageWindowChatMemory]
     * MessageWindowChatMemory class needs another bean of type ChatMemoryRepository
     */
    @Bean
    ChatMemory chatMemory() {
        return MessageWindowChatMemory.builder()
                .chatMemoryRepository(chatMemoryRepository())
                .build();
    }

    /**
     * ChatMemoryRepository is an interface so look for an implementation class [InMemoryChatMemoryRepository]
     */
    @Bean
    ChatMemoryRepository chatMemoryRepository() {
        return new InMemoryChatMemoryRepository();
    }
}
