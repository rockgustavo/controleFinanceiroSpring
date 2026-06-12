package com.controleFinanceiro.application.usecase.query.calculos;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.springframework.stereotype.Service;

import com.controleFinanceiro.application.dto.response.ProjecaoResponse;
import com.controleFinanceiro.application.exception.ValidationException;
import com.controleFinanceiro.domain.exception.NotFoundException;
import com.controleFinanceiro.domain.port.in.query.BuscarProjecaoPortfolioPort;
import com.controleFinanceiro.domain.port.out.query.CalculosQueryPort;
import com.controleFinanceiro.domain.shared.ErrorCodes;
import com.controleFinanceiro.domain.shared.MessageKeys;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BuscarProjecaoPortfolioUseCase implements BuscarProjecaoPortfolioPort {

    private final CalculosQueryPort calculosQuery;

    @Override
    public ProjecaoResponse execute(BigDecimal taxa, int meses) {
        if (taxa.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ValidationException(MessageKeys.PROJECAO_TAXA_INVALIDA, ErrorCodes.PROJECAO_TAXA_INVALIDA);
        }
        if (meses < 1 || meses > 600) {
            throw new ValidationException(MessageKeys.PROJECAO_MESES_INVALIDOS, ErrorCodes.PROJECAO_MESES_INVALIDOS);
        }

        var dados = calculosQuery.buscarDadosUltimoParaProjecao()
                .orElseThrow(() -> new NotFoundException(MessageKeys.SNAPSHOT_NENHUM_REGISTRADO, ErrorCodes.NENHUM_SNAPSHOT));

        BigDecimal fator = BigDecimal.ONE.add(taxa.divide(new BigDecimal("100"), 8, RoundingMode.HALF_UP));
        BigDecimal totalProjetado = dados.totalLiq()
                .multiply(BigDecimal.valueOf(Math.pow(fator.doubleValue(), meses)))
                .setScale(4, RoundingMode.HALF_UP);
        BigDecimal rendimentoProjetado = totalProjetado.subtract(dados.totalLiq());

        return new ProjecaoResponse(
                dados.data(),
                dados.totalLiq(),
                taxa,
                meses,
                totalProjetado,
                rendimentoProjetado
        );
    }
}
