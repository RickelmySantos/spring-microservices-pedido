package com.rsdesenvolvimento.pedido_service.repositorios;

import com.rsdesenvolvimento.pedido_service.core.auditoria.AuditoriaConfig;
import com.rsdesenvolvimento.pedido_service.modelo.entidades.ItemPedido;
import com.rsdesenvolvimento.pedido_service.modelo.entidades.Pedido;
import com.rsdesenvolvimento.pedido_service.modelo.enums.StatusEnum;
import com.rsdesenvolvimento.pedido_service.utils.TestDataBuilder;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.hibernate.exception.DataException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;


@DataJpaTest
@ActiveProfiles("test")
@Import(AuditoriaConfig.class)
public class PedidoRepositoryTest {

    @Autowired
    private PedidoRepository pedidoRepository;
    @Autowired
    private TestEntityManager entityManager;



    @Test
    void deveSalvarUmPedidoComSucesso() {
        // ARRANGE
        Pedido pedido = TestDataBuilder.umPedido().comUsuarioId("user-1234").comNomeUsuario("Xpto")
                .comEmailUsuario("xpto@gmail.com").comObservacao("Pedido de teste").pendente()
                .build();

        ItemPedido item1 = TestDataBuilder.umItemPedido().comPedido(pedido).comProdutoId(1L)
                .comNomeProduto("Picanha").comQuantidade(2)
                .comPrecoUnitario(BigDecimal.valueOf(100)).build();

        ItemPedido item2 = TestDataBuilder.umItemPedido().comPedido(pedido).comProdutoId(2L)
                .comNomeProduto("pudim de leite").comQuantidade(1)
                .comPrecoUnitario(BigDecimal.valueOf(50)).build();

        pedido.setItensPedido(List.of(item1, item2));


        // ACT

        Pedido pedidoSalvo = this.pedidoRepository.save(pedido);

        // ASSERT
        Assertions.assertThat(pedidoSalvo).isNotNull();
        Assertions.assertThat(pedidoSalvo.getId()).isNotNull();
        Assertions.assertThat(pedidoSalvo.getUsuarioId()).isEqualTo("user-1234");
        Assertions.assertThat(pedidoSalvo.getNomeUsuario()).isEqualTo("Xpto");

        Assertions.assertThat(pedidoSalvo.getItensPedido()).hasSize(2);
        Assertions.assertThat(pedidoSalvo.getItensPedido().get(0).getId()).isNotNull();
        Assertions.assertThat(pedidoSalvo.getItensPedido().get(0).getNomeProduto())
                .isEqualTo("Picanha");
        Assertions.assertThat(pedidoSalvo.getItensPedido().get(0).getQuantidade()).isEqualTo(2);
        Assertions.assertThat(pedidoSalvo.getItensPedido().get(0).getPrecoUnitario())
                .isEqualTo(BigDecimal.valueOf(100));

        Assertions.assertThat(pedidoSalvo.getItensPedido().get(1).getId()).isNotNull();
        Assertions.assertThat(pedidoSalvo.getItensPedido().get(1).getNomeProduto())
                .isEqualTo("pudim de leite");
        Assertions.assertThat(pedidoSalvo.getItensPedido().get(1).getQuantidade()).isEqualTo(1);
        Assertions.assertThat(pedidoSalvo.getItensPedido().get(1).getPrecoUnitario())
                .isEqualTo(BigDecimal.valueOf(50));

    }

