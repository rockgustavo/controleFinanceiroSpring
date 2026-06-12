package com.controleFinanceiro.domain.port.in.query;

import com.controleFinanceiro.application.dto.response.SnapshotResumoResponse;

import java.util.List;

public interface ListarSnapshotsPort {
    List<SnapshotResumoResponse> execute();
}
