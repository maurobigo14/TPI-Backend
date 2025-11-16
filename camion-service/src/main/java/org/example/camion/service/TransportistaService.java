package org.example.camion.service;

import org.example.camion.model.Transportista;
import org.example.camion.repository.TransportistaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TransportistaService {
    @Autowired
    private TransportistaRepository transportistaRepository;

    public Transportista crear(Transportista transportista) {
        return transportistaRepository.save(transportista);
    }

    public Optional<Transportista> obtenerPorId(Long id) {
        return transportistaRepository.findById(id);
    }

    public List<Transportista> obtenerTodos() {
        return transportistaRepository.findAll();
    }

    public List<Transportista> obtenerPorCamion(String dominio) {
        return transportistaRepository.findByCamion_Dominio(dominio);
    }

    public Transportista actualizar(Long id, Transportista transportista) {
        if (transportistaRepository.existsById(id)) {
            transportista.setIdTransportista(id);
            return transportistaRepository.save(transportista);
        }
        return null;
    }

    public boolean eliminar(Long id) {
        if (transportistaRepository.existsById(id)) {
            transportistaRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
