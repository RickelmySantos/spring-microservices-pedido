package com.rsdesenvolvimento.pedido_service.services;

import com.rsdesenvolvimento.pedido_service.core.client.dtos.AtualizarEstoqueRequestDto;
import com.rsdesenvolvimento.pedido_service.core.client.dtos.EstoqueResponseDto;
import com.rsdesenvolvimento.pedido_service.core.ports.EstoquePort;
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
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

@ExtendWith(MockitoExtension.class)
@DisplayName("EstoqueService")
class EstoqueServiceTest {

    @Mock
    private EstoquePort estoquePort;

    @InjectMocks
    private EstoqueService estoqueService;

    private AtualizarEstoqueRequestDto atualizarEstoqueDto;
    private EstoqueResponseDto estoqueResponseDto;
    private List<AtualizarEstoqueRequestDto> listaItens;

    @BeforeEach
    void setUp() {
        this.atualizarEstoqueDto = TestDataBuilder.umAtualizarEstoqueRequestDto().comProdutoId(1L)
                .comQuantidade(2).build();

        this.estoqueResponseDto =
                TestDataBuilder.umEstoqueResponseDto().comId(1L).comNome("Produto Teste")
                        .comEstoque(10).comPreco(BigDecimal.valueOf(50.00)).build();

        this.listaItens = Arrays.asList(this.atualizarEstoqueDto, TestDataBuilder
                .umAtualizarEstoqueRequestDto().comProdutoId(2L).comQuantidade(1).build());
    }

    @Nested
    @DisplayName("Validação de Estoque")
    class ValidacaoEstoque {

        @Test
        @DisplayName("Deve validar estoque com sucesso quando há estoque suficiente")
        void deveValidarEstoqueComSucessoQuandoHaEstoqueSuficiente() {
            // Arrange
            Mockito.when(
                    EstoqueServiceTest.this.estoquePort.validarEstoque(ArgumentMatchers.anyList()))
                    .thenReturn(true);

            // Act & Assert
            Assertions
                    .assertThatCode(() -> EstoqueServiceTest.this.estoqueService
                            .validarEstoque(EstoqueServiceTest.this.listaItens))
                    .doesNotThrowAnyException();

            // Verify
            Mockito.verify(EstoqueServiceTest.this.estoquePort, Mockito.times(1))
                    .validarEstoque(EstoqueServiceTest.this.listaItens);
        }

        @Test
        @DisplayName("Deve lançar exceção quando estoque é insuficiente")
        void deveLancarExcecaoQuandoEstoqueInsuficiente() {
            // Arrange
            Mockito.when(
                    EstoqueServiceTest.this.estoquePort.validarEstoque(ArgumentMatchers.anyList()))
                    .thenReturn(false);

            // Act & Assert
            Assertions
                    .assertThatThrownBy(() -> EstoqueServiceTest.this.estoqueService
                            .validarEstoque(EstoqueServiceTest.this.listaItens))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Estoque insuficiente para os itens solicitados.");

            Mockito.verify(EstoqueServiceTest.this.estoquePort, Mockito.times(1))
                    .validarEstoque(EstoqueServiceTest.this.listaItens);
        }

        @Test
        @DisplayName("Deve chamar estoquePort com lista correta")
        void deveChamarEstoquePortComListaCorreta() {
            // Arrange
            Mockito.when(
                    EstoqueServiceTest.this.estoquePort.validarEstoque(ArgumentMatchers.anyList()))
                    .thenReturn(true);

            // Act
            EstoqueServiceTest.this.estoqueService
                    .validarEstoque(EstoqueServiceTest.this.listaItens);

            // Assert
            Mockito.verify(EstoqueServiceTest.this.estoquePort)
                    .validarEstoque(EstoqueServiceTest.this.listaItens);
        }

        @Test
        @DisplayName("Deve lidar com lista vazia")
        void deveLidarComListaVazia() {
            // Arrange
            List<AtualizarEstoqueRequestDto> listaVazia = Collections.emptyList();
            Mockito.when(EstoqueServiceTest.this.estoquePort.validarEstoque(listaVazia))
                    .thenReturn(true);

            // Act & Assert
            Assertions
                    .assertThatCode(
                            () -> EstoqueServiceTest.this.estoqueService.validarEstoque(listaVazia))
                    .doesNotThrowAnyException();
        }

