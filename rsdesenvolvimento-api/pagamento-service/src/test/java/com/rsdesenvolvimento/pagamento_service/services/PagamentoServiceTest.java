package com.rsdesenvolvimento.pagamento_service.services;

import com.rsdesenvolvimento.pagamento_service.config.EstoqueFeignClient;
import com.rsdesenvolvimento.pagamento_service.config.NotificacaoProducer;
import com.rsdesenvolvimento.pagamento_service.config.PedidoClient;
import com.rsdesenvolvimento.pagamento_service.modelo.dtos.PagamentoRequestDto;
import com.rsdesenvolvimento.pagamento_service.modelo.dtos.ReservaEstoqueRequestDto;
import com.rsdesenvolvimento.pagamento_service.modelo.entidades.Pagamento;
import com.rsdesenvolvimento.pagamento_service.repositorios.PagamentoRepository;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes para PagamentoService")
class PagamentoServiceTest {

    @Mock
    private PagamentoRepository repository;

    @Mock
    private PedidoClient pedidoClient;

    @Mock
    private NotificacaoProducer notificacaoProducer;

    @Mock
    private EstoqueFeignClient estoqueFeignClient;

    @InjectMocks
    private PagamentoService pagamentoService;

    private PagamentoRequestDto pagamentoRequestDto;
    private Pagamento pagamentoEsperado;
    private List<ReservaEstoqueRequestDto> itens;

    @BeforeEach
    void setUp() {
        this.itens =
                List.of(new ReservaEstoqueRequestDto(1L, 2), new ReservaEstoqueRequestDto(2L, 1));

        this.pagamentoRequestDto = new PagamentoRequestDto();
        this.pagamentoRequestDto.setPedidoId(1L);
        this.pagamentoRequestDto.setValor(new BigDecimal("100.00"));
        this.pagamentoRequestDto.setItens(this.itens);

        this.pagamentoEsperado = Pagamento.builder().pedidoId(1L).valor(new BigDecimal("100.00"))
                .dataHora(LocalDateTime.now()).status("FINALIZADO").build();
    }

    @Test
    @DisplayName("Deve processar pagamento com sucesso")
    void deveProcessarPagamentoComSucesso() {
        // Given
        Mockito.when(this.repository.save(ArgumentMatchers.any(Pagamento.class)))
                .thenReturn(this.pagamentoEsperado);
        Mockito.when(this.pedidoClient.buscarEmailPorPedidoId(1L)).thenReturn("usuario@email.com");

        // When
        Pagamento resultado = this.pagamentoService.processarPagamento(this.pagamentoRequestDto);

        // Then
        Assertions.assertThat(resultado).isNotNull();
        Assertions.assertThat(resultado.getPedidoId()).isEqualTo(1L);
        Assertions.assertThat(resultado.getValor()).isEqualTo(new BigDecimal("100.00"));
        Assertions.assertThat(resultado.getStatus()).isEqualTo("FINALIZADO");
        Assertions.assertThat(resultado.getDataHora()).isNotNull();

        // Verificar interações
        Mockito.verify(this.repository, Mockito.times(1))
                .save(ArgumentMatchers.any(Pagamento.class));
        Mockito.verify(this.pedidoClient, Mockito.times(1)).atualizarStatus(1L, "FINALIZADO");
        Mockito.verify(this.estoqueFeignClient, Mockito.times(1)).reservarEstoque(this.itens);
        Mockito.verify(this.notificacaoProducer, Mockito.times(1))
                .enviarNotificacao("Pagamento do pedido 1 realizado com sucesso!");
        Mockito.verify(this.notificacaoProducer, Mockito.times(1))
                .enviarNotificacaoComDelay("EMAIL_PAGAMENTO_CONFIRMADO:1:usuario@email.com", 10000);
    }

    @Test
    @DisplayName("Deve salvar pagamento com dados corretos")
    void deveSalvarPagamentoComDadosCorretos() {
        // Given
        ArgumentCaptor<Pagamento> pagamentoCaptor = ArgumentCaptor.forClass(Pagamento.class);
        Mockito.when(this.repository.save(ArgumentMatchers.any(Pagamento.class)))
                .thenReturn(this.pagamentoEsperado);
        Mockito.when(this.pedidoClient.buscarEmailPorPedidoId(1L)).thenReturn("usuario@email.com");

        // When
        this.pagamentoService.processarPagamento(this.pagamentoRequestDto);

        // Then
        Mockito.verify(this.repository).save(pagamentoCaptor.capture());
        Pagamento pagamentoSalvo = pagamentoCaptor.getValue();

        Assertions.assertThat(pagamentoSalvo.getPedidoId()).isEqualTo(1L);
        Assertions.assertThat(pagamentoSalvo.getValor()).isEqualTo(new BigDecimal("100.00"));
        Assertions.assertThat(pagamentoSalvo.getStatus()).isEqualTo("FINALIZADO");
        Assertions.assertThat(pagamentoSalvo.getDataHora()).isNotNull();
        Assertions.assertThat(pagamentoSalvo.getDataHora()).isBeforeOrEqualTo(LocalDateTime.now());
    }

