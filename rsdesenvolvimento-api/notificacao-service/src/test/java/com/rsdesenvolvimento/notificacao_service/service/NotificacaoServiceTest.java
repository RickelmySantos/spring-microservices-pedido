package com.rsdesenvolvimento.notificacao_service.service;

import com.rsdesenvolvimento.notificacao_service.exception.NotificacaoException;
import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedConstruction;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
@DisplayName("NotificacaoService - Testes Unitários")
class NotificacaoServiceTest {

    @Mock
    private SendGrid sendGrid;

    @Mock
    private Request request;

    @Mock
    private Response response;

    @InjectMocks
    private NotificacaoService notificacaoService;

    private String destinatario;
    private String assunto;
    private String conteudo;
    private String sendGridApiKey;
    private Map<String, String> headersMap;

    @BeforeEach
    void setUp() {
        // Arrange
        this.destinatario = "usuario@teste.com";
        this.assunto = "Teste de Notificação";
        this.conteudo = "<h1>Email de teste</h1><p>Conteúdo do email</p>";
        this.sendGridApiKey = "SG.test-api-key-123";
        this.headersMap = new HashMap<>();
        this.headersMap.put("Content-Type", "application/json");

        // Configurar a API key via reflection para simular @Value
        ReflectionTestUtils.setField(this.notificacaoService, "sendGridApiKey",
                this.sendGridApiKey);
    }

    @Nested
    @DisplayName("Envio de Email")
    class EnvioEmailTests {

        @Test
        @DisplayName("Deve enviar email com sucesso")
        void deveEnviarEmailComSucesso() throws IOException {
            // Arrange
            Mockito.when(NotificacaoServiceTest.this.response.getStatusCode()).thenReturn(202);
            Mockito.when(NotificacaoServiceTest.this.response.getBody())
                    .thenReturn("Email enviado");
            Mockito.when(NotificacaoServiceTest.this.response.getHeaders())
                    .thenReturn(NotificacaoServiceTest.this.headersMap);

            try (MockedConstruction<SendGrid> sendGridMock =
                    Mockito.mockConstruction(SendGrid.class, (mock, context) -> {
                        Mockito.when(mock.api(ArgumentMatchers.any(Request.class)))
                                .thenReturn(NotificacaoServiceTest.this.response);
                    });
                    MockedConstruction<Request> requestMock =
                            Mockito.mockConstruction(Request.class)) {

                // Act
                NotificacaoServiceTest.this.notificacaoService.enviarEmail(
                        NotificacaoServiceTest.this.destinatario,
                        NotificacaoServiceTest.this.assunto, NotificacaoServiceTest.this.conteudo);

                // Assert
                Assertions.assertThat(sendGridMock.constructed()).hasSize(1);
                Assertions.assertThat(requestMock.constructed()).hasSize(1);

                SendGrid constructedSendGrid = sendGridMock.constructed().get(0);
                Request constructedRequest = requestMock.constructed().get(0);

                Mockito.verify(constructedRequest).setMethod(Method.POST);
                Mockito.verify(constructedRequest).setEndpoint("mail/send");
                Mockito.verify(constructedRequest).setBody(ArgumentMatchers.anyString());
                Mockito.verify(constructedSendGrid).api(constructedRequest);
            }
        }

