package com.dhiren.openai.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.memory.repository.jdbc.JdbcChatMemoryRepository;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class MemoryChatClientConfig {

    @Bean
    public ChatMemory chatMemory(JdbcChatMemoryRepository jdbcChatMemoryRepository) {
        return MessageWindowChatMemory.builder().maxMessages(10)
                .chatMemoryRepository(jdbcChatMemoryRepository).build();
    }

    @Bean("memoryChatClient")
    public ChatClient chatClient(ChatClient.Builder chatClientBuilder, ChatMemory chatMemory) {

        ChatOptions options = ChatOptions
                .builder().model("gpt-4.1-mini")
                .temperature(0.75)
                .build();

        MessageChatMemoryAdvisor messageChatMemoryAdvisor =
                MessageChatMemoryAdvisor.builder(chatMemory).build();

        SimpleLoggerAdvisor simpleLoggerAdvisor = new SimpleLoggerAdvisor();

        return chatClientBuilder
                .defaultOptions(options)
                .defaultAdvisors(List.of(messageChatMemoryAdvisor, simpleLoggerAdvisor))
                .build();
    }

}
