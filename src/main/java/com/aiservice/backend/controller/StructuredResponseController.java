package com.aiservice.backend.controller;

import com.aiservice.backend.entity.Topic;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.converter.ListOutputConverter;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/structure")
public class StructuredResponseController {

    private final ChatClient chatClient;

    public StructuredResponseController(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder
                .defaultAdvisors(new SimpleLoggerAdvisor())
                .build();
    }


    @GetMapping("/chat/list-objects")
    public ResponseEntity<List<Topic>> checkListPojoGeneration(@RequestParam("message") String message){

        /**
         * Prompt example:
         *      Generate  list of all topics and description in springBoot framework that a Spring boot developer with 2 years must know
         */

        List<Topic> topic = chatClient.prompt()
                .user(message)
                .call()
                .entity(new ParameterizedTypeReference<List<Topic>>() {
                });                                              // usage of ParameterizedTypeReference for List of Pojo objects
        return ResponseEntity.ok(topic);
    }


    @GetMapping("/chat/object")
    public ResponseEntity<Topic> checkPojoGeneration(@RequestParam("message") String message){

        /**
         * Prompt example:
         *     Describe about Java Programming in 100 words
         */

        Topic topic = chatClient.prompt()
                .user(message)
                .call()
//                .entity(new BeanOutputConverter<Topic>(Topic.class))  usage of BeanOutPutConverter
                .entity(Topic.class); // usage of clazz


        return ResponseEntity.ok(topic);
    }

    @GetMapping("/chat/list-strings")
    public ResponseEntity<List<String>> checkListGeneration(@RequestParam("message") String message){

        /**
         * Prompt example:
         *     List all the fruits that are cultivated in India
         */

        List<String> response = chatClient.prompt()
                .user(message)
                .call()
                .entity(new ListOutputConverter());  //usage od ListOutputConverter for list of strings


        return ResponseEntity.ok(response);
    }
}
