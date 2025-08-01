package com.rsdesenvolvimento.pedido_service.services;

import com.rsdesenvolvimento.pedido_service.core.client.dtos.EstoqueResponseDto;
import com.rsdesenvolvimento.pedido_service.modelo.dtos.ItemPedidoRequestDto;
import com.rsdesenvolvimento.pedido_service.modelo.dtos.PedidoRequesteDto;
import com.rsdesenvolvimento.pedido_service.modelo.enums.StatusEnum;
import com.rsdesenvolvimento.pedido_service.services.exceptions.EstoqueInsuficienteException;
import com.rsdesenvolvimento.pedido_service.services.exceptions.PedidoValidationException;
import com.rsdesenvolvimento.pedido_service.services.exceptions.StatusInvalidoException;
import com.rsdesenvolvimento.pedido_service.utils.TestDataBuilder;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class PedidoValidacaoServiceTest {


    @Mock
    private EstoqueService estoqueService;

    @InjectMocks
    private PedidoValidacaoService pedidoValidacaoService;

    @Nested
    @DisplayName("Validação de Pedido")
    class ValidarPedidoTests {


        @Test
        void deveLancarExceptionCasoDtoNulo() {
            PedidoRequesteDto dto = null;
            Assertions.assertThrows(PedidoValidationException.class,
                    () -> PedidoValidacaoServiceTest.this.pedidoValidacaoService
                            .validarPedido(dto));
        }

        @Test
        void deveLancarExcecaoQuandoListaDeItensForNula() {

            PedidoRequesteDto dto = new PedidoRequesteDto();
            dto.setItensPedido(null);

            Assertions.assertThrows(PedidoValidationException.class, () -> {
                PedidoValidacaoServiceTest.this.pedidoValidacaoService.validarPedido(dto);
            });
        }

        @Test
        void deveLancarExceptionCasoListaItensEstejaNulaOuVazia() {
            PedidoRequesteDto dto = new PedidoRequesteDto();
            dto.setItensPedido(Collections.emptyList());

            Assertions.assertThrows(PedidoValidationException.class,
                    () -> PedidoValidacaoServiceTest.this.pedidoValidacaoService
                            .validarPedido(dto));
        }

        @Test
        void devePassarSemException_QuandoPedidoValido() {
            PedidoRequesteDto dto = TestDataBuilder.umPedidoRequesteDto().build();

            Assertions
                    .assertDoesNotThrow(() -> PedidoValidacaoServiceTest.this.pedidoValidacaoService
                            .validarPedido(dto));
        }

    }

    @Nested
    @DisplayName("Validação de Disponibilidade de Estoque")
    class ValidarEstoqueTest {
        @Test
        void deveLancarException_QuandoEstoqueInsuficiente() {
            ItemPedidoRequestDto item =
                    TestDataBuilder.umItemPedidoRequestDto().comQuantidade(10).build();

            EstoqueResponseDto estoqueResponseDto =
                    TestDataBuilder.umEstoqueResponseDto().comEstoque(5).build();

            Mockito.when(PedidoValidacaoServiceTest.this.estoqueService
                    .buscarProduto(item.getProdutoId())).thenReturn(estoqueResponseDto);

            var exception = Assertions.assertThrows(EstoqueInsuficienteException.class, () -> {
                PedidoValidacaoServiceTest.this.pedidoValidacaoService
                        .validarDisponibilidadeEstoque(List.of(item));
            });
            org.assertj.core.api.Assertions.assertThat(exception.getMessage()).isEqualTo(
                    "Estoque insuficiente para o produto 'Produto Teste'. Disponível: 5, Solicitado: 10");
        }

        @Test
        void devePassarSemException_QuandoEstoqueSuficiente() {
            ItemPedidoRequestDto item =
                    TestDataBuilder.umItemPedidoRequestDto().comQuantidade(10).build();

            EstoqueResponseDto estoqueResponseDto =
                    TestDataBuilder.umEstoqueResponseDto().comEstoque(15).build();
            Mockito.when(PedidoValidacaoServiceTest.this.estoqueService
                    .buscarProduto(item.getProdutoId())).thenReturn(estoqueResponseDto);

            Assertions.assertDoesNotThrow(() -> {
                PedidoValidacaoServiceTest.this.pedidoValidacaoService
                        .validarDisponibilidadeEstoque(List.of(item));
            });
            Mockito.verify(PedidoValidacaoServiceTest.this.estoqueService, Mockito.times(1))
                    .buscarProduto(item.getProdutoId());
        }

    }
    @Nested
    @DisplayName("Validação de Status")
    class ValidarStatus {
        @Test
        void deveLancarException_QuandoStatusInvalido() {

            Assertions.assertThrows(StatusInvalidoException.class,
                    () -> PedidoValidacaoServiceTest.this.pedidoValidacaoService
                            .validarEConverterStatus("STATUS_VALIDO"));
        }

        @Test
        void deveLancarException_QuandoStatusForNulo() {
            Assertions.assertThrows(StatusInvalidoException.class,
                    () -> PedidoValidacaoServiceTest.this.pedidoValidacaoService
                            .validarEConverterStatus(null));
        }

        @Test
        void deveConverterStatusCorretamente_IgnorandoCase() {
            org.assertj.core.api.Assertions
                    .assertThat(PedidoValidacaoServiceTest.this.pedidoValidacaoService
                            .validarEConverterStatus("pendente"));
            org.assertj.core.api.Assertions
                    .assertThat(PedidoValidacaoServiceTest.this.pedidoValidacaoService
                            .validarEConverterStatus("PENDENTE"))
                    .isEqualTo(StatusEnum.PENDENTE);
        }
    }

    @Nested
    @DisplayName("Validação de Itens do Pedido")
    class ValidarItens {
        @Test
        void deveLancarException_QuandoProdutoIdNulo() {
            ItemPedidoRequestDto item =
                    TestDataBuilder.umItemPedidoRequestDto().comProdutoId(null).build();
            PedidoRequesteDto dto = new PedidoRequesteDto();
            dto.setItensPedido(List.of(item));

            Assertions.assertThrows(PedidoValidationException.class,
                    () -> PedidoValidacaoServiceTest.this.pedidoValidacaoService
                            .validarPedido(dto));
        }

        @Test
        void deveLancarException_QuandoQuantidadeNulaOuMenorQueZero() {
            ItemPedidoRequestDto item =
                    TestDataBuilder.umItemPedidoRequestDto().comQuantidade(null).build();
            PedidoRequesteDto dto = new PedidoRequesteDto();
            dto.setItensPedido(List.of(item));

            Assertions.assertThrows(PedidoValidationException.class,
                    () -> PedidoValidacaoServiceTest.this.pedidoValidacaoService
                            .validarPedido(dto));
        }

        @Test
        void deveLancarException_QuandoPrecoNuloOuMenorQueZero() {
            ItemPedidoRequestDto item =
                    TestDataBuilder.umItemPedidoRequestDto().comPrecoUnitario(null).build();
            PedidoRequesteDto dto = new PedidoRequesteDto();
            dto.setItensPedido(List.of(item));

            Assertions.assertThrows(PedidoValidationException.class,
                    () -> PedidoValidacaoServiceTest.this.pedidoValidacaoService
                            .validarPedido(dto));
        }
    }
}
