package org.example.camion.repository;

import java.util.List;
import java.util.Optional;

import org.example.camion.model.Camion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CamionRepository extends JpaRepository<Camion, String> {
    Optional<Camion> findByDominio(String dominio);
    List<Camion> findByDisponibilidad(Boolean disponibilidad);
}
