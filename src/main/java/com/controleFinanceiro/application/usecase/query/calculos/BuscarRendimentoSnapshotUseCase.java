package com.controleFinanceiro.application.usecase.query.calculos;

import java.math.BigDecimal;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.controleFinanceiro.application.dto.response.RendimentoResponse;
import com.controleFinanceiro.domain.exception.DomainException;
import com.controleFinanceiro.domain.exception.NotFoundException;
import com.controleFinanceiro.domain.port.in.query.BuscarRendimentoSnapshotPort;
import com.controleFinanceiro.domain.port.out.query.CalculosQueryPort;
import com.controleFinanceiro.domain.service.strategy.CagrStrategy;
import com.controleFinanceiro.domain.service.strategy.SimpleReturnStrategy;
import com.controleFinanceiro.domain.shared.ErrorCodes;
import com.controleFinanceiro.domain.shared.MessageKeys;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BuscarRendimentoSnapshotUseCase implements BuscarRendimentoSnapshotPort {

    private final CalculosQueryPort calculosQuery;

    @Override
    public RendimentoResponse execute(UUID id, String modo) {
        var dados = calculosQuery.buscarDadosParaRendimento(id)
                .orElseThrow(() -> new NotFoundException(MessageKeys.SNAPSHOT_NAO_ENCONTRADO, ErrorCodes.SNAPSHOT_NAO_ENCONTRADO, id));

        if (dados.totalLiqAnterior() == null || dados.totalLiqAnterior().compareTo(BigDecimal.ZERO) == 0) {
            throw new DomainException(MessageKeys.SNAPSHOT_SEM_HISTORICO, ErrorCodes.SNAPSHOT_SEM_HISTORICO);
        }

        String modoNormalizado = "cagr".equalsIgnoreCase(modo) ? "cagr" : "simple";
        var strategy = "cagr".equals(modoNormalizado) ? new CagrStrategy() : new SimpleReturnStrategy();

        int meses = Math.max(1, (int) ChronoUnit.MONTHS.between(dados.dataAnterior(), dados.data()));
        var rendimentoAbsoluto = dados.totalLiq().subtract(dados.totalLiqAnterior());
        var rendimentoPercentual = strategy.calculate(dados.totalLiqAnterior(), dados.totalLiq(), meses);

        return new RendimentoResponse(
                dados.snapshotId(),
                dados.data(),
                dados.totalLiq(),
                dados.dataAnterior(),
                dados.totalLiqAnterior(),
                rendimentoAbsoluto,
                rendimentoPercentual,
                modoNormalizado
        );
    }
}
