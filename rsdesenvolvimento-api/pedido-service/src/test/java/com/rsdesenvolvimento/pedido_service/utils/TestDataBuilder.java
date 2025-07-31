package com.rsdesenvolvimento.pedido_service.utils;

import com.rsdesenvolvimento.pedido_service.core.client.dtos.AtualizarEstoqueRequestDto;
import com.rsdesenvolvimento.pedido_service.core.client.dtos.EstoqueResponseDto;
import com.rsdesenvolvimento.pedido_service.modelo.dtos.ItemPedidoRequestDto;
import com.rsdesenvolvimento.pedido_service.modelo.dtos.ItemPedidoResponseDto;
import com.rsdesenvolvimento.pedido_service.modelo.dtos.PedidoRequesteDto;
import com.rsdesenvolvimento.pedido_service.modelo.dtos.PedidoResponseDto;
import com.rsdesenvolvimento.pedido_service.modelo.entidades.ItemPedido;
import com.rsdesenvolvimento.pedido_service.modelo.entidades.Pedido;
import com.rsdesenvolvimento.pedido_service.modelo.entidades.Usuario;
import com.rsdesenvolvimento.pedido_service.modelo.enums.StatusEnum;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

/**
 * Classe utilitária para criar objetos de teste (Test Data Builders) Facilita a criação de dados de
 * teste padronizados e reutilizáveis
 */
public final class TestDataBuilder {

    private TestDataBuilder() {
        // Classe utilitária - construtor privado
    }

    /**
     * Builder para criar entidade Pedido de teste
     */
    public static class PedidoTestBuilder {
        private Long id = null;
        private String usuarioId = "55861bd6-0651-4c53-b6b5-10c982468080";
        private String observacao = "Observação de teste";
        private String nomeUsuario = "Xpto Silva";
        private String emailUsuario = PedidoTestBuilder.generateUniqueEmail();
        private StatusEnum status = StatusEnum.PENDENTE;
        private LocalDateTime dataHoraCriacao = LocalDateTime.now();
        private List<ItemPedido> itensPedido = Arrays.asList();

        private static String generateUniqueEmail() {
            return "xpto" + System.nanoTime() + "@teste.com";
        }

        public PedidoTestBuilder comId(Long id) {
            this.id = id;
            return this;
        }

        public PedidoTestBuilder comUsuarioId(String usuarioId) {
            this.usuarioId = usuarioId;
            return this;
        }

        public PedidoTestBuilder comObservacao(String observacao) {
            this.observacao = observacao;
            return this;
        }

        public PedidoTestBuilder comNomeUsuario(String nomeUsuario) {
            this.nomeUsuario = nomeUsuario;
            return this;
        }

        public PedidoTestBuilder comEmailUsuario(String emailUsuario) {
            this.emailUsuario = emailUsuario;
            return this;
        }

        public PedidoTestBuilder comStatus(StatusEnum status) {
            this.status = status;
            return this;
        }

        public PedidoTestBuilder comDataHoraCriacao(LocalDateTime dataHoraCriacao) {
            this.dataHoraCriacao = dataHoraCriacao;
            return this;
        }

        public PedidoTestBuilder comItensPedido(List<ItemPedido> itensPedido) {
            this.itensPedido = itensPedido;
            return this;
        }

        public PedidoTestBuilder pendente() {
            this.status = StatusEnum.PENDENTE;
            return this;
        }

        public PedidoTestBuilder emAndamento() {
            this.status = StatusEnum.EM_ANDAMENTO;
            return this;
        }

        public PedidoTestBuilder finalizado() {
            this.status = StatusEnum.FINALIZADO;
            return this;
        }

        public PedidoTestBuilder cancelado() {
            this.status = StatusEnum.CANCELADO;
            return this;
        }

        public Pedido build() {
            return Pedido.builder().id(this.id).usuarioId(this.usuarioId)
                    .observacao(this.observacao).nomeUsuario(this.nomeUsuario)
                    .emailUsuario(this.emailUsuario).status(this.status)
                    .dataHoraCriacao(this.dataHoraCriacao).itensPedido(this.itensPedido).build();
        }
    }

    /**
     * Builder para criar ItemPedido de teste
     */
    public static class ItemPedidoTestBuilder {
        private Long id = null;
        private Long produtoId = 1L;
        private String nomeProduto = "Produto Teste";
        private int quantidade = 2;
        private BigDecimal precoUnitario = BigDecimal.valueOf(29.99);
        private Pedido pedido = null;

        public ItemPedidoTestBuilder comId(Long id) {
            this.id = id;
            return this;
        }

