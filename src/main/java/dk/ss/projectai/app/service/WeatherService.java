package dk.ss.projectai.app.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class WeatherService {

    private final WebClient weatherClient;

    public WeatherService(@Qualifier("externalWebClient") WebClient weatherClient) {
        this.weatherClient = weatherClient;
    }

    /**
     * Henter current weather som JSON string for 'q' (fx "Copenhagen" eller lat,long).
     * Returnerer reactive Mono<String>. I controller kan du .block() hvis du vil synkront.
     */
    public Mono<String> getCurrentWeatherRaw(String q) {
        return weatherClient.get()
                .uri(uriBuilder -> uriBuilder.path("/current.json")
                        .queryParam("q", q)
                        .build())
                .retrieve()
                .bodyToMono(String.class);
    }
}
