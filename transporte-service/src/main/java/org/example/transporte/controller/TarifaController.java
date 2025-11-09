package org.example.transporte.controller;

import org.example.transporte.model.Tarifa;
import org.example.transporte.service.TarifaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tarifas")
public class TarifaController {

    private final TarifaService tarifaService;

    @Autowired
    public TarifaController(TarifaService tarifaService) {
        this.tarifaService = tarifaService;
    }

    @GetMapping
    public List<Tarifa> getAllTarifas() {
        return tarifaService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Tarifa> getTarifaById(@PathVariable Long id) {
        return tarifaService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/buscar")
    public List<Tarifa> getTarifasByDescripcion(@RequestParam String descripcion) {
        return tarifaService.findByDescripcion(descripcion);
    }

    @GetMapping("/rango-costo")
    public List<Tarifa> getTarifasByRangoCosto(
            @RequestParam Double min,
            @RequestParam Double max) {
        return tarifaService.findByCostoBaseKmRange(min, max);
    }

    @PostMapping
    public Tarifa createTarifa(@RequestBody Tarifa tarifa) {
        return tarifaService.save(tarifa);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Tarifa> updateTarifa(
            @PathVariable Long id,
            @RequestBody Tarifa tarifa) {
        return tarifaService.findById(id)
                .map(existingTarifa -> {
                    tarifa.setId(id);
                    return ResponseEntity.ok(tarifaService.save(tarifa));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTarifa(@PathVariable Long id) {
        return tarifaService.findById(id)
                .map(tarifa -> {
                    tarifaService.deleteById(id);
                    return ResponseEntity.ok().<Void>build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
}