package com.rsdesenvolvimento.pagamento_service.services;

import com.rsdesenvolvimento.pagamento_service.config.NotificacaoProducer;
import com.rsdesenvolvimento.pagamento_service.config.PedidoClient;
import com.rsdesenvolvimento.pagamento_service.modelo.dtos.PagamentoRequestDto;
import com.rsdesenvolvimento.pagamento_service.modelo.entidades.Pagamento;
import com.rsdesenvolvimento.pagamento_service.repositorios.PagamentoRepository;
import java.time.LocalDateTime;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PagamentoService {

  private static final ExecutorService executor = Executors.newFixedThreadPool(2);
  private static final Logger logger = LoggerFactory.getLogger(PagamentoService.class);

  private final PagamentoRepository repository;
  private final PedidoClient pedidoClient;
  private final NotificacaoProducer notificacaoProducer;

  public Pagamento processarPagamento(PagamentoRequestDto dto) {
    Pagamento pagamento = Pagamento.builder().pedidoId(dto.getPedidoId()).valor(dto.getValor())
        .dataHora(LocalDateTime.now()).status("FINALIZADO").build();

    this.repository.save(pagamento);

    this.pedidoClient.atualizarStatus(dto.getPedidoId(), "FINALIZADO");

    this.notificacaoProducer
        .enviarNotificacao("Pagamento do pedido " + dto.getPedidoId() + " realizado com sucesso!");

    // new Thread(() -> {
    // try {
    // Thread.sleep(5000);
    // String mensagem = "EMAIL_PAGAMENTO_CONFIRMADO:" + dto.getPedidoId();
    // this.notificacaoProducer.enviarNotificacao(mensagem);
    // } catch (InterruptedException e) {
    // e.printStackTrace();
    // }
    // }).start();
    CompletableFuture.runAsync(() -> {
      try {
        Thread.sleep(5000);
        String mensagem = "EMAIL_PAGAMENTO_CONFIRMADO:" + dto.getPedidoId();
        this.notificacaoProducer.enviarNotificacao(mensagem);
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
        PagamentoService.logger.error("Erro ao processar notificação: {}", e.getMessage());
      }
    }, PagamentoService.executor);

    return pagamento;
  }


}
