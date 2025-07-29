package com.rsdesenvolvimento.estoque.services;

import com.rsdesenvolvimento.estoque.modelo.dtos.AtualizarEstoqueRequestDto;
import com.rsdesenvolvimento.estoque.modelo.dtos.EstoqueRequestDto;
import com.rsdesenvolvimento.estoque.modelo.dtos.EstoqueResponseDto;
import com.rsdesenvolvimento.estoque.modelo.entidade.Estoque;
import com.rsdesenvolvimento.estoque.modelo.mappers.EstoqueMapper;
import com.rsdesenvolvimento.estoque.repositorios.EstoqueRepository;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

@ExtendWith(MockitoExtension.class)
@DisplayName("EstoqueService - Testes Unitários")
class EstoqueServiceTest {

    @Mock
    private EstoqueRepository repository;

    @Mock
    private EstoqueMapper mapper;

    @Mock
    private CloudinaryService cloudinaryService;

    @Mock
    private MultipartFile multipartFile;

    @InjectMocks
    private EstoqueService estoqueService;

    private Estoque estoque;
    private EstoqueRequestDto estoqueRequestDto;
    private EstoqueResponseDto estoqueResponseDto;
    private AtualizarEstoqueRequestDto atualizarEstoqueDto;

    @BeforeEach
    void setUp() {
        this.estoque = Estoque.builder().id(1L).nome("Produto Teste")
                .descricao("Descrição do produto teste").preco(BigDecimal.valueOf(29.99))
                .categoria("CATEGORIA_TESTE").estoque(100)
                .imagemUrl("https://example.com/imagem.jpg").build();

        this.estoqueRequestDto = new EstoqueRequestDto();
        this.estoqueRequestDto.setNome("Produto Teste");
        this.estoqueRequestDto.setDescricao("Descrição do produto teste");
        this.estoqueRequestDto.setPreco(BigDecimal.valueOf(29.99));
        this.estoqueRequestDto.setCategoria("CATEGORIA_TESTE");
        this.estoqueRequestDto.setEstoque(100);

        this.estoqueResponseDto = new EstoqueResponseDto();
        this.estoqueResponseDto.setId(1L);
        this.estoqueResponseDto.setNome("Produto Teste");
        this.estoqueResponseDto.setDescricao("Descrição do produto teste");
        this.estoqueResponseDto.setPreco(BigDecimal.valueOf(29.99));
        this.estoqueResponseDto.setCategoria("CATEGORIA_TESTE");
        this.estoqueResponseDto.setEstoque(100);
        this.estoqueResponseDto.setImagemUrl("https://example.com/imagem.jpg");

        this.atualizarEstoqueDto = new AtualizarEstoqueRequestDto();
        this.atualizarEstoqueDto.setProdutoId(1L);
        this.atualizarEstoqueDto.setQuantidade(10);
    }

    @Nested
    @DisplayName("Testes do método salvar")
    class SalvarTests {

        @Test
        @DisplayName("Deve salvar produto com sucesso quando dados válidos são fornecidos")
        void deveSalvarProdutoComSucesso() throws Exception {
            // Given
            String urlImagem = "https://cloudinary.com/imagem.jpg";
            Mockito.when(EstoqueServiceTest.this.cloudinaryService
                    .uploadImage(EstoqueServiceTest.this.multipartFile)).thenReturn(urlImagem);
            Mockito.when(EstoqueServiceTest.this.mapper
                    .toEntity(EstoqueServiceTest.this.estoqueRequestDto))
                    .thenReturn(EstoqueServiceTest.this.estoque);
            Mockito.when(
                    EstoqueServiceTest.this.repository.save(ArgumentMatchers.any(Estoque.class)))
                    .thenReturn(EstoqueServiceTest.this.estoque);
            Mockito.when(EstoqueServiceTest.this.mapper.toDto(EstoqueServiceTest.this.estoque))
                    .thenReturn(EstoqueServiceTest.this.estoqueResponseDto);

            // When
            EstoqueResponseDto resultado = EstoqueServiceTest.this.estoqueService.salvar(
                    EstoqueServiceTest.this.estoqueRequestDto,
                    EstoqueServiceTest.this.multipartFile);

            // Then
            Assertions.assertThat(resultado).isNotNull();
            Assertions.assertThat(resultado.getNome()).isEqualTo("Produto Teste");
            Assertions.assertThat(resultado.getImagemUrl())
                    .isEqualTo("https://example.com/imagem.jpg");

            Mockito.verify(EstoqueServiceTest.this.cloudinaryService)
                    .uploadImage(EstoqueServiceTest.this.multipartFile);
            Mockito.verify(EstoqueServiceTest.this.mapper)
                    .toEntity(EstoqueServiceTest.this.estoqueRequestDto);
            Mockito.verify(EstoqueServiceTest.this.repository)
                    .save(ArgumentMatchers.any(Estoque.class));
            Mockito.verify(EstoqueServiceTest.this.mapper).toDto(EstoqueServiceTest.this.estoque);
        }

