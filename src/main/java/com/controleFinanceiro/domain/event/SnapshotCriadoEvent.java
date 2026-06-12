package com.controleFinanceiro.domain.event;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

public record SnapshotCriadoEvent(UUID snapshotId, LocalDate data, Instant ocorridoEm) {

    public static SnapshotCriadoEvent of(UUID snapshotId, LocalDate data) {
        return new SnapshotCriadoEvent(snapshotId, data, Instant.now());
    }
}
