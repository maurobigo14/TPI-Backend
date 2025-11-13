package org.example.solicitud.service.impl;

import org.example.solicitud.dto.SolicitudCrearRequest;
import org.example.solicitud.dto.SolicitudResponse;
import org.example.solicitud.model.Solicitud;
import org.example.solicitud.repository.SolicitudRepository;
import org.example.solicitud.service.SolicitudService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SolicitudServiceImpl implements SolicitudService {

    private final SolicitudRepository solicitudRepository;

    public SolicitudServiceImpl(SolicitudRepository solicitudRepository) {
        this.solicitudRepository = solicitudRepository;
    }

    @Override
    public SolicitudResponse crearSolicitud(SolicitudCrearRequest request) {

        Solicitud solicitud = new Solicitud();

        // cliente DNI desde el DTO
        solicitud.setClienteDni(request.getCliente().getDni());

        // contenedor, por ahora inventamos hasta integrar microservicio contenedor
        solicitud.setContenedorId((long) 1);  // temporal

        // estado inicial
        solicitud.setEstado("BORRADOR");

        solicitudRepository.save(solicitud);

        return new SolicitudResponse(
                solicitud.getNumero().longValue(),
                solicitud.getContenedorId().longValue(),
                solicitud.getClienteDni(),
                solicitud.getEstado()
        );
    }

    @Override
    public List<SolicitudResponse> listarSolicitudes() {
        return solicitudRepository.findAll()
                .stream()
                .map(s -> new SolicitudResponse(
                        s.getNumero().longValue(),
                        s.getContenedorId().longValue(),
                        s.getClienteDni(),
                        s.getEstado()))
                .collect(Collectors.toList());
    }

    @Override
    public List<SolicitudResponse> buscarPorCliente(String dni) {
        return solicitudRepository.findByClienteDni(dni)
                .stream()
                .map(s -> new SolicitudResponse(
                        s.getNumero().longValue(),
                        s.getContenedorId().longValue(),
                        s.getClienteDni(),
                        s.getEstado()))
                .collect(Collectors.toList());
    }
}
