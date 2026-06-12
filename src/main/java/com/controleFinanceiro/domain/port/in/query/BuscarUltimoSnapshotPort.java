package com.controleFinanceiro.domain.port.in.query;

import com.controleFinanceiro.application.dto.response.SnapshotResponse;

public interface BuscarUltimoSnapshotPort {
    SnapshotResponse execute();
}
