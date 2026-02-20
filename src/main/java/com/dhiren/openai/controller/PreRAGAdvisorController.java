package com.dhiren.openai.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/rag/pre")
public class PreRAGAdvisorController {

    private final ChatClient chatClient;

    public PreRAGAdvisorController(@Qualifier("preRagMemoryChatClient") ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    @GetMapping("/chat/v2")
    public ResponseEntity<String> chat(@RequestHeader("username") String userName,
                                       @RequestParam("message") String message) {

        String answer = chatClient.prompt()
                .advisors(advisorSpec ->
                        advisorSpec.param(ChatMemory.CONVERSATION_ID, userName))
                .user(message)
                .call()
                .content();

        return ResponseEntity.ok(answer);
    }

    @GetMapping("/doc/chat/v2")
    public ResponseEntity<String> docChat(@RequestHeader("username") String userName,
                                       @RequestParam("message") String message) {

        String answer = chatClient.prompt()
                .advisors(advisorSpec ->
                        advisorSpec.param(ChatMemory.CONVERSATION_ID, userName))
                .user(message)
                .call()
                .content();

        return ResponseEntity.ok(answer);
    }

}