        @Test
        @DisplayName("Deve lançar exceção quando falha no upload da imagem")
        void deveLancarExcecaoQuandoFalhaUploadImagem() throws Exception {
            // Given
            Mockito.when(EstoqueServiceTest.this.cloudinaryService
                    .uploadImage(EstoqueServiceTest.this.multipartFile))
                    .thenThrow(new RuntimeException("Erro no upload"));

            // When & Then
            Assertions
                    .assertThatThrownBy(() -> EstoqueServiceTest.this.estoqueService.salvar(
                            EstoqueServiceTest.this.estoqueRequestDto,
                            EstoqueServiceTest.this.multipartFile))
                    .isInstanceOf(RuntimeException.class).hasMessage("Erro no upload");

            Mockito.verify(EstoqueServiceTest.this.cloudinaryService)
                    .uploadImage(EstoqueServiceTest.this.multipartFile);
            Mockito.verifyNoInteractions(EstoqueServiceTest.this.mapper,
                    EstoqueServiceTest.this.repository);
        }
    }

    @Nested
    @DisplayName("Testes do método listarPorCategoria")
    class ListarPorCategoriaTests {

        @Test
        @DisplayName("Deve listar produtos por categoria específica")
        void deveListarProdutosPorCategoriaEspecifica() {
            // Given
            String categoria = "CATEGORIA_TESTE";
            List<Estoque> estoques = Arrays.asList(EstoqueServiceTest.this.estoque);
            List<EstoqueResponseDto> esperados =
                    Arrays.asList(EstoqueServiceTest.this.estoqueResponseDto);

            Mockito.when(EstoqueServiceTest.this.repository.findByCategoria(categoria))
                    .thenReturn(estoques);
            Mockito.when(EstoqueServiceTest.this.mapper.toDtoList(estoques)).thenReturn(esperados);

            // When
            List<EstoqueResponseDto> resultado =
                    EstoqueServiceTest.this.estoqueService.listarPorCategoria(categoria);

            // Then
            Assertions.assertThat(resultado).isNotEmpty().hasSize(1);
            Assertions.assertThat(resultado.get(0).getCategoria()).isEqualTo("CATEGORIA_TESTE");

            Mockito.verify(EstoqueServiceTest.this.repository).findByCategoria(categoria);
            Mockito.verify(EstoqueServiceTest.this.mapper).toDtoList(estoques);
        }

        @Test
        @DisplayName("Deve listar todos os produtos quando categoria é null")
        void deveListarTodosProdutosQuandoCategoriaNula() {
            // Given
            List<Estoque> estoques = Arrays.asList(EstoqueServiceTest.this.estoque);
            List<EstoqueResponseDto> esperados =
                    Arrays.asList(EstoqueServiceTest.this.estoqueResponseDto);

            Mockito.when(EstoqueServiceTest.this.repository.findAll()).thenReturn(estoques);
            Mockito.when(EstoqueServiceTest.this.mapper.toDtoList(estoques)).thenReturn(esperados);

            // When
            List<EstoqueResponseDto> resultado =
                    EstoqueServiceTest.this.estoqueService.listarPorCategoria(null);

            // Then
            Assertions.assertThat(resultado).isNotEmpty().hasSize(1);

            Mockito.verify(EstoqueServiceTest.this.repository).findAll();
            Mockito.verify(EstoqueServiceTest.this.repository, Mockito.never())
                    .findByCategoria(ArgumentMatchers.anyString());
            Mockito.verify(EstoqueServiceTest.this.mapper).toDtoList(estoques);
        }

