package org.example.solicitud.controller;

import org.example.solicitud.model.Solicitud;
import org.example.solicitud.repository.SolicitudRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/solicitudes")
public class SolicitudController {

    private final SolicitudRepository solicitudRepository;

    public SolicitudController(SolicitudRepository solicitudRepository) {
        this.solicitudRepository = solicitudRepository;
    }

    @GetMapping
    public List<Solicitud> list() {
        return solicitudRepository.findAll();
    }

    @GetMapping("/cliente/{dni}")
    public List<Solicitud> byCliente(@PathVariable String dni) {
        return solicitudRepository.findByClienteDni(dni);
    }

    @PostMapping
    public Solicitud create(@RequestBody Solicitud solicitud) {
        return solicitudRepository.save(solicitud);
    }
}