        @Test
        @DisplayName("Deve lançar exceção quando lista é nula")
        void deveLancarExcecaoQuandoListaNula() {
            // Arrange
            Mockito.when(EstoqueServiceTest.this.estoquePort.validarEstoque(null))
                    .thenReturn(false);

            // Act & Assert
            Assertions
                    .assertThatThrownBy(
                            () -> EstoqueServiceTest.this.estoqueService.validarEstoque(null))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Estoque insuficiente para os itens solicitados.");
        }
    }

    @Nested
    @DisplayName("Buscar Produto")
    class BuscarProduto {

        @Test
        @DisplayName("Deve buscar produto com sucesso quando produto existe")
        void deveBuscarProdutoComSucessoQuandoProdutoExiste() {
            // Arrange
            Long produtoId = 1L;
            Mockito.when(EstoqueServiceTest.this.estoquePort.buscarProduto(produtoId))
                    .thenReturn(EstoqueServiceTest.this.estoqueResponseDto);

            // Act
            EstoqueResponseDto resultado =
                    EstoqueServiceTest.this.estoqueService.buscarProduto(produtoId);

            // Assert
            Assertions.assertThat(resultado).isNotNull();
            Assertions.assertThat(resultado.getId()).isEqualTo(1L);
            Assertions.assertThat(resultado.getNome()).isEqualTo("Produto Teste");
            Assertions.assertThat(resultado.getEstoque()).isEqualTo(10);

            Mockito.verify(EstoqueServiceTest.this.estoquePort).buscarProduto(produtoId);
        }

        @Test
        @DisplayName("Deve lançar ResponseStatusException quando produto não existe")
        void deveLancarResponseStatusExceptionQuandoProdutoNaoExiste() {
            // Arrange
            Long produtoId = 999L;
            Mockito.when(EstoqueServiceTest.this.estoquePort.buscarProduto(produtoId))
                    .thenReturn(null);

            // Act & Assert
            Assertions
                    .assertThatThrownBy(
                            () -> EstoqueServiceTest.this.estoqueService.buscarProduto(produtoId))
                    .isInstanceOf(ResponseStatusException.class).satisfies(ex -> {
                        ResponseStatusException responseEx = (ResponseStatusException) ex;
                        Assertions.assertThat(responseEx.getStatusCode())
                                .isEqualTo(HttpStatus.NOT_FOUND);
                        Assertions.assertThat(responseEx.getReason())
                                .isEqualTo("Produto não encontrado");
                    });

            Mockito.verify(EstoqueServiceTest.this.estoquePort).buscarProduto(produtoId);
        }

        @Test
        @DisplayName("Deve retornar produto com todos os dados corretos")
        void deveRetornarProdutoComTodosOsDadosCorretos() {
            // Arrange
            EstoqueResponseDto produtoCompleto = TestDataBuilder.umEstoqueResponseDto().comId(123L)
                    .comNome("Produto Completo").comEstoque(50).comPreco(BigDecimal.valueOf(99.99))
                    .comDescricao("Descrição do produto").comCategoria("Categoria Teste").build();

            Mockito.when(EstoqueServiceTest.this.estoquePort.buscarProduto(123L))
                    .thenReturn(produtoCompleto);

            // Act
            EstoqueResponseDto resultado =
                    EstoqueServiceTest.this.estoqueService.buscarProduto(123L);

            // Assert
            Assertions.assertThat(resultado.getId()).isEqualTo(123L);
            Assertions.assertThat(resultado.getNome()).isEqualTo("Produto Completo");
            Assertions.assertThat(resultado.getEstoque()).isEqualTo(50);
            Assertions.assertThat(resultado.getPreco()).isEqualTo(BigDecimal.valueOf(99.99));
            Assertions.assertThat(resultado.getDescricao()).isEqualTo("Descrição do produto");
            Assertions.assertThat(resultado.getCategoria()).isEqualTo("Categoria Teste");
        }

        @Test
        @DisplayName("Deve propagar exceção do estoquePort")
        void devePropagarExcecaoDoEstoquePort() {
            // Arrange
            Long produtoId = 1L;
            RuntimeException excecaoSimulada = new RuntimeException("Erro no sistema");
            Mockito.when(EstoqueServiceTest.this.estoquePort.buscarProduto(produtoId))
                    .thenThrow(excecaoSimulada);

            // Act & Assert
            Assertions
                    .assertThatThrownBy(
                            () -> EstoqueServiceTest.this.estoqueService.buscarProduto(produtoId))
                    .isInstanceOf(RuntimeException.class).hasMessage("Erro no sistema");
        }

