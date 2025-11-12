package main.java.org.example.camion.repository;

import org.example.camion.model.Transportista;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransportistaRepository extends JpaRepository<Transportista, Long> {
    List<Transportista> findByCamionDominio(int dominio);
}
