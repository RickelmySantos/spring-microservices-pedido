package com.rsdesenvolvimento.pedido_service.services.exceptions;

public class StatusInvalidoException extends RuntimeException {

    public StatusInvalidoException(String message) {
        super(message);
    }

    public StatusInvalidoException(String message, Throwable cause) {
        super(message, cause);
    }
}
