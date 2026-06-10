package com.controleFinanceiro.application.usecase.command.asset;

import com.controleFinanceiro.domain.event.AssetChangedEvent;
import com.controleFinanceiro.domain.exception.ConflictException;
import com.controleFinanceiro.domain.model.Asset;
import com.controleFinanceiro.domain.port.in.command.CreateAssetPort;
import com.controleFinanceiro.domain.port.out.command.AssetRepositoryPort;
import com.controleFinanceiro.domain.port.out.messaging.EventPublisherPort;
import com.controleFinanceiro.domain.service.factory.AssetValidatorFactory;
import com.controleFinanceiro.domain.shared.ErrorCodes;
import com.controleFinanceiro.domain.shared.MessageKeys;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CreateAssetUseCase implements CreateAssetPort {

    private final AssetRepositoryPort repository;
    private final EventPublisherPort eventPublisher;

    @Override
    @Transactional
    public Asset execute(CreateAssetCommand command) {
        if (command.ticker() != null && repository.existsByTickerAndNotArchived(command.ticker())) {
            throw new ConflictException(MessageKeys.ASSET_TICKER_EXISTS, ErrorCodes.TICKER_ALREADY_EXISTS, command.ticker());
        }
        var asset = Asset.create(command.name(), command.type(), command.ticker(), command.notes());
        AssetValidatorFactory.forType(asset.getType()).validate(asset);
        var saved = repository.save(asset);
        eventPublisher.publish(AssetChangedEvent.created(saved.getId()));
        return saved;
    }
}
