package org.example.model;
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
@Table(name="depositos")

public class Deposito {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int identificacion;

    @Column(name = "nombre", length = 15)
    private String nombre;

    @Column(name = "direccion", length = 20)
    private String direccion;

    @Column(name = "coordenadas", length = 15)
    private int coordenadas;

    @Override
    public boolean equals(Object o) {
        if  (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Deposito depositos = (Deposito) o;
        return Objects.equals(identificacion, depositos.identificacion);
    }
    @Override
    public int hashCode() {
        return Objects.hash(identificacion);
    }
}
