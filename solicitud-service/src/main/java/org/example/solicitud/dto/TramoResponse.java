package org.example.solicitud.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TramoResponse {
    private String origenDireccion;
    private Double origenLat;
    private Double origenLng;

    private String destinoDireccion;
    private Double destinoLat;
    private Double destinoLng;

    private Double distanciaKm;
    private Integer tiempoMin;
    private Double costo;
}
