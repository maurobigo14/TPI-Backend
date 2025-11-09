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
@Table(name = "camiones")
public class Camion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "dominio")
    private Integer dominio;

    @Column(name = "nombre_transportista", nullable = false)
    private String nombreTransportista;

    @Column(name = "telefono", nullable = false)
    private String telefono;

    @Column(name = "capacidad_peso", nullable = false)
    @Min(value = 0, message = "La capacidad de peso no puede ser negativa")
    private Double capacidadPeso;

    @Column(name = "capacidad_volumen", nullable = false)
    @Min(value = 0, message = "La capacidad de volumen no puede ser negativa")
    private Double capacidadVolumen;

    @Column(name = "disponibilidad", nullable = false)
    private Boolean disponibilidad;

    @Column(name = "costos", nullable = false)
    @Min(value = 0, message = "Los costos no pueden ser negativos")
    private Double costos;
}