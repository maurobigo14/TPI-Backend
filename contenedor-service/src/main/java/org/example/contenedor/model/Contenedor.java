package org.example.contenedor.model;

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
@Table(name = "contenedores")
public class Contenedor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "peso")
    @Min(value = 0, message = "El peso no puede ser negativo")
    private Integer peso;

    @Column(name = "volumen")
    @Min(value = 0, message = "El volumen no puede ser negativo")
    private Integer volumen;

    @Column(name = "estado")
    private String estado;

    @Column(name = "cliente")
    private String clienteDni;
}