package com.rsdesenvolvimento.pedido_service.services;

import com.rsdesenvolvimento.pedido_service.core.client.dtos.AtualizarEstoqueRequestDto;
import com.rsdesenvolvimento.pedido_service.modelo.dtos.ItemPedidoRequestDto;
import com.rsdesenvolvimento.pedido_service.services.exceptions.EstoqueInsuficienteException;
import com.rsdesenvolvimento.pedido_service.utils.TestDataBuilder;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("OrquestacaoEstoqueService")
class OrquestracaoEstoqueServiceTest {

    @Mock
    private EstoqueService estoqueService;

    @InjectMocks
    private OrquestacaoEstoqueService orquestracaoEstoqueService;

    private ItemPedidoRequestDto itemTeste;
    private List<ItemPedidoRequestDto> itensTeste;

    @BeforeEach
    void setUp() {
        this.itemTeste = TestDataBuilder.umItemPedidoRequestDto().comProdutoId(1L).comQuantidade(2)
                .comPrecoUnitario(BigDecimal.valueOf(50.00)).build();

        this.itensTeste = Arrays.asList(this.itemTeste,
                TestDataBuilder.umItemPedidoRequestDto().comProdutoId(2L).comQuantidade(1)
                        .comPrecoUnitario(BigDecimal.valueOf(100.00)).build());
    }

    @Nested
    @DisplayName("Atualização de Estoque")
    class AtualizacaoEstoque {

        @Test
        @DisplayName("Deve atualizar estoque com sucesso para um item")
        void deveAtualizarEstoqueComSucessoParaUmItem() {
            // Arrange
            List<ItemPedidoRequestDto> itensUnico =
                    Collections.singletonList(OrquestracaoEstoqueServiceTest.this.itemTeste);

            // Act
            Assertions
                    .assertThatCode(
                            () -> OrquestracaoEstoqueServiceTest.this.orquestracaoEstoqueService
                                    .atualizarEstoqueParaPedido(itensUnico))
                    .doesNotThrowAnyException();

            // Assert
            Mockito.verify(OrquestracaoEstoqueServiceTest.this.estoqueService, Mockito.times(1))
                    .atualizarEstoque(ArgumentMatchers.any(AtualizarEstoqueRequestDto.class));
        }

        @Test
        @DisplayName("Deve atualizar estoque com sucesso para múltiplos itens")
        void deveAtualizarEstoqueComSucessoParaMultiplosItens() {
            // Act
            Assertions
                    .assertThatCode(
                            () -> OrquestracaoEstoqueServiceTest.this.orquestracaoEstoqueService
                                    .atualizarEstoqueParaPedido(
                                            OrquestracaoEstoqueServiceTest.this.itensTeste))
                    .doesNotThrowAnyException();

            // Assert
            Mockito.verify(OrquestracaoEstoqueServiceTest.this.estoqueService, Mockito.times(2))
                    .atualizarEstoque(ArgumentMatchers.any(AtualizarEstoqueRequestDto.class));
        }

        @Test
        @DisplayName("Deve chamar estoqueService com dados corretos")
        void deveChamarEstoqueServiceComDadosCorretos() {
            // Arrange
            ArgumentCaptor<AtualizarEstoqueRequestDto> captor =
                    ArgumentCaptor.forClass(AtualizarEstoqueRequestDto.class);

            List<ItemPedidoRequestDto> itensUnico =
                    Collections.singletonList(OrquestracaoEstoqueServiceTest.this.itemTeste);

            // Act
            OrquestracaoEstoqueServiceTest.this.orquestracaoEstoqueService
                    .atualizarEstoqueParaPedido(itensUnico);

            // Assert
            Mockito.verify(OrquestracaoEstoqueServiceTest.this.estoqueService)
                    .atualizarEstoque(captor.capture());

            AtualizarEstoqueRequestDto capturedDto = captor.getValue();
            Assertions.assertThat(capturedDto.getProdutoId()).isEqualTo(1L);
            Assertions.assertThat(capturedDto.getQuantidade()).isEqualTo(2);
        }

