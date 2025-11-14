package org.example.solicitud.repository;

import org.example.solicitud.model.AsignacionCamion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AsignacionCamionRepository extends JpaRepository<AsignacionCamion, Long> {
    Optional<AsignacionCamion> findByTramoId(Long tramoId);
    List<AsignacionCamion> findByCamionDominio(Integer dominio);
}
