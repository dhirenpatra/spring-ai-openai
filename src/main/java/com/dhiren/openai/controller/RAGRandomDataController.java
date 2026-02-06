package com.dhiren.openai.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/rag")
public class RAGRandomDataController {

    @Value("classpath:/prompts/systemPromptRandomDataRAG.st")
    private Resource resource;

    @Value("classpath:/prompts/newSystemPrompt.st")
    private Resource newSystemResource;

    private final ChatClient chatClient;
    private final VectorStore vectorStore;

    public RAGRandomDataController(@Qualifier("memoryChatClient") ChatClient chatClient, VectorStore vectorStore) {
        this.chatClient = chatClient;
        this.vectorStore = vectorStore;
    }

    @GetMapping("/chat")
    public ResponseEntity<String> chat(@RequestHeader("username") String userName,
                                       @RequestParam("message") String message) {
        SearchRequest searchRequest =
                SearchRequest.builder().query(message)
                        .topK(4)
                        .similarityThreshold(0.5)
                        .build();

        List<Document> documents = vectorStore.similaritySearch(searchRequest);

        String prompt = documents.stream().map(Document::getText)
                .collect(Collectors.joining(System.lineSeparator()));

        String answer = chatClient.prompt()
                .system(promptSystemSpec ->
                        promptSystemSpec.text(resource).param("documents", prompt))
                .advisors(advisorSpec ->
                        advisorSpec.param(ChatMemory.CONVERSATION_ID, userName))
                .user(message)
                .call()
                .content();

        return ResponseEntity.ok(answer);
    }

    @GetMapping("/doc/chat")
    public ResponseEntity<String> docChat(@RequestHeader("username") String userName,
                                       @RequestParam("message") String message) {
        SearchRequest searchRequest =
                SearchRequest.builder().query(message)
                        .topK(3)
                        .similarityThreshold(0.5)
                        .build();

        List<Document> documents = vectorStore.similaritySearch(searchRequest);

        String prompt = documents.stream().map(Document::getText)
                .collect(Collectors.joining(System.lineSeparator()));

        String answer = chatClient.prompt()
                .system(promptSystemSpec ->
                        promptSystemSpec.text(resource).param("documents", newSystemResource))
                .advisors(advisorSpec ->
                        advisorSpec.param(ChatMemory.CONVERSATION_ID, userName))
                .user(message)
                .call()
                .content();

        return ResponseEntity.ok(answer);
    }

}
