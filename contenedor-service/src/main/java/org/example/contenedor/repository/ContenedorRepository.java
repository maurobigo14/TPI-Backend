package org.example.contenedor.repository;

import org.example.contenedor.model.Contenedor;
import org.example.contenedor.model.EstadoContenedor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContenedorRepository extends JpaRepository<Contenedor, Integer> {
    List<Contenedor> findByClienteDni(String clienteDni);
    List<Contenedor> findByEstado(EstadoContenedor estado);
}