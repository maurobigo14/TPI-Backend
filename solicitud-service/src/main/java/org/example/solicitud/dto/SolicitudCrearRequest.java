package org.example.solicitud.dto;

import lombok.Data;

@Data
public class SolicitudCrearRequest {

    private ClienteDTO cliente;
    private ContenedorDTO contenedor;
    private UbicacionDTO origen;
    private UbicacionDTO destino;
}

