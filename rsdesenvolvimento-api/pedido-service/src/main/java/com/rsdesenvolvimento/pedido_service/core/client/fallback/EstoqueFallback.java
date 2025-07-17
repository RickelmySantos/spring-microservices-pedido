package com.rsdesenvolvimento.pedido_service.core.client.fallback;

import com.rsdesenvolvimento.pedido_service.core.client.dtos.AtualizarEstoqueRequestDto;
import com.rsdesenvolvimento.pedido_service.core.client.dtos.EstoqueResponseDto;
import com.rsdesenvolvimento.pedido_service.core.ports.EstoquePort;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class EstoqueFallback implements EstoquePort {

    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public Boolean validarEstoque(List<AtualizarEstoqueRequestDto> itens) {
        EstoqueFallback.log.warn(
                "Serviço de estoque indisponível. Usando fallback para validação de estoque: {}",
                itens);
        try {
            for (AtualizarEstoqueRequestDto item : itens) {
                String key = "estoque_produto_" + item.getProdutoId();
                Integer estoqueCache = (Integer) this.redisTemplate.opsForValue().get(key);

                if (estoqueCache == null || estoqueCache < item.getQuantidade()) {
                    EstoqueFallback.log.warn(
                            "Estoque insuficiente para o produto ID: {}. Disponível: {}, Necessário: {}",
                            item.getProdutoId(), estoqueCache, item.getQuantidade());
                    return false;
                }

            }
        } catch (Exception e) {
            EstoqueFallback.log.error("Erro ao validar estoque: {}", e.getMessage());
        }
        return true;
    }

    @Override
    public void reservarEstoque(List<AtualizarEstoqueRequestDto> itens) {
        EstoqueFallback.log.warn(
                "Serviço de estoque indisponível. Usando fallback para reserva de estoque: {}",
                itens);
        try {
            for (AtualizarEstoqueRequestDto item : itens) {
                String key = "estoque_produto_" + item.getProdutoId();
                Integer estoqueCache = (Integer) this.redisTemplate.opsForValue().get(key);

                if (estoqueCache != null && estoqueCache >= item.getQuantidade()) {
                    this.redisTemplate.opsForValue().set(key, estoqueCache - item.getQuantidade());
                    EstoqueFallback.log.info(
                            "Estoque reservado para o produto ID: {}. Novo estoque: {}",
                            item.getProdutoId(), estoqueCache - item.getQuantidade());
                } else {
                    EstoqueFallback.log.warn(
                            "Estoque insuficiente para reserva do produto ID: {}. Disponível: {}, Necessário: {}",
                            item.getProdutoId(), estoqueCache, item.getQuantidade());
                }
            }
        } catch (Exception e) {
            EstoqueFallback.log.error("Erro ao reservar estoque: {}", e.getMessage());
        }
    }

    @Override
    public EstoqueResponseDto buscarProduto(Long id) {
        EstoqueFallback.log.warn(
                "Serviço de estoque indisponível. Usando fallback para buscar produto ID: {}", id);
        return null;
    }

    @Override
    public AtualizarEstoqueRequestDto atualizarEstoque(AtualizarEstoqueRequestDto dto) {
        EstoqueFallback.log.warn(
                "Serviço de estoque indisponível. Usando fallback para atualizar estoque: {}", dto);
        return null;
    }
}
