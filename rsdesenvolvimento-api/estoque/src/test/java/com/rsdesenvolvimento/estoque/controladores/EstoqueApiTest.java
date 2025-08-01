package com.rsdesenvolvimento.estoque.controladores;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rsdesenvolvimento.estoque.core.exception.ImageUploadException;
import com.rsdesenvolvimento.estoque.modelo.dtos.AtualizarEstoqueRequestDto;
import com.rsdesenvolvimento.estoque.modelo.dtos.EstoqueRequestDto;
import com.rsdesenvolvimento.estoque.modelo.dtos.EstoqueResponseDto;
import com.rsdesenvolvimento.estoque.services.CloudinaryService;
import com.rsdesenvolvimento.estoque.services.EstoqueService;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.hamcrest.Matchers;
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
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.server.ResponseStatusException;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
@DisplayName("EstoqueApi - Testes de Integração")
class EstoqueApiTest {

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @Mock
    private EstoqueService estoqueService;

    @Mock
    private CloudinaryService cloudinaryService;

    @InjectMocks
    private EstoqueApi estoqueApi;

    private EstoqueRequestDto estoqueRequestDto;
    private EstoqueResponseDto estoqueResponseDto;
    private AtualizarEstoqueRequestDto atualizarEstoqueDto;

    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(this.estoqueApi).build();
        this.objectMapper = new ObjectMapper();

        // Arrange - DTOs de teste
        this.estoqueRequestDto = new EstoqueRequestDto();
        this.estoqueRequestDto.setNome("Produto Teste");
        this.estoqueRequestDto.setDescricao("Descrição do produto teste");
        this.estoqueRequestDto.setPreco(BigDecimal.valueOf(99.99));
        this.estoqueRequestDto.setEstoque(10);
        this.estoqueRequestDto.setCategoria("ELETRONICOS");

        this.estoqueResponseDto = new EstoqueResponseDto();
        this.estoqueResponseDto.setId(1L);
        this.estoqueResponseDto.setNome("Produto Teste");
        this.estoqueResponseDto.setDescricao("Descrição do produto teste");
        this.estoqueResponseDto.setPreco(BigDecimal.valueOf(99.99));
        this.estoqueResponseDto.setEstoque(10);
        this.estoqueResponseDto.setCategoria("ELETRONICOS");
        this.estoqueResponseDto.setImagemUrl("https://cloudinary.com/image/test.jpg");

