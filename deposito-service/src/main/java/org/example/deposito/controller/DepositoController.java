package org.example.deposito.controller;

import org.example.deposito.model.Deposito;
import org.example.deposito.repository.DepositoRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/depositos")
public class DepositoController {

    private final DepositoRepository depositoRepository;

    public DepositoController(DepositoRepository depositoRepository) {
        this.depositoRepository = depositoRepository;
    }

    @GetMapping
    public List<Deposito> list() {
        return depositoRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Deposito> get(@PathVariable Integer id) {
        return depositoRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Deposito create(@RequestBody Deposito deposito) {
        return depositoRepository.save(deposito);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Deposito> update(@PathVariable Integer id, @RequestBody Deposito deposito) {
        return depositoRepository.findById(id)
                .map(existing -> {
                    deposito.setId(id);
                    return ResponseEntity.ok(depositoRepository.save(deposito));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        return depositoRepository.findById(id)
                .map(existing -> {
                    depositoRepository.deleteById(id);
                    return ResponseEntity.ok().<Void>build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
}