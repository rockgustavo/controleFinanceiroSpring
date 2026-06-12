package com.controleFinanceiro.domain.port.in.query;

import com.controleFinanceiro.application.dto.response.AtivoResponse;

import java.util.List;

public interface ListarAtivosPort {
    List<AtivoResponse> execute();
}
