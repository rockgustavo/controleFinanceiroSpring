package com.controleFinanceiro.domain.service;

import org.junit.jupiter.api.Test;

import com.controleFinanceiro.domain.exception.DomainException;
import com.controleFinanceiro.domain.model.Ativo;
import com.controleFinanceiro.domain.model.TipoAtivo;
import com.controleFinanceiro.domain.service.validator.TickerObrigatorioValidator;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TickerObrigatorioValidatorTest {

    private final TickerObrigatorioValidator validator = new TickerObrigatorioValidator();

    @Test
    void validate_lanca_DomainException_quando_ticker_nulo() {
        var ativo = Ativo.create("Ação X", TipoAtivo.RENDA_VARIAVEL, null, null);

        assertThatThrownBy(() -> validator.validate(ativo))
                .isInstanceOf(DomainException.class);
    }

    @Test
    void validate_lanca_DomainException_quando_ticker_em_branco() {
        var ativo = Ativo.create("Ação X", TipoAtivo.FII, "  ", null);

        assertThatThrownBy(() -> validator.validate(ativo))
                .isInstanceOf(DomainException.class);
    }

    @Test
    void validate_nao_lanca_excecao_quando_ticker_preenchido() {
        var ativo = Ativo.create("Ação X", TipoAtivo.ETF, "IVVB11", null);

        assertThatCode(() -> validator.validate(ativo)).doesNotThrowAnyException();
    }
}
