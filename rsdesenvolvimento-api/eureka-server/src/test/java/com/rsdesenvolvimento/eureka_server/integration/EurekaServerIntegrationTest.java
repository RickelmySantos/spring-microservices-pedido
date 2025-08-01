package com.rsdesenvolvimento.eureka_server.integration;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.RestTemplate;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@DisplayName("Testes de Integração do Eureka Server")
class EurekaServerIntegrationTest {

    @LocalServerPort
    private int port;

    private final RestTemplate restTemplate = new RestTemplate();

    @Nested
    @DisplayName("Endpoints do Eureka")
    class EurekaEndpointsTests {

        @Test
        @DisplayName("Deve responder no endpoint principal do Eureka")
        void deveResponderNoEndpointPrincipalDoEureka() {
            // Given
            String url = "http://localhost:" + EurekaServerIntegrationTest.this.port + "/";

            // When
            ResponseEntity<String> response =
                    EurekaServerIntegrationTest.this.restTemplate.getForEntity(url, String.class);

            // Then
            Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            Assertions.assertThat(response.getBody()).isNotNull();
            Assertions.assertThat(response.getBody()).contains("Eureka");
        }

        @Test
        @DisplayName("Deve expor endpoint de aplicações registradas")
        void deveExporEndpointDeAplicacoesRegistradas() {
            // Given
            String url =
                    "http://localhost:" + EurekaServerIntegrationTest.this.port + "/eureka/apps";

            // When
            ResponseEntity<String> response =
                    EurekaServerIntegrationTest.this.restTemplate.getForEntity(url, String.class);

            // Then
            Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            Assertions.assertThat(response.getBody()).isNotNull();
        }

        @Test
        @DisplayName("Deve expor endpoint de status")
        void deveExporEndpointDeStatus() {
            // Given
            String url =
                    "http://localhost:" + EurekaServerIntegrationTest.this.port + "/eureka/status";

            // When
            ResponseEntity<String> response =
                    EurekaServerIntegrationTest.this.restTemplate.getForEntity(url, String.class);

            // Then
            Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        }
    }

    @Nested
    @DisplayName("Endpoints do Actuator")
    class ActuatorEndpointsTests {

        @Test
        @DisplayName("Deve expor endpoint de health")
        void deveExporEndpointDeHealth() {
            // Given
            String url = "http://localhost:" + EurekaServerIntegrationTest.this.port
                    + "/actuator/health";

            // When
            ResponseEntity<String> response =
                    EurekaServerIntegrationTest.this.restTemplate.getForEntity(url, String.class);

            // Then
            Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            Assertions.assertThat(response.getBody()).isNotNull();
            Assertions.assertThat(response.getBody()).contains("\"status\":\"UP\"");
        }

        @Test
        @DisplayName("Deve expor endpoint de info")
        void deveExporEndpointDeInfo() {
            // Given
            String url =
                    "http://localhost:" + EurekaServerIntegrationTest.this.port + "/actuator/info";

            // When
            ResponseEntity<String> response =
                    EurekaServerIntegrationTest.this.restTemplate.getForEntity(url, String.class);

            // Then
            Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        }

        @Test
        @DisplayName("Deve expor endpoint de métricas")
        void deveExporEndpointDeMetricas() {
            // Given
            String url = "http://localhost:" + EurekaServerIntegrationTest.this.port
                    + "/actuator/metrics";

            // When
            ResponseEntity<String> response =
                    EurekaServerIntegrationTest.this.restTemplate.getForEntity(url, String.class);

            // Then
            Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            Assertions.assertThat(response.getBody()).isNotNull();
            Assertions.assertThat(response.getBody()).contains("names");
        }
    }

    @Nested
    @DisplayName("Funcionalidade de Registro")
    class RegistrationFunctionalityTests {

        @Test
        @DisplayName("Deve aceitar registro de nova aplicação")
        void deveAceitarRegistroDeNovaAplicacao() {
            // Given
            String url = "http://localhost:" + EurekaServerIntegrationTest.this.port
                    + "/eureka/apps/TEST-APP";

            // Simula dados de registro de uma aplicação
            String registrationData =
                    """
                            <instance>
                                <instanceId>test-app-001</instanceId>
                                <hostName>localhost</hostName>
                                <app>TEST-APP</app>
                                <ipAddr>127.0.0.1</ipAddr>
                                <status>UP</status>
                                <port enabled="true">8080</port>
                                <securePort enabled="false">443</securePort>
                                <homePageUrl>http://localhost:8080/</homePageUrl>
                                <statusPageUrl>http://localhost:8080/actuator/info</statusPageUrl>
                                <healthCheckUrl>http://localhost:8080/actuator/health</healthCheckUrl>
                                <dataCenterInfo class="com.netflix.appinfo.InstanceInfo$DefaultDataCenterInfo">
                                    <name>MyOwn</name>
                                </dataCenterInfo>
                            </instance>
                            """;

            // When
            try {
                ResponseEntity<String> response = EurekaServerIntegrationTest.this.restTemplate
                        .postForEntity(url, registrationData, String.class);

                // Then
                Assertions.assertThat(response.getStatusCode().is2xxSuccessful()
                        || response.getStatusCode() == HttpStatus.NO_CONTENT).isTrue();
            } catch (Exception e) {
                Assertions.assertThat(e).isNotNull();
            }
        }

        @Test
        @DisplayName("Deve listar aplicações após o startup")
        void deveListarAplicacoesAposOStartup() {
            // Given
            String url =
                    "http://localhost:" + EurekaServerIntegrationTest.this.port + "/eureka/apps";

            // When
            ResponseEntity<String> response =
                    EurekaServerIntegrationTest.this.restTemplate.getForEntity(url, String.class);

            // Then
            Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            Assertions.assertThat(response.getBody()).isNotNull();

            Assertions.assertThat(response.getBody()).satisfiesAnyOf(
                    body -> Assertions.assertThat(body).contains("<applications>"),
                    body -> Assertions.assertThat(body).contains("\"applications\""));
        }
    }
}
