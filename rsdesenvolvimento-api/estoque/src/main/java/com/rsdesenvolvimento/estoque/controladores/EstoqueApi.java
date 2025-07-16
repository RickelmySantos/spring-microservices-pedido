package com.rsdesenvolvimento.estoque.controladores;

import com.rsdesenvolvimento.estoque.modelo.dtos.AtualizarEstoqueRequestDto;
import com.rsdesenvolvimento.estoque.modelo.dtos.EstoqueRequestDto;
import com.rsdesenvolvimento.estoque.modelo.dtos.EstoqueResponseDto;
import com.rsdesenvolvimento.estoque.services.CloudinaryService;
import com.rsdesenvolvimento.estoque.services.EstoqueService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/estoque")
@RequiredArgsConstructor
public class EstoqueApi {

    private final EstoqueService service;
    private final CloudinaryService cloudinaryService;

    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<EstoqueResponseDto> cadastrarProduto(
            @RequestPart("dados") EstoqueRequestDto dto,
            @RequestPart("imagem") MultipartFile imagem) throws Exception {
        return ResponseEntity.status(HttpStatus.CREATED).body(this.service.salvar(dto, imagem));
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
    public ResponseEntity<Boolean> validarEstoque(
            @RequestBody List<AtualizarEstoqueRequestDto> itens) {
        boolean disponivel = this.service.validarEstoque(itens);
        return ResponseEntity.ok(disponivel);
    }

    @PostMapping("/reservar")
    public ResponseEntity<Void> reservarEstoque(
            @RequestBody List<AtualizarEstoqueRequestDto> itens) {
        this.service.reservarEstoque(itens);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/atualizar")
    public ResponseEntity<Void> atualizarEstoque(@RequestBody AtualizarEstoqueRequestDto dto) {
        this.service.atualizarEstoque(dto);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadImagem(@RequestParam("file") MultipartFile file) {
        try {
            String url = this.cloudinaryService.uploadImage(file);
            return ResponseEntity.ok(url);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao fazer upload da imagem: " + e.getMessage());
        }
    }
}
