package com.rsdesenvolvimento.pedido_service.core.client;

import com.rsdesenvolvimento.pedido_service.core.client.dtos.AtualizarEstoqueRequestDto;
import com.rsdesenvolvimento.pedido_service.core.client.dtos.EstoqueResponseDto;
import com.rsdesenvolvimento.pedido_service.core.client.fallback.EstoqueFallback;
import com.rsdesenvolvimento.pedido_service.core.ports.EstoquePort;
import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "estoque-api", path = "/api/estoque", fallback = EstoqueFallback.class)
public interface EstoqueFeignClient extends EstoquePort {
    @Override
    @PostMapping("/validar")
    Boolean validarEstoque(@RequestBody List<AtualizarEstoqueRequestDto> itens);

    @Override
    @PostMapping("/reservar")
    void reservarEstoque(@RequestBody List<AtualizarEstoqueRequestDto> itens);

    @Override
    @GetMapping("/{id}")
    EstoqueResponseDto buscarProduto(@PathVariable("id") Long id);

    @Override
    @PostMapping("/atualizar")
    AtualizarEstoqueRequestDto atualizarEstoque(@RequestBody AtualizarEstoqueRequestDto dto);

}
