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
import org.example.solicitud.service.GoogleMapsService;
import org.example.solicitud.client.CamionClient;
import org.example.solicitud.dto.camion.CamionResponse;
import org.example.solicitud.exception.BadRequestException;
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
    private final GoogleMapsService googleMapsService;
                private final org.example.solicitud.client.TarifaClient tarifaClient;
                private final CamionClient camionClient;

        public SolicitudServiceImpl(SolicitudRepository solicitudRepository,
                                                                ClienteClient clienteClient,
                                                                ContenedorClient contenedorClient,
                                                                RutaRepository rutaRepository,
                                                                TramoRepository tramoRepository,
                                                                AsignacionCamionRepository asignacionCamionRepository,
                                                                GoogleMapsService googleMapsService,
                                                                org.example.solicitud.client.TarifaClient tarifaClient,
                                                                CamionClient camionClient) {
        this.solicitudRepository = solicitudRepository;
        this.clienteClient = clienteClient;
        this.contenedorClient = contenedorClient;
        this.rutaRepository = rutaRepository;
        this.tramoRepository = tramoRepository;
        this.asignacionCamionRepository = asignacionCamionRepository;
        this.googleMapsService = googleMapsService;
                                this.tarifaClient = tarifaClient;
                                this.camionClient = camionClient;
    }

        @Override
        public org.example.solicitud.dto.TramoEventoResponse iniciarTramo(org.example.solicitud.dto.IniciarTramoRequest request) {
                Optional<AsignacionCamion> optAsignacion = asignacionCamionRepository.findById(request.getAsignacionCamionId());
                if (optAsignacion.isEmpty()) {
                        throw new RuntimeException("Asignación no encontrada: " + request.getAsignacionCamionId());
                }

                AsignacionCamion asignacion = optAsignacion.get();
        
                // Validar que no esté ya iniciado
                if (asignacion.getFechaInicio() != null) {
                        throw new RuntimeException("El tramo ya ha sido iniciado");
                }

                asignacion.setFechaInicio(java.time.LocalDateTime.now());
                asignacion.setEstado(EstadoAsignacion.EN_TRANSITO);
                asignacionCamionRepository.save(asignacion);

                // Actualizar estado del tramo
                Tramo tramo = asignacion.getTramo();
                tramo.setEstado(EstadoTramo.EN_TRANSITO);
                tramoRepository.save(tramo);

                return org.example.solicitud.dto.TramoEventoResponse.builder()
                                .asignacionCamionId(asignacion.getId())
                                .tramoId(asignacion.getTramo().getId())
                                .evento("INICIADO")
                                .timestamp(asignacion.getFechaInicio())
                                .mensaje("Tramo iniciado exitosamente")
                                .build();
        }

        @Override
        public org.example.solicitud.dto.TramoEventoResponse finalizarTramo(org.example.solicitud.dto.FinalizarTramoRequest request) {
                Optional<AsignacionCamion> optAsignacion = asignacionCamionRepository.findById(request.getAsignacionCamionId());
                if (optAsignacion.isEmpty()) {
                        throw new RuntimeException("Asignación no encontrada: " + request.getAsignacionCamionId());
                }

                AsignacionCamion asignacion = optAsignacion.get();
        
                // Validar que esté iniciado
                if (asignacion.getFechaInicio() == null) {
                        throw new RuntimeException("El tramo no ha sido iniciado aún");
                }

                // Validar que no esté ya finalizado
                if (asignacion.getFechaFin() != null) {
                        throw new RuntimeException("El tramo ya ha sido finalizado");
                }

                asignacion.setFechaFin(java.time.LocalDateTime.now());
                asignacion.setEstado(EstadoAsignacion.COMPLETADA);
                asignacionCamionRepository.save(asignacion);

                // Actualizar estado del tramo
                Tramo tramo = asignacion.getTramo();
                tramo.setEstado(EstadoTramo.COMPLETADO);
                tramoRepository.save(tramo);

                // Calcular tiempo real de ejecución
                long minutosDuracion = java.time.temporal.ChronoUnit.MINUTES.between(
                                asignacion.getFechaInicio(), asignacion.getFechaFin());

                // Si todos los tramos de la ruta están finalizados, calcular costo total y persistir
                Ruta ruta = tramo.getRuta();
                List<Tramo> tramosRuta = tramoRepository.findByRutaId(ruta.getId());
                boolean todosCompletos = tramosRuta.stream().allMatch(t -> t.getEstado() == EstadoTramo.COMPLETADO);
                if (todosCompletos) {
                        double distanciaTotal = tramosRuta.stream().mapToDouble(t -> t.getDistanciaKm() != null ? t.getDistanciaKm() : 0.0).sum();

                        // Obtener contenedor para peso/volumen
                        Solicitud sol = solicitudRepository.findById(ruta.getSolicitudId()).orElse(null);
                        double pesoKg = 0.0;
                        double volumenM3 = 0.0;
                        if (sol != null && sol.getContenedorId() != null) {
                                try {
                                        org.example.solicitud.dto.contenedor.ContenedorResponse c = contenedorClient.obtenerContenedor(sol.getContenedorId().intValue());
                                        if (c != null) {
                                                pesoKg = c.getPeso();
                                                volumenM3 = c.getVolumen();
                                        }
                                } catch (Exception ignored) {
                                }
                        }

                        // Llamar a tarifa-service
                        org.example.solicitud.dto.tarifa.TarifaCalcRequest tarifaReq = org.example.solicitud.dto.tarifa.TarifaCalcRequest.builder()
                                        .distanciaKm(distanciaTotal)
                                        .pesoKg(pesoKg)
                                        .volumenM3(volumenM3)
                                        .diasEstadia(0)
                                        .build();
                        double costoFinalRuta = 0.0;
                        try {
                                org.example.solicitud.dto.tarifa.TarifaCalcResponse tarifaResp = tarifaClient.calcular(tarifaReq);
                                if (tarifaResp != null) costoFinalRuta = tarifaResp.getCostoTotal();
                        } catch (Exception ignored) {
                        }

                        // Persistir costo en la ruta y solicitud
                        ruta.setCostoTotal(costoFinalRuta);
                        rutaRepository.save(ruta);
                        if (sol != null) {
                                // Calcular tiempo real total: sumar duraciones de asignaciones
                                long tiempoRealTotal = 0;
                                for (Tramo t : tramosRuta) {
                                        asignacionCamionRepository.findByTramoId(t.getId()).ifPresent(a -> {
                                                if (a.getFechaInicio() != null && a.getFechaFin() != null) {
                                                        long mins = java.time.temporal.ChronoUnit.MINUTES.between(a.getFechaInicio(), a.getFechaFin());
                                                        // accumulate using array hack
                                                        // we'll handle accumulation outside
                                                }
                                        });
                                }
                                // Sum durations properly
                                long tiempoSum = tramosRuta.stream()
                                                .mapToLong(t -> asignacionCamionRepository.findByTramoId(t.getId())
                                                                .map(a -> (a.getFechaInicio() != null && a.getFechaFin() != null) ? java.time.temporal.ChronoUnit.MINUTES.between(a.getFechaInicio(), a.getFechaFin()) : 0L)
                                                                .orElse(0L))
                                                .sum();
                                sol.setCostoFinal(costoFinalRuta);
                                sol.setTiempoReal((int) tiempoSum);
                                solicitudRepository.save(sol);
                        }
                }

                return org.example.solicitud.dto.TramoEventoResponse.builder()
                                .asignacionCamionId(asignacion.getId())
                                .tramoId(asignacion.getTramo().getId())
                                .evento("FINALIZADO")
                                .timestamp(asignacion.getFechaFin())
                                .mensaje("Tramo finalizado. Duración: " + minutosDuracion + " minutos")
                                .build();
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
        // Algoritmo con Google Maps:
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

        // Ruta directa: origen -> destino
        GoogleMapsService.DistanceInfo directInfo = googleMapsService.getDistance(
                origenLat, origenLng, destinoLat, destinoLng);

        if (directInfo.isSuccess()) {
            double distDirect = directInfo.getDistanceKm();
            int timeDirect = directInfo.getDurationMinutes();

            // Call tarifa service for total route cost
            org.example.solicitud.dto.tarifa.TarifaCalcRequest tarifaReq = org.example.solicitud.dto.tarifa.TarifaCalcRequest.builder()
                    .distanciaKm(distDirect)
                    .pesoKg(pesoKg)
                    .volumenM3(volumenM3)
                    .diasEstadia(0)
                    .build();

            org.example.solicitud.dto.tarifa.TarifaCalcResponse tarifaResp = null;
            try {
                tarifaResp = tarifaClient.calcular(tarifaReq);
            } catch (Exception e) {
                // fallback to simple calcularCosto
            }

            double costoTotalRuta = tarifaResp != null ? tarifaResp.getCostoTotal() : calcularCosto(distDirect, pesoKg, volumenM3);

            org.example.solicitud.dto.TramoResponse tramoDirecto = org.example.solicitud.dto.TramoResponse.builder()
                    .origenDireccion(origenDir)
                    .origenLat(origenLat)
                    .origenLng(origenLng)
                    .destinoDireccion(destinoDir)
                    .destinoLat(destinoLat)
                    .destinoLng(destinoLng)
                    .distanciaKm(distDirect)
                    .tiempoMin(timeDirect)
                    .costo(costoTotalRuta) // only tramo in route
                    .build();

            org.example.solicitud.dto.RutaResponse rutaDirecta = org.example.solicitud.dto.RutaResponse.builder()
                    .descripcion("Directa (Google Maps): origen -> destino")
                    .tramos(List.of(tramoDirecto))
                    .distanciaTotalKm(distDirect)
                    .tiempoTotalMin(timeDirect)
                    .costoTotal(costoTotalRuta)
                    .build();
            rutas.add(rutaDirecta);
        }

        // Ruta vía depósito (punto intermedio)
        double midLat = (origenLat + destinoLat) / 2.0;
        double midLng = (origenLng + destinoLng) / 2.0;

        GoogleMapsService.DistanceInfo info1 = googleMapsService.getDistance(
                origenLat, origenLng, midLat, midLng);
        GoogleMapsService.DistanceInfo info2 = googleMapsService.getDistance(
                midLat, midLng, destinoLat, destinoLng);

        if (info1.isSuccess() && info2.isSuccess()) {
            double dist1 = info1.getDistanceKm();
            int time1 = info1.getDurationMinutes();

            double dist2 = info2.getDistanceKm();
            int time2 = info2.getDurationMinutes();

            double distanciaTotal = dist1 + dist2;
            int tiempoTotal = time1 + time2;

            // Call tarifa service for total route cost
            org.example.solicitud.dto.tarifa.TarifaCalcRequest tarifaReq = org.example.solicitud.dto.tarifa.TarifaCalcRequest.builder()
                    .distanciaKm(distanciaTotal)
                    .pesoKg(pesoKg)
                    .volumenM3(volumenM3)
                    .diasEstadia(0)
                    .build();

            org.example.solicitud.dto.tarifa.TarifaCalcResponse tarifaResp = null;
            try {
                tarifaResp = tarifaClient.calcular(tarifaReq);
            } catch (Exception e) {
                // fallback to local calculation per tramo
            }

            double costoTotal = tarifaResp != null ? tarifaResp.getCostoTotal() : (calcularCosto(dist1, pesoKg, volumenM3) + calcularCosto(dist2, pesoKg, volumenM3));

            // Distribute cost proportionally to distance
            double costo1 = (distanciaTotal > 0) ? costoTotal * (dist1 / distanciaTotal) : costoTotal / 2.0;
            double costo2 = costoTotal - costo1;

            org.example.solicitud.dto.TramoResponse t1 = org.example.solicitud.dto.TramoResponse.builder()
                    .origenDireccion(origenDir)
                    .origenLat(origenLat)
                    .origenLng(origenLng)
                    .destinoDireccion("Depósito Intermedio")
                    .destinoLat(midLat)
                    .destinoLng(midLng)
                    .distanciaKm(dist1)
                    .tiempoMin(time1)
                    .costo(costo1)
                    .build();

            org.example.solicitud.dto.TramoResponse t2 = org.example.solicitud.dto.TramoResponse.builder()
                    .origenDireccion("Depósito Intermedio")
                    .origenLat(midLat)
                    .origenLng(midLng)
                    .destinoDireccion(destinoDir)
                    .destinoLat(destinoLat)
                    .destinoLng(destinoLng)
                    .distanciaKm(dist2)
                    .tiempoMin(time2)
                    .costo(costo2)
                    .build();

            org.example.solicitud.dto.RutaResponse rutaViaDeposito = org.example.solicitud.dto.RutaResponse.builder()
                    .descripcion("Vía depósito (Google Maps): origen -> depósito -> destino")
                    .tramos(List.of(t1, t2))
                    .distanciaTotalKm(distanciaTotal)
                    .tiempoTotalMin(tiempoTotal)
                    .costoTotal(costoTotal)
                    .build();
            rutas.add(rutaViaDeposito);
        }

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

                // Validar capacidad del camión contra el contenedor de la solicitud
                try {
                        Ruta ruta = tramo.getRuta();
                        if (ruta != null && ruta.getSolicitudId() != null) {
                                java.util.Optional<Solicitud> solOpt = solicitudRepository.findById(ruta.getSolicitudId());
                                if (solOpt.isPresent()) {
                                        Solicitud sol = solOpt.get();
                                        if (sol.getContenedorId() != null) {
                                                org.example.solicitud.dto.contenedor.ContenedorResponse cont = contenedorClient.obtenerContenedor(sol.getContenedorId().intValue());
                                                if (cont == null) {
                                                        throw new BadRequestException("No se pudo obtener el contenedor para validación");
                                                }

                                                // obtener camion
                                                CamionResponse camion = camionClient.obtenerPorDominio(String.valueOf(request.getCamionDominio()));
                                                if (camion == null) {
                                                        throw new BadRequestException("Camión no encontrado: " + request.getCamionDominio());
                                                }

                                                Integer pesoCont = cont.getPeso();
                                                Integer volCont = cont.getVolumen();
                                                Double capacidadPeso = camion.getCapacidadPeso() != null ? camion.getCapacidadPeso() : 0.0;
                                                Double capacidadVol = camion.getCapacidadVolumen() != null ? camion.getCapacidadVolumen() : 0.0;

                                                if (pesoCont != null && pesoCont > capacidadPeso) {
                                                        throw new BadRequestException("Capacidad de peso insuficiente para el camión (peso contenedor: " + pesoCont + ", capacidad: " + capacidadPeso + ")");
                                                }
                                                if (volCont != null && volCont > capacidadVol) {
                                                        throw new BadRequestException("Capacidad de volumen insuficiente para el camión (volumen contenedor: " + volCont + ", capacidad: " + capacidadVol + ")");
                                                }
                                        }
                                }
                        }
                } catch (RuntimeException rex) {
                        throw rex;
                } catch (Exception e) {
                        throw new BadRequestException("Error validando capacidad del camión: " + e.getMessage(), e);
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

