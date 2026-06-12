package com.controleFinanceiro.application.dto.response;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record SnapshotResponse(
        UUID id,
        LocalDate data,
        String observacoes,
        BigDecimal totalBruto,
        BigDecimal totalLiq,
        List<PosicaoResponse> posicoes,
        Instant criadoEm
) {}