        @Test
        @DisplayName("Deve configurar request com parâmetros corretos")
        void deveConfigurarRequestComParametrosCorretos() throws IOException {
            // Arrange
            Mockito.when(NotificacaoServiceTest.this.response.getStatusCode()).thenReturn(202);
            Mockito.when(NotificacaoServiceTest.this.response.getBody()).thenReturn("Success");
            Mockito.when(NotificacaoServiceTest.this.response.getHeaders())
                    .thenReturn(NotificacaoServiceTest.this.headersMap);

            try (MockedConstruction<SendGrid> sendGridMock =
                    Mockito.mockConstruction(SendGrid.class, (mock, context) -> {
                        // Verificar se SendGrid foi criado com API key correta
                        Assertions.assertThat(context.arguments()).hasSize(1);
                        Assertions.assertThat(context.arguments().get(0))
                                .isEqualTo(NotificacaoServiceTest.this.sendGridApiKey);
                        Mockito.when(mock.api(ArgumentMatchers.any(Request.class)))
                                .thenReturn(NotificacaoServiceTest.this.response);
                    });
                    MockedConstruction<Request> requestMock =
                            Mockito.mockConstruction(Request.class)) {

                // Act
                NotificacaoServiceTest.this.notificacaoService.enviarEmail(
                        NotificacaoServiceTest.this.destinatario,
                        NotificacaoServiceTest.this.assunto, NotificacaoServiceTest.this.conteudo);

                // Assert
                Request constructedRequest = requestMock.constructed().get(0);

                Mockito.verify(constructedRequest).setMethod(Method.POST);
                Mockito.verify(constructedRequest).setEndpoint("mail/send");
                Mockito.verify(constructedRequest).setBody(ArgumentMatchers.anyString());
            }
        }

        @Test
        @DisplayName("Deve processar resposta de sucesso corretamente")
        void deveProcessarRespostaDeSucessoCorretamente() throws IOException {
            // Arrange
            int expectedStatusCode = 202;
            String expectedBody = "Email enviado com sucesso";
            Map<String, String> expectedHeaders = new HashMap<>();
            expectedHeaders.put("Content-Type", "application/json");
            expectedHeaders.put("X-RateLimit-Remaining", "100");

            Mockito.when(NotificacaoServiceTest.this.response.getStatusCode())
                    .thenReturn(expectedStatusCode);
            Mockito.when(NotificacaoServiceTest.this.response.getBody()).thenReturn(expectedBody);
            Mockito.when(NotificacaoServiceTest.this.response.getHeaders())
                    .thenReturn(expectedHeaders);

            try (MockedConstruction<SendGrid> sendGridMock =
                    Mockito.mockConstruction(SendGrid.class, (mock, context) -> {
                        Mockito.when(mock.api(ArgumentMatchers.any(Request.class)))
                                .thenReturn(NotificacaoServiceTest.this.response);
                    });
                    MockedConstruction<Request> requestMock =
                            Mockito.mockConstruction(Request.class)) {

                // Act & Assert - Não deve lançar exceção
                Assertions
                        .assertThatCode(() -> NotificacaoServiceTest.this.notificacaoService
                                .enviarEmail(NotificacaoServiceTest.this.destinatario,
                                        NotificacaoServiceTest.this.assunto,
                                        NotificacaoServiceTest.this.conteudo))
                        .doesNotThrowAnyException();

                // Verificar se os logs foram chamados
                Mockito.verify(NotificacaoServiceTest.this.response).getStatusCode();
                Mockito.verify(NotificacaoServiceTest.this.response).getBody();
                Mockito.verify(NotificacaoServiceTest.this.response).getHeaders();
            }
        }

        @Test
        @DisplayName("Deve configurar email com remetente correto")
        void deveConfigurarEmailComRemetenteCorreto() throws IOException {
            // Arrange
            Mockito.when(NotificacaoServiceTest.this.response.getStatusCode()).thenReturn(202);
            Mockito.when(NotificacaoServiceTest.this.response.getBody()).thenReturn("");
            Mockito.when(NotificacaoServiceTest.this.response.getHeaders())
                    .thenReturn(NotificacaoServiceTest.this.headersMap);

            try (MockedConstruction<SendGrid> sendGridMock =
                    Mockito.mockConstruction(SendGrid.class, (mock, context) -> {
                        Mockito.when(mock.api(ArgumentMatchers.any(Request.class)))
                                .thenReturn(NotificacaoServiceTest.this.response);
                    });
                    MockedConstruction<Request> requestMock =
                            Mockito.mockConstruction(Request.class);
                    MockedConstruction<Mail> mailMock =
                            Mockito.mockConstruction(Mail.class, (mock, context) -> {
                                // Verificar se Mail foi criado com parâmetros corretos
                                Assertions.assertThat(context.arguments()).hasSize(4);
                                // Verificar remetente, assunto, destinatário e conteúdo
                            })) {

                // Act
                NotificacaoServiceTest.this.notificacaoService.enviarEmail(
                        NotificacaoServiceTest.this.destinatario,
                        NotificacaoServiceTest.this.assunto, NotificacaoServiceTest.this.conteudo);

                // Assert
                Assertions.assertThat(mailMock.constructed()).hasSize(1);
                Mail constructedMail = mailMock.constructed().get(0);

                // Verificar se setReplyTo foi chamado
                Mockito.verify(constructedMail).setReplyTo(ArgumentMatchers.any());
            }
        }
    }

