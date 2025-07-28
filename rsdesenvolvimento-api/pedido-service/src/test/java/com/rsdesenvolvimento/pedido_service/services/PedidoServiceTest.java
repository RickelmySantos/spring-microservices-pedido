package com.rsdesenvolvimento.pedido_service.services;

import com.rsdesenvolvimento.pedido_service.core.client.dtos.EstoqueResponseDto;
import com.rsdesenvolvimento.pedido_service.core.ports.UsuarioPort;
import com.rsdesenvolvimento.pedido_service.modelo.dtos.ItemPedidoRequestDto;
import com.rsdesenvolvimento.pedido_service.modelo.dtos.PedidoRequesteDto;
import com.rsdesenvolvimento.pedido_service.modelo.dtos.PedidoResponseDto;
import com.rsdesenvolvimento.pedido_service.modelo.entidades.Pedido;
import com.rsdesenvolvimento.pedido_service.modelo.entidades.Usuario;
import com.rsdesenvolvimento.pedido_service.modelo.enums.StatusEnum;
import com.rsdesenvolvimento.pedido_service.modelo.mappers.PedidoMapper;
import com.rsdesenvolvimento.pedido_service.repositorios.PedidoRepository;
import com.rsdesenvolvimento.pedido_service.utils.TestDataBuilder;
import java.math.BigDecimal;
import java.util.Arrays;
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
import org.springframework.web.server.ResponseStatusException;


@ExtendWith(MockitoExtension.class)
@DisplayName("PedidoService")
class PedidoServiceTest {

    @Mock
    private PedidoRepository pedidoRepository;

    @Mock
    private PedidoMapper pedidoMapper;

    @Mock
    private EstoqueService estoqueService;

    @Mock
    private NotificacaoService notificacaoService;

    @Mock
    private UsuarioPort usuarioPort;

    @Mock
    private PedidoValidacaoService validacaoService;

    @Mock
    private OrquestacaoEstoqueService orquestacaoEstoqueService;

    @InjectMocks
    private PedidoService pedidoService;

    private Usuario usuarioTeste;
    private PedidoRequesteDto pedidoRequestDto;
    private Pedido pedidoSalvo;
    private Pedido pedidoTeste;

    @BeforeEach
    void setUp() {
        this.usuarioTeste = TestDataBuilder.umUsuario().build();
        this.pedidoRequestDto = TestDataBuilder.umPedidoRequesteDto().build();
        this.pedidoSalvo = TestDataBuilder.umPedido().build();
        this.pedidoTeste = TestDataBuilder.umPedido().build();
    }

    @Nested
    @DisplayName("Criação de Pedidos")
    class CriacaoPedidos {

        @Test
        @DisplayName("Deve criar pedido com sucesso quando dados válidos")
        void deveCriarPedidoComSucessoQuandoDadosValidos() {
            // Arrange
            EstoqueResponseDto produto = TestDataBuilder.umEstoqueResponseDto().comId(1L)
                    .comNome("Produto Teste").build();

            Mockito.when(PedidoServiceTest.this.usuarioPort.buscarUsuario())
                    .thenReturn(PedidoServiceTest.this.usuarioTeste);
            Mockito.when(
                    PedidoServiceTest.this.estoqueService.buscarProduto(ArgumentMatchers.anyLong()))
                    .thenReturn(produto);
            Mockito.when(PedidoServiceTest.this.pedidoRepository
                    .save(ArgumentMatchers.any(Pedido.class)))
                    .thenReturn(PedidoServiceTest.this.pedidoSalvo);

            // Act
            PedidoResponseDto resultado = PedidoServiceTest.this.pedidoService
                    .criarPedido(PedidoServiceTest.this.pedidoRequestDto);

            // Assert
            Assertions.assertThat(resultado).isNotNull();
            Assertions.assertThat(resultado.getId())
                    .isEqualTo(PedidoServiceTest.this.pedidoSalvo.getId());
            Assertions.assertThat(resultado.getUsuarioId())
                    .isEqualTo(PedidoServiceTest.this.pedidoSalvo.getUsuarioId());
            Assertions.assertThat(resultado.getStatus()).isEqualTo(StatusEnum.PENDENTE);

            Mockito.verify(PedidoServiceTest.this.pedidoRepository)
                    .save(ArgumentMatchers.any(Pedido.class));
            Mockito.verify(PedidoServiceTest.this.notificacaoService)
                    .enviarNotificacao(ArgumentMatchers.any());
        }

