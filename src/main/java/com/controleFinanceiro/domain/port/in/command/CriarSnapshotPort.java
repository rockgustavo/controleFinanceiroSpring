package com.controleFinanceiro.domain.port.in.command;

import com.controleFinanceiro.application.usecase.command.snapshot.CriarSnapshotCommand;
import com.controleFinanceiro.domain.model.Snapshot;

public interface CriarSnapshotPort {
    Snapshot execute(CriarSnapshotCommand command);
}