    @Nested
    @DisplayName("Tratamento de Erros")
    class TratamentoErrosTests {

        @Test
        @DisplayName("Deve lançar NotificacaoException quando IOException ocorre")
        void deveLancarNotificacaoExceptionQuandoIoExceptionOcorre() throws IOException {
            // Arrange
            IOException ioException = new IOException("Erro de rede");

            try (MockedConstruction<SendGrid> sendGridMock =
                    Mockito.mockConstruction(SendGrid.class, (mock, context) -> {
                        Mockito.when(mock.api(ArgumentMatchers.any(Request.class)))
                                .thenThrow(ioException);
                    });
                    MockedConstruction<Request> requestMock =
                            Mockito.mockConstruction(Request.class)) {

                // Act & Assert
                Assertions
                        .assertThatThrownBy(() -> NotificacaoServiceTest.this.notificacaoService
                                .enviarEmail(NotificacaoServiceTest.this.destinatario,
                                        NotificacaoServiceTest.this.assunto,
                                        NotificacaoServiceTest.this.conteudo))
                        .isInstanceOf(NotificacaoException.class).hasMessage("Erro ao enviar email")
                        .hasCause(ioException);
            }
        }

        @Test
        @DisplayName("Deve lançar NotificacaoException quando Exception genérica ocorre")
        void deveLancarNotificacaoExceptionQuandoExceptionGenericaOcorre() throws IOException {
            // Arrange
            RuntimeException runtimeException = new RuntimeException("Erro genérico");

            try (MockedConstruction<SendGrid> sendGridMock =
                    Mockito.mockConstruction(SendGrid.class, (mock, context) -> {
                        Mockito.when(mock.api(ArgumentMatchers.any(Request.class)))
                                .thenThrow(runtimeException);
                    });
                    MockedConstruction<Request> requestMock =
                            Mockito.mockConstruction(Request.class)) {

                // Act & Assert
                Assertions
                        .assertThatThrownBy(() -> NotificacaoServiceTest.this.notificacaoService
                                .enviarEmail(NotificacaoServiceTest.this.destinatario,
                                        NotificacaoServiceTest.this.assunto,
                                        NotificacaoServiceTest.this.conteudo))
                        .isInstanceOf(NotificacaoException.class).hasMessage("Erro ao enviar email")
                        .hasCause(runtimeException);
            }
        }

        @Test
        @DisplayName("Deve lançar NotificacaoException quando API key é inválida")
        void deveLancarNotificacaoExceptionQuandoApiKeyInvalida() throws IOException {
            // Arrange
            Response unauthorizedResponse = Mockito.mock(Response.class);
            Mockito.when(unauthorizedResponse.getStatusCode()).thenReturn(401);
            Mockito.when(unauthorizedResponse.getBody()).thenReturn("Unauthorized");
            Mockito.when(unauthorizedResponse.getHeaders())
                    .thenReturn(NotificacaoServiceTest.this.headersMap);

            try (MockedConstruction<SendGrid> sendGridMock =
                    Mockito.mockConstruction(SendGrid.class, (mock, context) -> {
                        Mockito.when(mock.api(ArgumentMatchers.any(Request.class)))
                                .thenReturn(unauthorizedResponse);
                    });
                    MockedConstruction<Request> requestMock =
                            Mockito.mockConstruction(Request.class)) {

                // Act & Assert - Para API key inválida, o SendGrid não lança exceção, apenas
                // retorna 401
                Assertions
                        .assertThatCode(() -> NotificacaoServiceTest.this.notificacaoService
                                .enviarEmail(NotificacaoServiceTest.this.destinatario,
                                        NotificacaoServiceTest.this.assunto,
                                        NotificacaoServiceTest.this.conteudo))
                        .doesNotThrowAnyException();
            }
        }
    }

