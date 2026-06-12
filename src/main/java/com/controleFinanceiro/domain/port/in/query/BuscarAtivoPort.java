package com.controleFinanceiro.domain.port.in.query;

import com.controleFinanceiro.application.dto.response.AtivoResponse;

import java.util.UUID;

public interface BuscarAtivoPort {
    AtivoResponse execute(UUID id);
}
