package org.example.contenedor.model;

public enum EstadoContenedor {
    NUEVO("nuevo"),
    EN_DEPOSITO("en depósito"),
    EN_TRANSITO("en tránsito"),
    ENTREGADO("entregado");

    private final String valor;

    EstadoContenedor(String valor) {
        this.valor = valor;
    }

    public String getValor() {
        return valor;
    }
}