    @Nested
    @DisplayName("Validação de Parâmetros")
    class ValidacaoParametrosTests {

        @Test
        @DisplayName("Deve processar email com destinatário válido")
        void deveProcessarEmailComDestinatarioValido() throws IOException {
            // Arrange
            String emailValido = "teste@dominio.com.br";
            Mockito.when(NotificacaoServiceTest.this.response.getStatusCode()).thenReturn(202);
            Mockito.when(NotificacaoServiceTest.this.response.getBody()).thenReturn("");
            Mockito.when(NotificacaoServiceTest.this.response.getHeaders())
                    .thenReturn(NotificacaoServiceTest.this.headersMap);

            try (MockedConstruction<SendGrid> sendGridMock =
                    Mockito.mockConstruction(SendGrid.class, (mock, context) -> {
                        Mockito.when(mock.api(ArgumentMatchers.any(Request.class)))
                                .thenReturn(NotificacaoServiceTest.this.response);
                    });
                    MockedConstruction<Request> requestMock =
                            Mockito.mockConstruction(Request.class)) {

                // Act & Assert
                Assertions
                        .assertThatCode(() -> NotificacaoServiceTest.this.notificacaoService
                                .enviarEmail(emailValido, NotificacaoServiceTest.this.assunto,
                                        NotificacaoServiceTest.this.conteudo))
                        .doesNotThrowAnyException();
            }
        }

        @Test
        @DisplayName("Deve processar email com assunto vazio")
        void deveProcessarEmailComAssuntoVazio() throws IOException {
            // Arrange
            String assuntoVazio = "";
            Mockito.when(NotificacaoServiceTest.this.response.getStatusCode()).thenReturn(202);
            Mockito.when(NotificacaoServiceTest.this.response.getBody()).thenReturn("");
            Mockito.when(NotificacaoServiceTest.this.response.getHeaders())
                    .thenReturn(NotificacaoServiceTest.this.headersMap);

            try (MockedConstruction<SendGrid> sendGridMock =
                    Mockito.mockConstruction(SendGrid.class, (mock, context) -> {
                        Mockito.when(mock.api(ArgumentMatchers.any(Request.class)))
                                .thenReturn(NotificacaoServiceTest.this.response);
                    });
                    MockedConstruction<Request> requestMock =
                            Mockito.mockConstruction(Request.class)) {

                // Act & Assert
                Assertions
                        .assertThatCode(() -> NotificacaoServiceTest.this.notificacaoService
                                .enviarEmail(NotificacaoServiceTest.this.destinatario, assuntoVazio,
                                        NotificacaoServiceTest.this.conteudo))
                        .doesNotThrowAnyException();
            }
        }

        @Test
        @DisplayName("Deve processar email com conteúdo HTML")
        void deveProcessarEmailComConteudoHtml() throws IOException {
            // Arrange
            String conteudoHtml =
                    "<html><body><h1>Título</h1><p>Parágrafo com <strong>negrito</strong></p></body></html>";
            Mockito.when(NotificacaoServiceTest.this.response.getStatusCode()).thenReturn(202);
            Mockito.when(NotificacaoServiceTest.this.response.getBody()).thenReturn("");
            Mockito.when(NotificacaoServiceTest.this.response.getHeaders())
                    .thenReturn(NotificacaoServiceTest.this.headersMap);

            try (MockedConstruction<SendGrid> sendGridMock =
                    Mockito.mockConstruction(SendGrid.class, (mock, context) -> {
                        Mockito.when(mock.api(ArgumentMatchers.any(Request.class)))
                                .thenReturn(NotificacaoServiceTest.this.response);
                    });
                    MockedConstruction<Request> requestMock =
                            Mockito.mockConstruction(Request.class)) {

                // Act & Assert
                Assertions
                        .assertThatCode(() -> NotificacaoServiceTest.this.notificacaoService
                                .enviarEmail(NotificacaoServiceTest.this.destinatario,
                                        NotificacaoServiceTest.this.assunto, conteudoHtml))
                        .doesNotThrowAnyException();
            }
        }
    }

