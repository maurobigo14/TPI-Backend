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
        if (contenedor.getNumeroIdentificacion() != null) {
            var existing = contenedorRepository.findByNumeroIdentificacion(contenedor.getNumeroIdentificacion());
            if (existing.isPresent()) {
                throw new IllegalStateException("Contenedor con numeroIdentificacion ya existe: " + contenedor.getNumeroIdentificacion());
            }
        }
        return contenedorRepository.save(contenedor);
    }

    public void deleteById(Integer id) {
        contenedorRepository.deleteById(id);
    }

    /**
     * Obtiene la ubicación actual del contenedor basándose en el último tramo completado.
     * Debe ser invocado desde el solicitud-service que tiene acceso a los tramos.
     * La ubicación real se determina por: depósito destino del último tramo COMPLETADO.
     * @param contenedorId ID del contenedor
     * @return String con la ubicación actual (nombre del depósito)
     */
    public String obtenerUbicacionActual(Integer contenedorId) {
        // Esta lógica requiere comunicación con solicitud-service via Feign Client
        // para obtener la información de tramos completados.
        // Por ahora retornamos un placeholder indicando que debe implementarse la integración.
        throw new UnsupportedOperationException(
            "La ubicación actual debe obtenerse desde solicitud-service consultando el último tramo completado"
        );
    }
}