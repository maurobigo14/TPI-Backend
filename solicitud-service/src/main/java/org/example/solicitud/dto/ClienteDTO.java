package org.example.solicitud.dto;

import lombok.Data;

@Data
public class ClienteDTO {
    private String dni;
    private String nombre;
    private String apellido;
    private String email;
    private String telefono;
}
