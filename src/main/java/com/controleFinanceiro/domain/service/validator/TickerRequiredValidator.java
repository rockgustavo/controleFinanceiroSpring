package com.controleFinanceiro.domain.service.validator;

import com.controleFinanceiro.domain.exception.DomainException;
import com.controleFinanceiro.domain.model.Asset;
import com.controleFinanceiro.domain.shared.ErrorCodes;
import com.controleFinanceiro.domain.shared.MessageKeys;

public class TickerRequiredValidator implements AssetValidator {

    @Override
    public void validate(Asset asset) {
        if (asset.getTicker() == null || asset.getTicker().isBlank()) {
            throw new DomainException(MessageKeys.ASSET_TICKER_REQUIRED, ErrorCodes.TICKER_REQUIRED, asset.getType().name());
        }
    }
}
