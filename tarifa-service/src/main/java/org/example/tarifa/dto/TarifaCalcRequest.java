package org.example.tarifa.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TarifaCalcRequest {
    private double distanciaKm;
    private double pesoKg;
    private double volumenM3;
    private int diasEstadia; // opcional, 0 por defecto
}
