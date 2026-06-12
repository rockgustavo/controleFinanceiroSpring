package com.controleFinanceiro.application.dto.response;

import java.math.BigDecimal;
import java.util.UUID;

public record PosicaoResponse(
        UUID ativoId,
        String nomeAtivo,
        String tipoAtivo,
        String ticker,
        BigDecimal quantidade,
        BigDecimal precoUnit,
        BigDecimal totalBruto,
        BigDecimal totalLiq,
        String taxa
) {}
