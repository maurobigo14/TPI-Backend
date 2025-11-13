package org.example.solicitud.dto.cliente;

import lombok.Data;

@Data
public class ClienteResponse {
    private Long id;
    private String dni;
    private String nombre;
    private String apellido;
    private String email;
    private String telefono;
}
