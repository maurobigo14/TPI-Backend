package org.example.solicitud.dto;


import lombok.Data;

@Data
public class UbicacionDTO {
    private String direccion;
    private double latitud;
    private double longitud;
}