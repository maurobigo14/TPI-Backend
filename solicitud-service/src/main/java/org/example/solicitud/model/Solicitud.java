package org.example.solicitud.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "solicitudes")
public class Solicitud {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer numero;

    private Integer contenedorId;

    private String clienteDni;

    private Double costoEstimado;

    private Integer tiempoEstimado;

    private Double costoFinal;

    private Integer tiempoReal;
}