        @Test
        @DisplayName("Deve processar cada item da lista individualmente")
        void deveProcessarCadaItemDaListaIndividualmente() {
            // Arrange
            ArgumentCaptor<AtualizarEstoqueRequestDto> captor =
                    ArgumentCaptor.forClass(AtualizarEstoqueRequestDto.class);

            // Act
            OrquestracaoEstoqueServiceTest.this.orquestracaoEstoqueService
                    .atualizarEstoqueParaPedido(OrquestracaoEstoqueServiceTest.this.itensTeste);

            // Assert
            Mockito.verify(OrquestracaoEstoqueServiceTest.this.estoqueService, Mockito.times(2))
                    .atualizarEstoque(captor.capture());

            List<AtualizarEstoqueRequestDto> capturedValues = captor.getAllValues();

            // Primeiro item
            Assertions.assertThat(capturedValues.get(0).getProdutoId()).isEqualTo(1L);
            Assertions.assertThat(capturedValues.get(0).getQuantidade()).isEqualTo(2);

            // Segundo item
            Assertions.assertThat(capturedValues.get(1).getProdutoId()).isEqualTo(2L);
            Assertions.assertThat(capturedValues.get(1).getQuantidade()).isEqualTo(1);
        }

        @Test
        @DisplayName("Deve lançar EstoqueInsuficienteException quando estoqueService falha")
        void deveLancarEstoqueInsuficienteExceptionQuandoEstoqueServiceFalha() {
            // Arrange
            Mockito.doThrow(new RuntimeException("Erro no serviço de estoque"))
                    .when(OrquestracaoEstoqueServiceTest.this.estoqueService)
                    .atualizarEstoque(ArgumentMatchers.any(AtualizarEstoqueRequestDto.class));

            List<ItemPedidoRequestDto> itensUnico =
                    Collections.singletonList(OrquestracaoEstoqueServiceTest.this.itemTeste);

            // Act & Assert
            Assertions
                    .assertThatThrownBy(
                            () -> OrquestracaoEstoqueServiceTest.this.orquestracaoEstoqueService
                                    .atualizarEstoqueParaPedido(itensUnico))
                    .isInstanceOf(EstoqueInsuficienteException.class)
                    .hasMessageContaining("Erro ao atualizar estoque para o pedido")
                    .hasCauseInstanceOf(RuntimeException.class);
        }

        @Test
        @DisplayName("Deve parar execução no primeiro erro")
        void deveParararExecucaoNoPrimeiroErro() {
            // Arrange
            Mockito.doThrow(new RuntimeException("Erro no primeiro item"))
                    .when(OrquestracaoEstoqueServiceTest.this.estoqueService)
                    .atualizarEstoque(ArgumentMatchers.any(AtualizarEstoqueRequestDto.class));

            // Act & Assert
            Assertions
                    .assertThatThrownBy(
                            () -> OrquestracaoEstoqueServiceTest.this.orquestracaoEstoqueService
                                    .atualizarEstoqueParaPedido(
                                            OrquestracaoEstoqueServiceTest.this.itensTeste))
                    .isInstanceOf(EstoqueInsuficienteException.class);

            Mockito.verify(OrquestracaoEstoqueServiceTest.this.estoqueService, Mockito.times(1))
                    .atualizarEstoque(ArgumentMatchers.any(AtualizarEstoqueRequestDto.class));
        }

        @Test
        @DisplayName("Deve lidar com lista vazia sem erro")
        void deveLidarComListaVaziaSemErro() {
            // Arrange
            List<ItemPedidoRequestDto> listaVazia = Collections.emptyList();

            // Act & Assert
            Assertions
                    .assertThatCode(
                            () -> OrquestracaoEstoqueServiceTest.this.orquestracaoEstoqueService
                                    .atualizarEstoqueParaPedido(listaVazia))
                    .doesNotThrowAnyException();

            Mockito.verify(OrquestracaoEstoqueServiceTest.this.estoqueService, Mockito.never())
                    .atualizarEstoque(ArgumentMatchers.any(AtualizarEstoqueRequestDto.class));
        }
    }

