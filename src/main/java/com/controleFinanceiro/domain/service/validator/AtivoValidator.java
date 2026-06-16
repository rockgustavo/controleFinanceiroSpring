package com.controleFinanceiro.domain.service.validator;

import com.controleFinanceiro.domain.model.Ativo;

public interface AtivoValidator {

    AtivoValidator SEM_EXIGENCIA_DE_TICKER = ativo -> { };

    void validate(Ativo ativo);
}
