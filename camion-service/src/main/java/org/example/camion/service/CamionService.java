package org.example.camion.service;

import org.example.camion.model.Camion;
import org.example.camion.repository.CamionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CamionService {
    
    @Autowired
    private CamionRepository camionRepository;

    public List<Camion> getAllCamiones() {
        return camionRepository.findAll();
    }

    public Optional<Camion> getCamionByDominio(String dominio) {
        return camionRepository.findByDominio(dominio);
    }

    public List<Camion> getCamionesByDisponibilidad(Boolean disponibilidad) {
        return camionRepository.findByDisponibilidad(disponibilidad);
    }

    public Camion saveCamion(Camion camion) {
        return camionRepository.save(camion);
    }

    public void deleteCamion(Long id) {
        camionRepository.deleteById(id);
    }
}