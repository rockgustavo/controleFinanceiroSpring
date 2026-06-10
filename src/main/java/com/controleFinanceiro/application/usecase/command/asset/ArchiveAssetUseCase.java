package com.controleFinanceiro.application.usecase.command.asset;

import com.controleFinanceiro.domain.exception.NotFoundException;
import com.controleFinanceiro.domain.port.in.command.ArchiveAssetPort;
import com.controleFinanceiro.domain.port.out.command.AssetRepositoryPort;
import com.controleFinanceiro.domain.shared.ErrorCodes;
import com.controleFinanceiro.domain.shared.MessageKeys;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ArchiveAssetUseCase implements ArchiveAssetPort {

    private final AssetRepositoryPort repository;

    @Override
    @Transactional
    public void execute(UUID id) {
        var asset = repository.findActiveById(id)
                .orElseThrow(() -> new NotFoundException(MessageKeys.ASSET_NOT_FOUND, ErrorCodes.ASSET_NOT_FOUND, id));
        asset.archive();
        repository.save(asset);
    }
}
