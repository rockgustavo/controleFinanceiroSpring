package com.controleFinanceiro.infrastructure.adapter.in.rest.command;

import com.controleFinanceiro.application.dto.request.AtualizarAtivoRequest;
import com.controleFinanceiro.application.dto.request.CriarAtivoRequest;
import com.controleFinanceiro.application.dto.response.ApiResponse;
import com.controleFinanceiro.application.dto.response.AtivoResponse;
import com.controleFinanceiro.application.usecase.command.ativo.AtualizarAtivoCommand;
import com.controleFinanceiro.application.usecase.command.ativo.CriarAtivoCommand;
import com.controleFinanceiro.domain.port.in.command.ArquivarAtivoPort;
import com.controleFinanceiro.domain.port.in.command.AtualizarAtivoPort;
import com.controleFinanceiro.domain.port.in.command.CriarAtivoPort;
import com.controleFinanceiro.domain.shared.MessageKeys;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/assets")
@RequiredArgsConstructor
@Tag(name = "Ativos - Command", description = "Operações de escrita para ativos")
public class AtivoCommandController {

    private final CriarAtivoPort criarAtivo;
    private final AtualizarAtivoPort atualizarAtivo;
    private final ArquivarAtivoPort arquivarAtivo;
    private final MessageSource messageSource;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Criar ativo")
    public ResponseEntity<ApiResponse<AtivoResponse>> criar(@Valid @RequestBody CriarAtivoRequest request) {
        var ativo = criarAtivo.execute(CriarAtivoCommand.from(request));
        var mensagem = messageSource.getMessage(MessageKeys.SUCESSO_ATIVO_CRIADO, null, LocaleContextHolder.getLocale());
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.ok(AtivoResponse.from(ativo), mensagem));
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Atualizar ativo")
    public ResponseEntity<ApiResponse<AtivoResponse>> atualizar(
            @PathVariable UUID id,
            @Valid @RequestBody AtualizarAtivoRequest request) {
        var ativo = atualizarAtivo.execute(id, AtualizarAtivoCommand.from(request));
        var mensagem = messageSource.getMessage(MessageKeys.SUCESSO_ATIVO_ATUALIZADO, null, LocaleContextHolder.getLocale());
        return ResponseEntity.ok(ApiResponse.ok(AtivoResponse.from(ativo), mensagem));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Arquivar ativo (soft delete)")
    public ResponseEntity<Void> arquivar(@PathVariable UUID id) {
        arquivarAtivo.execute(id);
        return ResponseEntity.noContent().build();
    }
}