        @Test
        @DisplayName("Deve listar todos os produtos quando categoria é vazia")
        void deveListarTodosProdutosQuandoCategoriaVazia() {
            // Given
            List<Estoque> estoques = Arrays.asList(EstoqueServiceTest.this.estoque);
            List<EstoqueResponseDto> esperados =
                    Arrays.asList(EstoqueServiceTest.this.estoqueResponseDto);

            Mockito.when(EstoqueServiceTest.this.repository.findAll()).thenReturn(estoques);
            Mockito.when(EstoqueServiceTest.this.mapper.toDtoList(estoques)).thenReturn(esperados);

            // When
            List<EstoqueResponseDto> resultado =
                    EstoqueServiceTest.this.estoqueService.listarPorCategoria("");

            // Then
            Assertions.assertThat(resultado).isNotEmpty().hasSize(1);

            Mockito.verify(EstoqueServiceTest.this.repository).findAll();
            Mockito.verify(EstoqueServiceTest.this.repository, Mockito.never())
                    .findByCategoria(ArgumentMatchers.anyString());
            Mockito.verify(EstoqueServiceTest.this.mapper).toDtoList(estoques);
        }

        @Test
        @DisplayName("Deve retornar lista vazia quando não encontra produtos")
        void deveRetornarListaVaziaQuandoNaoEncontraProdutos() {
            // Given
            String categoria = "CATEGORIA_INEXISTENTE";
            Mockito.when(EstoqueServiceTest.this.repository.findByCategoria(categoria))
                    .thenReturn(Collections.emptyList());
            Mockito.when(EstoqueServiceTest.this.mapper.toDtoList(Collections.emptyList()))
                    .thenReturn(Collections.emptyList());

            // When
            List<EstoqueResponseDto> resultado =
                    EstoqueServiceTest.this.estoqueService.listarPorCategoria(categoria);

            // Then
            Assertions.assertThat(resultado).isEmpty();

            Mockito.verify(EstoqueServiceTest.this.repository).findByCategoria(categoria);
            Mockito.verify(EstoqueServiceTest.this.mapper).toDtoList(Collections.emptyList());
        }
    }

    @Nested
    @DisplayName("Testes do método buscarPorId")
    class BuscarPorIdTests {

        @Test
        @DisplayName("Deve buscar produto por ID com sucesso")
        void deveBuscarProdutoPorIdComSucesso() {
            // Given
            Long id = 1L;
            Mockito.when(EstoqueServiceTest.this.repository.findById(id))
                    .thenReturn(Optional.of(EstoqueServiceTest.this.estoque));
            Mockito.when(EstoqueServiceTest.this.mapper.toDto(EstoqueServiceTest.this.estoque))
                    .thenReturn(EstoqueServiceTest.this.estoqueResponseDto);

            // When
            EstoqueResponseDto resultado = EstoqueServiceTest.this.estoqueService.buscarPorId(id);

            // Then
            Assertions.assertThat(resultado).isNotNull();
            Assertions.assertThat(resultado.getId()).isEqualTo(id);
            Assertions.assertThat(resultado.getNome()).isEqualTo("Produto Teste");

            Mockito.verify(EstoqueServiceTest.this.repository).findById(id);
            Mockito.verify(EstoqueServiceTest.this.mapper).toDto(EstoqueServiceTest.this.estoque);
        }

