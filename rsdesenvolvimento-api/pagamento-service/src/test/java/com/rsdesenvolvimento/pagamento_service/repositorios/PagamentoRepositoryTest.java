package com.rsdesenvolvimento.pagamento_service.repositorios;

import com.rsdesenvolvimento.pagamento_service.core.auditoria.AuditoriaConfig;
import com.rsdesenvolvimento.pagamento_service.modelo.entidades.Pagamento;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles("test")
@Import(AuditoriaConfig.class)
@DisplayName("Testes de Integração para PagamentoRepository")
class PagamentoRepositoryTest {

    @Autowired
    private PagamentoRepository pagamentoRepository;



    @Test
    @DisplayName("Deve buscar pagamento por ID")
    void deveBuscarPagamentoPorId() {
        // Given
        Pagamento pagamentoSalvo = this.pagamentoRepository.findById(2L).orElseThrow();

        // When
        Optional<Pagamento> pagamentoEncontrado =
                this.pagamentoRepository.findById(pagamentoSalvo.getId());

        // Then
        Assertions.assertThat(pagamentoEncontrado).isPresent();
        Assertions.assertThat(pagamentoEncontrado.get().getPedidoId()).isEqualTo(2L);
        Assertions.assertThat(pagamentoEncontrado.get().getValor())
                .isEqualTo(new BigDecimal("49.99"));
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
    void deveListarTodosOsPagamentosFinalizados() {

        // When
        List<Pagamento> pagamentos = this.pagamentoRepository.findByStatus("FINALIZADO");

        // Then
        Assertions.assertThat(pagamentos).hasSize(2);
        Assertions.assertThat(pagamentos).extracting(Pagamento::getPedidoId)
                .containsExactlyInAnyOrder(3L, 4L);
    }


    @Test
    @DisplayName("Deve deletar pagamento corretamente")
    void deveDeletarPagamentoCorretamente() {
        // When
        this.pagamentoRepository.deleteById(1l);

        // Then
        Optional<Pagamento> pagamentoDeletado = this.pagamentoRepository.findById(1L);
        Assertions.assertThat(pagamentoDeletado).isEmpty();
    }

    @Test
    @DisplayName("Deve atualizar pagamento existente")
    void deveAtualizarPagamentoExistente() {
        // Given
        Pagamento pagamentoSalvo = this.pagamentoRepository.findById(2L).orElseThrow();

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


        // When
        long count = this.pagamentoRepository.count();

        // Then
        Assertions.assertThat(count).isEqualTo(5L);
    }

    @Test
    @DisplayName("Deve verificar existência de pagamento por ID")
    void deveVerificarExistenciaDePagamentoPorId() {
        // Given
        Pagamento pagamentoSalvo = this.pagamentoRepository.findById(4L).orElseThrow();

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


}
