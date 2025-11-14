package org.example.solicitud.service.impl;

import org.example.solicitud.client.ClienteClient;
import org.example.solicitud.client.ContenedorClient;
import org.example.solicitud.dto.SolicitudCrearRequest;
import org.example.solicitud.dto.SolicitudResponse;
import org.example.solicitud.dto.SolicitudDetalleResponse;
import org.example.solicitud.dto.cliente.ClienteRequest;
import org.example.solicitud.dto.cliente.ClienteResponse;
import org.example.solicitud.dto.contenedor.ContenedorRequest;
import org.example.solicitud.dto.contenedor.ContenedorResponse;
import org.example.solicitud.model.Ruta;
import org.example.solicitud.model.Tramo;
import org.example.solicitud.model.EstadoTramo;
import org.example.solicitud.model.Solicitud;
import org.example.solicitud.model.EstadoSolicitud;
import org.example.solicitud.model.AsignacionCamion;
import org.example.solicitud.model.EstadoAsignacion;
import org.example.solicitud.repository.RutaRepository;
import org.example.solicitud.repository.TramoRepository;
import org.example.solicitud.repository.AsignacionCamionRepository;
import org.example.solicitud.dto.AsignarRutaRequest;
import org.example.solicitud.dto.AsignarRutaResponse;
import org.example.solicitud.dto.AsignarCamionRequest;
import org.example.solicitud.dto.AsignarCamionResponse;
import org.example.solicitud.repository.SolicitudRepository;
import org.example.solicitud.service.SolicitudService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.ArrayList;


@Service
public class SolicitudServiceImpl implements SolicitudService {

    private final SolicitudRepository solicitudRepository;
    private final ClienteClient clienteClient;
    private final ContenedorClient contenedorClient;
    private final RutaRepository rutaRepository;
    private final TramoRepository tramoRepository;
    private final AsignacionCamionRepository asignacionCamionRepository;

    public SolicitudServiceImpl(SolicitudRepository solicitudRepository,
                                ClienteClient clienteClient,
                                ContenedorClient contenedorClient,
                                RutaRepository rutaRepository,
                                TramoRepository tramoRepository,
                                AsignacionCamionRepository asignacionCamionRepository) {
        this.solicitudRepository = solicitudRepository;
        this.clienteClient = clienteClient;
        this.contenedorClient = contenedorClient;
        this.rutaRepository = rutaRepository;
        this.tramoRepository = tramoRepository;
        this.asignacionCamionRepository = asignacionCamionRepository;
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
        contReq.setNumeroIdentificacion(UUID.randomUUID().toString());
        contReq.setPeso((int) request.getContenedor().getPesoKg());
        contReq.setVolumen((int) request.getContenedor().getVolumenM3());
        contReq.setEstado("NUEVO");
        contReq.setClienteDni(cliente.getDni());

        ContenedorResponse contenedor = contenedorClient.crearContenedor(contReq);


        // ============================================================
        // 3️⃣ CREAR ENTIDAD SOLICITUD
        // ============================================================

        Solicitud solicitud = new Solicitud();

        solicitud.setClienteDni(cliente.getDni());
        solicitud.setContenedorId(contenedor.getId().longValue());
        solicitud.setOrigenDireccion(request.getOrigenDireccion());
        solicitud.setOrigenLat(request.getOrigenLat());
        solicitud.setOrigenLng(request.getOrigenLng());
        solicitud.setDestinoDireccion(request.getDestinoDireccion());
        solicitud.setDestinoLat(request.getDestinoLat());
        solicitud.setDestinoLng(request.getDestinoLng());
        solicitud.setEstado(EstadoSolicitud.BORRADOR);

        solicitud = solicitudRepository.save(solicitud);


        // ============================================================
        // 4️⃣ ARMAR RESPUESTA PARA EL FRONT
        // ============================================================

        return new SolicitudResponse(
                solicitud.getNumero(),
                solicitud.getContenedorId(),
                solicitud.getClienteDni(),
                solicitud.getEstado().getValor()
        );
    }


