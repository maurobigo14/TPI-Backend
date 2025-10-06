package org.example.model;
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
@Table(name="solicitudes")

public class Solicitud {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "numero")
    private int numero;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contenedor")
    private Contenedor contenedor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente")
    private Cliente cliente;

    @Column(name = "costo_estimado")
    @Min(value = 0, message = "La cantidad no puede ser negativa")
    private double costoEstimado;

    @Column(name = "tiempo_estimado")
    @Min(value = 0, message = "La cantidad no puede ser negativa")
    private int tiempoEstimado;

    @Column(name = "costo_final")
    @Min(value = 0, message = "La cantidad no puede ser negativa")
    private double costoFinal;

    @Column(name = "tiempoReal")
    @Min(value = 0, message = "La cantidad no puede ser negativa")
    private int tiempoReal;

    @Override
    public boolean equals(Object o) {
        if  (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Solicitud solicitud = (Solicitud) o;
        return Objects.equals(numero, solicitud.numero);
    }
    @Override
    public int hashCode() {
        return Objects.hash(numero);
    }
}
