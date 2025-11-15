package org.example.solicitud.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AsignarCamionRequest {
    private Long tramoId;
    private String camionDominio;
    private String transportistaDni;
}
