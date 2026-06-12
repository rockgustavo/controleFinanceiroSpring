package com.controleFinanceiro.domain.port.out.command;

import com.controleFinanceiro.domain.model.Snapshot;

import java.time.LocalDate;

public interface SnapshotRepositoryPort {
    Snapshot save(Snapshot snapshot);
    boolean existsByData(LocalDate data);
}
