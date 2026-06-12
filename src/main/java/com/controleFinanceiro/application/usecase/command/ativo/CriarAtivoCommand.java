package com.controleFinanceiro.application.usecase.command.ativo;

import com.controleFinanceiro.application.dto.request.CriarAtivoRequest;
import com.controleFinanceiro.application.exception.ValidationException;
import com.controleFinanceiro.domain.model.TipoAtivo;
import com.controleFinanceiro.domain.shared.ErrorCodes;
import com.controleFinanceiro.domain.shared.MessageKeys;

public record CriarAtivoCommand(String nome, TipoAtivo tipo, String ticker, String observacoes) {

    public CriarAtivoCommand {
        if (nome == null) throw new ValidationException(MessageKeys.ATIVO_NOME_OBRIGATORIO, ErrorCodes.NOME_OBRIGATORIO);
        if (tipo == null) throw new ValidationException(MessageKeys.ATIVO_TIPO_OBRIGATORIO, ErrorCodes.TIPO_OBRIGATORIO);
        if ((tipo == TipoAtivo.RENDA_VARIAVEL || tipo == TipoAtivo.FII || tipo == TipoAtivo.ETF)
                && (ticker == null || ticker.isBlank())) {
            throw new ValidationException(MessageKeys.ATIVO_TICKER_OBRIGATORIO, ErrorCodes.TICKER_OBRIGATORIO, tipo.name());
        }
    }

    public static CriarAtivoCommand from(CriarAtivoRequest request) {
        return new CriarAtivoCommand(request.nome(), request.tipo(), request.ticker(), request.observacoes());
    }
}
