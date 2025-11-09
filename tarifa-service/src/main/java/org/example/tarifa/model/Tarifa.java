package org.example.tarifa.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
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
    @Min(value = 0, message = "La cantidad no puede ser negativa")
    private Double costoBaseKm;

    @Column(name = "valor_litro_combustible")
    @Min(value = 0, message = "La cantidad no puede ser negativa")
    private Double valorLitroCombustible;

    @Column(name = "cosumo_promedio_km")
    @Min(value = 0, message = "La cantidad no puede ser negativa")
    private Double consumoPromedioKm;

    @Column(name = "costo_estadia_diaria")
    @Min(value = 0, message = "La cantidad no puede ser negativa")
    private Double costoEstadiaDiaria;

    @Column(name = "cargo_gestion")
    @Min(value = 0, message = "La cantidad no puede ser negativa")
    private Double cargoGestion;
}