        @Test
        @DisplayName("Deve executar Assert.isTrue quando primeira validação falha")
        void deveExecutarAssertIsTrueQuandoPrimeiraValidacaoFalha() {
            Mockito.when(EstoqueServiceTest.this.estoquePort
                    .validarEstoque(EstoqueServiceTest.this.listaItens)).thenReturn(false)
                    .thenReturn(false);

            // Act & Assert
            Assertions
                    .assertThatThrownBy(() -> EstoqueServiceTest.this.estoqueService
                            .validarEstoque(EstoqueServiceTest.this.listaItens))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Estoque insuficiente para os itens solicitados.");

            Mockito.verify(EstoqueServiceTest.this.estoquePort, Mockito.times(1))
                    .validarEstoque(EstoqueServiceTest.this.listaItens);
        }
    }

    @Nested
    @DisplayName("Atualizar Estoque")
    class AtualizarEstoque {

        @Test
        @DisplayName("Deve atualizar estoque com sucesso quando há estoque suficiente")
        void deveAtualizarEstoqueComSucessoQuandoHaEstoqueSuficiente() {
            // Arrange
            AtualizarEstoqueRequestDto dtoAtualizado = TestDataBuilder
                    .umAtualizarEstoqueRequestDto().comProdutoId(1L).comQuantidade(2).build();

            Mockito.when(EstoqueServiceTest.this.estoquePort.buscarProduto(1L))
                    .thenReturn(EstoqueServiceTest.this.estoqueResponseDto);
            Mockito.when(EstoqueServiceTest.this.estoquePort
                    .atualizarEstoque(ArgumentMatchers.any(AtualizarEstoqueRequestDto.class)))
                    .thenReturn(dtoAtualizado);

            // Act
            AtualizarEstoqueRequestDto resultado = EstoqueServiceTest.this.estoqueService
                    .atualizarEstoque(EstoqueServiceTest.this.atualizarEstoqueDto);

            // Assert
            Assertions.assertThat(resultado).isNotNull();
            Assertions.assertThat(resultado.getProdutoId()).isEqualTo(1L);
            Assertions.assertThat(resultado.getQuantidade()).isEqualTo(2);

            Mockito.verify(EstoqueServiceTest.this.estoquePort).buscarProduto(1L);
            Mockito.verify(EstoqueServiceTest.this.estoquePort)
                    .atualizarEstoque(EstoqueServiceTest.this.atualizarEstoqueDto);
        }

        @Test
        @DisplayName("Deve lançar ResponseStatusException quando estoque insuficiente")
        void deveLancarResponseStatusExceptionQuandoEstoqueInsuficiente() {
            // Arrange
            EstoqueResponseDto produtoComEstoqueBaixo = TestDataBuilder.umEstoqueResponseDto()
                    .comId(1L).comNome("Produto Com Estoque Baixo").comEstoque(1).build();

            Mockito.when(EstoqueServiceTest.this.estoquePort.buscarProduto(1L))
                    .thenReturn(produtoComEstoqueBaixo);

            // Act & Assert
            Assertions
                    .assertThatThrownBy(() -> EstoqueServiceTest.this.estoqueService
                            .atualizarEstoque(EstoqueServiceTest.this.atualizarEstoqueDto))
                    .isInstanceOf(ResponseStatusException.class).satisfies(ex -> {
                        ResponseStatusException responseEx = (ResponseStatusException) ex;
                        Assertions.assertThat(responseEx.getStatusCode())
                                .isEqualTo(HttpStatus.BAD_REQUEST);
                        Assertions.assertThat(responseEx.getReason()).isEqualTo(
                                "Estoque insuficiente para o produto: Produto Com Estoque Baixo");
                    });

            Mockito.verify(EstoqueServiceTest.this.estoquePort, Mockito.never())
                    .atualizarEstoque(ArgumentMatchers.any(AtualizarEstoqueRequestDto.class));
        }

