package com.controleFinanceiro.domain.port.out.external;

import com.controleFinanceiro.application.dto.response.TaxaTesourosResponse;
import com.controleFinanceiro.domain.model.DadosMercado;

import java.util.List;
import java.util.Optional;

public interface BrapiClientPort {
    Optional<DadosMercado> buscarDadosMercado();
    List<TaxaTesourosResponse> buscarTaxasTesouro();
}
