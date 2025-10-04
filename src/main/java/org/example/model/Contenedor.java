package org.example.model;
import lombok.Data;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Data

public class Contenedor {
    private int identificacion;
    private int peso;
    private int volumen;
    private String estado;
    private Cliente clienteAsociado;
}
