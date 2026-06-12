package com.controleFinanceiro.application.usecase.query.snapshot;

import com.controleFinanceiro.application.dto.response.SnapshotResponse;
import com.controleFinanceiro.domain.exception.NotFoundException;
import com.controleFinanceiro.domain.port.in.query.BuscarSnapshotPort;
import com.controleFinanceiro.domain.port.out.query.SnapshotQueryPort;
import com.controleFinanceiro.domain.shared.ErrorCodes;
import com.controleFinanceiro.domain.shared.MessageKeys;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BuscarSnapshotUseCase implements BuscarSnapshotPort {

    private final SnapshotQueryPort queryPort;

    @Override
    public SnapshotResponse execute(UUID id) {
        return queryPort.findById(id)
                .orElseThrow(() -> new NotFoundException(MessageKeys.SNAPSHOT_NAO_ENCONTRADO, ErrorCodes.SNAPSHOT_NAO_ENCONTRADO, id));
    }
}
