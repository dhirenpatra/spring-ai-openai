package com.dhiren.openai.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class PromptTemplateAIController {

    private final ChatClient client;

    @Value("${classpath:/prompts/userPrompts.st}")
    private Resource resource;

    public PromptTemplateAIController(ChatClient client) {
        this.client = client;
    }

    @GetMapping("/email")
    public String getStarted(@RequestParam("customerName") String name,
                             @RequestParam("customerMessage") String message) {
        return client.prompt()
                .options(
                        OpenAiChatOptions
                                .builder()
                                .model(OpenAiApi.ChatModel.GPT_3_5_TURBO)
                                .maxTokens(1000)
                                .build()
                )
                .system("""
                        You are a professional customer service assistant which helps drafting email
                        responses to improve the productivity of the customer support team
                        """)
                .user(promptUserSpec ->
                        promptUserSpec.text(resource)
                                .param("customerName",name)
                                .param("customerMessage",message)
                )
                .user(message)
                .call().content();
    }

}
