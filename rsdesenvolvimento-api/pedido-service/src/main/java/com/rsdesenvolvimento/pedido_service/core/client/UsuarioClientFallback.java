// package com.rsdesenvolvimento.pedido_service.core.client;

// import com.rsdesenvolvimento.pedido_service.core.client.dtos.UsuarioDto;
// import lombok.extern.slf4j.Slf4j;
// import org.springframework.cloud.openfeign.FallbackFactory;
// import org.springframework.stereotype.Component;

// @Component
// @Slf4j
// public class UsuarioClientFallback implements FallbackFactory<UsuarioClient> {



// @Override
// public UsuarioClient create(Throwable cause) {
// return id -> {
// UsuarioClientFallback.log.warn("⚠️ Fallback acionado: {} - {}",
// cause.getClass().getSimpleName(), cause.getMessage());
// UsuarioDto fallback = new UsuarioDto();
// fallback.setId(id);
// fallback.setNome("Usuário não encontrado (via fallback factory)");
// return fallback;
// };
// }

// }
