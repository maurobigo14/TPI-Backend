package org.example.solicitud.repository;

import org.example.solicitud.model.Solicitud;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SolicitudRepository extends JpaRepository<Solicitud, Integer> {
    List<Solicitud> findByClienteDni(String dni);
    List<Solicitud> findByContenedorId(Integer contenedorId);
}