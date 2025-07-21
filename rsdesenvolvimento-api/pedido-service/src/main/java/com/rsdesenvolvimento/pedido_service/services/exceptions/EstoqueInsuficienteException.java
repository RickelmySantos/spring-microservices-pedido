package com.rsdesenvolvimento.pedido_service.services.exceptions;

public class EstoqueInsuficienteException extends RuntimeException {

    public EstoqueInsuficienteException(String message) {
        super(message);
    }

    public EstoqueInsuficienteException(String message, Throwable cause) {
        super(message, cause);
    }
}
