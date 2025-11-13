package org.example.solicitud.client;

import org.example.solicitud.dto.contenedor.ContenedorRequest;
import org.example.solicitud.dto.contenedor.ContenedorResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
        name = "contenedor-service",
        url = "http://localhost:8082/api/contenedores"
)
public interface ContenedorClient {

    @PostMapping
    ContenedorResponse crearContenedor(@RequestBody ContenedorRequest request);
}
