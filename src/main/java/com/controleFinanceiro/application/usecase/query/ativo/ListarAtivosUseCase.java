package com.controleFinanceiro.application.usecase.query.ativo;

import com.controleFinanceiro.application.dto.response.AtivoResponse;
import com.controleFinanceiro.domain.port.in.query.ListarAtivosPort;
import com.controleFinanceiro.domain.port.out.query.AtivoQueryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ListarAtivosUseCase implements ListarAtivosPort {

    private final AtivoQueryPort queryPort;

    @Override
    public List<AtivoResponse> execute() {
        return queryPort.findAllActive();
    }
}
