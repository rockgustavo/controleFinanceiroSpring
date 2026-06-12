package com.controleFinanceiro.infrastructure.adapter.out.persistence;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Component;

import com.controleFinanceiro.domain.model.Posicao;
import com.controleFinanceiro.domain.model.Snapshot;
import com.controleFinanceiro.domain.port.out.command.SnapshotRepositoryPort;
import com.controleFinanceiro.infrastructure.adapter.out.persistence.entity.PosicaoEntity;
import com.controleFinanceiro.infrastructure.adapter.out.persistence.entity.SnapshotEntity;
import com.controleFinanceiro.infrastructure.adapter.out.persistence.repository.SnapshotJpaRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SnapshotJpaAdapter implements SnapshotRepositoryPort {

    private final SnapshotJpaRepository repository;

    @Override
    public Snapshot save(Snapshot snapshot) {
        return toDomain(repository.save(toEntity(snapshot)));
    }

    @Override
    public boolean existsByData(LocalDate data) {
        return repository.existsByData(data);
    }

    private SnapshotEntity toEntity(Snapshot snapshot) {
        var entity = new SnapshotEntity();
        entity.setId(snapshot.getId());
        entity.setData(snapshot.getData());
        entity.setObservacoes(snapshot.getObservacoes());
        entity.setCriadoEm(snapshot.getCriadoEm());

        List<PosicaoEntity> posicoes = snapshot.getPosicoes().stream()
                .map(p -> toPosicaoEntity(p, entity))
                .toList();
        entity.getPosicoes().addAll(posicoes);

        return entity;
    }

    private PosicaoEntity toPosicaoEntity(Posicao posicao, SnapshotEntity snapshot) {
        var entity = new PosicaoEntity();
        entity.setSnapshot(snapshot);
        entity.setAtivoId(posicao.getAtivoId());
        entity.setQuantidade(posicao.getQuantidade());
        entity.setPrecoUnit(posicao.getPrecoUnit());
        entity.setTotalBruto(posicao.getTotalBruto());
        entity.setTotalLiq(posicao.getTotalLiq());
        entity.setTaxa(posicao.getTaxa());
        return entity;
    }

    private Snapshot toDomain(SnapshotEntity entity) {
        var posicoes = entity.getPosicoes().stream()
                .map(p -> Posicao.reconstitute(
                        p.getAtivoId(), p.getQuantidade(), p.getPrecoUnit(),
                        p.getTotalBruto(), p.getTotalLiq(), p.getTaxa()))
                .toList();
        return Snapshot.reconstitute(
                entity.getId(), entity.getData(), entity.getObservacoes(),
                posicoes, entity.getCriadoEm());
    }
}
