package org.example.camion.controller;

import org.example.camion.model.Transportista;
import org.example.camion.service.TransportistaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/transportistas")
public class TransportistaController {
    @Autowired
    private TransportistaService transportistaService;

    @PostMapping
    public ResponseEntity<Transportista> crear(@RequestBody Transportista transportista) {
        Transportista creado = transportistaService.crear(transportista);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Transportista> obtenerPorId(@PathVariable Long id) {
        Optional<Transportista> transportista = transportistaService.obtenerPorId(id);
        return transportista.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<Transportista>> obtenerTodos() {
        List<Transportista> transportistas = transportistaService.obtenerTodos();
        return ResponseEntity.ok(transportistas);
    }

    @GetMapping("/camion/{dominio}")
    public ResponseEntity<List<Transportista>> obtenerPorCamion(@PathVariable String dominio) {
        List<Transportista> transportistas = transportistaService.obtenerPorCamion(dominio);
        return ResponseEntity.ok(transportistas);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Transportista> actualizar(@PathVariable Long id, @RequestBody Transportista transportista) {
        Transportista actualizado = transportistaService.actualizar(id, transportista);
        if (actualizado != null) {
            return ResponseEntity.ok(actualizado);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        if (transportistaService.eliminar(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
