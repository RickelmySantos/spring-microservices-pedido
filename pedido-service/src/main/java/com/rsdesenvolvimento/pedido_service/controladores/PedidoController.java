package com.rsdesenvolvimento.pedido_service.controladores;


import com.rsdesenvolvimento.pedido_service.modelo.dtos.PedidoRequesteDto;
import com.rsdesenvolvimento.pedido_service.modelo.dtos.PedidoResponseDto;
import com.rsdesenvolvimento.pedido_service.services.PedidoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/pedido")
@Slf4j
public class PedidoController {

  private final PedidoService pedidoService;

  @PostMapping
  public ResponseEntity<PedidoResponseDto> criar(@RequestBody PedidoRequesteDto dto) {
    PedidoController.log.info("🔍 Recebendo requisição para criar pedido: {}", dto);
    return ResponseEntity.status(HttpStatus.CREATED).body(this.pedidoService.criarPedido(dto));
  }

  @GetMapping("/teste")
  public ResponseEntity<String> testarApi() {
    return ResponseEntity.ok("✅ Pedido API está rodando corretamente!");
  }

}
