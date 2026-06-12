package com.controleFinanceiro.domain.port.in.query;

import java.util.List;
import java.util.UUID;

import com.controleFinanceiro.application.dto.response.AlocacaoItemResponse;

public interface BuscarAlocacaoSnapshotPort {
    List<AlocacaoItemResponse> execute(UUID id);
}
