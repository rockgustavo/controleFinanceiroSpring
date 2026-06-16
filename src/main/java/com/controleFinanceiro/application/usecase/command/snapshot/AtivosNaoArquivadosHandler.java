package com.controleFinanceiro.application.usecase.command.snapshot;

import com.controleFinanceiro.domain.exception.DomainException;
import com.controleFinanceiro.domain.exception.NotFoundException;
import com.controleFinanceiro.domain.port.out.command.AtivoRepositoryPort;
import com.controleFinanceiro.domain.shared.ErrorCodes;
import com.controleFinanceiro.domain.shared.MessageKeys;

public class AtivosNaoArquivadosHandler extends SnapshotValidationHandler {

    private final AtivoRepositoryPort ativoRepository;

    public AtivosNaoArquivadosHandler(AtivoRepositoryPort ativoRepository) {
        this.ativoRepository = ativoRepository;
    }

    @Override
    protected void validar(CriarSnapshotCommand command) {
        command.posicoes().forEach(p -> {
            var ativo = ativoRepository.findById(p.ativoId())
                    .orElseThrow(() -> new NotFoundException(MessageKeys.ATIVO_NAO_ENCONTRADO, ErrorCodes.ATIVO_NAO_ENCONTRADO, p.ativoId()));
            if (ativo.estaArquivado()) {
                throw new DomainException(MessageKeys.SNAPSHOT_ATIVO_ARQUIVADO, ErrorCodes.ATIVO_ARQUIVADO, p.ativoId());
            }
        });
    }
}
