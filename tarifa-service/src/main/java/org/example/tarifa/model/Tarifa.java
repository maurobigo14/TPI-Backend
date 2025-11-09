package org.example.tarifa.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Entity
@Table(name="tarifas")
public class Tarifa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "descripcion")
    private String descripcion;

    @Column(name = "costo_base_km")
    private Double costoBaseKm;

    @Column(name = "valor_litro_combustible")
    private Double valorLitroCombustible;

    @Column(name = "cosumo_promedio_km")
    private Double consumoPromedioKm;

    @Column(name = "costo_estadia_diaria")
    private Double costoEstadiaDiaria;

    @Column(name = "cargo_gestion")
    private Double cargoGestion;
}