package org.example.contenedor.repository;

import org.example.contenedor.model.Contenedor;
import org.example.contenedor.model.EstadoContenedor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ContenedorRepository extends JpaRepository<Contenedor, Integer> {
    List<Contenedor> findByClienteDni(String clienteDni);
    List<Contenedor> findByEstado(EstadoContenedor estado);
    Optional<Contenedor> findByNumeroIdentificacion(String numeroIdentificacion);
}