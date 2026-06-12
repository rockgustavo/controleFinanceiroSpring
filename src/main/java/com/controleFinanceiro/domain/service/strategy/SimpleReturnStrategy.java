package com.controleFinanceiro.domain.service.strategy;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class SimpleReturnStrategy implements ReturnCalculationStrategy {

    @Override
    public BigDecimal calculate(BigDecimal previous, BigDecimal current, int months) {
        return current.divide(previous, 8, RoundingMode.HALF_UP)
                .subtract(BigDecimal.ONE)
                .multiply(new BigDecimal("100"))
                .setScale(4, RoundingMode.HALF_UP);
    }
}
