package org.example.transporte.repository;

import org.example.transporte.model.Camion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CamionRepository extends JpaRepository<Camion, Integer> {
    List<Camion> findByDisponibilidad(Boolean disponibilidad);
    List<Camion> findByCapacidadPesoGreaterThanEqualAndCapacidadVolumenGreaterThanEqual(Double peso, Double volumen);
}