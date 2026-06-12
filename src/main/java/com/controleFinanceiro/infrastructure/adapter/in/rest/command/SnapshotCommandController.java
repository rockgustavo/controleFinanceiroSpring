package com.controleFinanceiro.infrastructure.adapter.in.rest.command;

import jakarta.validation.Valid;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.controleFinanceiro.application.dto.request.CriarSnapshotRequest;
import com.controleFinanceiro.application.dto.response.ApiResponse;
import com.controleFinanceiro.application.dto.response.SnapshotResponse;
import com.controleFinanceiro.application.usecase.command.snapshot.CriarSnapshotCommand;
import com.controleFinanceiro.domain.model.Snapshot;
import com.controleFinanceiro.domain.port.in.command.CriarSnapshotPort;
import com.controleFinanceiro.domain.port.out.query.SnapshotQueryPort;
import com.controleFinanceiro.domain.shared.MessageKeys;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/snapshots")
@RequiredArgsConstructor
@Tag(name = "Snapshots - Command", description = "Operações de escrita para snapshots")
public class SnapshotCommandController {

    private final CriarSnapshotPort criarSnapshot;
    private final SnapshotQueryPort snapshotQueryPort;
    private final MessageSource messageSource;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Criar snapshot de portfólio")
    public ResponseEntity<ApiResponse<SnapshotResponse>> criar(@Valid @RequestBody CriarSnapshotRequest request) {
        Snapshot snapshot = criarSnapshot.execute(CriarSnapshotCommand.from(request));
        SnapshotResponse response = snapshotQueryPort.findById(snapshot.getId()).orElseThrow();
        var mensagem = messageSource.getMessage(MessageKeys.SUCESSO_SNAPSHOT_CRIADO, null, LocaleContextHolder.getLocale());
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.ok(response, mensagem));
    }
}
