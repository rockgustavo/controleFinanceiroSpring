package com.controleFinanceiro.application.dto.request;

import com.controleFinanceiro.domain.model.AssetType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateAssetRequest(
        @NotBlank @Size(max = 100) String name,
        @NotNull AssetType type,
        @Size(max = 10) String ticker,
        String notes
) {}
