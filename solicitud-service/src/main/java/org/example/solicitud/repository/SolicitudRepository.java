package org.example.solicitud.repository;

import org.example.solicitud.model.Solicitud;
import org.example.solicitud.model.EstadoSolicitud;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SolicitudRepository extends JpaRepository<Solicitud, Long> {
    List<Solicitud> findByClienteDni(String dni);
    List<Solicitud> findByContenedorId(Long contenedorId);
    List<Solicitud> findByEstado(EstadoSolicitud estado);
}