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
        // Validar que el dominio no esté vacío
        if (camion.getDominio() == null || camion.getDominio().trim().isEmpty()) {
            throw new IllegalArgumentException("El dominio del camión no puede estar vacío");
        }
        
        // Validar que el dominio no exista
        if (camionRepository.findByDominio(camion.getDominio()).isPresent()) {
            throw new IllegalArgumentException("Ya existe un camión con el dominio: " + camion.getDominio());
        }
        
        // Validar campos obligatorios
        if (camion.getNombreTransportista() == null || camion.getNombreTransportista().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del transportista es obligatorio");
        }
        
        if (camion.getTelefono() == null || camion.getTelefono().trim().isEmpty()) {
            throw new IllegalArgumentException("El teléfono es obligatorio");
        }
        
        // Validar valores positivos
        if (camion.getCapacidadPeso() <= 0) {
            throw new IllegalArgumentException("La capacidad de peso debe ser mayor a 0");
        }
        
        if (camion.getCapacidadVolumen() <= 0) {
            throw new IllegalArgumentException("La capacidad de volumen debe ser mayor a 0");
        }
        
        if (camion.getCostos() < 0) {
            throw new IllegalArgumentException("Los costos no pueden ser negativos");
        }
        
        return camionRepository.save(camion);
    }

    public Camion updateCamion(String dominio, Camion camion) {
        Optional<Camion> existingCamionOpt = camionRepository.findByDominio(dominio);
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
        if (camion.getCapacidadPeso() > 0) {
            existingCamion.setCapacidadPeso(camion.getCapacidadPeso());
        }
        if (camion.getCapacidadVolumen() > 0) {
            existingCamion.setCapacidadVolumen(camion.getCapacidadVolumen());
        }
        existingCamion.setDisponibilidad(camion.isDisponibilidad());
        if (camion.getCostos() >= 0) {
            existingCamion.setCostos(camion.getCostos());
        }
        return camionRepository.save(existingCamion);
    }

    public boolean deleteCamion(String dominio) {
        Optional<Camion> camionOpt = camionRepository.findByDominio(dominio);
        if (camionOpt.isEmpty()) {
            return false;
        }
        camionRepository.delete(camionOpt.get());
        return true;
    }
}
