package com.controleFinanceiro.domain.service.strategy;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class CagrStrategy implements ReturnCalculationStrategy {

    @Override
    public BigDecimal calculate(BigDecimal previous, BigDecimal current, int months) {
        double ratio = current.divide(previous, 8, RoundingMode.HALF_UP).doubleValue();
        return BigDecimal.valueOf(Math.pow(ratio, 12.0 / months) - 1)
                .multiply(new BigDecimal("100"))
                .setScale(4, RoundingMode.HALF_UP);
    }
}
