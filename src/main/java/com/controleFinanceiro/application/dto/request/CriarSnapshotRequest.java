package com.controleFinanceiro.application.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.List;

public record CriarSnapshotRequest(
        @NotNull LocalDate data,
        String observacoes,
        @NotNull @NotEmpty @Valid List<PosicaoRequest> posicoes
) {}
