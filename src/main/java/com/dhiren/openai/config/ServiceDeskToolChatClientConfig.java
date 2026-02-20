package com.dhiren.openai.config;

import com.dhiren.openai.advisor.TokenUsageAuditAdvisor;
import com.dhiren.openai.tools.TimeTools;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.client.advisor.api.Advisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import java.util.List;

@Configuration
public class ServiceDeskToolChatClientConfig {

    @Value("classpath:/prompts/helpDeskSystemPrompt.st")
    private Resource helpDeskSystemPrompt;

    @Bean("serviceDeskToolChatClient")
    public ChatClient chatClient(ChatClient.Builder chatClientBuilder,
                                 ChatMemory chatMemory, TimeTools timeTools) {

        MessageChatMemoryAdvisor messageChatMemoryAdvisor =
                MessageChatMemoryAdvisor.builder(chatMemory).build();

        SimpleLoggerAdvisor simpleLoggerAdvisor = new SimpleLoggerAdvisor();
        Advisor tokenUsageAdvisor = new TokenUsageAuditAdvisor();

        return chatClientBuilder
                .defaultTools(timeTools)
                .defaultSystem(helpDeskSystemPrompt)
                .defaultAdvisors(List.of(messageChatMemoryAdvisor,
                        simpleLoggerAdvisor,
                        tokenUsageAdvisor))
                .build();
    }

}
