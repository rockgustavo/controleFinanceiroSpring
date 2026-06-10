package com.controleFinanceiro.domain.port.in.command;

import java.util.UUID;

public interface ArchiveAssetPort {
    void execute(UUID id);
}
