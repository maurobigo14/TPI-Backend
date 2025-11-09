package org.example.transporte.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tarifas")
public class Tarifa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "descripcion")
    private String descripcion;

    @Column(name = "costo_base_km")
    @Min(value = 0, message = "El costo base por km no puede ser negativo")
    private Double costoBaseKm;

    @Column(name = "valor_litro_combustible")
    @Min(value = 0, message = "El valor del litro de combustible no puede ser negativo")
    private Double valorLitroCombustible;

    @Column(name = "consumo_promedio_km")
    @Min(value = 0, message = "El consumo promedio por km no puede ser negativo")
    private Double consumoPromedioKm;

    @Column(name = "costo_estadia_diaria")
    @Min(value = 0, message = "El costo de estadía diaria no puede ser negativo")
    private Double costoEstadiaDiaria;

    @Column(name = "cargo_gestion")
    @Min(value = 0, message = "El cargo de gestión no puede ser negativo")
    private Double cargoGestion;
}