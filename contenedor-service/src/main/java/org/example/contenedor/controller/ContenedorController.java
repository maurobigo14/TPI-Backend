package org.example.contenedor.controller;

import org.example.contenedor.model.Contenedor;
import org.example.contenedor.dto.ContenedorPendienteResponse;
import org.example.contenedor.service.ContenedorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/contenedores")
public class ContenedorController {

    private final ContenedorService contenedorService;

    @Autowired
    public ContenedorController(ContenedorService contenedorService) {
        this.contenedorService = contenedorService;
    }

    @GetMapping
    public List<Contenedor> getAllContenedores() {
        return contenedorService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Contenedor> getContenedorById(@PathVariable Integer id) {
        return contenedorService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/cliente/{dni}")
    public List<Contenedor> getContenedoresByClienteDni(@PathVariable String dni) {
        return contenedorService.findByClienteDni(dni);
    }

    @GetMapping("/estado/{estado}")
    public List<Contenedor> getContenedoresByEstado(@PathVariable String estado) {
        return contenedorService.findByEstado(estado);
    }

    @PostMapping
    public Contenedor createContenedor(@RequestBody Contenedor contenedor) {
        return contenedorService.save(contenedor);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Contenedor> updateContenedor(@PathVariable Integer id, @RequestBody Contenedor contenedor) {
        return contenedorService.findById(id)
                .map(existingContenedor -> {
                    contenedor.setId(id);
                    return ResponseEntity.ok(contenedorService.save(contenedor));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteContenedor(@PathVariable Integer id) {
        return contenedorService.findById(id)
                .map(contenedor -> {
                    contenedorService.deleteById(id);
                    return ResponseEntity.ok().<Void>build();
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/pendientes/entrega")
    public List<ContenedorPendienteResponse> getPendientesDeEntrega() {
        return contenedorService.findPendientesDeEntrega();
    }
}
