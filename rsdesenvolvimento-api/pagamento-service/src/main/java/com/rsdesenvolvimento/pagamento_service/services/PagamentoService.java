package com.rsdesenvolvimento.pagamento_service.services;

import com.rsdesenvolvimento.pagamento_service.config.EstoqueFeignClient;
import com.rsdesenvolvimento.pagamento_service.config.NotificacaoProducer;
import com.rsdesenvolvimento.pagamento_service.config.PedidoClient;
import com.rsdesenvolvimento.pagamento_service.modelo.dtos.PagamentoRequestDto;
import com.rsdesenvolvimento.pagamento_service.modelo.entidades.Pagamento;
import com.rsdesenvolvimento.pagamento_service.repositorios.PagamentoRepository;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PagamentoService {

  // private static final ExecutorService executor = Executors.newFixedThreadPool(2);
  // private static final Logger logger = LoggerFactory.getLogger(PagamentoService.class);

  private final PagamentoRepository repository;
  private final PedidoClient pedidoClient;
  private final NotificacaoProducer notificacaoProducer;
  private final EstoqueFeignClient estoqueFeignClient;


  public Pagamento processarPagamento(PagamentoRequestDto dto) {
    Pagamento pagamento = Pagamento.builder().pedidoId(dto.getPedidoId()).valor(dto.getValor())
        .dataHora(LocalDateTime.now()).status("FINALIZADO").build();

    this.repository.save(pagamento);

    this.pedidoClient.atualizarStatus(dto.getPedidoId(), "FINALIZADO");

    this.estoqueFeignClient.reservarEstoque(dto.getItens());


    this.notificacaoProducer
        .enviarNotificacao("Pagamento do pedido " + dto.getPedidoId() + " realizado com sucesso!");
    // CompletableFuture.runAsync(() -> {
    // try {
    // Thread.sleep(5000);
    // String mensagem = "EMAIL_PAGAMENTO_CONFIRMADO:" + dto.getPedidoId();
    // this.notificacaoProducer.enviarNotificacao(mensagem);
    // } catch (InterruptedException e) {
    // Thread.currentThread().interrupt();
    // PagamentoService.logger.error("Erro ao processar notificação: {}", e.getMessage());
    // }
    // }, PagamentoService.executor);
    this.notificacaoProducer.enviarNotificacaoComDelay("EMAIL_PAGAMENTO_CONFIRMADO", 10000);

    return pagamento;
  }


}
