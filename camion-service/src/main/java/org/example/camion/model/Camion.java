package org.example.camion.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.Builder;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Entity
@Table(name="camiones")
public class Camion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "dominio", unique = true, nullable = false)
    private int dominio;

    @Column(name = "nombre_transportista", length = 15, nullable = false)
    private String nombreTransportista;

    @Column(name = "telefono", length = 15, nullable = false)
    @Min(value = 0, message = "La cantidad no puede ser negativa")
    private String telefono;

    @Column(name = "capacidad_peso", nullable = false)
    @Min(value = 0, message = "La cantidad no puede ser negativa")
    private double capacidadPeso;

    @Column(name = "capacidad_volumen", nullable = false)
    @Min(value = 0, message = "La cantidad no puede ser negativa")
    private double capacidadVolumen;

    @Column(name = "disponibilidad", nullable = false)
    private boolean disponibilidad;

    @Column(name = "costos", nullable = false)
    @Min(value = 0, message = "La cantidad no puede ser negativa")
    private double costos;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Camion camion = (Camion) o;
        return Objects.equals(dominio, camion.dominio);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dominio);
    }
}