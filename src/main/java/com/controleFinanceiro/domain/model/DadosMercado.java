package com.controleFinanceiro.domain.model;

import java.math.BigDecimal;

public record DadosMercado(
        BigDecimal selic,
        BigDecimal usdBrl,
        BigDecimal ibovespa,
        BigDecimal ivvb11,
        BigDecimal ipca
) {}
