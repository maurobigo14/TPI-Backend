package org.example.contenedor.controller;

import org.example.contenedor.model.Contenedor;
import org.example.contenedor.dto.ContenedorPendienteResponse;
import org.example.contenedor.service.ContenedorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

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
    public ResponseEntity<?> createContenedor(@RequestBody Contenedor contenedor) {
        try {
            Contenedor saved = contenedorService.save(contenedor);
            return ResponseEntity.status(201).body(saved);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(409).body(e.getMessage());
        } catch (org.springframework.dao.DataIntegrityViolationException e) {
            return ResponseEntity.status(409).body("Constraint violation: " + e.getMostSpecificCause().getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error al crear contenedor: " + e.getMessage());
        }
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
        return contenedorService.findPendientesDeEntrega()
                .stream()
                .map(c -> ContenedorPendienteResponse.builder()
                        .id(c.getId())
                        .numeroIdentificacion(c.getNumeroIdentificacion())
                        .peso(c.getPeso())
                        .volumen(c.getVolumen())
                        .estado(c.getEstado().getValor())
                        .clienteDni(c.getClienteDni())
                        .solicitudId(c.getSolicitudId())
                        .build())
                .collect(Collectors.toList());
    }

    /**
     * Endpoint para obtener la ubicación actual del contenedor.
     * La ubicación se determina consultando el último tramo completado de la ruta.
     * Requiere integración con solicitud-service para obtener información de tramos.
     */
    @GetMapping("/{id}/ubicacion-actual")
    public ResponseEntity<String> getUbicacionActual(@PathVariable Integer id) {
        try {
            String ubicacion = contenedorService.obtenerUbicacionActual(id);
            return ResponseEntity.ok(ubicacion);
        } catch (UnsupportedOperationException e) {
            return ResponseEntity.status(501).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error al obtener ubicación: " + e.getMessage());
        }
    }
}