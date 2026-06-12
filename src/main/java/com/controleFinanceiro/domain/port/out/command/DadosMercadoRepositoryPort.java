package com.controleFinanceiro.domain.port.out.command;

import com.controleFinanceiro.domain.model.DadosMercado;

import java.util.UUID;

public interface DadosMercadoRepositoryPort {
    void save(UUID snapshotId, DadosMercado dados);
}
