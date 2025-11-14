package org.example.solicitud.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SolicitudDetalleResponse {
    private Long numero;
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
}
