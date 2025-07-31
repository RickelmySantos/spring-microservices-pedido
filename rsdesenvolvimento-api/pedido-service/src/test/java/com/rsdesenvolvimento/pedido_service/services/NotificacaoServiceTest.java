package com.rsdesenvolvimento.pedido_service.services;

import com.rsdesenvolvimento.pedido_service.core.ports.NotificacaoPort;
import com.rsdesenvolvimento.pedido_service.modelo.entidades.Pedido;
import com.rsdesenvolvimento.pedido_service.utils.TestDataBuilder;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("NotificacaoService")
class NotificacaoServiceTest {

    @Mock
    private NotificacaoPort notificacaoPort;

    @InjectMocks
    private NotificacaoService notificacaoService;

    private Pedido pedidoTeste;

    @BeforeEach
    void setUp() {
        this.pedidoTeste =
                TestDataBuilder.umPedido().comId(123L).comEmailUsuario("usuario@teste.com").build();
    }

    @Nested
    @DisplayName("Envio de Notificação")
    class EnvioNotificacao {

        @Test
        @DisplayName("Deve enviar notificação com sucesso")
        void deveEnviarNotificacaoComSucesso() {
            // Act
            NotificacaoServiceTest.this.notificacaoService
                    .enviarNotificacao(NotificacaoServiceTest.this.pedidoTeste);

            // Assert
            Mockito.verify(NotificacaoServiceTest.this.notificacaoPort, Mockito.times(1))
                    .enviarNotificacao(ArgumentMatchers.anyString());
        }

        @Test
        @DisplayName("Deve criar mensagem no formato correto")
        void deveCriarMensagemNoFormatoCorreto() {
            // Arrange
            ArgumentCaptor<String> mensagemCaptor = ArgumentCaptor.forClass(String.class);

            // Act
            NotificacaoServiceTest.this.notificacaoService
                    .enviarNotificacao(NotificacaoServiceTest.this.pedidoTeste);

            // Assert
            Mockito.verify(NotificacaoServiceTest.this.notificacaoPort)
                    .enviarNotificacao(mensagemCaptor.capture());

            String mensagem = mensagemCaptor.getValue();
            Assertions.assertThat(mensagem).isEqualTo("PEDIDO_CRIADO:123:usuario@teste.com");
        }

        @Test
        @DisplayName("Deve incluir ID do pedido na mensagem")
        void deveIncluirIdDoPedidoNaMensagem() {
            // Arrange
            Pedido pedidoComIdEspecifico = TestDataBuilder.umPedido().comId(999L)
                    .comEmailUsuario("test@example.com").build();

            ArgumentCaptor<String> mensagemCaptor = ArgumentCaptor.forClass(String.class);

            // Act
            NotificacaoServiceTest.this.notificacaoService.enviarNotificacao(pedidoComIdEspecifico);

            // Assert
            Mockito.verify(NotificacaoServiceTest.this.notificacaoPort)
                    .enviarNotificacao(mensagemCaptor.capture());

            String mensagem = mensagemCaptor.getValue();
            Assertions.assertThat(mensagem).contains("999");
            Assertions.assertThat(mensagem).startsWith("PEDIDO_CRIADO:");
        }

        @Test
        @DisplayName("Deve incluir email do usuário na mensagem")
        void deveIncluirEmailDoUsuarioNaMensagem() {
            // Arrange
            Pedido pedidoComEmailEspecifico = TestDataBuilder.umPedido().comId(456L)
                    .comEmailUsuario("email.especifico@teste.org").build();

            ArgumentCaptor<String> mensagemCaptor = ArgumentCaptor.forClass(String.class);

            // Act
            NotificacaoServiceTest.this.notificacaoService
                    .enviarNotificacao(pedidoComEmailEspecifico);

            // Assert
            Mockito.verify(NotificacaoServiceTest.this.notificacaoPort)
                    .enviarNotificacao(mensagemCaptor.capture());

            String mensagem = mensagemCaptor.getValue();
            Assertions.assertThat(mensagem).contains("email.especifico@teste.org");
            Assertions.assertThat(mensagem).endsWith("email.especifico@teste.org");
        }

        @Test
        @DisplayName("Deve manter formato da mensagem com dois pontos como separadores")
        void deveManterFormatoDaMensagemComDoisPontosComoSeparadores() {
            // Arrange
            ArgumentCaptor<String> mensagemCaptor = ArgumentCaptor.forClass(String.class);

            // Act
            NotificacaoServiceTest.this.notificacaoService
                    .enviarNotificacao(NotificacaoServiceTest.this.pedidoTeste);

            // Assert
            Mockito.verify(NotificacaoServiceTest.this.notificacaoPort)
                    .enviarNotificacao(mensagemCaptor.capture());

            String mensagem = mensagemCaptor.getValue();
            String[] partes = mensagem.split(":");

            Assertions.assertThat(partes).hasSize(3);
            Assertions.assertThat(partes[0]).isEqualTo("PEDIDO_CRIADO");
            Assertions.assertThat(partes[1]).isEqualTo("123");
            Assertions.assertThat(partes[2]).isEqualTo("usuario@teste.com");
        }
    }

