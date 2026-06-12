package com.controleFinanceiro.infrastructure.adapter.out.persistence;

import com.controleFinanceiro.domain.model.Ativo;
import com.controleFinanceiro.domain.port.out.command.AtivoRepositoryPort;
import com.controleFinanceiro.infrastructure.adapter.out.persistence.entity.AtivoEntity;
import com.controleFinanceiro.infrastructure.adapter.out.persistence.repository.AtivoJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class AtivoJpaAdapter implements AtivoRepositoryPort {

    private final AtivoJpaRepository repository;

    @Override
    public Ativo save(Ativo ativo) {
        return toDomain(repository.save(toEntity(ativo)));
    }

    @Override
    public Optional<Ativo> findById(UUID id) {
        return repository.findById(id).map(this::toDomain);
    }

    @Override
    public Optional<Ativo> findActiveById(UUID id) {
        return repository.findByIdAndArquivadoEmIsNull(id).map(this::toDomain);
    }

    @Override
    public boolean existsByTickerAndNotArchived(String ticker) {
        return repository.existsByTickerAndArquivadoEmIsNull(ticker);
    }

    @Override
    public boolean existsByTickerAndNotArchivedExcluding(String ticker, UUID excludeId) {
        return repository.existsByTickerAndArquivadoEmIsNullAndIdNot(ticker, excludeId);
    }

    @NonNull
    private AtivoEntity toEntity(Ativo ativo) {
        var entity = new AtivoEntity();
        entity.setId(ativo.getId());
        entity.setNome(ativo.getNome());
        entity.setTipo(ativo.getTipo());
        entity.setTicker(ativo.getTicker());
        entity.setObservacoes(ativo.getObservacoes());
        entity.setCriadoEm(ativo.getCriadoEm());
        entity.setAtualizadoEm(ativo.getAtualizadoEm());
        entity.setArquivadoEm(ativo.getArquivadoEm());
        return entity;
    }

    private Ativo toDomain(AtivoEntity entity) {
        return Ativo.reconstitute(
                entity.getId(),
                entity.getNome(),
                entity.getTipo(),
                entity.getTicker(),
                entity.getObservacoes(),
                entity.getCriadoEm(),
                entity.getAtualizadoEm(),
                entity.getArquivadoEm()
        );
    }
}
