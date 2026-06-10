package com.controleFinanceiro.domain.service.factory;

import com.controleFinanceiro.domain.model.AssetType;
import com.controleFinanceiro.domain.service.validator.AssetValidator;
import com.controleFinanceiro.domain.service.validator.RendaFixaValidator;
import com.controleFinanceiro.domain.service.validator.TickerRequiredValidator;

public class AssetValidatorFactory {

    public static AssetValidator forType(AssetType type) {
        return switch (type) {
            case RENDA_VARIAVEL, FII, ETF -> new TickerRequiredValidator();
            case RENDA_FIXA -> new RendaFixaValidator();
        };
    }
}
