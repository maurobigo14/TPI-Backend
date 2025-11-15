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
    public ResponseEntity<Camion> getCamionByDominio(@PathVariable Integer dominio) {
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

    @PutMapping("/{dominio}")
    public ResponseEntity<Camion> updateCamion(@PathVariable Integer dominio, @RequestBody Camion camion) {
        camion.setDominio(dominio);
        return ResponseEntity.ok(camionService.saveCamion(camion));
    }

    @DeleteMapping("/{dominio}")
    public ResponseEntity<Void> deleteCamion(@PathVariable Integer dominio) {
        camionService.deleteCamion(dominio);
        return ResponseEntity.ok().build();
    }
}