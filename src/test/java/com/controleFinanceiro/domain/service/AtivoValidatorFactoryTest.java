package com.controleFinanceiro.domain.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import com.controleFinanceiro.domain.model.Ativo;
import com.controleFinanceiro.domain.model.TipoAtivo;
import com.controleFinanceiro.domain.service.factory.AtivoValidatorFactory;
import com.controleFinanceiro.domain.service.validator.TickerObrigatorioValidator;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

class AtivoValidatorFactoryTest {

    @ParameterizedTest
    @EnumSource(value = TipoAtivo.class, names = {"RENDA_VARIAVEL", "FII", "ETF"})
    void forType_retorna_TickerObrigatorioValidator_para_tipos_com_ticker(TipoAtivo tipo) {
        assertThat(AtivoValidatorFactory.forType(tipo))
                .isInstanceOf(TickerObrigatorioValidator.class);
    }

    @Test
    void forType_para_RENDA_FIXA_nao_exige_ticker() {
        var rendaFixaSemTicker = Ativo.create("Tesouro Selic", TipoAtivo.RENDA_FIXA, null, null);

        assertThatCode(() -> AtivoValidatorFactory.forType(TipoAtivo.RENDA_FIXA).validate(rendaFixaSemTicker))
                .doesNotThrowAnyException();
    }
}
