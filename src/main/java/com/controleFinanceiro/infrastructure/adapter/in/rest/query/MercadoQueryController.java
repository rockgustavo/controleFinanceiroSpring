package com.controleFinanceiro.infrastructure.adapter.in.rest.query;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.controleFinanceiro.application.dto.response.ApiResponse;
import com.controleFinanceiro.application.dto.response.IndicadoresMercadoResponse;
import com.controleFinanceiro.application.dto.response.TaxaTesourosResponse;
import com.controleFinanceiro.domain.port.in.query.BuscarIndicadoresMercadoPort;
import com.controleFinanceiro.domain.port.in.query.BuscarTaxasTesourosPort;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "Mercado", description = "Indicadores de mercado ao vivo via BRAPI")
@RestController
@RequestMapping("/api/market")
@RequiredArgsConstructor
public class MercadoQueryController {

    private final BuscarIndicadoresMercadoPort buscarIndicadores;
    private final BuscarTaxasTesourosPort buscarTaxasTesouro;

    @Operation(summary = "Indicadores ao vivo", description = "Selic, USD/BRL, Ibovespa, IVVB11, IPCA via BRAPI")
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'VIEWER')")
    public ResponseEntity<ApiResponse<IndicadoresMercadoResponse>> indicadores() {
        return ResponseEntity.ok(ApiResponse.ok(buscarIndicadores.execute()));
    }

    @Operation(summary = "Taxas de referência", description = "Selic e CDI via BRAPI prime-rate")
    @GetMapping("/tesouro")
    @PreAuthorize("hasAnyRole('ADMIN', 'VIEWER')")
    public ResponseEntity<ApiResponse<List<TaxaTesourosResponse>>> tesouro() {
        return ResponseEntity.ok(ApiResponse.ok(buscarTaxasTesouro.execute()));
    }
}
