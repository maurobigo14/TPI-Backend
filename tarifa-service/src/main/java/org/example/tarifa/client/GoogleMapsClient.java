package org.example.tarifa.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@FeignClient(name = "solicitud-service", url = "${solicitud-service.url:http://localhost:8085}")
public interface GoogleMapsClient {

    @GetMapping("/api/google-maps/distance")
    Map<String, Object> getDistance(
            @RequestParam double originLat,
            @RequestParam double originLng,
            @RequestParam double destLat,
            @RequestParam double destLng);
}
