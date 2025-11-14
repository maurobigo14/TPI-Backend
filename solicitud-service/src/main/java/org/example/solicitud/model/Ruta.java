package org.example.solicitud.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "rutas")
public class Ruta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "solicitud_id", nullable = false)
    private Long solicitudId;

    @Column(name = "descripcion")
    private String descripcion;

    @Column(name = "distancia_total_km")
    private Double distanciaTotalKm;

    @Column(name = "tiempo_total_min")
    private Integer tiempoTotalMin;

    @Column(name = "costo_total")
    private Double costoTotal;

    @OneToMany(mappedBy = "ruta", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Tramo> tramos;
}
