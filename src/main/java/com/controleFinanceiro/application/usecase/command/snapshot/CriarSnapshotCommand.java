package com.controleFinanceiro.application.usecase.command.snapshot;

import com.controleFinanceiro.application.dto.request.CriarSnapshotRequest;
import com.controleFinanceiro.application.exception.ValidationException;
import com.controleFinanceiro.domain.shared.ErrorCodes;
import com.controleFinanceiro.domain.shared.MessageKeys;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record CriarSnapshotCommand(LocalDate data, String observacoes, List<PosicaoInput> posicoes) {

    public record PosicaoInput(UUID ativoId, BigDecimal quantidade, BigDecimal precoUnit,
                                BigDecimal totalLiq, String taxa) {
        public PosicaoInput {
            if (ativoId == null)
                throw new ValidationException(MessageKeys.POSICAO_ATIVO_OBRIGATORIO, ErrorCodes.POSICAO_ATIVO_OBRIGATORIO);
            if (quantidade == null || quantidade.compareTo(BigDecimal.ZERO) <= 0)
                throw new ValidationException(MessageKeys.POSICAO_QUANTIDADE_INVALIDA, ErrorCodes.POSICAO_QUANTIDADE_INVALIDA);
            if (precoUnit == null || precoUnit.compareTo(BigDecimal.ZERO) <= 0)
                throw new ValidationException(MessageKeys.POSICAO_PRECO_INVALIDO, ErrorCodes.POSICAO_PRECO_INVALIDO);
            if (totalLiq == null || totalLiq.compareTo(BigDecimal.ZERO) < 0)
                throw new ValidationException(MessageKeys.POSICAO_TOTAL_LIQUIDO_INVALIDO, ErrorCodes.POSICAO_TOTAL_LIQUIDO_INVALIDO);
        }
    }

    public CriarSnapshotCommand {
        if (data == null)
            throw new ValidationException(MessageKeys.SNAPSHOT_DATA_OBRIGATORIA, ErrorCodes.SNAPSHOT_DATA_OBRIGATORIA);
        if (posicoes == null || posicoes.isEmpty())
            throw new ValidationException(MessageKeys.SNAPSHOT_SEM_POSICOES, ErrorCodes.SNAPSHOT_SEM_POSICOES);
    }

    public static CriarSnapshotCommand from(CriarSnapshotRequest request) {
        var posicoes = request.posicoes().stream()
                .map(p -> new PosicaoInput(p.ativoId(), p.quantidade(), p.precoUnit(), p.totalLiq(), p.taxa()))
                .toList();
        return new CriarSnapshotCommand(request.data(), request.observacoes(), posicoes);
    }
}
