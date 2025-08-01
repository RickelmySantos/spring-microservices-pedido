package com.rsdesenvolvimento.estoque.core.exception;

public class EstoqueException extends RuntimeException {

    public EstoqueException(String message) {
        super(message);
    }

    public EstoqueException(String message, Throwable cause) {
        super(message, cause);
    }

}
