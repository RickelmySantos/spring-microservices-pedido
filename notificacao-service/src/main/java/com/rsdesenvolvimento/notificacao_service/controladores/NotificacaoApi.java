package com.rsdesenvolvimento.notificacao_service.controladores;

import com.rsdesenvolvimento.notificacao_service.service.NotificacaoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/notificacoes")
public class NotificacaoApi {

  private final NotificacaoService notificacaoService;


  @PostMapping("/enviar-notificacao")
  public String enviarNotificacao(@RequestBody String email) {
    try {
      this.notificacaoService.enviarEmail(email, "Seu pedido foi realizado com sucesso!");
      return "Notificação enviada!";
    } catch (Exception e) {
      return "Erro ao enviar notificação: " + e.getMessage();
    }
  }
}
