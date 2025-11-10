package org.example.deposito.repository;

import org.example.deposito.model.Deposito;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DepositoRepository extends JpaRepository<Deposito, Long> {
    List<Deposito> findByActivoTrue();
    List<Deposito> findByCiudadIgnoreCase(String ciudad);
    boolean existsByNombreIgnoreCase(String nombre);
}
