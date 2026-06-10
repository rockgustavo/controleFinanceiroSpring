package com.controleFinanceiro.application.usecase.command.asset;

import com.controleFinanceiro.domain.event.AssetChangedEvent;
import com.controleFinanceiro.domain.exception.ConflictException;
import com.controleFinanceiro.domain.exception.NotFoundException;
import com.controleFinanceiro.domain.model.Asset;
import com.controleFinanceiro.domain.port.in.command.UpdateAssetPort;
import com.controleFinanceiro.domain.port.out.command.AssetRepositoryPort;
import com.controleFinanceiro.domain.port.out.messaging.EventPublisherPort;
import com.controleFinanceiro.domain.service.factory.AssetValidatorFactory;
import com.controleFinanceiro.domain.shared.ErrorCodes;
import com.controleFinanceiro.domain.shared.MessageKeys;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UpdateAssetUseCase implements UpdateAssetPort {

    private final AssetRepositoryPort repository;
    private final EventPublisherPort eventPublisher;

    @Override
    @Transactional
    public Asset execute(UUID id, UpdateAssetCommand command) {
        var asset = repository.findActiveById(id)
                .orElseThrow(() -> new NotFoundException(MessageKeys.ASSET_NOT_FOUND, ErrorCodes.ASSET_NOT_FOUND, id));
        if (command.ticker() != null
                && !command.ticker().equals(asset.getTicker())
                && repository.existsByTickerAndNotArchivedExcluding(command.ticker(), id)) {
            throw new ConflictException(MessageKeys.ASSET_TICKER_EXISTS, ErrorCodes.TICKER_ALREADY_EXISTS, command.ticker());
        }
        asset.update(command.name(), command.ticker(), command.notes());
        AssetValidatorFactory.forType(asset.getType()).validate(asset);
        var saved = repository.save(asset);
        eventPublisher.publish(AssetChangedEvent.updated(saved.getId()));
        return saved;
    }
}
