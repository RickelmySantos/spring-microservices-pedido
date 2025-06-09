package com.rsdesenvolvimento.pedido_service.core.config;

import com.rsdesenvolvimento.pedido_service.core.ports.UsuarioPort;
import com.rsdesenvolvimento.pedido_service.modelo.entidades.Usuario;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;

@Component
public class SecurityUtil implements UsuarioPort {


    // public static String getUserId() {

    // Authentication auth = SecurityContextHolder.getContext().getAuthentication();

    // if (auth instanceof JwtAuthenticationToken) {
    // var token = (JwtAuthenticationToken) auth;
    // return (String) token.getTokenAttributes().get("sub");
    // }
    // return null;
    // }

    // public static String getUsername() {
    // Authentication auth = SecurityContextHolder.getContext().getAuthentication();

    // if (auth instanceof JwtAuthenticationToken) {
    // var token = (JwtAuthenticationToken) auth;
    // return (String) token.getTokenAttributes().get("preferred_username");
    // }
    // return null;
    // }

    // public static String getUserEmail() {
    // Authentication auth = SecurityContextHolder.getContext().getAuthentication();

    // if (auth instanceof JwtAuthenticationToken) {
    // var token = (JwtAuthenticationToken) auth;
    // return (String) token.getTokenAttributes().get("email");
    // }
    // return null;
    // }

    @Override
    public Usuario buscarUsuario() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth instanceof JwtAuthenticationToken) {
            var tokrn = (JwtAuthenticationToken) auth;
            return new Usuario((String) tokrn.getTokenAttributes().get("sub"),
                    (String) tokrn.getTokenAttributes().get("preferred_username"),
                    (String) tokrn.getTokenAttributes().get("email"));
        }
        return null;
    }


}
