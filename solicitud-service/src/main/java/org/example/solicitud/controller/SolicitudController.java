package org.example.solicitud.controller;

import org.example.solicitud.dto.SolicitudCrearRequest;
import org.example.solicitud.dto.SolicitudResponse;
import org.example.solicitud.dto.SolicitudDetalleResponse;
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

    @GetMapping("/{numero}")
    public ResponseEntity<SolicitudDetalleResponse> obtenerPorNumero(@PathVariable Long numero) {
        return solicitudService.obtenerPorNumero(numero)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/cliente/{dni}")
    public List<SolicitudResponse> byCliente(@PathVariable String dni) {
        return solicitudService.buscarPorCliente(dni);
    }

    @GetMapping("/estado/{estado}")
    public List<SolicitudResponse> porEstado(@PathVariable String estado) {
        return solicitudService.buscarPorEstado(estado);
    }

    @PostMapping
    public ResponseEntity<SolicitudResponse> create(@RequestBody SolicitudCrearRequest request) {

        SolicitudResponse response = solicitudService.crearSolicitud(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/rutas/tentativas")
    public ResponseEntity<List<org.example.solicitud.dto.RutaResponse>> rutasTentativas(@RequestBody SolicitudCrearRequest request) {
        List<org.example.solicitud.dto.RutaResponse> rutas = solicitudService.generarRutasTentativas(request);
        return ResponseEntity.ok(rutas);
    }
}
