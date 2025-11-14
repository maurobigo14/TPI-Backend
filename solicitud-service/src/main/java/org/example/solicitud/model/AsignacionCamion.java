package org.example.solicitud.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "asignaciones_camion")
public class AsignacionCamion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "tramo_id", nullable = false)
    private Tramo tramo;

    @Column(name = "camion_dominio", nullable = false)
    private Integer camionDominio;

    @Column(name = "transportista_dni")
    private String transportistaDni;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado")
    private EstadoAsignacion estado;

    @Column(name = "fecha_asignacion")
    private LocalDateTime fechaAsignacion;

    @Column(name = "fecha_inicio")
    private LocalDateTime fechaInicio;

    @Column(name = "fecha_fin")
    private LocalDateTime fechaFin;
}
