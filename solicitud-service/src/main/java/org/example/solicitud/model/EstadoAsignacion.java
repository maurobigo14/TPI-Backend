package org.example.solicitud.model;

public enum EstadoAsignacion {
    ASIGNADO("asignado"),
    EN_TRANSITO("en tránsito"),
    ENTREGADO("entregado"),
    COMPLETADA("completada"),
    CANCELADO("cancelado");

    private final String valor;

    EstadoAsignacion(String valor) {
        this.valor = valor;
    }

    public String getValor() {
        return valor;
    }
}
