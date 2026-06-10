package com.controleFinanceiro.domain.port.out.command;

import com.controleFinanceiro.domain.model.Asset;

import java.util.Optional;
import java.util.UUID;

public interface AssetRepositoryPort {
    Asset save(Asset asset);
    Optional<Asset> findActiveById(UUID id);
    boolean existsByTickerAndNotArchived(String ticker);
    boolean existsByTickerAndNotArchivedExcluding(String ticker, UUID excludeId);
}
