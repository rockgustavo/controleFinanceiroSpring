package com.controleFinanceiro.domain.model;

import java.math.BigDecimal;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PosicaoTest {

    @Test
    void of_calcula_totalBruto_como_quantidade_vezes_precoUnit() {
        var ativoId = UUID.randomUUID();

        var posicao = Posicao.of(ativoId, new BigDecimal("10"), new BigDecimal("50.25"),
                new BigDecimal("480"), "CDB 110% CDI");

        assertThat(posicao.getTotalBruto()).isEqualByComparingTo(new BigDecimal("502.50"));
        assertThat(posicao.getAtivoId()).isEqualTo(ativoId);
        assertThat(posicao.getQuantidade()).isEqualByComparingTo(new BigDecimal("10"));
        assertThat(posicao.getPrecoUnit()).isEqualByComparingTo(new BigDecimal("50.25"));
        assertThat(posicao.getTotalLiq()).isEqualByComparingTo(new BigDecimal("480"));
        assertThat(posicao.getTaxa()).isEqualTo("CDB 110% CDI");
    }

    @Test
    void of_totalBruto_independe_do_valor_enviado_pelo_cliente() {
        var posicao = Posicao.of(UUID.randomUUID(), new BigDecimal("5"), new BigDecimal("100"),
                new BigDecimal("450"), null);

        assertThat(posicao.getTotalBruto()).isEqualByComparingTo(new BigDecimal("500"));
    }

    @Test
    void reconstitute_aceita_totalBruto_como_armazenado_sem_recalcular() {
        var totalBrutoArmazenado = new BigDecimal("999.99");

        var posicao = Posicao.reconstitute(UUID.randomUUID(), new BigDecimal("5"), new BigDecimal("100"),
                totalBrutoArmazenado, new BigDecimal("950"), null);

        assertThat(posicao.getTotalBruto()).isEqualByComparingTo(totalBrutoArmazenado);
    }
}
