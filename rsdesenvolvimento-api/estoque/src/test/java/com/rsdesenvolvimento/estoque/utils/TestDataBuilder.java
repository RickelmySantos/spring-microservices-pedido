package com.rsdesenvolvimento.estoque.utils;

import com.rsdesenvolvimento.estoque.modelo.dtos.AtualizarEstoqueRequestDto;
import com.rsdesenvolvimento.estoque.modelo.dtos.EstoqueRequestDto;
import com.rsdesenvolvimento.estoque.modelo.dtos.EstoqueResponseDto;
import com.rsdesenvolvimento.estoque.modelo.entidade.Estoque;
import java.math.BigDecimal;

/**
 * Classe utilitária para criar objetos de teste (Test Data Builders) Facilita a criação de dados de
 * teste padronizados e reutilizáveis
 */
public final class TestDataBuilder {

    private TestDataBuilder() {
        // Classe utilitária - construtor privado
    }

    /**
     * Builder para criar entidade Estoque de teste
     */
    public static class EstoqueTestBuilder {
        private Long id = 1L;
        private String nome = "Produto Teste";
        private String descricao = "Descrição do produto teste";
        private BigDecimal preco = BigDecimal.valueOf(29.99);
        private String categoria = "ELETRONICOS";
        private Integer estoque = 100;
        private String imagemUrl = "https://example.com/imagem.jpg";

        public EstoqueTestBuilder comId(Long id) {
            this.id = id;
            return this;
        }

        public EstoqueTestBuilder comNome(String nome) {
            this.nome = nome;
            return this;
        }

        public EstoqueTestBuilder comDescricao(String descricao) {
            this.descricao = descricao;
            return this;
        }

        public EstoqueTestBuilder comPreco(BigDecimal preco) {
            this.preco = preco;
            return this;
        }

        public EstoqueTestBuilder comCategoria(String categoria) {
            this.categoria = categoria;
            return this;
        }

        public EstoqueTestBuilder comEstoque(Integer estoque) {
            this.estoque = estoque;
            return this;
        }

        public EstoqueTestBuilder comImagemUrl(String imagemUrl) {
            this.imagemUrl = imagemUrl;
            return this;
        }

        public EstoqueTestBuilder estoqueEsgotado() {
            this.estoque = 0;
            return this;
        }

        public EstoqueTestBuilder estoqueComPoucasUnidades() {
            this.estoque = 5;
            return this;
        }

        public Estoque build() {
            return Estoque.builder().id(this.id).nome(this.nome).descricao(this.descricao)
                    .preco(this.preco).categoria(this.categoria).estoque(this.estoque)
                    .imagemUrl(this.imagemUrl).build();
        }
    }

    /**
     * Builder para criar EstoqueRequestDto de teste
     */
    public static class EstoqueRequestDtoTestBuilder {
        private String nome = "Produto Teste";
        private String descricao = "Descrição do produto teste";
        private BigDecimal preco = BigDecimal.valueOf(29.99);
        private String categoria = "ELETRONICOS";
        private Integer estoque = 100;
        private String imagemUrl = "https://example.com/imagem.jpg";

        public EstoqueRequestDtoTestBuilder comNome(String nome) {
            this.nome = nome;
            return this;
        }

        public EstoqueRequestDtoTestBuilder comDescricao(String descricao) {
            this.descricao = descricao;
            return this;
        }

        public EstoqueRequestDtoTestBuilder comPreco(BigDecimal preco) {
            this.preco = preco;
            return this;
        }

        public EstoqueRequestDtoTestBuilder comCategoria(String categoria) {
            this.categoria = categoria;
            return this;
        }

        public EstoqueRequestDtoTestBuilder comEstoque(Integer estoque) {
            this.estoque = estoque;
            return this;
        }

        public EstoqueRequestDtoTestBuilder comImagemUrl(String imagemUrl) {
            this.imagemUrl = imagemUrl;
            return this;
        }

        public EstoqueRequestDto build() {
            EstoqueRequestDto dto = new EstoqueRequestDto();
            dto.setNome(this.nome);
            dto.setDescricao(this.descricao);
            dto.setPreco(this.preco);
            dto.setCategoria(this.categoria);
            dto.setEstoque(this.estoque);
            dto.setImagemUrl(this.imagemUrl);
            return dto;
        }
    }

