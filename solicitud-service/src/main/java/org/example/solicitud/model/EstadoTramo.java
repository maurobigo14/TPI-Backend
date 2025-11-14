package org.example.solicitud.model;

public enum EstadoTramo {
    PENDIENTE("pendiente"),
    EN_TRANSITO("en tránsito"),
    COMPLETADO("completado");

    private final String valor;

    EstadoTramo(String valor) {
        this.valor = valor;
    }

    public String getValor() {
        return valor;
    }
}
