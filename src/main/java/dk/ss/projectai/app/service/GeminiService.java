package dk.ss.projectai.app.service;

import dk.ss.projectai.app.client.GeminiClient;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;


@Service
public class GeminiService {
    private final GeminiClient geminiClient;

    public GeminiService(GeminiClient geminiClient) {
        this.geminiClient = geminiClient;
    }

    public record ResponseDto(String response) {}

    public Mono<ResponseDto> getGeminiResponse(String prompt) {
        return geminiClient.getResponses(prompt)
                .map(this::mapToResponseDto);
    }

    private ResponseDto mapToResponseDto(GeminiClient.GeminiResponse resp) {
        StringBuilder combinedText = new StringBuilder();
        if (resp.candidates() != null && !resp.candidates().isEmpty()) {
            for (GeminiClient.Candidate candidate : resp.candidates()) {
                if (candidate.content() != null && candidate.content().parts() != null) {
                    for (GeminiClient.Part part : candidate.content().parts()) {
                        combinedText.append(part.text());
                    }
                }
            }
        }
        return new ResponseDto(combinedText.toString());
    }
}
