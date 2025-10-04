package org.example.model;
import lombok.Data;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Data

public class Solicitud {
    private int numero;
    private Contenedor contenedor;
    private Cliente cliente;
    private double costoEstimado;
    private int tiempoEstimado;
    private double costoFinal;
    private int tiempoReal;
}
