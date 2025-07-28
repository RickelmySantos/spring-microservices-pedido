package com.rsdesenvolvimento.pagamento_service.config;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes para NotificacaoProducer")
class NotificacaoProducerTest {

    @Mock
    private RabbitTemplate rabbitTemplate;

    @InjectMocks
    private NotificacaoProducer notificacaoProducer;

    private String mensagemValida;

    @BeforeEach
    void setUp() {
        this.mensagemValida = "Mensagem de teste para notificação";
    }

    @Test
    @DisplayName("Deve enviar notificação com mensagem válida")
    void deveEnviarNotificacaoComMensagemValida() {
        // When
        this.notificacaoProducer.enviarNotificacao(this.mensagemValida);

        // Then
        Mockito.verify(this.rabbitTemplate, Mockito.times(1)).convertAndSend("notificacao-exchange",
                "notificacao-routing-key", this.mensagemValida);
    }

    @Test
    @DisplayName("Deve lançar exceção para mensagem nula")
    void deveLancarExcecaoParaMensagemNula() {
        // When & Then
        Assertions.assertThatThrownBy(() -> this.notificacaoProducer.enviarNotificacao(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Mensagem de notificação não pode ser vazia.");

        Mockito.verify(this.rabbitTemplate, Mockito.never()).convertAndSend(
                ArgumentMatchers.anyString(), ArgumentMatchers.anyString(),
                ArgumentMatchers.anyString());
    }

    @Test
    @DisplayName("Deve lançar exceção para mensagem vazia")
    void deveLancarExcecaoParaMensagemVazia() {
        // When & Then
        Assertions.assertThatThrownBy(() -> this.notificacaoProducer.enviarNotificacao(""))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Mensagem de notificação não pode ser vazia.");

        Mockito.verify(this.rabbitTemplate, Mockito.never()).convertAndSend(
                ArgumentMatchers.anyString(), ArgumentMatchers.anyString(),
                ArgumentMatchers.anyString());
    }

    @Test
    @DisplayName("Deve lançar exceção para mensagem apenas com espaços")
    void deveLancarExcecaoParaMensagemApenasComEspacos() {
        // When & Then
        Assertions.assertThatThrownBy(() -> this.notificacaoProducer.enviarNotificacao("   "))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Mensagem de notificação não pode ser vazia.");

        Mockito.verify(this.rabbitTemplate, Mockito.never()).convertAndSend(
                ArgumentMatchers.anyString(), ArgumentMatchers.anyString(),
                ArgumentMatchers.anyString());
    }

    @Test
    @DisplayName("Deve enviar notificação com delay usando exchange e routing key corretos")
    void deveEnviarNotificacaoComDelayUsandoExchangeERoutingKeyCorretos() {
        // Given
        long delay = 5000L;

        // When
        this.notificacaoProducer.enviarNotificacaoComDelay(this.mensagemValida, delay);

        // Then
        ArgumentCaptor<Message> messageCaptor = ArgumentCaptor.forClass(Message.class);
        Mockito.verify(this.rabbitTemplate, Mockito.times(1)).convertAndSend(
                ArgumentMatchers.eq("notificacao-delay-exchange"),
                ArgumentMatchers.eq("notificacao.delay"), messageCaptor.capture());

        Message capturedMessage = messageCaptor.getValue();
        Assertions.assertThat(capturedMessage.getBody()).isEqualTo(this.mensagemValida.getBytes());
        Assertions.assertThat(capturedMessage.getMessageProperties().getExpiration())
                .isEqualTo(String.valueOf(delay));
    }

    @Test
    @DisplayName("Deve configurar delay corretamente nas propriedades da mensagem")
    void deveConfigurarDelayCorretamenteNasPropriedadesDaMensagem() {
        // Given
        long delay = 10000L;

        // When
        this.notificacaoProducer.enviarNotificacaoComDelay(this.mensagemValida, delay);

        // Then
        ArgumentCaptor<Message> messageCaptor = ArgumentCaptor.forClass(Message.class);
        Mockito.verify(this.rabbitTemplate).convertAndSend(ArgumentMatchers.anyString(),
                ArgumentMatchers.anyString(), messageCaptor.capture());

        Message message = messageCaptor.getValue();
        Assertions.assertThat(message.getMessageProperties().getExpiration()).isEqualTo("10000");
    }

    @Test
    @DisplayName("Deve lançar exceção ao enviar notificação com delay para mensagem nula")
    void deveLancarExcecaoAoEnviarNotificacaoComDelayParaMensagemNula() {
        // When & Then
        Assertions
                .assertThatThrownBy(
                        () -> this.notificacaoProducer.enviarNotificacaoComDelay(null, 5000L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Mensagem de notificação não pode ser vazia.");

        Mockito.verify(this.rabbitTemplate, Mockito.never()).convertAndSend(
                ArgumentMatchers.anyString(), ArgumentMatchers.anyString(),
                ArgumentMatchers.any(Message.class));
    }

    @Test
    @DisplayName("Deve lançar exceção ao enviar notificação com delay para mensagem vazia")
    void deveLancarExcecaoAoEnviarNotificacaoComDelayParaMensagemVazia() {
        // When & Then
        Assertions
                .assertThatThrownBy(
                        () -> this.notificacaoProducer.enviarNotificacaoComDelay("", 5000L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Mensagem de notificação não pode ser vazia.");

        Mockito.verify(this.rabbitTemplate, Mockito.never()).convertAndSend(
                ArgumentMatchers.anyString(), ArgumentMatchers.anyString(),
                ArgumentMatchers.any(Message.class));
    }

    @Test
    @DisplayName("Deve aceitar delay zero")
    void deveAceitarDelayZero() {
        // Given
        long delay = 0L;

        // When
        this.notificacaoProducer.enviarNotificacaoComDelay(this.mensagemValida, delay);

        // Then
        ArgumentCaptor<Message> messageCaptor = ArgumentCaptor.forClass(Message.class);
        Mockito.verify(this.rabbitTemplate).convertAndSend(ArgumentMatchers.anyString(),
                ArgumentMatchers.anyString(), messageCaptor.capture());

        Message message = messageCaptor.getValue();
        Assertions.assertThat(message.getMessageProperties().getExpiration()).isEqualTo("0");
    }

    @Test
    @DisplayName("Deve aceitar delay negativo")
    void deveAceitarDelayNegativo() {
        // Given
        long delay = -1000L;

        // When
        this.notificacaoProducer.enviarNotificacaoComDelay(this.mensagemValida, delay);

        // Then
        ArgumentCaptor<Message> messageCaptor = ArgumentCaptor.forClass(Message.class);
        Mockito.verify(this.rabbitTemplate).convertAndSend(ArgumentMatchers.anyString(),
                ArgumentMatchers.anyString(), messageCaptor.capture());

        Message message = messageCaptor.getValue();
        Assertions.assertThat(message.getMessageProperties().getExpiration()).isEqualTo("-1000");
    }

    @Test
    @DisplayName("Deve preservar conteúdo da mensagem ao enviar com delay")
    void devePreservarConteudoDaMensagemAoEnviarComDelay() {
        // Given
        String mensagemEspecial = "Mensagem com caracteres especiais: ção, ã, é, í, ó, ú";
        long delay = 1000L;

        // When
        this.notificacaoProducer.enviarNotificacaoComDelay(mensagemEspecial, delay);

        // Then
        ArgumentCaptor<Message> messageCaptor = ArgumentCaptor.forClass(Message.class);
        Mockito.verify(this.rabbitTemplate).convertAndSend(ArgumentMatchers.anyString(),
                ArgumentMatchers.anyString(), messageCaptor.capture());

        Message message = messageCaptor.getValue();
        Assertions.assertThat(new String(message.getBody())).isEqualTo(mensagemEspecial);
    }
}
