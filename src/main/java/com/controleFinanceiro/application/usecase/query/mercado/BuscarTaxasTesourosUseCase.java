package com.controleFinanceiro.application.usecase.query.mercado;

import java.util.List;

import org.springframework.stereotype.Service;

import com.controleFinanceiro.application.dto.response.TaxaTesourosResponse;
import com.controleFinanceiro.domain.port.in.query.BuscarTaxasTesourosPort;
import com.controleFinanceiro.domain.port.out.external.BrapiClientPort;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BuscarTaxasTesourosUseCase implements BuscarTaxasTesourosPort {

    private final BrapiClientPort brapiClient;

    @Override
    public List<TaxaTesourosResponse> execute() {
        return brapiClient.buscarTaxasTesouro();
    }
}
