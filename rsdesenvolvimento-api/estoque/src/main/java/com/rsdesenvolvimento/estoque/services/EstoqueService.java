package com.rsdesenvolvimento.estoque.services;

import com.rsdesenvolvimento.estoque.core.exception.EstoqueException;
import com.rsdesenvolvimento.estoque.modelo.dtos.AtualizarEstoqueRequestDto;
import com.rsdesenvolvimento.estoque.modelo.dtos.EstoqueRequestDto;
import com.rsdesenvolvimento.estoque.modelo.dtos.EstoqueResponseDto;
import com.rsdesenvolvimento.estoque.modelo.entidade.Estoque;
import com.rsdesenvolvimento.estoque.modelo.mappers.EstoqueMapper;
import com.rsdesenvolvimento.estoque.repositorios.EstoqueRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class EstoqueService {

    private static final String PRODUTO_NAO_ENCONTRADO = "Produto não encontrado";


    private final EstoqueRepository repository;
    private final EstoqueMapper mapper;
    private final CloudinaryService cloudinaryService;

    @CachePut(value = "estoque", key = "#produtoRequestDTO.id")
    public EstoqueResponseDto salvar(EstoqueRequestDto produtoRequestDTO, MultipartFile imagem) {
        String url = this.cloudinaryService.uploadImage(imagem);
        Estoque produto = this.mapper.toEntity(produtoRequestDTO);
        produto.setImagemUrl(url);

        return this.mapper.toDto(this.repository.save(produto));
    }

    @Cacheable(value = "estoque", key = "#categoria !=null ? #categoria : 'all'")
    public List<EstoqueResponseDto> listarPorCategoria(String categoria) {
        List<Estoque> produtos = categoria != null && !categoria.isEmpty()
                ? this.repository.findByCategoria(categoria)
                : this.repository.findAll();
        return this.mapper.toDtoList(produtos);
    }

    @Transactional(readOnly = true)
    public EstoqueResponseDto buscarPorId(Long id) {
        Estoque produto = this.repository.findById(id)
                .orElseThrow(() -> new EstoqueException(EstoqueService.PRODUTO_NAO_ENCONTRADO));
        return this.mapper.toDto(produto);
    }

    public EstoqueResponseDto atualizar(Long id, EstoqueRequestDto dto) {
        Estoque produtoExistente = this.repository.findById(id)
                .orElseThrow(() -> new EstoqueException(EstoqueService.PRODUTO_NAO_ENCONTRADO));

        Estoque atualizado = this.mapper.toEntity(dto);
        atualizado.setId(produtoExistente.getId());

        return this.mapper.toDto(this.repository.save(atualizado));
    }

    public void excluir(Long id) {
        this.repository.deleteById(id);
    }

    public boolean validarEstoque(List<AtualizarEstoqueRequestDto> itens) {
        for (AtualizarEstoqueRequestDto item : itens) {
            Estoque produto = this.repository.findById(item.getProdutoId())
                    .orElseThrow(() -> new EstoqueException(EstoqueService.PRODUTO_NAO_ENCONTRADO));
            if (produto.getEstoque() < item.getQuantidade()) {
                return false;
            }
        }
        return true;
    }

    @CacheEvict(value = "estoque", allEntries = true)
    public void reservarEstoque(List<AtualizarEstoqueRequestDto> itens) {
        for (AtualizarEstoqueRequestDto item : itens) {
            Estoque produto = this.repository.findById(item.getProdutoId())
                    .orElseThrow(() -> new EstoqueException(EstoqueService.PRODUTO_NAO_ENCONTRADO));

            produto.setEstoque(produto.getEstoque() - item.getQuantidade());

            this.repository.save(produto);
        }
    }

    @Transactional
    public void atualizarEstoque(AtualizarEstoqueRequestDto dto) {
        Estoque produto = this.repository.findById(dto.getProdutoId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        EstoqueService.PRODUTO_NAO_ENCONTRADO));

        if (produto.getEstoque() < dto.getQuantidade()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Estoque insuficiente");
        }

        produto.setEstoque(produto.getEstoque() - dto.getQuantidade());

        this.repository.save(produto);
    }

}
