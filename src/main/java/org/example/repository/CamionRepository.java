package org.example.respository;

import org.example.model.Contenedor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface CamionRepository extends JpaRepository<Camion, Long> {

    Optional<Camion> findByDominio(String dominio);

    List<Camion> findByDisponibilidad(Boolean disponibilidad);
}