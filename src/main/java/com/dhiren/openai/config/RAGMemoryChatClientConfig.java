package com.dhiren.openai.config;

import com.dhiren.openai.advisor.TokenUsageAuditAdvisor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.client.advisor.api.Advisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.rag.advisor.RetrievalAugmentationAdvisor;
import org.springframework.ai.rag.retrieval.search.VectorStoreDocumentRetriever;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class RAGMemoryChatClientConfig {

    @Bean("ragMemoryChatClient")
    public ChatClient chatClient(ChatClient.Builder chatClientBuilder,
                                 ChatMemory chatMemory,
                                 RetrievalAugmentationAdvisor retrievalAugmentationAdvisor) {

        ChatOptions options = ChatOptions
                .builder().model("gpt-4.1-mini")
                .temperature(0.7)
                .build();

        MessageChatMemoryAdvisor messageChatMemoryAdvisor =
                MessageChatMemoryAdvisor.builder(chatMemory).build();

        SimpleLoggerAdvisor simpleLoggerAdvisor = new SimpleLoggerAdvisor();
        Advisor tokenUsageAdvisor = new TokenUsageAuditAdvisor();

        return chatClientBuilder
                .defaultOptions(options)
                .defaultAdvisors(List.of(
                        messageChatMemoryAdvisor,
                        simpleLoggerAdvisor,
                        tokenUsageAdvisor,
                        retrievalAugmentationAdvisor
                ))
                .build();
    }

    @Bean
    public RetrievalAugmentationAdvisor retrievalAugmentationAdvisor(VectorStore vectorStore) {
        return RetrievalAugmentationAdvisor
                .builder()
                .documentRetriever(
                        VectorStoreDocumentRetriever
                                .builder()
                                .vectorStore(vectorStore)
                                .topK(3)
                                .similarityThreshold(0.7)
                                .build()
                )
                .build();
    }

}