    /**
     * Builder para criar EstoqueResponseDto de teste
     */
    public static class EstoqueResponseDtoTestBuilder {
        private Long id = 1L;
        private String nome = "Produto Teste";
        private String descricao = "Descrição do produto teste";
        private BigDecimal preco = BigDecimal.valueOf(29.99);
        private String categoria = "ELETRONICOS";
        private Integer estoque = 100;
        private String imagemUrl = "https://example.com/imagem.jpg";

        public EstoqueResponseDtoTestBuilder comId(Long id) {
            this.id = id;
            return this;
        }

        public EstoqueResponseDtoTestBuilder comNome(String nome) {
            this.nome = nome;
            return this;
        }

        public EstoqueResponseDtoTestBuilder comDescricao(String descricao) {
            this.descricao = descricao;
            return this;
        }

        public EstoqueResponseDtoTestBuilder comPreco(BigDecimal preco) {
            this.preco = preco;
            return this;
        }

        public EstoqueResponseDtoTestBuilder comCategoria(String categoria) {
            this.categoria = categoria;
            return this;
        }

        public EstoqueResponseDtoTestBuilder comEstoque(Integer estoque) {
            this.estoque = estoque;
            return this;
        }

        public EstoqueResponseDtoTestBuilder comImagemUrl(String imagemUrl) {
            this.imagemUrl = imagemUrl;
            return this;
        }

        public EstoqueResponseDto build() {
            EstoqueResponseDto dto = new EstoqueResponseDto();
            dto.setId(this.id);
            dto.setNome(this.nome);
            dto.setDescricao(this.descricao);
            dto.setPreco(this.preco);
            dto.setCategoria(this.categoria);
            dto.setEstoque(this.estoque);
            dto.setImagemUrl(this.imagemUrl);
            return dto;
        }
    }

    /**
     * Builder para criar AtualizarEstoqueRequestDto de teste
     */
    public static class AtualizarEstoqueRequestDtoTestBuilder {
        private Long produtoId = 1L;
        private Integer quantidade = 10;

        public AtualizarEstoqueRequestDtoTestBuilder comProdutoId(Long produtoId) {
            this.produtoId = produtoId;
            return this;
        }

        public AtualizarEstoqueRequestDtoTestBuilder comQuantidade(Integer quantidade) {
            this.quantidade = quantidade;
            return this;
        }

        public AtualizarEstoqueRequestDto build() {
            AtualizarEstoqueRequestDto dto = new AtualizarEstoqueRequestDto();
            dto.setProdutoId(this.produtoId);
            dto.setQuantidade(this.quantidade);
            return dto;
        }
    }

    // Métodos factory para iniciar os builders
    public static EstoqueTestBuilder umEstoque() {
        return new EstoqueTestBuilder();
    }

    public static EstoqueRequestDtoTestBuilder umEstoqueRequestDto() {
        return new EstoqueRequestDtoTestBuilder();
    }

    public static EstoqueResponseDtoTestBuilder umEstoqueResponseDto() {
        return new EstoqueResponseDtoTestBuilder();
    }

    public static AtualizarEstoqueRequestDtoTestBuilder umaAtualizacaoDeEstoque() {
        return new AtualizarEstoqueRequestDtoTestBuilder();
    }

    // Constantes úteis para testes
    public static final class TestConstants {
        public static final String CATEGORIA_ELETRONICOS = "ELETRONICOS";
        public static final String CATEGORIA_LIVROS = "LIVROS";
        public static final String CATEGORIA_CASA_JARDIM = "CASA_JARDIM";
        public static final String CATEGORIA_INEXISTENTE = "CATEGORIA_INEXISTENTE";

        public static final BigDecimal PRECO_PADRAO = BigDecimal.valueOf(29.99);
        public static final BigDecimal PRECO_ALTO = BigDecimal.valueOf(999.99);
        public static final BigDecimal PRECO_BAIXO = BigDecimal.valueOf(9.99);

        public static final Integer ESTOQUE_PADRAO = 100;
        public static final Integer ESTOQUE_BAIXO = 5;
        public static final Integer ESTOQUE_ESGOTADO = 0;

        public static final String URL_IMAGEM_PADRAO = "https://example.com/imagem.jpg";
        public static final String URL_IMAGEM_CLOUDINARY = "https://cloudinary.com/imagem.jpg";

        private TestConstants() {
            // Classe de constantes - construtor privado
        }
    }
}
