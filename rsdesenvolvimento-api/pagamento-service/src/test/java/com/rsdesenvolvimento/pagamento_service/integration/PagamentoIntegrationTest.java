package com.rsdesenvolvimento.pagamento_service.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rsdesenvolvimento.pagamento_service.config.EstoqueFeignClient;
import com.rsdesenvolvimento.pagamento_service.config.NotificacaoProducer;
import com.rsdesenvolvimento.pagamento_service.config.PedidoClient;
import com.rsdesenvolvimento.pagamento_service.modelo.dtos.PagamentoRequestDto;
import com.rsdesenvolvimento.pagamento_service.modelo.dtos.ReservaEstoqueRequestDto;
import com.rsdesenvolvimento.pagamento_service.modelo.entidades.Pagamento;
import com.rsdesenvolvimento.pagamento_service.repositorios.PagamentoRepository;
import java.math.BigDecimal;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureWebMvc
@ActiveProfiles("test")
@Transactional
@DisplayName("Testes de Integração - Fluxo Completo de Pagamento")
class PagamentoIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PagamentoRepository pagamentoRepository;

    @MockitoBean
    private PedidoClient pedidoClient;

    @MockitoBean
    private EstoqueFeignClient estoqueFeignClient;

    @MockitoBean
    private NotificacaoProducer notificacaoProducer;

    private PagamentoRequestDto pagamentoRequestDto;

    @BeforeEach
    void setUp() {
        List<ReservaEstoqueRequestDto> itens =
                List.of(new ReservaEstoqueRequestDto(1L, 2), new ReservaEstoqueRequestDto(2L, 3));

        this.pagamentoRequestDto = new PagamentoRequestDto();
        this.pagamentoRequestDto.setPedidoId(100L);
        this.pagamentoRequestDto.setValor(new BigDecimal("299.99"));
        this.pagamentoRequestDto.setItens(itens);

        // Configurar mocks
        Mockito.when(this.pedidoClient.buscarEmailPorPedidoId(100L))
                .thenReturn("usuario.teste@email.com");
        Mockito.doNothing().when(this.pedidoClient).atualizarStatus(100L, "FINALIZADO");
        Mockito.doNothing().when(this.estoqueFeignClient).reservarEstoque(ArgumentMatchers.any());
        Mockito.doNothing().when(this.notificacaoProducer)
                .enviarNotificacao(ArgumentMatchers.anyString());
        Mockito.doNothing().when(this.notificacaoProducer).enviarNotificacaoComDelay(
                ArgumentMatchers.anyString(), ArgumentMatchers.anyLong());
    }

    @Test
    @DisplayName("Deve processar pagamento completo com sucesso - End to End")
    void deveProcessarPagamentoCompletoComSucessoEndToEnd() throws Exception {
        // Given
        String requestBody = this.objectMapper.writeValueAsString(this.pagamentoRequestDto);

        // When & Then
        this.mockMvc
                .perform(MockMvcRequestBuilders.put("/pagamentos")
                        .contentType(MediaType.APPLICATION_JSON).content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.pedidoId").value(100L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.valor").value(299.99))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("FINALIZADO"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.dataHora").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists());

        // Verificar se o pagamento foi salvo no banco
        List<Pagamento> pagamentos = this.pagamentoRepository.findAll();
        Assertions.assertThat(pagamentos).hasSize(1);

        Pagamento pagamentoSalvo = pagamentos.get(0);
        Assertions.assertThat(pagamentoSalvo.getPedidoId()).isEqualTo(100L);
        Assertions.assertThat(pagamentoSalvo.getValor()).isEqualTo(new BigDecimal("299.99"));
        Assertions.assertThat(pagamentoSalvo.getStatus()).isEqualTo("FINALIZADO");
        Assertions.assertThat(pagamentoSalvo.getDataHora()).isNotNull();

        // Verificar interações com serviços externos
        Mockito.verify(this.pedidoClient, Mockito.times(1)).atualizarStatus(100L, "FINALIZADO");
        Mockito.verify(this.estoqueFeignClient, Mockito.times(1))
                .reservarEstoque(this.pagamentoRequestDto.getItens());
        Mockito.verify(this.pedidoClient, Mockito.times(1)).buscarEmailPorPedidoId(100L);
        Mockito.verify(this.notificacaoProducer, Mockito.times(1))
                .enviarNotificacao("Pagamento do pedido 100 realizado com sucesso!");
        Mockito.verify(this.notificacaoProducer, Mockito.times(1)).enviarNotificacaoComDelay(
                "EMAIL_PAGAMENTO_CONFIRMADO:100:usuario.teste@email.com", 10000L);
    }

    @Test
    @DisplayName("Deve falhar quando serviço de pedido não está disponível")
    void deveFalharQuandoServicoDePedidoNaoEstaDisponivel() throws Exception {
        // Given
        Mockito.doThrow(new RuntimeException("Serviço indisponível")).when(this.pedidoClient)
                .atualizarStatus(100L, "FINALIZADO");

        String requestBody = this.objectMapper.writeValueAsString(this.pagamentoRequestDto);

        // When & Then
        this.mockMvc
                .perform(MockMvcRequestBuilders.put("/pagamentos")
                        .contentType(MediaType.APPLICATION_JSON).content(requestBody))
                .andExpect(MockMvcResultMatchers.status().is5xxServerError());

        // Verificar que o pagamento não foi salvo devido ao rollback
        List<Pagamento> pagamentos = this.pagamentoRepository.findAll();
        Assertions.assertThat(pagamentos).isEmpty();
    }

    @Test
    @DisplayName("Deve processar múltiplos pagamentos sequencialmente")
    void deveProcessarMultiplosPagamentosSequencialmente() throws Exception {
        // Given
        PagamentoRequestDto pagamento1 = new PagamentoRequestDto();
        pagamento1.setPedidoId(200L);
        pagamento1.setValor(new BigDecimal("150.00"));
        pagamento1.setItens(List.of(new ReservaEstoqueRequestDto(3L, 1)));

        PagamentoRequestDto pagamento2 = new PagamentoRequestDto();
        pagamento2.setPedidoId(201L);
        pagamento2.setValor(new BigDecimal("200.00"));
        pagamento2.setItens(List.of(new ReservaEstoqueRequestDto(4L, 2)));

        Mockito.when(this.pedidoClient.buscarEmailPorPedidoId(200L)).thenReturn("user1@email.com");
        Mockito.when(this.pedidoClient.buscarEmailPorPedidoId(201L)).thenReturn("user2@email.com");

        // When - Processar primeiro pagamento
        this.mockMvc
                .perform(MockMvcRequestBuilders.put("/pagamentos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(pagamento1)))
                .andExpect(MockMvcResultMatchers.status().isOk());

        // When - Processar segundo pagamento
        this.mockMvc
                .perform(MockMvcRequestBuilders.put("/pagamentos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(pagamento2)))
                .andExpect(MockMvcResultMatchers.status().isOk());

        // Then
        List<Pagamento> pagamentos = this.pagamentoRepository.findAll();
        Assertions.assertThat(pagamentos).hasSize(2);
        Assertions.assertThat(pagamentos).extracting(Pagamento::getPedidoId)
                .containsExactlyInAnyOrder(200L, 201L);
    }

    @Test
    @DisplayName("Deve manter consistência de dados quando falha parcialmente")
    void deveManterConsistenciaDeDadosQuandoFalhaParcialmente() throws Exception {
        // Given
        Mockito.doThrow(new RuntimeException("Falha no estoque")).when(this.estoqueFeignClient)
                .reservarEstoque(ArgumentMatchers.any());

        String requestBody = this.objectMapper.writeValueAsString(this.pagamentoRequestDto);

        // When & Then
        this.mockMvc
                .perform(MockMvcRequestBuilders.put("/pagamentos")
                        .contentType(MediaType.APPLICATION_JSON).content(requestBody))
                .andExpect(MockMvcResultMatchers.status().is5xxServerError());

        // Verificar que não há pagamentos salvos (rollback)
        List<Pagamento> pagamentos = this.pagamentoRepository.findAll();
        Assertions.assertThat(pagamentos).isEmpty();
    }

    @Test
    @DisplayName("Deve processar pagamento com valor grande corretamente")
    void deveProcessarPagamentoComValorGrandeCorretamente() throws Exception {
        // Given
        this.pagamentoRequestDto.setValor(new BigDecimal("999999.99"));
        String requestBody = this.objectMapper.writeValueAsString(this.pagamentoRequestDto);

        // When & Then
        this.mockMvc
                .perform(MockMvcRequestBuilders.put("/pagamentos")
                        .contentType(MediaType.APPLICATION_JSON).content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.valor").value(999999.99));

        List<Pagamento> pagamentos = this.pagamentoRepository.findAll();
        Assertions.assertThat(pagamentos.get(0).getValor()).isEqualTo(new BigDecimal("999999.99"));
    }

    @Test
    @DisplayName("Deve processar pagamento mesmo quando notificação falha")
    void deveProcessarPagamentoMesmoQuandoNotificacaoFalha() throws Exception {
        // Given
        Mockito.doThrow(new RuntimeException("Falha na notificação")).when(this.notificacaoProducer)
                .enviarNotificacao(ArgumentMatchers.anyString());

        String requestBody = this.objectMapper.writeValueAsString(this.pagamentoRequestDto);

        // When & Then
        this.mockMvc
                .perform(MockMvcRequestBuilders.put("/pagamentos")
                        .contentType(MediaType.APPLICATION_JSON).content(requestBody))
                .andExpect(MockMvcResultMatchers.status().is5xxServerError());

        // Verificar rollback
        List<Pagamento> pagamentos = this.pagamentoRepository.findAll();
        Assertions.assertThat(pagamentos).isEmpty();
    }
}
