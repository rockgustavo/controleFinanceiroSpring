package com.controleFinanceiro.domain.port.out.messaging;

import com.controleFinanceiro.domain.event.AtivoAlteradoEvent;
import com.controleFinanceiro.domain.event.SnapshotCriadoEvent;

public interface EventPublisherPort {
    void publish(AtivoAlteradoEvent event);
    void publish(SnapshotCriadoEvent event);
}
