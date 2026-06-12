package com.controleFinanceiro.application.usecase.command.ativo;

import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.controleFinanceiro.domain.exception.NotFoundException;
import com.controleFinanceiro.domain.port.in.command.ArquivarAtivoPort;
import com.controleFinanceiro.domain.port.out.command.AtivoRepositoryPort;
import com.controleFinanceiro.domain.shared.ErrorCodes;
import com.controleFinanceiro.domain.shared.MessageKeys;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ArquivarAtivoUseCase implements ArquivarAtivoPort {

    private final AtivoRepositoryPort repository;

    @Override
    @Transactional
    public void execute(UUID id) {
        var ativo = repository.findActiveById(id)
                .orElseThrow(() -> new NotFoundException(MessageKeys.ATIVO_NAO_ENCONTRADO, ErrorCodes.ATIVO_NAO_ENCONTRADO, id));
        ativo.arquivar();
        repository.save(ativo);
    }
}
