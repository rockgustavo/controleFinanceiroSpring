package com.controleFinanceiro.application.usecase.command.snapshot;

import com.controleFinanceiro.domain.exception.DomainException;
import com.controleFinanceiro.domain.shared.ErrorCodes;
import com.controleFinanceiro.domain.shared.MessageKeys;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class SemPosicoeDuplicadaHandler extends SnapshotValidationHandler {

    @Override
    protected void validar(CriarSnapshotCommand command) {
        Set<UUID> vistos = new HashSet<>();
        command.posicoes().forEach(p -> {
            if (!vistos.add(p.ativoId())) {
                throw new DomainException(MessageKeys.SNAPSHOT_POSICAO_DUPLICADA, ErrorCodes.POSICAO_DUPLICADA, p.ativoId());
            }
        });
    }
}
