package org.example.solicitud.dto.camion;

import lombok.Data;

@Data
public class CamionResponse {
    private Integer dominio;
    private String nombreTransportista;
    private String telefono;
    private Double capacidadPeso;
    private Double capacidadVolumen;
    private Boolean disponibilidad;
    private Double costos;
}
