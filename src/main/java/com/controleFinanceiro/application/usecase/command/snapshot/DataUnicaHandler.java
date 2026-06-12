package com.controleFinanceiro.application.usecase.command.snapshot;

import com.controleFinanceiro.domain.exception.ConflictException;
import com.controleFinanceiro.domain.port.out.command.SnapshotRepositoryPort;
import com.controleFinanceiro.domain.shared.ErrorCodes;
import com.controleFinanceiro.domain.shared.MessageKeys;

public class DataUnicaHandler extends SnapshotValidationHandler {

    private final SnapshotRepositoryPort repository;

    public DataUnicaHandler(SnapshotRepositoryPort repository) {
        this.repository = repository;
    }

    @Override
    protected void validar(CriarSnapshotCommand command) {
        if (repository.existsByData(command.data())) {
            throw new ConflictException(MessageKeys.SNAPSHOT_DATA_EXISTENTE, ErrorCodes.SNAPSHOT_DATA_EXISTENTE, command.data());
        }
    }
}
