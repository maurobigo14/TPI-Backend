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
public class AsignarCamionResponse {
    private Long asignacionId;
    private Long tramoId;
    private Integer camionDominio;
    private String transportistaDni;
    private String estado;
    private LocalDateTime fechaAsignacion;
}
