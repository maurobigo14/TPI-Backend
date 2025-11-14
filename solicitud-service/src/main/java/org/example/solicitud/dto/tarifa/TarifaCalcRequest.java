package org.example.solicitud.dto.tarifa;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TarifaCalcRequest {
    private double distanciaKm;
    private double pesoKg;
    private double volumenM3;
    private int diasEstadia;
}
