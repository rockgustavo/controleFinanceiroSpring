package com.controleFinanceiro.domain.port.in.query;

import java.math.BigDecimal;

import com.controleFinanceiro.application.dto.response.ProjecaoResponse;

public interface BuscarProjecaoPortfolioPort {
    ProjecaoResponse execute(BigDecimal taxa, int meses);
}
