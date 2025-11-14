package org.example.solicitud.repository;

import org.example.solicitud.model.Tramo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TramoRepository extends JpaRepository<Tramo, Long> {
    List<Tramo> findByRutaId(Long rutaId);
}
