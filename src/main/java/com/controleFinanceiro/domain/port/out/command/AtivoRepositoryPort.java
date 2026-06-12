package com.controleFinanceiro.domain.port.out.command;

import com.controleFinanceiro.domain.model.Ativo;

import java.util.Optional;
import java.util.UUID;

public interface AtivoRepositoryPort {
    Ativo save(Ativo ativo);
    Optional<Ativo> findById(UUID id);
    Optional<Ativo> findActiveById(UUID id);
    boolean existsByTickerAndNotArchived(String ticker);
    boolean existsByTickerAndNotArchivedExcluding(String ticker, UUID excludeId);
}
