package org.example.camion.controller;

import java.util.List;

import org.example.camion.model.Camion;
import org.example.camion.service.CamionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/camiones")
public class CamionController {

    @Autowired
    private CamionService camionService;

    @GetMapping
    public ResponseEntity<List<Camion>> getAllCamiones() {
        return ResponseEntity.ok(camionService.getAllCamiones());
    }

    @GetMapping("/{dominio}")
    public ResponseEntity<Camion> getCamionByDominio(@PathVariable String dominio) {
        return camionService.getCamionByDominio(dominio)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/disponibles")
    public ResponseEntity<List<Camion>> getCamionesDisponibles() {
        return ResponseEntity.ok(camionService.getCamionesByDisponibilidad(true));
    }

    @PostMapping
    public ResponseEntity<?> createCamion(@RequestBody Camion camion) {
        try {
            Camion saved = camionService.saveCamion(camion);
            return ResponseEntity.status(201).body(saved);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(java.util.Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(java.util.Map.of("error", "Error al crear el camión: " + e.getMessage()));
        }
    }

    @PutMapping("/{dominio}")
    public ResponseEntity<?> updateCamion(@PathVariable String dominio, @RequestBody Camion camion) {
        try {
            Camion updated = camionService.updateCamion(dominio, camion);
            if (updated == null) {
                return ResponseEntity.status(404).body(java.util.Map.of("error", "Camión no encontrado con dominio: " + dominio));
            }
            return ResponseEntity.ok(updated);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(java.util.Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(java.util.Map.of("error", "Error al actualizar el camión: " + e.getMessage()));
        }
    }

    @DeleteMapping("/{dominio}")
    public ResponseEntity<Void> deleteCamion(@PathVariable String dominio) {
        boolean deleted = camionService.deleteCamion(dominio);
        if (!deleted) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }
}
