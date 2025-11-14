package org.example.solicitud.repository;

import org.example.solicitud.model.Ruta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RutaRepository extends JpaRepository<Ruta, Long> {
    Optional<Ruta> findBySolicitudId(Long solicitudId);
}
