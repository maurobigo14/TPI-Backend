package org.example.tarifa.dto;

import lombok.*;
import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TarifaCalcRequest {
    private double distanciaKm;
    private double pesoKg;
    private double volumenM3;
    private int diasEstadia; // opcional, 0 por defecto. Se calcula automáticamente si hay fechas
    private Instant fechaInicio; // opcional - para calcular días automáticamente
    private Instant fechaFin; // opcional - para calcular días automáticamente
}