    @Nested
    @DisplayName("Reserva de Estoque")
    class ReservaEstoque {

        @Test
        @DisplayName("Deve reservar estoque com sucesso para um item")
        void deveReservarEstoqueComSucessoParaUmItem() {
            // Arrange
            List<ItemPedidoRequestDto> itensUnico =
                    Collections.singletonList(OrquestracaoEstoqueServiceTest.this.itemTeste);

            // Act & Assert
            Assertions
                    .assertThatCode(
                            () -> OrquestracaoEstoqueServiceTest.this.orquestracaoEstoqueService
                                    .reservarEstoqueParaPedido(itensUnico))
                    .doesNotThrowAnyException();
        }

        @Test
        @DisplayName("Deve reservar estoque com sucesso para múltiplos itens")
        void deveReservarEstoqueComSucessoParaMultiplosItens() {
            // Act & Assert
            Assertions
                    .assertThatCode(
                            () -> OrquestracaoEstoqueServiceTest.this.orquestracaoEstoqueService
                                    .reservarEstoqueParaPedido(
                                            OrquestracaoEstoqueServiceTest.this.itensTeste))
                    .doesNotThrowAnyException();
        }

        @Test
        @DisplayName("Deve lidar com lista vazia na reserva")
        void deveLidarComListaVaziaNaReserva() {
            // Arrange
            List<ItemPedidoRequestDto> listaVazia = Collections.emptyList();

            // Act & Assert
            Assertions
                    .assertThatCode(
                            () -> OrquestracaoEstoqueServiceTest.this.orquestracaoEstoqueService
                                    .reservarEstoqueParaPedido(listaVazia))
                    .doesNotThrowAnyException();
        }

        @Test
        @DisplayName("Não deve interagir com estoqueService na reserva")
        void naoDeveInteragirComEstoqueServiceNaReserva() {
            // Act
            OrquestracaoEstoqueServiceTest.this.orquestracaoEstoqueService
                    .reservarEstoqueParaPedido(OrquestracaoEstoqueServiceTest.this.itensTeste);

            // Assert
            Mockito.verify(OrquestracaoEstoqueServiceTest.this.estoqueService, Mockito.never())
                    .atualizarEstoque(ArgumentMatchers.any(AtualizarEstoqueRequestDto.class));
        }
    }

    @Nested
    @DisplayName("Validações de Entrada")
    class ValidacoesEntrada {

        @Test
        @DisplayName("Deve lançar exceção quando lista de itens é nula na atualização")
        void deveLancarExcecaoQuandoListaItensNulaNaAtualizacao() {
            // Act & Assert
            Assertions
                    .assertThatThrownBy(
                            () -> OrquestracaoEstoqueServiceTest.this.orquestracaoEstoqueService
                                    .atualizarEstoqueParaPedido(null))
                    .isInstanceOf(NullPointerException.class);
        }

        @Test
        @DisplayName("Deve lançar exceção quando lista de itens é nula na reserva")
        void deveLancarExcecaoQuandoListaItensNulaNaReserva() {
            // Act & Assert
            Assertions
                    .assertThatThrownBy(
                            () -> OrquestracaoEstoqueServiceTest.this.orquestracaoEstoqueService
                                    .reservarEstoqueParaPedido(null))
                    .isInstanceOf(NullPointerException.class);
        }

        @Test
        @DisplayName("Deve processar item com quantidade zero")
        void deveProcessarItemComQuantidadeZero() {
            // Arrange
            ItemPedidoRequestDto itemQuantidadeZero = TestDataBuilder.umItemPedidoRequestDto()
                    .comProdutoId(1L).comQuantidade(0).build();

            List<ItemPedidoRequestDto> itensComZero = Collections.singletonList(itemQuantidadeZero);

            // Act & Assert
            Assertions
                    .assertThatCode(
                            () -> OrquestracaoEstoqueServiceTest.this.orquestracaoEstoqueService
                                    .atualizarEstoqueParaPedido(itensComZero))
                    .doesNotThrowAnyException();
            Mockito.verify(OrquestracaoEstoqueServiceTest.this.estoqueService, Mockito.times(1))
                    .atualizarEstoque(ArgumentMatchers.any(AtualizarEstoqueRequestDto.class));
        }
    }

