package com.controleFinanceiro.domain.service.strategy;

import java.math.BigDecimal;

public interface ReturnCalculationStrategy {
    BigDecimal calculate(BigDecimal previous, BigDecimal current, int months);
}
