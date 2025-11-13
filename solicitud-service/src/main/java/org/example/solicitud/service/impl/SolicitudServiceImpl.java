package org.example.solicitud.service.impl;

import org.example.solicitud.client.ClienteClient;
import org.example.solicitud.client.ContenedorClient;
import org.example.solicitud.dto.SolicitudCrearRequest;
import org.example.solicitud.dto.SolicitudResponse;
import org.example.solicitud.dto.cliente.ClienteRequest;
import org.example.solicitud.dto.cliente.ClienteResponse;
import org.example.solicitud.dto.contenedor.ContenedorRequest;
import org.example.solicitud.dto.contenedor.ContenedorResponse;
import org.example.solicitud.model.Solicitud;
import org.example.solicitud.repository.SolicitudRepository;
import org.example.solicitud.service.SolicitudService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SolicitudServiceImpl implements SolicitudService {

    private final SolicitudRepository solicitudRepository;
    private final ClienteClient clienteClient;
    private final ContenedorClient contenedorClient;

    public SolicitudServiceImpl(SolicitudRepository solicitudRepository,
                                ClienteClient clienteClient,
                                ContenedorClient contenedorClient) {
        this.solicitudRepository = solicitudRepository;
        this.clienteClient = clienteClient;
        this.contenedorClient = contenedorClient;
    }

    @Override
    public SolicitudResponse crearSolicitud(SolicitudCrearRequest request) {

        // ============================================================
        // 1️⃣ BUSCAR O CREAR CLIENTE
        // ============================================================

        ClienteResponse cliente;

        try {
            cliente = clienteClient.obtenerPorDni(request.getCliente().getDni());
        }
        catch (Exception e) {
            ClienteRequest nuevo = new ClienteRequest();
            nuevo.setDni(request.getCliente().getDni());
            nuevo.setNombre(request.getCliente().getNombre());
            nuevo.setApellido(request.getCliente().getApellido());
            nuevo.setEmail(request.getCliente().getEmail());
            nuevo.setTelefono(request.getCliente().getTelefono());

            cliente = clienteClient.crearCliente(nuevo);
        }


        // ============================================================
        // 2️⃣ CREAR CONTENEDOR EN contenedor-service
        // ============================================================

        ContenedorRequest contReq = new ContenedorRequest();
        contReq.setPeso((int) request.getContenedor().getPesoKg());
        contReq.setVolumen((int) request.getContenedor().getVolumenM3());
        contReq.setEstado("nuevo"); // o "en depósito", lo que quieras
        contReq.setClienteDni(cliente.getDni());

        ContenedorResponse contenedor = contenedorClient.crearContenedor(contReq);


        // ============================================================
        // 3️⃣ CREAR ENTIDAD SOLICITUD
        // ============================================================

        Solicitud solicitud = new Solicitud();

        solicitud.setClienteDni(cliente.getDni());
        solicitud.setContenedorId(contenedor.getId().longValue());
        solicitud.setEstado("BORRADOR");

        solicitudRepository.save(solicitud);


        // ============================================================
        // 4️⃣ ARMAR RESPUESTA PARA EL FRONT
        // ============================================================

        return new SolicitudResponse(
                solicitud.getNumero().longValue(),
                solicitud.getContenedorId(),
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
                        s.getContenedorId(),
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
                        s.getContenedorId(),
                        s.getClienteDni(),
                        s.getEstado()))
                .collect(Collectors.toList());
    }
}

