package com.rsdesenvolvimento.estoque.repositorios;

import com.rsdesenvolvimento.estoque.core.auditoria.AuditoriaConfig;
import com.rsdesenvolvimento.estoque.modelo.entidade.Estoque;
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
@DisplayName("EstoqueRepository - Testes de Integração")
class EstoqueRepositoryTest {

    @Autowired
    private EstoqueRepository estoqueRepository;

    @Test
    @DisplayName("Deve encontrar produtos por categoria")
    void deveEncontrarProdutosPorCategoria() {
        // When
        List<Estoque> produtoPrincipal = this.estoqueRepository.findByCategoria("PRATO PRINCIPAL");
        List<Estoque> produtoSobremesa = this.estoqueRepository.findByCategoria("SOBREMESA");

        // Then
        Assertions.assertThat(produtoPrincipal).hasSize(2);
        Assertions.assertThat(produtoPrincipal).extracting(Estoque::getCategoria)
                .containsOnly("PRATO PRINCIPAL");

        Assertions.assertThat(produtoSobremesa).hasSize(1);
        Assertions.assertThat(produtoSobremesa.get(0).getCategoria()).isEqualTo("SOBREMESA");
    }

    @Test
    @DisplayName("Deve retornar lista vazia quando categoria não existe")
    void deveRetornarListaVaziaQuandoCategoriaNaoExiste() {
        // When
        List<Estoque> produtos = this.estoqueRepository.findByCategoria("CATEGORIA_INEXISTENTE");

        // Then
        Assertions.assertThat(produtos).isEmpty();
    }

    @Test
    @DisplayName("Deve salvar produto com sucesso")
    void deveSalvarProdutoComSucesso() {
        // Given
        Estoque novoProduto =
                Estoque.builder().nome("Produto Novo").descricao("Descrição do produto novo")
                        .preco(BigDecimal.valueOf(99.99)).categoria("CASA_JARDIM").estoque(25)
                        .imagemUrl("https://example.com/novo.jpg").build();

        // When
        Estoque produtoSalvo = this.estoqueRepository.save(novoProduto);

        // Then
        Assertions.assertThat(produtoSalvo).isNotNull();
        Assertions.assertThat(produtoSalvo.getId()).isNotNull();
        Assertions.assertThat(produtoSalvo.getNome()).isEqualTo("Produto Novo");
        Assertions.assertThat(produtoSalvo.getCategoria()).isEqualTo("CASA_JARDIM");
    }

    @Test
    @DisplayName("Deve encontrar produto por ID")
    void deveEncontrarProdutoPorId() {
        // When
        Optional<Estoque> produtoEncontrado = this.estoqueRepository.findById(1L);

        // Then
        Assertions.assertThat(produtoEncontrado).isPresent();
        Assertions.assertThat(produtoEncontrado.get().getNome()).isEqualTo("Produto 1");
        Assertions.assertThat(produtoEncontrado.get().getCategoria()).isEqualTo("PRATO PRINCIPAL");
    }

    @Test
    @DisplayName("Deve retornar Optional vazio quando produto não existe")
    void deveRetornarOptionalVazioQuandoProdutoNaoExiste() {
        // When
        Optional<Estoque> produtoInexistente = this.estoqueRepository.findById(999L);

        // Then
        Assertions.assertThat(produtoInexistente).isEmpty();
    }

    @Test
    @DisplayName("Deve atualizar produto existente")
    void deveAtualizarProdutoExistente() {
        // Given
        String novoNome = "Produto 1 Atualizado";
        Integer novoEstoque = 150;
        Estoque produto = this.estoqueRepository.findById(2L).orElseThrow();

        // When
        produto.setNome(novoNome);
        produto.setEstoque(novoEstoque);
        Estoque produtoAtualizado = this.estoqueRepository.save(produto);

        // Then
        Assertions.assertThat(produtoAtualizado.getNome()).isEqualTo(novoNome);
        Assertions.assertThat(produtoAtualizado.getEstoque()).isEqualTo(novoEstoque);

        // Verificar se foi realmente atualizado no banco
        Optional<Estoque> produtoVerificacao = this.estoqueRepository.findById(2L);
        Assertions.assertThat(produtoVerificacao).isPresent();
        Assertions.assertThat(produtoVerificacao.get().getNome()).isEqualTo(novoNome);
        Assertions.assertThat(produtoVerificacao.get().getEstoque()).isEqualTo(novoEstoque);
    }

    @Test
    @DisplayName("Deve excluir produto por ID")
    void deveExcluirProdutoPorId() {
        // Given
        Long idParaExcluir = 1L;

        // When
        this.estoqueRepository.deleteById(idParaExcluir);

        // Then
        Optional<Estoque> produtoExcluido = this.estoqueRepository.findById(idParaExcluir);
        Assertions.assertThat(produtoExcluido).isEmpty();

        // Verificar que outros produtos ainda existem
        List<Estoque> produtosRestantes = this.estoqueRepository.findAll();
        Assertions.assertThat(produtosRestantes).hasSize(2);
    }

    @Test
    @DisplayName("Deve encontrar todos os produtos")
    void deveEncontrarTodosOsProdutos() {
        // When
        List<Estoque> todosProdutos = this.estoqueRepository.findAll();

        // Then
        Assertions.assertThat(todosProdutos).hasSize(3);
        Assertions.assertThat(todosProdutos).extracting(Estoque::getNome)
                .containsExactlyInAnyOrder("Produto 1", "Produto 2", "Produto 3");
    }

    @Test
    @DisplayName("Deve manter integridade dos dados ao salvar produto com campos obrigatórios")
    void deveManterIntegridadeDadosAoSalvarProduto() {
        // Given
        Estoque produto = Estoque.builder().nome("Produto Teste Integridade")
                .descricao("Teste de integridade de dados").preco(BigDecimal.valueOf(15.50))
                .categoria("TESTE").estoque(10).build(); // Sem imagemUrl para testar campo opcional

        // When
        Estoque produtoSalvo = this.estoqueRepository.save(produto);

        // Then
        Assertions.assertThat(produtoSalvo).isNotNull();
        Assertions.assertThat(produtoSalvo.getId()).isNotNull();
        Assertions.assertThat(produtoSalvo.getNome()).isEqualTo("Produto Teste Integridade");
        Assertions.assertThat(produtoSalvo.getImagemUrl()).isNull(); // Campo opcional
        Assertions.assertThat(produtoSalvo.getPreco())
                .isEqualByComparingTo(BigDecimal.valueOf(15.50));
    }
}