        public ItemPedidoTestBuilder comProdutoId(Long produtoId) {
            this.produtoId = produtoId;
            return this;
        }

        public ItemPedidoTestBuilder comNomeProduto(String nomeProduto) {
            this.nomeProduto = nomeProduto;
            return this;
        }

        public ItemPedidoTestBuilder comQuantidade(int quantidade) {
            this.quantidade = quantidade;
            return this;
        }

        public ItemPedidoTestBuilder comPrecoUnitario(BigDecimal precoUnitario) {
            this.precoUnitario = precoUnitario;
            return this;
        }

        public ItemPedidoTestBuilder comPedido(Pedido pedido) {
            this.pedido = pedido;
            return this;
        }

        public ItemPedido build() {
            return ItemPedido.builder().id(this.id).produtoId(this.produtoId)
                    .nomeProduto(this.nomeProduto).quantidade(this.quantidade)
                    .precoUnitario(this.precoUnitario).pedido(this.pedido).build();
        }
    }

    /**
     * Builder para criar PedidoRequesteDto de teste
     */
    public static class PedidoRequesteDtoTestBuilder {
        private String usuarioId = "55861bd6-0651-4c53-b6b5-10c982468080";
        private String observacao = "Observação de teste";
        private String nomeUsuario = "Xpto Silva";
        private String emailUsuario = "xpto@teste.com";
        private List<ItemPedidoRequestDto> itensPedido =
                Arrays.asList(TestDataBuilder.umItemPedidoRequestDto().build());

        public PedidoRequesteDtoTestBuilder comUsuarioId(String usuarioId) {
            this.usuarioId = usuarioId;
            return this;
        }

        public PedidoRequesteDtoTestBuilder comObservacao(String observacao) {
            this.observacao = observacao;
            return this;
        }

        public PedidoRequesteDtoTestBuilder comNomeUsuario(String nomeUsuario) {
            this.nomeUsuario = nomeUsuario;
            return this;
        }

        public PedidoRequesteDtoTestBuilder comEmailUsuario(String emailUsuario) {
            this.emailUsuario = emailUsuario;
            return this;
        }

        public PedidoRequesteDtoTestBuilder comItensPedido(List<ItemPedidoRequestDto> itensPedido) {
            this.itensPedido = itensPedido;
            return this;
        }

        public PedidoRequesteDtoTestBuilder semItens() {
            this.itensPedido = Arrays.asList();
            return this;
        }

        public PedidoRequesteDto build() {
            PedidoRequesteDto dto = new PedidoRequesteDto();
            dto.setUsuarioId(this.usuarioId);
            dto.setObservacao(this.observacao);
            dto.setNomeUsuario(this.nomeUsuario);
            dto.setEmailUsuario(this.emailUsuario);
            dto.setItensPedido(this.itensPedido);
            return dto;
        }
    }

    /**
     * Builder para criar ItemPedidoRequestDto de teste
     */
    public static class ItemPedidoRequestDtoTestBuilder {
        private Long produtoId = 1L;
        private Integer quantidade = 2;
        private BigDecimal precoUnitario = BigDecimal.valueOf(29.99);

        public ItemPedidoRequestDtoTestBuilder comProdutoId(Long produtoId) {
            this.produtoId = produtoId;
            return this;
        }

        public ItemPedidoRequestDtoTestBuilder comQuantidade(Integer quantidade) {
            this.quantidade = quantidade;
            return this;
        }

        public ItemPedidoRequestDtoTestBuilder comPrecoUnitario(BigDecimal precoUnitario) {
            this.precoUnitario = precoUnitario;
            return this;
        }

        public ItemPedidoRequestDto build() {
            ItemPedidoRequestDto dto = new ItemPedidoRequestDto();
            dto.setProdutoId(this.produtoId);
            dto.setQuantidade(this.quantidade);
            dto.setPrecoUnitario(this.precoUnitario);
            return dto;
        }
    }

    /**
     * Builder para criar PedidoResponseDto de teste
     */
    public static class PedidoResponseDtoTestBuilder {
        private Long id = 1L;
        private String usuarioId = "55861bd6-0651-4c53-b6b5-10c982468080";
        private String observacao = "Observação de teste";
        private String nomeUsuario = "Xpto Silva";
        private String emailUsuario = "xpto@teste.com";
        private StatusEnum status = StatusEnum.PENDENTE;
        private LocalDateTime dataHoraCriacao = LocalDateTime.now();
        private List<ItemPedidoResponseDto> itensPedido = Arrays.asList();

        public PedidoResponseDtoTestBuilder comId(Long id) {
            this.id = id;
            return this;
        }

        public PedidoResponseDtoTestBuilder comUsuarioId(String usuarioId) {
            this.usuarioId = usuarioId;
            return this;
        }

