package com.controleFinanceiro.domain.event;

import java.time.Instant;
import java.util.UUID;

public record AtivoAlteradoEvent(UUID ativoId, String acao, Instant ocorridoEm) {

    public static AtivoAlteradoEvent created(UUID id) {
        return new AtivoAlteradoEvent(id, "CRIADO", Instant.now());
    }

    public static AtivoAlteradoEvent updated(UUID id) {
        return new AtivoAlteradoEvent(id, "ATUALIZADO", Instant.now());
    }
}
