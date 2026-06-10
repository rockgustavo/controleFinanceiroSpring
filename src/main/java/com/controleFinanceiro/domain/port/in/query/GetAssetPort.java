package com.controleFinanceiro.domain.port.in.query;

import com.controleFinanceiro.application.dto.response.AssetResponse;

import java.util.UUID;

public interface GetAssetPort {
    AssetResponse execute(UUID id);
}
