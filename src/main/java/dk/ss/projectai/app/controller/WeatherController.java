package dk.ss.projectai.app.controller;

import dk.ss.projectai.app.service.WeatherService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WeatherController {

    private final WeatherService weatherService;

    public WeatherController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    @GetMapping("/weather/current")
    public ResponseEntity<?> current(@RequestParam String q) {
        // Simpelt: blokér for demo (i realtime app brug reactive)
        String json = weatherService.getCurrentWeatherRaw(q).block();
        return ResponseEntity.ok(json);
    }
}
