package org.example.solicitud.model;

import jakarta.persistence.*;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "solicitudes")
public class Solicitud {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long contenedorId;
    private String clienteDni;

    private String origenDireccion;
    private Double origenLat;
    private Double origenLng;

    private String destinoDireccion;
    private Double destinoLat;
    private Double destinoLng;

    private String estado;

    private Double costoEstimado;
    private Integer tiempoEstimado;

    private Double costoFinal;
    private Integer tiempoReal;
    public Long getNumero() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getNumero'");
    }
}
