package org.example.solicitud.dto.contenedor;

import lombok.Data;

@Data
public class ContenedorResponse {

    private Integer id;
    private String numeroIdentificacion;
    private Integer peso;
    private Integer volumen;
    private String estado;
    private String clienteDni;  
}
