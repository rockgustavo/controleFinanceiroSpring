package com.controleFinanceiro.application.usecase.query.ativo;

import java.util.List;

import org.springframework.stereotype.Service;

import com.controleFinanceiro.application.dto.response.AtivoResponse;
import com.controleFinanceiro.domain.port.in.query.ListarAtivosPort;
import com.controleFinanceiro.domain.port.out.query.AtivoQueryPort;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ListarAtivosUseCase implements ListarAtivosPort {

    private final AtivoQueryPort queryPort;

    @Override
    public List<AtivoResponse> execute() {
        return queryPort.findAllActive();
    }
}
