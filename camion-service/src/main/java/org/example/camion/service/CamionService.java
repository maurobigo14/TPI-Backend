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
        Optional<Camion> existingCamionOpt = camionRepository.findById(dominio);
        if (existingCamionOpt.isEmpty()) {
            return null;
        }
        Camion existingCamion = existingCamionOpt.get();
        // Actualizar solo los campos que vienen en el request
        if (camion.getNombreTransportista() != null) {
            existingCamion.setNombreTransportista(camion.getNombreTransportista());
        }
        if (camion.getTelefono() != null) {
            existingCamion.setTelefono(camion.getTelefono());
        }
        existingCamion.setCapacidadPeso(camion.getCapacidadPeso());
        existingCamion.setCapacidadVolumen(camion.getCapacidadVolumen());
        existingCamion.setDisponibilidad(camion.isDisponibilidad());
        existingCamion.setCostos(camion.getCostos());
        return camionRepository.save(existingCamion);
    }

    public boolean deleteCamion(String dominio) {
        if (!camionRepository.existsById(dominio)) {
            return false;
        }
        camionRepository.deleteById(dominio);
        return true;
    }
}