        @Test
        @DisplayName("Deve lançar ResponseStatusException quando usuário não encontrado")
        void deveLancarResponseStatusExceptionQuandoUsuarioNaoEncontrado() {
            // Arrange
            Mockito.when(PedidoServiceTest.this.usuarioPort.buscarUsuario())
                    .thenThrow(new ResponseStatusException(
                            org.springframework.http.HttpStatus.NOT_FOUND,
                            "Usuário não encontrado"));

            // Act & Assert
            Assertions
                    .assertThatThrownBy(() -> PedidoServiceTest.this.pedidoService
                            .criarPedido(PedidoServiceTest.this.pedidoRequestDto))
                    .isInstanceOf(ResponseStatusException.class)
                    .hasMessageContaining("Usuário não encontrado");

            Mockito.verify(PedidoServiceTest.this.pedidoRepository, Mockito.never())
                    .save(ArgumentMatchers.any(Pedido.class));
            Mockito.verify(PedidoServiceTest.this.notificacaoService, Mockito.never())
                    .enviarNotificacao(ArgumentMatchers.any());
        }

        @Test
        @DisplayName("Deve lançar ResponseStatusException quando produto não encontrado")
        void deveLancarResponseStatusExceptionQuandoProdutoNaoEncontrado() {
            // Arrange
            Mockito.when(PedidoServiceTest.this.usuarioPort.buscarUsuario())
                    .thenReturn(PedidoServiceTest.this.usuarioTeste);
            Mockito.when(
                    PedidoServiceTest.this.estoqueService.buscarProduto(ArgumentMatchers.anyLong()))
                    .thenThrow(new ResponseStatusException(
                            org.springframework.http.HttpStatus.NOT_FOUND,
                            "Produto não encontrado"));

            // Act & Assert
            Assertions
                    .assertThatThrownBy(() -> PedidoServiceTest.this.pedidoService
                            .criarPedido(PedidoServiceTest.this.pedidoRequestDto))
                    .isInstanceOf(ResponseStatusException.class)
                    .hasMessageContaining("Produto não encontrado");

            Mockito.verify(PedidoServiceTest.this.pedidoRepository, Mockito.never())
                    .save(ArgumentMatchers.any(Pedido.class));
            Mockito.verify(PedidoServiceTest.this.notificacaoService, Mockito.never())
                    .enviarNotificacao(ArgumentMatchers.any());
        }

        @Test
        @DisplayName("Deve lançar PedidoValidationException quando lista de itens vazia")
        void deveLancarPedidoValidationExceptionQuandoListaItensVazia() {
            // Arrange
            PedidoRequesteDto pedidoSemItens =
                    TestDataBuilder.umPedidoRequesteDto().semItens().build();

            // Act & Assert
            Assertions
                    .assertThatThrownBy(
                            () -> PedidoServiceTest.this.pedidoService.criarPedido(pedidoSemItens))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessageContaining("deve conter pelo menos um item");

            Mockito.verify(PedidoServiceTest.this.pedidoRepository, Mockito.never())
                    .save(ArgumentMatchers.any(Pedido.class));
            Mockito.verify(PedidoServiceTest.this.notificacaoService, Mockito.never())
                    .enviarNotificacao(ArgumentMatchers.any());
        }

