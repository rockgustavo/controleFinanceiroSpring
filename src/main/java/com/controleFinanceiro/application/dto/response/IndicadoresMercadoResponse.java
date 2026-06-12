package com.controleFinanceiro.application.dto.response;

import java.math.BigDecimal;

public record IndicadoresMercadoResponse(
        BigDecimal selic,
        BigDecimal usdBrl,
        BigDecimal ibovespa,
        BigDecimal ivvb11,
        BigDecimal ipca
) {}
