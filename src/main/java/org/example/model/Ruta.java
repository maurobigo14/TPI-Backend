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
@Table(name="rutas")

public class Ruta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "solicitud")
    private Solicitud solicitud;

    @Column(name = "cantidad_tramos")
    @Min(value = 0, message = "La cantidad no puede ser negativa")
    private int cantidadTramos;

    @Column(name = "cantidad_depositos")
    @Min(value = 0, message = "La cantidad no puede ser negativa")
    private int cantidadDepositos;

    @Override
    public boolean equals(Object o) {
        if  (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ruta ruta = (Ruta) o;
        return Objects.equals(id, ruta.id);
    }
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