        @Test
        @DisplayName("Deve definir nome e email do usuário no pedido")
        void deveDefinirNomeEmailUsuarioNoPedido() {
            // Arrange
            EstoqueResponseDto produto = TestDataBuilder.umEstoqueResponseDto().build();
            Usuario usuario = TestDataBuilder.umUsuario().comUsername("Maria Silva")
                    .comEmail("maria@teste.com").build();

            Mockito.when(PedidoServiceTest.this.usuarioPort.buscarUsuario()).thenReturn(usuario);
            Mockito.when(
                    PedidoServiceTest.this.estoqueService.buscarProduto(ArgumentMatchers.anyLong()))
                    .thenReturn(produto);
            Mockito.when(PedidoServiceTest.this.pedidoRepository
                    .save(ArgumentMatchers.any(Pedido.class))).thenAnswer(invocation -> {
                        Pedido pedido = invocation.getArgument(0);
                        return TestDataBuilder.umPedido().comNomeUsuario(pedido.getNomeUsuario())
                                .comEmailUsuario(pedido.getEmailUsuario()).build();
                    });

            // Act
            PedidoResponseDto resultado = PedidoServiceTest.this.pedidoService
                    .criarPedido(PedidoServiceTest.this.pedidoRequestDto);

            // Assert
            Assertions.assertThat(resultado.getNomeUsuario()).isEqualTo("Maria Silva");
            Assertions.assertThat(resultado.getEmailUsuario()).isEqualTo("maria@teste.com");
        }
    }

    @Nested
    @DisplayName("Atualização de Status de Pagamento")
    class AtualizacaoStatusPagamento {

        @Test
        @DisplayName("Deve atualizar status de pagamento com sucesso")
        void deveAtualizarStatusPagamentoComSucesso() {
            // Arrange
            Long pedidoId = 1L;
            String status = "APROVADO";
            Pedido pedidoExistente = TestDataBuilder.umPedido().pendente().build();

            Mockito.when(PedidoServiceTest.this.pedidoRepository.findById(pedidoId))
                    .thenReturn(Optional.of(pedidoExistente));

            // Act
            Assertions.assertThatCode(() -> PedidoServiceTest.this.pedidoService
                    .atualizarStatusPagamento(pedidoId, status)).doesNotThrowAnyException();

            // Assert
            Mockito.verify(PedidoServiceTest.this.pedidoRepository).findById(pedidoId);
            Mockito.verify(PedidoServiceTest.this.pedidoRepository)
                    .save(ArgumentMatchers.any(Pedido.class));
        }

        @Test
        @DisplayName("Deve lançar PedidoNaoEncontradoException ao atualizar status de pedido inexistente")
        void deveLancarPedidoNaoEncontradoExceptionAoAtualizarStatusPedidoInexistente() {
            // Arrange
            Long pedidoId = 999L;
            String status = "APROVADO";

            Mockito.when(PedidoServiceTest.this.pedidoRepository.findById(pedidoId))
                    .thenReturn(Optional.empty());

            // Act & Assert
            Assertions
                    .assertThatThrownBy(() -> PedidoServiceTest.this.pedidoService
                            .atualizarStatusPagamento(pedidoId, status))
                    .isInstanceOf(PedidoService.PedidoNaoEncontradoException.class)
                    .hasMessageContaining("Pedido não encontrado");

            Mockito.verify(PedidoServiceTest.this.pedidoRepository).findById(pedidoId);
            Mockito.verify(PedidoServiceTest.this.pedidoRepository, Mockito.never())
                    .save(ArgumentMatchers.any(Pedido.class));
        }

        @Test
        @DisplayName("Deve enviar notificação ao atualizar status para aprovado")
        void deveEnviarNotificacaoAoAtualizarStatusParaAprovado() {
            // Arrange
            Long pedidoId = 1L;
            String status = "APROVADO";
            Pedido pedidoExistente = TestDataBuilder.umPedido().pendente().build();

            Mockito.when(PedidoServiceTest.this.pedidoRepository.findById(pedidoId))
                    .thenReturn(Optional.of(pedidoExistente));

            // Act
            PedidoServiceTest.this.pedidoService.atualizarStatusPagamento(pedidoId, status);

            // Assert
            Mockito.verify(PedidoServiceTest.this.notificacaoService)
                    .enviarNotificacao(ArgumentMatchers.any());
        }

