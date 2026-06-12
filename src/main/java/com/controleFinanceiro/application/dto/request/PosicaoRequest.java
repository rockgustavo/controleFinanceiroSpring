package com.controleFinanceiro.application.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.UUID;

public record PosicaoRequest(
        @NotNull UUID ativoId,
        @NotNull @DecimalMin("0.00000001") BigDecimal quantidade,
        @NotNull @DecimalMin("0.0001") BigDecimal precoUnit,
        @NotNull @DecimalMin("0") BigDecimal totalLiq,
        String taxa
) {}
