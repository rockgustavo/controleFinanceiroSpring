package com.controleFinanceiro.infrastructure.adapter.out.messaging;

import com.controleFinanceiro.domain.event.AssetChangedEvent;
import com.controleFinanceiro.domain.port.out.messaging.EventPublisherPort;
import com.controleFinanceiro.infrastructure.config.RabbitConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class RabbitEventPublisher implements EventPublisherPort {

    private final RabbitTemplate rabbitTemplate;

    @Override
    public void publish(AssetChangedEvent event) {
        try {
            rabbitTemplate.convertAndSend(RabbitConfig.EXCHANGE, "asset.changed", event);
        } catch (Exception e) {
            log.error("Falha ao publicar evento para ativo {}: {}", event.assetId(), e.getMessage());
        }
    }
}
