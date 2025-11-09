package org.example.transporte.service;

import org.example.transporte.model.Camion;
import org.example.transporte.repository.CamionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CamionService {

    private final CamionRepository camionRepository;

    @Autowired
    public CamionService(CamionRepository camionRepository) {
        this.camionRepository = camionRepository;
    }

    public List<Camion> findAll() {
        return camionRepository.findAll();
    }

    public Optional<Camion> findById(Integer dominio) {
        return camionRepository.findById(dominio);
    }

    public List<Camion> findByDisponibilidad(Boolean disponibilidad) {
        return camionRepository.findByDisponibilidad(disponibilidad);
    }

    public List<Camion> findByCapacidades(Double peso, Double volumen) {
        return camionRepository.findByCapacidadPesoGreaterThanEqualAndCapacidadVolumenGreaterThanEqual(peso, volumen);
    }

    public Camion save(Camion camion) {
        return camionRepository.save(camion);
    }

    public void deleteById(Integer dominio) {
        camionRepository.deleteById(dominio);
    }
}