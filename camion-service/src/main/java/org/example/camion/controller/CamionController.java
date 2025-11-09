package org.example.camion.controller;

import org.example.camion.model.Camion;
import org.example.camion.service.CamionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public ResponseEntity<Camion> createCamion(@RequestBody Camion camion) {
        return ResponseEntity.ok(camionService.saveCamion(camion));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Camion> updateCamion(@PathVariable Long id, @RequestBody Camion camion) {
        return ResponseEntity.ok(camionService.saveCamion(camion));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCamion(@PathVariable Long id) {
        camionService.deleteCamion(id);
        return ResponseEntity.ok().build();
    }
}