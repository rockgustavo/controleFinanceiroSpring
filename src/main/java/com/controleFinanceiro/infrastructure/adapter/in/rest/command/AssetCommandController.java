package com.controleFinanceiro.infrastructure.adapter.in.rest.command;

import com.controleFinanceiro.application.dto.request.CreateAssetRequest;
import com.controleFinanceiro.application.dto.request.UpdateAssetRequest;
import com.controleFinanceiro.application.dto.response.AssetResponse;
import com.controleFinanceiro.application.dto.response.ApiResponse;
import com.controleFinanceiro.application.usecase.command.asset.CreateAssetCommand;
import com.controleFinanceiro.application.usecase.command.asset.UpdateAssetCommand;
import com.controleFinanceiro.domain.port.in.command.ArchiveAssetPort;
import com.controleFinanceiro.domain.port.in.command.CreateAssetPort;
import com.controleFinanceiro.domain.port.in.command.UpdateAssetPort;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/assets")
@RequiredArgsConstructor
@Tag(name = "Assets - Command", description = "Operações de escrita para ativos")
public class AssetCommandController {

    private final CreateAssetPort createAsset;
    private final UpdateAssetPort updateAsset;
    private final ArchiveAssetPort archiveAsset;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Criar ativo")
    public ResponseEntity<ApiResponse<AssetResponse>> create(@Valid @RequestBody CreateAssetRequest request) {
        var asset = createAsset.execute(CreateAssetCommand.from(request));
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok(AssetResponse.from(asset), "Ativo criado"));
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Atualizar ativo")
    public ResponseEntity<ApiResponse<AssetResponse>> update(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateAssetRequest request) {
        var asset = updateAsset.execute(id, UpdateAssetCommand.from(request));
        return ResponseEntity.ok(ApiResponse.ok(AssetResponse.from(asset), "Ativo atualizado"));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Arquivar ativo (soft delete)")
    public ResponseEntity<Void> archive(@PathVariable UUID id) {
        archiveAsset.execute(id);
        return ResponseEntity.noContent().build();
    }
}
