package org.example.solicitud.controller;

import org.example.solicitud.service.GoogleMapsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/google-maps")
public class GoogleMapsController {

    private final GoogleMapsService googleMapsService;

    public GoogleMapsController(GoogleMapsService googleMapsService) {
        this.googleMapsService = googleMapsService;
    }

    /**
     * Calcula la distancia y tiempo entre dos puntos usando Google Maps
     * @param originLat Latitud del origen
     * @param originLng Longitud del origen
     * @param destLat Latitud del destino
     * @param destLng Longitud del destino
     * @return Objeto con distancia (km) y tiempo (minutos)
     */
    @GetMapping("/distance")
    public ResponseEntity<?> getDistance(
            @RequestParam double originLat,
            @RequestParam double originLng,
            @RequestParam double destLat,
            @RequestParam double destLng) {

        GoogleMapsService.DistanceInfo info = googleMapsService.getDistance(
                originLat, originLng, destLat, destLng);

        Map<String, Object> response = new HashMap<>();
        response.put("success", info.isSuccess());
        
        if (info.isSuccess()) {
            response.put("distanceKm", info.getDistanceKm());
            response.put("durationMinutes", info.getDurationMinutes());
            response.put("message", "Distancia calculada exitosamente");
        } else {
            response.put("message", "No se pudo calcular la distancia. Verifica las coordenadas.");
            response.put("distanceKm", 0.0);
            response.put("durationMinutes", 0);
        }

        return ResponseEntity.ok(response);
    }
}
