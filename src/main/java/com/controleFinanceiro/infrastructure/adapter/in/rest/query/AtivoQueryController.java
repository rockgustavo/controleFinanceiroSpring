package com.controleFinanceiro.infrastructure.adapter.in.rest.query;

import com.controleFinanceiro.application.dto.response.ApiResponse;
import com.controleFinanceiro.application.dto.response.AtivoResponse;
import com.controleFinanceiro.domain.port.in.query.BuscarAtivoPort;
import com.controleFinanceiro.domain.port.in.query.ListarAtivosPort;
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
@Tag(name = "Ativos - Query", description = "Operações de leitura para ativos")
public class AtivoQueryController {

    private final ListarAtivosPort listarAtivos;
    private final BuscarAtivoPort buscarAtivo;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'VIEWER')")
    @Operation(summary = "Listar ativos não arquivados")
    public ResponseEntity<ApiResponse<List<AtivoResponse>>> listar() {
        return ResponseEntity.ok(ApiResponse.ok(listarAtivos.execute()));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'VIEWER')")
    @Operation(summary = "Buscar ativo por ID")
    public ResponseEntity<ApiResponse<AtivoResponse>> buscarPorId(@PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.ok(buscarAtivo.execute(id)));
    }
}
