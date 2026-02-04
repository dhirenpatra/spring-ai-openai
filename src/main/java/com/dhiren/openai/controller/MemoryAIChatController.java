package com.dhiren.openai.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class MemoryAIChatController {

    private final ChatClient client;

    public MemoryAIChatController(@Qualifier("memoryChatClient") ChatClient client) {
        this.client = client;
    }

    @GetMapping("/memory")
    public String chat(@RequestParam("message") String message) {
        return client.prompt()
                .user(message)
                .call()
                .content();
    }

    @GetMapping("/memory/conversation")
    public String chatConversation(@RequestHeader("username") String userName,
                                   @RequestParam("message") String message) {
        return client.prompt()
                .advisors(advisorSpec -> advisorSpec.param(
                        ChatMemory.CONVERSATION_ID, userName
                ))
                .user(message)
                .call()
                .content();
    }

}
