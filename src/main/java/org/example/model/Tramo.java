package org.example.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.example.model.enums.Estado;
import org.example.model.enums.Tipo;
import lombok.Data;
import lombok.AllArgsConstructor;

import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Entity
@Table(name="solicitudes")

public class Tramo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tramo")
    private int idTramo;

    @Column(name = "origen")
    private String origen;

    @Column(name = "destino")
    private String destino;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo")
    private Tipo tipo;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado")
    private Estado estado;

    @Column(name = "costo_aproximado")
    @Min(value = 0, message = "La cantidad no puede ser negativa")
    private double costoAproximado;

    @Column(name = "costo_real")
    @Min(value = 0, message = "La cantidad no puede ser negativa")
    private double costoReal;

    @Column(name = "fecha_hora_inicio")
    private int fechaHoraInicio;

    @Column(name = "fecha_hora_fin")
    private int fechaHoraFin;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "camion")
    private Camion camion;

    @Override
    public boolean equals(Object o) {
        if  (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tramo tramo = (Tramo) o;
        return Objects.equals(idTramo, tramo.idTramo);
    }
    @Override
    public int hashCode() {
        return Objects.hash(idTramo);
    }
}
