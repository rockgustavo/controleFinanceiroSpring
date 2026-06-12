package com.controleFinanceiro.infrastructure.adapter.out.persistence;

import java.util.UUID;

import org.springframework.stereotype.Component;

import com.controleFinanceiro.domain.model.DadosMercado;
import com.controleFinanceiro.domain.port.out.command.DadosMercadoRepositoryPort;
import com.controleFinanceiro.infrastructure.adapter.out.persistence.entity.DadosMercadoEntity;
import com.controleFinanceiro.infrastructure.adapter.out.persistence.repository.DadosMercadoJpaRepository;
import com.controleFinanceiro.infrastructure.adapter.out.persistence.repository.SnapshotJpaRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DadosMercadoJpaAdapter implements DadosMercadoRepositoryPort {

    private final DadosMercadoJpaRepository repository;
    private final SnapshotJpaRepository snapshotJpaRepository;

    @Override
    public void save(UUID snapshotId, DadosMercado dados) {
        var entity = new DadosMercadoEntity();
        entity.setSnapshot(snapshotJpaRepository.getReferenceById(snapshotId));
        entity.setSelic(dados.selic());
        entity.setUsdBrl(dados.usdBrl());
        entity.setIbovespa(dados.ibovespa());
        entity.setIvvb11(dados.ivvb11());
        entity.setIpca(dados.ipca());
        repository.save(entity);
    }
}