        @Test
        @DisplayName("Deve lançar exceção quando produto não é encontrado")
        void deveLancarExcecaoQuandoProdutoNaoEncontrado() {
            // Given
            Long idInexistente = 999L;
            Mockito.when(EstoqueServiceTest.this.repository.findById(idInexistente))
                    .thenReturn(Optional.empty());

            // When & Then
            Assertions
                    .assertThatThrownBy(
                            () -> EstoqueServiceTest.this.estoqueService.buscarPorId(idInexistente))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Produto não encontrado");

            Mockito.verify(EstoqueServiceTest.this.repository).findById(idInexistente);
            Mockito.verifyNoInteractions(EstoqueServiceTest.this.mapper);
        }
    }

    @Nested
    @DisplayName("Testes do método atualizar")
    class AtualizarTests {

        @Test
        @DisplayName("Deve atualizar produto com sucesso")
        void deveAtualizarProdutoComSucesso() {
            // Given
            Long id = 1L;
            Estoque estoqueAtualizado = Estoque.builder().id(id).nome("Produto Atualizado")
                    .descricao("Nova descrição").preco(BigDecimal.valueOf(39.99))
                    .categoria("NOVA_CATEGORIA").estoque(50).build();

            EstoqueResponseDto responseAtualizado = new EstoqueResponseDto();
            responseAtualizado.setId(id);
            responseAtualizado.setNome("Produto Atualizado");

            Mockito.when(EstoqueServiceTest.this.repository.findById(id))
                    .thenReturn(Optional.of(EstoqueServiceTest.this.estoque));
            Mockito.when(EstoqueServiceTest.this.mapper
                    .toEntity(EstoqueServiceTest.this.estoqueRequestDto))
                    .thenReturn(estoqueAtualizado);
            Mockito.when(EstoqueServiceTest.this.repository.save(estoqueAtualizado))
                    .thenReturn(estoqueAtualizado);
            Mockito.when(EstoqueServiceTest.this.mapper.toDto(estoqueAtualizado))
                    .thenReturn(responseAtualizado);

            // When
            EstoqueResponseDto resultado = EstoqueServiceTest.this.estoqueService.atualizar(id,
                    EstoqueServiceTest.this.estoqueRequestDto);

            // Then
            Assertions.assertThat(resultado).isNotNull();
            Assertions.assertThat(resultado.getId()).isEqualTo(id);

            Mockito.verify(EstoqueServiceTest.this.repository).findById(id);
            Mockito.verify(EstoqueServiceTest.this.mapper)
                    .toEntity(EstoqueServiceTest.this.estoqueRequestDto);
            Mockito.verify(EstoqueServiceTest.this.repository).save(estoqueAtualizado);
            Mockito.verify(EstoqueServiceTest.this.mapper).toDto(estoqueAtualizado);
        }

        @Test
        @DisplayName("Deve lançar exceção quando produto para atualizar não é encontrado")
        void deveLancarExcecaoQuandoProdutoParaAtualizarNaoEncontrado() {
            // Given
            Long idInexistente = 999L;
            Mockito.when(EstoqueServiceTest.this.repository.findById(idInexistente))
                    .thenReturn(Optional.empty());

            // When & Then
            Assertions
                    .assertThatThrownBy(() -> EstoqueServiceTest.this.estoqueService
                            .atualizar(idInexistente, EstoqueServiceTest.this.estoqueRequestDto))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Produto não encontrado");

            Mockito.verify(EstoqueServiceTest.this.repository).findById(idInexistente);
            Mockito.verifyNoInteractions(EstoqueServiceTest.this.mapper);
        }
    }

    @Nested
    @DisplayName("Testes do método excluir")
    class ExcluirTests {

        @Test
        @DisplayName("Deve excluir produto com sucesso")
        void deveExcluirProdutoComSucesso() {
            // Given
            Long id = 1L;

            // When
            EstoqueServiceTest.this.estoqueService.excluir(id);

            // Then
            Mockito.verify(EstoqueServiceTest.this.repository).deleteById(id);
        }
    }

    @Nested
    @DisplayName("Testes do método validarEstoque")
    class ValidarEstoqueTests {

