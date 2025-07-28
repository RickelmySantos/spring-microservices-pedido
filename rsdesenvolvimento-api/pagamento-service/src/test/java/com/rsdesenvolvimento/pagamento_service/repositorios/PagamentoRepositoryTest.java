package com.rsdesenvolvimento.pagamento_service.repositorios;

import com.rsdesenvolvimento.pagamento_service.modelo.entidades.Pagamento;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles("test")
@DisplayName("Testes de Integração para PagamentoRepository")
class PagamentoRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private PagamentoRepository pagamentoRepository;

    private Pagamento pagamento1;
    private Pagamento pagamento2;

    @BeforeEach
    void setUp() {
        this.pagamento1 = Pagamento.builder().pedidoId(1L).valor(new BigDecimal("100.50"))
                .dataHora(LocalDateTime.of(2025, 7, 28, 10, 30)).status("FINALIZADO").build();

        this.pagamento2 = Pagamento.builder().pedidoId(2L).valor(new BigDecimal("250.75"))
                .dataHora(LocalDateTime.of(2025, 7, 28, 11, 45)).status("PENDENTE").build();
    }

    @Test
    @DisplayName("Deve salvar pagamento corretamente")
    void deveSalvarPagamentoCorretamente() {
        // When
        Pagamento pagamentoSalvo = this.pagamentoRepository.save(this.pagamento1);

        // Then
        Assertions.assertThat(pagamentoSalvo).isNotNull();
        Assertions.assertThat(pagamentoSalvo.getId()).isNotNull();
        Assertions.assertThat(pagamentoSalvo.getPedidoId()).isEqualTo(1L);
        Assertions.assertThat(pagamentoSalvo.getValor()).isEqualTo(new BigDecimal("100.50"));
        Assertions.assertThat(pagamentoSalvo.getDataHora())
                .isEqualTo(LocalDateTime.of(2025, 7, 28, 10, 30));
        Assertions.assertThat(pagamentoSalvo.getStatus()).isEqualTo("FINALIZADO");
    }

    @Test
    @DisplayName("Deve buscar pagamento por ID")
    void deveBuscarPagamentoPorId() {
        // Given
        Pagamento pagamentoSalvo = this.entityManager.persistAndFlush(this.pagamento1);

        // When
        Optional<Pagamento> pagamentoEncontrado =
                this.pagamentoRepository.findById(pagamentoSalvo.getId());

        // Then
        Assertions.assertThat(pagamentoEncontrado).isPresent();
        Assertions.assertThat(pagamentoEncontrado.get().getPedidoId()).isEqualTo(1L);
        Assertions.assertThat(pagamentoEncontrado.get().getValor())
                .isEqualTo(new BigDecimal("100.50"));
    }

    @Test
    @DisplayName("Deve retornar vazio ao buscar por ID inexistente")
    void deveRetornarVazioAoBuscarPorIdInexistente() {
        // When
        Optional<Pagamento> pagamento = this.pagamentoRepository.findById(999L);

        // Then
        Assertions.assertThat(pagamento).isEmpty();
    }

    @Test
    @DisplayName("Deve listar todos os pagamentos")
    void deveListarTodosOsPagamentos() {
        // Given
        this.entityManager.persistAndFlush(this.pagamento1);
        this.entityManager.persistAndFlush(this.pagamento2);

        // When
        List<Pagamento> pagamentos = this.pagamentoRepository.findAll();

        // Then
        Assertions.assertThat(pagamentos).hasSize(2);
        Assertions.assertThat(pagamentos).extracting(Pagamento::getPedidoId)
                .containsExactlyInAnyOrder(1L, 2L);
    }

    @Test
    @DisplayName("Deve retornar lista vazia quando não há pagamentos")
    void deveRetornarListaVaziaQuandoNaoHaPagamentos() {
        // When
        List<Pagamento> pagamentos = this.pagamentoRepository.findAll();

        // Then
        Assertions.assertThat(pagamentos).isEmpty();
    }

    @Test
    @DisplayName("Deve deletar pagamento corretamente")
    void deveDeletarPagamentoCorretamente() {
        // Given
        Pagamento pagamentoSalvo = this.entityManager.persistAndFlush(this.pagamento1);

        // When
        this.pagamentoRepository.deleteById(pagamentoSalvo.getId());

        // Then
        Optional<Pagamento> pagamentoDeletado =
                this.pagamentoRepository.findById(pagamentoSalvo.getId());
        Assertions.assertThat(pagamentoDeletado).isEmpty();
    }

    @Test
    @DisplayName("Deve atualizar pagamento existente")
    void deveAtualizarPagamentoExistente() {
        // Given
        Pagamento pagamentoSalvo = this.entityManager.persistAndFlush(this.pagamento1);

        // When
        pagamentoSalvo.setStatus("CANCELADO");
        pagamentoSalvo.setValor(new BigDecimal("200.00"));
        Pagamento pagamentoAtualizado = this.pagamentoRepository.save(pagamentoSalvo);

        // Then
        Assertions.assertThat(pagamentoAtualizado.getStatus()).isEqualTo("CANCELADO");
        Assertions.assertThat(pagamentoAtualizado.getValor()).isEqualTo(new BigDecimal("200.00"));
        Assertions.assertThat(pagamentoAtualizado.getId()).isEqualTo(pagamentoSalvo.getId());
    }

    @Test
    @DisplayName("Deve contar pagamentos corretamente")
    void deveContarPagamentosCorretamente() {
        // Given
        this.entityManager.persistAndFlush(this.pagamento1);
        this.entityManager.persistAndFlush(this.pagamento2);

        // When
        long count = this.pagamentoRepository.count();

        // Then
        Assertions.assertThat(count).isEqualTo(2);
    }

    @Test
    @DisplayName("Deve verificar existência de pagamento por ID")
    void deveVerificarExistenciaDePagamentoPorId() {
        // Given
        Pagamento pagamentoSalvo = this.entityManager.persistAndFlush(this.pagamento1);

        // When
        boolean existe = this.pagamentoRepository.existsById(pagamentoSalvo.getId());

        // Then
        Assertions.assertThat(existe).isTrue();
    }

    @Test
    @DisplayName("Deve retornar false para pagamento inexistente")
    void deveRetornarFalseParaPagamentoInexistente() {
        // When
        boolean existe = this.pagamentoRepository.existsById(999L);

        // Then
        Assertions.assertThat(existe).isFalse();
    }

    @Test
    @DisplayName("Deve persistir dados de auditoria automaticamente")
    void devePersistirDadosDeAuditoriaAutomaticamente() {
        // When
        Pagamento pagamentoSalvo = this.pagamentoRepository.save(this.pagamento1);

        // Then
        Assertions.assertThat(pagamentoSalvo.getDataHoraCriacao()).isNotNull();
        Assertions.assertThat(pagamentoSalvo.getDataHoraAtualizacao()).isNotNull();
    }
}
