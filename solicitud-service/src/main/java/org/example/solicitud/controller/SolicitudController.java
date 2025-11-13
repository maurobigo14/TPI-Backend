package org.example.solicitud.controller;

import org.example.solicitud.dto.SolicitudCrearRequest;
import org.example.solicitud.dto.SolicitudResponse;
import org.example.solicitud.service.SolicitudService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/solicitudes")
public class SolicitudController {

    private final SolicitudService solicitudService;

    public SolicitudController(SolicitudService solicitudService) {
        this.solicitudService = solicitudService;
    }

    @GetMapping
    public List<SolicitudResponse> list() {
        return solicitudService.listarSolicitudes();
    }

    @GetMapping("/cliente/{dni}")
    public List<SolicitudResponse> byCliente(@PathVariable String dni) {
        return solicitudService.buscarPorCliente(dni);
    }

    @PostMapping
    public ResponseEntity<SolicitudResponse> create(@RequestBody SolicitudCrearRequest request) {

        SolicitudResponse response = solicitudService.crearSolicitud(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
