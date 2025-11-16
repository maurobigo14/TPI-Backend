package org.example.contenedor.service;

import org.example.contenedor.model.Contenedor;
import org.example.contenedor.model.EstadoContenedor;
import org.example.contenedor.repository.ContenedorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ContenedorService {

    private final ContenedorRepository contenedorRepository;

    @Autowired
    public ContenedorService(ContenedorRepository contenedorRepository) {
        this.contenedorRepository = contenedorRepository;
    }

    public List<Contenedor> findAll() {
        return contenedorRepository.findAll();
    }

    public Optional<Contenedor> findById(Integer id) {
        return contenedorRepository.findById(id);
    }

    public List<Contenedor> findByClienteDni(String clienteDni) {
        return contenedorRepository.findByClienteDni(clienteDni);
    }

    public List<Contenedor> findByEstado(String estado) {
        try {
            // Convertir el String al enum EstadoContenedor
            EstadoContenedor estadoEnum = EstadoContenedor.valueOf(estado.toUpperCase());
            return contenedorRepository.findByEstado(estadoEnum);
        } catch (IllegalArgumentException e) {
            // Si el estado no es válido, retornar lista vacía
            return List.of();
        }
    }

    public List<Contenedor> findPendientesDeEntrega() {
        return contenedorRepository.findByEstado(EstadoContenedor.EN_TRANSITO);
    }

    public Contenedor save(Contenedor contenedor) {
        return contenedorRepository.save(contenedor);
    }

    public void deleteById(Integer id) {
        contenedorRepository.deleteById(id);
    }
}