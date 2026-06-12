package com.controleFinanceiro.infrastructure.adapter.out.messaging;

import java.util.UUID;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import com.controleFinanceiro.domain.event.AtivoAlteradoEvent;
import com.controleFinanceiro.domain.event.SnapshotCriadoEvent;
import com.controleFinanceiro.domain.port.out.messaging.EventPublisherPort;
import com.controleFinanceiro.infrastructure.config.RabbitConfig;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class RabbitEventPublisher implements EventPublisherPort {

    private final RabbitTemplate rabbitTemplate;

    @Override
    public void publish(AtivoAlteradoEvent event) {
        enviar(RabbitConfig.ROUTING_KEY_ATIVO_ALTERADO, event, event.ativoId());
    }

    @Override
    public void publish(SnapshotCriadoEvent event) {
        enviar(RabbitConfig.ROUTING_KEY_SNAPSHOT_CRIADO, event, event.snapshotId());
    }

    private void enviar(String routingKey, Object event, UUID recursoId) {
        try {
            rabbitTemplate.convertAndSend(RabbitConfig.EXCHANGE, routingKey, event);
        } catch (Exception e) {
            log.error("Falha ao publicar evento {} para o recurso {}: {}", routingKey, recursoId, e.getMessage());
        }
    }
}
