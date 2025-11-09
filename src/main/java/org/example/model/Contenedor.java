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
@Table(name="contenedores")

public class Contenedor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int identificacion;

    @Column(name = "peso")
    @Min(value = 0, message = "La cantidad no puede ser negativa")
    private int peso;

    @Column(name = "volumen")
    @Min(value = 0, message = "La cantidad no puede ser negativa")
    private int volumen;

    @Column(name = "estado")
    private String estado;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente")
    private Cliente clienteAsociado;

    @Override
    public boolean equals(Object o) {
        if  (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Contenedor contenedor = (Contenedor) o;
        return Objects.equals(identificacion, contenedor.identificacion);
    }
    @Override
    public int hashCode() {
        return Objects.hash(identificacion);
    }
}
