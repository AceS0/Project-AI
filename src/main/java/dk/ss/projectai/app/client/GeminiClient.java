package dk.ss.projectai.app.client;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class GeminiClient {
    private final WebClient client;

    public GeminiClient(@Qualifier("GeminiWebClient") WebClient client) {
        this.client = client;
    }

    // Gemini API response structure
    public record GeminiResponse(List<Candidate> candidates) {}
    public record Candidate(Content content) {}
    public record Content(List<Part> parts) {}
    public record Part(String text) {}

    // Gemini API request structure with system instructions
    public record GeminiRequest(List<ContentRequest> contents, SystemInstruction systemInstruction) {}
    public record SystemInstruction(List<PartRequest> parts) {}
    public record ContentRequest(String role, List<PartRequest> parts) {}
    public record PartRequest(String text) {}

    /**
     * Reactive method to get Gemini response with custom system instructions
     */
    public Mono<GeminiResponse> getResponses(String prompt, String systemInstructions) {
        PartRequest part = new PartRequest(prompt);
        ContentRequest content = new ContentRequest("user", List.of(part));

        SystemInstruction systemInstruction = null;
        if (systemInstructions != null && !systemInstructions.isBlank()) {
            systemInstruction = new SystemInstruction(List.of(new PartRequest(systemInstructions)));
        }

        GeminiRequest body = new GeminiRequest(List.of(content), systemInstruction);

        return client.post()
                .uri("/gemini-2.5-flash:generateContent")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(body)
                .retrieve()
                .onStatus(s -> s.value() == 400, r -> r.bodyToMono(String.class)
                        .map(msg -> new IllegalArgumentException("Gemini 400: " + msg)))
                .onStatus(s -> s.value() == 401, r -> r.bodyToMono(String.class)
                        .map(msg -> new IllegalArgumentException("Gemini 401 Unauthorized: " + msg)))
                .onStatus(s -> s.value() == 404, r -> r.bodyToMono(String.class)
                        .map(msg -> new IllegalArgumentException("Gemini 404 Not Found: " + msg + ". Check API endpoint and key.")))
                .onStatus(HttpStatusCode::isError, r -> r.bodyToMono(String.class)
                        .map(msg -> new IllegalArgumentException("Gemini Error: " + msg)))
                .bodyToMono(GeminiResponse.class);
    }

    /**
     * Reactive method to get Gemini response without system instructions
     */
    public Mono<GeminiResponse> getResponses(String prompt) {
        return getResponses(prompt, null);
    }

    /**
     * Synchronous method to generate text with optional system instructions
     */
    public String generateText(String prompt, String systemInstructions) {
        GeminiResponse response = getResponses(prompt, systemInstructions).block();

        if (response == null || response.candidates() == null || response.candidates().isEmpty()) {
            return "No response from AI";
        }

        StringBuilder text = new StringBuilder();
        for (Candidate candidate : response.candidates()) {
            if (candidate.content() != null && candidate.content().parts() != null) {
                for (Part part : candidate.content().parts()) {
                    if (part.text() != null) {
                        text.append(part.text());
                    }
                }
            }
        }

        return text.toString();
    }

    /**
     * Synchronous method to generate text (blocks until response is received)
     */
    public String generateText(String prompt) {
        return generateText(prompt, null);
    }
}
