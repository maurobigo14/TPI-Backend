package org.example.solicitud.client;

import org.example.solicitud.dto.camion.CamionResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "camion-service", url = "${camion-service.url:http://camion-service:8083}")
public interface CamionClient {

    @GetMapping("/api/camiones/{dominio}")
    CamionResponse obtenerPorDominio(@PathVariable("dominio") Integer dominio);
}
