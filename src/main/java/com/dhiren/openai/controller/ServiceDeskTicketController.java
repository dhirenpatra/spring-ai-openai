package com.dhiren.openai.controller;

import com.dhiren.openai.tools.HelpDeskTools;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/tools/tickets")
public class ServiceDeskTicketController {

    private final HelpDeskTools helpDeskTools;
    private final ChatClient chatClient;


    public ServiceDeskTicketController(HelpDeskTools helpDeskTools, @Qualifier("serviceDeskToolChatClient") ChatClient chatClient) {
        this.helpDeskTools = helpDeskTools;
        this.chatClient = chatClient;
    }

    @GetMapping
    public ResponseEntity<String> createTicket(@RequestHeader String userName, @RequestParam String issue) {

        String response = chatClient.prompt()
                .advisors(advisorSpec ->
                        advisorSpec.param(ChatMemory.CONVERSATION_ID, userName))
                .user(issue)
                .tools(helpDeskTools)
                .toolContext(Map.of("userName", userName))
                .call().content();

        return ResponseEntity.ok(response);
    }
}
