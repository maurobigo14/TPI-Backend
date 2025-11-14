package org.example.solicitud.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AsignarRutaRequest {
    private Long solicitudId;
    private String descripcion;
    private List<TramoRequest> tramos;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TramoRequest {
        private Integer numeroSecuencia;
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
}
