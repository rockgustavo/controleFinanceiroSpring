package com.controleFinanceiro.application.dto.response;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

public record SnapshotResumoResponse(
        UUID id,
        LocalDate data,
        BigDecimal totalLiq,
        int numeroPosicoes,
        Instant criadoEm
) {}
