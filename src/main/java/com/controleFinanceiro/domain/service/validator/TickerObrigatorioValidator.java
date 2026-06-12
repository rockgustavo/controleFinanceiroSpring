package com.controleFinanceiro.domain.service.validator;

import com.controleFinanceiro.domain.exception.DomainException;
import com.controleFinanceiro.domain.model.Ativo;
import com.controleFinanceiro.domain.shared.ErrorCodes;
import com.controleFinanceiro.domain.shared.MessageKeys;

public class TickerObrigatorioValidator implements AtivoValidator {

    @Override
    public void validate(Ativo ativo) {
        if (ativo.getTicker() == null || ativo.getTicker().isBlank()) {
            throw new DomainException(MessageKeys.ATIVO_TICKER_OBRIGATORIO, ErrorCodes.TICKER_OBRIGATORIO, ativo.getTipo().name());
        }
    }
}
