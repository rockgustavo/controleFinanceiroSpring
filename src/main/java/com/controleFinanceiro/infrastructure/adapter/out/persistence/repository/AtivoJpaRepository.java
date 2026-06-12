package com.controleFinanceiro.infrastructure.adapter.out.persistence.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.controleFinanceiro.infrastructure.adapter.out.persistence.entity.AtivoEntity;

public interface AtivoJpaRepository extends JpaRepository<AtivoEntity, UUID> {
    boolean existsByTickerAndArquivadoEmIsNull(String ticker);
    boolean existsByTickerAndArquivadoEmIsNullAndIdNot(String ticker, UUID id);
    Optional<AtivoEntity> findByIdAndArquivadoEmIsNull(UUID id);
}
