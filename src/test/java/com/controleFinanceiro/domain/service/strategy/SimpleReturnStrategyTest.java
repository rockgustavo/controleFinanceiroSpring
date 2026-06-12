package com.controleFinanceiro.domain.service.strategy;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

class SimpleReturnStrategyTest {

    private final SimpleReturnStrategy strategy = new SimpleReturnStrategy();

    @Test
    void calculate_retorna_rendimento_positivo() {
        var resultado = strategy.calculate(new BigDecimal("100"), new BigDecimal("110"), 12);
        assertThat(resultado).isEqualByComparingTo(new BigDecimal("10.0000"));
    }

    @Test
    void calculate_retorna_rendimento_negativo() {
        var resultado = strategy.calculate(new BigDecimal("100"), new BigDecimal("90"), 12);
        assertThat(resultado).isEqualByComparingTo(new BigDecimal("-10.0000"));
    }

    @Test
    void calculate_retorna_zero_quando_sem_variacao() {
        var resultado = strategy.calculate(new BigDecimal("100"), new BigDecimal("100"), 12);
        assertThat(resultado).isEqualByComparingTo(new BigDecimal("0.0000"));
    }

    @Test
    void calculate_ignora_parametro_meses() {
        var com1Mes = strategy.calculate(new BigDecimal("100"), new BigDecimal("110"), 1);
        var com24Meses = strategy.calculate(new BigDecimal("100"), new BigDecimal("110"), 24);
        assertThat(com1Mes).isEqualByComparingTo(com24Meses);
    }

    @Test
    void calculate_retorna_4_casas_decimais() {
        var resultado = strategy.calculate(new BigDecimal("300"), new BigDecimal("301"), 1);
        assertThat(resultado.scale()).isEqualTo(4);
    }
}
