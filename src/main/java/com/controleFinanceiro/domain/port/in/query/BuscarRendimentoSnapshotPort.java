package com.controleFinanceiro.domain.port.in.query;

import java.util.UUID;

import com.controleFinanceiro.application.dto.response.RendimentoResponse;

public interface BuscarRendimentoSnapshotPort {
    RendimentoResponse execute(UUID id, String modo);
}
