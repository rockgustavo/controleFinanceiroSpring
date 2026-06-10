package com.controleFinanceiro.application.dto.request;

import jakarta.validation.constraints.Size;

public record UpdateAssetRequest(
        @Size(max = 100) String name,
        @Size(max = 10) String ticker,
        String notes
) {}
