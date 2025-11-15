package org.example.solicitud.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AsignacionTransportistaResponse {
    private Long asignacionId;
    private Long tramoId;
    private Long rutaId;
    private Long solicitudId;
    private Integer numeroSecuencia;
    private String estadoAsignacion;
    private String estadoTramo;
    private String origenDireccion;
    private Double origenLat;
    private Double origenLng;
    private String destinoDireccion;
    private Double destinoLat;
    private Double destinoLng;
    private Double distanciaKm;
    private Integer tiempoMin;
    private Double costo;
    private String camionDominio;
    private String transportistaDni;
    private LocalDateTime fechaAsignacion;
    private LocalDateTime fechaInicio;
    private LocalDateTime fechaFin;
}

