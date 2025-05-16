package com.rsdesenvolvimento.notificacao_service.service;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class NotificacaoService {

  @Value("${sendgrid.api.key}")
  private String sendGridApiKey;

  public void enviarEmail(String to, String subject, String content) {
    Email from = new Email("rickelmysantos493@gmail.com", "RSDesenvolvimento - Notificações");
    Email toEmail = new Email(to);

    Content emailContent = new Content("text/html", content);
    Mail mail = new Mail(from, subject, toEmail, emailContent);

    mail.setReplyTo(new Email("contato@rsdesenvolvimento.com"));

    SendGrid sg = new SendGrid(this.sendGridApiKey);

    Request request = new Request();

    try {
      request.setMethod(Method.POST);
      request.setEndpoint("mail/send");
      request.setBody(mail.build());

      Response response = sg.api(request);

      System.out.println("Status Code: " + response.getStatusCode());
      System.out.println("Response Body: " + response.getBody());
      System.out.println("Response Headers: " + response.getHeaders());
    } catch (Exception e) {
      e.printStackTrace();
      throw new RuntimeException("Erro ao enviar email: " + e.getMessage());
    }
  }
}