    @Nested
    @DisplayName("Tratamento de Casos Especiais")
    class TratamentoCasosEspeciais {

        @Test
        @DisplayName("Deve lidar com ID nulo")
        void deveLidarComIdNulo() {
            // Arrange
            Pedido pedidoSemId = TestDataBuilder.umPedido().comId(null)
                    .comEmailUsuario("usuario@teste.com").build();

            ArgumentCaptor<String> mensagemCaptor = ArgumentCaptor.forClass(String.class);

            // Act
            NotificacaoServiceTest.this.notificacaoService.enviarNotificacao(pedidoSemId);

            // Assert
            Mockito.verify(NotificacaoServiceTest.this.notificacaoPort)
                    .enviarNotificacao(mensagemCaptor.capture());

            String mensagem = mensagemCaptor.getValue();
            Assertions.assertThat(mensagem).isEqualTo("PEDIDO_CRIADO:null:usuario@teste.com");
        }

        @Test
        @DisplayName("Deve lidar com email nulo")
        void deveLidarComEmailNulo() {
            // Arrange
            Pedido pedidoSemEmail =
                    TestDataBuilder.umPedido().comId(123L).comEmailUsuario(null).build();

            ArgumentCaptor<String> mensagemCaptor = ArgumentCaptor.forClass(String.class);

            // Act
            NotificacaoServiceTest.this.notificacaoService.enviarNotificacao(pedidoSemEmail);

            // Assert
            Mockito.verify(NotificacaoServiceTest.this.notificacaoPort)
                    .enviarNotificacao(mensagemCaptor.capture());

            String mensagem = mensagemCaptor.getValue();
            Assertions.assertThat(mensagem).isEqualTo("PEDIDO_CRIADO:123:null");
        }

        @Test
        @DisplayName("Deve lidar com email vazio")
        void deveLidarComEmailVazio() {
            // Arrange
            Pedido pedidoEmailVazio =
                    TestDataBuilder.umPedido().comId(789L).comEmailUsuario("").build();

            ArgumentCaptor<String> mensagemCaptor = ArgumentCaptor.forClass(String.class);

            // Act
            NotificacaoServiceTest.this.notificacaoService.enviarNotificacao(pedidoEmailVazio);

            // Assert
            Mockito.verify(NotificacaoServiceTest.this.notificacaoPort)
                    .enviarNotificacao(mensagemCaptor.capture());

            String mensagem = mensagemCaptor.getValue();
            Assertions.assertThat(mensagem).isEqualTo("PEDIDO_CRIADO:789:");
        }

        @Test
        @DisplayName("Deve lidar com email contendo caracteres especiais")
        void deveLidarComEmailContendoCaracteresEspeciais() {
            // Arrange
            Pedido pedidoEmailEspecial = TestDataBuilder.umPedido().comId(555L)
                    .comEmailUsuario("user+test@domain-name.co.uk").build();

            ArgumentCaptor<String> mensagemCaptor = ArgumentCaptor.forClass(String.class);

            // Act
            NotificacaoServiceTest.this.notificacaoService.enviarNotificacao(pedidoEmailEspecial);

            // Assert
            Mockito.verify(NotificacaoServiceTest.this.notificacaoPort)
                    .enviarNotificacao(mensagemCaptor.capture());

            String mensagem = mensagemCaptor.getValue();
            Assertions.assertThat(mensagem)
                    .isEqualTo("PEDIDO_CRIADO:555:user+test@domain-name.co.uk");
        }

        @Test
        @DisplayName("Deve lançar exceção quando pedido é nulo")
        void deveLancarExcecaoQuandoPedidoNulo() {
            // Act & Assert
            Assertions.assertThatThrownBy(
                    () -> NotificacaoServiceTest.this.notificacaoService.enviarNotificacao(null))
                    .isInstanceOf(NullPointerException.class);

            // Verify que não chamou o port
            Mockito.verify(NotificacaoServiceTest.this.notificacaoPort, Mockito.never())
                    .enviarNotificacao(ArgumentMatchers.anyString());
        }
    }

    @Nested
    @DisplayName("Interação com NotificacaoPort")
    class InteracaoNotificacaoPort {

        @Test
        @DisplayName("Deve chamar NotificacaoPort exatamente uma vez")
        void deveChamarNotificacaoPortExatamenteUmaVez() {
            // Act
            NotificacaoServiceTest.this.notificacaoService
                    .enviarNotificacao(NotificacaoServiceTest.this.pedidoTeste);

            // Assert
            Mockito.verify(NotificacaoServiceTest.this.notificacaoPort, Mockito.times(1))
                    .enviarNotificacao(ArgumentMatchers.anyString());
        }

