package dk.ss.projectai.app.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Configuration
public class WebClientConfig {

    @Bean
    WebClient.Builder webClientBuilder() {
        return WebClient.builder()
                // Rate limiting, retries, timeouts etc. can be configured here
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);

    }

    @Bean
    WebClient GeminiWebClient(
            WebClient.Builder b,
            @Value("${GEMINI_API_KEY}") String apiKey,
            @Value("${GEMINI_API_BASEURL}") String baseUrl
    ) {
        // Trim whitespace from API key and base URL
        apiKey = apiKey != null ? apiKey.trim() : null;
        baseUrl = baseUrl != null ? baseUrl.trim() : null;

        if (apiKey == null || apiKey.isBlank()) {
            throw new IllegalArgumentException("Gemini API-key must be provided in application.properties");
        }

        if (baseUrl == null || baseUrl.isBlank()) {
            throw new IllegalArgumentException("Gemini API baseUrl must be provided in application.properties");
        }

        return b.clone()
                .baseUrl(baseUrl)
                .defaultHeader("x-goog-api-key", apiKey)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    @Bean
    WebClient externalWebClient(
            WebClient.Builder b,
            @Value("${EXT_API_KEY}") String apiKey,
            @Value("${EXT_API_BASEURL}") String baseUrl
    ) {
        // Trim whitespace from API key and base URL
        apiKey = apiKey != null ? apiKey.trim() : null;
        baseUrl = baseUrl != null ? baseUrl.trim() : null;

        if (apiKey == null || apiKey.isBlank()) {
            throw new IllegalArgumentException("External API-key must be provided in application.properties");
        }

        if (baseUrl == null || baseUrl.isBlank()) {
            throw new IllegalArgumentException("External API baseUrl must be provided in application.properties");
        }

        String trimmedApiKey = apiKey; // Final variable for lambda
        ExchangeFilterFunction addKeyQuery = (request, next) -> {
            URI uriWithKey = UriComponentsBuilder.fromUri(request.url())
                    .queryParam("key", trimmedApiKey)  // ADD THE API KEY HERE!
                    .build(true)
                    .toUri();
            ClientRequest newReq = ClientRequest.from(request)
                    .url(uriWithKey)
                    .build();
            return next.exchange(newReq);
        };

        return b.clone()
                .baseUrl(baseUrl)
                .filter(addKeyQuery)
                .build();
    }
}