        @Test
        @DisplayName("Deve retornar true quando estoque é suficiente")
        void deveRetornarTrueQuandoEstoqueSuficiente() {
            // Given
            EstoqueServiceTest.this.atualizarEstoqueDto.setQuantidade(50);
            List<AtualizarEstoqueRequestDto> itens =
                    Arrays.asList(EstoqueServiceTest.this.atualizarEstoqueDto);

            Mockito.when(EstoqueServiceTest.this.repository.findById(1L))
                    .thenReturn(Optional.of(EstoqueServiceTest.this.estoque));

            // When
            boolean resultado = EstoqueServiceTest.this.estoqueService.validarEstoque(itens);

            // Then
            Assertions.assertThat(resultado).isTrue();
            Mockito.verify(EstoqueServiceTest.this.repository).findById(1L);
        }

        @Test
        @DisplayName("Deve retornar false quando estoque é insuficiente")
        void deveRetornarFalseQuandoEstoqueInsuficiente() {
            // Given
            EstoqueServiceTest.this.atualizarEstoqueDto.setQuantidade(150);
            List<AtualizarEstoqueRequestDto> itens =
                    Arrays.asList(EstoqueServiceTest.this.atualizarEstoqueDto);

            Mockito.when(EstoqueServiceTest.this.repository.findById(1L))
                    .thenReturn(Optional.of(EstoqueServiceTest.this.estoque));

            // When
            boolean resultado = EstoqueServiceTest.this.estoqueService.validarEstoque(itens);

            // Then
            Assertions.assertThat(resultado).isFalse();
            Mockito.verify(EstoqueServiceTest.this.repository).findById(1L);
        }

        @Test
        @DisplayName("Deve lançar exceção quando produto não é encontrado na validação")
        void deveLancarExcecaoQuandoProdutoNaoEncontradoNaValidacao() {
            // Given
            List<AtualizarEstoqueRequestDto> itens =
                    Arrays.asList(EstoqueServiceTest.this.atualizarEstoqueDto);
            Mockito.when(EstoqueServiceTest.this.repository.findById(1L))
                    .thenReturn(Optional.empty());

            // When & Then
            Assertions
                    .assertThatThrownBy(
                            () -> EstoqueServiceTest.this.estoqueService.validarEstoque(itens))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Produto não encontrado");

            Mockito.verify(EstoqueServiceTest.this.repository).findById(1L);
        }
    }

    @Nested
    @DisplayName("Testes do método reservarEstoque")
    class ReservarEstoqueTests {

        @Test
        @DisplayName("Deve reservar estoque com sucesso")
        void deveReservarEstoqueComSucesso() {
            // Given
            EstoqueServiceTest.this.atualizarEstoqueDto.setQuantidade(30);
            List<AtualizarEstoqueRequestDto> itens =
                    Arrays.asList(EstoqueServiceTest.this.atualizarEstoqueDto);

            Mockito.when(EstoqueServiceTest.this.repository.findById(1L))
                    .thenReturn(Optional.of(EstoqueServiceTest.this.estoque));
            Mockito.when(
                    EstoqueServiceTest.this.repository.save(ArgumentMatchers.any(Estoque.class)))
                    .thenReturn(EstoqueServiceTest.this.estoque);

            // When
            EstoqueServiceTest.this.estoqueService.reservarEstoque(itens);

            // Then
            Mockito.verify(EstoqueServiceTest.this.repository).findById(1L);
            Mockito.verify(EstoqueServiceTest.this.repository)
                    .save(EstoqueServiceTest.this.estoque);
            Assertions.assertThat(EstoqueServiceTest.this.estoque.getEstoque()).isEqualTo(70);
        }

        @Test
        @DisplayName("Deve lançar exceção quando produto não é encontrado na reserva")
        void deveLancarExcecaoQuandoProdutoNaoEncontradoNaReserva() {
            // Given
            List<AtualizarEstoqueRequestDto> itens =
                    Arrays.asList(EstoqueServiceTest.this.atualizarEstoqueDto);
            Mockito.when(EstoqueServiceTest.this.repository.findById(1L))
                    .thenReturn(Optional.empty());

            // When & Then
            Assertions
                    .assertThatThrownBy(
                            () -> EstoqueServiceTest.this.estoqueService.reservarEstoque(itens))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Produto não encontrado");

            Mockito.verify(EstoqueServiceTest.this.repository).findById(1L);
            Mockito.verify(EstoqueServiceTest.this.repository, Mockito.never())
                    .save(ArgumentMatchers.any());
        }
    }

