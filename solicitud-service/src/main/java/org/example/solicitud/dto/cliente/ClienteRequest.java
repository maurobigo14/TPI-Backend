package org.example.solicitud.dto.cliente;

import lombok.Data;

@Data
public class ClienteRequest {
    private String dni;
    private String nombre;
    private String apellido;
    private String email;
    private String telefono;
}
