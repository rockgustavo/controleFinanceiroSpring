package com.controleFinanceiro.application.usecase.command.asset;

import com.controleFinanceiro.application.dto.request.UpdateAssetRequest;

public record UpdateAssetCommand(String name, String ticker, String notes) {

    public static UpdateAssetCommand from(UpdateAssetRequest req) {
        return new UpdateAssetCommand(req.name(), req.ticker(), req.notes());
    }
}
