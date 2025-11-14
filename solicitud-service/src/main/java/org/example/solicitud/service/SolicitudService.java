package org.example.solicitud.service;

import org.example.solicitud.dto.SolicitudCrearRequest;
import org.example.solicitud.dto.SolicitudResponse;
import org.example.solicitud.dto.SolicitudDetalleResponse;
import org.example.solicitud.dto.AsignarRutaRequest;
import org.example.solicitud.dto.AsignarRutaResponse;
import org.example.solicitud.dto.AsignarCamionRequest;
import org.example.solicitud.dto.AsignarCamionResponse;

import java.util.List;
import java.util.Optional;

public interface SolicitudService {

    SolicitudResponse crearSolicitud(SolicitudCrearRequest request);

    List<SolicitudResponse> listarSolicitudes();

    List<SolicitudResponse> buscarPorCliente(String dni);

    Optional<SolicitudDetalleResponse> obtenerPorNumero(Long numero);

    List<SolicitudResponse> buscarPorEstado(String estado);

    java.util.List<org.example.solicitud.dto.RutaResponse> generarRutasTentativas(org.example.solicitud.dto.SolicitudCrearRequest request);

    AsignarRutaResponse asignarRuta(AsignarRutaRequest request);

    AsignarCamionResponse asignarCamionATramo(AsignarCamionRequest request);
}
