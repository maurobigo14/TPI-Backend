package org.example.solicitud.service;

import org.example.solicitud.dto.SolicitudCrearRequest;
import org.example.solicitud.dto.SolicitudResponse;
import org.example.solicitud.dto.SolicitudDetalleResponse;

import java.util.List;
import java.util.Optional;

public interface SolicitudService {

    SolicitudResponse crearSolicitud(SolicitudCrearRequest request);

    List<SolicitudResponse> listarSolicitudes();

    List<SolicitudResponse> buscarPorCliente(String dni);

    Optional<SolicitudDetalleResponse> obtenerPorNumero(Long numero);

    List<SolicitudResponse> buscarPorEstado(String estado);

    java.util.List<org.example.solicitud.dto.RutaResponse> generarRutasTentativas(org.example.solicitud.dto.SolicitudCrearRequest request);
}
