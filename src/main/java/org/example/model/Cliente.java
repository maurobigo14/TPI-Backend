package org.example.model;
import lombok.Data;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Data

public class Cliente {
    private String nombre;
    private String apellido;
    private String email;
    private String telefono;
    }