        public PedidoResponseDtoTestBuilder comObservacao(String observacao) {
            this.observacao = observacao;
            return this;
        }

        public PedidoResponseDtoTestBuilder comStatus(StatusEnum status) {
            this.status = status;
            return this;
        }

        public PedidoResponseDtoTestBuilder comNomeUsuario(String nomeUsuario) {
            this.nomeUsuario = nomeUsuario;
            return this;
        }

        public PedidoResponseDtoTestBuilder comEmailUsuario(String emailUsuario) {
            this.emailUsuario = emailUsuario;
            return this;
        }

        public PedidoResponseDto build() {
            PedidoResponseDto dto = new PedidoResponseDto();
            dto.setId(this.id);
            dto.setUsuarioId(this.usuarioId);
            dto.setObservacao(this.observacao);
            dto.setNomeUsuario(this.nomeUsuario);
            dto.setEmailUsuario(this.emailUsuario);
            dto.setStatus(this.status);
            dto.setDataHoraCriacao(this.dataHoraCriacao);
            dto.setItensPedido(this.itensPedido);
            return dto;
        }
    }

    /**
     * Builder para criar Usuario de teste
     */
    public static class UsuarioTestBuilder {
        private String id = "55861bd6-0651-4c53-b6b5-10c982468080"; // Alinhado com outros builders
        private String username = "Xpto Silva";
        private String email = "xpto@teste.com";

        public UsuarioTestBuilder comId(String id) {
            this.id = id;
            return this;
        }

        public UsuarioTestBuilder comUsername(String username) {
            this.username = username;
            return this;
        }

        public UsuarioTestBuilder comEmail(String email) {
            this.email = email;
            return this;
        }

        public Usuario build() {
            return new Usuario(this.id, this.username, this.email);
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
        private Integer quantidade = 2;

        public AtualizarEstoqueRequestDtoTestBuilder comProdutoId(Long produtoId) {
            this.produtoId = produtoId;
            return this;
        }

        public AtualizarEstoqueRequestDtoTestBuilder comQuantidade(Integer quantidade) {
            this.quantidade = quantidade;
            return this;
        }

        public AtualizarEstoqueRequestDto build() {
            return new AtualizarEstoqueRequestDto(this.produtoId, this.quantidade);
        }
    }

    // Métodos factory para iniciar os builders
    public static PedidoTestBuilder umPedido() {
        return new PedidoTestBuilder();
    }

    public static ItemPedidoTestBuilder umItemPedido() {
        return new ItemPedidoTestBuilder();
    }

    public static PedidoRequesteDtoTestBuilder umPedidoRequesteDto() {
        return new PedidoRequesteDtoTestBuilder();
    }

    public static ItemPedidoRequestDtoTestBuilder umItemPedidoRequestDto() {
        return new ItemPedidoRequestDtoTestBuilder();
    }

    public static PedidoResponseDtoTestBuilder umPedidoResponseDto() {
        return new PedidoResponseDtoTestBuilder();
    }

    public static UsuarioTestBuilder umUsuario() {
        return new UsuarioTestBuilder();
    }

    public static EstoqueResponseDtoTestBuilder umEstoqueResponseDto() {
        return new EstoqueResponseDtoTestBuilder();
    }

    public static AtualizarEstoqueRequestDtoTestBuilder umAtualizarEstoqueRequestDto() {
        return new AtualizarEstoqueRequestDtoTestBuilder();
    }

    // Constantes úteis para testes
    public static final class TestConstants {
        public static final String USER_ID_TEST = "55861bd6-0651-4c53-b6b5-10c982468080";
        public static final String NOME_USUARIO_TEST = "Xpto";
        public static final String EMAIL_USUARIO_TEST = "xpto@teste.com";
        public static final String OBSERVACAO_TEST = "Observação de teste";

        public static final Long PRODUTO_ID_TEST = 1L;
        public static final String NOME_PRODUTO_TEST = "Produto Teste";
        public static final Integer QUANTIDADE_TEST = 2;
        public static final BigDecimal PRECO_UNITARIO_TEST = BigDecimal.valueOf(29.99);

        public static final StatusEnum STATUS_PENDENTE = StatusEnum.PENDENTE;
        public static final StatusEnum STATUS_EM_ANDAMENTO = StatusEnum.EM_ANDAMENTO;
        public static final StatusEnum STATUS_FINALIZADO = StatusEnum.FINALIZADO;
        public static final StatusEnum STATUS_CANCELADO = StatusEnum.CANCELADO;

        private TestConstants() {
            // Classe de constantes - construtor privado
        }
    }
}
