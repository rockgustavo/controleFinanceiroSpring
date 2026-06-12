package com.controleFinanceiro.application.usecase.query.ativo;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.controleFinanceiro.application.dto.response.AtivoResponse;
import com.controleFinanceiro.domain.exception.NotFoundException;
import com.controleFinanceiro.domain.port.in.query.BuscarAtivoPort;
import com.controleFinanceiro.domain.port.out.query.AtivoQueryPort;
import com.controleFinanceiro.domain.shared.ErrorCodes;
import com.controleFinanceiro.domain.shared.MessageKeys;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BuscarAtivoUseCase implements BuscarAtivoPort {

    private final AtivoQueryPort queryPort;

    @Override
    public AtivoResponse execute(UUID id) {
        return queryPort.findActiveById(id)
                .orElseThrow(() -> new NotFoundException(MessageKeys.ATIVO_NAO_ENCONTRADO, ErrorCodes.ATIVO_NAO_ENCONTRADO, id));
    }
}
