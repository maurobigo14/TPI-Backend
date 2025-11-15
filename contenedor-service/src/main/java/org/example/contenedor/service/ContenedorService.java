package org.example.contenedor.service;

import org.example.contenedor.model.Contenedor;
import org.example.contenedor.model.EstadoContenedor;
import org.example.contenedor.repository.ContenedorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ContenedorService {

    private final ContenedorRepository contenedorRepository;
    private final RestTemplate restTemplate;

    @Value("${solicitud-service.url:http://solicitud-service:8085}")
    private String solicitudBaseUrl;

    @Autowired
    public ContenedorService(ContenedorRepository contenedorRepository) {
        this.contenedorRepository = contenedorRepository;
        this.restTemplate = new RestTemplate();
    }

    public List<Contenedor> findAll() {
        return contenedorRepository.findAll();
    }

    public Optional<Contenedor> findById(Integer id) {
        return contenedorRepository.findById(id);
    }

    public List<Contenedor> findByClienteDni(String clienteDni) {
        return contenedorRepository.findByClienteDni(clienteDni);
    }

    public List<Contenedor> findByEstado(String estado) {
        return contenedorRepository.findByEstado(estado);
    }

    public List<org.example.contenedor.dto.ContenedorPendienteResponse> findPendientesDeEntrega() {
        return contenedorRepository.findByEstado("EN_TRANSITO").stream()
                .map(c -> mapPendiente(c))
                .collect(Collectors.toList());
    }

    private org.example.contenedor.dto.ContenedorPendienteResponse mapPendiente(Contenedor c) {
        org.example.contenedor.dto.ContenedorPendienteResponse.ContenedorPendienteResponseBuilder builder =
                org.example.contenedor.dto.ContenedorPendienteResponse.builder()
                        .id(c.getId())
                        .numeroIdentificacion(c.getNumeroIdentificacion())
                        .peso(c.getPeso())
                        .volumen(c.getVolumen())
                        .estado(c.getEstado() != null ? c.getEstado().getValor() : null)
                        .clienteDni(c.getClienteDni())
                        .solicitudId(c.getSolicitudId());

        if (c.getSolicitudId() != null) {
            try {
                SolicitudDetalle dto = restTemplate.getForObject(
                        solicitudBaseUrl + "/api/solicitudes/" + c.getSolicitudId(),
                        SolicitudDetalle.class);
                if (dto != null) {
                    builder
                            .origenDireccion(dto.origenDireccion)
                            .origenLat(dto.origenLat)
                            .origenLng(dto.origenLng)
                            .destinoDireccion(dto.destinoDireccion)
                            .destinoLat(dto.destinoLat)
                            .destinoLng(dto.destinoLng);
                }
            } catch (Exception ignored) {
            }
        }
        return builder.build();
    }

    private static class SolicitudDetalle {
        public String origenDireccion;
        public Double origenLat;
        public Double origenLng;
        public String destinoDireccion;
        public Double destinoLat;
        public Double destinoLng;
    }

    public Contenedor save(Contenedor contenedor) {
        return contenedorRepository.save(contenedor);
    }

    public void deleteById(Integer id) {
        contenedorRepository.deleteById(id);
    }
}
