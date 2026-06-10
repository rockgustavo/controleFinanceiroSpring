package com.controleFinanceiro.domain.event;

import java.time.Instant;
import java.util.UUID;

public record AssetChangedEvent(UUID assetId, String action, Instant occurredAt) {

    public static AssetChangedEvent created(UUID id) {
        return new AssetChangedEvent(id, "CREATED", Instant.now());
    }

    public static AssetChangedEvent updated(UUID id) {
        return new AssetChangedEvent(id, "UPDATED", Instant.now());
    }
}
