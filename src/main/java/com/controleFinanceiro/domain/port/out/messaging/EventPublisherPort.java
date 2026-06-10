package com.controleFinanceiro.domain.port.out.messaging;

import com.controleFinanceiro.domain.event.AssetChangedEvent;

public interface EventPublisherPort {
    void publish(AssetChangedEvent event);
}