    @Test
    void deve_RetornarPedido_AO_BuscarPorId() {

        // ARRANGE
        Pedido pedido = TestDataBuilder.umPedido().comUsuarioId("user-1234").comNomeUsuario("Xpto")
                .comEmailUsuario("xpto@gmail.com").comObservacao("Pedido de teste").pendente()
                .build();

        ItemPedido item1 = TestDataBuilder.umItemPedido().comPedido(pedido).comProdutoId(1L)
                .comNomeProduto("Picanha").comQuantidade(2)
                .comPrecoUnitario(BigDecimal.valueOf(100)).build();

        ItemPedido item2 = TestDataBuilder.umItemPedido().comPedido(pedido).comProdutoId(2L)
                .comNomeProduto("pudim de leite").comQuantidade(1)
                .comPrecoUnitario(BigDecimal.valueOf(50)).build();

        pedido.setItensPedido(List.of(item1, item2));

        Pedido pedidoSalvo = this.pedidoRepository.save(pedido);

        // ACT
        Pedido result = this.pedidoRepository.findById(pedidoSalvo.getId()).orElse(null);

        // ASSERT
        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.getId()).isEqualTo(pedidoSalvo.getId());
        Assertions.assertThat(result.getUsuarioId()).isEqualTo("user-1234");
        Assertions.assertThat(result.getNomeUsuario()).isEqualTo("Xpto");
        Assertions.assertThat(result.getItensPedido()).hasSize(2);
        Assertions.assertThat(result.getItensPedido().get(0).getId()).isNotNull();
        Assertions.assertThat(result.getItensPedido().get(0).getNomeProduto()).isEqualTo("Picanha");
        Assertions.assertThat(result.getItensPedido().get(0).getQuantidade()).isEqualTo(2);
        Assertions.assertThat(result.getItensPedido().get(0).getPrecoUnitario())
                .isEqualTo(BigDecimal.valueOf(100));
        Assertions.assertThat(result.getItensPedido().get(1).getId()).isNotNull();
        Assertions.assertThat(result.getItensPedido().get(1).getNomeProduto())
                .isEqualTo("pudim de leite");
        Assertions.assertThat(result.getItensPedido().get(1).getQuantidade()).isEqualTo(1);
        Assertions.assertThat(result.getItensPedido().get(1).getPrecoUnitario())
                .isEqualTo(BigDecimal.valueOf(50));
    }

    @Test
    void deve_CriarPedidoCompleto_ComTestDataBuilder() {
        // ARRANGE

        Pedido pedido = TestDataBuilder.umPedido().comUsuarioId("user-9999")
                .comNomeUsuario("Maria Silva").comEmailUsuario("maria@teste.com")
                .comObservacao("Pedido de exemplo usando TestDataBuilder").emAndamento().build();

        ItemPedido item1 = TestDataBuilder.umItemPedido().comPedido(pedido)
                .comProdutoId(TestDataBuilder.TestConstants.PRODUTO_ID_TEST)
                .comNomeProduto("Produto A").comQuantidade(3)
                .comPrecoUnitario(TestDataBuilder.TestConstants.PRECO_UNITARIO_TEST).build();

        ItemPedido item2 = TestDataBuilder.umItemPedido().comPedido(pedido).comProdutoId(2L)
                .comNomeProduto("Produto B").comQuantidade(1)
                .comPrecoUnitario(BigDecimal.valueOf(15.50)).build();

        pedido.setItensPedido(List.of(item1, item2));

        // ACT
        Pedido pedidoSalvo = this.pedidoRepository.save(pedido);

        // ASSERT
        Assertions.assertThat(pedidoSalvo).isNotNull();
        Assertions.assertThat(pedidoSalvo.getId()).isNotNull();
        Assertions.assertThat(pedidoSalvo.getUsuarioId()).isEqualTo("user-9999");
        Assertions.assertThat(pedidoSalvo.getNomeUsuario()).isEqualTo("Maria Silva");
        Assertions.assertThat(pedidoSalvo.getEmailUsuario()).isEqualTo("maria@teste.com");
        Assertions.assertThat(pedidoSalvo.getObservacao())
                .isEqualTo("Pedido de exemplo usando TestDataBuilder");
        Assertions.assertThat(pedidoSalvo.getStatus())
                .isEqualTo(TestDataBuilder.TestConstants.STATUS_EM_ANDAMENTO);
        Assertions.assertThat(pedidoSalvo.getItensPedido()).hasSize(2);
    }

    @Test
    void deve_RetornarOptional_Vazio_QuandoPedidoNaoExistir() {
        // ARRANGE

        String idInexistente = "999";

        // ACT
        Pedido pedido = this.pedidoRepository.findById(Long.valueOf(idInexistente)).orElse(null);

        // ASSERT
        Assertions.assertThat(pedido).isNull();

    }

    @Test
    void deve_AtualizarPedidoExistente_QuandoEntidadeForModificada() {
        // ARRANGE

        Pedido pedido = TestDataBuilder.umPedido().comUsuarioId("user-1234").comNomeUsuario("Xpto")
                .comEmailUsuario("xpto@gmail.com")
                .comObservacao("Pedido de exemplo usando TestDataBuilder").emAndamento().build();

        Pedido pedidoSalvo = this.pedidoRepository.save(pedido);

        // ACT
        pedidoSalvo.setObservacao("Pedido atualizado com sucesso");
        pedidoSalvo.setStatus(StatusEnum.FINALIZADO);

        Pedido pedidoAtualizado = this.pedidoRepository.save(pedidoSalvo);

        // ASSERT
        Assertions.assertThat(pedidoAtualizado.getObservacao())
                .isEqualTo("Pedido atualizado com sucesso");
        Assertions.assertThat(pedidoAtualizado.getStatus()).isEqualTo(StatusEnum.FINALIZADO);
        Assertions.assertThat(pedidoAtualizado.getId()).isEqualTo(pedidoSalvo.getId());
        Assertions.assertThat(pedidoAtualizado.getDataHoraAtualizacao()).isNotNull();
        Assertions.assertThat(pedidoSalvo.getAtualizadoPor()).isEqualTo("PEDIDO_MS");
    }

    @Test
    void deve_DeletarPedidoExistente_QuandoIdForValido() {
        // ARRANGE

        Pedido pedido = TestDataBuilder.umPedido().build();

        Pedido pedidoSalvo = this.pedidoRepository.save(pedido);

        // ACT
        this.pedidoRepository.deleteById(pedidoSalvo.getId());

        // ASSERT
        Optional<Pedido> pedidoRemovido = this.pedidoRepository.findById(pedidoSalvo.getId());
        Assertions.assertThat(pedidoRemovido).isEmpty();

    }

    @Test
    void deve_DeletarEntidadePedido_QuandoEntidadeExistente() {
        // ARRANGE
        Pedido pedido = TestDataBuilder.umPedido().build();

        Pedido pedidoSalvo = this.pedidoRepository.save(pedido);

        // ACT
        this.pedidoRepository.delete(pedidoSalvo);

        // ASSERT
        Optional<Pedido> pedidoRemovido = this.pedidoRepository.findById(pedidoSalvo.getId());
        Assertions.assertThat(pedidoRemovido).isEmpty();
    }

    @Test
    void deve_RetornarTodosPedidos_QuandoExistiremPedidos() {
        // ARRANGE
        Pedido pedido1 = TestDataBuilder.umPedido().comUsuarioId("345").build();
        Pedido pedido2 = TestDataBuilder.umPedido().comUsuarioId("678")
                .comEmailUsuario("xpto2@gmail.com").build();

        this.pedidoRepository.save(pedido1);
        this.pedidoRepository.save(pedido2);

        // ACT
        List<Pedido> pedidos = this.pedidoRepository.findAll();

        // ASSERT
        Assertions.assertThat(pedidos).hasSizeGreaterThanOrEqualTo(2);
    }

    @Test
    void deve_VerificarPedidoExistente_QuandoIdForValido() {

        // ARRANGE
        Pedido pedido = this.pedidoRepository.save(TestDataBuilder.umPedido().build());

        // ACT
        Optional<Pedido> pedidoExistente = this.pedidoRepository.findById(pedido.getId());

        // ASSERT
        Assertions.assertThat(pedidoExistente).isPresent();
        Assertions.assertThat(pedidoExistente.get().getId()).isEqualTo(pedido.getId());
    }

    @Test
    void deve_RetornarTotalPedidos_QuandoExistiremPedidos() {
        // ARRANGE
        this.pedidoRepository.save(TestDataBuilder.umPedido().comUsuarioId("123")
                .comEmailUsuario("xpto1@gmail.com").build());
        this.pedidoRepository.save(TestDataBuilder.umPedido().comUsuarioId("456")
                .comEmailUsuario("xpto2@gmail.com").build());

        // ACT
        long count = this.pedidoRepository.count();

        // ASSERT
        Assertions.assertThat(count).isGreaterThanOrEqualTo(2);
        Assertions.assertThat(count).isEqualTo(this.pedidoRepository.findAll().size());
    }

    @Test
    void deve_RemoverItens_QuandoPedido_Deletado() {
        // ARRANGE
        Pedido pedido = TestDataBuilder.umPedido().build();

        ItemPedido item = TestDataBuilder.umItemPedido().comPedido(pedido).build();

        pedido.setItensPedido(Arrays.asList(item));

        Pedido pedidoSalvo = this.pedidoRepository.save(pedido);
        Long pedidoId = pedidoSalvo.getId();

        this.entityManager.flush();
        this.entityManager.clear();

        // ACT
        this.pedidoRepository.deleteById(pedidoSalvo.getId());
        this.entityManager.flush();

        // ASSERT
        Long countItens = this.entityManager.getEntityManager()
                .createQuery("SELECT COUNT(i) FROM ItemPedido i WHERE i.pedido.id = :pedidoId",
                        Long.class)
                .setParameter("pedidoId", pedidoId).getSingleResult();
        Assertions.assertThat(countItens).isEqualTo(0);
    }


    @Test
    void deveBuscarPedidoComLazyLoadingItens() {
        // ARRANGE
        Pedido pedido = TestDataBuilder.umPedido().build();
        ItemPedido item = TestDataBuilder.umItemPedido().comPedido(pedido).build();
        pedido.setItensPedido(Arrays.asList(item));

        Pedido pedidoSalvo = this.pedidoRepository.save(pedido);
        this.entityManager.flush();
        this.entityManager.clear();

        // ACT
        Optional<Pedido> pedidoOptional = this.pedidoRepository.findById(pedidoSalvo.getId());

        // ASSERT
        Assertions.assertThat(pedidoOptional).isPresent();
        Pedido pedidoEncontrado = pedidoOptional.get();
        Assertions.assertThat(pedidoEncontrado).isNotNull();
        Assertions.assertThat(pedidoEncontrado.getItensPedido()).hasSize(1);
    }

    @Test
    void deveLidarComObservacaoLonga() {
        // ARRANGE
        String observacaoLonga = "a".repeat(255); // Tamanho máximo
        Pedido pedido = TestDataBuilder.umPedido().comObservacao(observacaoLonga).build();

        // ACT
        Pedido pedidoSalvo = this.pedidoRepository.save(pedido);

        // ASSERT
        Assertions.assertThat(pedidoSalvo.getObservacao()).hasSize(255);
        Assertions.assertThat(pedidoSalvo.getObservacao()).isEqualTo(observacaoLonga);
    }

    @Test
    void deveLancarExcecaoComObservacaoMuitoLonga() {
        // ARRANGE
        String observacaoMuitoLonga = "a".repeat(256);
        Pedido pedido = TestDataBuilder.umPedido().comObservacao(observacaoMuitoLonga).build();

        // ACT & ASSERT
        Assertions.assertThatThrownBy(() -> {
            this.pedidoRepository.save(pedido);
            this.entityManager.flush();
        }).isInstanceOf(DataException.class);
    }

    @Test
    void deve_GarantirIsolamento_EntreTestes() {
        // ARRANGE
        Pedido pedido = TestDataBuilder.umPedido().build();

        // ACT
        this.entityManager.persistAndFlush(pedido);
        this.entityManager.clear();

        Pedido pedidoEncontrado = this.entityManager.find(Pedido.class, pedido.getId());

        // ASSERT
        Assertions.assertThat(pedidoEncontrado).isNotNull();
        Assertions.assertThat(pedidoEncontrado.getId()).isEqualTo(pedido.getId());
    }

    @Test
    void deve_Verificar_EstadoDetached_AposClear() {
        // ARRANGE
        Pedido pedido = TestDataBuilder.umPedido().build();
        this.entityManager.persistAndFlush(pedido);

        // ACT
        this.entityManager.clear();
        pedido.setObservacao("Modificação após clear");

        // ASSERT -
        Pedido pedidoNoBanco = this.entityManager.find(Pedido.class, pedido.getId());
        Assertions.assertThat(pedidoNoBanco.getObservacao()).isNotEqualTo("Modificação após clear");
    }

}
