package org.example.deposito.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "depositos")
public class Deposito {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre es obligatorio")
    @Column(unique = true, nullable = false)
    private String nombre;

    @NotBlank(message = "La dirección es obligatoria")
    private String direccion;

    @NotBlank(message = "La ciudad es obligatoria")
    private String ciudad;

    @NotNull(message = "El costo de estadía por día es obligatorio")
    private Double costoEstadiaPorDia;

    @NotNull(message = "La capacidad máxima es obligatoria")
    private Double capacidadMaxima;

    @NotNull
    private Boolean activo = true;

    public Deposito() {}

    // Getters
    public Long getId() { return id; }
    public String getNombre() { return nombre; }
    public String getDireccion() { return direccion; }
    public String getCiudad() { return ciudad; }
    public Double getCostoEstadiaPorDia() { return costoEstadiaPorDia; }
    public Double getCapacidadMaxima() { return capacidadMaxima; }
    public Boolean getActivo() { return activo; }

    // Setters
    public void setId(Long id) { this.id = id; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setDireccion(String direccion) { this.direccion = direccion; }
    public void setCiudad(String ciudad) { this.ciudad = ciudad; }
    public void setCostoEstadiaPorDia(Double costoEstadiaPorDia) { this.costoEstadiaPorDia = costoEstadiaPorDia; }
    public void setCapacidadMaxima(Double capacidadMaxima) { this.capacidadMaxima = capacidadMaxima; }
    public void setActivo(Boolean activo) { this.activo = activo; }
}
