package com.controleFinanceiro.application.usecase.command.asset;

import com.controleFinanceiro.application.dto.request.CreateAssetRequest;
import com.controleFinanceiro.application.exception.ValidationException;
import com.controleFinanceiro.domain.model.AssetType;
import com.controleFinanceiro.domain.shared.ErrorCodes;
import com.controleFinanceiro.domain.shared.MessageKeys;

public record CreateAssetCommand(String name, AssetType type, String ticker, String notes) {

    public CreateAssetCommand {
        if (name == null) throw new ValidationException(MessageKeys.ASSET_NAME_REQUIRED, ErrorCodes.ASSET_NAME_REQUIRED);
        if (type == null) throw new ValidationException(MessageKeys.ASSET_TYPE_REQUIRED, ErrorCodes.ASSET_TYPE_REQUIRED);
        if ((type == AssetType.RENDA_VARIAVEL || type == AssetType.FII || type == AssetType.ETF)
                && (ticker == null || ticker.isBlank())) {
            throw new ValidationException(MessageKeys.ASSET_TICKER_REQUIRED, ErrorCodes.TICKER_REQUIRED, type.name());
        }
    }

    public static CreateAssetCommand from(CreateAssetRequest req) {
        return new CreateAssetCommand(req.name(), req.type(), req.ticker(), req.notes());
    }
}
