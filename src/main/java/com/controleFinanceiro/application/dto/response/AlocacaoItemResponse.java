package com.controleFinanceiro.application.dto.response;

import java.math.BigDecimal;

public record AlocacaoItemResponse(
        String tipo,
        BigDecimal totalLiq,
        BigDecimal percentual
) {}
