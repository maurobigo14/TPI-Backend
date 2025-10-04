package org.example.model;
import lombok.Data;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Data

public class Deposito {
    private int identificacion;
    private String nombre;
    private String direccion;
    private int coordenadas;

}
