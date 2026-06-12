package com.controleFinanceiro.infrastructure.adapter.in.rest.query;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.controleFinanceiro.application.dto.response.AlocacaoItemResponse;
import com.controleFinanceiro.application.dto.response.ApiResponse;
import com.controleFinanceiro.application.dto.response.ProjecaoResponse;
import com.controleFinanceiro.application.dto.response.RendimentoResponse;
import com.controleFinanceiro.domain.port.in.query.BuscarAlocacaoSnapshotPort;
import com.controleFinanceiro.domain.port.in.query.BuscarProjecaoPortfolioPort;
import com.controleFinanceiro.domain.port.in.query.BuscarRendimentoSnapshotPort;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import lombok.RequiredArgsConstructor;

@Tag(name = "Cálculos", description = "Rendimentos, alocação e projeções do portfólio")
@RestController
@RequestMapping("/api/snapshots")
@RequiredArgsConstructor
public class CalculosQueryController {

    private final BuscarRendimentoSnapshotPort buscarRendimento;
    private final BuscarAlocacaoSnapshotPort buscarAlocacao;
    private final BuscarProjecaoPortfolioPort buscarProjecao;

    @Operation(summary = "Rendimento do snapshot", description = "Rendimento absoluto e percentual em relação ao snapshot anterior. mode=simple|cagr")
    @GetMapping("/{id}/returns")
    @PreAuthorize("hasAnyRole('ADMIN', 'VIEWER')")
    public ResponseEntity<ApiResponse<RendimentoResponse>> rendimento(
            @PathVariable UUID id,
            @RequestParam(defaultValue = "simple") String mode) {
        return ResponseEntity.ok(ApiResponse.ok(buscarRendimento.execute(id, mode)));
    }

    @Operation(summary = "Alocação por tipo de ativo", description = "Concentração percentual do portfólio por categoria")
    @GetMapping("/{id}/allocation")
    @PreAuthorize("hasAnyRole('ADMIN', 'VIEWER')")
    public ResponseEntity<ApiResponse<List<AlocacaoItemResponse>>> alocacao(@PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.ok(buscarAlocacao.execute(id)));
    }

    @Operation(summary = "Projeção do portfólio", description = "Crescimento composto do último snapshot com taxa mensal (%) e prazo em meses")
    @GetMapping("/latest/projection")
    @PreAuthorize("hasAnyRole('ADMIN', 'VIEWER')")
    public ResponseEntity<ApiResponse<ProjecaoResponse>> projecao(
            @RequestParam BigDecimal rate,
            @RequestParam int months) {
        return ResponseEntity.ok(ApiResponse.ok(buscarProjecao.execute(rate, months)));
    }
}
