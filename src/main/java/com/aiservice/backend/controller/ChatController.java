package com.aiservice.backend.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class ChatController {

    private final ChatClient chatClient;

    public ChatController(ChatClient chatClient) {
        this.chatClient = chatClient ;
    }

    /**
     * Injecting StringTemplate file as a Resource Object
     * will be used in User Prompt
     */

    @Value("classpath:promptTemplates/UserPrompt.st")
    private Resource userTemplate;

    /**
     * Prompt Stuffing for system , that is giving the answer book before looking into the question
     */
    @Value("classpath:promptTemplates/SystemPrompt.st")
    private Resource systemTemplate;


    /**
     * A Simple Get API with a prompt from user
     * @return
     */
    @GetMapping(value = "/chat", produces = MediaType.TEXT_PLAIN_VALUE)
    public String getChat(
            @RequestParam("explanation") String explanation,
            @RequestParam("user") String user,
            @RequestParam("topic") String topic
    ) {

        return chatClient
                .prompt()
                .system(systemTemplate)
                .user(
                        promptUserSpec -> promptUserSpec.text(userTemplate)
                                .params(
                                        Map.of(
                                        "explanation", explanation,
                                        "user",user, "topic",topic
                                        )
                                )

                )
                .call()
                .content();
    }

    /**
     * Streaming of Response from llm model , whenever llm created a word in response , it is transferred then and there
     *
     * @param explanation
     * @param user
     * @param topic
     * @return
     */
    @GetMapping(value = "/stream")
    public Flux<String> getStreaming(
            @RequestParam("explanation") String explanation,
            @RequestParam("user") String user,
            @RequestParam("topic") String topic
    ) {

        return chatClient
                .prompt()
                .system(systemTemplate)
                .user(
                        promptUserSpec -> promptUserSpec.text(userTemplate)
                                .params(
                                        Map.of(
                                                "explanation", explanation,
                                                "user",user, "topic",topic
                                        )
                                )

                )
                .stream()    // instead of call() we replace it with a stream() -> returns StreamResponseSpec
                .content();  // Returns Flux<String>
    }

}
