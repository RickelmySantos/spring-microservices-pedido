package com.rsdesenvolvimento.estoque.services;

import com.rsdesenvolvimento.estoque.modelo.dtos.ProdutoRequestDto;
import com.rsdesenvolvimento.estoque.modelo.dtos.ProdutoResponseDto;
import com.rsdesenvolvimento.estoque.modelo.entidade.Produto;
import com.rsdesenvolvimento.estoque.modelo.mappers.ProdutoMapper;
import com.rsdesenvolvimento.estoque.repositorios.ProdutoRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProdutoService {

  private final ProdutoRepository repository;
  private final ProdutoMapper mapper;


  public ProdutoResponseDto salvar(ProdutoRequestDto produtoRequestDTO) {
    Produto produto = this.mapper.toEntity(produtoRequestDTO);

    return this.mapper.toDto(this.repository.save(produto));
  }

  public List<ProdutoResponseDto> listarPorCategoria(String categoria) {
    List<Produto> produtos =
        categoria != null && !categoria.isEmpty() ? this.repository.findByCategoria(categoria)
            : this.repository.findAll();
    return this.mapper.toDtoList(produtos);
  }

  public ProdutoResponseDto buscarPorId(Long id) {
    Produto produto = this.repository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("Produto não encontrado"));
    return this.mapper.toDto(produto);
  }

  public ProdutoResponseDto atualizar(Long id, ProdutoRequestDto dto) {
    Produto produtoExistente = this.repository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("Produto não encontrado"));

    Produto atualizado = this.mapper.toEntity(dto);
    atualizado.setId(produtoExistente.getId());

    return this.mapper.toDto(this.repository.save(atualizado));
  }

  public void excluir(Long id) {
    this.repository.deleteById(id);
  }
}
