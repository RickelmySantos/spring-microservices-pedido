package com.rsdesenvolvimento.api_gateway.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class ApiGatewayIntegrationTest {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    void testHealthEndpoint() {
        this.webTestClient.get().uri("/actuator/health").exchange().expectStatus().isOk()
                .expectBody().jsonPath("$.status").isEqualTo("UP");
    }

    @Test
    void testInfoEndpoint() {
        this.webTestClient.get().uri("/actuator/info").exchange().expectStatus().isOk();
    }

    @Test
    void testCorsPreflightRequest() {
        this.webTestClient.options().uri("/test-endpoint").header("Origin", "http://localhost:4200")
                .header("Access-Control-Request-Method", "POST")
                .header("Access-Control-Request-Headers", "Authorization,Content-Type").exchange()
                .expectStatus().isOk().expectHeader().exists("Access-Control-Allow-Origin")
                .expectHeader().valueEquals("Access-Control-Allow-Origin", "http://localhost:4200")
                .expectHeader().exists("Access-Control-Allow-Methods").expectHeader()
                .exists("Access-Control-Allow-Headers").expectHeader()
                .exists("Access-Control-Allow-Credentials");
    }

    @Test
    void testCorsActualRequest() {
        this.webTestClient.get().uri("/estoque-api/produtos")
                .header("Origin", "http://localhost:4200").exchange().expectStatus().isNotFound()
                .expectHeader().exists("Access-Control-Allow-Origin");
    }

    @Test
    void testEstoqueApiIsPublic() {

        this.webTestClient.get().uri("/estoque-api/health").exchange().expectStatus().isNotFound();

        this.webTestClient.post().uri("/estoque-api/produtos")
                .header("Content-Type", "application/json")
                .bodyValue("{\"nome\":\"Produto Teste\"}").exchange().expectStatus().isNotFound();
    }

    @Test
    void testProtectedEndpointRequiresAuth() {
        this.webTestClient.get().uri("/usuario-api/profile").exchange().expectStatus()
                .isUnauthorized();

        this.webTestClient.get().uri("/pedido-api/pedidos").exchange().expectStatus()
                .isUnauthorized();
    }

    @Test
    void testCorsWithDifferentOrigin() {
        this.webTestClient.options().uri("/test-endpoint")
                .header("Origin", "http://malicious-site.com")
                .header("Access-Control-Request-Method", "GET").exchange().expectStatus()
                .isForbidden();
    }

    @Test
    void testSecurityHeadersPresent() {
        this.webTestClient.get().uri("/actuator/health").exchange().expectStatus().isOk()
                .expectHeader().exists("X-Content-Type-Options").expectHeader()
                .exists("X-Frame-Options").expectHeader().exists("X-XSS-Protection");
    }

    @Test
    void testMethodNotAllowed() {

        this.webTestClient.patch().uri("/estoque-api/test").exchange().expectStatus().isNotFound();
    }

    @Test
    void testLargeRequestBody() {

        StringBuilder largeBody = new StringBuilder();
        for (int i = 0; i < 1000; i++) {
            largeBody.append("This is a test line ").append(i).append("\n");
        }

        this.webTestClient.post().uri("/estoque-api/bulk-upload")
                .header("Content-Type", "text/plain").bodyValue(largeBody.toString()).exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void testMultipleSimultaneousRequests() {
        for (int i = 0; i < 10; i++) {
            this.webTestClient.get().uri("/actuator/health").exchange().expectStatus().isOk();
        }
    }
}
