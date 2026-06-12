package com.controleFinanceiro.application.usecase.command.ativo;

import com.controleFinanceiro.application.dto.request.AtualizarAtivoRequest;

public record AtualizarAtivoCommand(String nome, String ticker, String observacoes) {

    public static AtualizarAtivoCommand from(AtualizarAtivoRequest request) {
        return new AtualizarAtivoCommand(request.nome(), request.ticker(), request.observacoes());
    }
}
