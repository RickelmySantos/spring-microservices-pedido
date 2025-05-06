package com.rsdesenvolvimento.pedido_service.controladores;


import com.rsdesenvolvimento.pedido_service.modelo.dtos.PedidoRequesteDto;
import com.rsdesenvolvimento.pedido_service.modelo.dtos.PedidoResponseDto;
import com.rsdesenvolvimento.pedido_service.services.PedidoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/pedido")
public class PedidoController {

  private final PedidoService pedidoService;

  @PostMapping
  public ResponseEntity<PedidoResponseDto> criar(@RequestBody PedidoRequesteDto dto) {
    return ResponseEntity.status(HttpStatus.CREATED).body(this.pedidoService.criarPedido(dto));
  }
}
