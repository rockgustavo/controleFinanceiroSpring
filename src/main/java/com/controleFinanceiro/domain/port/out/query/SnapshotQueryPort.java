package com.controleFinanceiro.domain.port.out.query;

import com.controleFinanceiro.application.dto.response.SnapshotResponse;
import com.controleFinanceiro.application.dto.response.SnapshotResumoResponse;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SnapshotQueryPort {
    List<SnapshotResumoResponse> findAll();
    Optional<SnapshotResponse> findById(UUID id);
    Optional<SnapshotResponse> findLatest();
}
