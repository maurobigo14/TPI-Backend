package org.example.camion.model;

import jakarta.persistence.*;
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
    @Column(name = "dominio", unique = true, nullable = false, length = 20)
    private String dominio;

    @Column(name = "nombre_transportista", length = 15, nullable = false)
    private String nombreTransportista;

    @Column(name = "telefono", length = 15, nullable = false)
    private String telefono;

    @Column(name = "capacidad_peso", nullable = false)
    private double capacidadPeso;

    @Column(name = "capacidad_volumen", nullable = false)
    private double capacidadVolumen;

    @Column(name = "disponibilidad", nullable = false)
    private boolean disponibilidad;

    @Column(name = "costos", nullable = false)
    private double costos;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Camion camion = (Camion) o;
        return dominio != null && dominio.equals(camion.dominio);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(dominio);
    }
}
