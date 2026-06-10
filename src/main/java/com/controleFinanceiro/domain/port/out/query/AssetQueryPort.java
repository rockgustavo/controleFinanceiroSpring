package com.controleFinanceiro.domain.port.out.query;

import com.controleFinanceiro.application.dto.response.AssetResponse;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AssetQueryPort {
    List<AssetResponse> findAllActive();
    Optional<AssetResponse> findActiveById(UUID id);
}
