package com.rsdesenvolvimento.pedido_service.services;

import com.rsdesenvolvimento.pedido_service.core.ports.UsuarioPort;
import com.rsdesenvolvimento.pedido_service.modelo.dtos.PedidoRequesteDto;
import com.rsdesenvolvimento.pedido_service.modelo.dtos.PedidoResponseDto;
import com.rsdesenvolvimento.pedido_service.modelo.entidades.Pedido;
import com.rsdesenvolvimento.pedido_service.modelo.entidades.Usuario;
import com.rsdesenvolvimento.pedido_service.modelo.enums.StatusEnum;
import com.rsdesenvolvimento.pedido_service.modelo.mappers.PedidoMapper;
import com.rsdesenvolvimento.pedido_service.repositorios.PedidoRepository;
import java.util.Collections;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

@ExtendWith(MockitoExtension.class)
public class PedidoServiceTest {


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

    @InjectMocks
    private PedidoService pedidoService;

    @Test
    void dadaEntidade_Existente_Entao_Deve_CriarPedido_ComSucesso() {

        // ARRANGE
        PedidoRequesteDto requestDTO = new PedidoRequesteDto();
        requestDTO.setItens(Collections.emptyList());

        Usuario usuario = new Usuario("1L", "Xpto", "xpto@gmail.com");
        Pedido pedidoMapeado = new Pedido();
        Pedido pedidoSalvo = new Pedido();
        pedidoSalvo.setId(100L);

        PedidoResponseDto responseDto = new PedidoResponseDto();
        responseDto.setId(100L);

        Mockito.when(this.usuarioPort.buscarUsuario()).thenReturn(usuario);
        Mockito.doNothing().when(this.estoqueService).validarEstoque(ArgumentMatchers.any());
        Mockito.when(this.pedidoMapper.paraEntidade(requestDTO)).thenReturn(pedidoMapeado);
        Mockito.when(this.pedidoRepository.save(ArgumentMatchers.any(Pedido.class)))
                .thenReturn(pedidoSalvo);
        Mockito.doNothing().when(this.notificacaoService).enviarNotificacao(pedidoSalvo);
        Mockito.when(this.pedidoMapper.paraDto(pedidoSalvo)).thenReturn(responseDto);

        // ACT
        PedidoResponseDto resultado = this.pedidoService.criarPedido(requestDTO);

        // ASSERT
        Assertions.assertNotNull(resultado);
        Assertions.assertEquals(responseDto.getId(), resultado.getId());

        Mockito.verify(this.usuarioPort, Mockito.times(1)).buscarUsuario();
        Mockito.verify(this.estoqueService, Mockito.times(1)).validarEstoque(requestDTO.getItens());
        Mockito.verify(this.pedidoRepository, Mockito.times(1))
                .save(ArgumentMatchers.any(Pedido.class));
        Mockito.verify(this.notificacaoService, Mockito.times(1)).enviarNotificacao(pedidoSalvo);
        Mockito.verify(this.pedidoMapper, Mockito.times(1)).paraDto(pedidoSalvo);
    }


    @Test
    void deveLancar_Exception_AO_CriarPedido_Se_A_ValidacaoDeEstoque_Falhar() {
        // ARRANGE
        PedidoRequesteDto requestDTO = new PedidoRequesteDto();
        Usuario usuario = new Usuario("1L", "Xpto", "xpto@gmail.com");

        Mockito.when(this.usuarioPort.buscarUsuario()).thenReturn(usuario);
        Mockito.doThrow(new ResponseStatusException(HttpStatus.BAD_REQUEST, "Estoque insuficiente"))
                .when(this.estoqueService).validarEstoque(ArgumentMatchers.any());

        // ACT & ASSERT

        ResponseStatusException exception =
                Assertions.assertThrows(ResponseStatusException.class, () -> {
                    this.pedidoService.criarPedido(requestDTO);
                });
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        Assertions.assertEquals("Estoque insuficiente", exception.getReason());

        Mockito.verify(this.pedidoRepository, Mockito.never()).save(ArgumentMatchers.any());
        Mockito.verify(this.notificacaoService, Mockito.never())
                .enviarNotificacao(ArgumentMatchers.any());
    }

    @Test
    void deve_AtualizarStatus_QuandoPagamento_Concluido_ComSucesso() {
        Long idPedido = 1L;
        String novoStatus = "FINALIZADO";
        Pedido pedidoExistente = new Pedido();
        pedidoExistente.setId(idPedido);
        pedidoExistente.setStatus(StatusEnum.PENDENTE);

        Mockito.when(this.pedidoRepository.findById(idPedido))
                .thenReturn(Optional.of(pedidoExistente));
        ArgumentCaptor<Pedido> captor = ArgumentCaptor.forClass(Pedido.class);

        // ACT
        this.pedidoService.statusPagamento(idPedido, novoStatus);

        // ASSERT

        Mockito.verify(this.pedidoRepository, Mockito.times(1)).save(captor.capture());
        Pedido pedidoAtualizado = captor.getValue();

        Assertions.assertEquals(StatusEnum.FINALIZADO, pedidoAtualizado.getStatus());

    }

    void deve_LancarException_QuandoPedido_NaoEncontrado() {
        // ARRANGE
        Long pedidoIdInexistente = 30L;

        Mockito.when(this.pedidoRepository.findById(pedidoIdInexistente))
                .thenReturn(Optional.empty());

        // ACT & ASSERT
        ResponseStatusException exception =
                Assertions.assertThrows(ResponseStatusException.class, () -> {
                    this.pedidoService.statusPagamento(pedidoIdInexistente, "FINALIZADO");
                });

        Assertions.assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        Assertions.assertEquals("Pedido não encontrado", exception.getReason());

        Mockito.verify(this.pedidoRepository, Mockito.never()).save(ArgumentMatchers.any());
    }

    @Test
    void deveLancarExcecaoQuandoStatusInvalido() {
        // ARRANGE
        Long pedidoId = 1L;
        String statusInvalido = "STATUS_QUE_NAO_EXISTE";
        Pedido pedidoExistente = new Pedido();
        pedidoExistente.setId(pedidoId);

        Mockito.when(this.pedidoRepository.findById(pedidoId))
                .thenReturn(Optional.of(pedidoExistente));

        // ACT & ASSERT
        ResponseStatusException exception =
                Assertions.assertThrows(ResponseStatusException.class, () -> {
                    this.pedidoService.statusPagamento(pedidoId, statusInvalido);
                });

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        Assertions.assertEquals("Status inválido", exception.getReason());
        Mockito.verify(this.pedidoRepository, Mockito.never()).save(ArgumentMatchers.any());
    }

}
