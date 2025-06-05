package com.rsdesenvolvimento.pedido_service.core.config;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;

@Component
public class SecurityUtil {


    public static String getUserId() {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth instanceof JwtAuthenticationToken) {
            var token = (JwtAuthenticationToken) auth;
            return (String) token.getTokenAttributes().get("sub");
        }
        return null;
    }

    public static String getUsername() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth instanceof JwtAuthenticationToken) {
            var token = (JwtAuthenticationToken) auth;
            return (String) token.getTokenAttributes().get("preferred_username");
        }
        return null;
    }

    public static String getUserEmail() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth instanceof JwtAuthenticationToken) {
            var token = (JwtAuthenticationToken) auth;
            return (String) token.getTokenAttributes().get("email");
        }
        return null;
    }
}
