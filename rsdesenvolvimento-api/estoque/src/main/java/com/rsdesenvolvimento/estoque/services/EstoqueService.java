package com.rsdesenvolvimento.estoque.services;

import com.rsdesenvolvimento.estoque.modelo.dtos.EstoqueRequestDto;
import com.rsdesenvolvimento.estoque.modelo.dtos.EstoqueResponseDto;
import com.rsdesenvolvimento.estoque.modelo.dtos.ReservaEstoqueRequestDto;
import com.rsdesenvolvimento.estoque.modelo.entidade.Estoque;
import com.rsdesenvolvimento.estoque.modelo.mappers.EstoqueMapper;
import com.rsdesenvolvimento.estoque.repositorios.EstoqueRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EstoqueService {

  private final EstoqueRepository repository;
  private final EstoqueMapper mapper;


  public EstoqueResponseDto salvar(EstoqueRequestDto produtoRequestDTO) {
    Estoque produto = this.mapper.toEntity(produtoRequestDTO);

    return this.mapper.toDto(this.repository.save(produto));
  }

  public List<EstoqueResponseDto> listarPorCategoria(String categoria) {
    List<Estoque> produtos =
        categoria != null && !categoria.isEmpty() ? this.repository.findByCategoria(categoria)
            : this.repository.findAll();
    return this.mapper.toDtoList(produtos);
  }

  public EstoqueResponseDto buscarPorId(Long id) {
    Estoque produto = this.repository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("Produto não encontrado"));
    return this.mapper.toDto(produto);
  }

  public EstoqueResponseDto atualizar(Long id, EstoqueRequestDto dto) {
    Estoque produtoExistente = this.repository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("Produto não encontrado"));

    Estoque atualizado = this.mapper.toEntity(dto);
    atualizado.setId(produtoExistente.getId());

    return this.mapper.toDto(this.repository.save(atualizado));
  }

  public void excluir(Long id) {
    this.repository.deleteById(id);
  }

  public boolean validarEstoque(List<ReservaEstoqueRequestDto> itens) {
    for (ReservaEstoqueRequestDto item : itens) {
      Estoque produto = this.repository.findById(item.getProdutoId())
          .orElseThrow(() -> new IllegalArgumentException("Produto não encontrado"));
      if (produto.getEstoque() < item.getQuantidade()) {
        return false;
      }
    }
    return true;
  }

  public void reservarEstoque(List<ReservaEstoqueRequestDto> itens) {
    for (ReservaEstoqueRequestDto item : itens) {
      Estoque produto = this.repository.findById(item.getProdutoId())
          .orElseThrow(() -> new IllegalArgumentException("Produto não encontrado"));

      produto.setEstoque(produto.getEstoque() - item.getQuantidade());

      this.repository.save(produto);
    }
  }

}
