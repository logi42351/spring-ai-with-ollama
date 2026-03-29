package com.aiservice.backend.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.ollama.api.OllamaApi;
import org.springframework.ai.ollama.api.OllamaChatOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OllamaConfig {


    @Bean
    ChatClient chatClient(ChatClient.Builder chatClientBuilder) {

        /**
         * Use of chat Roles , to provide better context understanding
         * limiting the model's scope to respond user's query

         * System -> setting the context/ What this model is capable of/ what is the task assigned to this model.
         * User -> user prompts
         * Assistant -> Response given by Model

         * All these are Objects of type of Prompt -> User/System/Assistant/Tool message  ->Messages

         * Advisors are interceptors between the request sent to llm and response received from llm
         *   User prompt -> [Advisors] -> LLM -> [Advisors] -> Responded back to API Request
         *
         */

        return chatClientBuilder
                .defaultAdvisors(SimpleLoggerAdvisor.builder().build())
                .defaultSystem("""
                        You are a Senior Developer expertise is SpringBoot,
                        If you asks questions related to anything else, Instruct them clearly that you are not designed for it.
                        """)
                .defaultUser("How can you you help me?")
                .build();
    }

    @Bean
    OllamaChatOptions chatOptions(){
        return OllamaChatOptions.builder()
                .temperature(0.5)
                .model("mistral:7b")
                .maxTokens(1000)
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

//    @Bean
//    OllamaApi ollamaApi(){
//        return OllamaApi.builder()
//                .baseUrl("http://localhost:11434")
//                .build();
//    }

    @Bean
    OllamaChatModel ollamaChatModel(OllamaApi ollamaApi, OllamaChatOptions ollamaChatOptions){
        return OllamaChatModel.builder()
                .defaultOptions(ollamaChatOptions)
                .ollamaApi(ollamaApi)
                .build();
    }

}
