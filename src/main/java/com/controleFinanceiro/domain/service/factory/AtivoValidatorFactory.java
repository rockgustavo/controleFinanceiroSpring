package com.controleFinanceiro.domain.service.factory;

import com.controleFinanceiro.domain.model.TipoAtivo;
import com.controleFinanceiro.domain.service.validator.AtivoValidator;
import com.controleFinanceiro.domain.service.validator.RendaFixaValidator;
import com.controleFinanceiro.domain.service.validator.TickerObrigatorioValidator;

public class AtivoValidatorFactory {

    public static AtivoValidator forType(TipoAtivo tipo) {
        return switch (tipo) {
            case RENDA_VARIAVEL, FII, ETF -> new TickerObrigatorioValidator();
            case RENDA_FIXA -> new RendaFixaValidator();
        };
    }
}
