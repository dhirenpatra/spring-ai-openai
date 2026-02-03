package com.dhiren.openai.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class GoogleAIController {

    private final ChatClient client;

    public GoogleAIController(ChatClient.Builder clientBuilder) {
        this.client = clientBuilder.build();
    }

    @GetMapping("/chat")
    public String getStarted(@RequestParam("message") String message) {
        return client.prompt(message).call().content();
    }

}
