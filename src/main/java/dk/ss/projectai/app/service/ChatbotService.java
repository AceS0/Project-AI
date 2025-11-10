package dk.ss.projectai.app.service;

import dk.ss.projectai.app.client.GeminiClient;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class ChatbotService {

    private final WeatherService weatherService;
    private final GeminiClient geminiClient;

    // Weather expert system instructions
    private static final String WEATHER_EXPERT_INSTRUCTIONS =
        "You are a professional meteorologist and weather expert with years of experience. " +
        "Your expertise includes analyzing weather patterns, providing accurate forecasts, " +
        "and giving practical advice about weather conditions. " +
        "When given weather data, analyze it thoroughly and provide helpful, accurate insights. " +
        "Always be friendly, professional, and explain weather concepts in easy-to-understand terms. " +
        "Focus on practical advice like what to wear, whether to bring an umbrella, " +
        "if it's safe to travel, and any weather-related precautions people should take. " +
        "Use weather terminology appropriately but explain technical terms when needed.";

    public ChatbotService(WeatherService weatherService, GeminiClient geminiClient) {
        this.weatherService = weatherService;
        this.geminiClient = geminiClient;
    }

    /**
     * Reactive method: Ask AI a question with weather context (Weather Expert Mode)
     */
    public Mono<String> askWithWeatherReactive(String userQuestion, String location) {
        return weatherService.getCurrentWeatherRaw(location)
                .flatMap(weatherJson -> {
                    String prompt = "Current weather data for " + location + ":\n" + weatherJson + "\n\n"
                            + "User question: " + userQuestion + "\n\n"
                            + "Please analyze the weather data and answer the user's question with your expert advice.";

                    return geminiClient.getResponses(prompt, WEATHER_EXPERT_INSTRUCTIONS)
                            .map(this::extractTextFromResponse);
                });
    }

    /**
     * Synchronous method: Ask AI a question with weather context (Weather Expert Mode)
     */
    public String askWithWeather(String userQuestion, String location) {
        // Get weather data (blocking)
        String weatherJson = weatherService.getCurrentWeatherRaw(location).block();

        // Build prompt for Gemini with weather expert instructions
        String prompt = "Current weather data for " + location + ":\n" + weatherJson + "\n\n"
                + "User question: " + userQuestion + "\n\n"
                + "Please analyze the weather data and answer the user's question with your expert advice.";

        // Use synchronous method with weather expert instructions
        return geminiClient.generateText(prompt, WEATHER_EXPERT_INSTRUCTIONS);
    }

    /**
     * Extract text from Gemini response
     */
    private String extractTextFromResponse(GeminiClient.GeminiResponse response) {
        if (response == null || response.candidates() == null || response.candidates().isEmpty()) {
            return "No response from AI";
        }

        StringBuilder text = new StringBuilder();
        for (GeminiClient.Candidate candidate : response.candidates()) {
            if (candidate.content() != null && candidate.content().parts() != null) {
                for (GeminiClient.Part part : candidate.content().parts()) {
                    if (part.text() != null) {
                        text.append(part.text());
                    }
                }
            }
        }

        return text.toString();
    }
}
