package com.controleFinanceiro.infrastructure.adapter.out.persistence.repository;

import com.controleFinanceiro.infrastructure.adapter.out.persistence.entity.AssetEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface AssetJpaRepository extends JpaRepository<AssetEntity, UUID> {
    boolean existsByTickerAndDeletedAtIsNull(String ticker);
    boolean existsByTickerAndDeletedAtIsNullAndIdNot(String ticker, UUID id);
    Optional<AssetEntity> findByIdAndDeletedAtIsNull(UUID id);
}
