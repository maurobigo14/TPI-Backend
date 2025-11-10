package org.example.deposito.controller;

import org.example.deposito.model.Deposito;
import org.example.deposito.service.DepositoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/depositos")
public class DepositoController {

    private final DepositoService service;

    public DepositoController(DepositoService service) {
        this.service = service;
    }

    @GetMapping
    public List<Deposito> listar() {
        return service.listarTodos();
    }

    @GetMapping("/activos")
    public List<Deposito> listarActivos() {
        return service.listarActivos();
    }

    @GetMapping("/ciudad/{nombre}")
    public List<Deposito> porCiudad(@PathVariable String nombre) {
        return service.buscarPorCiudad(nombre);
    }

    @PostMapping
    public ResponseEntity<Deposito> crear(@RequestBody Deposito d) {
        return ResponseEntity.ok(service.crear(d));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Deposito> actualizar(@PathVariable Long id, @RequestBody Deposito d) {
        return ResponseEntity.ok(service.actualizar(id, d));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