        @Test
        @DisplayName("Deve propagar exceção do NotificacaoPort")
        void devePropagarExcecaoDoNotificacaoPort() {
            // Arrange
            RuntimeException excecaoSimulada = new RuntimeException("Erro no envio da notificação");
            Mockito.doThrow(excecaoSimulada).when(NotificacaoServiceTest.this.notificacaoPort)
                    .enviarNotificacao(ArgumentMatchers.anyString());

            // Act & Assert
            Assertions
                    .assertThatThrownBy(() -> NotificacaoServiceTest.this.notificacaoService
                            .enviarNotificacao(NotificacaoServiceTest.this.pedidoTeste))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessage("Erro no envio da notificação");
        }

        @Test
        @DisplayName("Deve funcionar independentemente do comportamento do port")
        void deveFuncionarIndependentementeDoComportamentoDoPort() {
            // Arrange - Port não faz nada (comportamento padrão do mock)

            // Act & Assert - Não deve lançar exceção
            Assertions
                    .assertThatCode(() -> NotificacaoServiceTest.this.notificacaoService
                            .enviarNotificacao(NotificacaoServiceTest.this.pedidoTeste))
                    .doesNotThrowAnyException();

            // Verify interação
            Mockito.verify(NotificacaoServiceTest.this.notificacaoPort)
                    .enviarNotificacao(ArgumentMatchers.anyString());
        }
    }

    @Nested
    @DisplayName("Testes de Múltiplas Notificações")
    class TestesMultiplasNotificacoes {

        @Test
        @DisplayName("Deve enviar múltiplas notificações independentemente")
        void deveEnviarMultiplasNotificacoesIndependentemente() {
            // Arrange
            Pedido pedido1 = TestDataBuilder.umPedido().comId(111L)
                    .comEmailUsuario("user1@test.com").build();

            Pedido pedido2 = TestDataBuilder.umPedido().comId(222L)
                    .comEmailUsuario("user2@test.com").build();

            ArgumentCaptor<String> mensagemCaptor = ArgumentCaptor.forClass(String.class);

            // Act
            NotificacaoServiceTest.this.notificacaoService.enviarNotificacao(pedido1);
            NotificacaoServiceTest.this.notificacaoService.enviarNotificacao(pedido2);

            // Assert
            Mockito.verify(NotificacaoServiceTest.this.notificacaoPort, Mockito.times(2))
                    .enviarNotificacao(mensagemCaptor.capture());

            java.util.List<String> mensagensCapturadas = mensagemCaptor.getAllValues();
            Assertions.assertThat(mensagensCapturadas).hasSize(2);
            Assertions.assertThat(mensagensCapturadas.get(0))
                    .isEqualTo("PEDIDO_CRIADO:111:user1@test.com");
            Assertions.assertThat(mensagensCapturadas.get(1))
                    .isEqualTo("PEDIDO_CRIADO:222:user2@test.com");
        }

        @Test
        @DisplayName("Deve continuar enviando mesmo após falha anterior")
        void deveContinuarEnviandoMesmoAposFalhaAnterior() {
            // Arrange
            Pedido pedido1 = TestDataBuilder.umPedido().comId(111L)
                    .comEmailUsuario("user1@test.com").build();

            Pedido pedido2 = TestDataBuilder.umPedido().comId(222L)
                    .comEmailUsuario("user2@test.com").build();

            // Simula falha apenas na primeira chamada
            Mockito.doThrow(new RuntimeException("Falha temporária")).doNothing()
                    .when(NotificacaoServiceTest.this.notificacaoPort)
                    .enviarNotificacao(ArgumentMatchers.anyString());

            // Act & Assert
            // Primeira chamada deve falhar
            Assertions.assertThatThrownBy(
                    () -> NotificacaoServiceTest.this.notificacaoService.enviarNotificacao(pedido1))
                    .isInstanceOf(RuntimeException.class);

            // Segunda chamada deve funcionar
            Assertions.assertThatCode(
                    () -> NotificacaoServiceTest.this.notificacaoService.enviarNotificacao(pedido2))
                    .doesNotThrowAnyException();

            // Verify que ambas foram chamadas
            Mockito.verify(NotificacaoServiceTest.this.notificacaoPort, Mockito.times(2))
                    .enviarNotificacao(ArgumentMatchers.anyString());
        }
    }

    @Nested
    @DisplayName("Validações de Dependências")
    class ValidacoesDependencias {

        @Test
        @DisplayName("Deve ter NotificacaoPort injetado")
        void deveTerNotificacaoPortInjetado() {
            Assertions.assertThat(NotificacaoServiceTest.this.notificacaoService).isNotNull();
        }

        @Test
        @DisplayName("Deve usar NotificacaoPort para envio")
        void deveUsarNotificacaoPortParaEnvio() {
            // Act
            NotificacaoServiceTest.this.notificacaoService
                    .enviarNotificacao(NotificacaoServiceTest.this.pedidoTeste);

            // Assert
            Mockito.verify(NotificacaoServiceTest.this.notificacaoPort)
                    .enviarNotificacao(ArgumentMatchers.anyString());
        }
    }
}
