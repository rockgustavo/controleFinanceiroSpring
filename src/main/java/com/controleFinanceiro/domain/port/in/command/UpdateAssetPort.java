package com.controleFinanceiro.domain.port.in.command;

import com.controleFinanceiro.application.usecase.command.asset.UpdateAssetCommand;
import com.controleFinanceiro.domain.model.Asset;

import java.util.UUID;

public interface UpdateAssetPort {
    Asset execute(UUID id, UpdateAssetCommand command);
}
