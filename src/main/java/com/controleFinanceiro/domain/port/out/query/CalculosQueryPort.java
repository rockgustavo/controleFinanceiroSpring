package com.controleFinanceiro.domain.port.out.query;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CalculosQueryPort {

    Optional<DadosRendimento> buscarDadosParaRendimento(UUID snapshotId);

    List<AlocacaoBruta> buscarAlocacaoPorTipo(UUID snapshotId);

    Optional<DadosProjecao> buscarDadosUltimoParaProjecao();

    record DadosRendimento(
            UUID snapshotId,
            LocalDate data,
            BigDecimal totalLiq,
            LocalDate dataAnterior,
            BigDecimal totalLiqAnterior
    ) {}

    record AlocacaoBruta(String tipo, BigDecimal totalLiq) {}

    record DadosProjecao(LocalDate data, BigDecimal totalLiq) {}
}