        this.atualizarEstoqueDto = new AtualizarEstoqueRequestDto();
        this.atualizarEstoqueDto.setProdutoId(1L);
        this.atualizarEstoqueDto.setQuantidade(5);
    }

    @Nested
    @DisplayName("Cadastro de Produto")
    class CadastroProdutoTests {

        @Test
        @DisplayName("Deve cadastrar produto com sucesso")
        void deveCadastrarProdutoComSucesso() throws Exception {
            // Arrange
            MockMultipartFile imagem = new MockMultipartFile("imagem", "test.jpg",
                    MediaType.IMAGE_JPEG_VALUE, "fake image content".getBytes());
            MockMultipartFile dados = new MockMultipartFile("dados", "",
                    MediaType.APPLICATION_JSON_VALUE, EstoqueApiTest.this.objectMapper
                            .writeValueAsBytes(EstoqueApiTest.this.estoqueRequestDto));

            Mockito.when(EstoqueApiTest.this.estoqueService.salvar(
                    ArgumentMatchers.any(EstoqueRequestDto.class),
                    ArgumentMatchers.any(MockMultipartFile.class)))
                    .thenReturn(EstoqueApiTest.this.estoqueResponseDto);

            // Act & Assert
            EstoqueApiTest.this.mockMvc
                    .perform(MockMvcRequestBuilders.multipart("/estoque").file(dados).file(imagem)
                            .contentType(MediaType.MULTIPART_FORM_DATA))
                    .andExpect(MockMvcResultMatchers.status().isCreated())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1L))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.nome").value("Produto Teste"))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.preco").value(99.99))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.estoque").value(10))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.categoria").value("ELETRONICOS"));

            Mockito.verify(EstoqueApiTest.this.estoqueService).salvar(
                    ArgumentMatchers.any(EstoqueRequestDto.class),
                    ArgumentMatchers.any(MockMultipartFile.class));
        }

        @Test
        @DisplayName("Deve retornar erro quando dados são inválidos")
        void deveRetornarErroQuandoDadosSaoInvalidos() throws Exception {
            // Arrange
            MockMultipartFile imagem = new MockMultipartFile("imagem", "test.jpg",
                    MediaType.IMAGE_JPEG_VALUE, "fake image content".getBytes());
            MockMultipartFile dadosInvalidos = new MockMultipartFile("dados", "",
                    MediaType.APPLICATION_JSON_VALUE, "invalid json".getBytes());

            // Act & Assert
            EstoqueApiTest.this.mockMvc
                    .perform(MockMvcRequestBuilders.multipart("/estoque").file(dadosInvalidos)
                            .file(imagem).contentType(MediaType.MULTIPART_FORM_DATA))
                    .andExpect(MockMvcResultMatchers.status().isBadRequest());

            Mockito.verify(EstoqueApiTest.this.estoqueService, Mockito.never())
                    .salvar(ArgumentMatchers.any(), ArgumentMatchers.any());
        }
    }

    @Nested
    @DisplayName("Listagem de Produtos")
    class ListagemProdutosTests {

        @Test
        @DisplayName("Deve listar todos os produtos quando categoria não informada")
        void deveListarTodosProdutosQuandoCategoriaNaoInformada() throws Exception {
            // Arrange
            List<EstoqueResponseDto> produtos = Arrays.asList(
                    EstoqueApiTest.this.estoqueResponseDto, EstoqueApiTest.this.estoqueResponseDto);

            Mockito.when(EstoqueApiTest.this.estoqueService.listarPorCategoria(null))
                    .thenReturn(produtos);

            // Act & Assert
            EstoqueApiTest.this.mockMvc
                    .perform(MockMvcRequestBuilders.get("/estoque")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(2)))
                    .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(1L))
                    .andExpect(MockMvcResultMatchers.jsonPath("$[1].id").value(1L));

            Mockito.verify(EstoqueApiTest.this.estoqueService).listarPorCategoria(null);
        }

        @Test
        @DisplayName("Deve filtrar produtos por categoria")
        void deveFiltrarProdutosPorCategoria() throws Exception {
            // Arrange
            List<EstoqueResponseDto> produtos =
                    Collections.singletonList(EstoqueApiTest.this.estoqueResponseDto);

            Mockito.when(EstoqueApiTest.this.estoqueService.listarPorCategoria("ELETRONICOS"))
                    .thenReturn(produtos);

            // Act & Assert
            EstoqueApiTest.this.mockMvc
                    .perform(
                            MockMvcRequestBuilders.get("/estoque").param("categoria", "ELETRONICOS")
                                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(1))).andExpect(
                            MockMvcResultMatchers.jsonPath("$[0].categoria").value("ELETRONICOS"));

            Mockito.verify(EstoqueApiTest.this.estoqueService).listarPorCategoria("ELETRONICOS");
        }

        @Test
        @DisplayName("Deve retornar lista vazia quando nenhum produto encontrado")
        void deveRetornarListaVaziaQuandoNenhumProdutoEncontrado() throws Exception {
            // Arrange
            Mockito.when(
                    EstoqueApiTest.this.estoqueService.listarPorCategoria("CATEGORIA_INEXISTENTE"))
                    .thenReturn(Collections.emptyList());

            // Act & Assert
            EstoqueApiTest.this.mockMvc
                    .perform(MockMvcRequestBuilders.get("/estoque")
                            .param("categoria", "CATEGORIA_INEXISTENTE")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(0)));
        }
    }

    @Nested
    @DisplayName("Busca de Produto")
    class BuscaProdutoTests {

        @Test
        @DisplayName("Deve buscar produto por ID com sucesso")
        void deveBuscarProdutoPorIdComSucesso() throws Exception {
            // Arrange
            Mockito.when(EstoqueApiTest.this.estoqueService.buscarPorId(1L))
                    .thenReturn(EstoqueApiTest.this.estoqueResponseDto);

            // Act & Assert
            EstoqueApiTest.this.mockMvc
                    .perform(MockMvcRequestBuilders.get("/estoque/1")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1L))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.nome").value("Produto Teste"));

            Mockito.verify(EstoqueApiTest.this.estoqueService).buscarPorId(1L);
        }

        @Test
        @DisplayName("Deve retornar 404 quando produto não encontrado")
        void deveRetornar404QuandoProdutoNaoEncontrado() throws Exception {
            // Arrange
            Mockito.when(EstoqueApiTest.this.estoqueService.buscarPorId(999L)).thenThrow(
                    new ResponseStatusException(HttpStatus.NOT_FOUND, "Produto não encontrado"));

            // Act & Assert
            EstoqueApiTest.this.mockMvc
                    .perform(MockMvcRequestBuilders.get("/estoque/999")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.status().isNotFound());

            Mockito.verify(EstoqueApiTest.this.estoqueService).buscarPorId(999L);
        }
    }

    @Nested
    @DisplayName("Atualização de Produto")
    class AtualizacaoProdutoTests {

        @Test
        @DisplayName("Deve atualizar produto com sucesso")
        void deveAtualizarProdutoComSucesso() throws Exception {
            // Arrange
            Mockito.when(EstoqueApiTest.this.estoqueService.atualizar(ArgumentMatchers.eq(1L),
                    ArgumentMatchers.any(EstoqueRequestDto.class)))
                    .thenReturn(EstoqueApiTest.this.estoqueResponseDto);

            // Act & Assert
            EstoqueApiTest.this.mockMvc
                    .perform(MockMvcRequestBuilders.put("/estoque/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(EstoqueApiTest.this.objectMapper
                                    .writeValueAsString(EstoqueApiTest.this.estoqueRequestDto)))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1L))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.nome").value("Produto Teste"));

            Mockito.verify(EstoqueApiTest.this.estoqueService).atualizar(ArgumentMatchers.eq(1L),
                    ArgumentMatchers.any(EstoqueRequestDto.class));
        }
    }

    @Nested
    @DisplayName("Exclusão de Produto")
    class ExclusaoProdutoTests {

        @Test
        @DisplayName("Deve excluir produto com sucesso")
        void deveExcluirProdutoComSucesso() throws Exception {
            // Arrange
            Mockito.doNothing().when(EstoqueApiTest.this.estoqueService).excluir(1L);

            // Act & Assert
            EstoqueApiTest.this.mockMvc
                    .perform(MockMvcRequestBuilders.delete("/estoque/1")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.status().isNoContent());

            Mockito.verify(EstoqueApiTest.this.estoqueService).excluir(1L);
        }
    }

    @Nested
    @DisplayName("Validação de Estoque")
    class ValidacaoEstoqueTests {

        @Test
        @DisplayName("Deve validar estoque com sucesso quando há disponibilidade")
        void deveValidarEstoqueComSucessoQuandoHaDisponibilidade() throws Exception {
            // Arrange
            List<AtualizarEstoqueRequestDto> itens =
                    Arrays.asList(EstoqueApiTest.this.atualizarEstoqueDto);

            Mockito.when(
                    EstoqueApiTest.this.estoqueService.validarEstoque(ArgumentMatchers.anyList()))
                    .thenReturn(true);

            // Act & Assert
            EstoqueApiTest.this.mockMvc
                    .perform(MockMvcRequestBuilders.post("/estoque/validar")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(EstoqueApiTest.this.objectMapper.writeValueAsString(itens)))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.content().string("true"));

            Mockito.verify(EstoqueApiTest.this.estoqueService)
                    .validarEstoque(ArgumentMatchers.anyList());
        }

        @Test
        @DisplayName("Deve retornar false quando estoque insuficiente")
        void deveRetornarFalseQuandoEstoqueInsuficiente() throws Exception {
            // Arrange
            List<AtualizarEstoqueRequestDto> itens =
                    Arrays.asList(EstoqueApiTest.this.atualizarEstoqueDto);

            Mockito.when(
                    EstoqueApiTest.this.estoqueService.validarEstoque(ArgumentMatchers.anyList()))
                    .thenReturn(false);

            // Act & Assert
            EstoqueApiTest.this.mockMvc
                    .perform(MockMvcRequestBuilders.post("/estoque/validar")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(EstoqueApiTest.this.objectMapper.writeValueAsString(itens)))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.content().string("false"));
        }
    }

    @Nested
    @DisplayName("Reserva de Estoque")
    class ReservaEstoqueTests {

        @Test
        @DisplayName("Deve reservar estoque com sucesso")
        void deveReservarEstoqueComSucesso() throws Exception {
            // Arrange
            List<AtualizarEstoqueRequestDto> itens =
                    Arrays.asList(EstoqueApiTest.this.atualizarEstoqueDto);

            Mockito.doNothing().when(EstoqueApiTest.this.estoqueService)
                    .reservarEstoque(ArgumentMatchers.anyList());

            // Act & Assert
            EstoqueApiTest.this.mockMvc
                    .perform(MockMvcRequestBuilders.post("/estoque/reservar")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(EstoqueApiTest.this.objectMapper.writeValueAsString(itens)))
                    .andExpect(MockMvcResultMatchers.status().isCreated());

            Mockito.verify(EstoqueApiTest.this.estoqueService)
                    .reservarEstoque(ArgumentMatchers.anyList());
        }
    }

    @Nested
    @DisplayName("Atualização de Estoque")
    class AtualizacaoEstoqueTests {

        @Test
        @DisplayName("Deve atualizar estoque com sucesso")
        void deveAtualizarEstoqueComSucesso() throws Exception {
            // Arrange
            Mockito.doNothing().when(EstoqueApiTest.this.estoqueService)
                    .atualizarEstoque(ArgumentMatchers.any(AtualizarEstoqueRequestDto.class));

            // Act & Assert
            EstoqueApiTest.this.mockMvc
                    .perform(MockMvcRequestBuilders.post("/estoque/atualizar")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(EstoqueApiTest.this.objectMapper
                                    .writeValueAsString(EstoqueApiTest.this.atualizarEstoqueDto)))
                    .andExpect(MockMvcResultMatchers.status().isOk());

            Mockito.verify(EstoqueApiTest.this.estoqueService)
                    .atualizarEstoque(ArgumentMatchers.any(AtualizarEstoqueRequestDto.class));
        }
    }

    @Nested
    @DisplayName("Upload de Imagem")
    class UploadImagemTests {

        @Test
        @DisplayName("Deve fazer upload de imagem com sucesso")
        void deveFazerUploadDeImagemComSucesso() throws Exception {
            // Arrange
            MockMultipartFile file = new MockMultipartFile("file", "test.jpg",
                    MediaType.IMAGE_JPEG_VALUE, "fake image content".getBytes());
            String expectedUrl = "https://cloudinary.com/image/uploaded.jpg";

            Mockito.when(EstoqueApiTest.this.cloudinaryService.uploadImage(ArgumentMatchers.any()))
                    .thenReturn(expectedUrl);

            // Act & Assert
            EstoqueApiTest.this.mockMvc
                    .perform(MockMvcRequestBuilders.multipart("/estoque/upload").file(file))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.content().string(expectedUrl));

            Mockito.verify(EstoqueApiTest.this.cloudinaryService)
                    .uploadImage(ArgumentMatchers.any());
        }

        @Test
        @DisplayName("Deve retornar erro quando upload falha")
        void deveRetornarErroQuandoUploadFalha() throws Exception {
            // Arrange
            MockMultipartFile file = new MockMultipartFile("file", "test.jpg",
                    MediaType.IMAGE_JPEG_VALUE, "fake image content".getBytes());

            Mockito.when(EstoqueApiTest.this.cloudinaryService.uploadImage(ArgumentMatchers.any()))
                    .thenThrow(new ImageUploadException("Erro no upload", new RuntimeException()));

            // Act & Assert
            EstoqueApiTest.this.mockMvc
                    .perform(MockMvcRequestBuilders.multipart("/estoque/upload").file(file))
                    .andExpect(MockMvcResultMatchers.status().isInternalServerError())
                    .andExpect(MockMvcResultMatchers.content()
                            .string(Matchers.containsString("Erro ao fazer upload da imagem")));
        }
    }

    @Nested
    @DisplayName("Testes de Integração Completa")
    class TestesIntegracaoCompleta {

        @Test
        @DisplayName("Deve executar fluxo completo de CRUD")
        void deveExecutarFluxoCompletoDeCrud() throws Exception {
            // Arrange
            Long produtoId = 1L;

            // Configurar mocks para fluxo completo
            Mockito.when(EstoqueApiTest.this.estoqueService.buscarPorId(produtoId))
                    .thenReturn(EstoqueApiTest.this.estoqueResponseDto);
            Mockito.when(EstoqueApiTest.this.estoqueService
                    .atualizar(ArgumentMatchers.eq(produtoId), ArgumentMatchers.any()))
                    .thenReturn(EstoqueApiTest.this.estoqueResponseDto);
            Mockito.doNothing().when(EstoqueApiTest.this.estoqueService).excluir(produtoId);

            // Act & Assert - Buscar produto
            EstoqueApiTest.this.mockMvc.perform(MockMvcRequestBuilders.get("/estoque/" + produtoId))
                    .andExpect(MockMvcResultMatchers.status().isOk());

            // Act & Assert - Atualizar produto
            EstoqueApiTest.this.mockMvc
                    .perform(MockMvcRequestBuilders.put("/estoque/" + produtoId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(EstoqueApiTest.this.objectMapper
                                    .writeValueAsString(EstoqueApiTest.this.estoqueRequestDto)))
                    .andExpect(MockMvcResultMatchers.status().isOk());

            // Act & Assert - Excluir produto
            EstoqueApiTest.this.mockMvc
                    .perform(MockMvcRequestBuilders.delete("/estoque/" + produtoId))
                    .andExpect(MockMvcResultMatchers.status().isNoContent());

            // Verificar interações
            Mockito.verify(EstoqueApiTest.this.estoqueService).buscarPorId(produtoId);
            Mockito.verify(EstoqueApiTest.this.estoqueService)
                    .atualizar(ArgumentMatchers.eq(produtoId), ArgumentMatchers.any());
            Mockito.verify(EstoqueApiTest.this.estoqueService).excluir(produtoId);
        }
    }
}
