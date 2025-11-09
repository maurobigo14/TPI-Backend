package org.example.transporte.service;

import org.example.transporte.model.Tarifa;
import org.example.transporte.repository.TarifaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TarifaService {

    private final TarifaRepository tarifaRepository;

    @Autowired
    public TarifaService(TarifaRepository tarifaRepository) {
        this.tarifaRepository = tarifaRepository;
    }

    public List<Tarifa> findAll() {
        return tarifaRepository.findAll();
    }

    public Optional<Tarifa> findById(Long id) {
        return tarifaRepository.findById(id);
    }

    public List<Tarifa> findByDescripcion(String descripcion) {
        return tarifaRepository.findByDescripcionContainingIgnoreCase(descripcion);
    }

    public List<Tarifa> findByCostoBaseKmRange(Double min, Double max) {
        return tarifaRepository.findByCostoBaseKmBetween(min, max);
    }

    public Tarifa save(Tarifa tarifa) {
        return tarifaRepository.save(tarifa);
    }

    public void deleteById(Long id) {
        tarifaRepository.deleteById(id);
    }
}