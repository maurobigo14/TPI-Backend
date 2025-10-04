package org.example.model;
import lombok.Data;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Data

public class Ruta {
    private Solicitud solicitud;
    private int cantidadTramos;
    private int cantidadDepositos;
}
