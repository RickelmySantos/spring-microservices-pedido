package com.rsdesenvolvimento.pedido_service.core.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rsdesenvolvimento.core.events.dto.NotificacaoDto;
import com.rsdesenvolvimento.pedido_service.core.config.RabbitConfig;
import com.rsdesenvolvimento.pedido_service.core.ports.NotificacaoPort;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class NotificacaoProducer implements NotificacaoPort {

    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper objectMapper;

    public NotificacaoProducer(RabbitTemplate rabbitTemplate, ObjectMapper objectMapper) {
        this.rabbitTemplate = rabbitTemplate;
        this.objectMapper = objectMapper;
    }


    @Override
    public void enviarNotificacao(NotificacaoDto notificacao) {
        try {

            String jsonPayload = this.objectMapper.writeValueAsString(notificacao);
            NotificacaoProducer.log.info("JSON que será enviado para a fila: {}", jsonPayload);

        } catch (JsonProcessingException e) {
            NotificacaoProducer.log.error(
                    "ERRO CRÍTICO: Falha ao converter o NotificacaoDto para JSON. O objeto pode estar inválido.",
                    e);

            return;
        }

        NotificacaoProducer.log.info("Enviando objeto para Exchange: '{}', Routing Key: '{}'",
                RabbitConfig.EXCHANGE_NAME, RabbitConfig.ROUTING_KEY);
        this.rabbitTemplate.convertAndSend(RabbitConfig.EXCHANGE_NAME, RabbitConfig.ROUTING_KEY,
                notificacao);
    }
}