    @Test
    @DisplayName("Deve atualizar status do pedido para FINALIZADO")
    void deveAtualizarStatusDoPedidoParaFinalizado() {
        // Given
        Mockito.when(this.repository.save(ArgumentMatchers.any(Pagamento.class)))
                .thenReturn(this.pagamentoEsperado);
        Mockito.when(this.pedidoClient.buscarEmailPorPedidoId(1L)).thenReturn("usuario@email.com");

        // When
        this.pagamentoService.processarPagamento(this.pagamentoRequestDto);

        // Then
        Mockito.verify(this.pedidoClient, Mockito.times(1)).atualizarStatus(1L, "FINALIZADO");
    }

    @Test
    @DisplayName("Deve reservar estoque com os itens corretos")
    void deveReservarEstoqueComItensCorretos() {
        // Given
        Mockito.when(this.repository.save(ArgumentMatchers.any(Pagamento.class)))
                .thenReturn(this.pagamentoEsperado);
        Mockito.when(this.pedidoClient.buscarEmailPorPedidoId(1L)).thenReturn("usuario@email.com");

        // When
        this.pagamentoService.processarPagamento(this.pagamentoRequestDto);

        // Then
        Mockito.verify(this.estoqueFeignClient, Mockito.times(1)).reservarEstoque(this.itens);
    }

    @Test
    @DisplayName("Deve enviar notificação imediata de pagamento realizado")
    void deveEnviarNotificacaoImediataDePagamentoRealizado() {
        // Given
        Mockito.when(this.repository.save(ArgumentMatchers.any(Pagamento.class)))
                .thenReturn(this.pagamentoEsperado);
        Mockito.when(this.pedidoClient.buscarEmailPorPedidoId(1L)).thenReturn("usuario@email.com");

        // When
        this.pagamentoService.processarPagamento(this.pagamentoRequestDto);

        // Then
        Mockito.verify(this.notificacaoProducer, Mockito.times(1))
                .enviarNotificacao("Pagamento do pedido 1 realizado com sucesso!");
    }

    @Test
    @DisplayName("Deve enviar notificação com delay contendo email do usuário")
    void deveEnviarNotificacaoComDelayContendoEmailDoUsuario() {
        // Given
        String emailUsuario = "usuario@email.com";
        Mockito.when(this.repository.save(ArgumentMatchers.any(Pagamento.class)))
                .thenReturn(this.pagamentoEsperado);
        Mockito.when(this.pedidoClient.buscarEmailPorPedidoId(1L)).thenReturn(emailUsuario);

        // When
        this.pagamentoService.processarPagamento(this.pagamentoRequestDto);

        // Then
        Mockito.verify(this.pedidoClient, Mockito.times(1)).buscarEmailPorPedidoId(1L);
        Mockito.verify(this.notificacaoProducer, Mockito.times(1))
                .enviarNotificacaoComDelay("EMAIL_PAGAMENTO_CONFIRMADO:1:" + emailUsuario, 10000);
    }

    @Test
    @DisplayName("Deve executar todas as operações na ordem correta")
    void deveExecutarTodasOperacoesNaOrdemCorreta() {
        // Given
        Mockito.when(this.repository.save(ArgumentMatchers.any(Pagamento.class)))
                .thenReturn(this.pagamentoEsperado);
        Mockito.when(this.pedidoClient.buscarEmailPorPedidoId(1L)).thenReturn("usuario@email.com");

        // When
        this.pagamentoService.processarPagamento(this.pagamentoRequestDto);

        // Then - Verificar ordem das chamadas
        var inOrder = Mockito.inOrder(this.repository, this.pedidoClient, this.estoqueFeignClient,
                this.notificacaoProducer);

        inOrder.verify(this.repository).save(ArgumentMatchers.any(Pagamento.class));
        inOrder.verify(this.pedidoClient).atualizarStatus(1L, "FINALIZADO");
        inOrder.verify(this.estoqueFeignClient).reservarEstoque(this.itens);
        inOrder.verify(this.notificacaoProducer).enviarNotificacao(ArgumentMatchers.anyString());
        inOrder.verify(this.pedidoClient).buscarEmailPorPedidoId(1L);
        inOrder.verify(this.notificacaoProducer).enviarNotificacaoComDelay(
                ArgumentMatchers.anyString(), ArgumentMatchers.eq(10000L));
    }
}