        @Test
        @DisplayName("Deve lançar exceção quando produto não existe")
        void deveLancarExcecaoQuandoProdutoNaoExiste() {
            // Arrange
            Mockito.when(EstoqueServiceTest.this.estoquePort.buscarProduto(1L)).thenReturn(null);

            // Act & Assert
            Assertions
                    .assertThatThrownBy(() -> EstoqueServiceTest.this.estoqueService
                            .atualizarEstoque(EstoqueServiceTest.this.atualizarEstoqueDto))
                    .isInstanceOf(ResponseStatusException.class).satisfies(ex -> {
                        ResponseStatusException responseEx = (ResponseStatusException) ex;
                        Assertions.assertThat(responseEx.getStatusCode())
                                .isEqualTo(HttpStatus.NOT_FOUND);
                        Assertions.assertThat(responseEx.getReason())
                                .isEqualTo("Produto não encontrado");
                    });
        }

        @Test
        @DisplayName("Deve funcionar com estoque exato")
        void deveFuncionarComEstoqueExato() {
            // Arrange
            EstoqueResponseDto produtoEstoqueExato = TestDataBuilder.umEstoqueResponseDto()
                    .comId(1L).comNome("Produto Estoque Exato").comEstoque(2).build();

            AtualizarEstoqueRequestDto dtoRetorno = TestDataBuilder.umAtualizarEstoqueRequestDto()
                    .comProdutoId(1L).comQuantidade(2).build();

            Mockito.when(EstoqueServiceTest.this.estoquePort.buscarProduto(1L))
                    .thenReturn(produtoEstoqueExato);
            Mockito.when(EstoqueServiceTest.this.estoquePort
                    .atualizarEstoque(ArgumentMatchers.any(AtualizarEstoqueRequestDto.class)))
                    .thenReturn(dtoRetorno);

            // Act
            AtualizarEstoqueRequestDto resultado = EstoqueServiceTest.this.estoqueService
                    .atualizarEstoque(EstoqueServiceTest.this.atualizarEstoqueDto);

            // Assert
            Assertions.assertThat(resultado).isNotNull();
            Mockito.verify(EstoqueServiceTest.this.estoquePort)
                    .atualizarEstoque(EstoqueServiceTest.this.atualizarEstoqueDto);
        }

        @Test
        @DisplayName("Deve atualizar com quantidade zero")
        void deveAtualizarComQuantidadeZero() {
            // Arrange
            AtualizarEstoqueRequestDto dtoQuantidadeZero = TestDataBuilder
                    .umAtualizarEstoqueRequestDto().comProdutoId(1L).comQuantidade(0).build();

            AtualizarEstoqueRequestDto dtoRetorno = TestDataBuilder.umAtualizarEstoqueRequestDto()
                    .comProdutoId(1L).comQuantidade(0).build();

            Mockito.when(EstoqueServiceTest.this.estoquePort.buscarProduto(1L))
                    .thenReturn(EstoqueServiceTest.this.estoqueResponseDto);
            Mockito.when(EstoqueServiceTest.this.estoquePort.atualizarEstoque(dtoQuantidadeZero))
                    .thenReturn(dtoRetorno);

            // Act
            AtualizarEstoqueRequestDto resultado =
                    EstoqueServiceTest.this.estoqueService.atualizarEstoque(dtoQuantidadeZero);

            // Assert
            Assertions.assertThat(resultado.getQuantidade()).isZero();
            Mockito.verify(EstoqueServiceTest.this.estoquePort).atualizarEstoque(dtoQuantidadeZero);
        }
    }

    @Nested
    @DisplayName("Testes de Integração")
    class TestesIntegracao {

        @Test
        @DisplayName("Deve executar fluxo completo de busca e atualização")
        void deveExecutarFluxoCompletoDeBuscaEAtualizacao() {
            // Arrange
            AtualizarEstoqueRequestDto dtoRetorno = TestDataBuilder.umAtualizarEstoqueRequestDto()
                    .comProdutoId(1L).comQuantidade(2).build();

            Mockito.when(EstoqueServiceTest.this.estoquePort.buscarProduto(1L))
                    .thenReturn(EstoqueServiceTest.this.estoqueResponseDto);
            Mockito.when(EstoqueServiceTest.this.estoquePort
                    .atualizarEstoque(EstoqueServiceTest.this.atualizarEstoqueDto))
                    .thenReturn(dtoRetorno);

            // Act
            AtualizarEstoqueRequestDto resultado = EstoqueServiceTest.this.estoqueService
                    .atualizarEstoque(EstoqueServiceTest.this.atualizarEstoqueDto);

            // Assert
            Assertions.assertThat(resultado).isNotNull();

            // Verify ordem de execução
            Mockito.inOrder(EstoqueServiceTest.this.estoquePort)
                    .verify(EstoqueServiceTest.this.estoquePort).buscarProduto(1L);
            Mockito.inOrder(EstoqueServiceTest.this.estoquePort)
                    .verify(EstoqueServiceTest.this.estoquePort)
                    .atualizarEstoque(EstoqueServiceTest.this.atualizarEstoqueDto);
        }

