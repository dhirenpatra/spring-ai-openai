package com.dhiren.openai.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/api")
public class StreamChatAIController {

    private final ChatClient client;

    public StreamChatAIController(ChatClient client) {
        this.client = client;
    }

    @GetMapping("/chat/stream")
    public Flux<String> getStarted(@RequestParam("message") String message) {
        return client.prompt()
                .user(message)
                .stream()
                .content();
    }

}
