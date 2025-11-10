package org.example.deposito.service;

import org.example.deposito.model.Deposito;
import org.example.deposito.repository.DepositoRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class DepositoService {

    private final DepositoRepository repo;

    public DepositoService(DepositoRepository repo) {
        this.repo = repo;
    }

    public List<Deposito> listarTodos() {
        return repo.findAll();
    }

    public List<Deposito> listarActivos() {
        return repo.findByActivoTrue();
    }

    public List<Deposito> buscarPorCiudad(String ciudad) {
        return repo.findByCiudadIgnoreCase(ciudad);
    }

    public Deposito crear(Deposito d) {
        if (repo.existsByNombreIgnoreCase(d.getNombre())) {
            throw new IllegalArgumentException("Ya existe un depósito con ese nombre");
        }
        return repo.save(d);
    }

    public Deposito actualizar(Long id, Deposito nuevo) {
        Deposito existente = repo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Depósito no encontrado"));
        existente.setDireccion(nuevo.getDireccion());
        existente.setCiudad(nuevo.getCiudad());
        existente.setCostoEstadiaPorDia(nuevo.getCostoEstadiaPorDia());
        existente.setCapacidadMaxima(nuevo.getCapacidadMaxima());
        existente.setActivo(nuevo.getActivo());
        return repo.save(existente);
    }

    public void eliminar(Long id) {
        repo.deleteById(id);
    }
}
