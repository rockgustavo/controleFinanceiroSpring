package com.controleFinanceiro.infrastructure.adapter.out.persistence.repository;

import com.controleFinanceiro.infrastructure.adapter.out.persistence.entity.AtivoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface AtivoJpaRepository extends JpaRepository<AtivoEntity, UUID> {
    boolean existsByTickerAndArquivadoEmIsNull(String ticker);
    boolean existsByTickerAndArquivadoEmIsNullAndIdNot(String ticker, UUID id);
    Optional<AtivoEntity> findByIdAndArquivadoEmIsNull(UUID id);
}