    @Override
    public List<SolicitudResponse> listarSolicitudes() {
        return solicitudRepository.findAll()
                .stream()
                .map(s -> new SolicitudResponse(
                        s.getNumero(),
                        s.getContenedorId(),
                        s.getClienteDni(),
                        s.getEstado().getValor()))
                .collect(Collectors.toList());
    }

    @Override
    public List<SolicitudResponse> buscarPorCliente(String dni) {
        return solicitudRepository.findByClienteDni(dni)
                .stream()
                .map(s -> new SolicitudResponse(
                        s.getNumero(),
                        s.getContenedorId(),
                        s.getClienteDni(),
                        s.getEstado().getValor()))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<SolicitudDetalleResponse> obtenerPorNumero(Long numero) {
        return solicitudRepository.findById(numero)
                .map(s -> SolicitudDetalleResponse.builder()
                        .numero(s.getNumero())
                        .contenedorId(s.getContenedorId())
                        .clienteDni(s.getClienteDni())
                        .origenDireccion(s.getOrigenDireccion())
                        .origenLat(s.getOrigenLat())
                        .origenLng(s.getOrigenLng())
                        .destinoDireccion(s.getDestinoDireccion())
                        .destinoLat(s.getDestinoLat())
                        .destinoLng(s.getDestinoLng())
                        .estado(s.getEstado().getValor())
                        .costoEstimado(s.getCostoEstimado())
                        .tiempoEstimado(s.getTiempoEstimado())
                        .costoFinal(s.getCostoFinal())
                        .tiempoReal(s.getTiempoReal())
                        .build());
    }

    @Override
    public List<SolicitudResponse> buscarPorEstado(String estado) {
        try {
            EstadoSolicitud estadoEnum = EstadoSolicitud.valueOf(estado.toUpperCase());
            return solicitudRepository.findByEstado(estadoEnum)
                    .stream()
                    .map(s -> new SolicitudResponse(
                            s.getNumero(),
                            s.getContenedorId(),
                            s.getClienteDni(),
                            s.getEstado().getValor()))
                    .collect(Collectors.toList());
        } catch (IllegalArgumentException e) {
            return List.of();
        }
    }

    @Override
    public org.example.solicitud.dto.AsignarRutaResponse asignarRuta(org.example.solicitud.dto.AsignarRutaRequest request) {
        // Verificar que la solicitud existe
        Solicitud solicitud = solicitudRepository.findById(request.getSolicitudId())
                .orElseThrow(() -> new RuntimeException("Solicitud no encontrada"));

        // Verificar que no exista ruta asignada previamente
        if (rutaRepository.findBySolicitudId(request.getSolicitudId()).isPresent()) {
            throw new RuntimeException("La solicitud ya tiene una ruta asignada");
        }

        // Calcular totales
        double distanciaTotal = request.getTramos().stream()
                .mapToDouble(org.example.solicitud.dto.AsignarRutaRequest.TramoRequest::getDistanciaKm)
                .sum();
        int tiempoTotal = request.getTramos().stream()
                .mapToInt(org.example.solicitud.dto.AsignarRutaRequest.TramoRequest::getTiempoMin)
                .sum();
        double costoTotal = request.getTramos().stream()
                .mapToDouble(org.example.solicitud.dto.AsignarRutaRequest.TramoRequest::getCosto)
                .sum();

        // Crear ruta
        Ruta ruta = Ruta.builder()
                .solicitudId(request.getSolicitudId())
                .descripcion(request.getDescripcion())
                .distanciaTotalKm(distanciaTotal)
                .tiempoTotalMin(tiempoTotal)
                .costoTotal(costoTotal)
                .build();
        ruta = rutaRepository.save(ruta);

        // Crear tramos
        java.util.List<Tramo> tramosGuardados = new java.util.ArrayList<>();
        for (int i = 0; i < request.getTramos().size(); i++) {
            org.example.solicitud.dto.AsignarRutaRequest.TramoRequest tramoReq = request.getTramos().get(i);
            Tramo tramo = Tramo.builder()
                    .ruta(ruta)
                    .numeroSecuencia(i + 1)
                    .origenDireccion(tramoReq.getOrigenDireccion())
                    .origenLat(tramoReq.getOrigenLat())
                    .origenLng(tramoReq.getOrigenLng())
                    .destinoDireccion(tramoReq.getDestinoDireccion())
                    .destinoLat(tramoReq.getDestinoLat())
                    .destinoLng(tramoReq.getDestinoLng())
                    .distanciaKm(tramoReq.getDistanciaKm())
                    .tiempoMin(tramoReq.getTiempoMin())
                    .costo(tramoReq.getCosto())
                    .estado(EstadoTramo.PENDIENTE)
                    .build();
            tramosGuardados.add(tramoRepository.save(tramo));
        }

        // Actualizar solicitud con costo estimado y tiempo estimado
        solicitud.setCostoEstimado(costoTotal);
        solicitud.setTiempoEstimado(tiempoTotal);
        solicitud.setEstado(EstadoSolicitud.PROGRAMADA);
        solicitudRepository.save(solicitud);

        // Armar respuesta
        java.util.List<org.example.solicitud.dto.TramoResponse> tramosResponse = tramosGuardados.stream()
                .map(t -> org.example.solicitud.dto.TramoResponse.builder()
                        .origenDireccion(t.getOrigenDireccion())
                        .origenLat(t.getOrigenLat())
                        .origenLng(t.getOrigenLng())
                        .destinoDireccion(t.getDestinoDireccion())
                        .destinoLat(t.getDestinoLat())
                        .destinoLng(t.getDestinoLng())
                        .distanciaKm(t.getDistanciaKm())
                        .tiempoMin(t.getTiempoMin())
                        .costo(t.getCosto())
                        .build())
                .collect(java.util.stream.Collectors.toList());

        return org.example.solicitud.dto.AsignarRutaResponse.builder()
                .rutaId(ruta.getId())
                .solicitudId(request.getSolicitudId())
                .descripcion(request.getDescripcion())
                .tramos(tramosResponse)
                .distanciaTotalKm(distanciaTotal)
                .tiempoTotalMin(tiempoTotal)
                .costoTotal(costoTotal)
                .build();
    }

    @Override
    public java.util.List<org.example.solicitud.dto.RutaResponse> generarRutasTentativas(org.example.solicitud.dto.SolicitudCrearRequest request) {
        // Algoritmo simple de rutas tentativas:
        // Genera 2 opciones: directo (origen -> destino) y vía depósito (origen -> punto intermedio -> destino)

        double origenLat = request.getOrigenLat();
        double origenLng = request.getOrigenLng();
        String origenDir = request.getOrigenDireccion();

        double destinoLat = request.getDestinoLat();
        double destinoLng = request.getDestinoLng();
        String destinoDir = request.getDestinoDireccion();

        double pesoKg = request.getContenedor().getPesoKg();
        double volumenM3 = request.getContenedor().getVolumenM3();

        List<org.example.solicitud.dto.RutaResponse> rutas = new ArrayList<>();

        // Helper lambda para calcular tramo
        java.util.function.BiFunction<double[], double[], org.example.solicitud.dto.TramoResponse> calcTramo = (from, to) -> {
            double dist = haversine(from[0], from[1], to[0], to[1]);
            int tiempoMin = (int) Math.round(dist); // approx: 1 km ~= 1 min (60 km/h)
            double costo = calcularCosto(dist, pesoKg, volumenM3);
            return org.example.solicitud.dto.TramoResponse.builder()
                    .origenDireccion(from[2] == 1.0 ? origenDir : "")
                    .origenLat(from[0])
                    .origenLng(from[1])
                    .destinoDireccion(to[2] == 1.0 ? destinoDir : "")
                    .destinoLat(to[0])
                    .destinoLng(to[1])
                    .distanciaKm(dist)
                    .tiempoMin(tiempoMin)
                    .costo(costo)
                    .build();
        };

        // Directo
        double[] o = new double[]{origenLat, origenLng, 1.0};
        double[] d = new double[]{destinoLat, destinoLng, 1.0};
        org.example.solicitud.dto.TramoResponse tramoDirecto = calcTramo.apply(o, d);
        org.example.solicitud.dto.RutaResponse rutaDirecta = org.example.solicitud.dto.RutaResponse.builder()
                .descripcion("Directa: origen -> destino")
                .tramos(List.of(tramoDirecto))
                .distanciaTotalKm(tramoDirecto.getDistanciaKm())
                .tiempoTotalMin(tramoDirecto.getTiempoMin())
                .costoTotal(tramoDirecto.getCosto())
                .build();
        rutas.add(rutaDirecta);

        // Vía depósito (punto intermedio aproximado)
        double midLat = (origenLat + destinoLat) / 2.0;
        double midLng = (origenLng + destinoLng) / 2.0;
        double[] m = new double[]{midLat, midLng, 0.0};
        org.example.solicitud.dto.TramoResponse t1 = calcTramo.apply(o, m);
        org.example.solicitud.dto.TramoResponse t2 = calcTramo.apply(m, d);
        double distanciaTotal = t1.getDistanciaKm() + t2.getDistanciaKm();
        int tiempoTotal = t1.getTiempoMin() + t2.getTiempoMin();
        double costoTotal = t1.getCosto() + t2.getCosto();
        org.example.solicitud.dto.RutaResponse rutaViaDeposito = org.example.solicitud.dto.RutaResponse.builder()
                .descripcion("Vía depósito: origen -> depósito -> destino")
                .tramos(List.of(t1, t2))
                .distanciaTotalKm(distanciaTotal)
                .tiempoTotalMin(tiempoTotal)
                .costoTotal(costoTotal)
                .build();
        rutas.add(rutaViaDeposito);

        return rutas;
    }

    private double calcularCosto(double distanciaKm, double pesoKg, double volumenM3) {
        double basePorKm = 1.2; // moneda por km
        double pesoFactor = 0.001 * pesoKg; // ejemplo
        double volumenFactor = 0.5 * volumenM3; // ejemplo
        return Math.round((distanciaKm * basePorKm + pesoFactor + volumenFactor) * 100.0) / 100.0;
    }

    // Haversine in km
    private double haversine(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371; // Radius of the earth in km
        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c;
        return Math.round(distance * 100.0) / 100.0;
    }

    @Override
    public org.example.solicitud.dto.AsignarCamionResponse asignarCamionATramo(org.example.solicitud.dto.AsignarCamionRequest request) {
        // Validar que el tramo existe
        Tramo tramo = tramoRepository.findById(request.getTramoId())
                .orElseThrow(() -> new RuntimeException("Tramo no encontrado"));

        // Validar que no tenga asignación previa
        if (asignacionCamionRepository.findByTramoId(request.getTramoId()).isPresent()) {
            throw new RuntimeException("El tramo ya tiene un camión asignado");
        }

        // Crear asignación
        AsignacionCamion asignacion = AsignacionCamion.builder()
                .tramo(tramo)
                .camionDominio(request.getCamionDominio())
                .transportistaDni(request.getTransportistaDni())
                .estado(EstadoAsignacion.ASIGNADO)
                .fechaAsignacion(java.time.LocalDateTime.now())
                .build();

        asignacion = asignacionCamionRepository.save(asignacion);

        // Actualizar estado del tramo a EN_TRANSITO
        tramo.setEstado(EstadoTramo.EN_TRANSITO);
        tramoRepository.save(tramo);

        return org.example.solicitud.dto.AsignarCamionResponse.builder()
                .asignacionId(asignacion.getId())
                .tramoId(asignacion.getTramo().getId())
                .camionDominio(asignacion.getCamionDominio())
                .transportistaDni(asignacion.getTransportistaDni())
                .estado(asignacion.getEstado().getValor())
                .fechaAsignacion(asignacion.getFechaAsignacion())
                .build();
    }
}

