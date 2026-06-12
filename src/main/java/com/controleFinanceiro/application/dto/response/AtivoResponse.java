package com.controleFinanceiro.application.dto.response;

import com.controleFinanceiro.domain.model.Ativo;

import java.util.UUID;

public record AtivoResponse(UUID id, String nome, String tipo, String ticker, String observacoes) {

    public static AtivoResponse from(Ativo ativo) {
        return new AtivoResponse(
                ativo.getId(),
                ativo.getNome(),
                ativo.getTipo().name(),
                ativo.getTicker(),
                ativo.getObservacoes()
        );
    }
}
