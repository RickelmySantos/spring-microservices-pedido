package com.rsdesenvolvimento.estoque.controladores;

import com.rsdesenvolvimento.estoque.modelo.dtos.ProdutoRequestDto;
import com.rsdesenvolvimento.estoque.modelo.dtos.ProdutoResponseDto;
import com.rsdesenvolvimento.estoque.services.ProdutoService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/produtos")
@RequiredArgsConstructor
public class ProdutoApi {

  private final ProdutoService produtoService;

  @PostMapping
  public ResponseEntity<ProdutoResponseDto> cadastrarProduto(@RequestBody ProdutoRequestDto dto) {
    return ResponseEntity.status(HttpStatus.CREATED).body(this.produtoService.salvar(dto));
  }

  @GetMapping
  public ResponseEntity<List<ProdutoResponseDto>> listarProdutos(
      @RequestParam(required = false) String categoria) {
    return ResponseEntity.ok(this.produtoService.listarPorCategoria(categoria));
  }

  @GetMapping("/{id}")
  public ResponseEntity<ProdutoResponseDto> buscarProduto(@PathVariable Long id) {
    return ResponseEntity.ok(this.produtoService.buscarPorId(id));
  }

  @PutMapping("/{id}")
  public ResponseEntity<ProdutoResponseDto> atualizarProduto(@PathVariable Long id,
      @RequestBody ProdutoRequestDto dto) {
    return ResponseEntity.ok(this.produtoService.atualizar(id, dto));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> excluirProduto(@PathVariable Long id) {
    this.produtoService.excluir(id);
    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }
}
