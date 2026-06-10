package com.controleFinanceiro.domain.port.in.command;

import com.controleFinanceiro.application.usecase.command.asset.CreateAssetCommand;
import com.controleFinanceiro.domain.model.Asset;

public interface CreateAssetPort {
    Asset execute(CreateAssetCommand command);
}
