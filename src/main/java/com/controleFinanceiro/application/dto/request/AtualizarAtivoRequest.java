package com.controleFinanceiro.application.dto.request;

import jakarta.validation.constraints.Size;

public record AtualizarAtivoRequest(
        @Size(max = 100) String nome,
        @Size(max = 10) String ticker,
        String observacoes
) {}
