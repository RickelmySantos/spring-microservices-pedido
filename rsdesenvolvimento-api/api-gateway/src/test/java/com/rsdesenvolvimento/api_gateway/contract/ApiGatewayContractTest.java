package com.rsdesenvolvimento.api_gateway.contract;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class ApiGatewayContractTest {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    void testActuatorHealthContract() {
        this.webTestClient.get().uri("/actuator/health").exchange().expectStatus().isOk()
                .expectHeader().contentType("application/vnd.spring-boot.actuator.v3+json")
                .expectBody().jsonPath("$.status").exists().jsonPath("$.status").isEqualTo("UP");
    }

    @Test
    void testActuatorInfoContract() {

        this.webTestClient.get().uri("/actuator/info").exchange().expectStatus().isOk()
                .expectHeader().contentType("application/vnd.spring-boot.actuator.v3+json");
    }

    @Test
    void testCorsContract() {
        this.webTestClient.options().uri("/any-path").header("Origin", "http://localhost:4200")
                .header("Access-Control-Request-Method", "GET")
                .header("Access-Control-Request-Headers", "Authorization").exchange().expectStatus()
                .isOk().expectHeader().exists("Access-Control-Allow-Origin").expectHeader()
                .valueEquals("Access-Control-Allow-Origin", "http://localhost:4200").expectHeader()
                .exists("Access-Control-Allow-Methods").expectHeader()
                .exists("Access-Control-Allow-Headers").expectHeader()
                .exists("Access-Control-Allow-Credentials").expectHeader()
                .valueEquals("Access-Control-Allow-Credentials", "true");
    }

    @Test
    void testAuthenticationContract() {
        this.webTestClient.get().uri("/pedido-api/pedidos").exchange().expectStatus()
                .isUnauthorized();

        this.webTestClient.get().uri("/usuario-api/profile").exchange().expectStatus()
                .isUnauthorized();

        this.webTestClient.get().uri("/pagamento-api/pagamentos").exchange().expectStatus()
                .isUnauthorized();
    }

    @Test
    void testPublicEndpointContract() {
        this.webTestClient.get().uri("/estoque-api/health").exchange().expectStatus().isNotFound();

        this.webTestClient.post().uri("/estoque-api/produtos")
                .header("Content-Type", "application/json").bodyValue("{}").exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void testOptionsMethodContract() {

        this.webTestClient.options().uri("/actuator/health").exchange().expectStatus().isOk();


        this.webTestClient.options().uri("/estoque-api/produtos").exchange().expectStatus()
                .isNotFound();
        this.webTestClient.options().uri("/pedido-api/pedidos").exchange().expectStatus()
                .isNotFound();
    }

    @Test
    void testErrorHandlingContract() {

        this.webTestClient.get().uri("/invalid/route/that/does/not/exist").exchange().expectStatus()
                .isUnauthorized();
    }

    @Test
    void testContentTypeContract() {

        this.webTestClient.post().uri("/estoque-api/produtos")
                .header("Content-Type", "application/json").bodyValue("{\"name\":\"Produto\"}")
                .exchange().expectStatus().isNotFound();

        this.webTestClient.post().uri("/estoque-api/produtos")
                .header("Content-Type", "application/xml")
                .bodyValue("<produto><name>Produto</name></produto>").exchange().expectStatus()
                .isNotFound();
    }

    @Test
    void testHttpMethodsContract() {

        this.webTestClient.get().uri("/estoque-api/produtos").exchange().expectStatus()
                .isNotFound();


        this.webTestClient.post().uri("/estoque-api/produtos")
                .header("Content-Type", "application/json").bodyValue("{}").exchange()
                .expectStatus().isNotFound();

        this.webTestClient.put().uri("/estoque-api/produtos/1")
                .header("Content-Type", "application/json").bodyValue("{}").exchange()
                .expectStatus().isNotFound();


        this.webTestClient.delete().uri("/estoque-api/produtos/1").exchange().expectStatus()
                .isNotFound();
    }

    @Test
    void testSecurityHeadersContract() {

        this.webTestClient.get().uri("/actuator/health").exchange().expectStatus().isOk()
                .expectHeader().exists("X-Content-Type-Options").expectHeader()
                .exists("X-Frame-Options").expectHeader().exists("X-XSS-Protection");
    }

    @Test
    void testCacheHeadersContract() {

        this.webTestClient.get().uri("/actuator/health").exchange().expectStatus().isOk()
                .expectHeader().exists("Cache-Control");
    }

    @Test
    void testCharsetContract() {

        this.webTestClient.get().uri("/actuator/health").exchange().expectStatus().isOk()
                .expectHeader().contentType("application/vnd.spring-boot.actuator.v3+json");

    }
}
