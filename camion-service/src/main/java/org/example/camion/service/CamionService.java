package org.example.camion.service;

import java.util.List;
import java.util.Optional;

import org.example.camion.model.Camion;
import org.example.camion.repository.CamionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public Camion updateCamion(String dominio, Camion camion) {
        if (!camionRepository.existsById(dominio)) {
            return null;
        }
        camion.setDominio(dominio);
        return camionRepository.save(camion);
    }

    public boolean deleteCamion(String dominio) {
        if (!camionRepository.existsById(dominio)) {
            return false;
        }
        camionRepository.deleteById(dominio);
        return true;
    }
}
