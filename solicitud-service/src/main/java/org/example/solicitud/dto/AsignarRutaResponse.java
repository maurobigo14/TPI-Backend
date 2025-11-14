package org.example.solicitud.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AsignarRutaResponse {
    private Long rutaId;
    private Long solicitudId;
    private String descripcion;
    private List<TramoResponse> tramos;
    private Double distanciaTotalKm;
    private Integer tiempoTotalMin;
    private Double costoTotal;
}
