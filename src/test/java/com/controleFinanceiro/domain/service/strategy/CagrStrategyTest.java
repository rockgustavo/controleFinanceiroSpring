package com.controleFinanceiro.domain.service.strategy;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

class CagrStrategyTest {

    private final CagrStrategy strategy = new CagrStrategy();

    @Test
    void calculate_retorna_taxa_anualizada_para_1_ano() {
        // (110/100)^(12/12) - 1 = 10%
        var resultado = strategy.calculate(new BigDecimal("100"), new BigDecimal("110"), 12);
        assertThat(resultado).isEqualByComparingTo(new BigDecimal("10.0000"));
    }

    @Test
    void calculate_anualiza_rendimento_de_6_meses() {
        // (110/100)^(12/6) - 1 = 1.1^2 - 1 = 21%
        var resultado = strategy.calculate(new BigDecimal("100"), new BigDecimal("110"), 6);
        assertThat(resultado).isEqualByComparingTo(new BigDecimal("21.0000"));
    }

    @Test
    void calculate_anualiza_rendimento_de_2_anos() {
        // (121/100)^(12/24) - 1 = sqrt(1.21) - 1 = 1.1 - 1 = 10%
        var resultado = strategy.calculate(new BigDecimal("100"), new BigDecimal("121"), 24);
        assertThat(resultado).isEqualByComparingTo(new BigDecimal("10.0000"));
    }

    @Test
    void calculate_retorna_4_casas_decimais() {
        var resultado = strategy.calculate(new BigDecimal("100"), new BigDecimal("110"), 12);
        assertThat(resultado.scale()).isEqualTo(4);
    }
}
