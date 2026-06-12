package com.controleFinanceiro.application.usecase.query.snapshot;

import org.springframework.stereotype.Service;

import com.controleFinanceiro.application.dto.response.SnapshotResponse;
import com.controleFinanceiro.domain.exception.NotFoundException;
import com.controleFinanceiro.domain.port.in.query.BuscarUltimoSnapshotPort;
import com.controleFinanceiro.domain.port.out.query.SnapshotQueryPort;
import com.controleFinanceiro.domain.shared.ErrorCodes;
import com.controleFinanceiro.domain.shared.MessageKeys;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BuscarUltimoSnapshotUseCase implements BuscarUltimoSnapshotPort {

    private final SnapshotQueryPort queryPort;

    @Override
    public SnapshotResponse execute() {
        return queryPort.findLatest()
                .orElseThrow(() -> new NotFoundException(MessageKeys.SNAPSHOT_NENHUM_REGISTRADO, ErrorCodes.NENHUM_SNAPSHOT));
    }
}
