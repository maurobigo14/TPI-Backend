package org.example.transporte.controller;

import org.example.transporte.model.Camion;
import org.example.transporte.service.CamionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/camiones")
public class CamionController {

    private final CamionService camionService;

    @Autowired
    public CamionController(CamionService camionService) {
        this.camionService = camionService;
    }

    @GetMapping
    public List<Camion> getAllCamiones() {
        return camionService.findAll();
    }

    @GetMapping("/{dominio}")
    public ResponseEntity<Camion> getCamionByDominio(@PathVariable Integer dominio) {
        return camionService.findById(dominio)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/disponibles")
    public List<Camion> getCamionesDisponibles() {
        return camionService.findByDisponibilidad(true);
    }

    @GetMapping("/capacidad")
    public List<Camion> getCamionesPorCapacidad(
            @RequestParam Double peso,
            @RequestParam Double volumen) {
        return camionService.findByCapacidades(peso, volumen);
    }

    @PostMapping
    public Camion createCamion(@RequestBody Camion camion) {
        return camionService.save(camion);
    }

    @PutMapping("/{dominio}")
    public ResponseEntity<Camion> updateCamion(
            @PathVariable Integer dominio,
            @RequestBody Camion camion) {
        return camionService.findById(dominio)
                .map(existingCamion -> {
                    camion.setDominio(dominio);
                    return ResponseEntity.ok(camionService.save(camion));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{dominio}")
    public ResponseEntity<Void> deleteCamion(@PathVariable Integer dominio) {
        return camionService.findById(dominio)
                .map(camion -> {
                    camionService.deleteById(dominio);
                    return ResponseEntity.ok().<Void>build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
}