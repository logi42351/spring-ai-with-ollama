package com.aiservice.backend.advisors;

import lombok.Builder;
import org.jspecify.annotations.NullMarked;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClientRequest;
import org.springframework.ai.chat.client.ChatClientResponse;
import org.springframework.ai.chat.client.advisor.api.CallAdvisor;
import org.springframework.ai.chat.client.advisor.api.CallAdvisorChain;
import org.springframework.ai.chat.metadata.Usage;

/**
 * A Custom Advisor created by us , usually it should implement
 *    either CallAdvisor or StreamAdvisor

 *    Using CallAdvisorChain bean , We can pass the ChatClientRequest Object to the next Advisors in that chain
 */

@Builder
public class TokenAuditAdvisor implements CallAdvisor {

    private final Logger logger =  LoggerFactory.getLogger(TokenAuditAdvisor.class);

    @Override
    @NullMarked
    public ChatClientResponse adviseCall(ChatClientRequest chatClientRequest,CallAdvisorChain callAdvisorChain) {
        ChatClientResponse chatClientResponse = callAdvisorChain.nextCall(chatClientRequest);

        Usage usage = null;

        if (chatClientResponse.chatResponse() != null) {
            usage = chatClientResponse.chatResponse().getMetadata().getUsage();
        }

        logger.info("Token Usage Audit {}",usage);

        return chatClientResponse;
    }

    @Override
    @NullMarked
    public String getName() {
        return "TokenAuditAdvisor";
    }

    /**
     *  Order given to the Advisor in the CallAdvisorChain
     *  Higher value is preferred first
     *  Same order value doesn't guarantee execution order
     **/

    @Override
    public int getOrder() {
        return 1;
    }
}
