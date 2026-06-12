package com.controleFinanceiro.application.dto.request;

import com.controleFinanceiro.domain.model.TipoAtivo;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CriarAtivoRequest(
        @NotBlank @Size(max = 100) String nome,
        @NotNull TipoAtivo tipo,
        @Size(max = 10) String ticker,
        String observacoes
) {}
