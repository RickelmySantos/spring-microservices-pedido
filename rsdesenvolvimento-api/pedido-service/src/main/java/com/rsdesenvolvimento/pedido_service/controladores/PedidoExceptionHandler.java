package com.rsdesenvolvimento.pedido_service.controladores;

import com.rsdesenvolvimento.pedido_service.services.exceptions.EstoqueInsuficienteException;
import com.rsdesenvolvimento.pedido_service.services.exceptions.PedidoNaoEncontradoException;
import com.rsdesenvolvimento.pedido_service.services.exceptions.PedidoValidationException;
import com.rsdesenvolvimento.pedido_service.services.exceptions.StatusInvalidoException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
@Slf4j
public class PedidoExceptionHandler {

    @ExceptionHandler(PedidoNaoEncontradoException.class)
    public ResponseEntity<ErrorResponse> handlePedidoNaoEncontrado(
            PedidoNaoEncontradoException ex) {
        PedidoExceptionHandler.log.warn("Pedido não encontrado: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse("PEDIDO_NAO_ENCONTRADO", ex.getMessage()));
    }

    @ExceptionHandler(PedidoValidationException.class)
    public ResponseEntity<ErrorResponse> handlePedidoValidation(PedidoValidationException ex) {
        PedidoExceptionHandler.log.warn("Erro de validação: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse("VALIDACAO_ERRO", ex.getMessage()));
    }

    @ExceptionHandler(EstoqueInsuficienteException.class)
    public ResponseEntity<ErrorResponse> handleEstoqueInsuficiente(
            EstoqueInsuficienteException ex) {
        PedidoExceptionHandler.log.warn("Estoque insuficiente: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse("ESTOQUE_INSUFICIENTE", ex.getMessage()));
    }

    @ExceptionHandler(StatusInvalidoException.class)
    public ResponseEntity<ErrorResponse> handleStatusInvalido(StatusInvalidoException ex) {
        PedidoExceptionHandler.log.warn("Status inválido: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse("STATUS_INVALIDO", ex.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
        PedidoExceptionHandler.log.error("Erro interno: {}", ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse("ERRO_INTERNO", "Erro interno do servidor"));
    }

    /**
     * Classe para padronizar as respostas de erro
     */
    public record ErrorResponse(String codigo, String mensagem) {
    }
}