    @Nested
    @DisplayName("Testes do método atualizarEstoque")
    class AtualizarEstoqueTests {

        @Test
        @DisplayName("Deve atualizar estoque com sucesso")
        void deveAtualizarEstoqueComSucesso() {
            // Given
            EstoqueServiceTest.this.atualizarEstoqueDto.setQuantidade(20);
            Mockito.when(EstoqueServiceTest.this.repository.findById(1L))
                    .thenReturn(Optional.of(EstoqueServiceTest.this.estoque));
            Mockito.when(
                    EstoqueServiceTest.this.repository.save(ArgumentMatchers.any(Estoque.class)))
                    .thenReturn(EstoqueServiceTest.this.estoque);

            // When
            EstoqueServiceTest.this.estoqueService
                    .atualizarEstoque(EstoqueServiceTest.this.atualizarEstoqueDto);

            // Then
            Mockito.verify(EstoqueServiceTest.this.repository).findById(1L);
            Mockito.verify(EstoqueServiceTest.this.repository)
                    .save(EstoqueServiceTest.this.estoque);
            Assertions.assertThat(EstoqueServiceTest.this.estoque.getEstoque()).isEqualTo(80);
        }

        @Test
        @DisplayName("Deve lançar exceção quando produto não é encontrado na atualização")
        void deveLancarExcecaoQuandoProdutoNaoEncontradoNaAtualizacao() {
            // Given
            Mockito.when(EstoqueServiceTest.this.repository.findById(1L))
                    .thenReturn(Optional.empty());

            // When & Then
            Assertions
                    .assertThatThrownBy(() -> EstoqueServiceTest.this.estoqueService
                            .atualizarEstoque(EstoqueServiceTest.this.atualizarEstoqueDto))
                    .isInstanceOf(ResponseStatusException.class).satisfies(ex -> {
                        ResponseStatusException responseEx = (ResponseStatusException) ex;
                        Assertions.assertThat(responseEx.getStatusCode())
                                .isEqualTo(HttpStatus.BAD_REQUEST);
                        Assertions.assertThat(responseEx.getReason())
                                .isEqualTo("Produto não encontrado");
                    });

            Mockito.verify(EstoqueServiceTest.this.repository).findById(1L);
            Mockito.verify(EstoqueServiceTest.this.repository, Mockito.never())
                    .save(ArgumentMatchers.any());
        }

        @Test
        @DisplayName("Deve lançar exceção quando estoque é insuficiente na atualização")
        void deveLancarExcecaoQuandoEstoqueInsuficienteNaAtualizacao() {
            // Given
            EstoqueServiceTest.this.atualizarEstoqueDto.setQuantidade(150);
            Mockito.when(EstoqueServiceTest.this.repository.findById(1L))
                    .thenReturn(Optional.of(EstoqueServiceTest.this.estoque));

            // When & Then
            Assertions
                    .assertThatThrownBy(() -> EstoqueServiceTest.this.estoqueService
                            .atualizarEstoque(EstoqueServiceTest.this.atualizarEstoqueDto))
                    .isInstanceOf(ResponseStatusException.class).satisfies(ex -> {
                        ResponseStatusException responseEx = (ResponseStatusException) ex;
                        Assertions.assertThat(responseEx.getStatusCode())
                                .isEqualTo(HttpStatus.BAD_REQUEST);
                        Assertions.assertThat(responseEx.getReason())
                                .isEqualTo("Estoque insuficiente");
                    });

            Mockito.verify(EstoqueServiceTest.this.repository).findById(1L);
            Mockito.verify(EstoqueServiceTest.this.repository, Mockito.never())
                    .save(ArgumentMatchers.any());
        }
    }
}
