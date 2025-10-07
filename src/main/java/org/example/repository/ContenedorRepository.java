package org.example.repository;

import org.example.model.Contenedor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ContenedorRepository extends JpaRepository<Contenedor, Long> {

    Optional<Contenedor> findByIdentificacion(String identificacion);

    List<Contenedor> findByEstado(String estado);
}

