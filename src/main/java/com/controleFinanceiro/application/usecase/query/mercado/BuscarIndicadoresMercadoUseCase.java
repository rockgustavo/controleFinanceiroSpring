package com.controleFinanceiro.application.usecase.query.mercado;

import com.controleFinanceiro.application.dto.response.IndicadoresMercadoResponse;
import com.controleFinanceiro.domain.port.in.query.BuscarIndicadoresMercadoPort;
import com.controleFinanceiro.domain.port.out.external.BrapiClientPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BuscarIndicadoresMercadoUseCase implements BuscarIndicadoresMercadoPort {

    private final BrapiClientPort brapiClient;

    @Override
    public IndicadoresMercadoResponse execute() {
        return brapiClient.buscarDadosMercado()
                .map(d -> new IndicadoresMercadoResponse(d.selic(), d.usdBrl(), d.ibovespa(), d.ivvb11(), d.ipca()))
                .orElse(new IndicadoresMercadoResponse(null, null, null, null, null));
    }
}
