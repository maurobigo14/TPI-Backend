package org.example.ruta.controller;

import org.example.ruta.model.Ruta;
import org.example.ruta.repository.RutaRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rutas")
public class RutaController {

    private final RutaRepository rutaRepository;

    public RutaController(RutaRepository rutaRepository) {
        this.rutaRepository = rutaRepository;
    }

    @GetMapping
    public List<Ruta> list() {
        return rutaRepository.findAll();
    }

    @GetMapping("/solicitud/{id}")
    public List<Ruta> bySolicitud(@PathVariable Integer id) {
        return rutaRepository.findBySolicitudId(id);
    }

    @PostMapping
    public Ruta create(@RequestBody Ruta ruta) {
        return rutaRepository.save(ruta);
    }
}