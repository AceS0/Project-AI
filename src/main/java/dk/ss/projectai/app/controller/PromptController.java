package dk.ss.projectai.app.controller;

import dk.ss.projectai.app.service.GeminiService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/prompt")
public class PromptController {

    private final GeminiService GeminiService;

    public PromptController(GeminiService GeminiService) {
        this.GeminiService = GeminiService;
    }

    public record QueryRequest(String prompt) {}

    @PostMapping
    public Mono<ResponseEntity<GeminiService.ResponseDto>> ask(@RequestBody QueryRequest queryRequest) {
        var response = GeminiService.getGeminiResponse(queryRequest.prompt);
        return response.map(ResponseEntity::ok);
    }
}

