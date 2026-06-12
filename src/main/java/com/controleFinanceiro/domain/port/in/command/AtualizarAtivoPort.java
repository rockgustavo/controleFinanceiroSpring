package com.controleFinanceiro.domain.port.in.command;

import com.controleFinanceiro.application.usecase.command.ativo.AtualizarAtivoCommand;
import com.controleFinanceiro.domain.model.Ativo;

import java.util.UUID;

public interface AtualizarAtivoPort {
    Ativo execute(UUID id, AtualizarAtivoCommand command);
}