    @Nested
    @DisplayName("Testes de Integração")
    class TestesIntegracao {

        @Test
        @DisplayName("Deve criar AtualizarEstoqueRequestDto com dados corretos do item")
        void deveCriarAtualizarEstoqueRequestDtoComDadosCorretos() {
            // Arrange
            ItemPedidoRequestDto itemEspecifico =
                    TestDataBuilder.umItemPedidoRequestDto().comProdutoId(99L).comQuantidade(5)
                            .comPrecoUnitario(BigDecimal.valueOf(25.99)).build();

            ArgumentCaptor<AtualizarEstoqueRequestDto> captor =
                    ArgumentCaptor.forClass(AtualizarEstoqueRequestDto.class);

            // Act
            OrquestracaoEstoqueServiceTest.this.orquestracaoEstoqueService
                    .atualizarEstoqueParaPedido(Collections.singletonList(itemEspecifico));

            // Assert
            Mockito.verify(OrquestracaoEstoqueServiceTest.this.estoqueService)
                    .atualizarEstoque(captor.capture());

            AtualizarEstoqueRequestDto dto = captor.getValue();
            Assertions.assertThat(dto.getProdutoId()).isEqualTo(99L);
            Assertions.assertThat(dto.getQuantidade()).isEqualTo(5);
        }

        @Test
        @DisplayName("Deve manter ordem de processamento dos itens")
        void deveManterOrdemProcessamentoItens() {
            // Arrange
            List<ItemPedidoRequestDto> itensOrdenados =
                    Arrays.asList(TestDataBuilder.umItemPedidoRequestDto().comProdutoId(1L).build(),
                            TestDataBuilder.umItemPedidoRequestDto().comProdutoId(2L).build(),
                            TestDataBuilder.umItemPedidoRequestDto().comProdutoId(3L).build());

            ArgumentCaptor<AtualizarEstoqueRequestDto> captor =
                    ArgumentCaptor.forClass(AtualizarEstoqueRequestDto.class);

            // Act
            OrquestracaoEstoqueServiceTest.this.orquestracaoEstoqueService
                    .atualizarEstoqueParaPedido(itensOrdenados);

            // Assert
            Mockito.verify(OrquestracaoEstoqueServiceTest.this.estoqueService, Mockito.times(3))
                    .atualizarEstoque(captor.capture());

            List<AtualizarEstoqueRequestDto> valoresCaptured = captor.getAllValues();
            Assertions.assertThat(valoresCaptured.get(0).getProdutoId()).isEqualTo(1L);
            Assertions.assertThat(valoresCaptured.get(1).getProdutoId()).isEqualTo(2L);
            Assertions.assertThat(valoresCaptured.get(2).getProdutoId()).isEqualTo(3L);
        }
    }

    @Nested
    @DisplayName("Validações de Dependências")
    class ValidacoesDependencias {

        @Test
        @DisplayName("Deve ter EstoqueService injetado")
        void deveTerEstoqueServiceInjetado() {
            Assertions.assertThat(OrquestracaoEstoqueServiceTest.this.orquestracaoEstoqueService)
                    .isNotNull();
        }

        @Test
        @DisplayName("Deve usar estoqueService para atualização")
        void deveUsarEstoqueServiceParaAtualizacao() {
            // Arrange
            List<ItemPedidoRequestDto> itensUnico =
                    Collections.singletonList(OrquestracaoEstoqueServiceTest.this.itemTeste);

            // Act
            OrquestracaoEstoqueServiceTest.this.orquestracaoEstoqueService
                    .atualizarEstoqueParaPedido(itensUnico);

            // Assert
            Mockito.verify(OrquestracaoEstoqueServiceTest.this.estoqueService, Mockito.times(1))
                    .atualizarEstoque(ArgumentMatchers.any(AtualizarEstoqueRequestDto.class));
        }
    }
}