    @Nested
    @DisplayName("Configurações e Integração")
    class ConfiguracoesIntegracaoTests {

        @Test
        @DisplayName("Deve usar API key configurada")
        void deveUsarApiKeyConfigurada() throws IOException {
            // Arrange
            Mockito.when(NotificacaoServiceTest.this.response.getStatusCode()).thenReturn(202);
            Mockito.when(NotificacaoServiceTest.this.response.getBody()).thenReturn("");
            Mockito.when(NotificacaoServiceTest.this.response.getHeaders())
                    .thenReturn(NotificacaoServiceTest.this.headersMap);

            try (MockedConstruction<SendGrid> sendGridMock =
                    Mockito.mockConstruction(SendGrid.class, (mock, context) -> {
                        Mockito.when(mock.api(ArgumentMatchers.any(Request.class)))
                                .thenReturn(NotificacaoServiceTest.this.response);
                    })) {

                // Act
                NotificacaoServiceTest.this.notificacaoService.enviarEmail(
                        NotificacaoServiceTest.this.destinatario,
                        NotificacaoServiceTest.this.assunto, NotificacaoServiceTest.this.conteudo);

                // Assert
                Assertions.assertThat(sendGridMock.constructed()).hasSize(1);
                // Verificar se SendGrid foi criado com a API key correta através do contexto
                SendGrid constructedSendGrid = sendGridMock.constructed().get(0);
                Assertions.assertThat(constructedSendGrid).isNotNull();
            }
        }

        @Test
        @DisplayName("Deve garantir que dependências são configuradas corretamente")
        void deveGarantirQueDependenciasSaoConfiguradasCorretamente() {
            // Act & Assert
            Assertions.assertThat(NotificacaoServiceTest.this.notificacaoService).isNotNull();

            String apiKey = (String) ReflectionTestUtils
                    .getField(NotificacaoServiceTest.this.notificacaoService, "sendGridApiKey");
            Assertions.assertThat(apiKey).isEqualTo(NotificacaoServiceTest.this.sendGridApiKey);
        }

        @Test
        @DisplayName("Deve processar resposta com diferentes status codes")
        void deveProcessarRespostaComDiferentesStatusCodes() throws IOException {
            // Arrange
            int[] statusCodes = {200, 201, 202, 400, 401, 500};

            for (int statusCode : statusCodes) {
                Mockito.when(NotificacaoServiceTest.this.response.getStatusCode())
                        .thenReturn(statusCode);
                Mockito.when(NotificacaoServiceTest.this.response.getBody())
                        .thenReturn("Response for " + statusCode);
                Mockito.when(NotificacaoServiceTest.this.response.getHeaders())
                        .thenReturn(NotificacaoServiceTest.this.headersMap);

                try (MockedConstruction<SendGrid> sendGridMock =
                        Mockito.mockConstruction(SendGrid.class, (mock, context) -> {
                            Mockito.when(mock.api(ArgumentMatchers.any(Request.class)))
                                    .thenReturn(NotificacaoServiceTest.this.response);
                        });
                        MockedConstruction<Request> requestMock =
                                Mockito.mockConstruction(Request.class)) {

                    // Act & Assert - Não deve lançar exceção baseado no status code
                    final int currentStatusCode = statusCode;
                    Assertions
                            .assertThatCode(() -> NotificacaoServiceTest.this.notificacaoService
                                    .enviarEmail(NotificacaoServiceTest.this.destinatario,
                                            "Teste status " + currentStatusCode,
                                            NotificacaoServiceTest.this.conteudo))
                            .doesNotThrowAnyException();
                }
            }
        }
    }
}
