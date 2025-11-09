package org.example.cliente.controller;

import org.example.cliente.model.Cliente;
import org.example.cliente.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clientes")
public class ClienteController {

    private final ClienteService clienteService;

    @Autowired
    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @GetMapping
    public List<Cliente> getAllClientes() {
        return clienteService.findAll();
    }

    @GetMapping("/{dni}")
    public ResponseEntity<Cliente> getClienteByDni(@PathVariable String dni) {
        return clienteService.findByDni(dni)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Cliente createCliente(@RequestBody Cliente cliente) {
        return clienteService.save(cliente);
    }

    @PutMapping("/{dni}")
    public ResponseEntity<Cliente> updateCliente(@PathVariable String dni, @RequestBody Cliente cliente) {
        return clienteService.findByDni(dni)
                .map(existingCliente -> {
                    cliente.setDni(dni);
                    return ResponseEntity.ok(clienteService.save(cliente));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{dni}")
    public ResponseEntity<Void> deleteCliente(@PathVariable String dni) {
        return clienteService.findByDni(dni)
                .map(cliente -> {
                    clienteService.deleteByDni(dni);
                    return ResponseEntity.ok().<Void>build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
}