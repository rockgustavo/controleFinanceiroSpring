package com.controleFinanceiro.application.usecase.query.asset;

import com.controleFinanceiro.application.dto.response.AssetResponse;
import com.controleFinanceiro.domain.exception.NotFoundException;
import com.controleFinanceiro.domain.port.in.query.GetAssetPort;
import com.controleFinanceiro.domain.port.out.query.AssetQueryPort;
import com.controleFinanceiro.domain.shared.ErrorCodes;
import com.controleFinanceiro.domain.shared.MessageKeys;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GetAssetUseCase implements GetAssetPort {

    private final AssetQueryPort queryPort;

    @Override
    public AssetResponse execute(UUID id) {
        return queryPort.findActiveById(id)
                .orElseThrow(() -> new NotFoundException(MessageKeys.ASSET_NOT_FOUND, ErrorCodes.ASSET_NOT_FOUND, id));
    }
}
