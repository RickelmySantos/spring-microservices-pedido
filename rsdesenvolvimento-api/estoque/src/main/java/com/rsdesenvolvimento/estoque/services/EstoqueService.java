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
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class EstoqueService {

    private static final String PRODUTO_NAO_ENCONTRADO = "Produto não encontrado";


    private final EstoqueRepository repository;
    private final EstoqueMapper mapper;
    private final CloudinaryService cloudinaryService;


    public EstoqueResponseDto salvar(EstoqueRequestDto produtoRequestDTO, MultipartFile imagem)
            throws Exception {
        String url = this.cloudinaryService.uploadImage(imagem);
        Estoque produto = this.mapper.toEntity(produtoRequestDTO);
        produto.setImagemUrl(url);

        return this.mapper.toDto(this.repository.save(produto));
    }

    public List<EstoqueResponseDto> listarPorCategoria(String categoria) {
        List<Estoque> produtos = categoria != null && !categoria.isEmpty()
                ? this.repository.findByCategoria(categoria)
                : this.repository.findAll();
        return this.mapper.toDtoList(produtos);
    }

    public EstoqueResponseDto buscarPorId(Long id) {
        Estoque produto = this.repository.findById(id).orElseThrow(
                () -> new IllegalArgumentException(EstoqueService.PRODUTO_NAO_ENCONTRADO));
        return this.mapper.toDto(produto);
    }

    public EstoqueResponseDto atualizar(Long id, EstoqueRequestDto dto) {
        Estoque produtoExistente = this.repository.findById(id).orElseThrow(
                () -> new IllegalArgumentException(EstoqueService.PRODUTO_NAO_ENCONTRADO));

        Estoque atualizado = this.mapper.toEntity(dto);
        atualizado.setId(produtoExistente.getId());

        return this.mapper.toDto(this.repository.save(atualizado));
    }

    public void excluir(Long id) {
        this.repository.deleteById(id);
    }

    public boolean validarEstoque(List<ReservaEstoqueRequestDto> itens) {
        for (ReservaEstoqueRequestDto item : itens) {
            Estoque produto = this.repository.findById(item.getProdutoId()).orElseThrow(
                    () -> new IllegalArgumentException(EstoqueService.PRODUTO_NAO_ENCONTRADO));
            if (produto.getEstoque() < item.getQuantidade()) {
                return false;
            }
        }
        return true;
    }

    public void reservarEstoque(List<ReservaEstoqueRequestDto> itens) {
        for (ReservaEstoqueRequestDto item : itens) {
            Estoque produto = this.repository.findById(item.getProdutoId()).orElseThrow(
                    () -> new IllegalArgumentException(EstoqueService.PRODUTO_NAO_ENCONTRADO));

            produto.setEstoque(produto.getEstoque() - item.getQuantidade());

            this.repository.save(produto);
        }
    }

}
