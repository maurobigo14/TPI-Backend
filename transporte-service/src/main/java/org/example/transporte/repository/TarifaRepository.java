package org.example.transporte.repository;

import org.example.transporte.model.Tarifa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TarifaRepository extends JpaRepository<Tarifa, Long> {
    List<Tarifa> findByDescripcionContainingIgnoreCase(String descripcion);
    List<Tarifa> findByCostoBaseKmBetween(Double min, Double max);
}