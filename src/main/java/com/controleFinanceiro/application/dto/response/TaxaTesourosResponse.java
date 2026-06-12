package com.controleFinanceiro.application.dto.response;

import java.math.BigDecimal;

public record TaxaTesourosResponse(String tipo, BigDecimal valor, String referencia) {}
