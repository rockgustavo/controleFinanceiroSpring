package com.controleFinanceiro.application.dto.response;

import com.controleFinanceiro.domain.model.Asset;

import java.util.UUID;

public record AssetResponse(UUID id, String name, String type, String ticker, String notes) {

    public static AssetResponse from(Asset asset) {
        return new AssetResponse(
                asset.getId(),
                asset.getName(),
                asset.getType().name(),
                asset.getTicker(),
                asset.getNotes()
        );
    }
}
