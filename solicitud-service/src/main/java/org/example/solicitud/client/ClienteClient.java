package org.example.solicitud.client;

import org.example.solicitud.dto.cliente.ClienteRequest;
import org.example.solicitud.dto.cliente.ClienteResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(
        name = "cliente-service",
        path = "/api/clientes"
)
public interface ClienteClient {

    @GetMapping("/{dni}")
    ClienteResponse obtenerPorDni(@PathVariable String dni);

    @PostMapping
    ClienteResponse crearCliente(@RequestBody ClienteRequest request);
}
