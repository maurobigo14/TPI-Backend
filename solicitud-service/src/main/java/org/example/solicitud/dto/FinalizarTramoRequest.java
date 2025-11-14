package org.example.solicitud.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FinalizarTramoRequest {
    private Long asignacionCamionId;
    private String observaciones;
}
