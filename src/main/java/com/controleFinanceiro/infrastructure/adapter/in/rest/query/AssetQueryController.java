package com.controleFinanceiro.infrastructure.adapter.in.rest.query;

import com.controleFinanceiro.application.dto.response.ApiResponse;
import com.controleFinanceiro.application.dto.response.AssetResponse;
import com.controleFinanceiro.domain.port.in.query.GetAssetPort;
import com.controleFinanceiro.domain.port.in.query.ListAssetsPort;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/assets")
@RequiredArgsConstructor
@Tag(name = "Assets - Query", description = "Operações de leitura para ativos")
public class AssetQueryController {

    private final ListAssetsPort listAssets;
    private final GetAssetPort getAsset;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'VIEWER')")
    @Operation(summary = "Listar ativos ativos")
    public ResponseEntity<ApiResponse<List<AssetResponse>>> list() {
        return ResponseEntity.ok(ApiResponse.ok(listAssets.execute()));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'VIEWER')")
    @Operation(summary = "Buscar ativo por ID")
    public ResponseEntity<ApiResponse<AssetResponse>> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.ok(getAsset.execute(id)));
    }
}
