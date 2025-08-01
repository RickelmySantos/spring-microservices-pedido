package com.rsdesenvolvimento.api_gateway.config;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.cors.reactive.CorsConfigurationSource;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class SecurityConfigTest {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private SecurityConfig securityConfig;

    @Test
    void testCorsConfigurationSource() {
        CorsConfigurationSource corsConfigurationSource =
                this.securityConfig.corsConfigurationSource();
        Assertions.assertNotNull(corsConfigurationSource);
    }

    @Test
    void testPublicEndpointsAllowedWithoutAuthentication() {

        this.webTestClient.get().uri("/actuator/health").exchange().expectStatus().isOk();

        this.webTestClient.get().uri("/estoque-api/test").exchange().expectStatus().isNotFound();
    }

    @Test
    void testOptionsRequestsAllowed() {
        this.webTestClient.options().uri("/any-endpoint").exchange().expectStatus().isNotFound();
    }

    @Test
    void testProtectedEndpointsRequireAuthentication() {
        this.webTestClient.get().uri("/protected-endpoint").exchange().expectStatus()
                .isUnauthorized();
    }

    @Test
    void testCorsHeadersPresent() {
        this.webTestClient.options().uri("/test").header("Origin", "http://localhost:4200")
                .header("Access-Control-Request-Method", "GET")
                .header("Access-Control-Request-Headers", "Authorization").exchange().expectStatus()
                .isOk().expectHeader().exists("Access-Control-Allow-Origin").expectHeader()
                .exists("Access-Control-Allow-Methods").expectHeader()
                .exists("Access-Control-Allow-Headers");
    }
}
