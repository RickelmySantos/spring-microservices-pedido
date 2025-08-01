package com.rsdesenvolvimento.api_gateway.config;

import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

class KeycloakRoleConverterTest {

  private KeycloakRoleConverter converter;

  @BeforeEach
  void setUp() {
    this.converter = new KeycloakRoleConverter();
  }

  @Test
  void testConvertWithValidRoles() {
    // Given
    Map<String, Object> realmAccess = Map.of("roles", List.of("USER", "ADMIN", "MODERATOR"));

    Jwt jwt = Jwt.withTokenValue("token").header("alg", "RS256").claim("sub", "user123")
        .claim("realm_access", realmAccess).issuedAt(Instant.now())
        .expiresAt(Instant.now().plusSeconds(3600)).build();

    // When
    Collection<GrantedAuthority> authorities = this.converter.convert(jwt);

    // Then
    Assertions.assertNotNull(authorities);
    Assertions.assertEquals(3, authorities.size());

    Assertions.assertTrue(authorities.contains(new SimpleGrantedAuthority("ROLE_USER")));
    Assertions.assertTrue(authorities.contains(new SimpleGrantedAuthority("ROLE_ADMIN")));
    Assertions.assertTrue(authorities.contains(new SimpleGrantedAuthority("ROLE_MODERATOR")));
  }

  @Test
  void testConvertWithEmptyRoles() {
    // Given
    Map<String, Object> realmAccess = Map.of("roles", List.of());

    Jwt jwt = Jwt.withTokenValue("token").header("alg", "RS256").claim("sub", "user123")
        .claim("realm_access", realmAccess).issuedAt(Instant.now())
        .expiresAt(Instant.now().plusSeconds(3600)).build();

    // When
    Collection<GrantedAuthority> authorities = this.converter.convert(jwt);

    // Then
    Assertions.assertNotNull(authorities);
    Assertions.assertTrue(authorities.isEmpty());
  }

  @Test
  void testConvertWithNullRealmAccess() {
    // Given
    Jwt jwt = Jwt.withTokenValue("token").header("alg", "RS256").claim("sub", "user123")
        .claim("realm_access", null).issuedAt(Instant.now())
        .expiresAt(Instant.now().plusSeconds(3600)).build();

    // When
    Collection<GrantedAuthority> authorities = this.converter.convert(jwt);

    // Then
    Assertions.assertNotNull(authorities);
    Assertions.assertTrue(authorities.isEmpty());
  }

  @Test
  void testConvertWithMissingRealmAccess() {
    // Given
    Jwt jwt = Jwt.withTokenValue("token").header("alg", "RS256").claim("sub", "user123")
        .issuedAt(Instant.now()).expiresAt(Instant.now().plusSeconds(3600)).build();

    // When
    Collection<GrantedAuthority> authorities = this.converter.convert(jwt);

    // Then
    Assertions.assertNotNull(authorities);
    Assertions.assertTrue(authorities.isEmpty());
  }

  @Test
  void testConvertWithEmptyRealmAccess() {
    // Given
    Map<String, Object> realmAccess = Map.of();

    Jwt jwt = Jwt.withTokenValue("token").header("alg", "RS256").claim("sub", "user123")
        .claim("realm_access", realmAccess).issuedAt(Instant.now())
        .expiresAt(Instant.now().plusSeconds(3600)).build();

    // When
    Collection<GrantedAuthority> authorities = this.converter.convert(jwt);

    // Then
    Assertions.assertNotNull(authorities);
    Assertions.assertTrue(authorities.isEmpty());
  }

  @Test
  void testConvertWithSingleRole() {
    // Given
    Map<String, Object> realmAccess = Map.of("roles", List.of("USER"));

    Jwt jwt = Jwt.withTokenValue("token").header("alg", "RS256").claim("sub", "user123")
        .claim("realm_access", realmAccess).issuedAt(Instant.now())
        .expiresAt(Instant.now().plusSeconds(3600)).build();

    // When
    Collection<GrantedAuthority> authorities = this.converter.convert(jwt);

    // Then
    Assertions.assertNotNull(authorities);
    Assertions.assertEquals(1, authorities.size());
    Assertions.assertTrue(authorities.contains(new SimpleGrantedAuthority("ROLE_USER")));
  }

  @Test
  void testRolePrefixIsAdded() {
    // Given
    Map<String, Object> realmAccess = Map.of("roles", List.of("custom_role"));

    Jwt jwt = Jwt.withTokenValue("token").header("alg", "RS256").claim("sub", "user123")
        .claim("realm_access", realmAccess).issuedAt(Instant.now())
        .expiresAt(Instant.now().plusSeconds(3600)).build();

    // When
    Collection<GrantedAuthority> authorities = this.converter.convert(jwt);

    // Then
    Assertions.assertNotNull(authorities);
    Assertions.assertEquals(1, authorities.size());
    Assertions.assertTrue(authorities.contains(new SimpleGrantedAuthority("ROLE_custom_role")));
  }
}
