package com.controleFinanceiro.domain.port.in.command;

import com.controleFinanceiro.application.usecase.command.ativo.CriarAtivoCommand;
import com.controleFinanceiro.domain.model.Ativo;

public interface CriarAtivoPort {
    Ativo execute(CriarAtivoCommand command);
}
