package org.example.solicitud.dto.tarifa;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TarifaCalcResponse {
    private double distanciaKm;
    private double costoBaseKm;
    private double costoCombustible;
    private double cargoGestion;
    private double costoEstadia;
    private double costoTotal;
    private String moneda;
}
