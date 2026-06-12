package com.controleFinanceiro.infrastructure.adapter.in.rest.query;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.controleFinanceiro.application.dto.response.ApiResponse;
import com.controleFinanceiro.application.dto.response.SnapshotResponse;
import com.controleFinanceiro.application.dto.response.SnapshotResumoResponse;
import com.controleFinanceiro.domain.port.in.query.BuscarSnapshotPort;
import com.controleFinanceiro.domain.port.in.query.BuscarUltimoSnapshotPort;
import com.controleFinanceiro.domain.port.in.query.ListarSnapshotsPort;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/snapshots")
@RequiredArgsConstructor
@Tag(name = "Snapshots - Query", description = "Operações de leitura para snapshots")
public class SnapshotQueryController {

    private final ListarSnapshotsPort listarSnapshots;
    private final BuscarSnapshotPort buscarSnapshot;
    private final BuscarUltimoSnapshotPort buscarUltimoSnapshot;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'VIEWER')")
    @Operation(summary = "Listar todos os snapshots (resumo)")
    public ResponseEntity<ApiResponse<List<SnapshotResumoResponse>>> listar() {
        return ResponseEntity.ok(ApiResponse.ok(listarSnapshots.execute()));
    }

    @GetMapping("/latest")
    @PreAuthorize("hasAnyRole('ADMIN', 'VIEWER')")
    @Operation(summary = "Buscar snapshot mais recente")
    public ResponseEntity<ApiResponse<SnapshotResponse>> buscarUltimo() {
        return ResponseEntity.ok(ApiResponse.ok(buscarUltimoSnapshot.execute()));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'VIEWER')")
    @Operation(summary = "Buscar snapshot por ID")
    public ResponseEntity<ApiResponse<SnapshotResponse>> buscarPorId(@PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.ok(buscarSnapshot.execute(id)));
    }
}