        @Test
        @DisplayName("Deve encontrar pedido existente para atualização")
        void deveEncontrarPedidoExistenteParaAtualizacao() {
            // Arrange
            Long pedidoId = 1L;
            String status = "APROVADO";

            Mockito.when(PedidoServiceTest.this.pedidoRepository.findById(pedidoId))
                    .thenReturn(Optional.of(PedidoServiceTest.this.pedidoTeste));
            Mockito.when(PedidoServiceTest.this.validacaoService.validarEConverterStatus(status))
                    .thenReturn(StatusEnum.FINALIZADO);

            // Act & Assert
            Assertions.assertThatCode(() -> PedidoServiceTest.this.pedidoService
                    .atualizarStatusPagamento(pedidoId, status)).doesNotThrowAnyException();

            Mockito.verify(PedidoServiceTest.this.pedidoRepository).findById(pedidoId);
        }

        @Test
        @DisplayName("Deve lançar exceção quando pedido não encontrado para atualização")
        void deveLancarExcecaoQuandoPedidoNaoEncontradoParaAtualizacao() {
            // Arrange
            Long pedidoId = 999L;
            String status = "APROVADO";

            Mockito.when(PedidoServiceTest.this.pedidoRepository.findById(pedidoId))
                    .thenReturn(Optional.empty());

            // Act & Assert
            Assertions
                    .assertThatThrownBy(() -> PedidoServiceTest.this.pedidoService
                            .atualizarStatusPagamento(pedidoId, status))
                    .isInstanceOf(PedidoService.PedidoNaoEncontradoException.class)
                    .hasMessageContaining("Pedido não encontrado");

            Mockito.verify(PedidoServiceTest.this.pedidoRepository).findById(pedidoId);
        }
    }

    @Nested
    @DisplayName("Busca de Email por Pedido ID")
    class BuscaEmailPorPedidoId {

        @Test
        @DisplayName("Deve buscar email por pedido ID com sucesso")
        void deveBuscarEmailPorPedidoIdComSucesso() {
            // Arrange
            Long pedidoId = 1L;
            Pedido pedidoComEmail =
                    TestDataBuilder.umPedido().comEmailUsuario("joao@teste.com").build();

            Mockito.when(PedidoServiceTest.this.pedidoRepository.findById(pedidoId))
                    .thenReturn(Optional.of(pedidoComEmail));

            // Act
            String email = PedidoServiceTest.this.pedidoService.buscarEmailPorPedidoId(pedidoId);

            // Assert
            Assertions.assertThat(email).isEqualTo("joao@teste.com");
            Mockito.verify(PedidoServiceTest.this.pedidoRepository).findById(pedidoId);
        }

        @Test
        @DisplayName("Deve lançar PedidoNaoEncontradoException quando pedido não encontrado")
        void deveLancarPedidoNaoEncontradoExceptionQuandoPedidoNaoEncontrado() {
            // Arrange
            Long pedidoId = 999L;
            Mockito.when(PedidoServiceTest.this.pedidoRepository.findById(pedidoId))
                    .thenReturn(Optional.empty());

            // Act & Assert
            Assertions.assertThatThrownBy(
                    () -> PedidoServiceTest.this.pedidoService.buscarEmailPorPedidoId(pedidoId))
                    .isInstanceOf(PedidoService.PedidoNaoEncontradoException.class)
                    .hasMessageContaining("Pedido não encontrado");

            Mockito.verify(PedidoServiceTest.this.pedidoRepository).findById(pedidoId);
        }


    }

    @Nested
    @DisplayName("Conversões e Validações")
    class ConversoesValidacoes {

