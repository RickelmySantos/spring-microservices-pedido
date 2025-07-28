package com.rsdesenvolvimento.estoque.repositorios;

import com.rsdesenvolvimento.estoque.modelo.entidade.Estoque;
import java.math.BigDecimal;
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
@DisplayName("EstoqueRepository - Testes de Integração")
class EstoqueRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private EstoqueRepository estoqueRepository;

    private Estoque estoque1;
    private Estoque estoque2;
    private Estoque estoque3;

    @BeforeEach
    void setUp() {
        // Limpar dados antes de cada teste
        this.entityManager.clear();

        // Criar produtos de teste
        this.estoque1 = Estoque.builder().nome("Produto 1").descricao("Descrição do produto 1")
                .preco(BigDecimal.valueOf(29.99)).categoria("ELETRONICOS").estoque(100)
                .imagemUrl("https://example.com/produto1.jpg").build();

        this.estoque2 = Estoque.builder().nome("Produto 2").descricao("Descrição do produto 2")
                .preco(BigDecimal.valueOf(49.99)).categoria("ELETRONICOS").estoque(50)
                .imagemUrl("https://example.com/produto2.jpg").build();

        this.estoque3 = Estoque.builder().nome("Produto 3").descricao("Descrição do produto 3")
                .preco(BigDecimal.valueOf(19.99)).categoria("LIVROS").estoque(200)
                .imagemUrl("https://example.com/produto3.jpg").build();

        // Persistir no banco H2
        this.entityManager.persistAndFlush(this.estoque1);
        this.entityManager.persistAndFlush(this.estoque2);
        this.entityManager.persistAndFlush(this.estoque3);
    }

    @Test
    @DisplayName("Deve encontrar produtos por categoria")
    void deveEncontrarProdutosPorCategoria() {
        // When
        List<Estoque> produtosEletronicos = this.estoqueRepository.findByCategoria("ELETRONICOS");
        List<Estoque> produtosLivros = this.estoqueRepository.findByCategoria("LIVROS");

        // Then
        Assertions.assertThat(produtosEletronicos).hasSize(2);
        Assertions.assertThat(produtosEletronicos).extracting(Estoque::getCategoria)
                .containsOnly("ELETRONICOS");

        Assertions.assertThat(produtosLivros).hasSize(1);
        Assertions.assertThat(produtosLivros.get(0).getCategoria()).isEqualTo("LIVROS");
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
        Optional<Estoque> produtoEncontrado =
                this.estoqueRepository.findById(this.estoque1.getId());

        // Then
        Assertions.assertThat(produtoEncontrado).isPresent();
        Assertions.assertThat(produtoEncontrado.get().getNome()).isEqualTo("Produto 1");
        Assertions.assertThat(produtoEncontrado.get().getCategoria()).isEqualTo("ELETRONICOS");
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

        // When
        this.estoque1.setNome(novoNome);
        this.estoque1.setEstoque(novoEstoque);
        Estoque produtoAtualizado = this.estoqueRepository.save(this.estoque1);

        // Then
        Assertions.assertThat(produtoAtualizado.getNome()).isEqualTo(novoNome);
        Assertions.assertThat(produtoAtualizado.getEstoque()).isEqualTo(novoEstoque);

        // Verificar se foi realmente atualizado no banco
        Optional<Estoque> produtoVerificacao =
                this.estoqueRepository.findById(this.estoque1.getId());
        Assertions.assertThat(produtoVerificacao).isPresent();
        Assertions.assertThat(produtoVerificacao.get().getNome()).isEqualTo(novoNome);
        Assertions.assertThat(produtoVerificacao.get().getEstoque()).isEqualTo(novoEstoque);
    }

    @Test
    @DisplayName("Deve excluir produto por ID")
    void deveExcluirProdutoPorId() {
        // Given
        Long idParaExcluir = this.estoque1.getId();

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
