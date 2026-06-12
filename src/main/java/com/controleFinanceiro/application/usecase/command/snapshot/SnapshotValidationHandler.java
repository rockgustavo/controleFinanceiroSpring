package com.controleFinanceiro.application.usecase.command.snapshot;

public abstract class SnapshotValidationHandler {

    private SnapshotValidationHandler proximo;

    public SnapshotValidationHandler entao(SnapshotValidationHandler proximo) {
        this.proximo = proximo;
        return proximo;
    }

    public final void handle(CriarSnapshotCommand command) {
        validar(command);
        if (proximo != null) proximo.handle(command);
    }

    protected abstract void validar(CriarSnapshotCommand command);
}