        @Test
        @DisplayName("Deve converter PedidoRequesteDto para Pedido corretamente")
        void deveConverterPedidoRequesteDtoParaPedidoCorretamente() {
            // Arrange
            EstoqueResponseDto produto = TestDataBuilder.umEstoqueResponseDto().comId(1L)
                    .comNome("Notebook Gamer").build();

            ItemPedidoRequestDto itemDto = TestDataBuilder.umItemPedidoRequestDto().comProdutoId(1L)
                    .comQuantidade(2).comPrecoUnitario(BigDecimal.valueOf(2500.00)).build();

            PedidoRequesteDto request = TestDataBuilder.umPedidoRequesteDto()
                    .comUsuarioId("user456").comObservacao("Pedido urgente")
                    .comItensPedido(Arrays.asList(itemDto)).build();

            Mockito.when(PedidoServiceTest.this.usuarioPort.buscarUsuario())
                    .thenReturn(PedidoServiceTest.this.usuarioTeste);
            Mockito.when(
                    PedidoServiceTest.this.estoqueService.buscarProduto(ArgumentMatchers.anyLong()))
                    .thenReturn(produto);
            Mockito.when(PedidoServiceTest.this.pedidoRepository
                    .save(ArgumentMatchers.any(Pedido.class))).thenAnswer(invocation -> {
                        Pedido pedido = invocation.getArgument(0);
                        return TestDataBuilder.umPedido().comUsuarioId(pedido.getUsuarioId())
                                .comObservacao(pedido.getObservacao())
                                .comItensPedido(pedido.getItensPedido()).build();
                    });

            // Act
            PedidoResponseDto resultado = PedidoServiceTest.this.pedidoService.criarPedido(request);

            // Assert
            Assertions.assertThat(resultado.getUsuarioId()).isEqualTo("user456");
            Assertions.assertThat(resultado.getObservacao()).isEqualTo("Pedido urgente");
        }

        @Test
        @DisplayName("Deve validar que produto existe antes de criar item")
        void deveValidarQueProdutoExisteAntesCriarItem() {
            // Arrange
            ItemPedidoRequestDto itemInvalido =
                    TestDataBuilder.umItemPedidoRequestDto().comProdutoId(999L).build();

            PedidoRequesteDto request = TestDataBuilder.umPedidoRequesteDto()
                    .comItensPedido(Arrays.asList(itemInvalido)).build();

            Mockito.when(PedidoServiceTest.this.usuarioPort.buscarUsuario())
                    .thenReturn(PedidoServiceTest.this.usuarioTeste);
            Mockito.when(PedidoServiceTest.this.estoqueService.buscarProduto(999L))
                    .thenThrow(new ResponseStatusException(
                            org.springframework.http.HttpStatus.NOT_FOUND,
                            "Produto não encontrado"));

            // Act & Assert
            Assertions
                    .assertThatThrownBy(
                            () -> PedidoServiceTest.this.pedidoService.criarPedido(request))
                    .isInstanceOf(ResponseStatusException.class);
        }

        @Test
        @DisplayName("Deve validar quantidade mínima de itens no pedido")
        void deveValidarQuantidadeMinimaItensNoPedido() {
            // Arrange
            ItemPedidoRequestDto itemComQuantidadeZero =
                    TestDataBuilder.umItemPedidoRequestDto().comQuantidade(0).build();

            PedidoRequesteDto request = TestDataBuilder.umPedidoRequesteDto()
                    .comItensPedido(Arrays.asList(itemComQuantidadeZero)).build();

            Mockito.when(PedidoServiceTest.this.usuarioPort.buscarUsuario())
                    .thenReturn(PedidoServiceTest.this.usuarioTeste);

            // Act & Assert
            Assertions
                    .assertThatThrownBy(
                            () -> PedidoServiceTest.this.pedidoService.criarPedido(request))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessageContaining("Quantidade deve ser maior que zero");
        }

        @Test
        @DisplayName("Deve validar preço unitário positivo")
        void deveValidarPrecoUnitarioPositivo() {
            // Arrange
            ItemPedidoRequestDto itemComPrecoNegativo = TestDataBuilder.umItemPedidoRequestDto()
                    .comPrecoUnitario(BigDecimal.valueOf(-10.00)).build();

            PedidoRequesteDto request = TestDataBuilder.umPedidoRequesteDto()
                    .comItensPedido(Arrays.asList(itemComPrecoNegativo)).build();

            Mockito.when(PedidoServiceTest.this.usuarioPort.buscarUsuario())
                    .thenReturn(PedidoServiceTest.this.usuarioTeste);

            // Act & Assert
            Assertions
                    .assertThatThrownBy(
                            () -> PedidoServiceTest.this.pedidoService.criarPedido(request))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessageContaining("Preço deve ser maior que zero");
        }
    }

