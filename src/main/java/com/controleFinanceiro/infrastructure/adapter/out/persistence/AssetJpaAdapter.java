package com.controleFinanceiro.infrastructure.adapter.out.persistence;

import com.controleFinanceiro.domain.model.Asset;
import com.controleFinanceiro.domain.port.out.command.AssetRepositoryPort;
import com.controleFinanceiro.infrastructure.adapter.out.persistence.entity.AssetEntity;
import com.controleFinanceiro.infrastructure.adapter.out.persistence.repository.AssetJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class AssetJpaAdapter implements AssetRepositoryPort {

    private final AssetJpaRepository repository;

    @Override
    public Asset save(Asset asset) {
        return toDomain(repository.save(toEntity(asset)));
    }

    @Override
    public Optional<Asset> findActiveById(UUID id) {
        return repository.findByIdAndDeletedAtIsNull(id).map(this::toDomain);
    }

    @Override
    public boolean existsByTickerAndNotArchived(String ticker) {
        return repository.existsByTickerAndDeletedAtIsNull(ticker);
    }

    @Override
    public boolean existsByTickerAndNotArchivedExcluding(String ticker, UUID excludeId) {
        return repository.existsByTickerAndDeletedAtIsNullAndIdNot(ticker, excludeId);
    }

    @NonNull
    private AssetEntity toEntity(Asset asset) {
        var entity = new AssetEntity();
        entity.setId(asset.getId());
        entity.setName(asset.getName());
        entity.setType(asset.getType());
        entity.setTicker(asset.getTicker());
        entity.setNotes(asset.getNotes());
        entity.setCreatedAt(asset.getCreatedAt());
        entity.setUpdatedAt(asset.getUpdatedAt());
        entity.setDeletedAt(asset.getArchivedAt());
        return entity;
    }

    private Asset toDomain(AssetEntity entity) {
        return Asset.reconstitute(
                entity.getId(),
                entity.getName(),
                entity.getType(),
                entity.getTicker(),
                entity.getNotes(),
                entity.getCreatedAt(),
                entity.getUpdatedAt(),
                entity.getDeletedAt()
        );
    }
}
