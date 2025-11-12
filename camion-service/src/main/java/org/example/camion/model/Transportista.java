package org.example.camion.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Entity
@Table(name = "transportistas")
public class Transportista {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_transportista")
    private Long idTransportista;

    @Column(name = "nombre", length = 100, nullable = false)
    private String nombre;

    @Column(name = "telefono", length = 15, nullable = false)
    private String telefono;

    @ManyToOne
    @JoinColumn(name = "id_camion", nullable = false, foreignKey = @ForeignKey(name = "fk_transportista_camion"))
    private Camion camion;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Transportista transportista = (Transportista) o;
        return Objects.equals(idTransportista, transportista.idTransportista);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idTransportista);
    }
}