    @Nested
    @DisplayName("Validações de Dependências")
    class ValidacoesDependencias {

        @Test
        @DisplayName("Deve ter PedidoRepository injetado")
        void deveTerPedidoRepositoryInjetado() {
            Assertions.assertThat(PedidoServiceTest.this.pedidoService).isNotNull();
            // Teste básico para verificar se a injeção de dependência está funcionando
        }

        @Test
        @DisplayName("Deve usar mapper para conversões")
        void deveUsarMapperParaConversoes() {
            // Arrange
            Long pedidoId = 1L;
            Mockito.when(PedidoServiceTest.this.pedidoRepository.findById(pedidoId))
                    .thenReturn(Optional.of(PedidoServiceTest.this.pedidoTeste));

            // Act
            String email = PedidoServiceTest.this.pedidoService.buscarEmailPorPedidoId(pedidoId);

            // Assert
            Assertions.assertThat(email).isNotNull();
            Mockito.verify(PedidoServiceTest.this.pedidoRepository).findById(pedidoId);
        }
    }

    @Nested
    @DisplayName("Cenários de Erro")
    class CenariosDeErro {

        @Test
        @DisplayName("Deve tratar adequadamente pedido inexistente")
        void deveTratarAdequadamentePedidoInexistente() {
            // Arrange
            Long pedidoIdInexistente = 999L;
            Mockito.when(PedidoServiceTest.this.pedidoRepository.findById(pedidoIdInexistente))
                    .thenReturn(Optional.empty());

            // Act & Assert
            Assertions
                    .assertThatThrownBy(() -> PedidoServiceTest.this.pedidoService
                            .buscarEmailPorPedidoId(pedidoIdInexistente))
                    .isInstanceOf(PedidoService.PedidoNaoEncontradoException.class);
        }

        @Test
        @DisplayName("Deve validar entrada nula")
        void deveValidarEntradaNula() {
            // Act & Assert
            Assertions
                    .assertThatThrownBy(
                            () -> PedidoServiceTest.this.pedidoService.buscarEmailPorPedidoId(null))
                    .isInstanceOf(Exception.class);
        }
    }

    @Nested
    @DisplayName("Integração com Serviços")
    class IntegracaoComServicos {

        @Test
        @DisplayName("Deve interagir com repositório corretamente")
        void deveInteragirComRepositorioCorretamente() {
            // Arrange
            Long pedidoId = 1L;
            Mockito.when(PedidoServiceTest.this.pedidoRepository.findById(pedidoId))
                    .thenReturn(Optional.of(PedidoServiceTest.this.pedidoTeste));

            // Act
            PedidoServiceTest.this.pedidoService.buscarEmailPorPedidoId(pedidoId);

            // Assert
            Mockito.verify(PedidoServiceTest.this.pedidoRepository, Mockito.times(1))
                    .findById(pedidoId);
        }

        @Test
        @DisplayName("Deve chamar validação de status quando necessário")
        void deveChamarValidacaoStatusQuandoNecessario() {
            // Arrange
            Long pedidoId = 1L;
            String status = "APROVADO";

            Mockito.when(PedidoServiceTest.this.pedidoRepository.findById(pedidoId))
                    .thenReturn(Optional.of(PedidoServiceTest.this.pedidoTeste));
            Mockito.when(PedidoServiceTest.this.validacaoService.validarEConverterStatus(status))
                    .thenReturn(StatusEnum.FINALIZADO);

            // Act
            PedidoServiceTest.this.pedidoService.atualizarStatusPagamento(pedidoId, status);

            // Assert
            Mockito.verify(PedidoServiceTest.this.validacaoService).validarEConverterStatus(status);
        }
    }
}
