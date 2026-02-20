package com.dhiren.openai.config;

import com.dhiren.openai.advisor.TokenUsageAuditAdvisor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.client.advisor.api.Advisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.rag.advisor.RetrievalAugmentationAdvisor;
import org.springframework.ai.rag.preretrieval.query.transformation.TranslationQueryTransformer;
import org.springframework.ai.rag.retrieval.search.VectorStoreDocumentRetriever;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class PreRetrieveChatClientConfig {

    @Bean("preRagMemoryChatClient")
    public ChatClient chatClient(ChatClient.Builder chatClientBuilder,
                                 ChatMemory chatMemory,
                                 RetrievalAugmentationAdvisor preRetrievalAugmentationAdvisor) {

        MessageChatMemoryAdvisor messageChatMemoryAdvisor =
                MessageChatMemoryAdvisor.builder(chatMemory).build();

        SimpleLoggerAdvisor simpleLoggerAdvisor = new SimpleLoggerAdvisor();
        Advisor tokenUsageAdvisor = new TokenUsageAuditAdvisor();

        return chatClientBuilder
                .defaultAdvisors(List.of(
                        messageChatMemoryAdvisor,
                        simpleLoggerAdvisor,
                        tokenUsageAdvisor,
                        preRetrievalAugmentationAdvisor
                ))
                .build();
    }

    @Bean
    public RetrievalAugmentationAdvisor preRetrievalAugmentationAdvisor(
            VectorStore vectorStore, ChatClient chatClient) {
        return RetrievalAugmentationAdvisor
                .builder()
                .queryTransformers(
                        TranslationQueryTransformer
                                .builder()
                                .chatClientBuilder(chatClient.mutate())
                                .targetLanguage("English").build()
                )
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
