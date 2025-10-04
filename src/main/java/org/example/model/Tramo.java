package org.example.model;

import org.example.model.enums.Estado;
import org.example.model.enums.Tipo;
import lombok.Data;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Data

public class Tramo {
    private String origen;
    private String destino;
    private Tipo tipo;
    private Estado estado;
    private double costoAproximado;
    private double costoReal;
    private int fechaHoraInicio;
    private int fechaHoraFin;
    private Camion camion;
}
