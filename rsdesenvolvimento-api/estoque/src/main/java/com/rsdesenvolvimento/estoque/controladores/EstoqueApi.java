package com.rsdesenvolvimento.estoque.controladores;

import com.rsdesenvolvimento.estoque.modelo.dtos.EstoqueRequestDto;
import com.rsdesenvolvimento.estoque.modelo.dtos.EstoqueResponseDto;
import com.rsdesenvolvimento.estoque.modelo.dtos.ReservaEstoqueRequestDto;
import com.rsdesenvolvimento.estoque.services.EstoqueService;
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
@RequestMapping("/estoque")
@RequiredArgsConstructor
public class EstoqueApi {

  private final EstoqueService service;

  @PostMapping
  public ResponseEntity<EstoqueResponseDto> cadastrarProduto(@RequestBody EstoqueRequestDto dto) {
    return ResponseEntity.status(HttpStatus.CREATED).body(this.service.salvar(dto));
  }

  @GetMapping
  public ResponseEntity<List<EstoqueResponseDto>> listarProdutos(
      @RequestParam(required = false) String categoria) {
    return ResponseEntity.ok(this.service.listarPorCategoria(categoria));
  }

  @GetMapping("/{id}")
  public ResponseEntity<EstoqueResponseDto> buscarProduto(@PathVariable Long id) {
    return ResponseEntity.ok(this.service.buscarPorId(id));
  }

  @PutMapping("/{id}")
  public ResponseEntity<EstoqueResponseDto> atualizarProduto(@PathVariable Long id,
      @RequestBody EstoqueRequestDto dto) {
    return ResponseEntity.ok(this.service.atualizar(id, dto));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> excluirProduto(@PathVariable Long id) {
    this.service.excluir(id);
    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }

  @PostMapping("/validar")
  public ResponseEntity<Boolean> validarEstoque(@RequestBody List<ReservaEstoqueRequestDto> itens) {
    boolean disponivel = this.service.validarEstoque(itens);
    return ResponseEntity.ok(disponivel);
  }

  @PostMapping("/reservar")
  public ResponseEntity<Void> reservarEstoque(@RequestBody List<ReservaEstoqueRequestDto> itens) {
    this.service.reservarEstoque(itens);
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }
}