        @Test
        @DisplayName("Deve validar múltiplos itens e processar individualmente")
        void deveValidarMultiplosItensEProcessarIndividualmente() {
            // Arrange
            Mockito.when(EstoqueServiceTest.this.estoquePort
                    .validarEstoque(EstoqueServiceTest.this.listaItens)).thenReturn(true);

            // Act - Validar primeiro
            EstoqueServiceTest.this.estoqueService
                    .validarEstoque(EstoqueServiceTest.this.listaItens);

            // Act - Depois processar cada item
            for (AtualizarEstoqueRequestDto item : EstoqueServiceTest.this.listaItens) {
                EstoqueResponseDto produto = TestDataBuilder.umEstoqueResponseDto()
                        .comId(item.getProdutoId()).comEstoque(10).build();

                Mockito.when(EstoqueServiceTest.this.estoquePort.buscarProduto(item.getProdutoId()))
                        .thenReturn(produto);
                Mockito.when(EstoqueServiceTest.this.estoquePort.atualizarEstoque(item))
                        .thenReturn(item);

                AtualizarEstoqueRequestDto resultado =
                        EstoqueServiceTest.this.estoqueService.atualizarEstoque(item);

                Assertions.assertThat(resultado).isNotNull();
            }

            // Verify
            Mockito.verify(EstoqueServiceTest.this.estoquePort)
                    .validarEstoque(EstoqueServiceTest.this.listaItens);
            Mockito.verify(EstoqueServiceTest.this.estoquePort, Mockito.times(2))
                    .buscarProduto(ArgumentMatchers.anyLong());
            Mockito.verify(EstoqueServiceTest.this.estoquePort, Mockito.times(2))
                    .atualizarEstoque(ArgumentMatchers.any(AtualizarEstoqueRequestDto.class));
        }
    }

    @Nested
    @DisplayName("Validações de Dependências")
    class ValidacoesDependencias {

        @Test
        @DisplayName("Deve ter EstoquePort injetado")
        void deveTerEstoquePortInjetado() {
            Assertions.assertThat(EstoqueServiceTest.this.estoqueService).isNotNull();
        }

        @Test
        @DisplayName("Deve usar EstoquePort para todas as operações")
        void deveUsarEstoquePortParaTodasAsOperacoes() {
            // Arrange
            Mockito.when(
                    EstoqueServiceTest.this.estoquePort.validarEstoque(ArgumentMatchers.anyList()))
                    .thenReturn(true);
            Mockito.when(
                    EstoqueServiceTest.this.estoquePort.buscarProduto(ArgumentMatchers.anyLong()))
                    .thenReturn(EstoqueServiceTest.this.estoqueResponseDto);
            Mockito.when(EstoqueServiceTest.this.estoquePort
                    .atualizarEstoque(ArgumentMatchers.any(AtualizarEstoqueRequestDto.class)))
                    .thenReturn(EstoqueServiceTest.this.atualizarEstoqueDto);

            // Act
            EstoqueServiceTest.this.estoqueService
                    .validarEstoque(EstoqueServiceTest.this.listaItens);
            EstoqueServiceTest.this.estoqueService.buscarProduto(1L);
            EstoqueServiceTest.this.estoqueService
                    .atualizarEstoque(EstoqueServiceTest.this.atualizarEstoqueDto);

            // Assert
            Mockito.verify(EstoqueServiceTest.this.estoquePort)
                    .validarEstoque(ArgumentMatchers.anyList());
            Mockito.verify(EstoqueServiceTest.this.estoquePort, Mockito.times(2))
                    .buscarProduto(ArgumentMatchers.anyLong());
            Mockito.verify(EstoqueServiceTest.this.estoquePort)
                    .atualizarEstoque(ArgumentMatchers.any(AtualizarEstoqueRequestDto.class));
        }
    }
}
