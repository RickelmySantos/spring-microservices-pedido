package com.rsdesenvolvimento.pedido_service.core.config;

import com.rsdesenvolvimento.pedido_service.core.ports.UsuarioPort;
import com.rsdesenvolvimento.pedido_service.modelo.entidades.Usuario;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;

@Component
public class SecurityUtil implements UsuarioPort {

    @Override
    public Usuario buscarUsuario() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth instanceof JwtAuthenticationToken token) {
            return new Usuario((String) token.getTokenAttributes().get("sub"),
                    (String) token.getTokenAttributes().get("preferred_username"),
                    (String) token.getTokenAttributes().get("email"));
        }
        return null;
    }


}
