package org.example.solicitud.model;

import jakarta.persistence.*;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tramos")
public class Tramo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "ruta_id", nullable = false)
    private Ruta ruta;

    @Column(name = "numero_secuencia")
    private Integer numeroSecuencia;

    @Column(name = "origen_direccion")
    private String origenDireccion;

    @Column(name = "origen_lat")
    private Double origenLat;

    @Column(name = "origen_lng")
    private Double origenLng;

    @Column(name = "destino_direccion")
    private String destinoDireccion;

    @Column(name = "destino_lat")
    private Double destinoLat;

    @Column(name = "destino_lng")
    private Double destinoLng;

    @Column(name = "distancia_km")
    private Double distanciaKm;

    @Column(name = "tiempo_min")
    private Integer tiempoMin;

    @Column(name = "costo")
    private Double costo;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado")
    private EstadoTramo estado;
}
