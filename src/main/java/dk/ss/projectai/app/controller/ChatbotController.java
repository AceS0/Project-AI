package dk.ss.projectai.app.controller;

import dk.ss.projectai.app.service.ChatbotService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/chatbot")
public class ChatbotController {

    private final ChatbotService chatbotService;

    public ChatbotController(ChatbotService chatbotService) {
        this.chatbotService = chatbotService;
    }

    public record ChatRequest(String question, String location) {}
    public record ChatResponse(String answer) {}

    /**
     * Reactive endpoint: Ask AI with weather context
     * Example: POST /api/chatbot/ask
     * Body: {"question": "Should I bring an umbrella?", "location": "Copenhagen"}
     */
    @PostMapping("/ask")
    public Mono<ResponseEntity<ChatResponse>> askWithWeather(@RequestBody ChatRequest request) {
        return chatbotService.askWithWeatherReactive(request.question(), request.location())
                .map(answer -> ResponseEntity.ok(new ChatResponse(answer)))
                .onErrorResume(error ->
                    Mono.just(ResponseEntity.badRequest()
                        .body(new ChatResponse("Error: " + error.getMessage()))));
    }

    /**
     * Synchronous endpoint: Ask AI with weather context (blocking)
     * Example: POST /api/chatbot/ask-sync
     * Body: {"question": "What should I wear today?", "location": "London"}
     */
    @PostMapping("/ask-sync")
    public ResponseEntity<ChatResponse> askWithWeatherSync(@RequestBody ChatRequest request) {
        try {
            String answer = chatbotService.askWithWeather(request.question(), request.location());
            return ResponseEntity.ok(new ChatResponse(answer));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new ChatResponse("Error: " + e.getMessage()));
        }
    }
}

