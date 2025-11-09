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
@Table(name="clientes")

public class Cliente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "dni")
    private String dni;

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "apellido", length = 15)
    private String apellido;

    @Column(name = "email", length = 20)
    private String email;

    @Column(name = "telefono", length = 15)
    private String telefono;

    @Override
    public boolean equals(Object o) {
        if  (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cliente cliente = (Cliente) o;
        return Objects.equals(dni, cliente.dni);
    }
    @Override
    public int hashCode() {
        return Objects.hash(dni);
    }
    }

