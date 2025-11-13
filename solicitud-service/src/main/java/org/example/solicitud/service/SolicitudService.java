package org.example.solicitud.service;

import org.example.solicitud.dto.SolicitudCrearRequest;
import org.example.solicitud.dto.SolicitudResponse;

import java.util.List;

public interface SolicitudService {

    SolicitudResponse crearSolicitud(SolicitudCrearRequest request);

    List<SolicitudResponse> listarSolicitudes();

    List<SolicitudResponse> buscarPorCliente(String dni);
}
