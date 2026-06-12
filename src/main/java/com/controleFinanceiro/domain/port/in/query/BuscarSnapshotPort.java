package com.controleFinanceiro.domain.port.in.query;

import com.controleFinanceiro.application.dto.response.SnapshotResponse;

import java.util.UUID;

public interface BuscarSnapshotPort {
    SnapshotResponse execute(UUID id);
}
