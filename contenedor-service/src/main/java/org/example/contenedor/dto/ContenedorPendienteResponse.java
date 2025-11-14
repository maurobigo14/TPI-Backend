package org.example.contenedor.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContenedorPendienteResponse {
    private Integer id;
    private String numeroIdentificacion;
    private Integer peso;
    private Integer volumen;
    private String estado;
    private String clienteDni;
    private Long solicitudId;
    private String origenDireccion;
    private Double origenLat;
    private Double origenLng;
    private String destinoDireccion;
    private Double destinoLat;
    private Double destinoLng;
}
