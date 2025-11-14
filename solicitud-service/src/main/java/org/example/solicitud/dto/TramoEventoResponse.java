package org.example.solicitud.dto;

import lombok.*;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TramoEventoResponse {
    private Long asignacionCamionId;
    private Long tramoId;
    private String evento; // "INICIADO" | "FINALIZADO"
    private LocalDateTime timestamp;
    private String mensaje;
}
