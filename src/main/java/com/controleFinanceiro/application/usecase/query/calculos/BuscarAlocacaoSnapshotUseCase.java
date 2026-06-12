package com.controleFinanceiro.application.usecase.query.calculos;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.controleFinanceiro.application.dto.response.AlocacaoItemResponse;
import com.controleFinanceiro.domain.exception.NotFoundException;
import com.controleFinanceiro.domain.port.in.query.BuscarAlocacaoSnapshotPort;
import com.controleFinanceiro.domain.port.out.query.CalculosQueryPort;
import com.controleFinanceiro.domain.port.out.query.SnapshotQueryPort;
import com.controleFinanceiro.domain.shared.ErrorCodes;
import com.controleFinanceiro.domain.shared.MessageKeys;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BuscarAlocacaoSnapshotUseCase implements BuscarAlocacaoSnapshotPort {

    private final CalculosQueryPort calculosQuery;
    private final SnapshotQueryPort snapshotQuery;

    @Override
    public List<AlocacaoItemResponse> execute(UUID id) {
        snapshotQuery.findById(id)
                .orElseThrow(() -> new NotFoundException(MessageKeys.SNAPSHOT_NAO_ENCONTRADO, ErrorCodes.SNAPSHOT_NAO_ENCONTRADO, id));

        var linhas = calculosQuery.buscarAlocacaoPorTipo(id);
        var total = linhas.stream()
                .map(CalculosQueryPort.AlocacaoBruta::totalLiq)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        if (total.compareTo(BigDecimal.ZERO) == 0) {
            return linhas.stream()
                    .map(l -> new AlocacaoItemResponse(l.tipo(), l.totalLiq(), BigDecimal.ZERO))
                    .toList();
        }

        return linhas.stream()
                .map(l -> new AlocacaoItemResponse(
                        l.tipo(),
                        l.totalLiq(),
                        l.totalLiq().divide(total, 6, RoundingMode.HALF_UP)
                                .multiply(new BigDecimal("100"))
                                .setScale(2, RoundingMode.HALF_UP)
                ))
                .toList();
    }
}
