package com.controleFinanceiro.domain.port.out.query;

import com.controleFinanceiro.application.dto.response.AtivoResponse;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AtivoQueryPort {
    List<AtivoResponse> findAllActive();
    Optional<AtivoResponse> findActiveById(UUID id);
}
