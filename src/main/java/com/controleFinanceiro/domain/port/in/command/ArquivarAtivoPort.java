package com.controleFinanceiro.domain.port.in.command;

import java.util.UUID;

public interface ArquivarAtivoPort {
    void execute(UUID id);
}
