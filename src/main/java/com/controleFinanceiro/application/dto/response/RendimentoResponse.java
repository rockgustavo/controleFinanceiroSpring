package com.controleFinanceiro.application.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record RendimentoResponse(
        UUID snapshotId,
        LocalDate data,
        BigDecimal totalLiqAtual,
        LocalDate dataAnterior,
        BigDecimal totalLiqAnterior,
        BigDecimal rendimentoAbsoluto,
        BigDecimal rendimentoPercentual,
        String modo
) {}
