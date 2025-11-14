package org.example.solicitud.model;

public enum EstadoSolicitud {
    BORRADOR("borrador"),
    PROGRAMADA("programada"),
    EN_TRANSITO("en tránsito"),
    ENTREGADA("entregada");

    private final String valor;

    EstadoSolicitud(String valor) {
        this.valor = valor;
    }

    public String getValor() {
        return valor;
    }
}
