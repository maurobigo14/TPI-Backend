package org.example.solicitud.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SolicitudResponse {
    private Long id;
    private Long contenedorId;
    private String clienteDni;
    private String estado;
}

