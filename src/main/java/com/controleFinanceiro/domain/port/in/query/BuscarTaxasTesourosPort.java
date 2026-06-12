package com.controleFinanceiro.domain.port.in.query;

import com.controleFinanceiro.application.dto.response.TaxaTesourosResponse;

import java.util.List;

public interface BuscarTaxasTesourosPort {
    List<TaxaTesourosResponse> execute();
}
