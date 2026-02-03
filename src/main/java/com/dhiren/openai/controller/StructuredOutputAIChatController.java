package com.dhiren.openai.controller;

import com.dhiren.openai.advisor.model.CountryDetails;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.converter.ListOutputConverter;
import org.springframework.ai.converter.MapOutputConverter;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/api")
public class StructuredOutputAIChatController {

    private final ChatClient client;

    public StructuredOutputAIChatController(ChatClient client) {
        this.client = client;
    }

    @GetMapping("/country-details")
    public ResponseEntity<CountryDetails> getCountryAndItsCities(@RequestParam("message") String message) {
        return ResponseEntity.ok(client.prompt()
                .user(message)
                .call()
                .entity(CountryDetails.class));
    }

    @GetMapping("/cities")
    public ResponseEntity<List<String>> getCities(@RequestParam("message") String message) {
        return ResponseEntity.ok(client.prompt()
                .user(message)
                .call()
                .entity(new ListOutputConverter()));
    }

    @GetMapping("/cities-map")
    public ResponseEntity<Map<String, Object>> getCitiesMap(@RequestParam("message") String message) {
        return ResponseEntity.ok(client.prompt()
                .user(message)
                .call()
                .entity(new MapOutputConverter()));
    }

    @GetMapping("/cities-list")
    public ResponseEntity<List<CountryDetails>> getCitiesCountries(@RequestParam("message") String message) {
        return ResponseEntity.ok(client.prompt()
                .user(message)
                .call()
                .entity(new ParameterizedTypeReference<>() {})
        );
    }

}
