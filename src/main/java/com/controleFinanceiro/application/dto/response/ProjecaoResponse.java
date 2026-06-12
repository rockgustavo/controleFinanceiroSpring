package com.controleFinanceiro.application.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ProjecaoResponse(
        LocalDate dataReferencia,
        BigDecimal totalLiqAtual,
        BigDecimal taxaMensalPercentual,
        int meses,
        BigDecimal totalProjetado,
        BigDecimal rendimentoProjetado
) {}
